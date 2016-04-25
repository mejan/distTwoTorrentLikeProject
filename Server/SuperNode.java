package Server;
import java.rmi.Remote;
import java.rmi.RemoteException;
import Chord.*;

/**
 * Created by heka1203 on 2016-04-22.
 */
public interface SuperNode extends Remote {
    public IdNode getClosestNode(IdNode idNode) throws RemoteException;
    public void addNode(IdNode idNode) throws RemoteException;
}