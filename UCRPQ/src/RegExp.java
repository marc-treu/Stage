import java.util.List;

public interface RegExp {

	public enum Type{
		Atom,
		Union,
		Concatenation,
		Star;
	}
	
	public Type type();
	
	public List<RegExp> children();

	public String toCypher();
	
	public boolean isCypherable();
	
}
