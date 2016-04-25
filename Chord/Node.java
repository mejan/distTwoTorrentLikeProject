package Chord;


import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by heka1203 on 2016-04-22.
 */
//ONLY methods accessible remotely should be placed here
public interface Node extends Remote {


    public IdNode getIdNode() throws RemoteException;

    public IdNode getSuccessor() throws RemoteException;

    public IdNode getPredecessor() throws RemoteException;

    public void setSuccessor(IdNode successor) throws RemoteException;

    public void setPredecessor(IdNode predecessor) throws RemoteException;

    public IdNode findSuccessor(int id) throws RemoteException , NotBoundException, MalformedURLException;

    public IdNode findCloestPrecedingFinger(int id) throws RemoteException;


    public void updateFingerTable(IdNode idNode, int i) throws RemoteException, NotBoundException, MalformedURLException;


}