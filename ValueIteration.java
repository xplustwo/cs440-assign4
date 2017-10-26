
public class ValueIteration {
    // General settings 
    //private static double Ra = -3;            // reward in non-terminal states (used to initialize r[][])
	private static double Ra = 0;
    private static double gamma = 0.6;          // discount factor, standard is 1 but should be less than to be optimal
    //private static double pGood = 0.8;        // probability of taking intended action
   // private static double pBad = (1-pGood)/2; // 2 bad actions, split prob between them
    private static int N = 10000;             // max number of iterations of Value Iteration
    private static double deltaMin = 0.001;    // convergence criterion for iteration
 
    // Main data structures
    private static double U[][];  // long-term utility
    private static double Up[][]; // UPrime, used in updates
    private static double R[][];  // instantaneous reward
    private static char  Pi[][];  // policy
     
    //private static int rMax = 3, cMax = 4;
    private static int rMax = 2, cMax = 2; //a 2x2 to get all 4 states
     
    public static void main(String[] args)    {
        int r,c;
        double delta = 0;
        long elapsed = 0;
        
        
        // policy: initially null
        Pi = new char[rMax][cMax]; 
         
        // initialize U'
        Up = new double[rMax][cMax]; // row, col
        for (r=0; r < rMax; r++) {
            for (c=0; c < cMax; c++) {
                Up[r][c] = 1;
            }
        }
        // Don't initialize U: will set U=Uprime in iterations
        U = new double[rMax][cMax];
         
        
        //setting up mdp reward grid       
        R = new double[rMax][cMax]; // row, col
        for (r=0; r < rMax; r++) {
            for (c=0; c < cMax; c++) {
            	if(r == 0 && c == 1){
            		R[r][c] = 1; //positive sink state
            	} else{ 
            		R[r][c] = Ra; //all else are just 0
            	}
            }
        }        
         
         
        // Now perform Value Iteration.
        
        long startTime = System.currentTimeMillis();
        
        int n = 0;
        do {
            // Simultaneous updates: Initialize U = Up, then compute changes in Up using prev value of U.
            duplicate(Up, U); // src, dest
            n++;
            delta = 0;
            for (r=0; r < rMax; r++) {
                for (c=0; c < cMax; c++) {
                	
                    updateUPrime(r, c);
                    double diff = Math.abs(Up[r][c] - U[r][c]);
                    if (diff > delta)
                        delta = diff;
                    
                }
            }
            
            
            displayUtilities(n);  
            System.out.println();
            displayPolicies(n);
            System.out.println();
            
            
        } while (delta > deltaMin && n < N);
         
        // Display final matrix
        System.out.println("FINAL RESULT. Total number of iterations: " + n + "\n");
        
        
        //Report Computation Time
        elapsed = System.currentTimeMillis() - startTime;
        System.out.println("Total Computation Time of Value Iteration: " + elapsed + " ms.");
        
        
    }
    
    static int tmp = 1;
    static int turn = 1; 
    public static void updateUPrime(int r, int c)    {
        // IMPORTANT: this modifies the value of Up, using values in U.
         
        double a[] = new double[4]; // 4 actions
        
        
        //there are no terminal states
     
        // If at a sink state or unreachable state, use that value
        // There is only one on the map, at s3
        /*if ((r==0 && c==1)) {
            Up[r][c] = R[r][c];
        }
        else {*/

        	// i.e. max of (transition matrix probability * reward given a transition + future value)
        	
        	//a will hold sum of P(s'| a, s)*V(s'), in which V is the utility function and s' is a successor state to s
        	
        	a[0] = aNorth(r,c) * TProb(getA('N', r, c), r, c, nR(r,c), nC(r,c)) /*+ aWest(r,c)*(TProb(getA('W', r, c), r, c, wR(r,c), wC(r,c))) + aEast(r,c)*(TProb(getA('E', r, c), r, c, eR(r,c), eC(r,c)))*/;       	
        	a[1] = aSouth(r,c) * TProb(getA('S', r, c), r, c, sR(r,c), sC(r,c)) /*+ aWest(r,c)*(TProb(getA('W', r, c), r, c, wR(r,c), wC(r,c))) + aEast(r,c)*(TProb(getA('E', r, c), r, c, eR(r,c), eC(r,c)))*/;
            a[2] = aWest(r,c) * TProb(getA('W', r, c), r, c, wR(r,c), wC(r,c)) /*+ aSouth(r,c)*(TProb(getA('S', r, c), r, c, sR(r,c), sC(r,c))) + aNorth(r,c)*(TProb(getA('N', r, c), r, c, nR(r,c), nC(r,c)))*/;
            a[3] = aEast(r,c) * TProb(getA('E', r, c), r, c, eR(r,c), eC(r,c)) /*+ aSouth(r,c)*(TProb(getA('S', r, c), r, c, sR(r,c), sC(r,c))) + aNorth(r,c)*(TProb(getA('N', r, c), r, c, nR(r,c), nC(r,c)))*/;
            
            
            int best = maxindex(a);
            //System.out.println("values are: " + a[0] + " " + a[1] + " " + a[2] + " " + a[3]);
            
            //utility should now equal the max of  Transition Probability *( Reward of going to next state + gamma*(last Value) )
             
            Up[r][c] = R[r][c] + gamma * a[best];
             
            // update policy
            Pi[r][c] = (best == 0 ? 'N' : (best == 1 ? 'S' : (best == 2 ? 'W': 'E')));
        //}
    }
     
