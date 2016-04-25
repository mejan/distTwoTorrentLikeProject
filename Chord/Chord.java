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
            IdNode closestNode = superNode.getClosestNode(node.getIdNode());
            superNode.addNode(node.getIdNode());
            //TODO:
            // fix that we change the fingerIdNode of each fingerId
            if(closestNode == null){
                System.out.println("We are alone");
                for(int i = 0; i < Hash.HASH_LENGTH; i++){
                    IdNode idNode = node.getIdNode();
                    node.setFingerIdSuccessor(i, idNode);

                }
                node.setPredecessor(node.getIdNode());
                node.setSuccessor(node.getIdNode());
                /*for(Finger f : node.getFingerTable()){
                    System.out.println(f.getId());
                }*/
            }


            else{
                //System.out.println("We are node: " + node.getIdNode().toString());
                //System.out.println("Found node n' to connect via: " + closestNode.toString());

                node.initFingerTable(closestNode);
                node.updateOthers();

            }


        } catch (NotBoundException e) {
            System.err.println("There is no super node.......");
        }
    }

    public static int getFingerIdOf(int id, int i){
        return (id+ (int)Math.pow(2, i)) % (int)Math.pow(2, Hash.HASH_LENGTH);
    }
}