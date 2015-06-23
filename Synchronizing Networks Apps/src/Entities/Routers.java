package Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Routers implements Serializable
{
	private List<Device> connectedDevices;
	private List<Routers> connectedRouters;
	private String id;
	private Location loc;
	
	public Routers(String id)
	{
		this.id = id;
		connectedDevices = new ArrayList();
		connectedRouters = new ArrayList();
	}
	
	public String getRouterName()
	{
		return id;
	}
	
	public void addDevice(Device d)
	{
		connectedDevices.add(d);
	}

	public void addRouter(Routers r)
	{
		if(!existRouter(r))
			connectedRouters.add(r);
	}
	
	private boolean existRouter(Routers r)
	{
		for (Routers router: connectedRouters) 
			if(router.id.equals(r.id))
				return true;
		return false;
		
	}
	
	public boolean existDeviceByName(String deviceIP)
	{
		for (Device device: connectedDevices) 
			if(device.getDeviceName().equals(deviceIP))
				return true;
		return false;
		
	}
	
	public void setLocation(Location l)
	{
		loc = l;
	}
	
	public Location getLocation()
	{
		return loc;
	}
	
	public List<Device> getConnectedDevices()
	{
		return connectedDevices;
	}
	
	public List<Routers> getConnectedRouters()
	{
		return connectedRouters;
	}
}
