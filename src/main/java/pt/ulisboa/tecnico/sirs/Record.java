package pt.ulisboa.tecnico.sirs;

import pt.ulisboa.tecnico.sirs.exception.SecurityLibraryException;
import pt.ulisboa.tecnico.sirs.security.Signable;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;

import static pt.ulisboa.tecnico.sirs.Entity.concat;
import static pt.ulisboa.tecnico.sirs.Entity.toBase64;

public abstract class Record implements Serializable, Signable {
    private final Certificate doctorCertificate;
    private final Certificate patientCertificate;
    private final long date;
    private final String record;

    public Record(final Certificate doctorCertificate, final Certificate patientCertificate, final String record) {
        this.doctorCertificate = doctorCertificate;
        this.patientCertificate = patientCertificate;
        this.date = System.currentTimeMillis();
        this.record = record;
    }

    /**
     * This will be useful to sign records
     * @return byte array which corresponds to this array
     */
    public byte[] getBytes() throws SecurityLibraryException {
        try {
            return concat(
                    doctorCertificate.getEncoded(),
                    patientCertificate.getEncoded(),
                    ByteBuffer.allocate(Long.BYTES).putLong(date).array(),
                    record.getBytes()
            );
        } catch (CertificateEncodingException e) {
            throw new SecurityLibraryException(e);
        }
    }

    public Certificate getDoctorCertificate() {
        return doctorCertificate;
    }

    public Certificate getPatientCertificate() {
        return patientCertificate;
    }

    public PublicKey getPatientPublicKey() {
        return patientCertificate.getPublicKey();
    }

    public PublicKey getDoctorPublicKey() {
        return doctorCertificate.getPublicKey();
    }

    public long getDate() {
        return date;
    }

    public String getRecord() {
        return record;
    }

    @Override
    public String toString() {
        return "Record{" +
                "doctorKey=" + toBase64(doctorCertificate.getPublicKey().getEncoded()) +
                ", patientKey=" + toBase64(doctorCertificate.getPublicKey().getEncoded()) +
                ", date=" + date +
                ", record='" + record + '\'' +
                '}';
    }
}
