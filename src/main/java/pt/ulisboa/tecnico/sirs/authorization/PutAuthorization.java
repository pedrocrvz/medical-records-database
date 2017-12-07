package pt.ulisboa.tecnico.sirs.authorization;

import pt.ulisboa.tecnico.sirs.SecureRecord;
import pt.ulisboa.tecnico.sirs.exception.SecurityLibraryException;
import pt.ulisboa.tecnico.sirs.security.DigitalSignature;

import java.io.Serializable;
import java.security.cert.Certificate;

/**
 * This is an class that defines a put authorization
 * Put authorizations are signed by hospitals, and assure that the put request being made is valid
 * NHS verifies the validity of the digital signature, and if its valid fulfills the request, otherwise rejects
 */
public class PutAuthorization extends Authorization implements Serializable {
    final SecureRecord secureRecord;

    public PutAuthorization(SecureRecord sr, Certificate hc, Certificate dc, Certificate pc, byte[] hs) {
        super(hc, dc, pc, hs);
        secureRecord = sr;
    }

    /**
     * This method invokes the super method, and verifies if hospital correctly signed everything
     * @param ca Certificate Authority, in the context of problem is the certificate of NHS
     * @return Returns a boolean
     * @throws SecurityLibraryException
     */
    @Override
    public boolean isValid(Certificate ca) throws SecurityLibraryException {
        return super.isValid(ca) && secureRecord.hasValidSignature() && DigitalSignature.isValidSignature(hospitalCertificate.getPublicKey(), secureRecord.getBytes(), hospitalSignature);
    }
}
