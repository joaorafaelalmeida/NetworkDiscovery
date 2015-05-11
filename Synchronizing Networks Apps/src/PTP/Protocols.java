package PTP;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Protocols 
{
	/**
	 * Protocolos que iremos usar, nao tem argumentos e nao retornam nada por agora
	 * depois possivelmente vao ser alterados
	 * */
	public static byte[] Sync(String ip, int sessionID, int packID)
	{
		/**
		 * Pacote enviado pelo mestre, vai conter apenas o id de sess�o e o seu endere�o ip
		 * Formato do pacote:
		 * 4 bytes para o tamanho do id da sessao
		 * 4 bytes para o tamanho do id do pacote
		 * 4 bytes para o tamanho do endere�o ip
		 * x bytes para o endere�o ip
		 * */
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream w = new DataOutputStream(baos);
		  
		try 
		{
			//Id da sessao
			w.writeInt(sessionID);
			w.writeInt(packID);
			
			//Endere�o ip do master
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
		 * 4 bytes para o tamanho do id da sessao
		 * 4 bytes para o tamanho do id do pacote
		 * 8 bytes para o tempo atual
		 * */
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream w = new DataOutputStream(baos);
		  
		try 
		{		
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
	
	public void DelayRequest()
	{
		/**
		 * Enviado pelos slaves, vai conter o id de sessao, ip e nome(talvez)
		 * */
	}
	
	public void DelayResponse()
	{
		/**
		 * Enviado pelo mestre, vai conter o id de sessao, e a hora de rece��o do delayrequest
		 * */
	}
	
	public void TimeOk()
	{
		/**
		 * Enviado pelo mestre quando o espa�o temporal � aceitavel, vai conter o id de sessao e ok 
		 * */
	}
	
}
