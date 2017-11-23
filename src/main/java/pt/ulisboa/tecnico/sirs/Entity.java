package pt.ulisboa.tecnico.sirs;

import java.security.*;
import java.security.cert.Certificate;
import java.util.Base64;
import java.util.Base64.Encoder;

public abstract class Entity {
    private final KeyPair assymetricKeys;

    public Entity(final KeyStore ks, final String keyAlias, final String ksPassword)
            throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException {
        assymetricKeys = getKeyPairFromKeyStore(ks, keyAlias, ksPassword);
    }

    /**
     * This method loads a KeyPair from a KeyStore
     * @param ks Keystore
     * @param keyAlias Alias for key
     * @param ksPassword Password for keystore
     * @return
     * @throws KeyStoreException
     * @throws UnrecoverableKeyException
     * @throws NoSuchAlgorithmException
     */
    private KeyPair getKeyPairFromKeyStore(final KeyStore ks, final String keyAlias, final String ksPassword)
            throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException {
        final Key key = ks.getKey(keyAlias, ksPassword.toCharArray());
        final Certificate cert = ks.getCertificate(keyAlias);
        final PublicKey publicKey = cert.getPublicKey();

        return new KeyPair(publicKey, (PrivateKey) key);
    }

    /**
     * This method returns this entity's PublicKey
     * @return Returns PublicKey
     */
    public PublicKey getPublicKey(){
        return assymetricKeys.getPublic();
    }

    public String getBase64PublicKey(){
        return getBase64PublicKey(this.getPublicKey());
    }

    /**
     * This method returns this entity's PublicKey encoded in Base64
     * @return Returns PublicKey encoded in Base64
     */
    public static String getBase64PublicKey(PublicKey publicKey){
        byte[] publicKeyBytes = publicKey.getEncoded();
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(publicKeyBytes);
    }
}
