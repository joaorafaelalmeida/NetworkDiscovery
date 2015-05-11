package Topology;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Entities.Device;
import Entities.Neighbour;
import Entities.Routers;

public class ImportTopology 
{
	private File file;
	private List<Routers> routersList;
	
	public ImportTopology(File file)
	{	
		this.file = file;
		this.routersList = getElementListFromFile();
	}
	
	public List<Routers> getElementListFromFile()
	{
		List<Routers> tmp = new ArrayList<>();
		BufferedReader in;
		try 
		{
			in = new BufferedReader(new FileReader(file));
			String line = in.readLine();//Titulo do ficheiro
			int rows = Integer.parseInt(in.readLine().split(":")[1]);//Numero de dispositivos
			
			for(int i = 0; i < rows; i++)
			{
				line = in.readLine();
				String[] dataReaded = line.split(";");//[0] nome, [1] ip, [2] gateway
				//tmp.add();
			}
			
			
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tmp;
	}
}
