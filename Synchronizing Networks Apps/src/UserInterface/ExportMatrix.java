package UserInterface;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Entities.Device;
import Entities.Neighbour;
import UserInterface.ValidationFunctions.*;

public class ExportMatrix 
{
	public static List<Device> getAllDataFromMatrix(JPanel matrixPanel, int row)
	{
		Component[] components = matrixPanel.getComponents();

		int index = 0;
		int countButtons = 0;
		List<Device> allDevices = new ArrayList<>();
		List<Device> matrix = new ArrayList<>();
		Device tmp = null;
	    for (int i = 0; i < components.length; i++) 
	    {
	    	if(components[i].getClass().getName().toString().equals("javax.swing.JButton"))
	    	{
	    		JButton buttonTemp = (JButton)components[i];
	    		countButtons++;
	    		if(countButtons>1)
	    		{
	    			if(countButtons<=row)
	    				allDevices.add(new Device(buttonTemp.getText()));
	    			else
	    				tmp = new Device(buttonTemp.getText());
	    		}
	    	}

	    	if(components[i].getClass().getName().toString().equals("javax.swing.JTextField"))
	        {
	    		JTextField textTemp = (JTextField)components[i];
	        	index++;
	        	Neighbour neighbourTmp = new Neighbour(allDevices.get(index-1).getDeviceName(),Double.parseDouble(textTemp.getText()));
	        	tmp.addNewNeighbour(neighbourTmp);
	        	if(index == row-1)
	        	{
	        		matrix.add(tmp);
	        		index = 0;
	        	}
	        }
	    }
	    
	    return organizeMatrix(matrix);
	}
	
	private static List<Device> organizeMatrix(List<Device> matrix)
	{
		/**
		 * � suposto fazer as medias de caminhos diferentes entre os mesmo pontos
		 * */
		return matrix;
	}
	
	public static boolean validMatrixInJPanel(JPanel matrixPanel, int row)
	{
		Component[] components = matrixPanel.getComponents();

		int index = 0;
		int diagonalFields = row+2;
	    for (int i = 0; i < components.length; i++) 
	    {
	    	index++;
	    	if(components[i].getClass().getName().toString().equals("javax.swing.JTextField"))
	        {
	    		JTextField textTemp = (JTextField)components[i];
	    		if(index == diagonalFields)
	        	{
	    			diagonalFields += row+1;
	    			if(!textTemp.getText().equals("0"))
	    				return false; //Diagonal incorrecta
	        	}
	    		else
	    		{
	    			if(!ValidationFunctions.isNumeric(textTemp.getText()) || textTemp.getText().equals("0"))
	    				return false;
	    		}
	        }
	    }		
		return true;
	}
}
