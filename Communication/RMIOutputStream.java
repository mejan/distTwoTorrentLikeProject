package Communication;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * Created by heka1203 on 2016-05-05.
 */
public class RMIOutputStream extends OutputStream implements Serializable {
    private RMIOutputStreamInterf out;

    public RMIOutputStream(RMIOutputStreamImpl out){
        this.out = out;
    }
    @Override
    public void write(int b) throws IOException {
        out.write(b);
    }
    public void write(byte[] buf, int offset, int length) throws IOException {
        out.write(buf, offset, length);
    }
    public void close() throws IOException {
        out.close();
    }
}
