package DHash;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by heka1203 on 2016-04-20.
 */
public class IdNode {


    private String ip;
    private int port;
    private String id;


    public IdNode(String ip, int port){
        this.ip = ip;
        this.port = port;
        try {
            id = Hash.hash(ip + port);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }
    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public String getId() {
        return id;
    }
    public boolean equals(Object o){
        if(o == this) return true;
        else
            return this.toString().equals(o.toString());

    }
    @Override
    public String toString(){
        return id;
    }

}
