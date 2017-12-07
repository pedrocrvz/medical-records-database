package pt.ulisboa.tecnico.sirs;

import pt.ulisboa.tecnico.sirs.authorization.Authorization;
import pt.ulisboa.tecnico.sirs.authorization.FetchAuthorization;
import pt.ulisboa.tecnico.sirs.authorization.PutAuthorization;
import pt.ulisboa.tecnico.sirs.exception.NotAuthorizedException;
import pt.ulisboa.tecnico.sirs.exception.SecurityLibraryException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.PublicKey;
import java.util.List;

/**
 * This is the NHS interface that will be called by remote entities, like Doctors
 */
public interface NHSInterface extends Remote {
    /**
     * This method receives an authorization issued by a trusted hospital and a record. If the authorization is valid, the record is inserted in the datastore, otherwise NotAuthorizedException is thrown
     * @param authorization Authorization issued by a trusted hospital
     * @param record Record being inserted
     * @throws RemoteException Is thrown when RMI is shutdown/network inaccessible
     * @throws NotAuthorizedException Is thrown when authorization is invalid
     * @throws SecurityLibraryException Is thrown when digital signature validation runs into problem
     */
    void putRecord(PutAuthorization authorization, Record record) throws RemoteException, NotAuthorizedException, SecurityLibraryException;

    /**
     * This method receives an authorization issued by a trusted hospital and a patient public key. If the authorization is valid, the NHS returns all patient records with that public key, otherwise NotAuthorizedException is thrown
     * @param authorization Authorization issued by a trusted hospital
     * @param patient Public key correspondent to the patient
     * @return Returns a list with patient records
     * @throws RemoteException Is thrown when RMI is shutdown/network inaccessible
     * @throws NotAuthorizedException Is thrown when authorization is invalid
     * @throws SecurityLibraryException Is thrown when digital signature validation runs into problem
     */
    List<Record> getRecords(FetchAuthorization authorization, PublicKey patient) throws RemoteException, NotAuthorizedException, SecurityLibraryException;
}
