import java.util.List;

public class Concatenation extends NAry {

	List<RegExp> children;
	String separator;
	
	public Concatenation(RegExp... args) {
		super(".",args);
	}	
	
	@Override
	public Type type() {
		return Type.Concatenation;
	}

	@Override
	public List<RegExp> children() {
		return this.children;
	}
	
	@Override
	public String toString() {
		return super.toString();
	}
	
}
