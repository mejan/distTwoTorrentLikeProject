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
import java.util.*;

/**
 * Created by heka1203 on 2016-04-20.
 */

public class NodeImpl extends UnicastRemoteObject implements Node {
    private Node successor;
    private Node predecessor;
    private IdNode idNode;
    private ArrayList<Finger> fingerTable;
    private HashMap<Integer, ArrayList> fileTable;
    public int nHops;

    public NodeImpl(int port) throws NoSuchAlgorithmException, RemoteException, UnknownHostException, AlreadyBoundException, MalformedURLException {
        this.idNode = new IdNode(InetAddress.getLocalHost().getHostAddress(), port);
        this.fingerTable = new ArrayList<>(Hash.HASH_LENGTH);
        this.fileTable = new HashMap<Integer, ArrayList>();

        //init all indices
        for(int i = 0; i < Hash.HASH_LENGTH; i++){
            setFinger(i, new Finger((this.getId() + (int)Math.pow(2, i)) % (int)Math.pow(2, Hash.HASH_LENGTH), null));
        }
        register(port); //try register
    }

    public int getId(){
        return this.getIdNode().getId();
    }

    private void register(int port) throws RemoteException, AlreadyBoundException, MalformedURLException {
        LocateRegistry.createRegistry(port);
        //System.setProperty("java.rmi.server.hostname", idNode.getIp()); MAY BE NEEDED FOR LATER!!!
        Naming.bind(getLookupUrl(idNode), this);

    }

    private String getLookupUrl(IdNode idNode){
        return "//" + idNode.getIp() + ":" + idNode.getPort() + "/" + idNode.toString();
    }

    public Node lookupNode(IdNode idNode) throws RemoteException, NotBoundException, MalformedURLException {
        return (Node)Naming.lookup(getLookupUrl(idNode));
    }

    private void setFinger(int index, Finger finger){
        fingerTable.add(index, finger);
    }
    
    private Finger getFinger(int index){
        return fingerTable.get(index);
    }

    public Node getFingerNode(int index){
        return getFinger(index).getNode();
    }

    public int getFingerId(int index){
        return getFinger(index).getId();
    }

    public void setFingerNode(int index, Node idNode){
        getFinger(index).setIdNode(idNode);
    }

    public void initFingerTable(Node node) throws RemoteException, NotBoundException, MalformedURLException {
        //Setting self to this
        Node self = this;
        //Setting the first index node in finger table.
        int firstId = (getId() + (int)Math.pow(2, 0)) % (int)Math.pow(2, fingerTable.size());
        setFingerNode(0, node.findSuccessor(firstId));

        //setting this nodes predecessor to successors predecessor
        setPredecessor(getSuccessor().getPredecessor());
        //Setting the successors predecessor to this node.
        getSuccessor().setPredecessor(this);

        //Fixing the rest of the finger table (without changing others)
        for(int i = 0; i < fingerTable.size()-1; i++){
            // check if fingerIndexID+1 is in interval n<= fingerIndexId+1 < fingerIndexNode.
            if(isExceedingFinger(getFingerId(i+1), self, getFingerNode(i))){
                //Set successor to fingerID+1 as fingerNode.
                //System.out.println("true in inittable");
                setFingerNode(i+1, getFingerNode(i));
            } else{
                //Find what will be fingerID+1's fingerNode.
                setFingerNode(i+1, node.findSuccessor(getFingerId(i+1)));

            }
        }
    }

    private boolean isExceedingFinger(int id, Node first, Node last) throws RemoteException {

        if(first.getId() < last.getId()){
            return (id >= first.getId() && id < last.getId());
        } else{
            return (id >= first.getId() || id < last.getId());
        }
    }

    public ArrayList<Finger> getFingerTable(){
        return fingerTable;
    }

    public Node findSuccessor(int id) throws RemoteException, NotBoundException, MalformedURLException {
        Node node = findPredecessor(id);
        return node.getSuccessor();
    }

    public Node findPredecessor(int id) throws RemoteException, NotBoundException, MalformedURLException {
        Node node = this;
        int hops = 0;
        while(!isBetweenSuccesor(id, node, node.getSuccessor())){
            node = node.findClosestPrecedingFinger(id);
            ++hops;
        }
        nHops = hops;
        return node;
    }

