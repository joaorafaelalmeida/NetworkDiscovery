package Testes;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Main {

  public static void main(String[] args) throws IOException 
  {

	  
	  
	  //escreve
	  ByteArrayOutputStream baos = new ByteArrayOutputStream();
	  DataOutputStream w = new DataOutputStream(baos);
	  String msg = "Joao foda";
	  
	  w.writeInt(msg.length());
	  w.write(msg.getBytes());

	  w.flush();
//le
	  DataInputStream ois = new DataInputStream(new ByteArrayInputStream(baos.toByteArray()));
	  int t = ois.readInt();
	  byte[] result = new byte[t];
	  ois.read(result);
	  System.out.println(t + new String(result));
	  
  }
}