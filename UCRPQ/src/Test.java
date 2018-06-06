
public class Test {

	public static void main(String[] args) {
		
		RPQ rpq1 = new RPQ("x1",
				new Concatenation(new Atom("a"), new Atom("b",false)),
				"x2"); 
		System.out.println(rpq1);
		
		RPQ rpq2 = new RPQ("x",
				new Union(new Star(new Atom("a")),new Atom("b")),
				"y");
		System.out.println(rpq2);
	}

}
