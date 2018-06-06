import java.util.List;

public class Atom implements RegExp {

	String etiquette;
	
	public Atom(String e) {
		this.etiquette = e;
	}
	
	
	@Override
	public Type type() {
		return Type.Atom;
	}


	@Override
	public List<RegExp> children() {
		throw new UnsupportedOperationException();
	}

}
