package main;

public class RPQ {
	
	String origin;
	String destination;
	RegExp expression;
	
	
	public RPQ(String origin, RegExp expression, String destination){
		this.origin = origin;
		this.destination = destination;
		this.expression = expression.flatten();
	}
	
	
	public String toCypher(){
		StringBuilder br = new StringBuilder();
		
		if(isCypherable()) {
			br.append("MATCH ("+this.origin+")"+this.expression.toCypher()+"("+this.destination+")");			
			br.append("\nRETURN "+this.origin+", "+this.destination);
		}else {
			br.append("La RPQ doit etre modifier");
		}
	
		return br.toString();
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
