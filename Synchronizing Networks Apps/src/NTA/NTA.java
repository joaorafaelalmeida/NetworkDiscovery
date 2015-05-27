package NTA;

import java.awt.EventQueue;

public class NTA {

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
					GraphicInterface window;
					//ISTO TALVEZ ESTEJA MAL
					/*if(args.length>0)
						window = new GraphicInterface(args[0]);
					else*/
						window = new GraphicInterface();
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
