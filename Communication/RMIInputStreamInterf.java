package Communication;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by heka1203 on 2016-05-05.
 */
public interface RMIInputStreamInterf extends Remote {
    public byte[] readBytes(int len) throws IOException;
    public int read() throws RemoteException;
    public void close() throws IOException;


}
