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
		probSpec.loadInputFile("testcases/platinum1.txt");
		MySolver mySolver = new MySolver(probSpec);
		List<Integer> prevState = new ArrayList<Integer>();
		prevState.add(2);prevState.add(0);prevState.add(1);
		List<Integer> action = new ArrayList<Integer>();
		action.add(1);action.add(0);action.add(2);
		double prob = mySolver.getReward(prevState, action);
		mySolver.doOfflineComputation();
		System.out.println("Reward for s="+prevState.toString()+" and a="+action.toString()+" is: "+Double.toString(prob)+"\n\n");
		for (int i = 0; i <mySolver.getStates().size();i++) {
        	System.out.print("State: "+mySolver.getStates().get(i).toString()+"\t V: "+Double.toString(mySolver.getUValueIter()[i]) + 
        			"\t pi: "+mySolver.getOptimalPolicyValueIter().get(i).toString()+"\n");
        }

		HashMap<int[],Integer> theMap = new HashMap<int[],Integer>();
		int[] initial = {1,2};
		theMap.put(initial,Integer.valueOf(67));
		int[] fin = initial;
		System.out.println("\n\nOutput Integer: "+theMap.get(fin));
		

		HashMap<List<Integer>,Integer> theMap2 = new HashMap<List<Integer>,Integer>();
		List<Integer> init= new ArrayList<Integer>();
		init.add(6);init.add(8);
		theMap2.put(init,Integer.valueOf(67));
		List<Integer> fi= new ArrayList<Integer>();
		fi.add(6);fi.add(8);
		System.out.println("\n\nOutput Integer: "+theMap2.get(fi));
		
		//System.out.println(x);
	}
}
