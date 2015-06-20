package ConClasses;

import java.util.concurrent.Semaphore;

import MeasureDelays.MeasureDelays;

public class Interface 
{
	//private final RepositorioGeral logger;
	private Semaphore acess;
    public Interface() 
    {
    	acess = new Semaphore(1);
    }

    public Message processAndReply(Message inMessage) throws MessageException 
    {
        Message outMessage = null;                           // mensagem de resposta

        /* valida��o da mensagem recebida */
        switch (inMessage.getType()) 
        {
            case Message.DELAY_REQUEST:
            	if(inMessage.getIp() == null)
            		throw new MessageException("Ip inv�lido!", inMessage);
                break;
            case Message.REQ_SEND_SLAVE_IP:
            	if(inMessage.getIp() == null)
            		throw new MessageException("Ip inv�lido!", inMessage);
                break;
            case Message.REQ_PERMISSION:
            	break;
            case Message.REQ_SEND_DEVICE:
            	if(inMessage.getDevice() == null)
            		throw new MessageException("Invalid device", inMessage);
            	break;
            default:
                throw new MessageException("Tipo inv�lido!", inMessage);
        }

        /* seu processamento */
        switch (inMessage.getType()) 
        {
            case Message.DELAY_REQUEST:
                outMessage = new Message(Message.DELAY_RESPONSE, System.nanoTime()); 
                break;
            case Message.REQ_SEND_SLAVE_IP:
				try 
				{
					acess.acquire();
					MeasureDelays.addDevices(inMessage.getIp());
	            	acess.release();
				} 
				catch (InterruptedException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
            	outMessage = new Message(Message.ACK_SEND_SLAVE_IP); 
                break;
            case Message.REQ_PERMISSION:
            	outMessage = new Message(Message.ACK_PERMISSION); 
            	break;
            case Message.REQ_SEND_DEVICE:
            	Matrix.Matrix.devices.add(inMessage.getDevice());
            	outMessage = new Message(Message.ACK_SEND_DEVICE); 
            	break;	
        }

        return (outMessage);
    }
}
