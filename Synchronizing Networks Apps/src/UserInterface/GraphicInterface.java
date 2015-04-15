package UserInterface;



import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JScrollPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.ImageIcon;

import Entities.Device;

public class GraphicInterface 
{
	private JFrame frame;
	private int row;
	private List<Device> devicesList;
	private JTextField newDeviceJTextField;

	public JFrame getFrame()
	{
		return frame;
	}

	/**
	 * Create the application.
	 */
	public GraphicInterface() 
	{
		row = 1;
		devicesList = new ArrayList<>();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() 
	{
		ControllerValidation control = new ControllerValidation();
		ControllerFile conFile = new ControllerFile();
		
		frame = new JFrame();
		frame.setBounds(100, 100, 600, 420);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel applicaitonPanel = new JPanel();
		frame.getContentPane().add(applicaitonPanel, BorderLayout.CENTER);
		applicaitonPanel.setLayout(null);
		
		JButton addDeviceButton = new JButton("Add new device");
		
		
		addDeviceButton.setBounds(10, 348, 151, 23);
		applicaitonPanel.add(addDeviceButton);
		
		JPanel matrixPrincipalPanel = new JPanel();
		matrixPrincipalPanel.setBounds(171, 133, 403, 238);
		applicaitonPanel.add(matrixPrincipalPanel);
		matrixPrincipalPanel.setLayout(new GridLayout(1,0));
		
		JScrollPane matrixScrollPane = new JScrollPane();
		matrixPrincipalPanel.add(matrixScrollPane);
		
		JPanel matrixPanel = new JPanel();
		matrixScrollPane.setViewportView(matrixPanel);
		matrixPanel.setLayout(new GridLayout(1, 0));
		
		newDeviceJTextField = new JTextField();
		newDeviceJTextField.setBounds(10, 280, 151, 23);
		applicaitonPanel.add(newDeviceJTextField);
		newDeviceJTextField.setColumns(10);
		
		JButton synchronizeButton = new JButton("Synchronize");
		synchronizeButton.setBounds(10, 133, 151, 23);
		applicaitonPanel.add(synchronizeButton);
		
		JButton measureDelaysButton = new JButton("Measure delays");
		measureDelaysButton.setBounds(10, 167, 151, 23);
		applicaitonPanel.add(measureDelaysButton);
		
		JLabel iconLabel = new JLabel("");
		iconLabel.setIcon(new ImageIcon("C:\\Universidade\\3\u00BA Ano - 2\u00BA Semestre\\Projecto\\pi-network-discovery\\Synchronizing Networks Apps\\routerMascotev3mini mini.png"));
		iconLabel.setBounds(10, 11, 150, 96);
		applicaitonPanel.add(iconLabel);
		
		JButton exportDataButton = new JButton("Export data");
		exportDataButton.setBounds(262, 99, 151, 23);
		applicaitonPanel.add(exportDataButton);
		
		JButton readDataButton = new JButton("Read data");
		readDataButton.setBounds(423, 99, 151, 23);
		applicaitonPanel.add(readDataButton);
		
		JButton deleteDeviceButton = new JButton("Delete device");
		deleteDeviceButton.setBounds(10, 314, 151, 23);
		applicaitonPanel.add(deleteDeviceButton);

		
		/** 
		 * Function for buttons
		 * */
		
		addDeviceButton.addActionListener(new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e) 
			{	
				if(control.validDeviceToInsert(devicesList, newDeviceJTextField.getText()))
				{
					matrixPanel.removeAll(); 
					
					devicesList.add(new Device(newDeviceJTextField.getText()));
					
					row ++;
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
								matrixPanel.add(new JButton(devicesList.get(j-1).getDeviceName()));
							else
								matrixPanel.add(new JTextField());
						}
					
					newDeviceJTextField.setText("");
					matrixPanel.revalidate();
				}
				else
					JOptionPane.showMessageDialog(frame,"This device already exist in matrix!", "Error", JOptionPane.ERROR_MESSAGE);
				
			}
		});
		
		deleteDeviceButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				if(control.validDeviceToDelete(devicesList, newDeviceJTextField.getText()))
				{
					matrixPanel.removeAll(); 
					
					devicesList.remove(control.getIndexFromList(devicesList, newDeviceJTextField.getText()));
					
					row --;
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
								matrixPanel.add(new JButton(devicesList.get(j-1).getDeviceName()));
							else
							{
								JTextField textField = new JTextField();
								matrixPanel.add(textField);
								if(i==j)//VER PORQUE NAO FUNCIONA
									textField.setEnabled(false);
							}
						}
					
					newDeviceJTextField.setText("");
					matrixPanel.revalidate();
				}
				else
					JOptionPane.showMessageDialog(frame,"This device don't exist in matrix!", "Error", JOptionPane.ERROR_MESSAGE);
			}
		});
		
		exportDataButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				JFileChooser arquivo = new JFileChooser();  
                arquivo.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                arquivo.setDialogTitle("Select a location to save your file with existent data");
                arquivo.showOpenDialog(null);
                if(arquivo.getSelectedFile() != null)
                {
                	
                	test(matrixPanel);
                	//devicesList = getAllDataFromMatrix();
                	conFile.save(arquivo.getSelectedFile().getAbsolutePath(), devicesList);
                	JOptionPane.showMessageDialog(frame,"Matrix is saved in file with success!", "Success", JOptionPane.DEFAULT_OPTION);
                }
			}
		});
		
	}
	
	private List<Device> getAllDataFromMatrix()
	{
		List<Device> tmp = new ArrayList<>();
		
		return null;
	}
	
	
	private void test(JPanel matrixPanel)
	{
		/**
		 * A ideia � ler os devices depois por linha ler os atrasos sendo a coluna o vizinho
		 * */
		Component[] components = matrixPanel.getComponents();

		int index = 0;
	    for (int i = 0; i < components.length; i++) 
	    {

	        if(components[i].getClass().getName().toString().equals("javax.swing.JTextField"))
	        {
	        	components[i].setEnabled(false);
	        }
	    }
	}
	
	
}