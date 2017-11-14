package solver;
import problem.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class MainClass {
	public static void main(String args[]) throws IOException{
		ProblemSpec probSpec=new ProblemSpec();
		probSpec.loadInputFile("testcases/platinum1.txt");
		MySolver mySolver = new MySolver(probSpec);
		mySolver.doOfflineComputation();
		//System.out.println(mySolver.getOptimalPolicy());
		for(List<Integer> key : mySolver.getStates())
			System.out.println(key + " - " +
				mySolver.getOptimalPolicy().get(key));	
		double[] uValIter = mySolver.getUValueIter();
		int count = 0;
		for (List<Integer> state : mySolver.getStates()) {
			count++;
			System.out.println("Num-" + count + "Fut State: " + state + "\tReward: " + mySolver.getfutureRewards().get(state) );
		}
			
	}
}
