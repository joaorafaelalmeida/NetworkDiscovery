package SNA;

import java.awt.EventQueue;

import PTP.PTP;
import PTP.Type;
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
							GraphicInterface window = new GraphicInterface();
							window.getFrame().setVisible(true);
						} 
						catch (Exception e) 
						{
							e.printStackTrace();
						}
					}
				});
				
				Thread ptp = new Thread(new PTP("225.4.5.6", Type.SLAVE, 6969));//Estes dados vao ser lidos de um ficheiro config
				ptp.start();
				try 
				{
					ptp.join();
				} 
				catch (InterruptedException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case 1:
				/**
				 * Recebe o ficheiro config  ou o ip do multicast ou o help
				 * */
				CommandLineFunctions.OpcionWithOneParameter(args[0]);
				break;
			case 2:
				/**
				 * Recebe o ip do multicast e o nome do host
				 * */
				break;
			default: System.out.println("Invalid arguments!");
		}
	}
}
