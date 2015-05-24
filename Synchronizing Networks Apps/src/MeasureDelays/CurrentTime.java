package MeasureDelays;

public class CurrentTime 
{
	private long serverTime;
	private long machineTime;
	
	public CurrentTime()
	{
		machineTime = System.nanoTime();
	}
	
	public void setServerTime(long time)
	{
		serverTime = time;
	}
	
	public void ajustServerTime(long time)
	{
		serverTime += time;
	}
	
	public long getServerTime()
	{
		return serverTime + (System.nanoTime()-machineTime);
	}
}
