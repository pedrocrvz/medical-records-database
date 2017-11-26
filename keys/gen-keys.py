#!/usr/bin/env python3

import argparse
import sys
import subprocess
import multiprocessing as mp

password = "password123"
keysize = "2048"
pool = mp.Pool(processes=mp.cpu_count())

def perror(message):
	sys.stderr.write(message+"\n")
	exit(-1)

def exec_command(command):
	try:
		subprocess.check_output(command, shell=True)
	except subprocess.CalledProcessError as e:
		perror(e.output.decode('utf8'))

def remove_keys():
	print("Clearing keys")
	exec_command("rm -f *.crt *.csr *.jks")

def create_nhs():
	print("Generating NHS")
	exec_command('keytool -genkeypair -alias NHS -keystore NHS.jks -storepass '+password+' -validity 400 -keysize '+keysize+' -sigalg SHA256withRSA -keyalg RSA -dname CN=NHS -noprompt -keypass '+password+' -ext bc:c=ca:true  -ext eku=sA -storetype pkcs12')
	exec_command('keytool -keystore NHS.jks -alias NHS -exportcert -rfc -storepass '+password+' > NHS.crt')

def create_hospital(id, num_doctors):
	entity = "Hospital-"+str(id)
	print("Generating " + entity)
	exec_command('keytool -genkeypair -alias '+entity+' -keystore '+entity+'.jks -storepass '+password+' -validity 300 -keysize '+keysize+' -sigalg SHA256withRSA -keyalg RSA -dname CN='+entity+' -noprompt -keypass '+password+' -ext bc:c=ca:true -ext eku=sA -storetype pkcs12')
	exec_command('keytool -keystore '+entity+'.jks -storepass '+password+' -alias '+entity+' -certreq -file '+entity+'.csr')
	exec_command('keytool -keystore NHS.jks -storepass '+password+' -alias NHS -gencert -rfc -infile '+entity+'.csr -outfile '+entity+'.crt -validity 300 -ext bc=ca:true -ext eku=sA')
	exec_command('keytool -keystore '+entity+'.jks -storepass '+password+' -keypass '+password+' -importcert -alias NHS -file NHS.crt -noprompt')
	exec_command('keytool -keystore '+entity+'.jks -storepass '+password+' -keypass '+password+' -importcert -alias '+entity+' -file '+entity+'.crt')
	for i in range(1, num_doctors+1):
		mp.Process(target=create_doctor, args=(i, id)).start()
		#create_doctor(i, id)

def create_doctor(id, num_hospital):
	hospital = "Hospital-"+str(num_hospital)
	entity = "Doctor-"+str(id)+"_"+hospital
	print("Generating " + entity)
	exec_command('keytool -genkeypair -alias '+entity+' -keystore '+entity+'.jks -storepass '+password+' -validity 200 -keysize '+keysize+' -sigalg SHA256withRSA -keyalg RSA -dname CN='+entity+' -noprompt -keypass '+password+' -ext bc:c=ca:false -ext eku=sA -ext eku=cA -storetype pkcs12')
	exec_command('keytool -keystore '+entity+'.jks -storepass '+password+' -alias '+entity+' -certreq -file '+entity+'.csr')
	exec_command('keytool -keystore '+hospital+'.jks -storepass '+password+' -alias '+hospital+' -gencert -rfc -infile '+entity+'.csr -outfile '+entity+'.crt -validity 200 -ext bc=ca:false -ext eku=sA')
	exec_command('keytool -keystore '+entity+'.jks -storepass '+password+' -keypass '+password+' -importcert -alias '+hospital+' -file '+hospital+'.crt -noprompt')
	exec_command('keytool -keystore '+entity+'.jks -storepass '+password+' -keypass '+password+' -importcert -alias '+entity+' -file '+entity+'.crt')

def create_patient(id):
	entity = "Patient-"+str(id)
	print("Generating " + entity)
	exec_command('keytool -genkeypair -alias '+entity+' -keystore '+entity+'.jks -storepass '+password+' -validity 200 -keysize '+keysize+' -sigalg SHA256withRSA -keyalg RSA -dname CN='+entity+' -noprompt -keypass '+password+' -ext bc:c=ca:false -ext eku=cA -storetype pkcs12')
	exec_command('keytool -keystore '+entity+'.jks -storepass '+password+' -alias '+entity+' -certreq -file '+entity+'.csr')
	exec_command('keytool -keystore NHS.jks -storepass '+password+' -alias NHS -gencert -rfc -infile '+entity+'.csr -outfile '+entity+'.crt -validity 200 -ext bc=ca:false -ext eku=cA')
	exec_command('keytool -keystore '+entity+'.jks -storepass '+password+' -keypass '+password+' -importcert -alias NHS -file NHS.crt -noprompt')
	exec_command('keytool -keystore '+entity+'.jks -storepass '+password+' -keypass '+password+' -importcert -alias '+entity+' -file '+entity+'.crt')


parser = argparse.ArgumentParser(description='Generate Key Structure.')
parser.add_argument('--h', action="store", dest="NUM_HOSPITALS", help="Number of hospitals", type=int)
parser.add_argument('--d', action="store", dest="NUM_DOCTORS", help="Number of doctors per hospital", type=int)
parser.add_argument('--p', action="store", dest="NUM_PATIENTS", help="Number of patients", type=int)
parser.add_argument('--clear', action='store_true', dest="CLEAR", help='Clears keys')

arguments = parser.parse_args()
h = arguments.NUM_HOSPITALS
d = arguments.NUM_DOCTORS
p = arguments.NUM_PATIENTS
clear = arguments.CLEAR

if(clear):
	remove_keys()
	exit(0)

if(h is None or d is None or p is None or not(h >= 1 and d >= 1 and p >= 1)):
	parser.error("--h, --d, --p must be valid values")

create_nhs()

for i in range(1,h+1):
	mp.Process(target=create_hospital, args=(i,d)).start()
	#create_hospital(i, d)

for i in range(1,p+1):
	mp.Process(target=create_patient, args=(i,)).start()
	#create_patient(i)


