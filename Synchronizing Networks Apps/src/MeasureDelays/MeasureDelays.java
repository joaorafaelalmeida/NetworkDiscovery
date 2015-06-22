package MeasureDelays;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import UserInterface.ControllerInterface;
import ConClasses.ClientCom;
import ConClasses.Interface;
import ConClasses.Message;
import ConClasses.MessageException;
import ConClasses.Proxy;
import ConClasses.ServerCom;
import Entities.Device;

public class MeasureDelays implements Runnable
{
	private String ipMulticastAddress;
	private String ipDevice;
	private InetAddress group;
	private Type type;
	private int port;
	private int sessionID;
	private final JFrame frame;
	private static List<Device> devices = new ArrayList<Device>();
	private JPanel matrixPanel;
	
	public MeasureDelays (String ipMulticastAddress, Type type, int port, String ipDevice)
	{
		this.ipMulticastAddress = ipMulticastAddress;
		this.type = type;
		this.port = port;
		this.ipDevice = ipDevice;
		this.frame = null;
		try 
		{
			group = InetAddress.getByName(ipMulticastAddress);
		} 
		catch (UnknownHostException e) 
		{
			System.out.println("UnknownHostException: " + e);
		}
	}
	
	public MeasureDelays (String ipMulticastAddress, Type type, int port, String ipDevice, JFrame frame, JPanel matrixPanel)
	{
		this.ipMulticastAddress = ipMulticastAddress;
		this.type = type;
		this.port = port;
		this.ipDevice = ipDevice;
		this.frame = frame;
		this.matrixPanel = matrixPanel;
		try 
		{
			group = InetAddress.getByName(ipMulticastAddress);
		} 
		catch (UnknownHostException e) 
		{
			JOptionPane.showMessageDialog(frame, "UnknownHostException: " + e, "Error", JOptionPane.ERROR_MESSAGE);
			System.out.println("UnknownHostException: " + e);
		}
	}

