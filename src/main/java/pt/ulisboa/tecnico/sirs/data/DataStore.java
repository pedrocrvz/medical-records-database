package pt.ulisboa.tecnico.sirs.data;

import pt.ulisboa.tecnico.sirs.Record;
import pt.ulisboa.tecnico.sirs.exception.SecurityLibraryException;

import java.security.PublicKey;
import java.util.List;

/**
 * This interface defines the methods for NHS compatible Data Storage
 */
public interface DataStore {
    /**
     * This method returns all records for a patient with given PublicKey
     * @param patientKey Patient's PublicKey
     * @return Returns a List containing all Patient records
     * @throws SecurityLibraryException This is thrown in case of a catastrophe in AES library
     */
    List<Record> getRecords(PublicKey patientKey) throws SecurityLibraryException;

    /**
     * This method will insert a record Record for a patient with given PublicKey patientkey
     * @param patientKey Patient's PublicKey
     * @param record Record to insert
     * @throws SecurityLibraryException This is thrown in case of a catastrophe in AES library
     */
    void putRecord(PublicKey patientKey, Record record) throws SecurityLibraryException;
}
