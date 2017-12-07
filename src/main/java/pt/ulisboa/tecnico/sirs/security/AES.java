package pt.ulisboa.tecnico.sirs.security;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.security.*;

/**
 * Defines methods to encrypt and decrypt data using Java AES implementation
 * Uses AES/CBC/PKCS5Padding AES cipher, and SHA1PRNG to generate secure random IVs
 */
public class AES {
    public static String PRNG_ALGORITHM = "SHA1PRNG";
    public static String SYMMETRIC_ALGORITHM = "AES/CBC/PKCS5Padding";

    /**
     * Returns an array with byteNumber bytes of secure random bytes.
     *
     * @param byteNumber - number of bytes of the returning byte[]
     * @return randomBytes - byte[] with random suff
     * @throws NoSuchAlgorithmException
     */
    public static byte[] generateIV(int byteNumber)
            throws NoSuchAlgorithmException {
        byte[] randomBytes = new byte[byteNumber];
        SecureRandom secureRandom = SecureRandom.getInstance(PRNG_ALGORITHM);
        secureRandom.nextBytes(randomBytes);
        return randomBytes;
    }

    /**
     * It decrypts using AES-CBC bytesToEncrypt using aesKey.
     * It will return the ciphered or the plain text
     * If aesKey has not the proper Padding (PKCS5Padding) an exception will be thrown.
     * @param object
     * @param aesKey
     * @param iv
     * @return byte[]
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static Serializable decrypt(SealedObject object, Key aesKey, byte[] iv)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException, ClassNotFoundException {
        Cipher cipher = Cipher.getInstance(SYMMETRIC_ALGORITHM);
        IvParameterSpec ips = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, aesKey, ips);

        return (Serializable) object.getObject(cipher);
    }

    /**
     * It encrypts using AES-CBC bytesToEncrypt using aesKey.
     * It will return the ciphered or the plain text
     * If aesKey has not the proper Padding (PKCS5Padding) an exception will be thrown.
     * @param object
     * @param aesKey
     * @param iv
     * @return byte[]
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static SealedObject encrypt(Serializable object, Key aesKey, byte[] iv)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException {
        Cipher cipher = Cipher.getInstance(SYMMETRIC_ALGORITHM);
        IvParameterSpec ips = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, aesKey, ips);

        return new SealedObject(object, cipher);
    }
}
