package Chord;


import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by heka1203 on 2016-04-22.
 */
//ONLY methods accessible remotely should be placed here
public interface Node extends Remote {


    public IdNode getIdNode() throws RemoteException;
    public int getId() throws RemoteException;

    public Node getSuccessor() throws RemoteException;

    public Node getPredecessor() throws RemoteException;

    public void setSuccessor(Node successor) throws RemoteException;

    public void setPredecessor(Node predecessor) throws RemoteException;

    public Node findSuccessor(int id) throws RemoteException , NotBoundException, MalformedURLException;

    public Node findClosestPrecedingFinger(int id) throws RemoteException;
    public Node findPredecessor(int id) throws RemoteException, NotBoundException, MalformedURLException;

    public void updateFingerTable(int index, Node node) throws RemoteException, NotBoundException, MalformedURLException;
    public void moveFileTable(Node newSucessor) throws RemoteException;
    public void mergeFileTable(HashMap<Integer, ArrayList> mergeWith) throws RemoteException;
    public void addNodeFileTable(int fileId, final Node node) throws RemoteException;


}