package Chord;

/**
 * Created by mejan on 2016-04-22.
 */
public class Finger {
    private IdNode node;
    private int id;

    Finger(int toId, IdNode toNode){
        id = toId;
        node = new IdNode(toNode);
    }

    public IdNode getNode(){
        return node;
    }

    public int getId(){
        return id;
    }
}
