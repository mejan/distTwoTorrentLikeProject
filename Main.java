/**
 * Created by mejan on 2016-04-18.
 */

import Chord.*;

import java.io.File;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException, NotBoundException, NoSuchAlgorithmException {
        /*ArrayList<NodeImpl> nodes = new ArrayList<>();
        
        try{
            SuperNodeImpl superNode = new SuperNodeImpl(5000);
            //for(int port = 8000; port < 8015; port++){ //2 nodes
            //for(int port = 8500; port > 8450; port--){
            for(int port = 8000; port < 8007; port++){
                Thread.sleep(100);
                int rnd = (int)(Math.random() * (10000 - 6000) + 1) + 6000;
                NodeImpl node = new NodeImpl(port);
                Chord.join(node);
                nodes.add(node);
            }

        }catch(Exception e){
            e.printStackTrace();

            System.exit(0);
        }
        for(NodeImpl n : nodes){

            if(n.getId() == 57763){
                Node tmp = n.findSuccessor(40465);
                System.out.println("Hops: " + n.nHops);
                System.out.println("Hittade successor: "+tmp.getId()+" Det vi vill ha är: 15701");
            }
        }*/

        Scanner input = new Scanner(System.in);
        String in = input.nextLine();
        //FileUtils.splitFile(in);
        FileUtils.getListOfFiles(new File(in));
        /*String arr[] = FileUtils.createChunks(in);
        System.out.println("Enter new path and name: ");
        String out = input.nextLine();
        FileUtils.createFileFromChunks(in, arr, out);
        for(String s : arr){
            System.out.println("Chunk name: " + s);
        }*/

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