package pt.ulisboa.tecnico.sirs;

import java.security.*;

public class Doctor extends Entity {

    public Doctor(KeyStore ks, String keyAlias, String ksPassword)
            throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException {
        super(ks, keyAlias, ksPassword);
    }


}
