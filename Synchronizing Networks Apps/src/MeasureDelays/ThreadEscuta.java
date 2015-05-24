package MeasureDelays;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import Entities.Device;
import Entities.Neighbour;

public class ThreadEscuta extends Thread
{
	private InetAddress group;
	private int port;
	private Device device;
	private byte[] ipMaster;
	
	public ThreadEscuta(InetAddress group, int port, byte[] ipMaster)
	{
		this.group = group;
		this.port = port;
		this.ipMaster = ipMaster;
		device = new Device(MyIP.getMyIP());

	}
	
	@Override
	public void run() 
	{
		boolean end = true;
		while(end)
		{
			//Recebe atrasos
			MulticastSocket multicastSocket = null;
			CurrentTime time = null;
			try
			{
				multicastSocket = new MulticastSocket(port);
				multicastSocket.joinGroup(group);
				multicastSocket.setTimeToLive(64);
				
				byte[] buf = new byte[256];
				DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
		        multicastSocket.receive(msgPacket);
		        DataInputStream ois = new DataInputStream(new ByteArrayInputStream(buf));
	        	int flag = ois.readInt();
	        	if(flag == Flags.SendDelays.getCode())
	        	{
	        		byte[] ip = new byte[ois.readInt()];
	        		long delay = ois.readLong();
	        		device.addNewNeighbour(new Neighbour(new String(ip), delay));
	        	}
	        	
	        	if(flag == Flags.EndDelays.getCode())
	        	{
	        		end = false;
	        		Protocols.SendDeviceToMaster(ipMaster, port, device);
	        	}
	        	
	        	if(flag == Flags.EstablishMulticastTree.getCode())
	        	{
	        		//Ignorar
	        	}
			}
			catch (IOException e) 
			{
				System.out.println("IOException: " + e);
			}
		}
		
	}
}
