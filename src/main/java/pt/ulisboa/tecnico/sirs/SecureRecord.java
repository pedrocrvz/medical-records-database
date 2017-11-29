package pt.ulisboa.tecnico.sirs;

import pt.ulisboa.tecnico.sirs.exception.SecurityLibraryException;
import pt.ulisboa.tecnico.sirs.security.DigitalSignature;
import pt.ulisboa.tecnico.sirs.security.Signable;

import java.io.Serializable;
import java.security.PublicKey;
import java.security.cert.Certificate;

import static pt.ulisboa.tecnico.sirs.Entity.concat;
import static pt.ulisboa.tecnico.sirs.Entity.toBase64;

public class SecureRecord extends Record implements Serializable, Signable {
    private final byte[] doctorSignature;

    public SecureRecord(final Certificate dc, final Certificate pc, final String r, final Doctor d)
            throws SecurityLibraryException {
        super(dc, pc, r);
        doctorSignature = d.signBytes(super.getBytes());
    }

    public byte[] getBytes() throws SecurityLibraryException {
        return concat(
                super.getBytes(),
                doctorSignature
        );
    }

    /**
     * This method verifies if the signature of record is valid
     * @return Returns whether this Record is secure or not
     * @throws SecurityLibraryException Is thrown whenever Security library runs into trouble
     */
    public boolean hasValidSignature() throws SecurityLibraryException {
        return DigitalSignature.isValidSignature(getDoctorPublicKey(), super.getBytes(), doctorSignature);
    }

    @Override
    public String toString() {
        return "SecureRecord{" +
                "record=" + super.toString() +
                ", signature=" + toBase64(doctorSignature) +
                '}';
    }
}
