package Chord;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Created by heka1203 on 2016-04-28.
 */
public class FileUtils {
    private static final int chunkSize = 1024; //bytes

    private FileUtils(){}

    public static byte[] read(Path path){
        try {
            return Files.readAllBytes(path);
            //splitToChunks(readBytes, filepath);

        } catch (IOException e) {
            System.err.println("Could not read from file");
        }
        return null;
    }

    public static String[] createChunks(String filePath) throws NoSuchAlgorithmException {
        Path path = Paths.get(filePath);

        if(Files.exists(path))
            return splitToChunks(read(path), path);

        return null;
    }

    public static void createFileFromChunks(String filepath, String[] chunkNames, String outFile) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        for(int i = 0; i < chunkNames.length; i++){
            Path path = Paths.get(filepath + chunkNames[i]);
            out.write(Files.readAllBytes(path));
        }
        Files.write(Paths.get(outFile), out.toByteArray());
    }

    private static String[] splitToChunks(byte[] readBytes, Path path) throws NoSuchAlgorithmException {
        System.out.println((int)Math.ceil(readBytes.length / (double)chunkSize));
        int nChunks = (int)Math.ceil(readBytes.length / (double)chunkSize);
        int chunkSize = readBytes.length / nChunks;
        String chunkPath = path.toAbsolutePath().toString();
        String chunkNames[] = new String[nChunks];
        int offset = 0;

        for(int i = 0; i < nChunks; i++){
            byte[] buf;
            String chunkName = String.valueOf(Hash.hash(path.getFileName().toString() + i));

            buf = Arrays.copyOfRange(readBytes, offset, offset + chunkSize);
            offset += chunkSize;
            if(buf.length > 0){
                writeChunkFiles(buf, Paths.get(chunkPath+chunkName));
                chunkNames[i] = chunkName;
            }
        }
        return chunkNames;
    }

    private static void writeChunkFiles(byte[] readBytes, Path path){

        try {
            Files.write(path, readBytes);
        } catch (IOException e) {
            System.err.println("Could not write to file");
        }
    }

}