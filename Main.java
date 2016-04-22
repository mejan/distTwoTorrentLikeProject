/**
 * Created by mejan on 2016-04-18.
 */

import Chord.*;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;


public class Main {

    public static void main(String[] args)  {
        try{
            NodeImpl node = new NodeImpl(30000);
            NodeImpl node2 = new NodeImpl(15000);
            //NodeImpl n = (NodeImpl)LocateRegistry.getRegistry(InetAddress.getLocalHost().getHostAddress(), 10005);
            //Node n2 = (Node)LocateRegistry.getRegistry(InetAddress.getLocalHost().getHostAddress(), 30000);
           // System.out.println(n.toString());
            //System.out.println(n2.toString());
        }catch(Exception e){
            System.out.println("err" + e.getMessage());
            System.exit(0);
        }

        System.exit(0);
    }

}