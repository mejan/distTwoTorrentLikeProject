/**
 * Created by mejan on 2016-04-18.
 */

import Chord.*;
import Server.SuperNodeImpl;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;


public class Main {

    public static void main(String[] args)  {
        try{
            SuperNodeImpl superNode = new SuperNodeImpl(5000);
            NodeImpl node = new NodeImpl(30000);
            NodeImpl node2 = new NodeImpl(15000);
            Chord.join(node);
            Chord.join(node2);



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