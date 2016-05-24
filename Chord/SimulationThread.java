package Chord;

import Server.FileList;

import java.io.File;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by heka1203 on 2016-05-24.
 */
public class SimulationThread implements Runnable{
    private NodeImpl node;
    SimulationThread(NodeImpl node){
        this.node = node;
    }

    @Override
    public void run() {
        while(true){
            try {
                FileList files = Chord.getAvailableFiles();
                ArrayList<File> file = files.getRandomFile();
                Chord.dow
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            }
        }
    }
}
