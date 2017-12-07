package pt.ulisboa.tecnico.sirs;

import pt.ulisboa.tecnico.sirs.exception.NotAuthorizedException;
import pt.ulisboa.tecnico.sirs.exception.SecurityLibraryException;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.*;
import java.util.List;

/**
 * Doctor class extending abstract Entity
 */
public class Doctor extends Entity {
    private final Hospital hospital;

    public Doctor(KeyStore ks, String keyAlias, String ksPassword, Hospital hospital)
            throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException {
        super(ks, keyAlias, ksPassword);
        this.hospital = hospital;
    }

    /**
     * Doctor writes a Patient record, and then sends it to NHS
     * @param patient Patient
     * @param record Record - whatever the doctor wants to write
     * @throws RemoteException Thrown when RMI or NHS is inaccessible
     * @throws NotBoundException Thrown when NHS is inaccessible
     * @throws NotAuthorizedException Thrown when authorization was rejected by NHS
     * @throws SecurityLibraryException Thrown when NHS runs into problems with digital signature library
     */
    public void writeRecord(final Patient patient, final String record)
            throws NotAuthorizedException, SecurityLibraryException, RemoteException, NotBoundException {
        final SecureRecord sr = new SecureRecord(this.getCertificate(), patient.getCertificate(), record, this);

        getStub().putRecord(hospital.authorizePutRecord(sr), sr);
        System.out.println("Record sent to NHS");
    }

    /**
     * Doctor fetches patient records from the NHS
     * @param patient Patient
     * @return Returns a list with previous patient records
     * @throws RemoteException Thrown when RMI or NHS is inaccessible
     * @throws NotBoundException Thrown when NHS is inaccessible
     * @throws NotAuthorizedException Thrown when authorization was rejected by NHS
     * @throws SecurityLibraryException Thrown when NHS runs into problems with digital signature library
     */
    public List<Record> getRecords(final Patient patient) throws RemoteException, NotBoundException,
            NotAuthorizedException, SecurityLibraryException {
        return getStub().getRecords(hospital.authorizeFetchRecord(this, patient), patient.getPublicKey());
    }

    @Override
    public String toString() {
        return "Doctor{"+toBase64(getPublicKey().getEncoded())+"}";
    }

    /**
     * Seaches for the NHS stub in RMI registry
     * @return NHS stub
     * @throws RemoteException Thrown when RMI is inaccessible
     * @throws NotBoundException Thrown when NHS is not found in RMI
     */
    public static NHSInterface getStub() throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry();
        return (NHSInterface) registry.lookup("NHS");
    }
}
