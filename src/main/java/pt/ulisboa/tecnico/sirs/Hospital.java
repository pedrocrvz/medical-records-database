package pt.ulisboa.tecnico.sirs;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

public class Hospital extends Entity {

    public Hospital(KeyStore ks, String keyAlias, String ksPassword)
            throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException {
        super(ks, keyAlias, ksPassword);
    }

    public static void main(String[] args){
        try {
            Hospital hospital = new Hospital(loadKeyStore(args[0], args[2]), args[1], args[2]);
        } catch (UnrecoverableKeyException | NoSuchAlgorithmException | CertificateException | IOException |
                KeyStoreException e) {
            e.printStackTrace();
        }
    }
}
