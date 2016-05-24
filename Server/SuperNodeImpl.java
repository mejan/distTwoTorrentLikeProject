package Server;

import java.awt.*;
import java.io.File;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import Chord.*;
/**
 * Created by heka1203 on 2016-04-22.
 */
public class SuperNodeImpl extends UnicastRemoteObject implements SuperNode {
    private static final String ip;
    static{
        try{
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new HeadlessException(e.getMessage());
        }

    }

    private static final int port = 7000;
    private static final NodeList nodeList = new NodeList();
    private static FileList fileList = new FileList(new HashMap<String, ArrayList<File>>());

    public SuperNodeImpl() throws RemoteException {
        try {
            System.setProperty("java.rmi.server.hostname", ip);
            LocateRegistry.createRegistry(port);
            Naming.bind("//" + ip + ":" + port + "/superNode" , this);
        } catch (AlreadyBoundException e) {
            System.err.println("The object is already bound at this address.");
        } catch (MalformedURLException e) {
            System.err.println("Invalid lookup URL.");
        }

    }


    public FileList getFileList() throws RemoteException {
        return fileList;
    }
    public void addFile(String filename, ArrayList<File> files) throws RemoteException{
        fileList.add(filename, files);
    }


    public IdNode getClosestNode(IdNode idNode){
        return nodeList.getClosestNode(idNode);
    }
    public void addNode(IdNode idNode){
        nodeList.addNode(idNode);
    }
    public int getNumberOfNodes(){
        return nodeList.size();
    }

    /*public static void main(String args[]){
        try{
            new SuperNodeImpl();
        } catch (RemoteException e) {
            System.err.println("Remote exception: " + e.getMessage());
        }

    }*/

}