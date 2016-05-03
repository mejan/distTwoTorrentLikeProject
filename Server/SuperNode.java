package Server;
import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;

import Chord.*;

/**
 * Created by heka1203 on 2016-04-22.
 */
public interface SuperNode extends Remote {
    public IdNode getClosestNode(IdNode idNode) throws RemoteException;
    public void addNode(IdNode node) throws RemoteException;
    public void addFile(File file, List<File> files) throws RemoteException;
    public HashMap<String, List> getFileTable() throws RemoteException;

}