package SNA;

import MeasureDelays.MeasureDelays;
import MeasureDelays.MyIP;
import MeasureDelays.Type;
import UserInterface.ControllerInterface;

public class CommandLineFunctions 
{
	public static void OpcionWithOneParameter(String parameter)
	{
		if(parameter.equals("-help"))
		{
			showHelp();
		}
		else
			if(ControllerInterface.isIP(parameter))
			{
				System.out.println("SNA Command Line");
				while(true)
				{
					try 
					{
						Thread ptp = new Thread(new MeasureDelays(parameter, Type.SLAVE, 8888, MyIP.getMyIP()));
						ptp.start();
						ptp.join();
					} 
					catch (InterruptedException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			else
			{
				System.out.println("Opção invalida!");
			}
				
	}
	
	private static void showHelp()
	{
		System.out.println("SNA Help");
		System.out.println("No parameters: Run graphic aplication");
		System.out.println("SNA -help: Show helps");
		//System.out.println("SNA [file]: Read all parameters from file");
		System.out.println("SNA [ip]: ip multicast");
	}
	
	
}
