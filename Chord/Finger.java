package Chord;

/**
 * Created by mejan on 2016-04-22.
 */
public class Finger {
    private IdNode node;
    private int id;

    Finger(int toId, IdNode toNode){
        id = toId;
        node = toNode;
    }

    public IdNode getIdNode(){
        return node;
    }

    public int getId(){
        return id;
    }

    public void setId(int toSet){id = toSet; }

    public void setIdNode(IdNode toNode){node = toNode;}

}
