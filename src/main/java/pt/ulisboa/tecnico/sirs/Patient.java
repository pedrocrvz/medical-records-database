package pt.ulisboa.tecnico.sirs;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

/**
 * Patient class extending abstract Entity
 */
public class Patient extends Entity {
    public Patient(KeyStore ks, String keyAlias, String ksPassword)
            throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException {
        super(ks, keyAlias, ksPassword);
    }

    @Override
    public String toString() {
        return "Patient{"+toBase64(getPublicKey().getEncoded())+"}";
    }
}
