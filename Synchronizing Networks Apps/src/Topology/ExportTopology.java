package Topology;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import Entities.Device;
import Entities.Routers;

public class ExportTopology 
{
	private List<Routers> routerList;
	
	public ExportTopology(List<Routers> routersList) 
	{
		this.routerList = routersList;
	}
	
	public void saveInFile(String absolutePath) 
	{
		File file;
		if(absolutePath.length() >0)
			file = new File(absolutePath+"/NTATopology.txt");
		else
			file = new File("NTATopology.txt");
		
		FileOutputStream fos; 
		try
		{
			fos = new FileOutputStream(file);
			fos.write("Synchronizing Networks Apps Topology".getBytes());
			fos.write(System.getProperty("line.separator").getBytes());
			
			fos.write(("number of routers:" + routerList.size()).getBytes());
			fos.write(System.getProperty("line.separator").getBytes());
			
			for(Routers tmp: routerList)
			{
				fos.write(lineToWriteInFile(tmp).getBytes());
				fos.write(System.getProperty("line.separator").getBytes());
			}
			fos.close();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	private String lineToWriteInFile(Routers router)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(router.getRouterName());
		sb.append(";");
		sb.append(router.getConnectedDevices().size()).append(";");
		for(Device tmp: router.getConnectedDevices())
			sb.append(tmp.getDeviceName()).append(";");
		
		sb.append(router.getConnectedRouters().size()).append(";");
		for(Routers tmp: router.getConnectedRouters())
			sb.append(tmp.getRouterName()).append(";");
		
		return sb.toString();
	}
}
