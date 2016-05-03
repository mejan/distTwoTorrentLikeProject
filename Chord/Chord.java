package Chord;
/**
 * Created by heka1203 on 2016-04-20.
 */

import Server.SuperNode;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.*;


public class Chord {
    private static String superNodeIp;
    private static int superNodePort = 5000;
    private static SuperNode superNode;

    public static void join(NodeImpl node) throws IOException {
        superNodeIp = InetAddress.getLocalHost().getHostAddress();
        //System.out.println("Ip: "+ superNodeIp);
        try {
            superNode = (SuperNode)Naming.lookup("//" + superNodeIp + ":" + superNodePort + "/nodeList");
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

    public static void put(File file, Node node) throws RemoteException, NotBoundException, MalformedURLException {
        java.util.List<File> files = FileUtils.splitFile(file);

        for(File f : files){
            int fileId = Integer.parseInt(f.toString());
            Node successor = node.findSuccessor(fileId);
            successor.addNodeFileTable(fileId, node);
                //successor.
        }
        if(superNode != null){ //add whole filename and its  chunks to supernode list
            superNode.addFile(file, files);
        }
    }

    public static void listAvailableFiles(Node node) throws RemoteException {
        HashMap fileTable = superNode.getFileTable();
        Iterator it = fileTable.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry) it.next();
            String filename = (String)pair.getKey();
            ArrayList<File> files = (ArrayList<File>)pair.getValue();
            System.out.println("File is: "+filename);
            for(File file : files){
                System.out.println("id: "+file.getName());
            }
            System.out.println("\n\n");
        }

    }


}