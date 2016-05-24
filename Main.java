/**
 * Created by mejan on 2016-04-18.
 */

import Chord.*;
import Server.SuperNodeImpl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static ArrayList<NodeImpl> nodes = new ArrayList<>();

    public static void listNodes(){
        System.out.println("List of available nodes:");
        for(Node node : nodes){
            try {
                System.out.println("Node with id: \t" + node.getId());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static void listFilesInChord(){
        try {
            Chord.listAvailableFiles();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }

    public static NodeImpl findNode(int id){
        for(NodeImpl node : nodes)
            if(node.getId() == id) return node;
        return null;
    }

    public static void createDir(File file){
        if(!file.exists())
            file.mkdir();
    }

    public static void main(String[] args) throws IOException, NotBoundException, NoSuchAlgorithmException {
        SuperNodeImpl superNode = new SuperNodeImpl();

        Scanner input = new Scanner(System.in);
        System.out.println("Specify how many nodes to create");

        int numNodes = input.nextInt();
        System.out.println("Specify path where the node folders should be created: "); ///Users/heka1203/Documents/testingdist/
        File nodesDir = new File(input.next());
        for(int i = 0; i < numNodes; i++){
            File nodeDir = new File(nodesDir, "node" + i);
            createDir(nodeDir);

            int port = (int)((Math.random() * (6000 - 3000) + 1) + 3000);
            File downloads = new File(nodeDir, "downloads");
            File uploads = new File(nodeDir, "uploads");
            createDir(downloads);
            createDir(uploads);
            try {
                NodeImpl node = new NodeImpl(port, downloads, uploads);
                Chord.join(node);
                nodes.add(node);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("Successfully created all nodes.");
        String message = "1. List nodes available in chord ring.\n" +
                "2. List files and file owner available in chord ring. \n" +
                "3. Upload file from a specific node.\n" +
                "4. Download file from a specific node. \n" +
                "5. Start auto sim \n";
        System.out.println(message);

        loop: while(input.hasNext()){
            int choice = input.nextInt();
            switch(choice){
                case 1:
                    listNodes();
                    break;
                case 2:
                    listFilesInChord();
                    break;
                case 3:{
                    System.out.println("Enter which node to upload from:");
                    Node node = findNode(input.nextInt());
                    System.out.println("Enter which file to upload"); ///Users/heka1203/Documents/testingdist/node1/uploads/phpfJVW9D.png
                    Chord.putFiles(new File(input.next()), node);
                    break;
                }

                case 4: {
                    System.out.println("Enter which node to download to:");
                    NodeImpl node = findNode(input.nextInt());
                    if(node == null) throw new RuntimeException("Node not found");
                    System.out.println("Enter which file id to download");
                    File file = new File(input.next());
                    Chord.downloadFile(file, node);
                    break;
                }
                case 5: {

                }
                default:
                    break loop;
            }
            System.out.println(message);
        }

        System.exit(0);


    }
}

/*Enumeration<NetworkInterface> nInterfaces = NetworkInterface.getNetworkInterfaces();

while (nInterfaces.hasMoreElements()) {
    Enumeration<InetAddress> inetAddresses = nInterfaces.nextElement().getInetAddresses();
    while (inetAddresses.hasMoreElements()) {
        String address = inetAddresses.nextElement().getHostAddress();
        if(address.contains("192.168"))
            System.out.println(address);
    }
}*/