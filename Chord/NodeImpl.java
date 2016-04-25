package Chord;


import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * Created by heka1203 on 2016-04-20.
 */

public class NodeImpl extends UnicastRemoteObject implements Node {

    private IdNode successor;
    private IdNode predecessor;
    private IdNode idNode;
    private ArrayList<Finger> fingerTable;

    public NodeImpl(int port) throws NoSuchAlgorithmException, RemoteException, UnknownHostException, AlreadyBoundException, MalformedURLException {

        this.idNode = new IdNode(InetAddress.getLocalHost().getHostAddress(), port);
        this.fingerTable = new ArrayList<>(Hash.HASH_LENGTH); //initial capacity
        for(int i = 0; i < Hash.HASH_LENGTH; i++){
            int fingerId = Chord.getFingerIdOf(this.getIdNode().getId(), i);
            fingerTable.add(i, new Finger(fingerId, null));
        }
        register(port); //try register

        //TODO:
        //Make node implement a remote interface *
        //Make the node register it self as an remote object to its ip and port number *
        //contactserver for list of nodes
        //set successor and/or predecessor if list is not empty

    }

    private void register(int port) throws RemoteException, AlreadyBoundException, MalformedURLException {
        LocateRegistry.createRegistry(port);
        //System.setProperty("java.rmi.server.hostname", idNode.getIp()); MAY BE NEEDED FOR LATER!!!
        Naming.bind(getLookupUrl(idNode), this);

    }

    private String getLookupUrl(IdNode idNode){
        return "//" + idNode.getIp() + ":" + idNode.getPort() + "/" + idNode.toString();
    }

    public void setFinger(int index, Finger finger){
        fingerTable.add(index, finger);
    }
    
    public Finger getFinger(int index){
        return fingerTable.get(index);
    }

    public void setFingerIdSuccessor(int index, IdNode toSet){
        Finger finger = new Finger(Chord.getFingerIdOf(getIdNode().getId(), index), toSet);
        if(fingerTable.size() - 1 < index)
            fingerTable.add(index, finger);
        else
            fingerTable.set(index, finger);
    }

    public void initFingerTable(IdNode remoteId) throws RemoteException, NotBoundException, MalformedURLException {
        //Find our first successor at entry 0 in the fingertable and set it
        int fingerId = Chord.getFingerIdOf(getIdNode().getId(), 0);
        Node remoteNode = (Node)Naming.lookup(getLookupUrl(remoteId));
        IdNode remoteSuccessorId = remoteNode.findSuccessor(fingerId);
        setFinger(0, new Finger(fingerId, remoteSuccessorId));
        System.out.println(getFinger(0).getIdNode());
        //Now find our successor.predecessor
        Node remoteSuccessor = (Node)Naming.lookup(getLookupUrl(getSuccessor()));
        setPredecessor(remoteSuccessor.getPredecessor());

        /*for (int index = 0; index< Hash.HASH_LENGTH-1; index++){
            if(isFingerInBetween(index)){
                System.out.println(getFinger(0).getIdNode());
                //setFingerIdSuccessor(index + 1, getFinger(index).getIdNode());
            } else{
                remoteNode = (Node)Naming.lookup(getLookupUrl(remoteId));
                setFingerIdSuccessor(index+1, remoteNode.findSuccessor(getFinger(index+1).getId()));
            }
        }*/

        System.out.println("First index is: " + getFinger(0).getId() +" Successor of first index: " + getFinger(0).getIdNode().getId());
        System.out.println("Our predecessor: " + getPredecessor().getId());
        //implement remote methods
    }

    public ArrayList<Finger> getFingerTable(){return fingerTable;}

    //Available remote methods goes here
    public IdNode findSuccessor(int id) throws RemoteException, NotBoundException, MalformedURLException {
        IdNode nodeId = findPredecessor(id);

        String remoteUrl = getLookupUrl(nodeId);
        Node node = (Node)Naming.lookup(remoteUrl);

        return node.getSuccessor();
    }

    public IdNode findPredecessor(int id) {
        IdNode node = this.getIdNode();
        if (node != getPredecessor()){
            while (!isIdInBetween(id)) {
                node = findCloestPrecedingFinger(id);
            }
        }
        return node;
    }

    public IdNode findCloestPrecedingFinger(int id){
        for(int i = Hash.HASH_LENGTH - 1; i >= 0; i--){
            Finger finger = getFinger(i);
            if(isFingerInBetween(i, id)){
                return finger.getIdNode();
            }
        }
        return this.getIdNode();
    }

    public boolean isFingerInBetween(int index){
        int fingerId = getFinger(index + 1).getId();
        int fingerIdNode = getFinger(index).getIdNode().getId();
        return this.getIdNode().getId() <= fingerId && fingerId < fingerIdNode;
    }

    public boolean isIdInBetween(int id){
        return this.idNode.getId() < id && id <= this.getSuccessor().getId(); //|| (this.getSuccessor().getId() <= id && id < this.idNode.getId();
    }

    public boolean isFingerInBetween(int index, int id){
        return (id < getFinger(index).getIdNode().getId() && getFinger(index).getIdNode().getId() < this.idNode.getId()); // ||  (this.idNode.getId()  getFinger(index).getIdNode().getId() && getFinger(index).getId() < id);
    }

    public IdNode getIdNode() {
        return idNode;
    }

    public IdNode getSuccessor() {
        return getFinger(0).getIdNode();
    }

    public IdNode getPredecessor() {
        return predecessor;
    }

    public void setSuccessor(IdNode successor) {
        if(successor != null)
            System.out.println("Set successor: " + successor.toString());
        this.successor = successor;
    }

    public void setPredecessor(IdNode predecessor) {
        if(predecessor != null)
            System.out.println("Set predecessor." + predecessor.toString());
        this.predecessor = predecessor;
    }

}