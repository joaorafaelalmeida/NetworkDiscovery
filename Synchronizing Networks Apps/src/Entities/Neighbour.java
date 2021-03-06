package Entities;

import java.io.Serializable;


public class Neighbour implements Serializable
{
	private final String deviceName;
	private double[] distance;
	
	public Neighbour(String deviceName, double distance)
	{
		this.deviceName = deviceName;
		this.distance = new double[1];
		this.distance[0] = distance;
	}
	
	public Neighbour(String deviceName)
	{
		this.deviceName = deviceName;
		this.distance = new double[0];
	}

	public void resetDistance(double distance)
	{
		this.distance = new double[1];
		this.distance[0] = distance;
	}
	
	public String getDeviceName() 
	{
		return deviceName;
	}

	public double getDistance()
	{
		double tmpDistance = 0;
		for (int i = 0; i < distance.length; i++) 
			tmpDistance += distance[i];
		return tmpDistance/(distance.length);
	}
	
	public double getDistanceTwoDecimal()
	{
		double tmpDistance = 0;
		for (int i = 0; i < distance.length; i++) 
			tmpDistance += distance[i];
		return ((double)((int)(tmpDistance/(distance.length)*100)))/100;
	}
	
	public void setDistance(double dist) 
	{
		double[] tmp = new double[distance.length+1];
		for (int i = 0; i < distance.length; i++) 
			tmp[i] = distance[i];
		tmp[distance.length] = dist;
		distance = tmp;
	}

	@Override
	public String toString() 
	{
		return "Neighbour [deviceName=" + deviceName + ", distance=" + getDistance()
				+ "]";
	}
	
	
	
	
	
}
