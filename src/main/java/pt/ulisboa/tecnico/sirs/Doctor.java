package pt.ulisboa.tecnico.sirs;

import pt.ulisboa.tecnico.sirs.exception.NotAuthorizedException;
import pt.ulisboa.tecnico.sirs.exception.ServerCipherException;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.*;
import java.util.List;

public class Doctor extends Entity {
    public Doctor(KeyStore ks, String keyAlias, String ksPassword)
            throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException {
        super(ks, keyAlias, ksPassword);
    }

    public void writeRecord(final Patient patient, final String record)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, RemoteException,
            NotBoundException, NotAuthorizedException, ServerCipherException {
        final SecureRecord sr = new SecureRecord(this.getPublicKey(), patient.getPublicKey(), record, this);

        getStub().putRecord(sr);
        System.out.println("Record sent to NHS");
    }

    public List<Record> getRecords(final Patient patient) throws RemoteException, NotBoundException,
            NotAuthorizedException, ServerCipherException {
        return getStub().getRecords(patient.getPublicKey());
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
