package MeasureDelays;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;

import ConClasses.Interface;
import ConClasses.Message;
import ConClasses.MessageException;
import ConClasses.Proxy;
import ConClasses.ServerCom;

public class ThreadSendTimes extends Thread
{
	private int port;
	private InetAddress group;
	private byte[] ipMaster;
	private CurrentTime time;
	
	public ThreadSendTimes(InetAddress group, int port, byte[] ipMaster, CurrentTime time)
	{
		this.group = group;
		this.port = port;
		this.ipMaster = ipMaster;
		this.time = time;
	}
	
	@Override
	public void run() 
	{
		//Recebe ordem e envia atrasos
		Interface inter = new Interface();
        ServerCom scon, sconi;                                          // canais de comunicação

        /* estabelecimento do servico */
        scon = new ServerCom(port);                // criação do canal de escuta e sua associação
        scon.start();                                                   // com o endereço público
        
        boolean end = false; 
        /* processamento de pedidos */
    	try
    	{
    		while(!end)
    		{
    			try
    			{
	    			sconi = scon.accept();                                      // entrada em processo de escuta7
		    		Message inMessage = (Message) sconi.readObject();       // ler pedido do cliente
		    		if(inMessage.getType() == Message.REQ_PERMISSION)
		    		{	
		    			System.out.println("");
		                MulticastSocket multicastSocket = null;
		        		
		        		try
		        		{
		        			multicastSocket = new MulticastSocket(port);
		        			multicastSocket.joinGroup(group);
		        			multicastSocket.setTimeToLive(64);
		        			
		        			//Enviar pacotes para se estabelecer nova arvore multicast
		        			//Demos 30 segundos para a rede se restabelecer de novo
		        			System.out.print("NewMulticastTree ");
		        			for (int j = 0; j < 10; j++) 
		        	        {
		        				System.out.print(".");
		        				byte[] msg = new byte[0];
		        	            msg = Protocols.NewMulticastTree();
		        	            DatagramPacket messageOut = new DatagramPacket(msg, msg.length, group, port);
		        	            multicastSocket.send(messageOut);
		        	            Thread.sleep(3000);//Esperar 3 segundos entre pacote
		        	        }
		        			System.out.print("\nSend delay packages ");
		        			
		        			//Envio dos atrasos
		        			for (int j = 0; j < 10; j++) //MUDAR para 100
		        	        {
		        				System.out.print(".");
		        	        	byte[] msg = new byte[0];
		        	            msg = Protocols.SendDelays(time.getServerTime());
		        	            DatagramPacket messageOut = new DatagramPacket(msg, msg.length, group, port);
		        	            multicastSocket.send(messageOut);
		        	            Thread.sleep(50);
		        			}
		        			System.out.println("");
		        			//Enviar pacote ao master a dizer que ja fez tudo
		        			//Protocols.SendToMasterFinishMeasures(ipMaster, port);
		        			
		        		}
		        		catch (IOException e) 
		        		{
		        			System.out.println("IOException: " + e);
		        		} 
		        		catch (InterruptedException e) 
		        		{
		        			// TODO Auto-generated catch block
		        			e.printStackTrace();
		        		}
		            	end = true;
		    		}
		    		Message outMessage = inter.processAndReply(inMessage);
	        		sconi.writeObject(outMessage);                                  // enviar resposta ao cliente
	                sconi.close();
    			}
    			catch(SocketTimeoutException e)
    			{}
    		}
    		
    	}
    	catch (MessageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
            
    	
		
	}
}
