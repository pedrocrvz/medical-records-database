package pt.ulisboa.tecnico.sirs;

import pt.ulisboa.tecnico.sirs.Entity;

import java.io.Serializable;
import java.security.PublicKey;
import java.util.Date;

import static pt.ulisboa.tecnico.sirs.Entity.toBase64;

public abstract class Record implements Serializable {
    private final PublicKey doctorKey;
    private final PublicKey patientKey;
    private final Date date;
    private final String record;

    public Record(final PublicKey doctorKey, final PublicKey patientKey, final String record) {
        this.doctorKey = doctorKey;
        this.patientKey = patientKey;
        this.date = new Date();
        this.record = record;
    }

    /**
     * This will be useful to sign records
     * @return byte array which corresponds to this array
     */
    public final byte[] getBytes(){
        String doctorKey = Entity.getBase64PublicKey(this.doctorKey);
        String patientKey = Entity.getBase64PublicKey(this.patientKey);

        String object = doctorKey + patientKey + this.date.toString() + record;
        return object.getBytes();
    }

    public final PublicKey getPatientPublicKey() {
        return patientKey;
    }

    public final PublicKey getDoctorPublicKey() {
        return doctorKey;
    }

    public Date getDate() {
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
