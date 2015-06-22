package Matrix;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Entities.Device;
import Entities.Neighbour;

public class ImportMatrix 
{
	private File file;
	private List<Device> devicesList;
	
	public ImportMatrix(File file)
	{	
		this.file = file;
		this.devicesList = getListOfDevicesFromFile();
	}
	
	public ImportMatrix(List<Device> devicesList)
	{	
		this.devicesList = devicesList;
	}
	
	public List<Device> getListOfDevicesFromFile()
	{
		List<Device> tmp = new ArrayList<>();
		BufferedReader in;
		try 
		{
			in = new BufferedReader(new FileReader(file));
			String line = in.readLine();//Titulo do ficheiro
			int rows = Integer.parseInt(in.readLine().split(":")[1]);//Numero de dispositivos
			
			//Dispositivos
			line = in.readLine();
			String[] devicesName = line.split(";");
			for (int i = 0; i < devicesName.length; i++) 
				tmp.add(new Device(devicesName[i]));
			
			for(int i = 0; i < rows; i++)
			{
				line = in.readLine();
				String[] tmpLine = line.split(":");//tmpLine[0] = deviceName, tmpLine[1] = distancias separadas por ";"
				String[] distances = tmpLine[1].split(";");
				for(Device device: tmp)
					if(device.getDeviceName().equals(tmpLine[0]))
						for(int j = 0; j < rows; j++)
							device.addNewNeighbour(new Neighbour(tmp.get(j).getDeviceName(),Double.parseDouble((distances[j]))));
			}
			
			
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tmp;
	}
	
	public List<Device> getMatrixInList()
	{
		return devicesList;
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
	
	public String[] getDevicesNameInStringArray()
	{
		String[] names = new String[devicesList.size()];
		for (int i = 0; i < devicesList.size(); i++) 
			names[i] = devicesList.get(i).getDeviceName();
		return names;
	}
}
