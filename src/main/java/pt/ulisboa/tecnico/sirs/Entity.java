package pt.ulisboa.tecnico.sirs;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Base64;

public abstract class Entity {
    private final KeyPair keys;

    public Entity(final KeyStore ks, final String keyAlias, final String ksPassword)
            throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException {
        keys = getKeyPairFromKeyStore(ks, keyAlias, ksPassword);
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
    public KeyPair getKeyPairFromKeyStore(final KeyStore ks, final String keyAlias, final String ksPassword)
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
        return keys.getPublic();
    }

    public String getBase64PublicKey(){
        return getBase64PublicKey(this.getPublicKey());
    }

    public static String toBase64(byte[] bytes){
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(bytes);
    }

    /**
     * This method returns this entity's PublicKey encoded in Base64
     * @return Returns PublicKey encoded in Base64
     */
    public static String getBase64PublicKey(PublicKey publicKey){
        byte[] publicKeyBytes = publicKey.getEncoded();
        return toBase64(publicKeyBytes);
    }

    public static KeyStore loadKeyStore(String path, String password)
            throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException {
        FileInputStream is = new FileInputStream(path);

        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(is, password.toCharArray());
        return keystore;
    }

    public byte[] signBytes(byte[] bytesToSign)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        return Security.makeDigitalSignature(bytesToSign, keys.getPrivate());
    }
}
