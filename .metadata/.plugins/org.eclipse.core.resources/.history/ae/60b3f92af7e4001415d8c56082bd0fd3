package UserInterface;

import java.util.List;

import Entities.Device;


public class ValidationFunctions 
{
	public static boolean validDeviceToInsert(List<Device> list, String text)
	{
		if(text != null)
			if(text.length()>0)
				if(!existsDevice(list, text))
					return true;

		return false;
	}
	
	public static boolean validDeviceToDelete(List<Device> list, String text)
	{
		if(text != null)
			if(text.length()>0)
				if(existsDevice(list, text))
					return true;

		return false;
	}
	
	public static boolean existsDevice(List<Device> list, String text)
	{
		for(Device tmp: list)
			if(tmp.getDeviceName().equals(text))
				return true;
		return false;
	}
	
	public static int getIndexFromList(List<Device> list, String text)
	{
		for(int index = 0; index < list.size(); index ++)
			if(list.get(index).getDeviceName().equals(text))
				return index;
		return -1;
	}
	
	public static boolean isNumeric(String str)  
	{  
		try  
		{  
			double d = Double.parseDouble(str);  
		}  
		catch(NumberFormatException nfe)  
		{  
			return false;  
		}  
		return true;  
	}
}
