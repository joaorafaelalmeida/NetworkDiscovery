package Matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import Entities.Device;

public class Matrix 
{
	public static List<Device> devices = new ArrayList<Device>();
	public static Semaphore acess = new Semaphore(1);
	
	public static void addDevice(Device dev)
	{
		try 
		{
			acess.acquire();
			devices.add(dev);
			acess.release();
		} 
		catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
