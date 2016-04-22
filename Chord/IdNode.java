package Chord;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;

/**
 * Created by mejan on 2016-04-22.
 */
public class IdNode implements Serializable { /*, Comparable<IdNode>, Comparator<IdNode> */


    private String ip;
    private int port;
    private int id;


    public IdNode(String ip, int port){
        this.ip = ip;
        this.port = port;
        try {
            id = Hash.hash(ip + port);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public IdNode(IdNode toSet){
        ip = toSet.getIp();
        port = toSet.getPort();
        id = toSet.getId();
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public int getId() {
        return id;
    }

    public boolean equals(Object o){
        if(o == this) return true;
        else if(o == null) return false;
        else return this.toString().equals(o.toString());
    }

    @Override
    public String toString(){
        return String.valueOf(id);
    }

    //Behövs detta???
    /*
    @Override
    public int compare(IdNode o1, IdNode o2) {
        return o1.toString().compareToIgnoreCase(o2.toString());
    }
    @Override
    public int compareTo(IdNode o) {
        return this.toString().compareToIgnoreCase(o.toString());
    }*/
}
