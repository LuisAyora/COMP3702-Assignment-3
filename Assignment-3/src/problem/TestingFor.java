package problem;

import java.util.ArrayList;
import java.util.List;

public class TestingFor {
	
	public static void main(String[] args) {
	
		int i=0;
		int j=0;
		int k=0;
		//int index = 0;
		List<ArrayList<Integer>>Number = new ArrayList<ArrayList<Integer>>();	
		//List<Integer>Number=new ArrayList<Integer>();
		
		for (i=0;i<=5;i++) {
			for (j=0;j<=5-i;j++) {
				for (k=0; k <=5-i-j; k++) {
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
