package MeasureDelays;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import ConClasses.ClientCom;
import ConClasses.Message;
import Entities.Device;

public class Protocols 
{
	/**
	 * Protocolos que iremos usar, nao tem argumentos e nao retornam nada por agora
	 * depois possivelmente vao ser alterados
	 * */
	public static byte[] Sync(String ip, int sessionID, int packID)
	{
		/**
		 * Pacote enviado pelo mestre, vai conter apenas o id de sessão e o seu endereço ip
		 * Formato do pacote:
		 * 4 bytes para a flag
		 * 4 bytes para o tamanho do id da sessao
		 * 4 bytes para o tamanho do id do pacote
		 * 4 bytes para o tamanho do endereço ip
		 * x bytes para o endereço ip
		 * */
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream w = new DataOutputStream(baos);
		  
		try 
		{
			//Flag
			w.writeInt(Flags.Sync.getCode());
			
			//Id da sessao
			w.writeInt(sessionID);
			w.writeInt(packID);
			
			//Endereço ip do master
			w.writeInt(ip.length());
			w.write(ip.getBytes());
			
			w.flush();
			
			return baos.toByteArray();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return null;
	}
		
	public static byte[] SendTimeSync(int sessionID, int packID)
	{
		/**
		 * Pacote enviado pelo mestre, vai conter o id de sessao e o hora de envio do sync
		 * Formato do pacote:
		 * 4 bytes para a flag
		 * 4 bytes para o tamanho do id da sessao
		 * 4 bytes para o tamanho do id do pacote
		 * 8 bytes para o tempo atual
		 * */
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream w = new DataOutputStream(baos);
		  
		try 
		{	
			//Flag
			w.writeInt(Flags.SendTimeSync.getCode());
			
			//Id da sessao
			w.writeInt(sessionID);
			w.writeInt(packID);
			//Tempo atual
			w.writeLong(System.nanoTime());

			w.flush();
			
			return baos.toByteArray();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static byte[] StartReadSlaves(int sessionID)
	{
		/**
		 * Enviado pelo master a indicar aos slaves que podem começar a enviar pacotes de registo
		 * Formato do pacote:
		 * 4 bytes para a flag
		 * 4 bytes para o tamanho do id da sessao
		 * 
		 * */
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream w = new DataOutputStream(baos);
		  
		try 
		{	
			//Flag
			w.writeInt(Flags.StartReadSlaves.getCode());
			
			//Id da sessao
			w.writeInt(sessionID);

			w.flush();			
			return baos.toByteArray();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void AdvertiseMaster(byte[] ipMaster, int port) throws UnknownHostException
	{
		/**
		 * Enviado pelos slaves com o seu ip
		 * */
		
		ClientCom con = new ClientCom(new String(ipMaster), port);
        Message inMessage, outMessage;
        
        while (!con.open()) // aguarda ligação
        {
            try 
            {
                Thread.currentThread().sleep((long) (10));
            } 
            catch (InterruptedException e) 
            {}
        }
        outMessage = new Message(Message.REQ_SEND_SLAVE_IP, MyIP.getMyIP().getBytes());
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        
        if (inMessage.getType() != Message.ACK_SEND_SLAVE_IP) 
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
	}
	
	public static long DelayMesaures(byte[] ipMaster, int port, CurrentTime time) throws UnknownHostException
	{
		/**
		 * Enviado pelos slaves, para medir os atrasos em unicast
		 * */
		
		ClientCom con = new ClientCom(new String(ipMaster), port);
        Message inMessage, outMessage;
        
        while (!con.open()) // aguarda ligação
        {
            try 
            {
                Thread.currentThread().sleep((long) (10));
            } 
            catch (InterruptedException e) 
            {}
        }
        
        outMessage = new Message(Message.DELAY_REQUEST, MyIP.getMyIP().getBytes());
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        
        if (inMessage.getType() != Message.DELAY_RESPONSE) 
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
        
        //time.setMachineTime(System.nanoTime());
        time.ajustServerTime((inMessage.getTime() - time.getServerTime()));//+(endTime-startTime));
        return Math.abs(time.getServerTime() - inMessage.getTime());
	}
		
	public static byte[] SendDelays(CurrentTime time)
	{
		/**
		 * Envio de tempo por cada maquina
		 * O pacote contem:
		 * 4 bytes para a flag
		 * 4 bytes para o tamanho do ip
		 * x byte para o ip
		 * 8 bytes para o tempo
		 * */
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream w = new DataOutputStream(baos);
		  
		try 
		{	
			//Flag
			w.writeInt(Flags.SendDelays.getCode());
			
			//ip
			w.writeInt(MyIP.getMyIP().length());
			w.write(MyIP.getMyIP().getBytes());
			
			//tempo
			w.writeLong(time.getServerTime());

			w.flush();			
			return baos.toByteArray();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static byte[] SendDelays(long time)
	{
		/**
		 * Envio de tempo por cada maquina
		 * O pacote contem:
		 * 4 bytes para a flag
		 * 4 bytes para o tamanho do ip
		 * x byte para o ip
		 * 8 bytes para o tempo
		 * */
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream w = new DataOutputStream(baos);
		  
		try 
		{	
			//Flag
			w.writeInt(Flags.SendDelays.getCode());
			
			//ip
			w.writeInt(MyIP.getMyIP().length());
			w.write(MyIP.getMyIP().getBytes());
			
			//tempo
			w.writeLong(time);

			w.flush();			
			return baos.toByteArray();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void SendDeviceToMaster(byte[] ipMaster, int port, Device device) throws UnknownHostException
	{
		/**
		 * Enviado pelos slaves, para medir os atrasos em unicast
		 * */
		
		ClientCom con = new ClientCom(new String(ipMaster), port);
        Message inMessage, outMessage;
        
        while (!con.open()) // aguarda ligação
        {
            try 
            {
                Thread.currentThread().sleep((long) (10));
            } 
            catch (InterruptedException e) 
            {}
        }
        
        outMessage = new Message(Message.REQ_SEND_DEVICE, device);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        
        if (inMessage.getType() != Message.ACK_SEND_DEVICE) 
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
	}

	public static byte[] NewMulticastTree()
	{
		/**
		 * Envio de um pacote simples para se estabelecer a multicast tree
		 * O pacote contem:
		 * 4 bytes para a flag
		 * */
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream w = new DataOutputStream(baos);
		  
		try 
		{	
			//Flag
			w.writeInt(Flags.EstablishMulticastTree.getCode());

			w.flush();			
			return baos.toByteArray();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void SendPermission(String deviceIp, int port)
	{
		
		ClientCom con = new ClientCom(deviceIp, port);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try 
            {
                Thread.currentThread().sleep((long) (10));
            } 
            catch (InterruptedException e) 
            {}
        }
        
        outMessage = new Message(Message.REQ_PERMISSION);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        
        if (inMessage.getType() != Message.ACK_PERMISSION) 
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
	}

	public static void SendToMasterFinishMeasures(byte[] ipMaster, int port) 
	{
		/**
		 * Enviado pelos slaves para o master a indicar que terminou de enviar pacotes com tempos, este pacoe é em unicast
		 * */
		
		ClientCom con = new ClientCom(new String(ipMaster), port);
        Message inMessage, outMessage;
        
        while (!con.open()) // aguarda ligação
        {
            try 
            {
                Thread.currentThread().sleep((long) (10));
            } 
            catch (InterruptedException e) 
            {}
        }
        
        outMessage = new Message(Message.REQ_FINISH);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        
        if (inMessage.getType() != Message.ACK_FINISH) 
        {
            System.out.println("Thread " + Thread.currentThread().getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
		
	}

	public static byte[] AdvertiseSlavesToSendDevies()
	{
		/**
		 * Envio de um pacote simples para se estabelecer a multicast tree
		 * O pacote contem:
		 * 4 bytes para a flag
		 * */
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream w = new DataOutputStream(baos);
		  
		try 
		{	
			//Flag
			w.writeInt(Flags.EndDelays.getCode());

			w.flush();			
			return baos.toByteArray();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