    public static int maxindex(double a[])     {
        int b=0;
        for (int i=1; i<a.length; i++)
            b = (a[b] > a[i]) ? b : i;
        return b;
    }
     
    public static double aNorth(int r, int c)    {
        // can't go north if at row 0 or if in cell (2,1)
        if ((r == 0))
            return U[r][c];
        return U[r-1][c];
    }
 
    public static double aSouth(int r, int c)    {
        // can't go south if at row 2 or if in cell (0,1)
        if ((r == rMax-1))
            return U[r][c];
        return U[r+1][c];
    }

    public static double aWest(int r, int c)    {
        // can't go west if at col 0 or if in cell (1,2)
        if ((c == 0))
            return U[r][c];
        return U[r][c-1];
    }
 
    public static double aEast(int r, int c)    {
        // can't go east if at col 3 or if in cell (1,0)
        if ((c == cMax-1))
            return U[r][c];
        return U[r][c+1];
    }
     
    public static void duplicate(double[][]src, double[][]dst)    {
        // Copy data from src to dst
        for (int x=0; x < src.length; x++) {
            for (int y=0; y < src[x].length; y++) {
                dst[x][y] = src[x][y];
            }
        }
    }
    
    
    
    //coordinates
    public static int nR(int r, int c){
        if ((r == 0))
            return r;
        return r-1;   	
    }
    public static int nC(int r, int c){
    	return c;
    }
    public static int wR(int r, int c){
        return r;  	
    }
    public static int wC(int r, int c){
        if ((c == 0))
            return c;
        return c-1;    	
    }
    public static int eR(int r, int c){
    	return r;
    }
    public static int eC(int r, int c){
        if ((c == cMax-1))
            return c;
        return c+1;    	
    }
    public static int sR(int r, int c){
        if ((r == 1))
            return r;
        return r+1;      	
    }
    public static int sC(int r, int c){
    	return c;
    }
    
    
    
    
    //char directions, e.g. n = north
    public static int getA(char dir, int r, int c){
    	
    	if(dir == 'N'){
    		if(r == 0 && c == 0){
    			return 2;
    		}
    		if(r == 0 && c == 1){
    			return -1; //invalid 
    		}
    		if(r == 1 && c == 0){
    			return 1;
    		}
    		if(r == 1 && c == 1){
    			return 1;
    		}
    	}
    	if (dir == 'W'){
    		if(r == 0 && c == 0){
    			return 3;
    		}
    		if(r == 0 && c == 1){
    			return 4; 
    		}
    		if(r == 1 && c == 0){
    			return 2;
    		}
    		if(r == 1 && c == 1){
    			return 4;
    		}   		
    	}
    	if (dir == 'S'){
    		if(r == 0 && c == 0){
    			return 3;
    		}
    		if(r == 0 && c == 1){
    			return 3; 
    		}
    		if(r == 1 && c == 0){
    			return 2;
    		}
    		if(r == 1 && c == 1){
    			return 4;
    		}     		
    	}
    	if (dir == 'E'){
    		if(r == 0 && c == 0){
    			return 2;
    		}
    		if(r == 0 && c == 1){
    			return -1; //invalid 
    		}
    		if(r == 1 && c == 0){
    			return 2;
    		}
    		if(r == 1 && c == 1){
    			return 1;
    		}        		
    	}
    	
    	return -1; //invalid
    }
    
    
    
