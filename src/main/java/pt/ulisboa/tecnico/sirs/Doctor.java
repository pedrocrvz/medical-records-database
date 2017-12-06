package pt.ulisboa.tecnico.sirs;

import pt.ulisboa.tecnico.sirs.exception.NotAuthorizedException;
import pt.ulisboa.tecnico.sirs.exception.SecurityLibraryException;
import pt.ulisboa.tecnico.sirs.rmi.TLSClientSocketFactory;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.*;
import java.security.cert.Certificate;
import java.util.List;

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
     * @throws RemoteException
     * @throws NotBoundException
     * @throws NotAuthorizedException
     * @throws SecurityLibraryException
     */
    public void writeRecord(final Patient patient, final String record)
            throws NotAuthorizedException, SecurityLibraryException, RemoteException, NotBoundException {
        final SecureRecord sr = new SecureRecord(this.getCertificate(), patient.getCertificate(), record, this);

        getStub().putRecord(hospital.authorizePutRecord(sr), sr);
        System.out.println("Record sent to NHS");
    }

    public List<Record> getRecords(final Patient patient) throws RemoteException, NotBoundException,
            NotAuthorizedException, SecurityLibraryException {
        return getStub().getRecords(hospital.authorizeFetchRecord(this, patient), patient.getPublicKey());
    }

    @Override
    public String toString() {
        return "Doctor{"+toBase64(getPublicKey().getEncoded())+"}";
    }

    public static NHSInterface getStub() throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry();
        return (NHSInterface) registry.lookup("NHS");
    }
}
