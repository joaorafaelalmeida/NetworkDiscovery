package Formulations;


import gurobi.GRB;
import gurobi.GRBEnv;
import gurobi.GRBException;
import gurobi.GRBLinExpr;
import gurobi.GRBModel;
import gurobi.GRBVar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import Entities.Device;
import Entities.Routers;
import Matrix.ImportMatrix;

public class Robust 
{
	private File file;
	private ImportMatrix matrixImport;
	private List<Device> listDevices;
	private List<Routers> listRouters;
	
	private double objectValue;
    private int n; 
    private double[][] D; // = new double[][]{{0, 0.1111, 0.1314, 0.1114, 0.1717},{0.1111, 0, 0.0811, 0.2022, 0.2625},{0.1314, 0.0811, 0, 0.2225, 0.2828},{0.1114, 0.2022, 0.2225, 0, 0.2222},{0.1717, 0.2625, 0.2828, 0.2222, 0}}; 
    private double [][] B; // = new double[][]{{0, 0.016665, 0.01971, 0.01671, 0.025755},{0.016665, 0, 0.012165, 0.03033, 0.039375},{0.01971, 0.012165, 0, 0.033375, 0.04242},{0.01671, 0.03033, 0.033375, 0, 0.03333},{0.025755, 0.039375, 0.04242, 0.03333, 0}};    
    private String [] names;
    private int n1; 
    private int n2;
    private int	c; 	
    private int [][] solX;
    private int [][][][] solF;
    private int [][][] solY;
    private double [][] solV;
    private double solTeta;
    private int gama;
    
    public Robust(String fileName)
    {
    	this.file = new File("nome");
		matrixImport = new ImportMatrix(file);
		n = matrixImport.getNumTotalDevices();
		D = matrixImport.getMatrixInDoubleArray();
		B = new double [n][n];
		for(int i = 0; i < n; i++)
			for(int j = 0; j < n; j++)
				B[i][j]=D[i][j]*0.15;
		listDevices = matrixImport.getMatrixInList();
		names = matrixImport.getDevicesNameInStringArray();
		listRouters = new ArrayList<Routers>();
		n1 = n-2;
		n2 = 2*n-2;
		c = (n-1)-Math.floorDiv(n, 2);
		solX= new int[n-2][n2]; 
		solF = new int[n2][n2][n][n];
		solY = new int[n-2][n][n];
		solV = new double[n][n];
		
		switch(n)
		{
			case 5: case 6: case 7:
				gama=3;
				break;
			case 8: case 9: case 10: case 11: case 12: case 13: case 14: case 15: case 16:
				gama=4;
				break;
			case 17: case 18: case 19: case 20:
				gama=5;
				break;
			default:
				gama=0;
				break;
		}
    } 
    
    /*
    public Robust()
	{
		n=5;
		switch(n)
		{
			case 5: case 6: case 7:
				gama=3;
				break;
			case 8: case 9: case 10: case 11: case 12: case 13: case 14: case 15: case 16:
				gama=4;
				break;
			case 17: case 18: case 19: case 20:
				gama=5;
				break;
			default:
				gama=0;
				break;
		}
	}
	*/
    
