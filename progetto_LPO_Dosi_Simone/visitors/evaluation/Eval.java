package visitors.evaluation;

import java.io.PrintWriter;

import environments.EnvironmentException;
import environments.GenEnvironment;
import parser.ast.Block;
import parser.ast.Exp;
import parser.ast.VarIdent;
import parser.ast.Stmt;
import parser.ast.StmtSeq;
import visitors.Visitor;
import visitors.typechecking.Seasons;

import static java.util.Objects.requireNonNull;

public class Eval implements Visitor<Value> {

	private final GenEnvironment<Value> env = new GenEnvironment<>();
	private final PrintWriter printWriter; // output stream used to print values

	public Eval() {
		printWriter = new PrintWriter(System.out, true);
	}

	public Eval(PrintWriter printWriter) {
		this.printWriter = requireNonNull(printWriter);
	}

	// dynamic semantics for programs; no value returned by the visitor

	@Override
	public Value visitProg(StmtSeq stmtSeq) {
		try {
			stmtSeq.accept(this);
			// possible runtime errors
			// EnvironmentException: undefined variable
		} catch (EnvironmentException e) {
			throw new EvaluatorException(e);
		}
		return null;
	}

	// dynamic semantics for statements; no value returned by the visitor

	@Override
	public Value visitAssignStmt(VarIdent ident, Exp exp) {
		env.update(ident, exp.accept(this));
		return null;
	}

	@Override
	public Value visitPrintStmt(Exp exp) {
		printWriter.println(exp.accept(this));
		return null;
	}

	@Override
	public Value visitVarStmt(VarIdent ident, Exp exp) {
		env.dec(ident, exp.accept(this));
		return null;
	}

	@Override
	public Value visitIfStmt(Exp exp, Block thenBlock, Block elseBlock) {
		if (exp.accept(this).toBool())
			thenBlock.accept(this);
		else if (elseBlock != null)
			elseBlock.accept(this);
		return null;
	}

	@Override
	public Value visitBlock(StmtSeq stmtSeq) {
		env.enterScope();
		stmtSeq.accept(this);
		env.exitScope();
		return null;
	}

	// dynamic semantics for sequences of statements
	// no value returned by the visitor

	@Override
	public Value visitSingleStmt(Stmt stmt) {
		stmt.accept(this);
		return null;
	}

	@Override
	public Value visitMoreStmt(Stmt first, StmtSeq rest) {
		first.accept(this);
		rest.accept(this);
		return null;
	}

	// dynamic semantics of expressions; a value is returned by the visitor

	@Override
	public Value visitAdd(Exp left, Exp right) {
		return new IntValue(left.accept(this).toInt() + right.accept(this).toInt());
	}

	@Override
	public Value visitIntLiteral(int value) {
		return new IntValue(value);
	}

	@Override
	public Value visitMul(Exp left, Exp right) {
		return new IntValue(left.accept(this).toInt() * right.accept(this).toInt());
	}

	@Override
	public Value visitSign(Exp exp) {
		return new IntValue(-exp.accept(this).toInt());
	}

	@Override
	public Value visitVarIdent(VarIdent id) {
		return env.lookup(id);
	}

	@Override
	public Value visitNot(Exp exp) {
		return new BoolValue(!exp.accept(this).toBool());
	}

	@Override
	public Value visitAnd(Exp left, Exp right) {
		return new BoolValue(left.accept(this).toBool() && right.accept(this).toBool());
	}

	@Override
	public Value visitBoolLiteral(boolean value) {
		return new BoolValue(value);
	}

	@Override
	public Value visitEq(Exp left, Exp right) {
		return new BoolValue(left.accept(this).equals(right.accept(this)));
	}

	@Override
	public Value visitPairLit(Exp left, Exp right) {
		return new PairValue(left.accept(this), right.accept(this));
	}

	@Override
	public Value visitFst(Exp exp) {
		return exp.accept(this).toProd().getFstVal();
	}

	@Override
	public Value visitSnd(Exp exp) {
		return exp.accept(this).toProd().getSndVal();
	}

	@Override
	public Value visitSeasonOf(Exp exp) {
		// TODO Auto-generated method stub
		return new SeasonsValue(Seasons.values()[exp.accept(this).toInt()]);
	}

	@Override
	public Value visitFor(VarIdent dent,Exp exp, Block forBlock) {
		// TODO Auto-generated method stub
		int x;
		for(x=dent.accept(this).toInt();x<=exp.accept(this).toInt();++x)
		{
			env.update(dent, new  IntValue(x));
			forBlock.accept(this);

		}
		env.update(dent, new  IntValue(x));
		return null;
	}

	@Override
	public Value visitHash(Exp exp) {
		// TODO Auto-generated method stub
		
		return new IntValue(exp.accept(this).toSeason().ordinal());
	}

	@Override
	public Value visitLess(Exp left, Exp right) {
		// TODO Auto-generated method stub
		Value aux=left.accept(this);
		return valutaLess(aux,right.accept(this));
	/*	if(aux instanceof IntValue)
			return new BoolValue(aux.toInt()<right.accept(this).toInt());
		if(aux instanceof BoolValue)
		{
			if(right.accept(this).toBool()==true)
				if(aux.toBool()==false)
					return new BoolValue(true);
			return new BoolValue(false);
		}
		if (aux instanceof SeasonsValue )
		{
			return new BoolValue(aux.toSeason().compareTo(right.accept(this).toSeason())<0);
		}
		if(aux instanceof PairValue)
		{
			
		}*/
	}
	
	public Value valutaLess(Value left, Value right)
	{
		if(left instanceof IntValue)
			return new BoolValue(left.toInt()<right.toInt());
		if(left instanceof BoolValue)
		{
			if(right.toBool()==true)
				if(left.toBool()==false)
					return new BoolValue(true);
			return new BoolValue(false);
		}
		if (left instanceof SeasonsValue )
		{
			return new BoolValue(left.toSeason().compareTo(right.toSeason())<0);
		}
		if(left instanceof PairValue)
		{
			return checkPair(left,right);
		}
		throw new EvaluatorException("unexpected value");
	}
	
	public Value checkPair(Value left,Value right)
	{
		if(right instanceof PairValue)
		{
			if(((PairValue)left).getFstVal() instanceof PairValue)
				checkPair(((PairValue)left).getFstVal(),((PairValue) right).getFstVal());
			
			if(((PairValue)left).getSndVal() instanceof PairValue)
				checkPair(((PairValue)left).getSndVal(),((PairValue) right).getSndVal());
			if(valutaLess(((PairValue)left).getFstVal(),((PairValue)right).getFstVal()).toBool()&&valutaLess(((PairValue)left).getSndVal(),((PairValue)right).getSndVal()).toBool())
			{
				return new BoolValue(true);
			}
			else return new BoolValue (false);
		}
		throw new EvaluatorException("unexpected value");
	}

	@Override
	public Value visitSeason(String string) {
		// TODO Auto-generated method stub
		return new SeasonsValue(Seasons.valueOf(string));
	}
}
