package pt.ulisboa.tecnico.sirs;

import java.security.PublicKey;
import java.util.List;

public interface DataStore {
    List<Record> getRecords(PublicKey patientKey);
    void putRecord(PublicKey patientKey, Record record);
}
