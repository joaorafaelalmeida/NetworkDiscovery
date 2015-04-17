package UserInterface;

import java.awt.GridLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Entities.Device;

public class ControllerInterface 
{
	public void reloadMatrix(JPanel matrixPanel, int row, List<Device> devicesList)
	{
		matrixPanel.setLayout(new GridLayout(row, row));
		for(int i = 0; i<row; i++)
			if(i==0)
				matrixPanel.add(new JButton(""));
			else
				matrixPanel.add(new JButton(devicesList.get(i-1).getDeviceName()));
		
		for(int j = 1; j<row; j++)
			for(int i = 0; i<row; i++)
			{
				if(i == 0)
				{
					matrixPanel.add(new JButton(devicesList.get(j-1).getDeviceName()));
				}
				else
				{
					if(ValidationFunctions.existsNeighbour(devicesList.get(j-1).getNeighbours(),devicesList.get(i-1).getDeviceName()))
					{
						String distance = String.valueOf(devicesList.get(j-1).getNeighbours().get(i-1).getDistance());
						matrixPanel.add(new JTextField(distance));
					}
					else
						if(i==j)
							matrixPanel.add(new JTextField("0.0"));
						else
							matrixPanel.add(new JTextField());
				}
			}
	}

	
}
