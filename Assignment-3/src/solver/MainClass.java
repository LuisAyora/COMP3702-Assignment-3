package solver;
import problem.ProblemSpec;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class MainClass {
	public static void main(String args[]) throws IOException{
		ProblemSpec probSpec=new ProblemSpec();
		probSpec.loadInputFile("testcases/bronze1.txt");
		MySolver mySolver = new MySolver(probSpec);
		mySolver.doOfflineComputation();
		//System.out.println(mySolver.getOptimalPolicy());
		for(List<Integer> key : mySolver.getOptimalPolicy().keySet())
			System.out.println(key + " - " +
				mySolver.getOptimalPolicy().get(key));
		
		for (List<Integer> state : mySolver.getStates())
			System.out.println("Fut State: " + state + " Reward: " + mySolver.getfutureRewards().get(state));
	}
}
