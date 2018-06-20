package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;



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
		RPQ rpq3 = new RPQ("x",
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

		RPQ rpq6 = new RPQ("x",
				new Concatenation(
				new Union(new Atom("a"),new Atom("a",false)),new Atom("c",false)),
				"y");
		System.out.println(rpq6);
		System.out.println(rpq6.toCypher());

		RPQ rpq7 = new RPQ("x",
				new Union(new Atom("a"),new Union(new Atom("b"),new Atom("c"))),
				"y");
		System.out.println(rpq7);
		System.out.println(rpq7.toCypher());

		RPQ rpq8 = new RPQ("x",
				new Star(new Atom("x")),
				"y");
		System.out.println(rpq8);
		System.out.println(rpq8.toCypher());

		RPQ rpq9 = new RPQ("x",
				new Star(new Union(new Atom("a"),new Atom("b",false),new Atom("b"))),
				"y");
		System.out.println(rpq9);

		System.out.println(rpq9.toCypher());


	    List<String> strings
	      = Arrays.asList( "x,((a+b).(a-+b-)).(a-+(a+a)+a-)*,y",
	                       "x,(a+b)+c,y",
	                       "x,(a+a),y",
	                       "x,(a+a-),y",
	                       "x,(a.b).c,y",
	                       "x,(a+b)*,y",
	                       "x,(a+b)*.a-*.b*.(a+c)*,y"
	                     );
	    List<RPQ> rpqs = strings.stream().map(Parser::parseRPQ).collect(Collectors.toList());
		for (RPQ rpq : rpqs) {
			System.out.println(rpq);
			System.out.println(rpq.toCypher());
		}


		System.out.println();


		CRPQ crpq1 = new CRPQ(new ArrayList<RPQ>(Arrays.asList(rpq1,rpq2)));
		CRPQ crpq2 = new CRPQ(new ArrayList<RPQ>(Arrays.asList(rpq3,rpq5)));
		UCRPQ ucrpq1 = new UCRPQ(new ArrayList<CRPQ>(Arrays.asList(crpq1,crpq2)));

		System.out.println(ucrpq1);
		System.out.println(ucrpq1.toCypher());



		System.out.println();

		RPQ rpq10 = new RPQ("x",
				new Union(new Concatenation(new Atom("a"), new Atom("b")),new Atom("c")),
				"y");
		CRPQ crpq = new CRPQ(new ArrayList<RPQ>(Arrays.asList(rpq10)));
		UCRPQ ucrpq = new UCRPQ(new ArrayList<CRPQ>(Arrays.asList(crpq)));

		System.out.println(ucrpq);
		System.out.println(ucrpq.toCypher());
		System.out.println();


		RPQ rpq11 = new RPQ("x",
				new Union(new Concatenation(new Union(new Concatenation(new Atom("a"),new Atom("b")),new Concatenation(new Atom("c"),new Atom("d"))),new Atom("e")),new Atom("f")),
				"y");

		CRPQ crpq0 = new CRPQ(new ArrayList<RPQ>(Arrays.asList(rpq11)));
		UCRPQ ucrpq0 = new UCRPQ(new ArrayList<CRPQ>(Arrays.asList(crpq0)));

		System.out.println(ucrpq0);
		System.out.println(ucrpq0.toCypher());
		System.out.println();



		RPQ rpq12 = new RPQ("x",
				new Concatenation(new Union(new Concatenation(new Atom("a"),new Atom("b")),new Atom("c")),
						new Union(new Concatenation(new Atom("d"),new Atom("e")),new Atom("f"))),
				"y");

		CRPQ crpq3 = new CRPQ(new ArrayList<RPQ>(Arrays.asList(rpq12)));
		UCRPQ ucrpq3 = new UCRPQ(new ArrayList<CRPQ>(Arrays.asList(crpq3)));

		System.out.println(ucrpq3);
		System.out.println(ucrpq3.toCypher());
		System.out.println();


		CRPQ crpq4 = new CRPQ(new ArrayList<RPQ>(Arrays.asList(rpq1,rpq7,rpq4,rpq6)));
		UCRPQ ucrpq4 = new UCRPQ(new ArrayList<CRPQ>(Arrays.asList(crpq4)));

		System.out.println(ucrpq4);
		System.out.println(ucrpq4.toCypher());
		System.out.println();

    System.out.println(Parser.parseRPQ("x,((a.b+c).d+e).f,y").getCypherable());
		System.out.println(Parser.parseRPQ("x,(a.b+a.b).(a.b+a.b),y").getCypherable());
		System.out.println(Parser.parseRPQ("x,a+b,y").getCypherable());
    System.out.println(Parser.parseUCRPQ("(x,(a+b),y)&x,(c-),y|x,(a+b),y"));

	}
}
