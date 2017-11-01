package solver;
import problem.ProblemSpec;
import java.io.IOException;

public class MainClass {
	public void main(String args[]) throws IOException{
		ProblemSpec probSpec=new ProblemSpec();
		probSpec.loadInputFile("testcases/bronze1.txt");
		MySolver mySolver = new MySolver(probSpec);
		int[] prevState = {1,2};
		int[] action = {1,2};
		double prob = mySolver.getReward(prevState, action);
		System.out.println("Reward for s="+prevState.toString()+" and a="+action.toString()+" is: "+Double.toString(prob));
		
		
	}
}
