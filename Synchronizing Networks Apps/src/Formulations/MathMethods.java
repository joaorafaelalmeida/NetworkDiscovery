package Formulations;


public class MathMethods {
	
	public int factorial(int n) 
	{
		int fact = 1; // this  will be the result
	    for (int i = 1; i <= n; i++) 
	    {
	    	fact *= i;
	    }
	    return fact;
	}

	public int binom(int n, int k)
    {
        if (k==0)
            return 1;
        else if (k>n-k)
            return binom(n, n-k);
        else
            return binom(n-1, k-1)*n/k;
    }
}
