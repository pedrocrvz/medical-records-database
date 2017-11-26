package pt.ulisboa.tecnico.sirs;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import java.security.*;

public class Security {
    public static String SIGNATURE_ALGORITHM = "SHA256WithRSA";
    public static String PRNG_ALGORITHM = "SHA1PRNG";
    public static String SYMMETRIC_ALGORITHM = "AES/CBC/PKCS5Padding";

    /**
     * Checks validity of signature of signedBytes, checks if this signature was created
     * with entity private key
     * @param publicKey
     * @param signedBytes
     * @param signature
     * @return returns true if signature is valid for key provided, false otherwise
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public static boolean isValidSignature(PublicKey publicKey, byte[] signedBytes, byte[] signature)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature sig = Signature.getInstance(SIGNATURE_ALGORITHM);
        sig.initVerify(publicKey);
        sig.update(signedBytes);
        return sig.verify(signature);
    }

    /**
     * It digests the message and signs that digest with senders private key.
     * This method does everything, so you don't need to use digest method before.
     * @param bytesToSign
     * @param privateKey
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public static byte[] makeDigitalSignature(byte[] bytesToSign, PrivateKey privateKey)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature sig = Signature.getInstance(SIGNATURE_ALGORITHM);
        sig.initSign(privateKey);
        sig.update(bytesToSign);
        return sig.sign();
    }

    /**
     * Returns an array with byteNumber bytes of secure random bytes.
     *
     * @param byteNumber - number of bytes of the returning byte[]
     * @return randomBytes - byte[] with random suff
     * @throws NoSuchAlgorithmException
     */
    private static byte[] getSecureRandomNumber(int byteNumber)
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
     * @param bytesToDecrypt
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
    public static byte[] decryptBytes(byte[] bytesToDecrypt, Key aesKey, byte[] iv)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        return doAES(bytesToDecrypt, aesKey, iv, Cipher.DECRYPT_MODE);
    }

    /**
     * It encrypts using AES-CBC bytesToEncrypt using aesKey.
     * It will return the ciphered or the plain text
     * If aesKey has not the proper Padding (PKCS5Padding) an exception will be thrown.
     * @param bytesToEncrypt
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
    public static byte[] encryptBytes(byte[] bytesToEncrypt, Key aesKey, byte[] iv)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        return doAES(bytesToEncrypt, aesKey, iv, Cipher.ENCRYPT_MODE);
    }

    private static byte[] doAES(byte[] bytesToEncrypt, Key aesKey, byte[] iv, int mode)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(SYMMETRIC_ALGORITHM);
        IvParameterSpec ips = new IvParameterSpec(iv);
        cipher.init(mode, aesKey, ips);
        return cipher.doFinal(bytesToEncrypt);
    }

}
