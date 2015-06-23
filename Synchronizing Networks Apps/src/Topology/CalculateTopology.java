package Topology;
import java.util.ArrayList;
import java.util.List;

import Entities.*;
import Formulations.*;

public class CalculateTopology 
{
	private List<Device> devices;
	private List<Routers> routers;
	
	public CalculateTopology(String algoritmo, List<Device> devicesMatrix)
	{
		devices = new ArrayList<Device>();
		routers = new ArrayList<Routers>();
		
		if(algoritmo.equals("Exact Formulation"))
		{
			ExactFormulation calculo = new ExactFormulation(devicesMatrix);
			devices = calculo.getListDevices();
			routers = calculo.getListRouter();
		}
		
		if(algoritmo.equals("Feasibility Pump"))
		{
			FeasibilityPump calculo = new FeasibilityPump(devicesMatrix);
			devices = calculo.getListDevices();
			routers = calculo.getListRouter();
		}
		
		if(algoritmo.equals("Robust Formulation"))
		{
			Robust calculo = new Robust(devicesMatrix);
			devices = calculo.getListDevices();
			routers = calculo.getListRouter();
		}
		organizeDeviceList();
	}

	private void organizeDeviceList()
	{
		List<Device> tmp = new ArrayList<Device>();
		for(Routers router: routers)
			for(Device device: devices)
				if(router.existDeviceByName(device.getDeviceName()))
				{
					tmp.add(device);
					
				}
		devices = tmp;
	}
	
	public List<Device> getDevices() 
	{
		return devices;
	}

	public List<Routers> getRouters()
	{
		return routers;
	}
	
	

}
