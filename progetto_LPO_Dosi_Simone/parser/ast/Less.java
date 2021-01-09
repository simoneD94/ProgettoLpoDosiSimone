package parser.ast;

import visitors.Visitor;

public class Less extends BinaryOp
{
	public Less(Exp left,Exp right)
	{
		super(left,right);
	}
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visitLess(left, right);
	}
}
