package pt.ulisboa.tecnico.sirs;

import java.util.Base64;
import java.util.Date;

public class Record {
    private final Doctor doctor;
    private final Patient patient;
    private final Date date;
    private final String record;

    public Record(final Doctor doctor, final Patient patient, final String record) {
        this.doctor = doctor;
        this.patient = patient;
        this.date = new Date();
        this.record = record;
    }

    public byte[] getBytes(){
        String doctorKey = doctor.getBase64PublicKey();
        String patientKey = patient.getBase64PublicKey();

        String object = doctorKey + patientKey + this.date.toString() + record;
        return object.getBytes();
    }

    public Doctor getDoctor() {
        return doctor;
    }
}
