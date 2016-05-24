package Communication;


import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by heka1203 on 2016-05-05.
 */
public interface RMIOutputStreamInterf extends Remote {
    public void write(int b) throws IOException;
    public void write(byte[] buf, int offset, int length) throws IOException;
    public void close() throws IOException;

}
