package pt.ulisboa.tecnico.sirs.authorization;

import pt.ulisboa.tecnico.sirs.exception.SecurityLibraryException;
import pt.ulisboa.tecnico.sirs.security.DigitalSignature;

import java.security.cert.Certificate;

public class FetchAuthorization extends Authorization {
    public FetchAuthorization(Certificate hc, Certificate dc, Certificate pc, byte[] hs){
        super(hc, dc, pc, hs);
    }

    @Override
    public boolean isValid() throws SecurityLibraryException {
        return DigitalSignature.isValidSignature(
                hospitalCertificate.getPublicKey(),
                doctorCertificate.getPublicKey().getEncoded(),
                hospitalSignature
        );
    }
}
