package Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Routers implements Serializable
{
	private List<Device> connectedDevices;
	private List<Routers> connectedRouters;
	private String id;
	
	public Routers(String id)
	{
		this.id = id;
		connectedDevices = new ArrayList();
		connectedRouters = new ArrayList();
	}
	
	public void addDevice(Device d)
	{
		connectedDevices.add(d);
	}

	public void addRouter(Routers r)
	{
		connectedRouters.add(r);
	}
	
	
}
