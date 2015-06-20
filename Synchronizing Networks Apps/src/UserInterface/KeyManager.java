package UserInterface;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import MeasureDelays.Flags;

public class KeyManager
{
	public static boolean insertPasswordInFile(String password)
	{
		try 
		{
			KeyGenerator gen = KeyGenerator.getInstance("AES");
			gen.init(128);
	        SecretKey key = gen.generateKey();
	        Cipher cipher = Cipher.getInstance("AES");
	        cipher.init(Cipher.ENCRYPT_MODE, key);
	        byte[] passwordCifrada = cipher.doFinal(password.getBytes());
	        
	        //Save in file
	        FileOutputStream fos = new FileOutputStream(new File("pass"));

        	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    		DataOutputStream w = new DataOutputStream(baos);
    			
    		//key
    		w.writeInt(key.getEncoded().length);
    		w.write(key.getEncoded());
    		
    		//password
    		w.writeInt(passwordCifrada.length);
    		w.write(passwordCifrada);

    		w.flush();			
            fos.write(baos.toByteArray());
            fos.close();
	        
		}
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
        
		return true;
	}
	

	public static boolean doLogin(String password, JFrame frame)
	{
		try 
		{
			InputStream is = new FileInputStream(new File("pass"));
	        DataInputStream ois = new DataInputStream(is);
	        
	        //read key
	        byte[] key = new byte[ois.readInt()];
	        ois.read(key);
	        
	        //read pass
	        byte[] pass = new byte[ois.readInt()];
	        ois.read(pass);
	        
	        SecretKey dkey = new SecretKeySpec(key,"AES");
	        Cipher cipher = Cipher.getInstance("AES");
	        cipher.init(Cipher.DECRYPT_MODE, dkey);
	        
	        byte[] passwordDecifrada = cipher.doFinal(pass);
	        
	        if(new String(passwordDecifrada).equals(password))
	        	return true;
	        
		}
		catch(FileNotFoundException e)
		{
			JOptionPane.showMessageDialog(frame,"Confirm password!", "Error", JOptionPane.ERROR_MESSAGE);
			return insertPasswordInFile(password);
		}
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		return false;
	}


}
