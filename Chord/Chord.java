package Chord;
/**
 * Created by heka1203 on 2016-04-20.
 */

import Server.SuperNode;
import java.io.IOException;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.NotBoundException;


public class Chord {
    private static String superNodeIp;
    private static int superNodePort = 5000;

    public static void join(NodeImpl node) throws IOException {
        superNodeIp = InetAddress.getLocalHost().getHostAddress();
        //System.out.println("Ip: "+ superNodeIp);
        try {
            SuperNode superNode = (SuperNode)Naming.lookup("//" + superNodeIp + ":" + superNodePort + "/nodeList");
            IdNode closestNodeId = superNode.getClosestNode(node.getIdNode());
            superNode.addNode(node.getIdNode());
            if(closestNodeId == null){

                for(int i = 0; i < node.getFingerTable().size(); i++){
                    node.setFingerNode(0, node);
                }
                node.setPredecessor(node);
                node.setSuccessor(node);
            }


            else{
                Node closestNode = node.lookupNode(closestNodeId);
                node.initFingerTable(closestNode);
                node.updateOthers();
                //TODO: Fix the update.
            }
            System.out.println("\n\n");

        } catch (NotBoundException e) {
            System.err.println("(Join) There is no super node.......");
        }
    }

}