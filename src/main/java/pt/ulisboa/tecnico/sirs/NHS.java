package pt.ulisboa.tecnico.sirs;

import pt.ulisboa.tecnico.sirs.data.DataStore;
import pt.ulisboa.tecnico.sirs.data.SecureDataStore;
import pt.ulisboa.tecnico.sirs.exception.ServerCipherException;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;
import java.io.IOException;
import java.net.BindException;
import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.List;

public class NHS extends Entity implements NHSInterface {
    private DataStore ds;

    public NHS(KeyStore ks, String keyAlias, String ksPassword)
            throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, RemoteException,
            MalformedURLException {
        super(ks, keyAlias, ksPassword);
        ds = new SecureDataStore(ks.getKey("datastore", ksPassword.toCharArray()));
    }

    @Override
    public List<Record> getRecords(PublicKey patient) throws RemoteException, ServerCipherException {
        System.out.println("[NHS] Fetching records from " + toBase64(patient.getEncoded()));
        return ds.getRecords(patient);
    }

    @Override
    public void putRecord(Record record) throws RemoteException, ServerCipherException {
        System.out.println("[NHS] Inserting record " + record);
        ds.putRecord(record.getPatientPublicKey(), record);
    }

    public static void main(String[] args){
        try {
            NHS nhs = new NHS(loadKeyStore(args[0], args[2]), args[1], args[2]);
            NHSInterface stub = (NHSInterface) UnicastRemoteObject.exportObject(nhs, 0);


            //SslRMIClientSocketFactory clientSocketFactory = new SslRMIClientSocketFactory();
            //SslRMIServerSocketFactory serverSocketFactory = new SslRMIServerSocketFactory();

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
            //Registry registry = LocateRegistry.createRegistry(9000, clientSocketFactory, serverSocketFactory);
            System.out.println("Binding NHS");
            registry.bind("NHS", stub);

            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    try {
                        System.out.println("Unbinding NHS");
                        registry.unbind("NHS");
                    } catch (RemoteException | NotBoundException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (ExportException e){
            System.out.println("Starting error: port is in use, is NHS already up?");
            System.exit(1);
        } catch (ConnectException e){
            System.out.println("Connection error: could not connect to RMI registry");
            System.exit(1);
        } catch (AlreadyBoundException e){
            System.out.println("RMI Registry error: " + e.getMessage() + " is already assigned");
            System.exit(1);
        } catch (NoSuchAlgorithmException | UnrecoverableKeyException | KeyStoreException | CertificateException |
                IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
