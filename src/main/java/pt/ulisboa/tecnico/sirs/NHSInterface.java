package pt.ulisboa.tecnico.sirs;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.PublicKey;
import java.util.List;

public interface NHSInterface extends Remote {
    void putRecord(Record record) throws RemoteException;
    List<Record> getRecords(PublicKey patient) throws RemoteException;
}
