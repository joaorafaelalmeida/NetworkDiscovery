package ConClasses;

public class Proxy extends Thread
{
    private static int nProxy = 0;

    private ServerCom sconi;

    private Interface inter;

    public Proxy(ServerCom sconi, Interface inter) 
    {
        super("Proxy_" + Proxy.getProxyId());
        this.sconi = sconi;
        this.inter = inter;
    }

    @Override
    public void run() 
    {
        Message inMessage = null,                       // mensagem de entrada
                outMessage = null;                      // mensagem de sa?da
        

        inMessage = (Message) sconi.readObject();       // ler pedido do cliente
        try 
        {
            outMessage = inter.processAndReply(inMessage);         // process?-lo
        } 
        catch (MessageException e) 
        {
        	System.out.println("Thread " + getName() + ": " + e.getMessage() + "!");
        	System.out.println(e.getMessageVal().toString());
            System.exit(1);
        }
        sconi.writeObject(outMessage);                                  // enviar resposta ao cliente
        sconi.close();                                                  // fechar canal de comunica??o
    }

    private static int getProxyId() 
    {
        Class<?> cl = null;                                  
        int proxyId;                                         

        try 
        {
            cl = Class.forName("ConClasses.Proxy");
        } 
        catch (ClassNotFoundException e) 
        {
            System.out.println("O tipo de dados ClientProxy n?o foi encontrado!");
            e.printStackTrace();
            System.exit(1);
        }

        synchronized (cl) 
        {
            proxyId = nProxy;
            nProxy += 1;
        }

        return proxyId;
    }
}