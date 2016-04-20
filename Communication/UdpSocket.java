/**
 * Created by mejan on 2016-04-20.
 */

package Communication;

import java.io.IOException;
import java.net.*;

public class UdpSocket extends Thread {
    UdpSocket(){
        sendPort = 1337;
        recivevePort = 1338;
        sendToIpAddress = "127.0.0.1";
        connected = false;
        packetSize = orginalSize;
    }

    UdpSocket(String toIP, int toSend, int toReciveve, int toPacket, boolean toCon){
        sendToIpAddress = toIP;
        sendPort = toSend;
        recivevePort = toReciveve;
        packetSize = toPacket;
        connected = toCon;
    }

    public void run(){
        //Need to fix more
        /*while (connected){
            byte[] buffer = new byte[packetSize];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            try {
                socket.receive(packet);
                if(packet.getLength() > 0){
                    //Read message HERE
                }
            } catch (){
                //Catch timeout message.
            }

        }*/
    }

    public void setSendPort(int toSet){
        sendPort = toSet;
    }

    public void setRecivevePort(int toSet){
        recivevePort = toSet;
    }

    public void setSendToIpAddress(String toSet){
        sendToIpAddress = toSet;
    }

    public void startRunning (){
        connected = true;
    }

    public void stopRunning () {
        connected = false;
    }

    public void resetPacketSize(){
        packetSize = orginalSize;
    }

    public void setPacketSize(int toSet){
        packetSize = toSet;
    }

    private int sendPort;
    private int recivevePort;
    private String sendToIpAddress;
    private boolean connected;
    private int packetSize;
    private  final int orginalSize = 1024;
}
