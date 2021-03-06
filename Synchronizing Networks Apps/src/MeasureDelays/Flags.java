package MeasureDelays;

public enum Flags 
{
	Sync(1), SendTimeSync(2), StartReadSlaves(3), SendDelays(4), EndDelays(5), EstablishMulticastTree(6);
	
	private int code;
	
	private Flags(int code)
	{
		this.code = code;
	}
	
	public int getCode()
	{
		return code;
	}
	
	public static Flags getFlagByCode(int code)
	{
		for(Flags tmp: Flags.values())
			if(tmp.getCode() == code)
				return tmp;
		return null;
	}
}