    //actions determined by int, e.g. a1 = 1
    //r1,c1 = first state 
    //r2, c2 = second state 
    //all based on given MDP transition probability table T(si, a, sj)
    //this returns the "good" probability. 
    public static double TProb(int action, int r1, int c1, int r2, int c2){
    	
    	//S1, a1, S1
    	if((r1 == 1 && c1 == 0) && action == 1 && (r2 == 1 && c2 == 0)){
    		return 0.2;
    	}
    	//S1, a1, S2
    	if((r1 == 1 && c1 == 0) && action == 1 && (r2 == 0 && c2 == 0)){
    		return 0.8;
    	}    	
    	//S1, a2, S1
    	if((r1 == 1 && c1 == 0) && action == 2 && (r2 == 1 && c2 == 0)){
    		return 0.2;
    	}    	
    	//S1, a2, S4
    	if((r1 == 1 && c1 == 0) && action == 2 && (r2 == 1 && c2 == 1)){
    		return 0.8;
    	}     	
    	//S2, a2, S2
    	if((r1 == 0 && c1 == 0) && action == 2 && (r2 == 0 && c2 == 0)){
    		return 0.2;
    	} 
    	//S2, a2, S3
    	if((r1 == 0 && c1 == 0) && action == 2 && (r2 == 0 && c2 == 1)){
    		return 0.8;
    	}   	
    	//S2, a3, S2
    	if((r1 == 0 && c1 == 0) && action == 3 && (r2 == 0 && c2 == 0)){
    		return 0.2;
    	}   
    	//S2, a3, S1
    	if((r1 == 0 && c1 == 0) && action == 3 && (r2 == 1 && c2 == 0)){
    		return 0.8;
    	}     	
    	//S3, a4, S2
    	if((r1 == 0 && c1 == 1) && action == 4 && (r2 == 0 && c2 == 0)){
    		return 1.0;
    	} 
    	//S3, a3, S4
    	if((r1 == 0 && c1 == 1) && action == 3 && (r2 == 1 && c2 == 1)){
    		return 1.0;
    	}     	
    	//S4, a1, S4
    	if((r1 == 1 && c1 == 1) && action == 1 && (r2 == 1 && c2 == 1)){
    		return 0.1;
    	}    	
    	//S4, a1, S3
    	if((r1 == 1 && c1 == 1) && action == 1 && (r2 == 0 && c2 == 1)){
    		return 0.9;
    	}     	
    	//S4, a4, S4
    	if((r1 == 1 && c1 == 1) && action == 4 && (r2 == 1 && c2 == 1)){
    		return 0.2;
    	}  
    	//S4, a4, S1
    	if((r1 == 1 && c1 == 1) && action == 4 && (r2 == 1 && c2 == 0)){
    		return 0.8;
    	}      	
    	
    	//System.out.println("action is " + action + " and r and c are " + r1 + " , " + c1 + " and " + r2 + " , " + c2);
    	return 0;
    }
    
    
    public static void displayUtilities(int n){

        System.out.println("Utilities after " + n + " iteration(s):\n");
        for (int r=0; r < rMax; r++) {
            for (int c=0; c < cMax; c++) {
                System.out.printf("% 6.4f\t", U[r][c]);
            }
            System.out.print("\n");
        }
    }
    
    public static void displayPolicies(int n){
    	
        System.out.println("Policy after " + n + " iteration(s):\n");
    	
        for (int r=0; r < rMax; r++) {
            for (int c=0; c < cMax; c++) {
            	if(getA(Pi[r][c], r, c) == -1){
            		System.out.print("0" + "   ");
            	}
            	else{
            		System.out.print(getA(Pi[r][c], r, c) + "   ");
            	}
            }
            System.out.print("\n");
        }    	
    }
    
}
