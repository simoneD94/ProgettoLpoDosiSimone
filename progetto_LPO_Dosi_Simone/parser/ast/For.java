package parser.ast;
import visitors.Visitor;
import static java.util.Objects.requireNonNull;

public class For implements Stmt
{
	private final VarIdent ident;
	private final Exp exp;
	private final Block forBlock;

	public For(VarIdent ident,Exp exp, Block forBlock) {
		this.ident=ident;
		this.exp = requireNonNull(exp);
		this.forBlock = requireNonNull(forBlock);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "("+ ident +","+ exp + "," + forBlock + ")";
	}

	@Override
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visitFor(ident,exp, forBlock);
	}

}
