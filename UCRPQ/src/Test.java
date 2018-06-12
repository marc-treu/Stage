
public class Test {

	public static void main(String[] args) {
		
		// x,a,y
		RPQ rpq1 = new RPQ("x",
					new Atom("a"),
					"y");
		System.out.println(rpq1);
		System.out.println(rpq1.toCypher());

		
		// x,a.b,y
		RPQ rpq2 = new RPQ("x",
				new Concatenation(new Atom("a"),new Atom("b")),
				"y");
		System.out.println(rpq2);
		System.out.println(rpq2.toCypher());
		
		
		// x,(a.b-),y
		RPQ rpq3 = new RPQ("x1",
					new Concatenation(new Atom("a"), new Atom("b",false)),
					"y"); 
		System.out.println(rpq3);
		System.out.println(rpq3.toCypher());

		
		// x,((a+b-).c),y
		RPQ rpq4 = new RPQ("x",
				new Concatenation(
				new Union(new Atom("a"),new Atom("b",false)),new Atom("c")),
				"y");
		System.out.println(rpq4);
		System.out.println(rpq4.toCypher());
		
		RPQ rpq5 = new RPQ("x",
				new Concatenation(
				new Union(new Atom("a"),new Atom("b")),new Atom("c",false)),
				"y");
		System.out.println(rpq5);
		System.out.println(rpq5.toCypher());


	}

}
