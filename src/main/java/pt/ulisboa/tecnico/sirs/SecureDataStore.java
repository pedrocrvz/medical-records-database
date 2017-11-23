package pt.ulisboa.tecnico.sirs;

import java.security.Key;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SecureDataStore implements DataStore {
    private HashMap<PublicKey, List<Record>> records;

    @Override
    public List<Record> getRecords(PublicKey patientKey) {
        createPatientListIfDoesNotExist(patientKey);
        return records.get(patientKey);
    }

    @Override
    public void putRecord(PublicKey patientKey, Record record) {
        createPatientListIfDoesNotExist(patientKey);
        records.get(patientKey).add(record);
    }

    private void createPatientListIfDoesNotExist(PublicKey patientKey){
        if(records.containsKey(patientKey)) return;
        records.put(patientKey, new ArrayList<>());
    }
}
