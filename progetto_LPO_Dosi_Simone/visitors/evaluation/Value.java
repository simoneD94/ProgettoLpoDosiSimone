package visitors.evaluation;

import visitors.typechecking.Seasons;

public interface Value {
	/* default conversion methods */
	default int toInt() {
		throw new EvaluatorException("Expecting an integer");
	}

	default boolean toBool() {
		throw new EvaluatorException("Expecting a boolean");
	}

	default PairValue toProd() {
		throw new EvaluatorException("Expecting a pair");
	}
	default  Seasons toSeason()
	{
		throw new EvaluatorException("Expecting a season");
	}
}
