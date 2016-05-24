package Server;
import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Chord.*;

/**
 * Created by heka1203 on 2016-04-22.
 */
public interface SuperNode extends Remote {

    public IdNode getClosestNode(IdNode idNode) throws RemoteException;
    public void addNode(IdNode node) throws RemoteException;

    public FileList getFileList() throws RemoteException;
    public void addFile(String filename, ArrayList<File> files) throws RemoteException;
    public int getNumberOfNodes() throws RemoteException;

}