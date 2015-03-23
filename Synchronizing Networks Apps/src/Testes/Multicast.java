/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Testes;

import java.net.*;

/**
 *
 * @author Rafael
 */
public class Multicast 
{
    public static void main(String[] args) throws Exception
    {
        String msg = "Hello";
        InetAddress group = InetAddress.getByName("228.5.6.7");
        MulticastSocket s = new MulticastSocket(6789);
        s.joinGroup(group);
        DatagramPacket hi = new DatagramPacket(msg.getBytes(), msg.length(),
                                    group, 6789);
        s.send(hi);
        
        // get their responses!
        byte[] buf = new byte[1000];
        DatagramPacket recv = new DatagramPacket(buf, buf.length);
        s.receive(recv);

        System.out.println("aqui");
        // OK, I'm done talking - leave the group...
        s.leaveGroup(group);
    }
}
