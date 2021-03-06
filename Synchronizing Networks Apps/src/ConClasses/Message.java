package ConClasses;

import java.io.Serializable;

import Entities.Device;

public class Message implements Serializable
{
    public static final int DELAY_REQUEST = 1;
    public static final int DELAY_RESPONSE = 2; 
    public static final int REQ_SEND_SLAVE_IP = 3;
    public static final int ACK_SEND_SLAVE_IP = 4;
    public static final int REQ_SEND_DEVICE = 5;
    public static final int ACK_SEND_DEVICE = 6;
    public static final int REQ_PERMISSION = 7;
    public static final int ACK_PERMISSION = 8;    
    public static final int REQ_FINISH = 9;
    public static final int ACK_FINISH = 10;
    
    private int msgType;
    private long time;
    private byte[] ip;
    private Device device;
    
    public Message (int type)
    {
    	msgType = type;
    }
    
    public Message (int type, byte[] ip)
    {
        msgType = type;
        this.ip = ip;
    }
    
    public Message (int type, long time)
    {
        msgType = type;
        this.time = time;
    }
    
    public Message(int type, Device dev)
    {
    	msgType = type;
    	device = dev;
    }

    
    public int getType()
    {
       return msgType;
    }
    
    public long getTime()
    {
       return time;
    }
    
    public String getIp()
    {
    	return new String(ip);
    }

    public Device getDevice()
    {
    	return device;
    }
    @Override
    public String toString() 
    {
        return "Message{" + "msgType=" + msgType + ", time=" + time +'}';
    }
}
