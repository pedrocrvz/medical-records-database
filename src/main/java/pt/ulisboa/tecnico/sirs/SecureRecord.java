package pt.ulisboa.tecnico.sirs;

import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;

import static pt.ulisboa.tecnico.sirs.Entity.toBase64;

public class SecureRecord extends Record implements Serializable {
    private byte[] signature;

    public SecureRecord(final PublicKey doctorKey, final PublicKey patientKey,
                        final String record) {
        super(doctorKey, patientKey, record);
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
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
        return Security.isValidSignature(getDoctorPublicKey(), getBytes(), signature);
    }

    @Override
    public String toString() {
        return "SecureRecord{" +
                "record=" + super.toString() +
                ", signature=" + toBase64(signature) +
                '}';
    }
}
