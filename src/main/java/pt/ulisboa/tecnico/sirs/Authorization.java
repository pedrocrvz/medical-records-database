package pt.ulisboa.tecnico.sirs;

import java.security.PublicKey;
import java.security.cert.Certificate;

public class Authorization {
    private final Certificate hospitalCertificate;
    private final Certificate doctorCertificate;
    private final Certificate patientCertificate;
    private final byte[] hospitalSignature;
    private final SecureRecord secureRecord;

    public Authorization(SecureRecord sr, Certificate hc, Certificate dc, Certificate pc, byte[] hs){
        secureRecord = sr;
        hospitalCertificate = hc;
        doctorCertificate = dc;
        patientCertificate = pc;
        hospitalSignature = hs;
    }

    public Authorization(Certificate hc, Certificate dc, Certificate pc, byte[] hs){
        hospitalCertificate = hc;
        doctorCertificate = dc;
        patientCertificate = pc;
        hospitalSignature = hs;
        secureRecord = null;
    }

    public PublicKey patientPublicKey(){
        return patientCertificate.getPublicKey();
    }

    public boolean isValid(){
        //TODO
        return true;
    }
}
