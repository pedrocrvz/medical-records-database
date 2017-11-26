package pt.ulisboa.tecnico.sirs;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.List;

public class NHS extends Entity implements NHSInterface {
    private DataStore ds = new SecureDataStore();

    public NHS(KeyStore ks, String keyAlias, String ksPassword)
            throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, RemoteException,
            MalformedURLException {
        super(ks, keyAlias, ksPassword);
    }

    public static NHSInterface getStub() throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry();
        return (NHSInterface) registry.lookup("NHS");
    }

    @Override
    public List<Record> getRecords(PublicKey patient) throws RemoteException {
        System.out.println("[NHS] Fetching records from " + patient);
        return ds.getRecords(patient);
    }

    @Override
    public void putRecord(Record record) throws RemoteException {
        System.out.println("[NHS] Inserting record " + record);
        ds.putRecord(record.getPatientPublicKey(), record);
    }

    public static void main(String[] args){
        try{
            NHS nhs = new NHS(loadKeyStore(args[0], args[2]), args[1], args[2]);
            NHSInterface stub = (NHSInterface) UnicastRemoteObject.exportObject(nhs, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
            System.out.println("Binding NHS");
            registry.bind("NHS", stub);


            final Thread mainThread = Thread.currentThread();
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    try {
                        System.out.println("Unbinding NHS");
                        //UnicastRemoteObject.unexportObject(nhs, false);
                        registry.unbind("NHS");
                    } catch (RemoteException | NotBoundException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (NoSuchAlgorithmException | UnrecoverableKeyException | KeyStoreException | CertificateException |
                IOException | AlreadyBoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
