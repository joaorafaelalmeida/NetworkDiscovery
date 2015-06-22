package SNA;

import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;

import MeasureDelays.MeasureDelays;
import MeasureDelays.MyIP;
import MeasureDelays.Type;
import NTA.NTAGraphicInterface;
import UserInterface.ControllerInterface;
import UserInterface.KeyManager;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class StartMenuSNA {

	private JFrame frame;
	private JTextField textField;
	private JPasswordField passwordField;

	public JFrame getFrame()
	{
		return frame;
	}

	/**
	 * Create the application.
	 */
	public StartMenuSNA() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() 
	{
		frame = new JFrame();
		frame.setBounds(100, 100, 470, 210);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton slaveButton = new JButton("Slave");
		slaveButton.setBounds(41, 41, 231, 23);
		frame.getContentPane().add(slaveButton);
		
		JButton masterButton = new JButton("Master");
		masterButton.setBounds(41, 75, 231, 23);
		frame.getContentPane().add(masterButton);
		
		JLabel lblNewLabel = new JLabel("Password");
		lblNewLabel.setBounds(41, 109, 103, 23);
		frame.getContentPane().add(lblNewLabel);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(132, 110, 140, 20);
		frame.getContentPane().add(passwordField);
		
		JLabel iconLabel = new JLabel("");
		iconLabel.setIcon(new ImageIcon("routerMascotev3mini mini.png"));
		iconLabel.setBounds(290, 37, 150, 96);
		frame.getContentPane().add(iconLabel);
		
		/**
		 * Functions for buttons
		 * */
		
		masterButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				//Ler pass e fazer validação
				String pass = new String(passwordField.getPassword());
				if(pass.length()>0)
					if(KeyManager.doLogin(pass, frame))
					{
						UserInterface.GraphicInterface menu = new UserInterface.GraphicInterface();
                        menu.getFrame().setVisible(true);
                        frame.dispose(); 
					}
					else
						JOptionPane.showMessageDialog(frame,"Invalid password!", "Error", JOptionPane.ERROR_MESSAGE);
				else
					JOptionPane.showMessageDialog(frame,"Please insert a password!", "Error", JOptionPane.ERROR_MESSAGE);
			}
		});
		
		slaveButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0) 
			{				
				String ip = JOptionPane.showInputDialog(frame, "Introduce the multicast ip address! If you don´t introduce ip address, this will be read from configuration file!");
				if(ip!= null)
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
							Thread ptp = new Thread(new MeasureDelays(ip, Type.SLAVE, 8888, MyIP.getMyIP()));
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
						            	Thread ptp = new Thread(new MeasureDelays(ip, Type.SLAVE, 8888, MyIP.getMyIP()));
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
	}
}
