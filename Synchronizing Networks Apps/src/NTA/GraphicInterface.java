package NTA;

import Entities.Device;
import Entities.Location;
import Entities.Routers;
import Matrix.ImportMatrix;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;

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
	/*public GraphicInterface(String locMatrix) 
	{
		devices = new ArrayList();
		routers = new ArrayList();
		initialize();
	}*/

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() 
	{
		frame = new JFrame();
		frame.setBounds(100, 100, 689, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setVisible(true);
		frame.setResizable(false);

		TopologyPanel topologyPanel = new TopologyPanel();
		topologyPanel.setBounds(10, 45, frame.getWidth()-25, frame.getHeight()-100);
		topologyPanel.setBackground(Color.WHITE);
		drawAreaHight = topologyPanel.getHeight();
		drawAreaWidth = topologyPanel.getWidth();
		
		frame.getContentPane().add(topologyPanel);
		topologyPanel.setLayout(null);
		
		JButton loadTopologyButton = new JButton("Import topology from file");
		loadTopologyButton.setBounds(10, 11, 200, 23);
		frame.getContentPane().add(loadTopologyButton);
		
		JButton btnMesauresDelays = new JButton("Mesaures delays");
		btnMesauresDelays.setBounds(220, 11, 200, 23);
		frame.getContentPane().add(btnMesauresDelays);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(430, 12, 200, 20);
		comboBox.addItem("Exact Formulation");
		comboBox.addItem("Robust Formulation");
		comboBox.addItem("Feasibility Pump");
		frame.getContentPane().add(comboBox);
		
		loadTopologyButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				//Importar do ficheiro
				JFileChooser arquivo = new JFileChooser();  
		        arquivo.setDialogTitle("Select a topology result to be readed");
		        arquivo.showOpenDialog(null);
		        if(arquivo.getSelectedFile() != null)
		        {
		        	routers = NTAControler.readRoutersFromFile(arquivo.getSelectedFile());
		        	devices = NTAControler.readDevicesFromFile();
		        }

				//Desenhar Topologia
				if(!devices.isEmpty())
				{
					double raio;
					int numPc = devices.size();
					double angulo = Math.PI*2/numPc;
					double centroX = drawAreaWidth/2-30;
					double centroY = drawAreaHight/2-25;
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
						Image dimg = img.getScaledInstance(30,30, Image.SCALE_SMOOTH);//lbl.getWidth(), lbl.getHeight(), Image.SCALE_SMOOTH);
						
						JLabel lbl = new JLabel(devices.get(i).getDeviceName(),new ImageIcon(dimg),SwingConstants.CENTER);
						lbl.setSize(94, 45);
						
					    lbl.setVerticalTextPosition(JLabel.BOTTOM);
					    lbl.setHorizontalTextPosition(JLabel.CENTER);
						
						
						//lbl.setIcon(new ImageIcon(dimg));
						topologyPanel.add(lbl);
						
						//Calcular localização
						locX = centroX + (raio*Math.cos(anguloProvisorio));
						locY = centroY + (raio*Math.sin(anguloProvisorio));
						
						anguloProvisorio += angulo;
						lbl.setLocation((int)locX,(int)locY);
						
						Location devLoc = new Location((int)locX+(94/2),(int)locY+(45/2));
						devices.get(i).setLocation(devLoc);
					}
					
					//Posicionar routers
					int numRouters = routers.size();
					angulo = Math.PI*2/numRouters;
					anguloProvisorio = 0;
					for (int i = 0; i < numRouters; i++) 
					{
						double anguloTmp = anguloProvisorio;
						double a = centroX-100;
						double b = centroY-100;
						raio = (a*b);
						raio /= (Math.sqrt(b*b + (a*a - b*b) * Math.pow(Math.sin(anguloTmp),2)));
						raio -= 5;
						double locY, locX = locY = 0;
						
						BufferedImage img = null;
						try 
						{
						    img = ImageIO.read(new File("routerSymbol.png"));
						} 
						catch (IOException e) 
						{
						    e.printStackTrace();
						}
						Image dimg = img.getScaledInstance(45,33, Image.SCALE_SMOOTH);//lbl.getWidth(), lbl.getHeight(), Image.SCALE_SMOOTH);
						
						JLabel lbl = new JLabel(routers.get(i).getRouterName(),new ImageIcon(dimg),SwingConstants.CENTER);
						lbl.setSize(94, 65);
						
					    lbl.setVerticalTextPosition(JLabel.BOTTOM);
					    lbl.setHorizontalTextPosition(JLabel.CENTER);
						
						
						//lbl.setIcon(new ImageIcon(dimg));
						topologyPanel.add(lbl);
						
						//Calcular localização
						locX = centroX + (raio*Math.cos(anguloProvisorio));
						locY = centroY + (raio*Math.sin(anguloProvisorio));
						
						anguloProvisorio += angulo;
						lbl.setLocation((int)locX,(int)locY);
						
						Location routLoc = new Location((int)locX+(94/2),(int)locY+(65/2));
						routers.get(i).setLocation(routLoc);
					}

					topologyPanel.revalidate();
					topologyPanel.repaint();
					
				}
				
			}
		});
		
		
		
	}
	private class TopologyPanel extends JPanel
	{
		public TopologyPanel () 
		  {
		    this.setBackground (Color.cyan);
		  }

		  public void paintComponent (Graphics g)
		  {
		    super.paintComponent (g);
		    for (Routers router : routers) 
			{
		    	g.setColor(Color.blue);
				for (Device device : router.getConnectedDevices())
					g.drawLine(router.getLocation().getX(),
									router.getLocation().getY(),
									device.getLocation().getX(),
									device.getLocation().getY());
				g.setColor(Color.red);
				for (Routers rout : router.getConnectedRouters())
					for (Routers rTemp : routers)
						if(rTemp.getRouterName().equals(rout.getRouterName()))
							g.drawLine(router.getLocation().getX(),
														router.getLocation().getY(),
														rTemp.getLocation().getX(),
														rTemp.getLocation().getY());
				
			}

		  }
	}
}
