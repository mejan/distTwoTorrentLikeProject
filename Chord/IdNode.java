package Chord;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;

/**
 * Created by mejan on 2016-04-22.
 */
public class IdNode implements Serializable, Comparable<IdNode>, Comparator<IdNode>  {


    private String ip;
    private int port;
    private final int id;


    public IdNode(String ip, int port) throws NoSuchAlgorithmException{
        this.ip = ip;
        this.port = port;
        this.id = Hash.hash(ip + port);
    }

    //??
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

    @Override
    public int hashCode(){
        return Integer.valueOf(ip) + port  + id;
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof IdNode)) return false;
        if(o == this) return true;
        return this.getId() == ((IdNode) o).getId();
    }

    @Override
    public String toString(){
        return String.valueOf(id);
    }


    @Override
    public int compare(IdNode o1, IdNode o2) {
        return Integer.compare(o1.getId(), o2.getId());
    }

    @Override
    public int compareTo(IdNode o) {
        return Integer.compare(this.getId(), o.getId());
    }


    //Behövs detta???

}
