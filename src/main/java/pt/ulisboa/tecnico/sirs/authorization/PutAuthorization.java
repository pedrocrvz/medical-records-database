package pt.ulisboa.tecnico.sirs.authorization;

import pt.ulisboa.tecnico.sirs.Entity;
import pt.ulisboa.tecnico.sirs.SecureRecord;
import pt.ulisboa.tecnico.sirs.exception.SecurityLibraryException;
import pt.ulisboa.tecnico.sirs.security.DigitalSignature;

import java.io.Serializable;
import java.security.cert.Certificate;

public class PutAuthorization extends Authorization implements Serializable {
    final SecureRecord secureRecord;

    public PutAuthorization(SecureRecord sr, Certificate hc, Certificate dc, Certificate pc, byte[] hs) {
        super(hc, dc, pc, hs);
        secureRecord = sr;
    }

    @Override
    public boolean isValid() throws SecurityLibraryException {
        if(!secureRecord.hasValidSignature())
            return false;

        if(!DigitalSignature.isValidSignature(
                hospitalCertificate.getPublicKey(),
                secureRecord.getBytes(),
                hospitalSignature))
            return false;
        return true;
    }
}
