package Chord; /**
 * Created by heka1203 on 2016-04-20.
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Chord {
   /* private static final String serverIp = "localhost";
    private static final int serverPort = 10005;

    public static void join(Chord.NodeImpl node) throws IOException {
        Socket socket = new Socket(InetAddress.getByName(serverIp), serverPort);
        try(ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream())){

            out.writeObject(node.getIdNode());
            List<IdNode> nodeList = (ArrayList<IdNode>)in.readObject();

            IdNode successorId = findSuccessor(node.getIdNode(), nodeList);
            IdNode predecessorId = findPredecessor(node.getIdNode(), nodeList);
            node.setSuccessor(successorId);
            node.setPredecessor(predecessorId);
        } catch (ClassNotFoundException e) {
            System.err.println("Could not typecast read object to list");
            if(socket != null)
                socket.close();
        } finally{
            if(socket != null)
                socket.close();
        }
    }

    public static IdNode findSuccessor(IdNode idNode, List<IdNode> nodeList){
        int index = Collections.binarySearch(nodeList, idNode);
        if(index >= 0 && (nodeList.size()-1) > index){
            System.out.println("This node found (findSuccessor)");
            return nodeList.get(index+1);
        }
        return null;
    }

    public static IdNode findPredecessor(IdNode idNode, List<IdNode> nodeList){
        int index = Collections.binarySearch(nodeList, idNode);
        if(index > 0){
            System.out.println("This node found (findPredecessor)");
            return nodeList.get(index-1);
        }
        return null;
    }*/
}