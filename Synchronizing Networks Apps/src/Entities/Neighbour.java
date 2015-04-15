package Entities;


public class Neighbour 
{
	private final String deviceName;
	private double distance;
	
	public Neighbour(String deviceName, double distance)
	{
		this.deviceName = deviceName;
		this.distance = distance;
	}
	
	public Neighbour(String deviceName)
	{
		this.deviceName = deviceName;
		this.distance = 0;
	}

	public String getDeviceName() 
	{
		return deviceName;
	}

	public double getDistance()
	{
		return distance;
	}

	public void setDistance(double distance) 
	{
		this.distance = distance;
	}
	
	
	
	
}