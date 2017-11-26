package pt.ulisboa.tecnico.sirs;

import pt.ulisboa.tecnico.sirs.exception.NotAuthorizedException;
import pt.ulisboa.tecnico.sirs.exception.SecurityLibraryException;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
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
        final SecureRecord sr = new SecureRecord(this.getPublicKey(), patient.getPublicKey(), record, this);

        getStub().putRecord(sr);
        System.out.println("Record sent to NHS");
    }

    public List<Record> getRecords(final Patient patient) throws RemoteException, NotBoundException,
            NotAuthorizedException, SecurityLibraryException {
        return getStub().getRecords(patient.getPublicKey());
    }

    public Certificate getHospitalCertificate() {
        return hospital.getCertificate();
    }

    @Override
    public String toString() {
        return "Doctor{"+toBase64(getPublicKey().getEncoded())+"}";
    }

    public static NHSInterface getStub() throws RemoteException, NotBoundException {
        //SslRMIClientSocketFactory clientSocketFactory = new SslRMIClientSocketFactory();
        //Registry registry = LocateRegistry.getRegistry("localhost", 9000, clientSocketFactory);
        Registry registry = LocateRegistry.getRegistry();
        return (NHSInterface) registry.lookup("NHS");
    }
}
