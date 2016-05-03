package Server;

import java.io.File;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Chord.*;
/**
 * Created by heka1203 on 2016-04-22.
 */
public class SuperNodeImpl extends UnicastRemoteObject implements SuperNode {
    private static String ip;
    private static int port;
    private static NodeList nodeList;
    private static HashMap<String, List> fileTable;

    public SuperNodeImpl(int port) throws UnknownHostException, RemoteException, AlreadyBoundException, MalformedURLException {
        this.ip = InetAddress.getLocalHost().getHostAddress();
        this.port = port;
        this.nodeList = new NodeList();
        this.fileTable = new HashMap<String, List>();
        LocateRegistry.createRegistry(port);

        Naming.bind("//" + this.ip + ":" + this.port + "/nodeList", this);
    }

    public void addFile(File file, final List<File> files) throws RemoteException{
        if(!fileTable.containsKey(file.getName())){ //List not init.
            ArrayList<File> fileIdes = new ArrayList<File>(){{ addAll(files);}};
            fileTable.put(file.getName(), fileIdes);
        }
    }


    public HashMap<String, List> getFileTable() throws RemoteException {
        return fileTable;
    }

    public IdNode getClosestNode(IdNode idNode){
        return nodeList.getClosestNode(idNode);
    }
    public void addNode(IdNode idNode){
        nodeList.addNode(idNode);
    }

}