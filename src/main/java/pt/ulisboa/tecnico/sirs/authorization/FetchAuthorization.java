package pt.ulisboa.tecnico.sirs.authorization;

import pt.ulisboa.tecnico.sirs.exception.SecurityLibraryException;
import pt.ulisboa.tecnico.sirs.security.DigitalSignature;

import java.io.Serializable;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;

import static pt.ulisboa.tecnico.sirs.Entity.concat;

public class FetchAuthorization extends Authorization implements Serializable {
    public FetchAuthorization(Certificate hc, Certificate dc, Certificate pc, byte[] hs){
        super(hc, dc, pc, hs);
    }

    @Override
    public boolean isValid() throws SecurityLibraryException {
        try {
            return DigitalSignature.isValidSignature(
                    hospitalCertificate.getPublicKey(),
                    concat(doctorCertificate.getEncoded(), patientCertificate.getEncoded()),
                    hospitalSignature
            );
        } catch (CertificateEncodingException e) {
            throw new SecurityLibraryException(e);
        }
    }
}
