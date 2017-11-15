package solver;
import problem.*;
import java.io.IOException;
import java.util.List;

public class MainClass {
	public static void main(String args[]) throws IOException{
		ProblemSpec probSpec=new ProblemSpec();
		probSpec.loadInputFile("testcases/bronze1-hard.txt");
		MySolver mySolver = new MySolver(probSpec);
		mySolver.doOfflineComputation();
		//System.out.println(mySolver.getOptimalPolicy());
		for(List<Integer> key : mySolver.getStates())
			System.out.println(key + " - " +
				mySolver.getOptimalPolicy().get(key));	
		int count = 0;
		for (List<Integer> state : mySolver.getStates()) {
			count++;
			System.out.println("Num-" + count + "Fut State: " + state + "\tReward: " + mySolver.getfutureRewards().get(state) );
		}
			
	}
}
