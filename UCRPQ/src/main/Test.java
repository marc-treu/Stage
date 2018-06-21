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
		//System.out.println(rpq9);
		//System.out.println(rpq9.toCypher());



	    List<String> rpq_strings
	      = Arrays.asList( "x,((a+b).(a-+b-)).(a-+(a+a)+a-)*,y",
	                       "x,(a+b)+c,y",
	                       "x,(a+a),y",
	                       "x,(a+a-),y",
	                       "x,(a.b).c,y",
	                       "x,(a+b)*,y",
	                       "x,(a+b)*.a-*.b*.(a+c)*,y",
	                       "x,(a.b)+c,y",
	                       "x,a+b,y",
	                       "x,((a.b+c).d+e).f,y",
	                       "x,(a.b+a.b).(a.b+a.b),y",
	                       "x,((a.b+c).d+e).(f.g+h*),y"
	                     );
	    List<RPQ> rpqs = rpq_strings.stream().map(Parser::parseRPQ).collect(Collectors.toList());
		for (RPQ rpq : rpqs) {
			System.out.println("\n"+rpq.toString());
			System.out.println(rpq.toCypher());
		}

		
		List<String> crpq_string 
			= Arrays.asList("(x,(a.b.c),y)&(x,a*,y)",
					"(x,((a.b)+(c.d)),y)&(x,a*,y)",
					"(x,((a.b+c).d+e).(f.g+h*),y)&(x,((a.b)+(c.d)),y)&(x,a*,y)");
		List<CRPQ> crpqs = crpq_string.stream().map(Parser::parseCRPQ).collect(Collectors.toList());
		for (CRPQ cr : crpqs) {
			System.out.println("\n"+cr.toString());
			System.out.println(cr.toCypher());
		}
		
		//System.out.println(Parser.parseUCRPQ("(x,(a+b),y)&x,(c-),y|x,(a+b),y"));
		
	}
}
