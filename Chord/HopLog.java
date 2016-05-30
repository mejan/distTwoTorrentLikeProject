package Chord;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by heka1203 on 2016-05-26.
 */
public class HopLog {
    public BufferedWriter writer;
    public StringBuffer buffer;

    public HopLog(File file) throws IOException {
        this.writer = new BufferedWriter(new FileWriter(file));
        this.buffer = new StringBuffer();
    }
    public void append(String result){
        buffer.append(result);
        buffer.append(System.getProperty("line.separator"));
    }
    public void write() throws IOException {
        writer.write(buffer.toString());
        writer.close();
    }
}
