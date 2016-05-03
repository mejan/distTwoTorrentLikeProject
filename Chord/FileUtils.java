package Chord;

import java.io.*;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


/**
 * Created by heka1203 on 2016-04-28.
 */
public class FileUtils {
    private static final int chunkSize = 1024; //bytes

    private FileUtils(){}
    public static void splitFile(String path){
        File file = new File(path);
        splitFile(file);
    }

    public static ArrayList<File> splitFile(File file){

        String filename = file.getName();
        String filepath = file.toString().replaceAll(""+filename+"", "");


        ArrayList<File> files = new ArrayList<File>();


        try(FileInputStream in = new FileInputStream(file)){
            byte[] buf = new byte[chunkSize];
            int readBytes = 0;
            int chunk = 1;
            while((readBytes = in.read(buf)) != -1){
                //write to new file
                String chunkName = String.valueOf(Hash.hash(filename + chunk));
                File chunkFile = new File(filepath + chunkName);
                try(FileOutputStream out = new FileOutputStream(chunkFile)){
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
        createListOfFiles(files, new File(filepath + "torrent.txt"));
        //merge back
        //mergeFiles(files, new File(filepath + filename + "new"));

        return files;
    }
    public static void mergeFiles(ArrayList<File> files, File outFile){
        try(FileOutputStream out = new FileOutputStream(outFile)) {
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

}