package Server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import Chord.*;
/**
 * Created by heka1203 on 2016-04-22.
 */
public class NodeList implements Serializable {
    public NodeList(){
        nodeList = new ArrayList<>((int)Math.pow(2, 16));
    }

    public void addNode(IdNode idNode){
        if(idNode == null) throw new RuntimeException("Cannot add null node");
        synchronized (nodeList){
            nodeList.add(idNode);
            //System.out.println("Added new node "+idNode.toString());
        }
        sortList();
    }


    public void removeNode(IdNode idNode){
        if(idNode == null) throw new RuntimeException("Cannot remove null node");
        synchronized(nodeList){
            for(int i = 0; i < nodeList.size(); i++){
                if(nodeList.get(i).equals(idNode))
                    nodeList.remove(i);
            }
        }
    }

    private void sortList(){
        synchronized (nodeList){
            Collections.sort(nodeList);
        }
    }

    public IdNode getClosestNode(IdNode idNode){
        if(idNode == null) throw new RuntimeException("Cannot search for null node");
        if(nodeList.isEmpty()) return null;

        synchronized (nodeList){
            int closestIndex = Collections.binarySearch(nodeList, idNode);
            if(closestIndex > 0)
                return nodeList.get(closestIndex);
            else
                return nodeList.get(0);

        }
    }

    private ArrayList<IdNode> nodeList;
}