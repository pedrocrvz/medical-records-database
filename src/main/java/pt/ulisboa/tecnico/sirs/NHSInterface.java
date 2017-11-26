package pt.ulisboa.tecnico.sirs;

import pt.ulisboa.tecnico.sirs.exception.NotAuthorizedException;
import pt.ulisboa.tecnico.sirs.exception.ServerCipherException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.PublicKey;
import java.util.List;

public interface NHSInterface extends Remote {
    void putRecord(Record record) throws RemoteException, NotAuthorizedException, ServerCipherException;
    List<Record> getRecords(PublicKey patient) throws RemoteException, NotAuthorizedException, ServerCipherException;
}
