package pt.ulisboa.tecnico.sirs.security;

import pt.ulisboa.tecnico.sirs.exception.SecurityLibraryException;

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
     * @param publicKey Entity's public key that signed the bytes
     * @param signedBytes Signed bytes
     * @param signature Signature
     * @return returns true if signature is valid for key provided, false otherwise
     * @throws SecurityLibraryException Whenever the library runs into a problem
     */
    public static boolean isValidSignature(PublicKey publicKey, byte[] signedBytes, byte[] signature)
            throws SecurityLibraryException {
        try {
            java.security.Signature sig = java.security.Signature.getInstance(SIGNATURE_ALGORITHM);
            sig.initVerify(publicKey);
            sig.update(signedBytes);
            return sig.verify(signature);
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            throw new SecurityLibraryException(e);
        }
    }

    /**
     * It digests the message and signs that digest with senders private key.
     * This method does everything, so you don't need to use digest method before.
     * @param bytesToSign Bytes to sign
     * @param privateKey Entity's private key which will be used to sign the bytes
     * @return returns the signature as a byte array
     * @throws SecurityLibraryException Whenever the library runs into a problem
     */
    public static byte[] makeDigitalSignature(byte[] bytesToSign, PrivateKey privateKey)
            throws  SecurityLibraryException {
        try {
            java.security.Signature sig = java.security.Signature.getInstance(SIGNATURE_ALGORITHM);
            sig.initSign(privateKey);
            sig.update(bytesToSign);
            return sig.sign();
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            throw new SecurityLibraryException(e);
        }
    }
}
