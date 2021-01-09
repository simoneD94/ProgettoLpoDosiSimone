package parser.ast;

import visitors.Visitor;

public class SeasonOf extends UnaryOp {
	public SeasonOf(Exp exp) {
		super(exp);
		// TODO Auto-generated constructor stub
	}
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visitSeasonOf(exp);
		}
}