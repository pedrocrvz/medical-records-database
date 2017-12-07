package pt.ulisboa.tecnico.sirs;

import pt.ulisboa.tecnico.sirs.authorization.FetchAuthorization;
import pt.ulisboa.tecnico.sirs.authorization.PutAuthorization;
import pt.ulisboa.tecnico.sirs.data.DataStore;
import pt.ulisboa.tecnico.sirs.data.SecureDataStore;
import pt.ulisboa.tecnico.sirs.exception.NotAuthorizedException;
import pt.ulisboa.tecnico.sirs.exception.SecurityLibraryException;
import pt.ulisboa.tecnico.sirs.rmi.TLSClientSocketFactory;
import pt.ulisboa.tecnico.sirs.rmi.TLSServerSocketFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.List;

/**
 * NHS class extending abstract Entity
 */
public class NHS extends Entity implements NHSInterface {
    private DataStore ds;

    public NHS(KeyStore ks, String keyAlias, String ksPassword)
            throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, RemoteException,
            MalformedURLException {
        super(ks, keyAlias, ksPassword);
        ds = new SecureDataStore(ks.getKey("datastore", ksPassword.toCharArray()));
    }


    /**
     * This method receives an authorization issued by a trusted hospital and a patient public key. If the authorization is valid, the NHS returns all patient records with that public key, otherwise NotAuthorizedException is thrown
     * @param authorization Authorization issued by a trusted hospital
     * @param patient Public key correspondent to the patient
     * @return Returns a list with patient records
     * @throws RemoteException Is thrown when RMI is shutdown/network inaccessible
     * @throws NotAuthorizedException Is thrown when authorization is invalid
     * @throws SecurityLibraryException Is thrown when digital signature validation runs into problem
     */
    @Override
    public List<Record> getRecords(FetchAuthorization authorization, PublicKey patient)
            throws RemoteException, SecurityLibraryException, NotAuthorizedException {
        if(authorization == null || !authorization.isValid(getCertificate())) throw new NotAuthorizedException();

        System.out.println("[NHS] Fetching records from " + toBase64(patient.getEncoded()));
        return ds.getRecords(patient);
    }

    /**
     * This method receives an authorization issued by a trusted hospital and a record. If the authorization is valid, the record is inserted in the datastore, otherwise NotAuthorizedException is thrown
     * @param authorization Authorization issued by a trusted hospital
     * @param record Record being inserted
     * @throws RemoteException Is thrown when RMI is shutdown/network inaccessible
     * @throws NotAuthorizedException Is thrown when authorization is invalid
     * @throws SecurityLibraryException Is thrown when digital signature validation runs into problem
     */
    @Override
    public void putRecord(PutAuthorization authorization, Record record)
            throws RemoteException, SecurityLibraryException, NotAuthorizedException {
        if(authorization == null || !authorization.isValid(getCertificate())) throw new NotAuthorizedException();
        if(!authorization.getDoctorCertificate().getPublicKey().equals(record.getDoctorPublicKey())) throw new NotAuthorizedException();

        System.out.println("[NHS] Inserting record " + record);
        ds.putRecord(record.getPatientPublicKey(), record);
    }

    public static void main(String[] args){
        try {
            String entity = "NHS";
            String keystore = "keys/"+entity+".jks";
            String password = "password123";

            System.setProperty("javax.net.ssl.keyStore", keystore);
            System.setProperty("javax.net.ssl.keyStorePassword", password);
            System.setProperty("javax.net.ssl.trustStore", "keys/truststore.jks");
            System.setProperty("javax.net.ssl.trustStorePassword", password);
            System.setProperty("javax.net.debug", "ssl");

            NHS nhs = new NHS(loadKeyStore(keystore, password), entity, password);

            TLSClientSocketFactory clientSocketFactory = new TLSClientSocketFactory();
            TLSServerSocketFactory serverSocketFactory = new TLSServerSocketFactory();
            NHSInterface stub = (NHSInterface) UnicastRemoteObject.exportObject(nhs, 0, clientSocketFactory, serverSocketFactory);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
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
            e.printStackTrace();
            System.exit(1);
        } catch (ConnectException e){
            System.out.println("Connection error: could not connect to RMI registry");
            e.printStackTrace();
            System.exit(1);
        } catch (AlreadyBoundException e){
            System.out.println("RMI Registry error: " + e.getMessage() + " is already assigned");
            e.printStackTrace();
            System.exit(1);
        } catch (NoSuchAlgorithmException | UnrecoverableKeyException | KeyStoreException | CertificateException |
                IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
