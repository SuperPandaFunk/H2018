package ca.polymtl.inf8480.tp1.shared;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.io.Serializable;

public class LockMessage implements Serializable{

    public LockMessage(Boolean succ, String err){
        success = succ;
        errMess = err;
    }

    public Boolean isSuccessful(){
        return success;
    }

    public String getMessage(){
        return errMess;
    }

    private boolean success;
    private String errMess;
}