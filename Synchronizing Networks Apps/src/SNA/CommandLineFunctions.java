package SNA;

public class CommandLineFunctions 
{
	public static void OpcionWithOneParameter(String parameter)
	{
		if(parameter.equals("-help"))
		{
			showHelp();
		}
		else
			if(parameter.equals("file"))//Change this
			{
				
			}
	}
	
	private static void showHelp()
	{
		System.out.println("Help");
		System.out.println("No parameters: Run graphic aplication");
		System.out.println("-help: Show helps");
		System.out.println("file: Read all parameters from file");
		System.out.println("ip: ip multicast");
		System.out.println("ip hostname: Host name from matrix");
	}
}
