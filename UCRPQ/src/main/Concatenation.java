package main;

import java.util.ArrayList;
import java.util.List;

public class Concatenation extends NAry {

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
		sb.append(this.children.get(0).toCypher());

		for(int i=1 ;i<this.children.size();++i) {
			sb.append("()");
			sb.append(this.children.get(i).toCypher());
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
	public List<RegExp> getCypherable() {
		
		int i = 0;
		List<RegExp> resultat = new ArrayList<>();
		
		while (i<this.children.size()){
			
			// On ne s'interresse pas au cas de l'étoile pour l'instant
			// Il ne reste que le cas de l'union a traiter donc
			// Car l'Atom n'a pas besoin de transformation, et la Concatenation 
			// Ne pas exister grace a la fonction flatten
			if(this.children.get(i).type()==Type.Union) {// Si on trouve une union d'indice i
				
				RegExp[] temp = new RegExp[this.children.size()];
				
				// On va la séparer grace a getCypherable() dans Union
				for(RegExp e : this.children.get(i).getCypherable()) {
					// Et pour chaque nouvelle expression on va recree une nouvelle RPQ
					for (int j = 0;j<this.children.size();++j) {
						if (j!=i) // en faisant bien attention a garder le meme ordre 
							temp[j]=this.children.get(j);
						else
							temp[j]=e;
						
					}
					// créant ainsi une nouvelle Concatenation 
					resultat.add(new Concatenation(temp));
				}
				// On retourne notre liste de Concatenation 
				return resultat;				
			}
			++i;
		}
		return null;
	}

}
