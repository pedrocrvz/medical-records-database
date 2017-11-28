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
     *
     * @param authorization
     * @param record
     * @throws RemoteException
     * @throws NotAuthorizedException
     * @throws SecurityLibraryException
     */
    void putRecord(PutAuthorization authorization, Record record) throws RemoteException, NotAuthorizedException, SecurityLibraryException;

    /**
     *
     * @param authorization
     * @param patient
     * @return
     * @throws RemoteException
     * @throws NotAuthorizedException
     * @throws SecurityLibraryException
     */
    List<Record> getRecords(FetchAuthorization authorization, PublicKey patient) throws RemoteException, NotAuthorizedException, SecurityLibraryException;
}
