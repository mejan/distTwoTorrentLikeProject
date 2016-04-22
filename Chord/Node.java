package Chord;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by heka1203 on 2016-04-22.
 */
public interface Node extends Remote {
    public IdNode getIdNode() throws RemoteException;
    public IdNode getSuccessor() throws RemoteException;

    public void setSuccessor(IdNode successor) throws RemoteException;

    public IdNode getPredecessor() throws RemoteException;

    public void setPredecessor(IdNode predecessor) throws RemoteException;
    public boolean isConnected() throws RemoteException;

    public void setConnected(boolean connected) throws RemoteException;

}