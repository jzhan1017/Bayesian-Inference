/**
 * @author James Zhan and Clifford Joseph
 *
 */

package core;

import java.util.ArrayList;
import java.util.List;

import inference.Inferencer;

public class ExactInference implements Inferencer {
	
	@Override
	public Distribution ask(BayesianNetwork bn, RandomVariable X, Assignment e) {
		
		Distribution Q = new Distribution();
		// assign a probability for every possible domain value in variable X
		for (int i = 0; i < X.getDomain().size(); i ++) {
			Assignment exi = e.copy();
			exi.set(X, X.getDomain().get(i));
			Q.put(X.getDomain().get(i), enumerateAll(bn, bn.getVariableListTopologicallySorted(), exi));
		}
		Q.normalize();
		return Q;
	}
	
	public double enumerateAll(BayesianNetwork bn, List<RandomVariable> list, Assignment e) {
		ArrayList<RandomVariable> temp = new ArrayList<>(list);
		if (temp.isEmpty()) {
			return 1;
		} 
		RandomVariable Y = temp.remove(0);
		// if variable Y has already been assigned, then get the prior probability of variable Y given its parents
		// otherwise sum through variable Y's domain 
		if (e.containsKey(Y)) {
			return bn.getProb(Y, e)*enumerateAll(bn, temp, e);
		} else {
			double prob = 0;
			for (Object yI : Y.getDomain()) {
				Assignment ey  = e.copy();
				ey.set(Y, yI);
				prob += bn.getProb(Y, ey)*enumerateAll(bn, temp, ey);
			}
			return prob;
		}
		
	}
}
