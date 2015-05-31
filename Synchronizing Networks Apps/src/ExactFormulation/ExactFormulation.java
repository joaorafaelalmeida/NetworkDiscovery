package ExactFormulation;
import java.io.File;

import gurobi.*;
import Matrix.*;

public class ExactFormulation
{
	
	//private File file;
	//ImportMatrix matrixImport;
    int n; 
    double[][] D = new double[][]{{0, 0.1111, 0.1314, 0.1114, 0,1717},{0.1111, 0, 0.0811, 0.2022, 0.2625},{0.1314, 0.0811, 0, 0.2225, 0.2828},{0.1114, 0.2022, 0.2225, 0, 0.2222},{0.1717, 0.2625, 0.2828, 0.2222, 0}}; 
    //String [] names;
    int n1; 
    int n2;
    int	c;
    		
    
    
	/*public ExactFormulation(String fileName)
	{
		
		this.file = new File("nome");
		matrixImport = new ImportMatrix(file);
		n = matrixImport.getNumTotalDevices();
		D = matrixImport.getMatrixInDoubleArray();
		names = matrixImport.getDevicesNameInStringArray();
	}*/
	
	public ExactFormulation()
	{
		n=5;
	}
	
	public void getTree()
	{
		n1 = n-2;
		n2 = 2*n-2;
		c = (n-1)-Math.floorDiv(n, 2);
		GRBVar[][] x = new GRBVar[n-2][n2];
		GRBVar[][][][] f = new GRBVar[n2][n2][n][n];
		GRBVar[][][] y =new GRBVar[n-2][n][n];
		
		try 
		{   
			GRBEnv env = new GRBEnv("exact.log");
		    GRBModel exactModel = new GRBModel(env);
		    
		    
		    for(int i = 0; i<=c-1; i++)
				x[i][i+1]=exactModel.addVar(0, 1, 0, GRB.BINARY, "x"+Integer.toString(i+1)+Integer.toString(i+2));
		    
		    for(int j = n-2; j<=n2-1; j++)
		    	x[0][j]=exactModel.addVar(0, 1, 0, GRB.BINARY, "x"+Integer.toString(1)+Integer.toString(j+1));
		    
		    for(int i = 1; i<=n1-1; i++)
		    	for(int j=0; j<=n2-1 ; j++)
		    		if(j>i)
		    			x[i][j]=exactModel.addVar(0, 1, 0, GRB.BINARY, "x"+Integer.toString(i+1)+Integer.toString(j+1));
		    
		    for(int i=0; i<=c-1;i++)
		    	for(int k=0; k<=n-1; k++)
		    		for(int l=0; l<=n-1; l++)
		    			if(l>k)
		    				f[i][i+1][k][l]=exactModel.addVar(0, 1, 0, GRB.BINARY, "f"+Integer.toString(i+1)+Integer.toString(i+2)+Integer.toString(k+n-1)+Integer.toString(l+n-1));
		    
		    for(int i=0; i<=c-1;i++)
		    	for(int k=0; k<=n-1; k++)
		    		for(int l=0; l<=n-1; l++)
		    			if(l>k)
		    				f[i+1][i][k][l]=exactModel.addVar(0, 1, 0, GRB.BINARY, "f"+Integer.toString(i+2)+Integer.toString(i+1)+Integer.toString(k+n-1)+Integer.toString(l+n-1));
		    
		    for(int i=0; i<=n1-1;i++)
		    	for(int j=0; j<=n-3; j++)
		    		for(int k=0; k<=n-1; k++)
		    			for(int l=0; l<=n-1; l++)
		    				if(j>i && l>k)
		    					f[i][j][k][l]=exactModel.addVar(0, 1, 0, GRB.BINARY, "f"+Integer.toString(i+1)+Integer.toString(j+1)+Integer.toString(k+n-1)+Integer.toString(l+n-1));
		    
		    for(int i=0; i<=n1-1;i++)
		    	for(int j=0; j<=n-3; j++)
		    		for(int k=0; k<=n-1; k++)
		    			for(int l=0; l<=n-1; l++)
		    				if(j>i && l>k)
		    					f[j][i][k][l]=exactModel.addVar(0, 1, 0, GRB.BINARY, "f"+Integer.toString(j+1)+Integer.toString(i+1)+Integer.toString(k+n-1)+Integer.toString(l+n-1));
		    
		    for(int i=0; i<=n1-1;i++)
		    	for(int k=0; k<=n-1; k++)
		    		for(int l=0; l<=n-1; l++)
		    			if(l>k)
		    				f[k][i][k][l]=exactModel.addVar(0, 1, 0, GRB.BINARY, "f"+Integer.toString(k+n-1)+Integer.toString(i+1)+Integer.toString(k+n-1)+Integer.toString(l+n-1));
		    
		    
		    for(int i=0; i<=n1-1;i++)
		    	for(int k=0; k<=n-1; k++)
		    		for(int l=0; l<=n-1; l++)
		    			if(l>k)
		    				f[i][l][k][l]=exactModel.addVar(0, 1, 0, GRB.BINARY, "f"+Integer.toString(i+1)+Integer.toString(l+n-1)+Integer.toString(k+n-1)+Integer.toString(l+n-1));
		    
		    for(int i=0; i<=n-3;i++)
		    	for(int k=0; k<=n-1; k++)
		    		for(int l=0; l<=n-1; l++)
		    			if(l>k)
		    				y[i][k][l]=exactModel.addVar(0, 1, 0, GRB.BINARY, "y"+Integer.toString(i+2)+Integer.toString(k+n-1)+Integer.toString(l+n-1));
		    
		      exactModel.update();
		    
		    //Set objective
		    GRBLinExpr expr = new GRBLinExpr();
		    for(int i=0; i<=n-3; i++)
		    	for(int k=0; k<=n-1; k++)
		    		for(int l=0; l<=n-1; l++)
		    			if(l>k)
		    			{
		    				expr.addTerm(D[k][l]*Math.pow(2, -(i+2)), y[i][k][l]);
		    			}
		    exactModel.setObjective(expr, GRB.MINIMIZE); 
		  
		    System.out.println("antes das Constrains");
		    exactModel.update();
		    //Constrains:
		    //set tree
		    
		    System.out.println("antes de NAIN");
		    for(int j=n1; j<=n2-1; j++)
		    {
		    	expr = new GRBLinExpr();
		    	for(int i=0; i<=n1-1; i++)
		    		expr.addTerm(1, x[i][j]);
		    	exactModel.addConstr(expr, GRB.EQUAL, 1, "NAIN"+Integer.toString(j));
		    }
		    
		    System.out.println("antes de NATOTAL");
		    
		    GRBLinExpr expr1 = new GRBLinExpr();
		    for(int i=0; i<=n1-1; i++)
		    	for(int j=i+1; j<=n2-1; j++)
		    		expr1.addTerm(1, x[i][j]);
		    exactModel.addConstr(expr1, GRB.EQUAL, 2, "NATOTAL");
		    System.out.println("depois de Natotal");
		    
		    for(int i=0; i<=n1-1; i++)
		    {
		    	expr = new GRBLinExpr();
		    	/*for(int j=0; j<=n1-1; j++)
		    	{
		    		if(j>i)
		    			expr.addTerm(1, x[i][j]);
		    		else 
		    			if(j<i)
		    				expr.addTerm(1, x[j][i]);
		    			else
		    				expr.addTerm(0, x[i][j]);
		    	}*/
		    	for(int j=n1; j<=n2-1; j++)
		    		expr.addTerm(1, x[i][j]);
		    	exactModel.addConstr(expr, GRB.EQUAL, 2, "NAOUT"+Integer.toString(i));
		    }
		    
		    for(int i=0; i<=c-1; i++)
		    {
		    	expr = new GRBLinExpr();
		    	expr.addTerm(1, x[i][i+1]);
		    	exactModel.addConstr(expr, GRB.EQUAL, 1, "RestX1"+Integer.toString(i));
		    }
		    
		    expr = new GRBLinExpr();
		    for(int j=n1; j<=n2-1; j++)
		    	expr.addTerm(1, x[1][j]);
		    exactModel.addConstr(expr, GRB.EQUAL, 2, "RestX2");
		    
		    expr = new GRBLinExpr();
		    for(int j=n1; j<=n2-1; j++)
		    	expr.addTerm(1, x[n-3][j]);
		    exactModel.addConstr(expr, GRB.EQUAL, 2, "RestX3");
		    
		    for(int i=0; i<=n1-1; i++)
		    {
		    	expr = new GRBLinExpr();
			    for(int j=n1; j<=n2-1; j++)
			    	expr.addTerm(1, x[1][j]);
			    exactModel.addConstr(expr, GRB.LESS_EQUAL, 2, "RestX4"+Integer.toString(i));
		    }
		    
		    for(int i=0; i<=n1-1; i++)
		    	for(int j=0; j<=n1-1; j++)
		    		for(int a=0; a<=n1-1; a++)
		    		{
		    			expr = new GRBLinExpr();
		    			if(i<j && a<i && a<j)
		    			{
		    				expr.addTerm(1, x[i][j]);
		    				expr.addTerm(1, x[a][i]);
		    				expr.addTerm(1, x[a][j]);
		    			}
		    			exactModel.addConstr(expr, GRB.LESS_EQUAL, 2, "RestX5"+Integer.toString(i)+Integer.toString(j)+Integer.toString(a));
		    		}
		    
		    for(int i=0; i<=n1-1; i++)
		    	for(int j=0; j<=n1-1; j++)
		    		for(int a=0; a<=n1-1; a++)
		    		{
		    			expr = new GRBLinExpr();
		    			if(i<j && i<a && a<j)
		    			{
		    				expr.addTerm(1, x[i][j]);
		    				expr.addTerm(1, x[i][a]);
		    				expr.addTerm(1, x[a][j]);
		    			}
		    			exactModel.addConstr(expr, GRB.LESS_EQUAL, 2, "RestX5"+Integer.toString(i)+Integer.toString(j)+Integer.toString(a));
		    		}
		    
		    for(int i=0; i<=n1-1; i++)
		    	for(int j=0; j<=n1-1; j++)
		    		for(int a=0; a<=n1-1; a++)
		    		{
		    			expr = new GRBLinExpr();
		    			if(i<j && a<i && j<a)
		    			{
		    				expr.addTerm(1, x[i][j]);
		    				expr.addTerm(1, x[i][a]);
		    				expr.addTerm(1, x[j][a]);
		    			}
		    			exactModel.addConstr(expr, GRB.LESS_EQUAL, 2, "RestX5"+Integer.toString(i)+Integer.toString(j)+Integer.toString(a));
		    		}
		    
		    for(int k=0; k<=n-1; k++)
		    	for(int l=0; l<=n-1; l++)
		    		if(k<l)
		    		{
		    			expr = new GRBLinExpr();
		    			for(int j=0; j<=n1-1;j++)
		    				expr.addTerm(1, f[k+n-1][j][k][l]);
		    			exactModel.addConstr(expr, GRB.EQUAL, 1, "FLUXOk"+Integer.toString(k+n-1)+Integer.toString(l+n-1));
		    		}
		    
		    for(int k=0; k<=n-1; k++)
		    	for(int l=0; l<=n-1; l++)
		    		if(k<l)
		    		{
		    			expr = new GRBLinExpr();
		    			for(int j=0; j<=n1-1;j++)
		    				expr.addTerm(1, f[j][l+n-1][k][l]);
		    			exactModel.addConstr(expr, GRB.EQUAL, 1, "FLUXOl"+Integer.toString(k+n-1)+Integer.toString(l+n-1));
		    		}
		    
		    for(int i=0; i<=n1-1; i++)
		    	for(int k=0; k<=n-1; k++)
		    		for(int l=0; l<=n-1; l++)
		    			if(k<l)
		    			{
		    				expr = new GRBLinExpr();
		    				for(int j=0; j<=n1-1; j++)
		    					if(j!=i)
		    						expr.addTerm(1, f[i][j][k][l]);
		    				expr.addTerm(1, f[i][l+n-1][k][l]);
		    				for(int j=0; j<=n1-1; j++)
		    					if(j!=i)
		    						expr.addTerm(-1, f[j][i][k][l]);
		    				expr.addTerm(-1, f[k+n-1][1][k][l]);
		    				exactModel.addConstr(expr, GRB.EQUAL, 0, "FLUXO"+Integer.toString(i+1)+Integer.toString(k+n-1)+Integer.toString(l+n-1));
		    			}
		    
		    for(int i=0; i<=n1-1; i++)
		    	for(int k=0; k<=n-1; k++)
		    		for(int l=0; l<=n-1; l++)
		    			if(k<l)
		    			{
		    				expr = new GRBLinExpr();
		    				for(int j=0; j<=n1-1; j++)
		    					if(j!=i)
		    						expr.addTerm(1, f[i][j][k][l]);
		    				expr.addTerm(1, f[i][l+n-1][k][l]);
		    				expr.addTerm(-1, f[k+n-1][i][k][l]);
		    				exactModel.addConstr(expr, GRB.GREATER_EQUAL, 0, "FLUXO1"+Integer.toString(i+1)+Integer.toString(k+n-1)+Integer.toString(l+n-1));
		    			}

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
		    					expr.addTerm(1, f[j][l+n-1][k][l]);
		    					expr.addTerm(-1, f[i][j][k][l]);
		    					exactModel.addConstr(expr, GRB.GREATER_EQUAL, 0, "FLUXO2"+Integer.toString(i+1)+Integer.toString(j+1)+Integer.toString(k+n-1)+Integer.toString(l+n-1));
		    				}
		    
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
		    					exactModel.addConstr(expr, GRB.LESS_EQUAL, 0, "LIGACAO"+Integer.toString(i+1)+Integer.toString(j+1)+Integer.toString(k+n-1)+Integer.toString(l+n-1));
		    				}
		    
