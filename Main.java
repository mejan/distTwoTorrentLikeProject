/**
 * Created by mejan on 2016-04-18.
 */

import Chord.*;
import Server.SuperNodeImpl;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import java.util.ArrayList;
public class Main {

    public static void main(String[] args)  {
        try{
            SuperNodeImpl superNode = new SuperNodeImpl(5000);
            ArrayList<NodeImpl> nodes = new ArrayList<>();
            for(int port = 8000; port < 8500; port++){
                Thread.sleep(100);
                NodeImpl node = new NodeImpl(port);
                Chord.join(node);
                nodes.add(node);
            }
            System.out.println("Finger taable: ");


            for(NodeImpl n : nodes){

                for(Finger f : n.getFingerTable()){
                    System.out.println("FingerId: " + f.getId() + '\t' + " FingerNodeId: " + f.getIdNode());
                }
                System.out.println("\n\n");
            }




        }catch(Exception e){
            e.printStackTrace();
            //System.err.println(e.getStackTrace());

            System.exit(0);
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