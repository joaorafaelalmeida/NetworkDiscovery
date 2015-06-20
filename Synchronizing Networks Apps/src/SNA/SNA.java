package SNA;

import java.awt.EventQueue;
import java.net.InetAddress;
import java.net.UnknownHostException;

import MeasureDelays.MeasureDelays;
import MeasureDelays.Type;
import UserInterface.GraphicInterface;

public class SNA 
{
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) 
	{
		switch(args.length)
		{
			case 0:
				EventQueue.invokeLater(new Runnable() 
				{
					public void run() 
					{
						try 
						{
							//StartMenuSNA window = new StartMenuSNA();
							GraphicInterface window = new GraphicInterface();
							window.getFrame().setVisible(true);
						} 
						catch (Exception e) 
						{
							e.printStackTrace();
						}
					}
				});
				break;
			case 1:
				/**
				 * Recebe o ficheiro config  ou o ip do multicast ou o help
				 * */
				CommandLineFunctions.OpcionWithOneParameter(args[0]);
				break;
			default: System.out.println("Invalid arguments!");
		}
	}
}
