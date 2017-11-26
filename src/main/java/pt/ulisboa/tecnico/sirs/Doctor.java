package pt.ulisboa.tecnico.sirs;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.List;

public class Doctor extends Entity {
    public Doctor(KeyStore ks, String keyAlias, String ksPassword)
            throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException {
        super(ks, keyAlias, ksPassword);
    }

    public void writeRecord(final Patient patient, final String record)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, RemoteException,
            NotBoundException {
        SecureRecord sr = new SecureRecord(this.getPublicKey(), patient.getPublicKey(), record);
        sr.setSignature(signBytes(sr.getBytes()));

        NHS.getStub().putRecord(sr);
        System.out.println("Record sent to NHS");
    }

    public List<Record> getRecords(final Patient patient) throws RemoteException, NotBoundException {
        return NHS.getStub().getRecords(patient.getPublicKey());
    }

    @Override
    public String toString() {
        return "Doctor{"+toBase64(getPublicKey().getEncoded())+"}";
    }

    public static void main(String[] args){
        try {
            Doctor doctor = new Doctor(loadKeyStore(args[0], args[2]), args[1], args[2]);
            Patient patient = new Patient(loadKeyStore("keys/Patient-1.jks", "password123"), "Patient-1", "password123");

            doctor.writeRecord(patient, "O paciente mostra indícos de ALS");
            doctor.writeRecord(patient, "O paciente xyz");
            doctor.writeRecord(patient, "O paciente cenas");

            List<Record> records = doctor.getRecords(patient);
            for(Record r: records){
                System.out.println(((SecureRecord)r).hasValidSignature() + " " + r);
            }
        } catch (UnrecoverableKeyException | NoSuchAlgorithmException | IOException | KeyStoreException |
                CertificateException | InvalidKeyException | NotBoundException | SignatureException e) {
            e.printStackTrace();
        }
    }
}
