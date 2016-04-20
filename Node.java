import DHash.IdNode;

/**
 * Created by heka1203 on 2016-04-20.
 */

public class Node {
    private IdNode idNode;

    public Node(int port){
        idNode = new IdNode("localhost", port);
        //contactserver for list of nodes
        //set successor and/or predecessor if list is not empty
    }
}