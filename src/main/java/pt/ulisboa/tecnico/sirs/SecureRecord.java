package pt.ulisboa.tecnico.sirs;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;

public class SecureRecord {
    final private Record record;
    final private byte[] signature;

    public SecureRecord(Record record, byte[] signature){
        this.record = record;
        this.signature = signature;
    }

    /**
     * This method verifies if the signature of record is valid
     * @return Returns whether this Record is secure or not
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public boolean hasValidSignature() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        byte[] object = record.getBytes();
        Signature sig = Entity.getSignature();
        sig.initVerify(record.getDoctor().getPublicKey());
        return sig.verify(signature);
    }
}
