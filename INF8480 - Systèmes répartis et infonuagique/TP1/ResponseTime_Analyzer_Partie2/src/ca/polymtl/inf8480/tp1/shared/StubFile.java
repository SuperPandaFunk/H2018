package ca.polymtl.inf8480.tp1.shared;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.io.Serializable;

public class StubFile implements Serializable{

    public StubFile(String name){
        name_ = name;
    }

    public String getName(){
        return name_;
    }

    public int getLockID(){
        return lockID_;
    }

    public String getLockString(){
        if (lockID_ == 0){
            return "Non verrouille";
        }else{
            return "Verrouille par " + lockID_;
        }
    }

    public byte[] getData(){
        return data_;
    }

    public void setLockID(int lockID){
        lockID_ = lockID;
    }

    public void setData(byte[] data){
        data_ = data;
    }

    public void releaseLock(){
        lockID_ = 0;
    }

    private String name_;
    private int lockID_;
    private byte[] data_;
}