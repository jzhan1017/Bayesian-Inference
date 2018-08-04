

package core;


import java.util.List;

import java.util.Random;


public class ApproximateInference {
	/**
	 * @author James Zhan
	 *
	 */
	public Distribution likelihoodWeighting(RandomVariable X, Assignment e, BayesianNetwork bn, int N) {
		Distribution W = new Distribution();
		for (int i = 0; i < X.getDomain().size(); i ++) {
			W.put(X.getDomain().get(i), 0);
		}
		for (int i = 0; i < N; i ++) {
			LikelihoodObject a = weightedSample(bn, bn.getVariableListTopologicallySorted(), e);
			Object o = a.getAssignment().get(X);
			double current = W.get(o);
			W.replace(o, current + a.getWeight());
		}
		W.normalize();
		return W;
	}
	
	public LikelihoodObject weightedSample(BayesianNetwork bn,  List<RandomVariable> list, Assignment e) {
		double w = 1;
		LikelihoodObject a = new LikelihoodObject();
		a.setAssignment(e.copy());
		Random generator = new Random();
		for (int i = 0; i < list.size(); i ++) {
			
			RandomVariable x1  = list.get(i);
			if (e.containsKey(x1)) {
				a.getAssignment().put(x1, e.get(x1));
				w = w*bn.getProb(x1, a.getAssignment());
				a.setWeight(w);
			} else {
				
				double samp = generator.nextDouble();
				double limit = 0;
				
				for(int j = 0; j < x1.domain.size();j++){
					a.getAssignment().set(x1,x1.domain.get(j));
					limit += bn.getProb(x1, a.getAssignment());
					
					if(samp <= limit){
						break;
					}
				}
			}
		}
		return a;
	}
	public Assignment priorSample(BayesianNetwork bn){
		Random generator = new Random();
		Assignment x = new Assignment();
		for(RandomVariable xI : bn.getVariableListTopologicallySorted()){
			double samp = generator.nextDouble();
			double limit = 0;
			
			for(int i = 0; i < xI.domain.size();i++){
				x.set(xI,xI.domain.get(i));
				limit += bn.getProb(xI, x);
				
				if(samp <= limit){
					break;
				}
			}
			
			
		}
		return x;
	}
//	
	public Assignment priorSample(List<RandomVariable> list,BayesianNetwork bn){
		Random generator = new Random();
		Assignment x = new Assignment();
		for(RandomVariable xI : list){
			double samp = generator.nextDouble();
			double limit = 0;
			
			for(int i = 0; i < xI.domain.size();i++){
				x.set(xI,xI.domain.get(i));
				limit += bn.getProb(xI, x);
				
				if(samp <= limit){
					break;
				}
			}
			
			
		}
		return x;
	}
	
	/**
	 * @author Clifford Joseph
	 *
	 */
	public Distribution rejectionSampling(RandomVariable x, Assignment e, BayesianNetwork bn,int samples){
		
		Distribution n = new Distribution();	
		for(Object xI : x.getDomain()){
			n.put(xI, 0);
		}
		for(int i = 0; i < samples; i++){
			Assignment pre = priorSample(bn);
			if(pre.isConsistent(e)){
				//System.out.println();
				for(Object xI: x.getDomain()){
					if(pre.get(x).equals(xI)){
						double update = n.get(xI)+1;
						n.put(xI, update);
					}
				}
			}
			else{
				//System.out.print(".");
			}
			
		}
		//System.out.println();
		n.normalize();
		return n;
	}
}	
		

//	public Distribution gibbsAsk(RandomVariable X, Assignment e, BayesianNetwork bn, int N) {
//		Distribution W = new Distribution();
//		for (int i = 0; i < X.getDomain().size(); i ++) {
//			W.put(X.getDomain().get(i), 0);
//		}
//		List<RandomVariable> list = bn.getVariableListTopologicallySorted();
//		ArrayList<RandomVariable> Z = new ArrayList<>();
//		for (RandomVariable rv: list) {
//			if (!e.containsKey(rv)) {
////			if (!e.variableSet().contains(rv)) {
//				Z.add(rv);
//			}
//		}
//		Assignment x = e.copy();
//		List<RandomVariable> markBlanket = new ArrayList<>();
//		for (int i = 0; i < N; i ++) {
//			Random generator = new Random();
//			//Assignment z = new Assignment();
//			for (RandomVariable rv: Z) {
//				//for(Object rvi: rv.getDomain()) {
//					//double pi = 1;
//					for (RandomVariable parent: bn.getParents(rv)) {
//						x.put(parent, e.get(parent));
//						markBlanket.add(parent);
//						//pi *= bn.getProb(rv, x);
//					}
//					for (RandomVariable children: bn.getChildren(rv)) {
//						x.put(children, e.get(children));
//						markBlanket.add(children);
//						for (RandomVariable parent: bn.getParents(children)) {
//							x.put(parent, e.get(parent));
//							markBlanket.add(parent);
//							//pi *= bn.getProb(parent, x);
//						}
//					}
//					double samp = generator.nextDouble();
//					double limit = 0;
//					
//					for(int j = 0; j < rv.domain.size();j++){
//						x.set(rv,rv.domain.get(j));
//						limit += bn.getProb(rv, x);
//						
//						if(samp <= limit){
//							break;
//						}
//					}
//
//					Object o = x.get(rv);
//					double current = W.get(o);
//					W.replace(o, current+1);
//				//}
//			}
//
//		}
//		W.normalize();
//		return W;
//	}
//}
