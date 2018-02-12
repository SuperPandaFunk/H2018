package ca.polymtl.inf8480.tp1.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ServerInterface extends Remote {
	int execute(int a, int b, byte[] c) throws RemoteException;
    int test(byte[] bytes) throws RemoteException;
}
