package UserInterface;

import java.util.List;

import Entities.Device;


public class ControllerValidation 
{
	public boolean validDeviceToInsert(List<Device> list, String text)
	{
		if(text != null)
			if(text.length()>0)
				if(!existsDevice(list, text))
					return true;

		return false;
	}
	
	public boolean validDeviceToDelete(List<Device> list, String text)
	{
		if(text != null)
			if(text.length()>0)
				if(existsDevice(list, text))
					return true;

		return false;
	}
	
	public boolean existsDevice(List<Device> list, String text)
	{
		for(Device tmp: list)
			if(tmp.getDeviceName().equals(text))
				return true;
		return false;
	}
	
	public int getIndexFromList(List<Device> list, String text)
	{
		for(int index = 0; index < list.size(); index ++)
			if(list.get(index).getDeviceName().equals(text))
				return index;
		return -1;
	}
}