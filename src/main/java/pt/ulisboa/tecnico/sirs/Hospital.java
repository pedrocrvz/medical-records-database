package pt.ulisboa.tecnico.sirs;

import pt.ulisboa.tecnico.sirs.authorization.FetchAuthorization;
import pt.ulisboa.tecnico.sirs.authorization.PutAuthorization;
import pt.ulisboa.tecnico.sirs.exception.NotAuthorizedException;
import pt.ulisboa.tecnico.sirs.exception.SecurityLibraryException;
import pt.ulisboa.tecnico.sirs.security.DigitalSignature;

import java.io.IOException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.util.List;

/**
 * Hospital class extending abstract Entity
 */
public class Hospital extends Entity {

    public Hospital(KeyStore ks, String keyAlias, String ksPassword)
            throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException {
        super(ks, keyAlias, ksPassword);
    }

    /**
     * This method returns a signed authorization that the NHS will accept
     * @param record Record that will be sent along with this authorization
     * @return Returns an authorization
     * @throws SecurityLibraryException Thrown when digital signatures library runs into problem
     */
    public PutAuthorization authorizePutRecord(SecureRecord record) throws SecurityLibraryException {
        return new PutAuthorization(
                record,
                this.getCertificate(),
                record.getDoctorCertificate(),
                record.getPatientCertificate(),
                signBytes(record.getBytes())
        );
    }

    /**
     * This method returns a signed authorization that the NHS will accept
     * @param doctor Doctor responsible for patient
     * @param patient Patient that is being evaluated
     * @return Returns an authorization
     * @throws SecurityLibraryException Thrown when digital signatures library runs into problem
     */
    public FetchAuthorization authorizeFetchRecord(Doctor doctor, Patient patient) throws SecurityLibraryException {
        try {
            return new FetchAuthorization(
                    this.getCertificate(),
                    doctor.getCertificate(),
                    patient.getCertificate(),
                    signBytes(concat(doctor.getCertificate().getEncoded(), patient.getCertificate().getEncoded())));
        } catch (CertificateEncodingException e) {
            throw new SecurityLibraryException(e);
        }
    }

    public static void main(String[] args){
        try {
            System.setProperty("javax.net.ssl.keyStore", "keys/Doctor-1_Hospital-1.jks");
            System.setProperty("javax.net.ssl.keyStorePassword", "password123");
            System.setProperty("javax.net.ssl.trustStore", "keys/truststore.jks");
            System.setProperty("javax.net.ssl.trustStorePassword", "password123");
            System.setProperty("javax.net.debug", "ssl");

            Hospital hospital = new Hospital(loadKeyStore("keys/Hospital-1.jks", "password123"), "Hospital-1", "password123");
            Doctor doctor = new Doctor(loadKeyStore("keys/Doctor-1_Hospital-1.jks", "password123"), "Doctor-1_Hospital-1", "password123", hospital);
            Patient patient = new Patient(loadKeyStore("keys/Patient-1.jks", "password123"), "Patient-1", "password123");

            doctor.writeRecord(patient, "O paciente mostra indícos de ALS");
            doctor.writeRecord(patient, "O paciente xyz");
            doctor.writeRecord(patient, "O paciente cenas");

            List<Record> records = doctor.getRecords(patient);
            for (Record r : records) {
                System.out.println(((SecureRecord) r).hasValidSignature() + " " + r);
            }
        } catch( NotAuthorizedException e){
            System.out.println("Not authorized to perform action");
            e.printStackTrace();
        } catch (NotBoundException e) {
            System.out.println("RMI Registry error: " + e.getMessage() + " was not found");
            e.printStackTrace();
            System.exit(1);
        } catch (SecurityLibraryException e) {
            System.out.println("Server ran into problems :\\");
            e.printStackTrace();
            System.exit(1);
        } catch (ConnectException e) {
            System.out.println("Connection error: could not connect to RMI registry");
            e.printStackTrace();
            System.exit(1);
        } catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException |
                CertificateException | IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
