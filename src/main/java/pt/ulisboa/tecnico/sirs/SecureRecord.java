package pt.ulisboa.tecnico.sirs;

import pt.ulisboa.tecnico.sirs.security.DigitalSignature;

import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;

import static pt.ulisboa.tecnico.sirs.Entity.toBase64;

public class SecureRecord extends Record implements Serializable {
    private final byte[] signature;

    public SecureRecord(final PublicKey dk, final PublicKey pk, final String r, final Doctor d)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        super(dk, pk, r);
        signature = d.signBytes(getBytes());
    }

    /**
     * This method verifies if the signature of record is valid
     * @return Returns whether this Record is secure or not
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public boolean hasValidSignature()
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        return DigitalSignature.isValidSignature(getDoctorPublicKey(), getBytes(), signature);
    }

    @Override
    public String toString() {
        return "SecureRecord{" +
                "record=" + super.toString() +
                ", signature=" + toBase64(signature) +
                '}';
    }
}
