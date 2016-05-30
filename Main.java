/**
 * Created by mejan on 2016-04-18.
 */

import Chord.*;
import Server.FileList;
import Server.SuperNodeImpl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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



    public static NodeImpl findNode(int id){
        for(NodeImpl node : nodes)
            if(node.getId() == id) return node;
        return null;
    }

    public static void createDir(File file){
        if(!file.exists())
            file.mkdir();
    }

    public static void main(String[] args) throws IOException, NotBoundException, NoSuchAlgorithmException, InterruptedException {
        SuperNodeImpl superNode = new SuperNodeImpl();

        Scanner input = new Scanner(System.in);
        System.out.println("Specify how many nodes to create");

        int numNodes = input.nextInt();
        System.out.println("Specify path where the node folders should be created: "); ///Users/heka1203/Documents/testingdist/
        File nodesDir = new File(input.next());
        for(int i = 0; i < numNodes; i++){
            File nodeDir = new File(nodesDir, "node" + i);
            createDir(nodeDir);

            int port = 11000 + i;
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
        String message = "\n1. List nodes available in chord ring.\n" +
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


                    FileList files = Chord.getAvailableFiles();
                    long runtime = 30 * 1000;
                    long currentTime = System.currentTimeMillis();
                    boolean done = false;
                    final int nChunks = 9;
                    while(!done){
                        Random random = new Random();
                        NodeImpl node = nodes.get(random.nextInt(nodes.size()));
                        //take 9 chunks at a time

                        for(int i = 0; i < nChunks; i++){
                            File chunk = files.getRandomChunk();
                            Chord.downloadFile(chunk, node);
                        }
                        done = (Math.abs(currentTime - System.currentTimeMillis()) >= runtime);
                    }
                    /*int numberOfChunks = Chord.getNumberOfChunks("test.jpg");
                    for(NodeImpl n : nodes){
                        System.out.println("Responsible for # of chunks: " + n.getLengthOfFileTable());
                    }*/
                    /*int nNodes = nodes.size();
                    String simInfo = "Simulation time: " + runtime + "\n"+
                            "Number of nodes: " + nNodes + "\n"+
                            "Number of chunks downloaded each time per node: " + nChunks + "\n";
                    HopLog hopLog = new HopLog(new File("/home/mejan/Documents/" + String.valueOf(nNodes) + ".txt"));
                    double totalSearches = 0;
                    double totalHops = 0;
                    double coutedhops = 0;
                    for(NodeImpl n : nodes){
                        double nHops = n.getNumberOfhops();
                        double nSearches = n.getNumberOfSearches();
                        if(nHops != 0.0 && nSearches != 0.0){
                            coutedhops += 1;
                            hopLog.append(String.valueOf(nHops));
                            totalHops += nHops;
                            totalSearches += nSearches;

                        }

                    }
                    double avgNHops = totalHops / coutedhops;

                    simInfo += "Avg. Number of searches per node: " + (double)totalSearches / nNodes + "\n" +
                            "Total number of hops: " + totalHops + "\n" +
                            "Total number of searches: " + totalSearches + "\n";
                    simInfo += "Avg. Number of hops: " + avgNHops;

                    hopLog.append(simInfo);
                    hopLog.write();*/
                    double trueTrust =0;
                    for(NodeImpl node: nodes){
                        if(node.getPrevStartValue() >= 0.5){
                            trueTrust += 1;
                        }
                    }
                    System.out.println("Real trust procentage: " + trueTrust/Chord.getNumberOfNodesInChord());
                    System.out.println("Trusted ratio: " + Chord.getTrustedRatio());
                }
                default:
                    break loop;
            }
            System.out.println(message);
        }

        System.exit(0);


    }
}