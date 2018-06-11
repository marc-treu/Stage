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
	
	public String toString() {
		return (this.direction) ? this.etiquette: this.etiquette+"-";
	}

	@Override
	public String toCypher() {
		return (this.direction) ? "-[:"+this.etiquette+"]->" : "<-[:"+this.etiquette+"]-";
	}
	
	public boolean getDirection() {
		return this.direction;
	}
	
	public String getEtiquette() {
		return this.etiquette; 
	}

	@Override
	public boolean isCypherable() {
		return true;
	}

}
