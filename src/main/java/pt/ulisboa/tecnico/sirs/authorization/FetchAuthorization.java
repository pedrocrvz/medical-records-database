package pt.ulisboa.tecnico.sirs.authorization;

import pt.ulisboa.tecnico.sirs.exception.SecurityLibraryException;
import pt.ulisboa.tecnico.sirs.security.DigitalSignature;

import java.io.Serializable;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;

import static pt.ulisboa.tecnico.sirs.Entity.concat;


/**
 * This is an class that defines a fetch authorization
 * Fetch authorizations are signed by hospitals, and assure that the fetch request being made is valid
 * NHS verifies the validity of the digital signature, and if its valid fulfills the request, otherwise rejects
 */
public class FetchAuthorization extends Authorization implements Serializable {
    public FetchAuthorization(Certificate hc, Certificate dc, Certificate pc, byte[] hs){
        super(hc, dc, pc, hs);
    }

    /**
     * This method invokes the super method, and verifies if hospital correctly signed everything
     * @param ca Certificate Authority, in the context of problem is the certificate of NHS
     * @return Returns a boolean
     * @throws SecurityLibraryException
     */
    @Override
    public boolean isValid(Certificate ca) throws SecurityLibraryException {
        try {
            return super.isValid(ca) && DigitalSignature.isValidSignature(hospitalCertificate.getPublicKey(), concat(doctorCertificate.getEncoded(), patientCertificate.getEncoded()), hospitalSignature);
        } catch (CertificateEncodingException e) {
            throw new SecurityLibraryException(e);
        }
    }
}
