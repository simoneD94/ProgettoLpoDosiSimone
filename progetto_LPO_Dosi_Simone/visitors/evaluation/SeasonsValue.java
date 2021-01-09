package visitors.evaluation;

import visitors.typechecking.Seasons;

public class SeasonsValue extends PrimValue<Seasons> {
	public SeasonsValue(Seasons value)
	{
		super(value);
	}
	public final boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof SeasonsValue))
			return false;
		return value.equals(((SeasonsValue) obj).value);
	}
	@Override
	public Seasons toSeason()
	{
		return value;
	}
}
