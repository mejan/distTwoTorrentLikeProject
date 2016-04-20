/**
 * Created by mejan on 2016-04-20.
 */

package Communication;

import java.net.*;

public class TcpSocket extends Thread{

    TcpSocket(){
        sendToIpAddress = "127.0.0.1";
        sendToPort = 1339;
        recivevePort = 1340;
        connected = false;
        packetSize = orginalPacketSize;
    }

    TcpSocket(String toIP, int toSendPort, int toRecivevePort, int toPacketSize, boolean toCon){
        sendToIpAddress = toIP;
        sendToPort = toSendPort;
        recivevePort = toRecivevePort;
        packetSize = toPacketSize;
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

    public void setSendToIpAddress(String toSet){
        sendToIpAddress = toSet;
    }

    public void setSendToPort(int toSet){
        sendToPort = toSet;
    }

    public void setRecivevePort(int toSet){
        recivevePort = toSet;
    }

    public void startRunning(){
        connected = true;
    }

    public void stopRunning(){
        connected = false;
    }

    public void changePacketSize(int toSet){
        packetSize = toSet;
    }

    public void resetPacketSize(){
        packetSize = orginalPacketSize;
    }


    private String sendToIpAddress;
    private int sendToPort;
    private int recivevePort;
    private boolean connected;
    private int packetSize;
    private final int orginalPacketSize = 1024;
}
