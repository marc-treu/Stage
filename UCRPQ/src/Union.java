import java.util.List;

public class Union extends NAry {
	
	
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

	@Override
	public String toCypher() {
		// TODO Auto-generated method stub
		return null;
	}

}
