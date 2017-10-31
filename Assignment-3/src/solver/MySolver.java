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
		double[][] mat = new double[][] { { 0.2, 0.2, 0.2, 0.2, 0.2 },
						 { 0.2, 0.2, 0.2, 0.1, 0.3 },
						 { 0.2, 0.2, 0.2, 0.1, 0.3 },
						 { 0.2, 0.2, 0.2, 0.1, 0.3 },
						 { 0.2, 0.2, 0.2, 0.1, 0.3 }
					   };
		
		Matrix m = new Matrix(mat);

		Matrix T = genTransFunction(m);
		for(int i = 0; i < T.getNumRows(); i++)
			System.out.println(T.getRow(i).toString());
		
	}


}
