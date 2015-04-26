package PTP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class PTP implements Runnable
{
	private String ipMulticastAddress;
	private InetAddress group;
	private Type type;
	private MulticastSocket multicastSock;
	private int port;
	
	public PTP (String ipMulticastAddress, Type type, int port)
	{
		this.ipMulticastAddress = ipMulticastAddress;
		this.type = type;
		this.port = port;
		
		try 
		{
			group = InetAddress.getByName(ipMulticastAddress);
		} 
		catch (UnknownHostException e) 
		{
			// MUDAR ISTO AQUI
			e.printStackTrace();
		}
	}

	@Override
	public void run() 
	{
		if(type.equals(Type.SLAVE))
		{
			try 
			{
				multicastSock = new MulticastSocket(port);
				multicastSock.joinGroup(group);
				
				byte[] buffer = new byte[100];//Mudar isto
	            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
	            
	            multicastSock.receive(packet);
	            //Depois faz mais coisas
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(type.equals(Type.MASTER))
		{
			try 
			{
				multicastSock = new MulticastSocket();
				String msg = "Pacote de sincronizaçao";//Mudar isto
	            
	            DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.length());
	            
	            multicastSock.send(packet);
	            //Depois faz mais coisas
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
	}
	
	
}
