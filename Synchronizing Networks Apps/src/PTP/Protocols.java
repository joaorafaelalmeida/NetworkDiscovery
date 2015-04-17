package PTP;

public class Protocols 
{
	/**
	 * Protocolos que iremos usar, nao tem argumentos e nao retornam nada por agora
	 * depois possivelmente vao ser alterados
	 * */
	public void Sync()
	{
		/**
		 * Pacote enviado pelo mestre, vai conter apenas o id de sess�o e o seu endere�o ip
		 * */
	}
	
	public void SendTimeSync()
	{
		/**
		 * Pacote enviado pelo mestre, vai conter o id de sessao e o hora de envio do sync
		 * */
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
