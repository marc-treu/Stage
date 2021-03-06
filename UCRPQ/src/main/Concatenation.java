package main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Concatenation extends NAry {
	
	private static int x = 0;

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

	public String toCypher() {

		StringBuilder sb = new StringBuilder();
		Set<String> hs = new HashSet<String>();
		String temp;

		hs.addAll(this.children.get(0).getEtiquette());
		sb.append(this.children.get(0).toCypher());

		for(int i=1 ;i<this.children.size();++i) {
			
			if(!hs.stream().anyMatch(this.children.get(i).getEtiquette()::contains)) {
				sb.append("()");
				sb.append(this.children.get(i).toCypher());
			}
			else {
				temp = "_x"+(x++);
				sb.append("("+temp+")\nMATCH ("+temp+")");
				sb.append(this.children.get(i).toCypher());
			}
			
			hs.addAll(this.children.get(i).getEtiquette());

		}

		return sb.toString();
	}

	@Override
	public boolean isCypherable() {

		for (RegExp e : this.children) {
			if(!(e.isCypherable())) {
				return false;
			}
		}
		return true;
	}

	@Override
	public List<String> getInitaux() {
		
		if(this.children.get(0).type()!=Type.Star || this.children.size()==1 )
			return this.children.get(0).getInitaux();
		
		
		List<String> resultat = new ArrayList<>();
		resultat.addAll(this.children.get(0).getInitaux());
		resultat.addAll(new Concatenation( 
				this.children.subList(1, this.children.size()).toArray(new RegExp[this.children.size()-1])).getInitaux() );
		
		return resultat;
	}


	@Override
	public boolean getSuivant(List<String> sv, String s) {
		
		for (int i = 0 ;i<this.children.size() ;++i) {
			if(this.children.get(i).containsEtiquette(s)){ // On trouve le bon descendant
				if(this.children.get(i).getSuivant(sv, s)) // Si on peux lui appliquer getSuivant()
					return true; // ie, si ce n'est pas un atom, une union, le derniere element d'une concatenation ou une étoile
				
				if (i < this.children.size()-1 ) {// Sinon, Si le descendant n'est pas le dernier element
					sv.addAll(new Concatenation( this.children.subList( // On ajoute la suite de la Concatenation a partie de l'element i+1
							i+1, this.children.size()).toArray(new RegExp[this.children.size()-1])).getInitaux() );
					return true;
				}
				else // Sinon, si c'est le dernier element, alors on renvoie faux
					return false;
			}
		}
		
		return false;
	}
	
	
}
