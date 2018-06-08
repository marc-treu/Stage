import java.util.List;

public class Union extends NAry {
	
	List<RegExp> children;
	String separator;
	
	public Union(RegExp... args) {
		super("+",args);
	}
	
	@Override
	public Type type() {
		return Type.Union;
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
