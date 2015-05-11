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
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.ImageIcon;

import Entities.Device;
import Entities.Neighbour;
import Matrix.ControllerMatrix;
import Matrix.ExportMatrix;
import Matrix.ImportMatrix;
import PTP.PTP;
import PTP.Type;

public class GraphicInterface 
{
	private JFrame frame;
	private int row;
	private List<Device> devicesList;
	private JTextField newDeviceJTextField;
	private JTextField ipMulticastAddressJTextField;
	private JTextField hostNameTextField;

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
		ControllerInterface control = new ControllerInterface();
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
		iconLabel.setIcon(new ImageIcon("routerMascotev3mini mini.png"));
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
		
		ipMulticastAddressJTextField = new JTextField();
		ipMulticastAddressJTextField.setColumns(10);
		ipMulticastAddressJTextField.setBounds(423, 65, 151, 23);
		applicaitonPanel.add(ipMulticastAddressJTextField);
		
		JLabel ipMulticastAdressLabel = new JLabel("IP Multicast Address");
		ipMulticastAdressLabel.setBounds(262, 69, 151, 14);
		applicaitonPanel.add(ipMulticastAdressLabel);
		
		JLabel hostNameLabel = new JLabel("Host name (optional)");
		hostNameLabel.setBounds(262, 35, 151, 14);
		applicaitonPanel.add(hostNameLabel);
		
		hostNameTextField = new JTextField();
		hostNameTextField.setColumns(10);
		hostNameTextField.setBounds(423, 31, 151, 23);
		applicaitonPanel.add(hostNameTextField);
		
		JButton slaveModeButton = new JButton("Slave mode");
		slaveModeButton.setBounds(10, 201, 151, 23);
		applicaitonPanel.add(slaveModeButton);

		
		/** 
		 * Function for buttons
		 * */
		
		addDeviceButton.addActionListener(new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e) 
			{	
				if(ValidationFunctions.validDeviceToInsert(devicesList, newDeviceJTextField.getText()))
				{
					matrixPanel.removeAll(); 
					
					devicesList.add(new Device(newDeviceJTextField.getText()));
					
					row ++;
					control.reloadMatrix(matrixPanel, row, devicesList);
					
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
				if(ValidationFunctions.validDeviceToDelete(devicesList, newDeviceJTextField.getText()))
				{
					matrixPanel.removeAll(); 
					
					devicesList.remove(ValidationFunctions.getIndexFromList(devicesList, newDeviceJTextField.getText()));
					//Remover em todos os visinhos o dispositivo
					ControllerMatrix.removeNeighbourFromAllNeighbourList(devicesList, newDeviceJTextField.getText());
					
					row --;
					control.reloadMatrix(matrixPanel, row, devicesList);
					
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
                	if(ControllerMatrix.validMatrixInJPanel(matrixPanel, row))
                	{
                		ExportMatrix configFile = new ExportMatrix(ControllerMatrix.getAllDataFromMatrix(matrixPanel, row));
	                	configFile.saveInFile(arquivo.getSelectedFile().getAbsolutePath());
	                	JOptionPane.showMessageDialog(frame,"Matrix is saved in file with success!", "Success", JOptionPane.DEFAULT_OPTION);
                	}
                	else
    					JOptionPane.showMessageDialog(frame,"Some fields in matrix don't are valid", "Error", JOptionPane.ERROR_MESSAGE);
                }
			}
		});
		
		readDataButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				JFileChooser arquivo = new JFileChooser();  
                arquivo.setDialogTitle("Select a matrix to be readed");
                arquivo.showOpenDialog(null);
                if(arquivo.getSelectedFile() != null)
                {
                	ImportMatrix impMatrix = new ImportMatrix(arquivo.getSelectedFile());
                	devicesList = impMatrix.getListOfDevicesFromFile();
                	row = devicesList.size()+1;
                	
                	matrixPanel.removeAll(); 
					matrixPanel.setLayout(new GridLayout(row, row));
					for(int i = 0; i<row; i++)
						if(i==0)
							matrixPanel.add(new JButton(""));
						else
							matrixPanel.add(new JButton(devicesList.get(i-1).getDeviceName()));
					
					for(int j = 1; j<row; j++)//Linha
						for(int i = 0; i<row; i++)//Coluna
						{
							if(i == 0)
								matrixPanel.add(new JButton(devicesList.get(j-1).getDeviceName()));
							else
							{
								String distance = String.valueOf(devicesList.get(j-1).getNeighbours().get(i-1).getDistance());
								matrixPanel.add(new JTextField(distance));
							}
						}
					
					newDeviceJTextField.setText("");
					matrixPanel.revalidate();
                	
                }
			}
		});
		
		synchronizeButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0)
			{
				try 
				{
					//Estes dados vao ser lidos de um ficheiro config
					Thread ptp = new Thread(new PTP("224.0.0.3", Type.MASTER, 8888, InetAddress.getLocalHost().getHostAddress()));
					ptp.start();
					ptp.join();
				} 
				catch (InterruptedException | UnknownHostException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
		});
		
		slaveModeButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				
				//Fazer validações dos campos
				try 
				{
					//Estes dados vao ser lidos de um ficheiro config
					Thread ptp = new Thread(new PTP("224.0.0.3", Type.SLAVE, 8888, InetAddress.getLocalHost().getHostAddress()));
					ptp.start();
					ptp.join();
				} 
				catch (InterruptedException | UnknownHostException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
}
