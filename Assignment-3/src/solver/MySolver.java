package solver;

/**
 * COMP3702 A3 2017 Support Code
 * v1.0
 * last updated by Nicholas Collins 19/10/17
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import problem.VentureManager;
import problem.Matrix;
import problem.ProblemSpec;

public class MySolver implements FundingAllocationAgent {
	
	private ProblemSpec spec = new ProblemSpec();
	private VentureManager ventureManager;
    private List<Matrix> probabilities;
	
	public MySolver(ProblemSpec spec) throws IOException {
	    this.spec = spec;
		ventureManager = spec.getVentureManager();
        probabilities = spec.getProbabilities();
	}
	
	public void doOfflineComputation() {
	    // TODO Write your own code here.
	}
	
	public List<Integer> generateAdditionalFundingAmounts(List<Integer> manufacturingFunds,
														  int numFortnightsLeft) {
		// Example code that allocates an additional $10 000 to each venture.
		// TODO Replace this with your own code.

		List<Integer> additionalFunding = new ArrayList<Integer>();

		int totalManufacturingFunds = 0;
		for (int i : manufacturingFunds) {
			totalManufacturingFunds += i;
		}
		
		int totalAdditional = 0;
		for (int i = 0; i < ventureManager.getNumVentures(); i++) {
			if (totalManufacturingFunds >= ventureManager.getMaxManufacturingFunds() ||
			        totalAdditional >= ventureManager.getMaxAdditionalFunding()) {
				additionalFunding.add(0);
			} else {
				additionalFunding.add(1);
				totalAdditional ++;
				totalManufacturingFunds ++;
			}
		}

		return additionalFunding;
	}
	
	
	public static Matrix genTransFunction(Matrix m) {
		double[][] out = new double[m.getNumRows()][m.getNumCols()];
		for(int i = 0; i < m.getNumRows(); i++) {
			for(int j = 0; j < m.getNumCols(); j++) {
				if(j == 0) 
					out[i][j] = sumRow(m.getRow(i), i);
				else if(j <= i)
					out[i][j] = m.get(i, i - j);
				else
					out[i][j] = 0;
			}
		}
		return new Matrix(out);
		
	}
	
	private static double sumRow(List<Double> row, int index) {
		double sum = 0;
		for(int i = index; i < row.size(); i++)
			sum += row.get(i);
		return sum;
	}
	

	/**
	 * Obtains Reward of a state action pair
	 * @precondition size of state and action needs to be the same, problem already loaded
	 * @param state
	 * @param action
	 * @return reward defined as expected profit
	 */
	public double getReward(int[] state, int[] action) {
		int newState[]=sumArray(state,action);
		if (arrayElementSum(newState)>this.ventureManager.getMaxManufacturingFunds()) {
			throw new IllegalArgumentException("Action not Valid for given state at getReward(state,action)");
		}
		double totalFortnightReward = 0;
		for (int w =0;w<ventureManager.getNumVentures();w++ ) {
			double individualExpected = 0;
			for (int i = 0; i < probabilities.get(w).getNumCols(); i++) {
				int sold = Math.min(newState[w], i);
	            individualExpected += (sold) * spec.getSalePrices().get(w) *
	            		0.6 * probabilities.get(w).get(newState[w], i);
	            
	            int missed = i - sold;
	            individualExpected -= missed * spec.getSalePrices().get(w) 
	            		* 0.25 * probabilities.get(w).get(newState[w], i);
			}
			totalFortnightReward += individualExpected;
		}
			
		return totalFortnightReward;
	}
	
	/**
	 *  Sums two integer arrays
	 * @param arr1: first array
	 * @param arr2: second array
	 * @return  arr1+arr2
	 */
	public int[] sumArray(int[] arr1,int[] arr2) {
		int out[]=new int[arr1.length];
		for (int i = 0;i<arr1.length;i++) {
			out[i] = arr1[i]+arr2[i];
		}
		return out;
	}
	

	public int arrayElementSum(int[] arr1) {
		int out = 0;
		for (int i = 0;i<arr1.length;i++) {
			out += arr1[i];
		}
		return out;
	}
	public double transitionFunction(List<Matrix> probabilities, 
			int[] state, int[] action, int[] statePrime) {
		double prob = 1;
		for(int i = 0; i < probabilities.size(); i++)
			prob *= probabilities.get(i).get(state[i] + action[i],
					statePrime[i]);
		return prob;
	}
	
	public List<int []> getCombinations3(int maxSum){
		List<int []> combinations = new ArrayList<int []>();
		for (int i=0;i<=maxSum;i++) {
			for (int j=0;j<=maxSum-i;j++) {
				for (int k=0; k <=maxSum-i-j; k++) {
					int[] sublist = {i,j,k};
					combinations.add(sublist);
					//System.out.println(sublist);
				}
			}
		}
		return combinations;
	}
	
	public List<int []> getCominations2(int maxSum) {
		List<int []> combinations = new ArrayList<int []>();
		for(int i = 0; i <= maxSum; i++) {
			for(int j = 0; j <= maxSum - i; j++) {
				int [] action = { i, j };
				combinations.add(action);
			}
		}
		return combinations;
	}
	
	public ArrayList<int []> getActions(ProblemSpec spec) {
		ArrayList<int []> actions = new ArrayList<int []>();
		int max = spec.getVentureManager().getMaxAdditionalFunding();
		if(spec.getProbabilities().size() == 2) {
			for(int i = 0; i <= max; i++) {
				for(int j = 0; j <= max - i; j++) {
					int [] action = { i, j };
					actions.add(action);
				}
			}
		}
		else if(spec.getProbabilities().size() == 3) {
			for(int i = 0; i <= max; i++) {
				for(int j = 0; j <= max - i; j++) {
					for(int k = 0; k <= max - i - j; k++) {
						int [] action = { i, j, k };
						actions.add(action);
					}
				}
			}
		}
		return actions;
	}
}

