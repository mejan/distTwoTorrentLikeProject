package Server;

import java.io.File;
import java.io.Serializable;
import java.util.*;

/**
 * Created by heka1203 on 2016-05-04.
 */

public class FileList implements Serializable {
    private static final long serialVersionUID = 1L;
    private HashMap<String, ArrayList<File>> fileList;

    public FileList(HashMap<String, ArrayList<File>> fileList){
        this.fileList = fileList;
    }
    public void add(String filename, ArrayList<File> files){
        fileList.put(filename, files);
    }
    public HashMap<String, ArrayList<File>> getFileList(){
        return this.fileList;
    }

    public void print(){
        for(Map.Entry<String, ArrayList<File>> entry : fileList.entrySet()){
            String filename = (String)entry.getKey();
            ArrayList<File> files = (ArrayList<File>)entry.getValue();
            System.out.println("\nFilename: " + filename);
            for(File file : files){
                System.out.print('\n' + file.getName());
            }
        }
    }

    public ArrayList<File> find(String filename){
        if(fileList.containsKey(filename))
            return fileList.get(filename);

        return null;
    }
    public int getNumberOfChunks(String filename){
        return find(filename).size();
    }
    public File getRandomChunk(){
        if(fileList.isEmpty()) return null;
        Random random = new Random();
        ArrayList<String> filenames = new ArrayList<String>(fileList.keySet());
        String filename = filenames.get(random.nextInt(filenames.size()));
        ArrayList<File> chunks = fileList.get(filename);
        return chunks.get(random.nextInt(chunks.size()));
    }

}
