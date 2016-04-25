package Server;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import Chord.*;
/**
 * Created by heka1203 on 2016-04-22.
 */
public class SuperNodeImpl extends UnicastRemoteObject implements SuperNode {
    private static String ip;
    private static int port;
    private static NodeList nodeList;

    public SuperNodeImpl(int port) throws UnknownHostException, RemoteException, AlreadyBoundException, MalformedURLException {
        this.ip = InetAddress.getLocalHost().getHostAddress();
        this.port = port;
        this.nodeList = new NodeList();

        LocateRegistry.createRegistry(port);

        Naming.bind("//" + this.ip + ":" + this.port + "/nodeList", this);
    }
    public IdNode getClosestNode(IdNode idNode){
        return nodeList.getClosestNode(idNode);
    }
    public void addNode(IdNode idNode){
        nodeList.addNode(idNode);
    }

}