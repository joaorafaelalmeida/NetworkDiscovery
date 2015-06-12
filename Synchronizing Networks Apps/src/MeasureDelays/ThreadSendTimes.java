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
	
	public ThreadSendTimes(InetAddress group, int port, byte[] ipMaster)
	{
		this.group = group;
		this.port = port;
		this.ipMaster = ipMaster;
	}
	
	@Override
	public void run() 
	{
		//Recebe ordem e envia atrasos
		Interface inter = new Interface();
        ServerCom scon, sconi;                                          // canais de comunica��o
        Proxy cliProxy;                                     // thread agente prestador do servi�o

        /* estabelecimento do servico */
        scon = new ServerCom(port);                // cria��o do canal de escuta e sua associa��o
        scon.start();                                                   // com o endere�o p�blico
        
        /* processamento de pedidos */
    	try
    	{
    		sconi = scon.accept();                                      // entrada em processo de escuta
    		Message inMessage = (Message) sconi.readObject();       // ler pedido do cliente
    		if(inMessage.getType() == Message.REQ_PERMISSION)
    		{
    			Message outMessage = inter.processAndReply(inMessage);
        		sconi.writeObject(outMessage);                                  // enviar resposta ao cliente
                sconi.close();
                
                MulticastSocket multicastSocket = null;
        		CurrentTime time = null;
        		try
        		{
        			multicastSocket = new MulticastSocket(port);
        			multicastSocket.joinGroup(group);
        			multicastSocket.setTimeToLive(64);
        			
        			//Enviar pacotes para se estabelecer nova arvore multicast
        			//Demos 30 segundos para a rede se restabelecer de novo
        			for (int j = 0; j < 10; j++) 
        	        {
        				byte[] msg = new byte[0];
        	            msg = Protocols.NewMulticastTree();
        	            DatagramPacket messageOut = new DatagramPacket(msg, msg.length, group, port);
        	            multicastSocket.send(messageOut);
        	            Thread.sleep(3000);//Esperar 3 segundos entre pacote
        	        }
        			
        			//Envio dos atrasos
        			for (int j = 0; j < 100; j++) 
        	        {
        	        	byte[] msg = new byte[0];
        	            msg = Protocols.SendDelays(System.nanoTime());
        	            DatagramPacket messageOut = new DatagramPacket(msg, msg.length, group, port);
        	            multicastSocket.send(messageOut);
        	            Thread.sleep(50);
        			}
        			
        			//Enviar pacote ao master a dizer que ja fez tudo
        			Protocols.SendToMasterFinishMeasures(ipMaster, port);
        			
        		}
        		catch (IOException e) 
        		{
        			System.out.println("IOException: " + e);
        		} 
        		catch (InterruptedException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
            	
    		}
    		
    	}
    	catch(SocketTimeoutException e)
    	{} 
    	catch (MessageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
            
    	
		
	}
}