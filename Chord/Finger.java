package Chord;

/**
 * Created by mejan on 2016-04-22.
 */
public class Finger {
    private Node node;
    private int id;

    Finger(int toId, Node toNode){
        id = toId;
        node = toNode;
    }

    public Node getNode(){
        return node;
    }

    public int getId(){
        return id;
    }

    public void setId(int toSet){
        id = toSet;
    }

    public void setIdNode(Node toNode){
        node = toNode;
    }

}
