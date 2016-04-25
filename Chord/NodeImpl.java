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

    public void setFingerIdSuccessor(int index, IdNode idNode){
        fingerTable.get(index).setIdNode(idNode);
    }

    public void initFingerTable(IdNode remoteId) throws RemoteException, NotBoundException, MalformedURLException {
        //Find our first successor at entry 0 in the fingertable and set it
        int fingerId = Chord.getFingerIdOf(getIdNode().getId(), 0);
        Node remoteNode = (Node)Naming.lookup(getLookupUrl(remoteId));
        IdNode remoteSuccessorId = remoteNode.findSuccessor(fingerId);
        setFingerIdSuccessor(0, remoteSuccessorId);
        //setFinger(0, new Finger(fingerId, remoteSuccessorId));
        //Now find our successor.predecessor
        Node remoteSuccessor = (Node)Naming.lookup(getLookupUrl(getSuccessor()));
        setPredecessor(remoteSuccessor.getPredecessor());
        //Setting seccessors predecesors to this node.
        remoteSuccessor.setPredecessor(getIdNode());
        //System.out.println("This node id: " + getIdNode().getId());
        for (int index = 0; index < fingerTable.size()-1; index++){
            if(isFingerInBetween(index)){

                setFingerIdSuccessor(index+1, getFinger(index).getIdNode());
            } else{
                remoteNode = (Node)Naming.lookup(getLookupUrl(remoteId));
                fingerId = getFinger(index+1).getId();
                setFingerIdSuccessor(index+1, remoteNode.findSuccessor(fingerId));
            }

            //setFingerIdSuccessor(index+1, getFinger(index).getIdNode());
        }

        //System.out.println("First index is: " + getFinger(0).getId() +" Successor of first index: " + getFinger(0).getIdNode().getId());
        //System.out.println("Our predecessor: " + getPredecessor().getId());
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

    public IdNode findPredecessor(int id) throws RemoteException, NotBoundException, MalformedURLException {
        IdNode n = this.getIdNode();
            int numHops = 0;
            while(!isIdInBetween(id) && numHops != Hash.HASH_LENGTH){
                Node remoteNode = (Node)Naming.lookup(getLookupUrl(n));
                n = remoteNode.findCloestPrecedingFinger(id);
                ++numHops;
            }
            /*while (!isIdInBetween(id)) {
                node = findCloestPrecedingFinger(id);
            }*/
        return n;
    }


    public IdNode findCloestPrecedingFinger(int id){
        for(int i = fingerTable.size() - 1; i >= 0; i--){
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
        //System.out.println(" this node: " + getIdNode().getId() + " fingerID: " + fingerId + " fingerIdNode" + fingerIdNode);
        return this.getIdNode().getId() <= fingerId && fingerId < fingerIdNode;
    }

    public boolean isIdInBetween(int id){
        if(this.getIdNode().getId() == this.getSuccessor().getId()){
            return true;
        }
        //System.out.println("n':" + this.idNode.getId() + " id: " + id + " n'.successor" + this.getSuccessor().getId());
        //System.out.println(this.idNode.getId() < id && id <= this.getSuccessor().getId());
        return this.idNode.getId() < id && id <= this.getSuccessor().getId(); //|| (this.getSuccessor().getId() <= id && id < this.idNode.getId();
    }

    public boolean isFingerInBetween(int index, int id){
        return (id < getFinger(index).getIdNode().getId() && getFinger(index).getIdNode().getId() < this.idNode.getId()); // ||  (this.idNode.getId()  getFinger(index).getIdNode().getId() && getFinger(index).getId() < id);
    }

    public void updateOthers() throws RemoteException, NotBoundException, MalformedURLException {
        //TODO:
        //Check why update is not working (all get the same successor(to everything).
        //Migth be a id number size problem.
        for(int i = 0; i < fingerTable.size() - 1; i++){
            IdNode nodeId = findPredecessor(Chord.getFingerIdOf(getIdNode().getId(), i));
            Node node = (Node)Naming.lookup(getLookupUrl(nodeId));
            node.updateFingerTable(this.idNode, i);
        }
    }
    public void updateFingerTable(IdNode idNode, int i) throws RemoteException, NotBoundException, MalformedURLException {
        if(getIdNode().getId() <= idNode.getId() && idNode.getId() < getFinger(i).getIdNode().getId()){
            setFingerIdSuccessor(i, idNode);
            IdNode pId = getPredecessor();
            Node p = (Node)Naming.lookup(getLookupUrl(pId));
            p.updateFingerTable(idNode, i);
        }
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
            //System.out.println("Set successor: " + successor.toString());
        this.successor = successor;
    }

    public void setPredecessor(IdNode predecessor) {
        if(predecessor != null)
            //System.out.println("Set predecessor." + predecessor.toString());
        this.predecessor = predecessor;
    }

}