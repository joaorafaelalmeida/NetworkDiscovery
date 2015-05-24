package NTA;

import Entities.Device;
import Entities.Routers;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GraphicInterface {

	private JFrame frame;
	private int drawAreaHight;
	private int drawAreaWidth;
	private List<Device> devices;
	private List<Routers> routers;
	
	public JFrame getFrame()
	{
		return frame;
	}

	/**
	 * Create the application.
	 */
	public GraphicInterface() {
		devices = new ArrayList();
		routers = new ArrayList();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() 
	{
		frame = new JFrame();
		frame.setBounds(100, 100, 620, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		/*frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		frame.setVisible(true);*/

		JPanel topologyPanel = new JPanel();
		topologyPanel.setBounds(10, 45, 584, 406);
		topologyPanel.setBackground(Color.WHITE);
		drawAreaHight = topologyPanel.getHeight();
		drawAreaWidth = topologyPanel.getWidth();
		
		frame.getContentPane().add(topologyPanel);
		topologyPanel.setLayout(null);
		
		JButton loadTopologyButton = new JButton("Import topology");
		loadTopologyButton.setBounds(10, 11, 151, 23);
		frame.getContentPane().add(loadTopologyButton);
		
		
		loadTopologyButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				//Importar do ficheiro
				
				
				//Desenhar Topologia
				if(!devices.isEmpty())
				{
					double raio;
					int numPc = devices.size();
					double angulo = Math.PI*2/numPc;
					double centroX = drawAreaWidth/2-35;
					double centroY = drawAreaHight/2-35;
					double anguloProvisorio = 0;
					
					//Posicionar pcs
					for (int i = 0; i < numPc; i++) 
					{
						double anguloTmp = anguloProvisorio;
						double a = centroX;
						double b = centroY;
						raio = (a*b);
						raio /= (Math.sqrt(b*b + (a*a - b*b) * Math.pow(Math.sin(anguloTmp),2)));
						raio -= 5;
						double locY, locX = locY = 0;
						
						BufferedImage img = null;
						try 
						{
						    img = ImageIO.read(new File("hostSymbol.png"));
						} 
						catch (IOException e) 
						{
						    e.printStackTrace();
						}
						Image dimg = img.getScaledInstance(50,50, Image.SCALE_SMOOTH);//lbl.getWidth(), lbl.getHeight(), Image.SCALE_SMOOTH);
						
						JLabel lbl = new JLabel(devices.get(i).getDeviceName(),new ImageIcon(dimg),SwingConstants.CENTER);
						lbl.setSize(50, 65);
						
					    lbl.setVerticalTextPosition(JLabel.BOTTOM);
					    lbl.setHorizontalTextPosition(JLabel.CENTER);
						
						
						//lbl.setIcon(new ImageIcon(dimg));
						topologyPanel.add(lbl);
						
						//Calcular localização
						locX = centroX + (raio*Math.cos(anguloProvisorio));
						locY = centroY + (raio*Math.sin(anguloProvisorio));
						
						anguloProvisorio += angulo;
						lbl.setLocation((int)locX,(int)locY);
					}
					
					//Posicionar routers
				}
				
			}
		});
		
		
		
	}
	

}
