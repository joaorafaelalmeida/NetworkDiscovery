package ExactFormulation;
import java.io.File;
import gurobi.*;
import Matrix.*;

public class ExactFormulation
{
	
	private File file;
	ImportMatrix matrixImport;
    int n; 
    double[][] D; 
    String [] names;
    int n1; 
    int n2;
    int	c;
    
    
	public ExactFormulation(String fileName)
	{
		this.file = new File("nome");
		matrixImport = new ImportMatrix(file);
		n = matrixImport.getNumTotalDevices();
		D = matrixImport.getMatrixInDoubleArray();
		names = matrixImport.getDevicesNameInStringArray();
		
		
	}
	
	public void qualquercoisa()
	{
		n1 = n-2;
		n2 = 2*n-2;
		c = (n-1)-Math.floorDiv(n, 2);
		
		try 
		{   
			GRBEnv env = new GRBEnv("exact.log");
		    GRBModel exactModel = new GRBModel(env);		      
		} 
		catch (GRBException e) 
		{
		      System.out.println("Error code: " + e.getErrorCode() + ". " + e.getMessage());
		}
	}
	
}
