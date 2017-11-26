package pt.ulisboa.tecnico.sirs;

import pt.ulisboa.tecnico.sirs.exception.SecurityLibraryException;
import pt.ulisboa.tecnico.sirs.security.DigitalSignature;
import sun.security.x509.X500Name;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;

public abstract class Entity {
    private final PrivateKey privateKey;
    private final java.security.cert.Certificate certificate;

    public Entity(final KeyStore ks, final String keyAlias, final String ksPassword)
            throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException {
        privateKey = (PrivateKey) ks.getKey(keyAlias, ksPassword.toCharArray());
        certificate = ks.getCertificate(keyAlias);
    }

    /**
     * This method returns this entity's PublicKey
     * @return Returns PublicKey
     */
    final public PublicKey getPublicKey(){
        return certificate.getPublicKey();
    }

    final public String getBase64PublicKey(){
        return getBase64PublicKey(this.getPublicKey());
    }

    final byte[] signBytes(byte[] bytesToSign)
            throws SecurityLibraryException {
        return DigitalSignature.makeDigitalSignature(bytesToSign, privateKey);
    }

    final public String getEntityName(){
        if (certificate instanceof X509Certificate) {
            X509Certificate x509cert = (X509Certificate) certificate;
            try {
                return X500Name.asX500Name(x509cert.getSubjectX500Principal()).getCommonName();
            } catch (IOException e) {
                return null;
            }
        }
        return null;
    }

    final public Certificate getCertificate(){
        return certificate;
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
}