		    for(int i=0; i<=n1-1; i++)
		    	for(int k=0; k<=n-1; k++)
		    		for(int l=0; l<=n-1; l++)
		    			if(k<l)
		    			{
		    				expr = new GRBLinExpr();
		    				expr.addTerm(1, f[k+n-1][i][k][l]);
		    				expr.addTerm(-1, x[i][k+n-1]);
		    				exactModel.addConstr(expr, GRB.LESS_EQUAL, 0, "LIGACAO"+Integer.toString(i+1)+Integer.toString(k+n-1)+Integer.toString(k+n-1)+Integer.toString(l+n-1));
		    			}
		    
		    for(int i=0; i<=n1-1; i++)
		    	for(int k=0; k<=n-1; k++)
		    		for(int l=0; l<=n-1; l++)
		    			if(k<l)
		    			{
		    				expr = new GRBLinExpr();
		    				expr.addTerm(1, f[i][l+n-1][k][l]);
		    				expr.addTerm(-1, x[i][l+n-1]);
		    				exactModel.addConstr(expr, GRB.LESS_EQUAL, 0, "LIGACAO"+Integer.toString(i+1)+Integer.toString(l+n-1)+Integer.toString(k+n-1)+Integer.toString(l+n-1));
		    			}
		    
		  //y constrains 
		    for(int k=0; k<=n-1; k++)
	    		for(int l=0; l<=n-1; l++)
	    			if(k<l)
	    			{
	    				expr = new GRBLinExpr();
	    				for(int i=0; i<=n-3; i++)
	    					expr.addTerm(1, y[i][k][l]);
	    				exactModel.addConstr(expr, GRB.EQUAL, 1, "RESTY1"+Integer.toString(k+n-1)+Integer.toString(l+n-1));
	    			}
		    
