
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
		return null;
	}
	
}
