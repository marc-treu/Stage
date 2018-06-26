package main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class CRPQ {

	List<RPQ> children;
	
	public CRPQ(List<RPQ> children) {
		this.children = children;
	}
	

	public String getCypherExpression() {
		return this.getCypherExpression(new HashSet<String>());
	}	
	
	public String getCypherExpression(Set<String> returnNode) {
		
		StringBuilder sb = new StringBuilder();
				
		if(!this.isCypherable()) {
			List<CRPQ> resultat = RewritingRules.rewriteCRPQ(this);
			
			if(!resultat.get(0).isCypherable()) {
				return "L'expression est non expressible en Cypher";
			}
			
			sb.append(resultat.get(0).toCypher(returnNode));
			
			for (int i = 1; i<resultat.size() ;++i) {
				
				if(!resultat.get(i).isCypherable()) {
					return "L'expression est non expressible en Cypher";
				}
				
				sb.append("\nUNION\n");
				sb.append(resultat.get(i).toCypher(returnNode));
			}
			
		}
		else
			sb.append(this.toCypher(returnNode));
		
		return sb.toString();
	}
	
		private String toCypher(Set<String> returnNode) {
		StringBuilder sb = new StringBuilder();
		
		Set<String> hs = new HashSet<String>(returnNode);
		sb.append("MATCH ("+this.children.get(0).origin+")"+this.children.get(0).expression.toCypher()+"("+this.children.get(0).destination+")");
		hs.add(this.children.get(0).origin);
		hs.add(this.children.get(0).destination);

		for (int i = 1; i<this.children.size() ;++i) {
			sb.append("\nMATCH ("+this.children.get(i).origin+")"+this.children.get(i).expression.toCypher()+"("+this.children.get(i).destination+")");
			hs.add(this.children.get(i).origin);
			hs.add(this.children.get(i).destination);
		}

		Iterator<String> it = hs.iterator();
		
		sb.append("\nRETURN "+it.next());
		while (it.hasNext()) {
			sb.append(", "+it.next());
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

	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		sb.append(this.children.get(0).toString());
		
		for(int i=1 ;i<this.children.size();++i) {
			sb.append(") & (");
			sb.append(this.children.get(i).toString());
		}
		sb.append(")");
		return sb.toString();
		
	}
	
	public List<String> getReturnNode(){
		List<String> resultat = new ArrayList<>();
		for (RPQ r : this.children) {
			resultat.addAll(r.getReturnNode());
		}
		return resultat;
	}
	
}
