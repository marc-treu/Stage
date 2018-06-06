import java.util.List;

public class Atom implements RegExp {

	String etiquette;
	boolean direction = true;
	
	public Atom(String e) {
		this.etiquette = e;
	}
	
	public Atom(String e, boolean d) {
		this.etiquette = e;
		this.direction = d;
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
