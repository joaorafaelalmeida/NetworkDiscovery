package Entities;

import java.util.ArrayList;
import java.util.List;

public class Device 
{
	private final String deviceName;
	private List<Neighbour> neighbours;
	
	public Device(String deviceName)
	{
		this.deviceName = deviceName;
		neighbours = new ArrayList<>();
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

	
	
	
}
