package PTP;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PTP implements Runnable
{
	private String ipMulticastAddress;
	private String ipDevice;
	private InetAddress group;
	private Type type;
	private int port;
	private int sessionID;
	
	public PTP (String ipMulticastAddress, Type type, int port, String ipDevice)
	{
		this.ipMulticastAddress = ipMulticastAddress;
		this.type = type;
		this.port = port;
		this.ipDevice = ipDevice;
		
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
			System.out.println("Slave");
	        byte[] buf = new byte[256];//muda isto
	        try
	        {
	        	InetAddress group = InetAddress.getByName(ip);
	        	MulticastSocket clientSocket = new MulticastSocket(port);
	            clientSocket.joinGroup(group);
	            boolean waitForSync = true;
	            byte[] ipMaster = null;
	            while (waitForSync) 
	            {
	                DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
	                clientSocket.receive(msgPacket);

	                DataInputStream ois = new DataInputStream(new ByteArrayInputStream(buf));
	                int sessionIDTmp = ois.readInt();
	                int packID = ois.readInt();
	                
	          	  	ipMaster = new byte[ois.readInt()];
	          	  	ois.read(ipMaster);
	          	  	System.out.println("Sync package received, session id " + sessionIDTmp + " package id "+ packID);
	          	  	
	          	  	
	                buf = new byte[256];
	                msgPacket = new DatagramPacket(buf, buf.length);
	                clientSocket.receive(msgPacket);
	                
	                ois = new DataInputStream(new ByteArrayInputStream(buf));
	                int sessionIDTmp2 = ois.readInt();
	                int packID2 = ois.readInt();
	                
	                if(sessionIDTmp2 == sessionIDTmp && packID2 == packID) //Tudo ok
	                {
	                	long time = ois.readLong();
	                	System.out.println(time);
	                	if(packID == 4)//Mudar isto
	                		waitForSync = false;
	                }
	                else //Algo errado, tratar deste problema
	                {
	                	System.out.println("erro " + sessionIDTmp2 + " pid " + packID2);
	                }
	            }
	            
	            //Enviar solicita?ao ao servidor
	            byte[] buffer = ipDevice.getBytes();
	            
	            for (int i = 0; i < 5; i++) 
	            {
		            Thread.currentThread().sleep((long) (1000));
		            InetAddress address = InetAddress.getByName(new String(ipMaster));
		            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
		            DatagramSocket datagramSocket = new DatagramSocket();
		            datagramSocket.send(packet);
		            System.out.println("Enviado ao servidor");
	            }
	        } 
	        catch (IOException ex) 
	        {
	            ex.printStackTrace();
	        } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		sessionID = new Random().nextInt(9999);
		if(type.equals(Type.MASTER))
		{
			MulticastSocket s = null;
	        try 
	        {
	        	//Sincronizacao
	            for (int i = 0; i < 5; i++) 
	            {
		            InetAddress group = InetAddress.getByName(ipDevice);
		            s = new MulticastSocket(port);
		            s.joinGroup(group);
		            byte[] msg = Protocols.Sync(ipDevice, sessionID, i);
		            DatagramPacket messageOut
		                    = new DatagramPacket(msg, msg.length, group, port);
		            s.setTimeToLive(64);
		            s.send(messageOut);
		            
		            System.out.println("Server sent packet sync and timeSync with id " + i + " in session id "+sessionID);
	                Thread.sleep(500);
	            }
	            
	            //Receber respostas de slaves em unicast durante x segundos
	            long inicialTime = System.currentTimeMillis();
	            boolean waitForAnswer = true;
	            List<String> allDevices = new ArrayList();
	            while (waitForAnswer)
	            {
	            	DatagramSocket serverSocket = new DatagramSocket(port);
	            	byte[] respostaMsg = new byte[250];
	            	DatagramPacket receivePacket = new DatagramPacket(respostaMsg, respostaMsg.length);
	                serverSocket.receive(receivePacket);
	            	
	            	allDevices.add(new String(receivePacket.getData()));
	            	if(System.currentTimeMillis() - inicialTime > 10000)//depois mudar isto
	            		waitForAnswer = false;
	            }
	            
	            System.out.println("Show all devices, test only!");
	            for(String tmp: allDevices)
	            	System.out.println(tmp);
	            
	        }
	        catch (SocketException e) 
	        {
	            System.out.println("Socket: " + e.getMessage());
	        } 
	        catch (IOException e) 
	        {
	            System.out.println("IO: " + e.getMessage());
	        } 
	        catch (InterruptedException e) 
	        {
	        	System.out.println("Interrupted: " + e.getMessage());
			} 
	        finally 
	        {
	            if (s != null) 
	                s.close();
	        }
		     
	            
	            


		}
		
	}
	
	
}
