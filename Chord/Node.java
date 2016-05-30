package Chord;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
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

    public Node getNodeFileTable(int fileId) throws RemoteException;

    public void moveFileTable(Node newOwner) throws RemoteException;

    public void mergeFileTable(FileTable mergeWithFileTable) throws RemoteException;

    public void printFileTable() throws RemoteException;

    public void addEntryFileTable(int fileId, final Node owner) throws RemoteException;


    public OutputStream getOutputStream(File file) throws RemoteException, FileNotFoundException;
    public InputStream getInputStream(File file) throws RemoteException, FileNotFoundException;
    public File getUploads() throws RemoteException;

    public double getNodeRating(Node neighbor) throws RemoteException;
    public double getPrevStartValue() throws RemoteException;

}