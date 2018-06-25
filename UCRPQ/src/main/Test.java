package main;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Test {

	public static void main(String[] args) {
		
		System.out.println("####  RPQ  ####\n");


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
			System.out.println(rpq.getCypherExpression());
		}

		System.out.println("\n####  CRPQ  ####\n");
		
		List<String> crpq_string 
			= Arrays.asList("(x,(a.b.c),y)&(x,a*,y)",
					"(x,((a.b)+(c.d)),y)&(x,a*,y)",
					"(x,((a.b+c).d+e).(f.g+h*),y)&(x,((a.b)+(c.d)),y)&(x,a*,y)");
		List<CRPQ> crpqs = crpq_string.stream().map(Parser::parseCRPQ).collect(Collectors.toList());
		for (CRPQ cr : crpqs) {
			System.out.println("\n"+cr.toString());
			System.out.println(cr.toCypher());
		}
		

		System.out.println("\n####  UCRPQ  ####\n");

		
		List<String> ucrpq_string 
			= Arrays.asList("(x,(a.b.c),y)&(x,a*,y)|(x,(a*.(c+(b.d))),y)",
					"(x,(a+b),y)&x,(c-),y|x,(a+b),y",
					"(x,a,y)|(x,(a.c)*,y)");
		
		List<UCRPQ> ucrpqs = ucrpq_string.stream().map(Parser::parseUCRPQ).collect(Collectors.toList());
		for (UCRPQ ucr : ucrpqs) {
			System.out.println("\n"+ucr.toString());
			System.out.println(ucr.toCypher());
		}
	
		
	}
}