	@Override
	public void run() 
	{
		MulticastSocket multicastSocket = null;
		CurrentTime time = null;
		try
		{
			multicastSocket = new MulticastSocket(port);
			multicastSocket.joinGroup(group);
			multicastSocket.setTimeToLive(64);			
		}
		catch (IOException e) 
		{
			if(frame != null)
				JOptionPane.showMessageDialog(frame, "IOException: " + e, "Error", JOptionPane.ERROR_MESSAGE);
			System.out.println("IOException: " + e);
		}
		
		if(type.equals(Type.SLAVE))
		{
			System.out.println("Slave");
			boolean waitForSync = true;
            byte[] ipMaster = null;
            byte[] buf = new byte[256];//muda isto
            try
            {
            	int sessionID = -1;
            	int packID = -1;
	            while (waitForSync) 
	            {
	                DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
	                multicastSocket.receive(msgPacket);
	                
	                try
	                {
	                	DataInputStream ois = new DataInputStream(new ByteArrayInputStream(buf));
	                	int flag = ois.readInt();
	                	Flags tmp = Flags.getFlagByCode(flag);
	                	if(tmp != null)
		                	switch(tmp)
	                		{
		                		case Sync:
		                			sessionID = ois.readInt();
		    		                packID = ois.readInt();
		    		                
		    		          	  	ipMaster = new byte[ois.readInt()];
		    		          	  	ois.read(ipMaster);
		    		          	  	System.out.println("Sync package received, session id " + sessionID + " package id "+ packID + ", master ip " + new String(ipMaster));
		                			break;
		                			
		                		case SendTimeSync:
		                			
		                			buf = new byte[256];
		    		                msgPacket = new DatagramPacket(buf, buf.length);
		    		                multicastSocket.receive(msgPacket);

		    		                int sessionIDTmp = ois.readInt();
		    		                int packID2 = ois.readInt();
		    		                
		    		                if(sessionIDTmp == sessionID && packID2 == packID) //Tudo ok
		    		                {
		    		                	long timeRead = ois.readLong();
		    		                	time = new CurrentTime();
		    		                	time.setServerTime(timeRead);
		    		                	System.out.println("Time " + timeRead);
		    		                	waitForSync = false;
		    		                }
		    		                else //Algo errado, tratar deste problema
		    		                {
		    		                	System.out.println("Erro " + sessionIDTmp + " pid " + packID2);
		    		                }
		                			break;
		                			
	                			default:
	                				System.out.println("Invalid flag");
	                		}
	                }
	                catch(OutOfMemoryError e)
	                {
	                	System.out.println("Package damaged");
	                }
	            }
	            
	            //Rece��o de pacote a permitir que sejam feitas as solicita�oes ao servidor
	            boolean waitForUnicastSync = true;
	            while(waitForUnicastSync)
	            {
	            	DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
	                multicastSocket.receive(msgPacket);
	                
	                try
	                {
	                	DataInputStream ois = new DataInputStream(new ByteArrayInputStream(buf));
	                	int flag = ois.readInt();
	                	Flags tmp = Flags.getFlagByCode(flag);
	                	if(tmp != null)
		                	switch(tmp)
	                		{
		                		case Sync:
		                			break;
		                		case SendTimeSync:
		                			break;
		                		case StartReadSlaves:
		                			int sessionIDTmp = ois.readInt();
					          	  	System.out.println("Package received that allow conections to the master, session id " + sessionIDTmp);
					          	  	waitForUnicastSync = false;
		                			break;
		                		default:
		                			System.out.println("Invalid flag");
		                			break;
	                		}                		
	                		
	                }
	                catch(OutOfMemoryError e)
	                {
	                	System.out.println("Package damaged");
	                }
	            }
	            
	            //Enviar ip da maquina ao servidor
	            Protocols.AdvertiseMaster(ipMaster, port);
	            
	            //Syncroniza��o
	            System.out.print("Syncronize ");
	            boolean endOfSync = true;
	            while(endOfSync)
	            {
		            long diff = Protocols.DelayMesaures(ipMaster, port, time);
		            System.out.print(".");
		            if(diff < 1000)
		            {
		            	endOfSync = false;
		            	System.out.println("\nDiference time " + diff);
		            }
	            }
	            
	            //Fica � espera de ordem para enviar os seu tempos
	            Thread thE = new ThreadEscuta(group, port, ipMaster, time);
	            Thread thS = new ThreadSendTimes(group, port, ipMaster, time);
	            thE.start();
	            thS.start();
	            System.out.println("Start thread Escuta and thread SendTimes");
	            try 
	            {
					thE.join();
					thS.join();
				} 
	            catch (InterruptedException e) 
	            {
					e.printStackTrace();
				}
            }
            catch (IOException e) 
			{
				if(frame != null)
					JOptionPane.showMessageDialog(frame, "IOException: " + e, "Error", JOptionPane.ERROR_MESSAGE);
				System.out.println("IOException: " + e);
			}
		}
		
		sessionID = new Random().nextInt(9999);
		if(type.equals(Type.MASTER))
		{
			devices = new ArrayList<Device>();
			System.out.println("Master");
			try
			{
				//Sincronizacao
				for (int i = 0; i < 5; i++) 
	            {
		            byte[] msg = Protocols.Sync(ipDevice, sessionID, i);
		            DatagramPacket messageOut = new DatagramPacket(msg, msg.length, group, port);
		            multicastSocket.send(messageOut);
		            
		            msg = new byte[0];
		            msg = Protocols.SendTimeSync(sessionID, i);
		            messageOut = new DatagramPacket(msg, msg.length, group, port);
		            multicastSocket.send(messageOut);
		            
		            System.out.println("Server sent packet sync and timeSync with id " + i + " in session id "+sessionID);
	                Thread.sleep(500);
	            }
				
				//Enviar pacote de StartReadSlaves, para os slaves se conetarem ao servidor
				byte[] msg = Protocols.StartReadSlaves(sessionID);
	            DatagramPacket messageOut = new DatagramPacket(msg, msg.length, group, port);
	            multicastSocket.send(messageOut);
	            System.out.println("Server sent packet that notify slaves to start unicast syncronization in session id "+sessionID);
				
	            
	            Interface inter = new Interface();
	            ServerCom scon, sconi;                                          // canais de comunica��o
	            Proxy cliProxy;                                     // thread agente prestador do servi�o

	            /* estabelecimento do servico */
	            scon = new ServerCom(port);                // cria��o do canal de escuta e sua associa��o
	            scon.start();                                                   // com o endere�o p�blico
	            
	            /* processamento de pedidos */
	            boolean syncTimes = true;
	            while (syncTimes) 
	            {
	            	try
	            	{
	            		sconi = scon.accept();                                      // entrada em processo de escuta
	            		cliProxy = new Proxy(sconi, inter);       // lan�amento do agente prestador do servi�o
		                cliProxy.start();
	            	}
	            	catch(SocketTimeoutException e)
	            	{
	            		syncTimes = false;
	            		break;
	            	}
	            }
	            System.out.println("Syncronization is complete with sucess!");
	            
	            /*
	             * Enviar pacotes de atraso
	             * Enviar permiss�o para os slaves mandaram os atrasos em unicast
	             * Receber os devices
	             * Enviar para a matrix 
	             * */
	            
	            System.out.print("Send delay packages ");
	            //Enviar pacotes de atraso (100 pacotes)
	            for (int j = 0; j < 10; j++) //Mudar para 100
	            {
	            	System.out.print(".");
	            	msg = new byte[0];
		            msg = Protocols.SendDelays(System.nanoTime());
		            messageOut = new DatagramPacket(msg, msg.length, group, port);
		            multicastSocket.send(messageOut);
		            Thread.sleep(50);
				}
	            System.out.println("");

	            //Cria thread para receber atrasos e contruir a sua propria matrix
	            Thread thE = new ThreadEscuta(group, port, MyIP.getMyIP().getBytes());
	            thE.start();
	            
	            //Enviar permissoes
	            for(Device tmp: devices)
	            {
	            	if(tmp!=null)
	            	{
		            	System.out.println("Permission for device " + tmp.getDeviceName());
	            		Protocols.SendPermission(tmp.getDeviceName(), port);
	            	}
	            	else
	            		System.out.println("NULL");
	            }
	            
	            //Enviar pacote em multicast a indicar que pode receber os devices
	            System.out.println("Receive delays from all slaves");
	            msg = Protocols.AdvertiseSlavesToSendDevies();
	            messageOut = new DatagramPacket(msg, msg.length, group, port);
	            multicastSocket.send(messageOut);
	            
	            //Receber diveces
	            int count = 0;
	            while (true) 
	            {
	            	try
	            	{
	            		sconi = scon.accept();                                      // entrada em processo de escuta
	            		cliProxy = new Proxy(sconi, inter);       // lan�amento do agente prestador do servi�o
		                cliProxy.start();
	            	}
	            	catch(SocketTimeoutException e)
	            	{
	            		count ++;
	            		if(count >3)
	            			break;
	            	}
	            }
	            System.out.println("Load matrix");
	            
	            
	            //Carregar matrix
	            ControllerInterface control = new ControllerInterface();
	            control.openMatrixFromMeasures(matrixPanel, Matrix.Matrix.devices.size(), Matrix.Matrix.devices);
	            matrixPanel.revalidate();

			}
			catch (IOException e) 
			{
				if(frame != null)
					JOptionPane.showMessageDialog(frame, "IOException: " + e, "Error", JOptionPane.ERROR_MESSAGE);
				System.out.println("IOException: " + e);
			} 
			catch (InterruptedException e) 
			{
				if(frame != null)
					JOptionPane.showMessageDialog(frame, "InterruptedException: " + e, "Error", JOptionPane.ERROR_MESSAGE);
				System.out.println("InterruptedException: " + e);
			}
		}
		
	}
	
	public static void addDevices(String ip)
	{
		Semaphore acess = new Semaphore(1);
		try 
		{
			acess.acquire();
			devices.add(new Device(ip, ip));
			acess.release();
		} 
		catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
}
