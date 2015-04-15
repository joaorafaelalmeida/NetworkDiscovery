package SNA;

import java.awt.EventQueue;

import UserInterface.GraphicInterface;

public class SNA 
{
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) 
	{
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
	}
}
