package pt.ulisboa.tecnico.sirs.data;

import pt.ulisboa.tecnico.sirs.Record;
import pt.ulisboa.tecnico.sirs.exception.ServerCipherException;

import java.security.PublicKey;
import java.util.List;

public interface DataStore {
    List<Record> getRecords(PublicKey patientKey) throws ServerCipherException;
    void putRecord(PublicKey patientKey, Record record) throws ServerCipherException;
}
