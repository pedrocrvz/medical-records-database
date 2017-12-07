package pt.ulisboa.tecnico.sirs.authorization;

import pt.ulisboa.tecnico.sirs.Entity;
import pt.ulisboa.tecnico.sirs.exception.SecurityLibraryException;

import java.io.Serializable;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

/**
 * This is an abstract class that defines an authorization
 * Authorizations are signed by hospitals, and assure that the request being made is valid
 * NHS verifies the validity of the digital signature, and if its valid fulfills the request, otherwise rejects
 */
public abstract class Authorization implements Serializable {
    final Certificate hospitalCertificate;
    final Certificate doctorCertificate;
    final Certificate patientCertificate;
    final byte[] hospitalSignature;

    public Authorization(Certificate hc, Certificate dc, Certificate pc, byte[] hs){
        hospitalCertificate = hc;
        doctorCertificate = dc;
        patientCertificate = pc;
        hospitalSignature = hs;
    }

    public Certificate getHospitalCertificate() {
        return hospitalCertificate;
    }

    public Certificate getDoctorCertificate() {
        return doctorCertificate;
    }

    public Certificate getPatientCertificate() {
        return patientCertificate;
    }

    /**
     * This method verifies if hospital, doctor and patient certificates were signed by the given ca certificate
     * @param ca Certificate Authority, in the context of problem is the certificate of NHS
     * @return Returns a boolean
     * @throws SecurityLibraryException
     */
    public boolean isValid(Certificate ca) throws SecurityLibraryException {
        try {
            hospitalCertificate.verify(ca.getPublicKey());
            patientCertificate.verify(ca.getPublicKey());
            doctorCertificate.verify(hospitalCertificate.getPublicKey());
        } catch (CertificateException | NoSuchAlgorithmException | InvalidKeyException | SignatureException | NoSuchProviderException e) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Authorization{" +
                "hospitalCertificate=" + hospitalCertificate +
                ", doctorCertificate=" + doctorCertificate +
                ", patientCertificate=" + patientCertificate +
                ", hospitalSignature=" + Entity.toBase64(hospitalSignature) +
                '}';
    }
}
