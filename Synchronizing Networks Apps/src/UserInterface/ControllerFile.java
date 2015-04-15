package UserInterface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import Entities.Device;

public class ControllerFile 
{
	public void save(String absolutePath, List<Device> devicesList) 
	{
		File file = new File(absolutePath+"/SNAMatix.txt");
		FileOutputStream fos; 
		try
		{
			fos = new FileOutputStream(file);
			fos.write("Deu".getBytes());
			
			fos.close();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
}