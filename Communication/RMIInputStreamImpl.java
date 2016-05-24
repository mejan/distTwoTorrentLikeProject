package Communication;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by heka1203 on 2016-05-05.
 */

public class RMIInputStreamImpl implements RMIInputStreamInterf {
    private InputStream in;
    private byte[] buf;

    public RMIInputStreamImpl(InputStream in, int port) throws RemoteException {
        this.in = in;
        UnicastRemoteObject.exportObject(this, port);
    }
    @Override
    public byte[] readBytes(int len) throws IOException {
        if(buf == null || buf.length != len){
            buf = new byte[len];
        }
        int i = in.read(buf);
        if (i < 0) //no bytes to read
            return null;
        if(i != len){
            byte[] tmp = new byte[i];
            System.arraycopy(buf, 0, tmp, 0, i);
            return tmp;
        }
        return buf;
    }

    @Override
    public int read() throws RemoteException {
        return 0;
    }

    @Override
    public void close() throws IOException {
        in.close();
    }
}
