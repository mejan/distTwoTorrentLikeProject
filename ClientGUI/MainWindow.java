/**
 * Created by mejan on 2016-04-20.
 */

package ClientGUI;

import javax.swing.JFrame;
import java.awt.*;


public class MainWindow extends JFrame{

    public MainWindow(){
        initGUI();
    }

    private void initGUI(){
        setTitle("Our torrent like client");
        setSize(300,200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}