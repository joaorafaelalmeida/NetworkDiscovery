package UserInterface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import Entities.Device;

public class MatrixFile 
{
	private List<Device> devicesList;
	
	public MatrixFile(List<Device> devicesList) 
	{
		this.devicesList = devicesList;
	}
	
	public void saveInFile(String absolutePath) 
	{
		File file = new File(absolutePath+"/SNAMatrix.txt");
		FileOutputStream fos; 
		try
		{
			fos = new FileOutputStream(file);
			fos.write("Synchronizing Networks Apps Matrix".getBytes());
			fos.write(System.getProperty("line.separator").getBytes());
			
			fos.write(("number of diveces:" + devicesList.size()).getBytes());
			fos.write(System.getProperty("line.separator").getBytes());
			
			fos.write("\t".getBytes());
			for(Device tmp: devicesList)
			{
				fos.write(tmp.getDeviceName().getBytes());
				fos.write("\t".getBytes());
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
		sb.append(":\t");
		for(Device tmp: devicesList)
		{
			sb.append(device.lookForDistanceByNeighbourName(tmp.getDeviceName()));
			sb.append("\t");
		}
		
		return sb.toString();
	}
	
	public int getNumTotalDevices()
	{
		return devicesList.size();
	}
	
	public double[][] getMatrixInDoubleArray()
	{
		double[][] matrix = new double[devicesList.size()][devicesList.size()];
		
		for(int i = 0; i < devicesList.size(); i++)
			for(int j = 0; j < devicesList.size(); j++)
			{
				matrix[i][j] = devicesList.get(i).lookForDistanceByNeighbourName(devicesList.get(j).getDeviceName());
			}
		
		return matrix;
	}
}