    public void getTree()
	{
		//for(int i=0; i<=n-1; i++)
		//	for(int j=0; j<=n-1; j++)
		//		System.out.println("("+i+" ,"+j+")= "+D[i][j]);
		n1 = n-2;
		n2 = 2*n-2;
		c = (n-1)-Math.floorDiv(n, 2);
		GRBVar[][] x = new GRBVar[n-2][n2];
		//solX= new int[n-2][n2]; 
		GRBVar[][][][] f = new GRBVar[n2][n2][n][n];
		//solF = new int[n2][n2][n][n]; 
		GRBVar[][][] y =new GRBVar[n-2][n][n];
		//solY = new int[n-2][n][n];
		GRBVar[][] v = new GRBVar[n][n];
		//solV = new double [n][n];
		
		try 
		{   
			GRBEnv env = new GRBEnv("robust.log");
		    GRBModel robustModel = new GRBModel(env);
		    
		    
		    for(int i = 0; i<=n1-1; i++)
		    	for(int j=i+1; j<=n2-1; j++)
		    		x[i][j]=robustModel.addVar(0, 1, 0, GRB.BINARY, "x"+Integer.toString(i+1)+Integer.toString(j+1));
		    	    
		    for(int i=0; i<=n1-2;i++)
		    	for(int j= i+1; j<=n1-1; j++)		    	
		    		for(int k=0; k<=n-2; k++)
		    			for(int l=k+1; l<=n-1; l++)
		    			{
		    				f[i][j][k][l]=robustModel.addVar(0, 1, 0, GRB.BINARY, "f"+Integer.toString(i+1)+Integer.toString(j+1)+Integer.toString(k+n-1)+Integer.toString(l+n-1));
		    				f[j][i][k][l]=robustModel.addVar(0, 1, 0, GRB.BINARY, "f"+Integer.toString(j+1)+Integer.toString(i+1)+Integer.toString(k+n-1)+Integer.toString(l+n-1));
		    			}
		    	    	    
		    for(int i=0; i<=n1-1; i++)
		    	for(int k=0; k<=n-2; k++)
		    		for(int l=k+1; l<=n-1; l++)
		    				f[k+n-2][i][k][l]=robustModel.addVar(0, 1, 0, GRB.BINARY, "f"+Integer.toString(k+n-1)+Integer.toString(i+1)+Integer.toString(k+n-1)+Integer.toString(l+n-1));
		    
		    
		    for(int i=0; i<=n1-1;i++)
		    	for(int k=0; k<=n-2; k++)
		    		for(int l=k+1; l<=n-1; l++)
		   				f[i][l+n-2][k][l]=robustModel.addVar(0, 1, 0, GRB.BINARY, "f"+Integer.toString(i+1)+Integer.toString(l+n-1)+Integer.toString(k+n-1)+Integer.toString(l+n-1));
		    				
		    for(int i=0; i<=n-3;i++)
		    	for(int k=0; k<=n-2; k++)
		    		for(int l=k+1; l<=n-1; l++)
		    			y[i][k][l]=robustModel.addVar(0, 1, 0, GRB.BINARY, "y"+Integer.toString(i+2)+Integer.toString(k+n-1)+Integer.toString(l+n-1));
		    
		    for(int k=0; k<=n-2; k++)
		    	for(int l=k+1; l<=n-1; l++)
		    		v[k][l]=robustModel.addVar(0, GRB.INFINITY, 0, GRB.CONTINUOUS, "v"+Integer.toString(k+n-1)+Integer.toString(l+n-1));

		    GRBVar teta = robustModel.addVar(0, GRB.INFINITY, 0, GRB.CONTINUOUS, "teta");
		     
		    robustModel.update();
		    
		    //Set objective
		    GRBLinExpr expr = new GRBLinExpr();
		    for(int i=0; i<=n-3; i++)
		    	for(int k=0; k<=n-2; k++)
		    		for(int l=k+1; l<=n-1; l++)
		    			{
		    				expr.addTerm(D[k][l]*Math.pow(2, -(i+2)), y[i][k][l]);
		    			}
		    expr.addTerm(gama, teta);
		    for(int k=0; k<=n-2; k++)
	    		for(int l=k+1; l<=n-1; l++)
	    		{
	    			expr.addTerm(1, v[k][l]);
	    		}
		        
		    robustModel.setObjective(expr, GRB.MINIMIZE); 
		  
		    //Constrains:
		    //set tree
		    for(int j=2; j<=n-3; j++)
		    {
		    	expr = new GRBLinExpr();
		    	expr.addTerm(1, x[0][j]);
		    	robustModel.addConstr(expr, GRB.EQUAL, 0, "FIXOx0"+Integer.toString(j+1));
		    }
		    
		    for(int j=2; j<=n-3; j++)
		    	for(int k=0; k<=n-2; k++)
		    		for(int l=k+1; l<=n-1; l++)	
		    		{
		    			expr = new GRBLinExpr();
		    			expr.addTerm(1, f[0][j][k][l]);
		    			robustModel.addConstr(expr, GRB.EQUAL, 0, "FIXOf0"+Integer.toString(j+1)+Integer.toString(k+n-1)+Integer.toString(l+n-1));
		    		}
		    
		    for(int j=2; j<=n-3; j++)
		    	for(int k=0; k<=n-2; k++)
		    		for(int l=k+1; l<=n-1; l++)	
		    		{
		    			expr = new GRBLinExpr();
		    			expr.addTerm(1, f[j][0][k][l]);
		    			robustModel.addConstr(expr, GRB.EQUAL, 0, "FIXOf0"+Integer.toString(j+1)+Integer.toString(k+n-1)+Integer.toString(l+n-1));
		    		}
		    for(int i=1; i<=c-2; i++)
		    	for(int j=i+2; j<=c; j++)
		    	{
			    	expr = new GRBLinExpr();
			    	expr.addTerm(1, x[i][j]);
			    	robustModel.addConstr(expr, GRB.EQUAL, 0, "FIXOx"+Integer.toString(i+1)+Integer.toString(j+1));
			    }
		    for(int i=1; i<=c-2; i++)
		    	for(int j=i+2; j<=c; j++)
		    		for(int k=0; k<=n-2; k++)
			    		for(int l=k+1; l<=n-1; l++)	
			    		{
			    			expr = new GRBLinExpr();
			    			expr.addTerm(1, f[i][j][k][l]);
			    			robustModel.addConstr(expr, GRB.EQUAL, 0, "FIXOf"+Integer.toString(i+1)+Integer.toString(j+1)+Integer.toString(k+n-1)+Integer.toString(l+n-1));
			    		}
		    for(int i=1; i<=c-2; i++)
		    	for(int j=i+2; j<=c; j++)
		    		for(int k=0; k<=n-2; k++)
			    		for(int l=k+1; l<=n-1; l++)	
			    		{
			    			expr = new GRBLinExpr();
			    			expr.addTerm(1, f[j][i][k][l]);
			    			robustModel.addConstr(expr, GRB.EQUAL, 0, "FIXOf"+Integer.toString(i+1)+Integer.toString(j+1)+Integer.toString(k+n-1)+Integer.toString(l+n-1));
			    		}
			
		    //NATOTAL
		    expr = new GRBLinExpr();
		    for(int i=0; i<=n1-1; i++)
		    	for(int j=i+1; j<=n2-1; j++)
		    		expr.addTerm(1, x[i][j]);	    
		    robustModel.addConstr(expr, GRB.EQUAL, 2*n-3, "NATOTAL");
		    
		    //NAIN    
		    for(int j=n1; j<=n2-1; j++)
		    {
		    	expr = new GRBLinExpr();
		    	for(int i=0; i<=n1-1; i++)
		    		expr.addTerm(1, x[i][j]);
		    	robustModel.addConstr(expr, GRB.EQUAL, 1, "NAIN"+Integer.toString(j));
		    }
		    
		   
		    //NAOUT
		    for(int i=0; i<=n1-1; i++)
		    {
		    	expr = new GRBLinExpr();
		    	for(int j=0; j<=n1-1; j++)
		    	{
		    		if(j>i)
		    			expr.addTerm(1, x[i][j]);
		    		else 
		    			if(j<i)
		    				expr.addTerm(1, x[j][i]);
		    	}
		    	for(int j=n1; j<=n2-1; j++)
		    		expr.addTerm(1, x[i][j]);
		    	robustModel.addConstr(expr, GRB.EQUAL, 3, "NAOUT"+Integer.toString(i));
		    }
		    
		    //RestX1
		    for(int i=0; i<=c-1; i++)
		    {
		    	expr = new GRBLinExpr();
		    	expr.addTerm(1, x[i][i+1]);
		    	robustModel.addConstr(expr, GRB.EQUAL, 1, "RestX1"+Integer.toString(i));
		    }
		   
		    //RestX2
		    expr = new GRBLinExpr();
		    for(int j=n1; j<=n2-1; j++)
		    	expr.addTerm(1, x[0][j]);
		    robustModel.addConstr(expr, GRB.EQUAL, 2, "RestX2");
		    
		    //RestX3
		    expr = new GRBLinExpr();
		    for(int j=n1; j<=n2-1; j++)
		    	expr.addTerm(1, x[n-3][j]);
		    robustModel.addConstr(expr, GRB.EQUAL, 2, "RestX3");
		    
		    //RestX4
		    for(int i=0; i<=n1-1; i++)
		    {
		    	expr = new GRBLinExpr();
			    for(int j=n1; j<=n2-1; j++)
			    	expr.addTerm(1, x[1][j]);
			    robustModel.addConstr(expr, GRB.LESS_EQUAL, 2, "RestX4"+Integer.toString(i));
		    }
		    
		    //RestX5
		    for(int a=0; a<=n1-3; a++)
		    	for(int i=a+1; i<=n1-2; i++)
		    		for(int j=i+1; j<=n1-1; j++)
		    		{
		    			expr = new GRBLinExpr();
		    			expr.addTerm(1, x[i][j]);
		    			expr.addTerm(1, x[a][i]);
		    			expr.addTerm(1, x[a][j]);
		    			robustModel.addConstr(expr, GRB.LESS_EQUAL, 2, "RestX5"+Integer.toString(i)+Integer.toString(j)+Integer.toString(a));
		    		}
		    
		    for(int i=0; i<=n1-3; i++)
		    	for(int a=i+1; a<=n1-2; a++)
		    		for(int j=a+1; j<=n1-1; j++)
		    		{
		    			expr = new GRBLinExpr();
		    			expr.addTerm(1, x[i][j]);
		    			expr.addTerm(1, x[i][a]);
		    			expr.addTerm(1, x[a][j]);
		    			robustModel.addConstr(expr, GRB.LESS_EQUAL, 2, "RestX5"+Integer.toString(i)+Integer.toString(j)+Integer.toString(a));
		    		}
		    
		    for(int i=0; i<=n1-3; i++)
		    	for(int j=i+1; j<=n1-2; j++)
		    		for(int a=j+1; a<=n1-1; a++)
		    		{
		    			expr = new GRBLinExpr();
		    			expr.addTerm(1, x[i][j]);
		    			expr.addTerm(1, x[i][a]);
		    			expr.addTerm(1, x[j][a]);
		    			robustModel.addConstr(expr, GRB.LESS_EQUAL, 2, "RestX5"+Integer.toString(i)+Integer.toString(j)+Integer.toString(a));
		    		}
		    
		    //Fluxok
		    for(int k=0; k<=n-2; k++)
		    	for(int l=k+1; l<=n-1; l++)
		    	{
		    		expr = new GRBLinExpr();
		    		for(int j=0; j<=n1-1;j++)
		    			expr.addTerm(1, f[k+n-2][j][k][l]);
		    		robustModel.addConstr(expr, GRB.EQUAL, 1, "FLUXOk"+Integer.toString(k+n-1)+Integer.toString(l+n-1));
		    	}
		    
		    //Fluxol
		    for(int k=0; k<=n-2; k++)
		    	for(int l=k+1; l<=n-1; l++)
		    	{
		    		expr = new GRBLinExpr();
		    		for(int j=0; j<=n1-1;j++)
		    			expr.addTerm(1, f[j][l+n-2][k][l]);
		    		robustModel.addConstr(expr, GRB.EQUAL, 1, "FLUXOl"+Integer.toString(k+n-1)+Integer.toString(l+n-1));
		    	}
		 
		    //Fluxo0
		    for(int i=0; i<=n1-1; i++)
		    	for(int k=0; k<=n-2; k++)
		    		for(int l=k+1; l<=n-1; l++)
		    		{
		    			expr = new GRBLinExpr();
		    			for(int j=0; j<=n1-1; j++)
		    				if(j!=i)
		    					expr.addTerm(1, f[i][j][k][l]);
		    			expr.addTerm(1, f[i][l+n-2][k][l]);
		    			for(int j=0; j<=n1-1; j++)
		    				if(j!=i)
		    					expr.addTerm(-1, f[j][i][k][l]);
		    			expr.addTerm(-1, f[k+n-2][i][k][l]);
		    			robustModel.addConstr(expr, GRB.EQUAL, 0, "FLUXO"+Integer.toString(i+1)+Integer.toString(k+n-1)+Integer.toString(l+n-1));
		    		}
		 
		    //Fluxo1
		    for(int i=0; i<=n1-1; i++)
		    	for(int k=0; k<=n-1; k++)
		    		for(int l=0; l<=n-1; l++)
		    			if(k<l)
		    			{
		    				expr = new GRBLinExpr();
		    				for(int j=0; j<=n1-1; j++)
		    					if(j!=i)
		    						expr.addTerm(1, f[i][j][k][l]);
		    				expr.addTerm(1, f[i][l+n-2][k][l]);
		    				expr.addTerm(-1, f[k+n-2][i][k][l]);
		    				robustModel.addConstr(expr, GRB.GREATER_EQUAL, 0, "FLUXO1"+Integer.toString(i+1)+Integer.toString(k+n-1)+Integer.toString(l+n-1));
		    			}
		    //Fluxo2
		    for(int i=0; i<=n1-1; i++)
		    	for(int j=0; j<=n1-1; j++)
		    		for(int k=0; k<=n-1; k++)
		    			for(int l=0; l<=n-1; l++)
		    				if(k<l && i!=j)
		    				{
		    					expr = new GRBLinExpr();
		    					for(int a=0; a<=n1-1; a++)
		    						if(a!=i && a!=j)
		    							expr.addTerm(1, f[j][a][k][l]);
		    					expr.addTerm(1, f[j][l+n-2][k][l]);
		    					expr.addTerm(-1, f[i][j][k][l]);
		    					robustModel.addConstr(expr, GRB.GREATER_EQUAL, 0, "FLUXO2"+Integer.toString(i+1)+Integer.toString(j+1)+Integer.toString(k+n-1)+Integer.toString(l+n-1));
		    				}
		    //Ligacao
		    for(int i=0; i<=n1-1; i++)
		    	for(int j=0; j<=n1-1; j++)
		    		for(int k=0; k<=n-1; k++)
		    			for(int l=0; l<=n-1; l++)
		    				if(k<l && i<j)
		    				{
		    					expr = new GRBLinExpr();
		    					expr.addTerm(1, f[i][j][k][l]);
		    					expr.addTerm(1, f[j][i][k][l]);
		    					expr.addTerm(-1, x[i][j]);
		    					robustModel.addConstr(expr, GRB.LESS_EQUAL, 0, "LIGACAO"+Integer.toString(i+1)+Integer.toString(j+1)+Integer.toString(k+n-1)+Integer.toString(l+n-1));
		    				}
		    
		    for(int i=0; i<=n1-1; i++)
		    	for(int k=0; k<=n-1; k++)
		    		for(int l=0; l<=n-1; l++)
		    			if(k<l)
		    			{
		    				expr = new GRBLinExpr();
		    				expr.addTerm(1, f[k+n-2][i][k][l]);
		    				expr.addTerm(-1, x[i][k+n-2]);
		    				robustModel.addConstr(expr, GRB.LESS_EQUAL, 0, "LIGACAO"+Integer.toString(i+1)+Integer.toString(k+n-1)+Integer.toString(k+n-1)+Integer.toString(l+n-1));
		    			}
		    
		    for(int i=0; i<=n1-1; i++)
		    	for(int k=0; k<=n-1; k++)
		    		for(int l=0; l<=n-1; l++)
		    			if(k<l)
		    			{
		    				expr = new GRBLinExpr();
		    				expr.addTerm(1, f[i][l+n-2][k][l]);
		    				expr.addTerm(-1, x[i][l+n-2]);
		    				robustModel.addConstr(expr, GRB.LESS_EQUAL, 0, "LIGACAO"+Integer.toString(i+1)+Integer.toString(l+n-1)+Integer.toString(k+n-1)+Integer.toString(l+n-1));
		    			}
		    
		  //y constrains
		    //RestY1
		    for(int k=0; k<=n-2; k++)
	    		for(int l=k+1; l<=n-1; l++)
	    		{
	    			expr = new GRBLinExpr();
	    			for(int i=0; i<=n-3; i++)
	    				expr.addTerm(1, y[i][k][l]);
	    			robustModel.addConstr(expr, GRB.EQUAL, 1, "RESTY1"+Integer.toString(k+n-1)+Integer.toString(l+n-1));
	    		}
		    
		  //RestY2
		    for(int k=0; k<=n-2; k++)
	    		for(int l=k+1; l<=n-1; l++)
	    		{
	    			expr = new GRBLinExpr();
	    			for(int i=0; i<=n-3; i++)
	    				expr.addTerm(i+2, y[i][k][l]);
	    			for(int i=0; i<=n1-1; i++)
	    				for(int j=0; j<=n1-1; j++)
	    					if(i!=j)
	    						expr.addTerm(-1, f[i][j][k][l]);
	    			robustModel.addConstr(expr, GRB.EQUAL, 2, "RESTY2"+Integer.toString(k+n-1)+Integer.toString(l+n-1));
	    		}
		    
		  //RestY3
		    expr = new GRBLinExpr();
		    for(int i=0; i<=n-3; i++)
		    	for(int k=0; k<=n-2; k++)
		    		for(int l=k+1; l<=n-1; l++)
		    			expr.addTerm(2*(i+2)*Math.pow(2, -(i+2)), y[i][k][l]);
		    robustModel.addConstr(expr, GRB.EQUAL, 2*n-3, "RESTY3");
		    
		  //RestY4
		    for(int k=0; k<=n-1; k++)
		    {
		       	expr = new GRBLinExpr();
		       	for(int i=0; i<=n-3; i++)
		       		for(int l=0; l<=n-1; l++)
		       			if(k<l)
		       				expr.addTerm(Math.pow(2, -(i+2)), y[i][k][l]);
		       			else
		       				if(k>l)
		       					expr.addTerm(Math.pow(2, -(i+2)), y[i][l][k]);
		       	robustModel.addConstr(expr, GRB.EQUAL, 0.5, "RESTY4"+Integer.toString(k+n-1));
		    }
		    
		    //v constrains
		    expr = new GRBLinExpr();
		    for(int k=0; k<=n-2; k++)
	    		for(int l=k+1; l<=n-1; l++)
	    		{
	    			expr.addTerm(-1, v[k][l]);
	    			expr.addTerm(-1, teta);
	    			for(int i=0; i<=n-3; i++)
	    		    	expr.addTerm(B[k][l]*Math.pow(2, -(i+2)), y[i][k][l]);
	    		  
	    		    robustModel.addConstr(expr, GRB.LESS_EQUAL, 0, "RESTV"+Integer.toString(k+n-1)+Integer.toString(l+n-1));
	    		}
		    
		    
		    
		    //extra constrains
		    //RestEX1
		    for(int i=0; i<=n-4; i++)
		    	for(int k=0; k<=n-1; k++)
		    	{
		    		expr = new GRBLinExpr();
		    		for(int l=0; l<=n-1; l++)
		    			if(l>k)
		    			{
		    				expr.addTerm(1, y[n-3][k][l]);
		    				expr.addTerm(-2, y[i][k][l]);
		    			}
		    			else
		    				if(l<k)
		    				{
		    					expr.addTerm(1, y[n-3][l][k]);
		    					expr.addTerm(-2, y[i][l][k]);
		    				}
		    		robustModel.addConstr(expr, GRB.LESS_EQUAL, 0, "RESTEX1"+Integer.toString(i+2)+Integer.toString(k+n-1));
		    	}
		    
		  //RestEX2
		    expr = new GRBLinExpr();
		    for(int k=0; k<=n-2; k++)
		    	for(int l=k+1; l<=n-1; l++)
	    			expr.addTerm(1, y[n-3][k][l]);
		    robustModel.addConstr(expr, GRB.LESS_EQUAL, 8, "RESTEX2");
		    
		  //RestEX3
		    for(int i=0; i<=n-4; i++)
		    	for(int k=0; k<=n-3; k++)
			    	for(int l=k+1; l<=n-2; l++)
			    		for(int q=l+1; q<=n-1; q++)
			    		{
			    			expr = new GRBLinExpr();
			    			expr.addTerm(1, y[0][k][l]);
			    			expr.addTerm(-1, y[i][k][q]);
			    			expr.addTerm(1, y[i][l][q]);
			    			robustModel.addConstr(expr, GRB.LESS_EQUAL, 1, "RESTEX3"+Integer.toString(i+2)+Integer.toString(k+n-1)+Integer.toString(l+n-1)+Integer.toString(q+n-1));
			    		}
		    
		  //RestEX4
		    for(int i=0; i<=n-4; i++)
		    	for(int k=0; k<=n-3; k++)
			    	for(int l=k+1; l<=n-2; l++)
			    		for(int q=l+1; q<=n-1; q++)
			    		{
			    			expr = new GRBLinExpr();
			    			expr.addTerm(1, y[0][k][l]);
			    			expr.addTerm(1, y[i][k][q]);
			    			expr.addTerm(-1, y[i][l][q]);
			    			robustModel.addConstr(expr, GRB.LESS_EQUAL, 1, "RESTEX4"+Integer.toString(i+2)+Integer.toString(k+n-1)+Integer.toString(l+n-1)+Integer.toString(q+n-1));
			    		}
		   
		  //RestEX5
		    for(int p=2; p<=n-1; p++)
		    	for(int j=2; j<=n-1; j++)
		    		for(int k=0; k<=n-3;k++)
		    			for(int l=k+1; l<=n-2; l++)
		    				for(int q=l+1; q<=n-1; q++)
		    				{
		    					expr = new GRBLinExpr();
		    					for(int i=Math.max(2, Math.abs(p-j)); i<=Math.min(p+j-2, n-1); i++)
		    						expr.addTerm(1, y[i-2][k][l]);
		    					expr.addTerm(-1, y[p-2][k][l]);
		    					expr.addTerm(-1, y[j-2][k][l]);
		    					robustModel.addConstr(expr, GRB.GREATER_EQUAL, -1, "RESTEX5"+Integer.toString(p)+Integer.toString(j)+Integer.toString(k+n-1)+Integer.toString(l+n-1)+Integer.toString(q+n-1));		    			
		    				}
		
		    // Optimize model
		    robustModel.optimize();
		    
		    //Get values of the model
		    objectValue = robustModel.get(GRB.DoubleAttr.ObjVal);
		//    System.out.println("Obj: " + robustModel.get(GRB.DoubleAttr.ObjVal));
		    
		    for(int i = 0; i<=n1-1; i++)
		    	for(int j=i+1; j<=n2-1; j++)
		    		solX[i][j] =(int) x[i][j].get(GRB.DoubleAttr.X);
		    
		    for(int i=0; i<=n1-2;i++)
		    	for(int j= i+1; j<=n1-1; j++)		    	
		    		for(int k=0; k<=n-2; k++)
		    			for(int l=k+1; l<=n-1; l++)
		    			{
		    				solF[i][j][k][l]=(int) f[i][j][k][l].get(GRB.DoubleAttr.X);
		    				solF[j][i][k][l]=(int) f[j][i][k][l].get(GRB.DoubleAttr.X);
		    			}
		    	    	    
		    for(int i=0; i<=n1-1; i++)
		    	for(int k=0; k<=n-2; k++)
		    		for(int l=k+1; l<=n-1; l++)
		    				solF[k+n-2][i][k][l]= (int) f[k+n-2][i][k][l].get(GRB.DoubleAttr.X);
		    
		    
		    for(int i=0; i<=n1-1;i++)
		    	for(int k=0; k<=n-2; k++)
		    		for(int l=k+1; l<=n-1; l++)
		   				solF[i][l+n-2][k][l] = (int) f[i][l+n-2][k][l].get(GRB.DoubleAttr.X);
		    				
		    for(int i=0; i<=n-3;i++)
		    	for(int k=0; k<=n-2; k++)
		    		for(int l=k+1; l<=n-1; l++)
		    			solY[i][k][l]= (int) y[i][k][l].get(GRB.DoubleAttr.X);
		    
		    setListsRoutersDevices();
		   
		    /*
		    for(int i = 0; i<=n1-1; i++)
		    	for(int j=i+1; j<=n2-1; j++)
		    		if(solX[i][j] == 1)
		    			System.out.println("Solution x("+i+","+j+")"); 
		    */
		    
		    // Dispose of model and environment
		    robustModel.dispose();
		    env.dispose();
		    
		    
		} 
		catch (GRBException e) 
		{
		      System.out.println("Error code: " + e.getErrorCode() + ". " + e.getMessage());
		}
	}
	
	public void setListsRoutersDevices()
	{
		for(int i = 0; i<=n1-1; i++)
	   	{
	    	Routers r= new Routers("R"+Integer.toString(i+1));
	    	listRouters.add(r);		
	    }
		
		for(int i = 0; i<=n1-2; i++)
	    	for(int j=i+1; j<=n1-1; j++)
	    		if(solX[i][j]==1)
	    		{
	    			listRouters.get(i).addRouter(listRouters.get(j));
	    			listRouters.get(j).addRouter(listRouters.get(i));
	    		}
		
		for(int i = 0; i<=n1-1; i++)
	    	for(int j=n1; j<=n2-1; j++)
	    		if(solX[i][j]==1)
	    		{
	    			listRouters.get(i).addDevice(listDevices.get(j-n1));
	    			listDevices.get(j-n1).setGateway(listRouters.get(i));
	    		}		
	}
	
	public List<Device> getListDevices()
	{
		return listDevices;
	}
	
	public List<Routers> getListRouter()
	{
		return listRouters;
	}
	

    
}
