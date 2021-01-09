package parser.ast;

import visitors.Visitor;

public class Hash extends UnaryOp {
	public Hash(Exp exp)
	{
		super(exp);
	}
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visitHash(exp);
	}
}
