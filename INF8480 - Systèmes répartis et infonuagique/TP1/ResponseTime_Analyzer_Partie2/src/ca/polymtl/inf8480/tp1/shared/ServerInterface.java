package ca.polymtl.inf8480.tp1.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ServerInterface extends Remote {
	StubFile CreateClientID() throws RemoteException;
    boolean create(String nom) throws RemoteException;
    ArrayList<StubFile> list() throws RemoteException;
    ArrayList<StubFile>  sync() throws RemoteException;
    StubFile get(String nom, byte[] checkSum) throws RemoteException;
    LockMessage lock(String nom, int clientID, byte[] checkSum) throws RemoteException;
    boolean push(String nom, byte[] contenu, int clientID) throws RemoteException;
}
