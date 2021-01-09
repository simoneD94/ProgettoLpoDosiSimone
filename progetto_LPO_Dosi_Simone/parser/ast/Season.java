package parser.ast;

import visitors.Visitor;

public class Season implements Exp
{
	private String value;
	public Season(String value) {
	this.value=value;
	}
	public String getValue()
	{
		return value;
	}
	@Override
	public String toString()
	{
		return value;
	}
	@Override
	public <T> T accept(Visitor<T> visitor) {
		// TODO Auto-generated method stub
		return visitor.visitSeason(value);
	}
	
}
