package pt.ulisboa.tecnico.sirs.security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import java.security.*;

public class DigitalSignature {
    public static String SIGNATURE_ALGORITHM = "SHA256WithRSA";

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
        java.security.Signature sig = java.security.Signature.getInstance(SIGNATURE_ALGORITHM);
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
        java.security.Signature sig = java.security.Signature.getInstance(SIGNATURE_ALGORITHM);
        sig.initSign(privateKey);
        sig.update(bytesToSign);
        return sig.sign();
    }

}
