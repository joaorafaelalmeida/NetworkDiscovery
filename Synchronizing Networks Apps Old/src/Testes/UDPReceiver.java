/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author Rafael
 */
public class UDPReceiver {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        try
        {
            DatagramSocket serverSocket = new DatagramSocket(5000);
            byte[] receiveData = new byte[8];
            byte[] sendData = new byte[8];
            System.out.println("start");
            while(true)
               {
                  DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                  serverSocket.receive(receivePacket);
                  String sentence = new String( receivePacket.getData());
                  System.out.println(sentence);
                  InetAddress IPAddress = receivePacket.getAddress();
                  /*String sendString = "polo";
                  sendData = sendString.getBytes();
                  DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 5000);
                  serverSocket.send(sendPacket);*/
               }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

    }
    
}
