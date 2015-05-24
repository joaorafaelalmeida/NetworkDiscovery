import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;


public class Main {

	public static void main(String[] args) throws SocketException
	{
		Enumeration en = NetworkInterface.getNetworkInterfaces();
		List<String> tmp = new ArrayList<String>();
	    while (en.hasMoreElements()) 
	    {
	        NetworkInterface i = (NetworkInterface) en.nextElement();
	        for (Enumeration en2 = i.getInetAddresses(); en2.hasMoreElements();) 
	        {
	            InetAddress addr = (InetAddress) en2.nextElement();
	            if (!addr.isLoopbackAddress())
	            	if (!addr.isLinkLocalAddress())
	            		if (!addr)
	                if (addr instanceof Inet4Address) 
	                	tmp.add(addr.toString().split("/")[1]);
	                	
	        }
	    }
	    System.out.println("aa");
	    for(String tm: tmp)
	    	System.out.println(tm);
		
	}

}