    private boolean isBetweenSuccesor(int id, Node first, Node last) throws RemoteException {

        if(first.getId() < last.getId()){
            return (id > first.getId() && id <= last.getId());
        } else{
            return (id > first.getId() || id <= last.getId());
        }
    }

    public Node findClosestPrecedingFinger(int id) throws RemoteException {
        Node node = this;
        for(int i = fingerTable.size() - 1; i >= 0; i--){
            if(isPrecedingFinger(id, node, getFingerNode(i))){
                return getFingerNode(i);
            }
        }
        return node;
    }
    
    private boolean isPrecedingFinger(int last, Node first, Node check) throws RemoteException{

        if(first.getId() < last){
            return (check.getId() > first.getId() &&  check.getId() < last);
        } else {
            return (check.getId() > first.getId() || check.getId() < last);
        }
    }

    public void updateOthers() throws RemoteException, NotBoundException, MalformedURLException {
        Node self = this;
        int modValue = (int) Math.pow(2, fingerTable.size());
        for(int i = 0; i < fingerTable.size(); i++){
            int id = (getId() - (int)Math.pow(2, i));
            if(id < 0) {
                id += modValue;
            }
            id %= modValue;
            
            Node pred = getPredecessor().findPredecessor(id); //Not acording to psedocode
            pred.updateFingerTable(i, self);
        }
    }

    public void updateFingerTable(int index, Node node) throws RemoteException, NotBoundException, MalformedURLException {
        if(isIthFinger(node, getFingerId(index), getFingerNode(index))){ //Mejan specialare (isIthFinger is not acordning to pseudocode)
            setFingerNode(index, node);
            Node pred = getPredecessor();
            pred.updateFingerTable(index, node);
        }
    }

    private boolean isIthFinger(Node check, int first, Node last) throws RemoteException {//Node first, Node last) throws RemoteException {
        if (first < last.getId()) {
            return (check.getId() > first && check.getId() < last.getId());
        } else{
            return (check.getId() > first || check.getId() < last.getId());
        }
    }

    public IdNode getIdNode() {
        return this.idNode;
    }

    public Node getSuccessor() {
        return getFingerNode(0);
    }

    public Node getPredecessor() {
        return predecessor;
    }

    public void setSuccessor(Node successor) {
        if(successor != null)
            //System.out.println("Set successor: " + successor.toString());
            this.successor = successor;
    }

    public void setPredecessor(Node predecessor) {
        if(predecessor != null)
            //System.out.println("Set predecessor." + predecessor.toString());
            this.predecessor = predecessor;
    }

    public void addNodeFileTable(int fileId, final Node node) throws RemoteException {
        if(!fileTable.containsKey(fileId)){ //List not init.
            ArrayList<Node> nodeList = new ArrayList<Node>(){{ add(node);}};
            fileTable.put(fileId, nodeList);
        } else {
            fileTable.get(fileId).add(node);
        }
        printFileTable();
    }
    private void printFileTable() throws RemoteException {
        Iterator it = fileTable.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            int fileId = (int)pair.getKey();
            List<Node> nodeList = (List<Node>)pair.getKey();
            for(Node n : nodeList){
                System.out.format("File id:%d \t Node owner:%d", fileId, n.getId());
            }
        }
    }

    public Node getNodeFileTable(int fileId){
        if(fileTable.containsKey(fileId)) {
            ArrayList<Node> nodeList = fileTable.get(fileId);
            return nodeList.get((int) (Math.random() * nodeList.size()));
        }
        return null;
    }


    public void moveFileTable(Node newSucessor) throws RemoteException {
        if(!fileTable.isEmpty()){
            HashMap<Integer,ArrayList> toSend = new HashMap<Integer, ArrayList>();

            Iterator it = fileTable.entrySet().iterator();
            while(it.hasNext()){

                Map.Entry pair = (Map.Entry) it.next();
                int key = (int)pair.getKey();

                if(isBetweenSuccesor(key,this,newSucessor)){
                    ArrayList<Node> nodeList = new ArrayList<Node>((ArrayList)pair.getValue());
                    toSend.put(key,nodeList);
                    it.remove();
                }
            }
            if(!toSend.isEmpty())
                newSucessor.mergeFileTable(toSend);
        }
    }

    public void mergeFileTable(HashMap<Integer, ArrayList> mergeWith){
        fileTable.putAll(mergeWith);
    }
}