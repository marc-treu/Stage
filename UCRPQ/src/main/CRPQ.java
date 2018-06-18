package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class CRPQ {

	List<RPQ> children;
	
	public CRPQ(List<RPQ> children) {
		this.children = children;
	}
	
	
	
	public String toCypher() {
		StringBuilder sb = new StringBuilder();
		
		if(this.isCypherable()) {
			Set<String> hs = new HashSet<String>();
			sb.append("MATCH ("+this.children.get(0).origin+")"+this.children.get(0).expression.toCypher()+"("+this.children.get(0).destination+")");
			hs.add(this.children.get(0).origin);
			hs.add(this.children.get(0).destination);

			for (int i = 1; i<this.children.size() ;++i) {
				sb.append(",\n\t("+this.children.get(i).origin+")"+this.children.get(i).expression.toCypher()+"("+this.children.get(i).destination+")");
				hs.add(this.children.get(i).origin);
				hs.add(this.children.get(i).destination);
			}

			Iterator<String> it = hs.iterator();
			
			sb.append("\nRETURN "+it.next());
			while (it.hasNext()) {
				sb.append(", "+it.next());
			}
		}else {
			sb.append("La CRPQ doit etre modifier");
		}
		
		return sb.toString();
	}
	
	public boolean isCypherable() {
		for (RPQ e : this.children) {
			if (!e.isCypherable())
				return false;
		}
		return true;
	}



	public List<CRPQ> getCypherable() {
		
		List<List <RPQ>> liste_rpq = new ArrayList<>();
		
		for (RPQ e : this.children) {
			List <RPQ> temp = e.getCypherable();
			if(temp!=null) 
				liste_rpq.add(temp);				
			else
				return null;
		}
				
		return getCypherableAux(liste_rpq);
	}
	
	
	private List<CRPQ> getCypherableAux(List<List <RPQ>> l) {
		List<CRPQ> resultat = new ArrayList<>();
		
		for (List<RPQ> liste_rpq : l) {
			for (RPQ r : liste_rpq) {
				resultat.add(new CRPQ(Arrays.asList(r)));
			}
		}
		
		return resultat;		
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		sb.append(this.children.get(0).toString());
		
		for(int i=1 ;i<this.children.size();++i) {
			sb.append(") AND (");
			sb.append(this.children.get(i).toString());
		}
		sb.append(")");
		return sb.toString();
		
	}
	
}
