package pt.ulisboa.tecnico.sirs;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

public class Patient extends Entity {
    private Hospital hospital;

    public Patient(KeyStore ks, String keyAlias, String ksPassword)
            throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException {
        super(ks, keyAlias, ksPassword);
    }
}
