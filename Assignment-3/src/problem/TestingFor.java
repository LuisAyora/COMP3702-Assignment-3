package problem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import problem.VentureManager;
import problem.ProblemSpec;

public class TestingFor {
	public ProblemSpec spec = new ProblemSpec();
	public VentureManager ventureManager1;
	
	public static void main(String[] args) {
	
		int i=0;
		int j=0;
		int k=0;
		int numMax = 5;
		List<ArrayList<Integer>>Number = new ArrayList<ArrayList<Integer>>();	
		//List<Integer>Number=new ArrayList<Integer>();
		
		for (i=0;i<=numMax;i++) {
			for (j=0;j<=numMax-i;j++) {
				for (k=0; k <=numMax-i-j; k++) {
					ArrayList<Integer>sublist=new ArrayList<Integer>();
					//ArrayList<Integer> sublist = new ArrayList<ArrayList<Integer>>();
					sublist.add(i);
					sublist.add(j);
					sublist.add(k);
					Number.add(sublist);
					System.out.println(sublist);
			}
				
			}
			//System.out.println(""+);
		}
		
	}
	
}
