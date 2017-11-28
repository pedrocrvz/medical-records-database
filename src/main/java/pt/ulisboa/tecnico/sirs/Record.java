package pt.ulisboa.tecnico.sirs;

import pt.ulisboa.tecnico.sirs.security.Signable;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.security.PublicKey;

import static pt.ulisboa.tecnico.sirs.Entity.concat;
import static pt.ulisboa.tecnico.sirs.Entity.toBase64;

public abstract class Record implements Serializable, Signable {
    private final PublicKey doctorKey;
    private final PublicKey patientKey;
    private final long date;
    private final String record;

    public Record(final PublicKey doctorKey, final PublicKey patientKey, final String record) {
        this.doctorKey = doctorKey;
        this.patientKey = patientKey;
        this.date = System.currentTimeMillis();
        this.record = record;
    }

    /**
     * This will be useful to sign records
     * @return byte array which corresponds to this array
     */
    public byte[] getBytes() {
        return concat(
                doctorKey.getEncoded(),
                patientKey.getEncoded(),
                ByteBuffer.allocate(Long.BYTES).putLong(date).array(),
                record.getBytes()
        );
    }

    public final PublicKey getPatientPublicKey() {
        return patientKey;
    }

    public final PublicKey getDoctorPublicKey() {
        return doctorKey;
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
                "doctorKey=" + toBase64(doctorKey.getEncoded()) +
                ", patientKey=" + toBase64(doctorKey.getEncoded()) +
                ", date=" + date +
                ", record='" + record + '\'' +
                '}';
    }
}