		    for(int k=0; k<=n-1; k++)
	    		for(int l=0; l<=n-1; l++)
	    			if(k<l)
	    			{
	    				expr = new GRBLinExpr();
	    				for(int i=0; i<=n-3; i++)
	    					expr.addTerm(i+2, y[i][k][l]);
	    				for(int i=0; i<=n1-1; i++)
	    					for(int j=0; j<=n1-1; j++)
	    						if(i!=j)
	    							expr.addTerm(-1, f[i][j][k][l]);
	    				exactModel.addConstr(expr, GRB.EQUAL, 2, "RESTY2"+Integer.toString(k+n-1)+Integer.toString(l+n-1));
	    			}
		    
		    expr = new GRBLinExpr();
		    for(int i=0; i<=n-3; i++)
		    	for(int k=0; k<=n-1; k++)
		    		for(int l=0; l<=n-1; l++)
		    			if(k<l)
		    				expr.addTerm(2*(i+2)*Math.pow(2, -(i+2)), y[i][k][l]);
		    exactModel.addConstr(expr, GRB.EQUAL, 2*n-3, "RESTY3");
		    
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
		       	exactModel.addConstr(expr, GRB.EQUAL, 0.5, "RESTY4"+Integer.toString(k+n-1));
		    }
		    
		    //extra constrains
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
		    		exactModel.addConstr(expr, GRB.LESS_EQUAL, 0, "RESTEX1"+Integer.toString(i+2)+Integer.toString(k+n-1));
		    	}
		    
		    expr = new GRBLinExpr();
		    for(int k=0; k<=n-1; k++)
		    	for(int l=0; l<=n-1; l++)
	    			if(l>k)
	    				expr.addTerm(1, y[n-3][l][k]);
		    exactModel.addConstr(expr, GRB.LESS_EQUAL, 8, "RESTEX2");
		    
		    for(int i=0; i<=n-4; i++)
		    	for(int k=0; k<=n-1; k++)
			    	for(int l=0; l<=n-1; l++)
			    		for(int q=0; q<=n-1; q++)
			    			if(k<l && k<q && l<q)
			    			{
			    				expr = new GRBLinExpr();
			    				expr.addTerm(1, y[0][k][l]);
			    				expr.addTerm(-1, y[i][k][q]);
			    				expr.addTerm(1, y[i][l][q]);
			    				exactModel.addConstr(expr, GRB.LESS_EQUAL, 1, "RESTEX3"+Integer.toString(i+2)+Integer.toString(k+n-1)+Integer.toString(l+n-1)+Integer.toString(q+n-1));
			    			}
		    
		    for(int i=0; i<=n-4; i++)
		    	for(int k=0; k<=n-1; k++)
			    	for(int l=0; l<=n-1; l++)
			    		for(int q=0; q<=n-1; q++)
			    			if(k<l && k<q && l<q)
			    			{
			    				expr = new GRBLinExpr();
			    				expr.addTerm(1, y[0][k][l]);
			    				expr.addTerm(1, y[i][k][q]);
			    				expr.addTerm(-1, y[i][l][q]);
			    				exactModel.addConstr(expr, GRB.LESS_EQUAL, 1, "RESTEX4"+Integer.toString(i+2)+Integer.toString(k+n-1)+Integer.toString(l+n-1)+Integer.toString(q+n-1));
			    			}
		    
		    for(int p=2; p<=n-1; p++)
		    	for(int j=2; j<=n-1; j++)
		    		for(int k=0; k<=n-1;k++)
		    			for(int l=0; l<=n-1; l++)
		    				for(int q=0; q<=n-1; q++)
		    					if(k<l && k<q && l<q)
		    					{
		    						expr = new GRBLinExpr();
		    						for(int i=Math.max(2, Math.abs(p-j)); i<=Math.min(p+j-2, n-1); i++)
		    							expr.addTerm(1, y[i-2][k][l]);
		    						expr.addTerm(-1, y[p-2][k][l]);
		    						expr.addTerm(-1, y[j-2][k][l]);
		    						exactModel.addConstr(expr, GRB.GREATER_EQUAL, -1, "RESTEX5"+Integer.toString(p)+Integer.toString(j)+Integer.toString(k+n-1)+Integer.toString(l+n-1)+Integer.toString(q+n-1));		    			
		    					}
		    					
		    
		    for(int q=2; q<=Math.floorDiv(n, 2);q++)
		    	for(int k=0; k<=n-1;k++)
		    		if(n>Math.pow(2, (q-1))+1)
		    		{
		    			expr = new GRBLinExpr();
		    			for(int i=0; i<=q-2; i++)
		    				for(int l=0; l<=n-1; l++)
		    					if(k<l)
		    						expr.addTerm(Math.pow(2, (q-i+2)), y[i][k][l]);
		    			exactModel.addConstr(expr, GRB.LESS_EQUAL, Math.pow(2, (q-1)), "RESTEX6"+Integer.toString(k+n-1)+Integer.toString(q));
		    		}
		    
		    // Optimize model
		    exactModel.optimize();
		    
		    //Print model
		    System.out.println("Obj: " + exactModel.get(GRB.DoubleAttr.ObjVal));
		    
		    // Dispose of model and environment

		    exactModel.dispose();
		    env.dispose();
		    
		    
		} 
		catch (GRBException e) 
		{
		      System.out.println("Error code: " + e.getErrorCode() + ". " + e.getMessage());
		}
	}
	
}


