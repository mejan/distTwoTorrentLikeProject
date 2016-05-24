package Communication;

import java.io.IOException;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by heka1203 on 2016-05-05.
 */
public class RMIOutputStreamImpl implements RMIOutputStreamInterf {
    private OutputStream out;

    public RMIOutputStreamImpl(OutputStream out, int port) throws RemoteException {
        this.out = out;
        UnicastRemoteObject.exportObject(this, port);
    }

    @Override
    public void write(int b) throws IOException {
        out.write(b);
    }

    @Override
    public void write(byte[] buf, int offset, int length) throws IOException {
        out.write(buf, offset, length);
    }

    @Override
    public void close() throws IOException {
        out.close();
    }
}
