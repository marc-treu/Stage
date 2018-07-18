package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Union extends NAry {

	int direction=-1; // 0 si inverser, 1 si normale, 2 si non orienté et -1 si non défini


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
	public String toCypher() {
		return this.toCypher(false);
	}

	public String toCypher(boolean fromStar) {
		// Initialisation
		StringBuilder sb = new StringBuilder();
		Set<String> hs = new HashSet<String>();
 

		if(this.direction==-1)// Si la direction n'est pas encore defini, normalement, isCypherable() est appeler avant toCypher()
			this.isCypherable();// La direction des atoms est donnée dans isCypherable()

		sb.append( this.direction==0 ? "<-[:" : "-[:");

		sb.append(((Atom)this.children.get(0)).getEtiquette().get(0));
		hs.addAll(((Atom)this.children.get(0)).getEtiquette());

		for(int i=1 ;i<this.children.size();++i) {
			if (!hs.contains(((Atom)this.children.get(i)).getEtiquette().get(0))) {
				hs.addAll(((Atom)this.children.get(i)).getEtiquette());
				sb.append("|");
				sb.append(((Atom)this.children.get(i)).getEtiquette().get(0));
			}
		}


		sb.append( fromStar ? (this.direction==1 ? "*0..]->" : "*0..]-") : (this.direction==1 ? "]->" : "]-"));
		return sb.toString();
	}

	@Override
	public boolean isCypherable() {

		//Iniatialisation
		Map<String, Integer> hm = new HashMap<>();
		String tmp_etiquette = "\0";

		for (RegExp e : this.children) {
			if(e.type()!=RegExp.Type.Atom)
				return false;

			tmp_etiquette =((Atom)e).getEtiquette().get(0);

			if(hm.containsKey(tmp_etiquette)) { // si l'etiquette est deja dans le set
				if (hm.get(tmp_etiquette).equals(((Atom)e).getDirection() ? 1 : 0 )== false
							&& hm.get(tmp_etiquette)!=2)
					hm.put(tmp_etiquette, 2);
			}
			else
				hm.put(tmp_etiquette, ((Atom)e).getDirection() ? 1 : 0);
		}

		// On teste si tous les Atom sont dans la même direction
		for (Map.Entry<String, Integer> entry : hm.entrySet())
			if (entry.getValue() != hm.get(tmp_etiquette))
				return false;

		// On initialisation la direction des etiquettes
		this.direction = hm.get(tmp_etiquette);

		return true;
	}

	public int getDirection() {
		if(this.direction==-1)// Si la direction n'est pas encore defini, normalement, isCypherable() est appeler avant toCypher()
			this.isCypherable();// La direction des atoms est donnée dans isCypherable()
		return this.direction;
	}

	@Override
	public RegExp flatten() {
		Set<String> hs = new HashSet<String>();
		List<RegExp> resultat = new ArrayList<>();
		for(RegExp e : ((Union) super.flatten()).children) {
			if (!hs.contains(e.toString())) {
				hs.add(e.toString());
				resultat.add(e);
			}
		}
		return resultat.size()==1 ? resultat.get(0).flatten() : new Union(resultat.toArray(new RegExp [resultat.size()]));
	}

	@Override
	public List<String> getInitaux() {
		ArrayList<String> resultat = new ArrayList<>();
		for(RegExp e : this.children) {
			resultat.addAll(e.getInitaux());
		}
		return resultat;
	}

	@Override
	public boolean getSuivant(List<String> sv, String s) {
		
		for(int i = 0; i<this.children.size(); ++i) {
			if (this.children.get(i).containsEtiquette(s)) { // On trouve le descends
				return this.children.get(i).getSuivant(sv, s); // On lui applique getSuivant
			} // On renverra true dans les cas ou le descends est une étoile ou une concatenation
		}// Dont on n'est pas au dernier element
		
		return false;
	}

}
