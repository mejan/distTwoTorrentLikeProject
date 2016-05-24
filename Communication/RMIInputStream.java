package Communication;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.rmi.RemoteException;

/**
 * Created by heka1203 on 2016-05-05.
 */
public class RMIInputStream extends InputStream implements Serializable{
    public RMIInputStreamInterf in;

    public RMIInputStream(RMIInputStreamImpl in){
        this.in = in;
    }
    public int read() throws RemoteException {
        return in.read();
    }
    public int read(byte[] b, int offset, int length) throws IOException {
        byte[] buf = in.readBytes(length);
        if(buf == null) return -1;
        int i = buf.length;
        System.arraycopy(buf, 0, b, offset, i);
        return i;
    }

    public void close() throws IOException {
        super.close();
    }
}
