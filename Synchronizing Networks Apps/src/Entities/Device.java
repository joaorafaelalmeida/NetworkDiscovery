package Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Device implements Serializable
{
	private final String deviceName;
	private List<Neighbour> neighbours;
	private String ipAddress;
	private Routers gateway;
	private Location loc;
	
	public Device(String deviceName)
	{
		this.deviceName = deviceName;
		neighbours = new ArrayList<>();
		this.ipAddress = deviceName;
	}
	
	public Device(String deviceName, String ip)
	{
		this.deviceName = deviceName;
		neighbours = new ArrayList<>();
		this.ipAddress = ip;
	}
	
	public void addNewNeighbour(Neighbour neighbour)
	{
		for(Neighbour tmp: neighbours)
			if(tmp.getDeviceName().equals(neighbour.getDeviceName()))
			{
				tmp.setDistance(neighbour.getDistance());
				return;
			}
		neighbours.add(neighbour);
	}

	public String getDeviceName() 
	{
		return deviceName;
	}

	public List<Neighbour> getNeighbours() 
	{
		return neighbours;
	}
	
	public double lookForDistanceByNeighbourName(String name)
	{
		for(Neighbour tmp: neighbours)
			if(tmp.getDeviceName().equals(name))
				return tmp.getDistance();
		return -1;
	}

	public void setDistanceToNeighbourByName(String name, double distance)
	{
		for(Neighbour tmp: neighbours)
			if(tmp.getDeviceName().equals(name))
				tmp.resetDistance(distance);
	}
	
	public void removeNeighbour(String name)
	{
		for(int i = 0; i < neighbours.size(); i++)
			if(neighbours.get(i).getDeviceName().equals(name))
				neighbours.remove(i);
	}
	
	@Override
	public String toString() 
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Device: deviceName=");
		sb.append(deviceName);
		sb.append(", neighbours=\n");
		for(Neighbour myNeighbours: neighbours)
		{
			sb.append(myNeighbours);
			sb.append("\n");
		}
		return sb.toString();
	}

	public Routers getGateway() 
	{
		return gateway;
	}

	public void setGateway(Routers gateway) 
	{
		this.gateway = gateway;
	}

	
	public void setLocation(Location l)
	{
		loc = l;
	}
	
	public Location getLocation()
	{
		return loc;
	}
	
}
