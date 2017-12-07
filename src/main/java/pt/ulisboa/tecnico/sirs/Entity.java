package pt.ulisboa.tecnico.sirs;

import pt.ulisboa.tecnico.sirs.exception.SecurityLibraryException;
import pt.ulisboa.tecnico.sirs.security.DigitalSignature;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Base64;

/**
 * Class that defines an Entity in the project
 */
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
    public PublicKey getPublicKey(){
        return certificate.getPublicKey();
    }

    public String getBase64PublicKey(){
        return getBase64PublicKey(this.getPublicKey());
    }

    byte[] signBytes(byte[] bytesToSign)
            throws SecurityLibraryException {
        return DigitalSignature.makeDigitalSignature(bytesToSign, privateKey);
    }

    public Certificate getCertificate(){
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

    /**
     * Loads a KeyStore from the file system
     * @param path Path to the keystore
     * @param password Password to open keystore
     * @return Returns the keystore
     * @throws IOException
     * @throws KeyStoreException
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     */
    public static KeyStore loadKeyStore(String path, String password)
            throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException {
        FileInputStream is = new FileInputStream(path);

        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(is, password.toCharArray());
        return keystore;
    }

    /**
     * This method concatenates multiple byte array into a single one
     * @param arrays the list of byte array
     * @return A single concatenated byte array
     */
    public static byte[] concat(byte[]... arrays){
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        for (byte[] b : arrays) {
            os.write(b, 0, b.length);
        }
        return os.toByteArray();
    }

    /**
     * This method transforms an object into a byte array
     * @param obj Object
     * @return A byte array containing the object
     * @throws IOException
     */
    public static byte[] toByteArray(Object obj) throws IOException {
        try(ByteArrayOutputStream b = new ByteArrayOutputStream()){
            try(ObjectOutputStream o = new ObjectOutputStream(b)){
                o.writeObject(obj);
            }
            return b.toByteArray();
        }
    }
}
