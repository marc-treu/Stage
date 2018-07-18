package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Star implements RegExp {

	RegExp child;


	public Star(RegExp child) {
		this.child=child;
	}

	@Override
	public Type type() {
		return Type.Star;
	}

	@Override
	public List<RegExp> children() {
		return new ArrayList<>(Arrays.asList(this.child));
	}

	public String toString() {
		return this.child.type()==RegExp.Type.Atom ? "("+this.child.toString()+")*" : this.child.toString()+"*" ;
	}

	@Override
	public String toCypher() {
		StringBuilder sb = new StringBuilder();
		if(this.child.type()==RegExp.Type.Atom) {// si on a seulement un Atom
			sb.append( ((Atom)this.child).getDirection()==false ? "<-[:" : "-[:");
			sb.append( ((Atom)this.child).getEtiquette().get(0));
			sb.append( ((Atom)this.child).getDirection()==true ? "*0..]->" : "*0..]-");
		}
		else // si on a une Union d'Atom
			sb.append( ((Union)this.child).toCypher(true));
		return sb.toString();
	}

	@Override
	public boolean isCypherable() {
		// Si l'étoile est au desous de la Concatenation,
		// alors la requête est n'est pas exprimable
		if(this.child.isCypherable() && this.child.type()!=RegExp.Type.Concatenation)
			return true;
		return false;
	}

	public Star flatten() {
		if(this.child.type()==RegExp.Type.Star)// Si on a Star(Star(...))
			return new Star( ((Star)this.child).getChild().flatten());

		return new Star(this.child.flatten());
	}

	public RegExp getChild(){
		return this.child;
	}

	@Override
	public List<String> getEtiquette() {
		return this.child.getEtiquette();
	}

	@Override
	public int getLength() {
		return this.child.getLength();
	}

	@Override
	public RegExp getRename(int i) {
		return new Star(this.child.getRename(i));
	}

	@Override
	public List<String> getInitaux() {
		return this.child.getInitaux();
	}

	@Override
	public boolean getSuivant(List<String> sv, String s) {

		if(this.child.getSuivant(sv, s))
			return true;
		else {
			sv.addAll(this.child.getInitaux());
			return false;
		}
	}
	
	@Override
	public boolean containsEtiquette(String s) {
		return this.child.containsEtiquette(s);
	}

}
