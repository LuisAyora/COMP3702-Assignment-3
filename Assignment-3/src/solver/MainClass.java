package solver;
import problem.ProblemSpec;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;

public class MainClass {
	public static void main(String args[]) throws IOException{
		ProblemSpec probSpec=new ProblemSpec();
		probSpec.loadInputFile("testcases/bronze1.txt");
		MySolver mySolver = new MySolver(probSpec);
		int[] prevState = {2,0};
		int[] action = {1,0};
		double prob = mySolver.getReward(prevState, action);
		System.out.println("Reward for s="+Arrays.toString(prevState)+" and a="+Arrays.toString(action)+" is: "+Double.toString(prob));
		//System.out.println(x);
		
		ProblemSpec problem = new ProblemSpec();
		problem.loadInputFile("testcases/platinum1.txt");
		MySolver solver = new MySolver(problem);
		
		ArrayList<int []> actions = solver.getActions(problem);
		System.out.println(Integer.toString(actions.size()));
		for(int i = 0; i < actions.size(); i++)
			System.out.println(Arrays.toString(actions.get(i)));
	}
}
