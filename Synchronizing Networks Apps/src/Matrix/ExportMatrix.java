package Matrix;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import Entities.Device;

public class ExportMatrix
{
	private List<Device> devicesList;
	
	public ExportMatrix(List<Device> devicesList) 
	{
		this.devicesList = devicesList;
	}
	
	public void saveInFile(String absolutePath) 
	{
		File file;
		if(absolutePath.length() >0)
			file = new File(absolutePath+"/SNAMatrix.txt");
		else
			file = new File("SNAMatrix.txt");
		
		FileOutputStream fos; 
		try
		{
			fos = new FileOutputStream(file);
			fos.write("Synchronizing Networks Apps Matrix".getBytes());
			fos.write(System.getProperty("line.separator").getBytes());
			
			fos.write(("number of diveces:" + devicesList.size()).getBytes());
			fos.write(System.getProperty("line.separator").getBytes());
			
			for(Device tmp: devicesList)
			{
				fos.write(tmp.getDeviceName().getBytes());
				fos.write(";".getBytes());
			}
			fos.write(System.getProperty("line.separator").getBytes());
			
			for(Device tmp: devicesList)
			{
				fos.write(lineWithDistanceToWriteInFile(tmp).getBytes());
				fos.write(System.getProperty("line.separator").getBytes());
			}
			
			
			fos.close();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	private String lineWithDistanceToWriteInFile(Device device)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(device.getDeviceName());
		sb.append(":");
		for(Device tmp: devicesList)
		{
			sb.append(device.lookForDistanceByNeighbourName(tmp.getDeviceName()));
			sb.append(";");
		}
		
		return sb.toString();
	}
	
	
}
