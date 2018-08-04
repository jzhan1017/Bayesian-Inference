Worked with Clifford Jordan Joseph, NETID = cjose12
To run Exact Inferencer type java MyBNInferencer filename queryvariable evidence
	Example is: java MyBNInferencer aima-alarm.xml B J true M true
To run LikelihoodWeighting type java LikelihoodMain samplesize filename queryvariable evidence
	Example is: java LikelihoodMain 10000 aima-alarm.xml B J true M true
	
To run RejectionSampling type java MyBNApproxInferencer samplesize filename queryvariable evidence
	Example is: java MyBNApproxInferencer 10000 aima-alarm.xml B J true M true