package pt.ulisboa.tecnico.sirs.data;

import org.javatuples.Pair;
import pt.ulisboa.tecnico.sirs.Record;
import pt.ulisboa.tecnico.sirs.SecureRecord;
import pt.ulisboa.tecnico.sirs.exception.SecurityLibraryException;
import pt.ulisboa.tecnico.sirs.security.AES;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import java.io.IOException;
import java.security.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class implements the Interface DataStore
 * All patient records are properly encrypted with the NHS symmetric key
 * Records have different IV (Initialization Vector) to improve security, giving use of CBC-AES
 */
public class SecureDataStore implements DataStore {
    //TODO verify data integrity when decrypting the data! HMAC/signature
    private HashMap<PublicKey, List<Pair<SealedObject, byte[]>>> encryptedRecords;
    private final Key key;

    public SecureDataStore(final Key key){
        encryptedRecords = new HashMap<>();
        this.key = key;
    }

    /**
     * This method returns all records for a patient with given PublicKey
     * @param patientKey Patient's PublicKey
     * @return Returns a List containing all Patient records
     * @throws SecurityLibraryException This is thrown in case of a catastrophe in AES library
     */
    @Override
    public final List<Record> getRecords(final PublicKey patientKey) throws SecurityLibraryException {
        createPatientListIfDoesNotExist(patientKey);
        List<Record> records = new ArrayList<>();
        for(Pair<SealedObject, byte[]> pair: encryptedRecords.get(patientKey))
            try {
                records.add((SecureRecord) AES.decrypt(pair.getValue0(), key, pair.getValue1()));
            } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException |
                    BadPaddingException | InvalidKeyException | IOException | IllegalBlockSizeException |
                    ClassNotFoundException e) {
                e.printStackTrace();
                throw new SecurityLibraryException(e);
            }

        return records;
    }

    /**
     * This method will insert a record Record for a patient with given PublicKey patientkey
     * @param patientKey Patient's PublicKey
     * @param record Record to insert
     * @throws SecurityLibraryException This is thrown in case of a catastrophe in AES library
     */
    @Override
    public void putRecord(final PublicKey patientKey, final Record record) throws SecurityLibraryException {
        createPatientListIfDoesNotExist(patientKey);
        try {
            byte[] iv = AES.generateIV(16);
            SealedObject encryptedRecord = AES.encrypt(record, key, iv);
            encryptedRecords.get(patientKey).add(new Pair<>(encryptedRecord, iv));
        } catch (NoSuchAlgorithmException | IOException | InvalidKeyException | NoSuchPaddingException |
                InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
            throw new SecurityLibraryException(e);
        }
    }

    private void createPatientListIfDoesNotExist(PublicKey patientKey){
        if(encryptedRecords.containsKey(patientKey)) return;
        encryptedRecords.put(patientKey, new ArrayList<>());
    }
}
