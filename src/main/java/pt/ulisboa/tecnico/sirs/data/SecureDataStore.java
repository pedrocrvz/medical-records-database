package pt.ulisboa.tecnico.sirs.data;

import org.javatuples.Pair;
import org.javatuples.Tuple;
import pt.ulisboa.tecnico.sirs.Record;
import pt.ulisboa.tecnico.sirs.SecureRecord;
import pt.ulisboa.tecnico.sirs.exception.ServerCipherException;
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

public class SecureDataStore implements DataStore {
    //TODO verify data integrity when decrypting the data! HMAC/signature
    private HashMap<PublicKey, List<Pair<SealedObject, byte[]>>> encryptedRecords;
    private final Key key;

    public SecureDataStore(final Key key){
        encryptedRecords = new HashMap<>();
        this.key = key;
    }

    @Override
    public final List<Record> getRecords(final PublicKey patientKey) throws ServerCipherException {
        createPatientListIfDoesNotExist(patientKey);
        List<Record> records = new ArrayList<>();
        for(Pair<SealedObject, byte[]> pair: encryptedRecords.get(patientKey))
            try {
                records.add((SecureRecord) AES.decrypt(pair.getValue0(), key, pair.getValue1()));
            } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException |
                    BadPaddingException | InvalidKeyException | IOException | IllegalBlockSizeException |
                    ClassNotFoundException e) {
                e.printStackTrace();
                throw new ServerCipherException();
            }

        return records;
    }

    @Override
    public void putRecord(final PublicKey patientKey, final Record record) throws ServerCipherException {
        createPatientListIfDoesNotExist(patientKey);
        try {
            byte[] iv = AES.generateIV(16);
            SealedObject encryptedRecord = AES.encrypt(record, key, iv);
            encryptedRecords.get(patientKey).add(new Pair<>(encryptedRecord, iv));
        } catch (NoSuchAlgorithmException | IOException | InvalidKeyException | NoSuchPaddingException |
                InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
            throw new ServerCipherException();
        }
    }

    private void createPatientListIfDoesNotExist(PublicKey patientKey){
        if(encryptedRecords.containsKey(patientKey)) return;
        encryptedRecords.put(patientKey, new ArrayList<>());
    }
}
