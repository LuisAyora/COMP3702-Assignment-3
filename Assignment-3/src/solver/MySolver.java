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
	
	
	private static Matrix genTransFunction(Matrix m) {
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
	
	public static void main(String[] args) {
		double[][] mat1 = new double[][] { { 0.2, 0.2, 0.2, 0.2, 0.2 },
						 { 0.2, 0.2, 0.2, 0.1, 0.3 },
						 { 0.2, 0.2, 0.2, 0.1, 0.3 },
						 { 0.2, 0.2, 0.2, 0.1, 0.3 },
						 { 0.2, 0.2, 0.2, 0.1, 0.3 }
					   };
		double[][] mat2 = new double[][] { { 0.3, 0.2, 0.2, 0.1, 0.2 },
							 { 0.3, 0.2, 0.2, 0.1, 0.2 },
							 { 0.3, 0.2, 0.2, 0.1, 0.2 },
							 { 0.3, 0.2, 0.2, 0.1, 0.2 },
							 { 0.3, 0.2, 0.2, 0.1, 0.2 }
						   };
		
		Matrix m1 = new Matrix(mat1);
		Matrix m2 = new Matrix(mat2);

		Matrix T1 = genTransFunction(m1);
		for(int i = 0; i < T1.getNumRows(); i++)
			System.out.println(T1.getRow(i).toString());
		System.out.println("");
		Matrix T2 = genTransFunction(m1);
		for(int i = 0; i < T2.getNumRows(); i++)
			System.out.println(T1.getRow(i).toString());
		System.out.println("");
		
		ArrayList<Integer> state = new ArrayList<Integer>();
		state.add(2);
		state.add(1);
		
		ArrayList<Integer> action = new ArrayList<Integer>();
		action.add(1);
		action.add(0);
		
		ArrayList<Integer> statePrime = new ArrayList<Integer>();
		statePrime.add(1);
		statePrime.add(0);
		
		ArrayList<Matrix> probabilities = new ArrayList<Matrix>();
		probabilities.add(T1);
		probabilities.add(T2);
		
		double prob = transitionFunction(probabilities, state, action,
				statePrime);
		System.out.println(Double.toString(prob));
		
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
	
	private static double transitionFunction(List<Matrix> probabilities, 
			List<Integer> state, List<Integer> action, 
			List<Integer> statePrime) {
		double prob = 1;
		for(int i = 0; i < probabilities.size(); i++)
			prob *= probabilities.get(i).get(state.get(i) + action.get(i),
					statePrime.get(i));
		return prob;
	}
}
