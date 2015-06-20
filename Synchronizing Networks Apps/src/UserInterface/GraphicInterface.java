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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JLabel;
import javax.swing.ImageIcon;

import Entities.Device;
import Entities.Neighbour;
import Matrix.ControllerMatrix;
import Matrix.ExportMatrix;
import Matrix.ImportMatrix;
import MeasureDelays.MeasureDelays;
import MeasureDelays.MyIP;
import MeasureDelays.Type;
import NTA.NTA;
import static Matrix.Matrix.*;

import javax.swing.JSeparator;

public class GraphicInterface 
{
	private JFrame frame;
	private JTextField newDeviceJTextField;
	private JTextField ipMulticastAddressJTextField;
	
	public JFrame getFrame()
	{
		return frame;
	}

	/**
	 * Create the application.
	 */
	public GraphicInterface() 
	{
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
		
		JButton addDeviceButton = new JButton("Add device");
		
		
		addDeviceButton.setBounds(10, 271, 151, 23);
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
		newDeviceJTextField.setBounds(10, 237, 151, 23);
		applicaitonPanel.add(newDeviceJTextField);
		newDeviceJTextField.setColumns(10);
		
		JButton synchronizeButton = new JButton("Measure delays");
		synchronizeButton.setBounds(10, 133, 151, 23);
		applicaitonPanel.add(synchronizeButton);
		
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
		deleteDeviceButton.setBounds(10, 305, 151, 23);
		applicaitonPanel.add(deleteDeviceButton);
		
		ipMulticastAddressJTextField = new JTextField();
		ipMulticastAddressJTextField.setColumns(10);
		ipMulticastAddressJTextField.setBounds(423, 65, 151, 23);
		applicaitonPanel.add(ipMulticastAddressJTextField);
		
		JLabel ipMulticastAdressLabel = new JLabel("IP Multicast Address");
		ipMulticastAdressLabel.setBounds(262, 69, 151, 14);
		applicaitonPanel.add(ipMulticastAdressLabel);
		
		JButton showTopologyButton = new JButton("Show topology");
		showTopologyButton.setBounds(10, 167, 151, 23);
		applicaitonPanel.add(showTopologyButton);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 201, 151, 2);
		applicaitonPanel.add(separator);
		
		JLabel lblNewDeviceName = new JLabel("New device name");
		lblNewDeviceName.setBounds(10, 212, 118, 14);
		applicaitonPanel.add(lblNewDeviceName);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 339, 151, 2);
		applicaitonPanel.add(separator_1);
		
		JButton btnResetPassword = new JButton("Reset password");
		btnResetPassword.setBounds(10, 348, 151, 23);
		applicaitonPanel.add(btnResetPassword);

		
		/** 
		 * Function for buttons
		 * */
		
		addDeviceButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{	
				if(ValidationFunctions.validDeviceToInsert(devices, newDeviceJTextField.getText()))
				{
					matrixPanel.removeAll(); 
					
					devices.add(new Device(newDeviceJTextField.getText()));
					
					control.openMatrixFromMeasures(matrixPanel, devices.size(), devices);
					
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
				if(ValidationFunctions.validDeviceToDelete(devices, newDeviceJTextField.getText()))
				{
					matrixPanel.removeAll(); 
					
					devices.remove(ValidationFunctions.getIndexFromList(devices, newDeviceJTextField.getText()));
					//Remover em todos os visinhos o dispositivo
					ControllerMatrix.removeNeighbourFromAllNeighbourList(devices, newDeviceJTextField.getText());
					
					control.openMatrixFromMeasures(matrixPanel, devices.size(), devices);
					
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
                	if(ControllerMatrix.validMatrixInJPanel(matrixPanel, devices.size()+1))
                	{
                		ExportMatrix configFile = new ExportMatrix(ControllerMatrix.getAllDataFromMatrix(matrixPanel, devices.size()+1));
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
                	devices = impMatrix.getListOfDevicesFromFile();
                	
                	matrixPanel.removeAll(); 
					matrixPanel.setLayout(new GridLayout(devices.size()+1, devices.size()+1));
					for(int i = 0; i<devices.size()+1; i++)
						if(i==0)
							matrixPanel.add(new JButton(""));
						else
							matrixPanel.add(new JButton(devices.get(i-1).getDeviceName()));
					
					for(int j = 1; j<devices.size()+1; j++)//Linha
						for(int i = 0; i<devices.size()+1; i++)//Coluna
						{
							if(i == 0)
								matrixPanel.add(new JButton(devices.get(j-1).getDeviceName()));
							else
							{
								String distance = String.valueOf(devices.get(j-1).getNeighbours().get(i-1).getDistance());
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
				String ip = ipMulticastAddressJTextField.getText();
				if(ControllerInterface.isIP(ip))
				{
					try 
					{
						PrintWriter fl = new PrintWriter(new File("config"));
						fl.println(ip);
			            fl.close();
					} 
					catch (FileNotFoundException e1) 
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		            
					try 
					{
						Thread ptp = new Thread(new MeasureDelays(ip, Type.MASTER, 8888, MyIP.getMyIP(),frame, matrixPanel));
						ptp.start();
						ptp.join();
					} 
					catch (InterruptedException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
					if(ip.length()>0)
						JOptionPane.showMessageDialog(frame,"The multicast ip address is not valid!", "Error", JOptionPane.ERROR_MESSAGE);
					else
					{
						//ler do ficheiro config o endereco
						try 
						{
							Scanner input = new Scanner(new File("config"));
							if(input.hasNext())
							{
				            	ip = input.nextLine();
				            	if(ControllerInterface.isIP(ip))
				            	{
					            	Thread ptp = new Thread(new MeasureDelays(ip, Type.MASTER, 8888, MyIP.getMyIP(),frame, matrixPanel));
					            	ptp.start();
					            	ptp.join();
				            	}
				            	else
				            		JOptionPane.showMessageDialog(frame,"The multicast ip address readed from file is not valid!", "Error", JOptionPane.ERROR_MESSAGE);
							}
							else
								JOptionPane.showMessageDialog(frame,"The config file is empty!", "Error", JOptionPane.ERROR_MESSAGE);
						} 
						catch (InterruptedException e) 
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						catch (FileNotFoundException e)
						{
							JOptionPane.showMessageDialog(frame,"The config file is empty!", "Error", JOptionPane.ERROR_MESSAGE);
						}
					}
			}
			
			
		});
				
		showTopologyButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{

            	if(ControllerMatrix.validMatrixInJPanel(matrixPanel, devices.size()))
            	{
            		ExportMatrix configFile = new ExportMatrix(ControllerMatrix.getAllDataFromMatrix(matrixPanel, devices.size()));
                	configFile.saveInFile("");
            	}
            	else
					JOptionPane.showMessageDialog(frame,"Some fields in matrix don't are valid", "Error", JOptionPane.ERROR_MESSAGE);

            	String[] locationMatrix  = {"SNAMatrix.txt"};
				NTA.main(locationMatrix);
			}
		});
	}
}
