package solver;

/**
 * COMP3702 A3 2017 Support Code
 * v1.0
 * last updated by Nicholas Collins 19/10/17
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import problem.VentureManager;
import problem.Matrix;
import problem.ProblemSpec;

public class MySolver implements FundingAllocationAgent {
	
	private ProblemSpec spec = new ProblemSpec();
	private VentureManager ventureManager;
    private List<Matrix> probabilities;
    private List<Matrix> transitions;
    private List<List<Integer>> states;
    private List<List<Integer>> actions;
    private List<List<Integer>> optimalPolicyValueIter;
    private HashMap<List<Integer>,List<Integer>> validActions;
    private HashMap<List<Integer>,Integer> indecesMap;        //Change this to <List<Integer>,Integer>
    private double[] uValueIter;
    private double maxError;
    private double convThreshold;
    
	public MySolver(ProblemSpec spec) throws IOException {
	    this.spec = spec;
		ventureManager = spec.getVentureManager();
        probabilities = spec.getProbabilities();
        transitions = obtainTransitions(probabilities);
        states = getCombinations(ventureManager.getMaxManufacturingFunds());
        actions = getCombinations(ventureManager.getMaxAdditionalFunding());
        validActions = obtainValidActions();
        indecesMap = obtainIndecesMap();
        maxError = 1e-7;
        convThreshold = maxError;//*(1-spec.getDiscountFactor())/spec.getDiscountFactor();
	}
	
	public void doOfflineComputation() {
	    // TODO Write your own code here.
		uValueIter = valueIteration();
        optimalPolicyValueIter = this.obtainPolicy(uValueIter);
	}
	
	public List<Integer> generateAdditionalFundingAmounts(List<Integer> manufacturingFunds,
														  int numFortnightsLeft) {
		// Example code that allocates an additional $10 000 to each venture.
		// TODO Replace this with your own code.
		

		int index = indecesMap.get(manufacturingFunds);
		List<Integer> additionalFunding = optimalPolicyValueIter.get(index);

		return additionalFunding;

		/*List<Integer> additionalFunding = new ArrayList<Integer>();

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

		return additionalFunding;*/
	}
	
	/**
	 *  Value iteration algorithm
	 * @return
	 */
	public double[] valueIteration(){
		double [] u =new double[this.states.size()];
		double [] uDash =new double[this.states.size()];
		int counter =0;
		double dist = Double.POSITIVE_INFINITY;
		do {
			u = uDash;
			//Iterate over all states
			for (int i = 0;i < states.size(); i++) {
				double maxim = Double.NEGATIVE_INFINITY;
				//Iterate over actions and initiate maximum
				for (int a : validActions.get(states.get(i))) {
					//a is indeces of valid actions					
					double summation = 0;
					for (int j = 0; j<states.size(); j++) {
						summation += u[j]*transitionFunction(transitions,
								states.get(i), actions.get(a), states.get(j));
					}
					double candidate = getReward(states.get(i), actions.get(a))+
							spec.getDiscountFactor()*summation;
					maxim = Math.max(maxim, candidate);
				}
				uDash[i] = maxim;
				//Assign maximum to ith state
			}
			counter ++;
			dist = vectDist(u,uDash);
			System.out.println("u: \n"+Arrays.toString(u)+"\n");
			System.out.println("u: \n"+Arrays.toString(uDash)+"\n");
			System.out.println("Max Distance : "+Double.toString(dist)+"\n");
		}while(dist>convThreshold);
		System.out.println("Number Of Iterations till convergence: "+Integer.toString(counter)+"\n");
		return uDash;
	}
	
	/**
	 * Obtains the optimal actions given Long term reward values
	 * @param u
	 * @return
	 */
	public List<List<Integer>> obtainPolicy(double [] u){
		List<List<Integer>> policyActions = new ArrayList<List<Integer>>(states.size());
		for (int i = 0;i < states.size(); i++) {
			double maxim = Double.NEGATIVE_INFINITY;
			List<Integer> action = new ArrayList<Integer>();
			//Iterate over actions and initiate maximum
			for (int a : validActions.get(states.get(i))) {
				//a is indeces of valid actions
				double summation = 0;
				for (int j = 0; j<states.size(); j++) {
					summation += u[j]*transitionFunction(transitions,
							states.get(i), actions.get(a), states.get(j));
				}
				double candidate = getReward(states.get(i), actions.get(a))+
						spec.getDiscountFactor()*summation;
				if (candidate > maxim)
					maxim = candidate;
					action = actions.get(a);
			}
			policyActions.add(i,action);
			//Assign maximum to ith state
		}
		return policyActions;
	}
	
	
	/**
	 * Obtains Distance as maximum difference
	 * @param a first vector
	 * @param b second vector
	 * @return total - Manhattan distance
	 */
	private double vectDist(double[] a,double[] b) {
		double out = Double.NEGATIVE_INFINITY;
		for (int i = 0; i <a.length;i++) {
			double diff = Math.abs(a[i]-b[i]);
			if (diff > out)
				out = diff;
		}
		return out;
	}
	
	/**
	 * Gets absolute value of the difference between two lists
	 * @param a
	 * @param b
	 * @return
	 */
	private double[] absVectDiff(double[] a,double[] b) {
		double [] result = new double[a.length];
		for (int i = 0; i < a.length; i++) {
			result[i] = Math.abs(a[i]-b[i]);
		}
		return result;
	}
	
	/**
	 * Obtains a transformed matrix from the original ones
	 * @param m-probabilities matrix
	 * @return
	 */
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
	
	/**
	 * Transforms the probability matrices into transition matrices
	 * @param probs
	 * @return
	 */
	private List<Matrix> obtainTransitions(List<Matrix>probs){
		List<Matrix> output = new ArrayList<Matrix>();
		for (int i =0;i<probs.size();i++) {
			output.add(genTransFunction(probs.get(i)));
		}
		return output;
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
	public double getReward(List<Integer> state, List<Integer> action) {
		List<Integer> newState=sumLists(state,action);
		if (listElementSum(newState)>this.ventureManager.getMaxManufacturingFunds()) {
			throw new IllegalArgumentException("Action not Valid for given state at getReward(state,action)");
		}
		double totalFortnightReward = 0;
		for (int w =0;w<ventureManager.getNumVentures();w++ ) {
			double individualExpected = 0;
			for (int i = 0; i < probabilities.get(w).getNumCols(); i++) {
				int sold = Math.min(newState.get(w), i);
	            individualExpected += (sold) * spec.getSalePrices().get(w) *
	            		0.6 * probabilities.get(w).get(newState.get(w), i);
	            
	            int missed = i - sold;
	            individualExpected -= missed * spec.getSalePrices().get(w) 
	            		* 0.25 * probabilities.get(w).get(newState.get(w), i);
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
	public List<Integer> sumLists(List<Integer> state,List<Integer> action) {
		List<Integer> out = new ArrayList<Integer>();
		for (int i = 0;i<state.size();i++) {
			out.add(state.get(i)+action.get(i));
		}
		return out;
	}
	
	
	public int listElementSum(List<Integer> newState) {
		int out = 0;
		for (int i = 0;i<newState.size();i++) {
			out += newState.get(i);
		}
		return out;
	}
	public double transitionFunction(List<Matrix> probabilities, 
			List<Integer> list, List<Integer> list2, List<Integer> list3) {
		double prob = 1;
		for(int i = 0; i < probabilities.size(); i++)
			prob *= probabilities.get(i).get(list.get(i) + list2.get(i),
					list3.get(i));
		return prob;
	}
	
	public List<List<Integer>> getCombinations(int maxNum) {
		List<List<Integer>> actions = new ArrayList<List<Integer>>();
		if(spec.getProbabilities().size() == 2) {
			for(int i = 0; i <= maxNum; i++) {
				for(int j = 0; j <= maxNum - i; j++) {
					List<Integer> action = new ArrayList<Integer>();
					action.add(i);
					action.add(j);
					actions.add(action);
				}
			}
		}
		else if(spec.getProbabilities().size() == 3) {
			for(int i = 0; i <= maxNum; i++) {
				for(int j = 0; j <= maxNum - i; j++) {
					for(int k = 0; k <= maxNum - i - j; k++) {
						List<Integer> action = new ArrayList<Integer>();
						action.add(i);
						action.add(j);
						action.add(k);
						actions.add(action);
					}
				}
			}
		}
		return actions;
	}
	
	/**
	 * Obtains map of the mapping of valid actions indeces per state
	 * @return HashMap
	 */
	private HashMap<List<Integer>,List<Integer>>  obtainValidActions(){
		HashMap<List<Integer>,List<Integer>> mapp= new HashMap<List<Integer>,List<Integer>>();
		for (int i = 0;i<states.size();i++) {
			List<Integer> act= new ArrayList<Integer>();
			for (int j = 0; j < actions.size(); j++) {
				if (isActionValid(states.get(i), actions.get(j))) 
					//System.out.println("Inside Validation if\n");
					act.add(j);
			}
			/*List<Integer> acts = new ArrayList<Integer>();
			for (int k =0;k<acts.size();k++)
				acts.add(act.get(k));*/
			mapp.put(states.get(i),act);
		}
		return mapp;
	}
	
	/**
	 * Returns hashmap that maps from states to their index
	 * @return indMap HashMap<int[],Integer>
	 */
	private HashMap<List<Integer>,Integer> obtainIndecesMap(){
		HashMap<List<Integer>,Integer> indMap = new HashMap<List<Integer>,Integer>();
		for (int i = 0; i<this.states.size(); i++) {
			indMap.put(states.get(i), i);
		}
		return indMap;
	}
	
	/**
	 * Checks if an action is valid given an state
	 * @param list current state
	 * @param list2 action given
	 * @return if validity
	 */
	private boolean isActionValid(List<Integer> list,List<Integer> list2) {
		int sum = listElementSum(sumLists(list,list2));
		if (sum>ventureManager.getMaxManufacturingFunds()) {
			return false;
		}
		return true;
	}
	
	//Queries 
	
	public List<List<Integer>> getStates() {
		return states;
	}
	
	public List<List<Integer>> getActions(){
		return actions;
	}
	
	public HashMap<List<Integer>,List<Integer>> getValidActions(){
		return validActions;
	}
	
	public List<List<Integer>> getOptimalPolicyValueIter(){
		return optimalPolicyValueIter;
	}
	
	public double[] getUValueIter() {
		return uValueIter;
	}
}