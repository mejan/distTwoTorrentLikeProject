package Chord;

import java.io.*;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;


/**
 * Created by heka1203 on 2016-04-28.
 */
public class FileUtils {
    public static final int chunkSize = 4096; //bytes

    private FileUtils(){}


    //src = ...
    //dest = ...
    public static ArrayList<File> splitFile(File src, File dest){

        String filename = src.getName();
        String filepath = src.toString().replaceAll(""+filename+"", "");

        ArrayList<File> files = new ArrayList<File>();

        try(FileInputStream in = new FileInputStream(src)){
            byte[] buf = new byte[chunkSize];
            int readBytes = 0;
            int chunk = 1;
            while((readBytes = in.read(buf)) != -1){
                //write to new file

                String chunkName = String.valueOf(Hash.hash(String.valueOf(ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE))));
                File chunkFile = new File(chunkName);
                try(FileOutputStream out = new FileOutputStream(new File(dest, chunkName))){
                    //System.out.println(chunkName);
                    out.write(buf, 0, readBytes);
                }
                files.add(chunkFile);
                ++chunk;


            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //createListOfFiles(files, new File(filepath + "torrent.txt"));
        //merge back
        //mergeFiles(files, new File(dest, "new"+filename));
        return files;
    }

    public static void mergeFiles(ArrayList<File> files, File outFile){
        try(FileOutputStream out = new FileOutputStream(outFile)) {
            if(!outFile.exists()) outFile.createNewFile();
            for(File f : files){
                Files.copy(f.toPath(), out);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void createListOfFiles(ArrayList<File> files, File outFile){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(outFile))) {
            for(File f : files){
                writer.write(f.getName());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<File> getListOfFiles(File torrentFile){
        ArrayList<File> files = null;
        try(BufferedReader reader = new BufferedReader(new FileReader(torrentFile))) {
            String line = "";
            files = new ArrayList<>();
            while((line = reader.readLine()) != null){
                files.add(new File(line));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return files;
    }

    public static void copy(InputStream in, OutputStream out) {
        byte buf[] = new byte[chunkSize * 64];
        int readBytes = 0;
        try{
            while((readBytes = in.read(buf)) >= 0){
                out.write(buf, 0, readBytes);
            }
            in.close();
            out.close();
        } catch (IOException e) {
            System.err.println("Could not read / write or close from streams.");
        }


    }

}