package NTA;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;

import Entities.Device;
import Entities.Neighbour;
import Entities.Routers;
import Matrix.ImportMatrix;

public class NTAControler 
{
	private static List<Device> devices = new ArrayList<Device>();
	
	public static List<Device> readDevicesFromFile()
	{
		return devices;
	}
	
	public static List<Routers> readRoutersFromFile(File file)
	{
		List<Routers> router = new ArrayList<Routers>();
		devices = new ArrayList<Device>();
		BufferedReader in;
		try 
		{
			in = new BufferedReader(new FileReader(file));
			String line = in.readLine();//Titulo do ficheiro
			int routers = Integer.parseInt(in.readLine().split(":")[1]);//Numero de routers
			
			//Routers
			for (int i = 0; i < routers; i++) 
			{
				line = in.readLine();
				String[] routerInfo = line.split(";");
				//gateway;n?de devices;pc1;pc3;n?routes;router1;
				Routers routerTmp = new Routers(routerInfo[0]);
				for (int deviceNumber = 0; deviceNumber < Integer.parseInt(routerInfo[1]); deviceNumber++)
				{
					Device tmp = new Device(routerInfo[(deviceNumber+2)]);
					devices.add(tmp);
					tmp.setGateway(routerTmp);
					routerTmp.addDevice(tmp);
				}
				
				for (int routerNumber = 0; routerNumber < Integer.parseInt(routerInfo[Integer.parseInt(routerInfo[1])+2]); routerNumber++)
				{
					String id = routerInfo[(routerNumber+Integer.parseInt(routerInfo[1]))+3];
					Routers tmp = getRouterFromListByID(router, id);
					if(tmp == null)
						tmp = new Routers(id);
					tmp.addRouter(routerTmp);
					routerTmp.addRouter(tmp);
				}
				router.add(routerTmp);
				
			}
			return router;

		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return router;
	}
	
	public static Routers getRouterFromListByID(List<Routers> router, String id)
	{
		for (Routers routers : router) 
			if(routers.getRouterName().equals(id))
				return routers;
		return null;
	}
}
