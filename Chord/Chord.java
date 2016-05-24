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


    public static void join(NodeImpl node) throws IOException {

        try {
            SuperNode superNode = (SuperNode)Naming.lookup("//" + superNodeIp + ":" + superNodePort + "/superNode");
            IdNode closestNodeId = superNode.getClosestNode(node.getIdNode());
            superNode.addNode(node.getIdNode());
            if(closestNodeId == null){
                //System.out.println("I am all alone, where have you hidden my mother :'(. My Name is: "+node.getId());
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
        //successor.

        //listAvailableFiles();
    }

    //Putting a "file"
    public static void putFiles(File file, Node node) throws RemoteException, NotBoundException, MalformedURLException {
        ArrayList<File> files = FileUtils.splitFile(file, node.getUploads());
        System.out.println("Original order:");
        for(File f : files){
            putFile(f, node);
            System.out.println(f.getName());
        }
        SuperNode superNode = (SuperNode)Naming.lookup("//" + superNodeIp + ":" + superNodePort + "/superNode");
        if(superNode != null){ //add whole filename and its  chunks to supernode list
            superNode.addFile(file.getName(), files);
        }
        System.out.println("Node with id: " + node.getId() + " added file: " + file.getName());
    }

    /*TODO: 1. Do some tests
          2. Implement file transfer*/

    public static Node lookup(File file, Node node) throws RemoteException, NotBoundException, MalformedURLException {
        int fileId = Integer.parseInt(file.getName());
        Node successor = node.findSuccessor(fileId);
        Node fileOwner = successor.getNodeFileTable(fileId);
        System.out.println("File owner of file id: " + fileId + " is node with id: " + fileOwner.getId());
        return fileOwner;
    }

    public static void downloadFile(File file, NodeImpl self) throws FileNotFoundException, RemoteException, MalformedURLException, NotBoundException {
        Node fileOwner = lookup(file, self);
        if(self.isTrusted(fileOwner.getId())) {
            System.out.println("This node is trusted: " + fileOwner.getId());
            self.download(fileOwner, file);
            putFile(file, self);
            FileUtils.copy(new FileInputStream(new File(self.getDownloads(), file.getName())), new FileOutputStream(new File(self.getUploads(), file.getName())));
        } else{
            System.out.println("This node is not trusted!!!!!!: "+ fileOwner.getId());
        }
    }
    public static void downloadFiles(File file, NodeImpl self) throws RemoteException, FileNotFoundException, MalformedURLException, NotBoundException {
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

        //call on put the file togather..?
    }

    public static FileList getAvailableFiles() throws RemoteException, MalformedURLException, NotBoundException {

        SuperNode superNode = (SuperNode)Naming.lookup("//" + superNodeIp + ":" + superNodePort + "/superNode");
        return superNode.getFileList();


        /*Iterator it = fileTable.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry) it.next();
            String filename = (String)pair.getKey();
            ArrayList<File> files = (ArrayList<File>)pair.getValue();
            System.out.println("File is: " + filename);
            for(File file : files){
                System.out.println("id: " + file.getName());
            }
            System.out.println("\n\n");
        }*/
    }


}