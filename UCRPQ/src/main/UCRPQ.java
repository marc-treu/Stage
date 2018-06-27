package main;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class UCRPQ {

	List<CRPQ> children;

	public UCRPQ(List<CRPQ> children) {
		this.children = children;
	}

	
	public String getCypherExpression() {
		
		StringBuilder sb = new StringBuilder();
				
		if(!this.isCypherable()) {
			UCRPQ resultat = RewritingRules.rewriteUCRPQ(this);
			
			if(!resultat.isCypherable()) {
				return "L'expression est non expressible en Cypher";
			}
			
			sb.append(resultat.toCypher());
			
		}
		else
			sb.append(this.toCypher());
		
		return sb.toString();
	}

	private String toCypher() {
		
		StringBuilder sb = new StringBuilder();
		Iterator<CRPQ> it = this.children.iterator();
		Set<String> sr = this.getReturnNode();
		
		sb.append(it.next().getCypherExpression(sr));

		while(it.hasNext()) {
			sb.append("\nUNION ALL\n");
			sb.append(it.next().getCypherExpression(sr));
		}

		return sb.toString();
	}

	private Set<String> getReturnNode() {
		Set<String> resultat = new HashSet<String>();
		
		for(CRPQ cr : this.children) {
			resultat.addAll(cr.getReturnNode());
		}
		
		return resultat;
	}


	public boolean isCypherable() {
		for (CRPQ e : this.children) {
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
			sb.append(") | (");
			sb.append(this.children.get(i).toString());
		}
		sb.append(")");
		return sb.toString();

	}

}

