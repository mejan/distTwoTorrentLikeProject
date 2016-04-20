/**
 * Created by mejan on 2016-04-18.
 */

import ClientGUI.MainWindow;
import java.awt.*;
import java.security.NoSuchAlgorithmException;
import DHash.Hash;

public class Main {

    public static void main(String[] args) {
        try {
            System.out.println(Hash.hash("12321312312312312"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainWindow test = new MainWindow();
                test.setVisible(true);
            }

        });

    }

}