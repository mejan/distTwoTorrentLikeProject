package Chord;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.NoSuchAlgorithmException;

/**
 * Created by heka1203 on 2016-04-20.
 */

public class NodeImpl extends UnicastRemoteObject implements Node {

    private IdNode successor;
    private IdNode predecessor;
    private boolean connected;
    private IdNode idNode;

    public NodeImpl(int port) throws NoSuchAlgorithmException, UnknownHostException, RemoteException {

        idNode = new IdNode(InetAddress.getLocalHost().getHostAddress(), port);
        Registry register = LocateRegistry.createRegistry(port);

        //TODO:
        //Make node implement a remote interface
        //Make the node register it self as an remote object to its ip and port number
        //contactserver for list of nodes
        //set successor and/or predecessor if list is not empty

    }
    public IdNode getIdNode() {
        return idNode;
    }
    public IdNode getSuccessor() {
        return successor;
    }

    public void setSuccessor(IdNode successor) {
        if(successor != null)
            System.out.println("Set successor: " + successor.toString());
        this.successor = successor;
    }

    public IdNode getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(IdNode predecessor) {
        if(predecessor != null)
            System.out.println("Set predecessor." + predecessor.toString());
        this.predecessor = predecessor;
    }
    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
}