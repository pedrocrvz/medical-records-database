package pt.ulisboa.tecnico.sirs.authorization;

import pt.ulisboa.tecnico.sirs.SecureRecord;
import pt.ulisboa.tecnico.sirs.exception.SecurityLibraryException;
import pt.ulisboa.tecnico.sirs.security.DigitalSignature;

import java.io.Serializable;
import java.security.PublicKey;
import java.security.cert.Certificate;

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

    public abstract boolean isValid() throws SecurityLibraryException;
}
