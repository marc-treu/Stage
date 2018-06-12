
public class RPQ {

	String origin;
	String destination;
	RegExp expression;


	public RPQ(String origin, RegExp expression, String destination){
		this.origin = origin;
		this.destination = destination;
		this.expression = expression;
	}


	public String toCypher(){
		StringBuilder br = new StringBuilder();

		if(isCypherable()) {
			br.append("MATCH ("+this.origin+")"+this.expression.toCypher()+"("+this.destination+")");
			br.append("\nRETURN "+this.origin+", "+this.destination);
		}

		return br.toString();
	}


	private boolean isCypherable() {
		return this.expression.isCypherable();
	}

	public String toString(){
		return this.origin+","+this.expression+","+this.destination;
	}

}
