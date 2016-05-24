package Chord;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by heka1203 on 2016-05-04.
 */

public class FileTable implements Serializable {
    private HashMap<Integer, ArrayList<Node>> fileTable;
    public FileTable(HashMap<Integer, ArrayList<Node>> fileTable){
        this.fileTable = fileTable;
    }
    public void add(int fileId, final Node owner){
        synchronized (fileTable){
            if(!fileTable.containsKey(fileId)){ //List not init.
                ArrayList<Node> nodeList = new ArrayList<Node>(){{ add(owner);}};
                fileTable.put(fileId, nodeList);
            } else {
                fileTable.get(fileId).add(owner);
            }
        }

    }
    public void print() throws RemoteException {
        for(Map.Entry entry : fileTable.entrySet()){
            int fileId = (int)entry.getKey();
            ArrayList<Node> nodes = (ArrayList<Node>)entry.getValue();
            System.out.println("\nFile id: " + fileId);
            for(Node node : nodes){
                System.out.println("\nNode id: " + node.getId());
            }
        }
    }
    public Node get(int fileId){
        if(fileTable.containsKey(fileId)){
            System.out.println("Size of filetable: " + fileTable.get(fileId).size());
            ArrayList<Node> nodes = fileTable.get(fileId);
            return nodes.get(ThreadLocalRandom.current().nextInt(nodes.size()));
        }

        return null;
    }
    public void merge(FileTable mergeWithFileTable){
        merge(mergeWithFileTable);
    }
    public void merge(HashMap<Integer, ArrayList<Node>> mergeWithFileTable){
        synchronized (fileTable){
            fileTable.putAll(mergeWithFileTable);
        }
    }

    public boolean isEmpty(){
        return fileTable.isEmpty();
    }

    public HashMap<Integer, ArrayList<Node>> getFileTable(){
        return this.fileTable;
    }

    public Iterator getEntryIterator(){
        return fileTable.entrySet().iterator();
    }


}
