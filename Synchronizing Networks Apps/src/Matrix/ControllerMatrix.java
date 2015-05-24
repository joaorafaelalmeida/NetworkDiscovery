package Matrix;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Entities.Device;
import Entities.Neighbour;
import UserInterface.ValidationFunctions;
import UserInterface.ValidationFunctions.*;

public class ControllerMatrix 
{
	/**
	 * Le os dados da tabela e adiciona numa lista de dispositivos chamados de Device
	 * Cada device tem os seus vizinhos e as distancias até eles numa lista interna de Neighbourgs
	 * */
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
	        	Neighbour neighbourTmp = new Neighbour(allDevices.get(index-1).getDeviceName(), Double.parseDouble(textTemp.getText()));
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
	
	/**
	 * Organiza a matrix, de modo a que a distance entre dois pontos seja igual
	 * */
	private static List<Device> organizeMatrix(List<Device> matrix)
	{
		double distance = 0;
		for(Device selected: matrix)
			for(Neighbour neigh: selected.getNeighbours())
				if(!neigh.getDeviceName().equals(selected.getDeviceName()))
					for(Device others: matrix)
						if(neigh.getDeviceName().equals(others.getDeviceName()))
						{
							distance = (neigh.getDistance()+others.lookForDistanceByNeighbourName(selected.getDeviceName()))/2;
							neigh.resetDistance(distance);
							others.setDistanceToNeighbourByName(selected.getDeviceName(), distance);
						}		
		return matrix;
	}
	
	/**
	 * Valida os dados que estão na tabela
	 * Existe dois tipos de erros possiveis, pode ser mudado depois os return false, para excepçoes
	 * */
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
	    			//Diagonal incorrecta
	    			diagonalFields += row+1;
	    			if(!ValidationFunctions.isNumeric(textTemp.getText()))
	    				return false; 
	    			if(Double.parseDouble(textTemp.getText()) != 0)
	    				return false;
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
	
	public static void removeNeighbourFromAllNeighbourList(List<Device> list, String text)
	{
		for(Device tmp : list)
			tmp.removeNeighbour(text);
	}
}
