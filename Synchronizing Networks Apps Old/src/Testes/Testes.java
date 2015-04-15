/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Testes;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 *
 * @author Rafael
 */
public class Testes {

    private static Scanner in = new Scanner(System.in);
    public static void main(String[] args) throws UnknownHostException, SocketException, IOException 
    {
        System.out.println("Introduza o endereço ip de destino: ");
        String ip = in.nextLine();
        while(true)
        {
            System.out.println("Mensagem a enviar: ");
            String msg = in.nextLine();

            //Sender
            byte[] buffer = msg.getBytes();
            InetAddress address = InetAddress.getByName(ip);
            DatagramPacket packet = new DatagramPacket(
                    buffer, buffer.length, address, 5000
                    );
            DatagramSocket datagramSocket = new DatagramSocket();
            datagramSocket.send(packet);

            //Receiver
            try
            {
                DatagramSocket serverSocket = new DatagramSocket(5000);
                byte[] receiveData = new byte[8];
                String sentence = "";
                while(sentence.length() >0)
                {
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    serverSocket.receive(receivePacket);
                    sentence = new String( receivePacket.getData());
                    System.out.println(sentence);
                }
            }
            catch (Exception e)
            {
                System.err.println(e);
            }
        }
    }
    
}
