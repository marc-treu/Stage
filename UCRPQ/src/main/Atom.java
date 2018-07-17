package main;

import java.util.Arrays;
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
	
	public List<String> getEtiquette() {
		return Arrays.asList(this.etiquette); 
	}

	@Override
	public boolean isCypherable() {
		return true;
	}

	
	public Atom flatten() {
		return this;
	}

	@Override
	public int getLength() {
		return 1;
	}

	@Override
	public RegExp getRename(int i) {
		return new Atom(this.etiquette+String.valueOf(i),direction);
	}

	@Override
	public List<String> getInitaux() {
		return Arrays.asList(this.etiquette);
	}

	@Override
	public List<String> getSuivant(String s) {
		
		if (s.equals("*")) // Si on viens d'une étoile
			return Arrays.asList(this.etiquette);
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsEtiquette(String s) {
		return this.etiquette.equals(s) ? true : false;
	}
	
}
