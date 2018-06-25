package main;

import java.util.List;

public class RPQ {
	
	String origin;
	String destination;
	RegExp expression;
	
	
	public RPQ(String origin, RegExp expression, String destination){
		this.origin = origin;
		this.destination = destination;
		this.expression = expression.flatten();
	}
	
	
	public String getCypherExpression() {
		
		StringBuilder sb = new StringBuilder();
				
		if(!this.isCypherable()) {
			List<RPQ> resultat = RewritingRules.rewriteRPQ(this);
			
			if(!resultat.get(0).isCypherable()) {
				return "L'expression est non expressible en Cypher";
			}
			
			sb.append(resultat.get(0).toCypher());
			
			for (int i = 1; i<resultat.size() ;++i) {
				
				if(!resultat.get(i).isCypherable()) {
					return "L'expression est non expressible en Cypher";
				}
				
				sb.append("\nUNION\n");
				sb.append(resultat.get(i).toCypher());
			}
			
		}
		else
			sb.append(this.toCypher());
		
		return sb.toString();
	}
	
	private String toCypher(){
		StringBuilder sb = new StringBuilder();
		
		sb.append("MATCH ("+this.origin+")"+this.expression.toCypher()+"("+this.destination+")");			
		sb.append("\nRETURN "+this.origin+", "+this.destination);
		
		return sb.toString();
	}
	
	/**
	 * Fonction qui teste si l'expression est exprimable en Cypher
	 * sans effectuer de modification
	 * Sont Exprimable directement en Cypher,
	 * les Atoms, les Concatenations et les Unions d'Atom, les Concatenations d'Union d'Atom
	 * Pour les Unions d'Atom, chaque Atom doit etre dans la meme direction ou bien toute les direction 
	 * sont presente pour chaque Atom
	 *  
	 *  return true si exprimable en Cypher tel quel, false sinon
	 */
	public boolean isCypherable() {
		return this.expression.isCypherable();
	}


	public String toString(){
		return this.origin+","+this.expression+","+this.destination;
	}

}
