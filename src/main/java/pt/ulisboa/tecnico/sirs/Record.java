package pt.ulisboa.tecnico.sirs;

import java.security.PublicKey;
import java.util.Date;

public class Record {
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

    public final byte[] getBytes(){
        String doctorKey = Entity.getBase64PublicKey(this.doctorKey);
        String patientKey = Entity.getBase64PublicKey(this.patientKey);

        String object = doctorKey + patientKey + this.date.toString() + record;
        return object.getBytes();
    }

    public final PublicKey getDoctorPublicKey() {
        return doctorKey;
    }
}
