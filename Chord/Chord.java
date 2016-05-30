package Chord;
/**
 * Created by heka1203 on 2016-04-20.
 */

import Server.FileList;
import Server.SuperNode;

import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;



public class Chord {

    public static final String superNodeIp;
    static{
        try{
            superNodeIp = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new HeadlessException(e.getMessage());
        }

    }
    public static final int superNodePort = 7000;
    private static int nTrusted = 0;
    private static int nUntrusted = 0;
    private static int nPuts = 0;



    public static void join(NodeImpl node) throws IOException {

        try {
            SuperNode superNode = (SuperNode)Naming.lookup("//" + superNodeIp + ":" + superNodePort + "/superNode");
            IdNode closestNodeId = superNode.getClosestNode(node.getIdNode());
            superNode.addNode(node.getIdNode());
            if(closestNodeId == null){
                for(int i = 0; i < node.getFingerTable().size(); i++){
                    node.setFingerNode(i, node);
                }
                node.setPredecessor(node);
                //node.setSuccessor(node);
            }


            else{
                Node closestNode = node.lookupNode(closestNodeId);
                if(closestNode == null){ throw new RuntimeException("closest node is null?"); }
                node.initFingerTable(closestNode);
                node.updateOthers();
                Node successor = node.getSuccessor();
                successor.moveFileTable(node);
            }


        } catch (NotBoundException e) {
            System.err.println("(Join) There is no super node.......");
        }
    }

    //Putting a chunk.
    public static void putFile(File file, Node node) throws RemoteException, NotBoundException, MalformedURLException {
        int fileId = Integer.parseInt(file.getName());
        Node successor = node.findSuccessor(fileId);
        successor.addEntryFileTable(fileId, node);
    }

    //Putting a "file"
    public static void putFiles(File file, Node node) throws RemoteException, NotBoundException, MalformedURLException {
        ArrayList<File> files = FileUtils.splitFile(file, node.getUploads());
        for(File f : files){
            putFile(f, node);
            System.out.println(f.getName());
        }
        SuperNode superNode = (SuperNode)Naming.lookup("//" + superNodeIp + ":" + superNodePort + "/superNode");
        if(superNode != null){ //add whole filename and its  chunks to supernode list
            superNode.addFile(file.getName(), files);
        }

    }

    /*TODO: 1. Do some tests
          2. Implement file transfer*/

    public static Node lookup(File file, Node node) throws RemoteException, NotBoundException, MalformedURLException {
        int fileId = Integer.parseInt(file.getName());
        if(node.getNodeFileTable(fileId) != null) return node;
        Node successor = node.findSuccessor(fileId);
        Node fileOwner = successor.getNodeFileTable(fileId);

        return fileOwner;
    }

    public static void downloadFile(File file, NodeImpl self) throws FileNotFoundException, RemoteException, MalformedURLException, NotBoundException, InterruptedException {

        Node fileOwner = lookup(file, self);
        if(fileOwner == self) return;
        if(self.isTrusted(fileOwner) /*true*/) { // just for simulation

            ++nTrusted;
            //trustLog.append("Node is TRUSTED:" + fileOwner.getId());
            self.download(fileOwner, file);
            putFile(file, self);
            FileUtils.copy(new FileInputStream(new File(self.getDownloads(), file.getName())), new FileOutputStream(new File(self.getUploads(), file.getName())));
        } else{
            ++nUntrusted;
            //trustLog.append("!!!Node is NOT TRUSTED!!!: "+ fileOwner.getId());
        }
    }
    public static void downloadFiles(File file, NodeImpl self) throws RemoteException, FileNotFoundException, MalformedURLException, NotBoundException, InterruptedException {
        SuperNode superNode = (SuperNode)Naming.lookup("//" + superNodeIp + ":" + superNodePort + "/superNode");

        FileList fileList = superNode.getFileList();
        ArrayList<File> files = fileList.find(file.getName());

        ArrayList<File> localFiles = new ArrayList<>();
        System.out.println("Received order: ");
        for(File f : files){
            System.out.println(f.getName());
            downloadFile(f, self);

            localFiles.add(new File(self.getDownloads(), f.getName()));
        }
        FileUtils.mergeFiles(localFiles, new File(self.getDownloads(), file.getName()));

    }

    public static FileList getAvailableFiles() throws RemoteException, MalformedURLException, NotBoundException {

        SuperNode superNode = (SuperNode)Naming.lookup("//" + superNodeIp + ":" + superNodePort + "/superNode");
        return superNode.getFileList();

    }
    public static int getNumberOfNodesInChord() throws RemoteException, NotBoundException, MalformedURLException {
        SuperNode superNode = (SuperNode)Naming.lookup("//" + Chord.superNodeIp + ":" + Chord.superNodePort + "/superNode");
        return superNode.getNumberOfNodes();
    }
    public static int getNumberOfChunks(String filename) throws RemoteException, NotBoundException, MalformedURLException {
        FileList fileList = getAvailableFiles();
        return fileList.getNumberOfChunks(filename);
    }
    public static double getTrustedRatio(){
        return (double)nTrusted / (nTrusted + nUntrusted);
    }



}