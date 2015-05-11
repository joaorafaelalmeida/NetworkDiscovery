package Entities;

import java.util.ArrayList;
import java.util.List;

public class Device 
{
	private final String deviceName;
	private List<Neighbour> neighbours;
	private String ipAddress;
	private Routers gateway;
	
	public Device(String deviceName)
	{
		this.deviceName = deviceName;
		neighbours = new ArrayList<>();
		this.ipAddress = "empty";
	}
	
	public Device(String deviceName, String ip)
	{
		this.deviceName = deviceName;
		neighbours = new ArrayList<>();
		this.ipAddress = ip;
	}
	
	public void addNewNeighbour(Neighbour neighbour)
	{
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
				tmp.setDistance(distance);
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

	
	
	
}
