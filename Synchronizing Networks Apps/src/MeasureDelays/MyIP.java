package MeasureDelays;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

public class MyIP 
{
	public static String getMyIP()
	{
		try
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
		                if (addr instanceof Inet4Address) 
		                	return addr.toString().split("/")[1];
		        }
		    }		    
		}
		catch(SocketException e)
		{
			System.out.println("SocketException: " + e);
		}
		return null;
	}
}
