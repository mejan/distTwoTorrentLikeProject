package Chord;


import Communication.RMIInputStream;
import Communication.RMIInputStreamImpl;
import Communication.RMIOutputStream;
import Communication.RMIOutputStreamImpl;

import java.io.*;
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
    private FileTable fileTable;

    public int nHops = 0;
    public int nSearches = 0;

    private File downloads;
    private File uploads;
    private Trust trust;


    public NodeImpl(int port, File downloads, File uploads) throws NoSuchAlgorithmException, RemoteException, UnknownHostException, AlreadyBoundException, MalformedURLException {
        this.idNode = new IdNode(InetAddress.getLocalHost().getHostAddress(), port);
        this.fingerTable = new ArrayList<>(Hash.HASH_LENGTH);
        this.fileTable = new FileTable(new HashMap<Integer, ArrayList<Node>>());

        //init all indices
        for(int i = 0; i < Hash.HASH_LENGTH; i++){
            setFinger(i, new Finger((this.getId() + (int)Math.pow(2, i)) % (int)Math.pow(2, Hash.HASH_LENGTH), null));
        }
        register(port); //try register
        if(!downloads.isDirectory() || !uploads.isDirectory()) throw new RuntimeException("Not a directory specified as upload or download path");
        this.downloads = downloads;
        this.uploads = uploads;
        this.trust = new Trust(this);
    }
    public File getUploads() throws RemoteException {
        return uploads;
    }
    public File getDownloads() {
        return downloads;
    }
    public int getId(){
        return this.getIdNode().getId();
    }
    public int getPort(){
        return this.getIdNode().getPort();
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

        while(!isBetweenSuccesor(id, node, node.getSuccessor())){
            node = node.findClosestPrecedingFinger(id);
            ++nHops;
        }

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
    /*---------------filetable operations---------------*/

    public void addEntryFileTable(int fileId, final Node owner) throws RemoteException {
        this.fileTable.add(fileId, owner);

    }

    public void printFileTable() throws RemoteException {
        this.fileTable.print();
    }

    public Node getNodeFileTable(int fileId){
        return this.fileTable.get(fileId);
    }

    public void mergeFileTable(FileTable mergeWithFileTable) throws RemoteException{
        this.fileTable.merge(mergeWithFileTable);
    }

    public void moveFileTable(Node newOwner) throws RemoteException {
        if(!fileTable.isEmpty()){

            HashMap<Integer,ArrayList<Node>> fileTableToMove = new HashMap<>();
            Iterator it = fileTable.getEntryIterator();

            while(it.hasNext()){
                Map.Entry entry = (Map.Entry)it.next();
                int fileId = (int)entry.getKey();
                if(isBetweenSuccesor(fileId, this, newOwner)){
                    fileTableToMove.put(fileId, (ArrayList)entry.getValue());
                    it.remove();

                }

            }
            if(!fileTableToMove.isEmpty()){
                newOwner.mergeFileTable(new FileTable(fileTableToMove));
            }

        }
    }


    /*---------------END filetable operations---------------*/

    /*---------------BEGIN stream operations----------------*/
    @Override
    public OutputStream getOutputStream(File file) throws FileNotFoundException, RemoteException {
        return new RMIOutputStream(new RMIOutputStreamImpl(new FileOutputStream(file), getPort()));
    }

    @Override
    public InputStream getInputStream(File file) throws FileNotFoundException, RemoteException {
        file = new File(getUploads(),file.toString());
        return new RMIInputStream(new RMIInputStreamImpl(new FileInputStream(file), getPort()));
    }

    public void copy(InputStream in, OutputStream out){
        FileUtils.copy(in, out);
    }

    public void download(Node owner, File src) throws FileNotFoundException, RemoteException, MalformedURLException, NotBoundException {
            //Destination file
        File dest = new File(downloads, src.getName());
        long time = System.currentTimeMillis();
        copy(owner.getInputStream(src), new FileOutputStream(dest));
        time = (System.currentTimeMillis() - time) / 1000;
        ++nSearches;

    }
    /*---------------END stream operations----------------*/

    /*---------------BEGIN trust operations---------------*/
    public double getNodeRating(Node neighbor) throws RemoteException{
        return trust.getNodeRating(neighbor);
    }

    public boolean isTrusted(final Node ownerId) throws RemoteException, NotBoundException, MalformedURLException {
        return trust.isTrusted(ownerId);
    }

    /*---------------END trust operations-----------------*/
    public double getNumberOfhops() {
        if(nSearches == 0) return 0.0;
        return (double)nHops / nSearches;
    }
    public double getPrevStartValue(){
        return trust.getPrevStartValue();
    }
    public int getNumberOfSearches(){
        return nSearches;
    }
    public int getLengthOfFileTable(){
        return this.fileTable.size();
    }


}