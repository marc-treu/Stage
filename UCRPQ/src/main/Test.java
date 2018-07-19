package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
	                       "x,c+(a.b),y",
	                       "x,a+b,y",
	                       "x,((a.b+c).d+e).f,y",
	                       "x,(a.b+a.b).(a.b+a.b),y",
	                       "x,((a.b+c).d+e).(f.g+h*),y",
	                       "x,(a+f)*.v.s.((b+f))*,y",
	                       "x,a.b.a,y",
	                       "x,a.b.a.a.a,y"
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
					"(x,a+b,y)&(y,(a+b)*,z)&(z,(a.b.d),x)",
					"(x,a+b,y)&(x,(a.b)*,y)",
					"(x,((a.b+c).d+e).(f.g+h*),y)&(x,((a.b)+(c.d)),y)&(x,a*,y)");
		List<CRPQ> crpqs = crpq_string.stream().map(Parser::parseCRPQ).collect(Collectors.toList());
		for (CRPQ cr : crpqs) {
			System.out.println("\n"+cr.toString());
			System.out.println(cr.getCypherExpression());
		}


		System.out.println("\n####  UCRPQ  ####\n");


		List<String> ucrpq_string
			= Arrays.asList("(x,(a.b.c),y)&(x,a*,y)|(x,(a*.(c+(b.d))),y)",
					"(x,(a+b),y)&y,(c-),z|w,(a+b),q",
					"(x,a,y)|(x,(a.c)*,y)",
					"x,((a+d)*.c.(b+d)*),y",
					"(x,((a+d)*.c.(b+d)*.(c+d)*),y)&(x,((a+d)*.c.(b+d)*.(c+d)*),y)",
					"(x,a.c.b.c*.b*.b.c,y)");

		List<UCRPQ> ucrpqs = ucrpq_string.stream().map(Parser::parseUCRPQ).collect(Collectors.toList());
		for (UCRPQ ucr : ucrpqs) {
			System.out.println("\n"+ucr.toString());
			System.out.println(ucr.getCypherExpression());
		}
		
		System.out.println(Automate.automateFromRegExp(Parser.parseRegExp("(a)")));	

		System.out.println(Automate.automateFromRegExp(Parser.parseRegExp("(a*)")));	
		
		System.out.println(Automate.automateFromRegExp(Parser.parseRegExp("(a.(b+d).e*.c)")));		
		System.out.println(Automate.automateFromRegExp(Parser.parseRegExp("(a+b+(c.f)+d)")));		
		System.out.println(Automate.automateFromRegExp(Parser.parseRegExp("(a*)")));		
		System.out.println(Automate.automateFromRegExp(Parser.parseRegExp("((a+b+(c.f)+d)*)")));		
		System.out.println(Automate.automateFromRegExp(Parser.parseRegExp("(a.c)+(a.d)")));		

		System.out.println(Automate.automateFromRegExp(Parser.parseRegExp("((a+b)*)+(((a.c)*).(d+e))")));
		System.out.println(Automate.automateFromRegExp(Parser.parseRegExp("((((a+b)).((((a+b+(c.(x+y+((a.b)*)))+d)*)+e)))*)")));
		
		System.out.println(Automate.automateFromRegExp(Parser.parseRegExp("a.(((a.c)+(d.r)))")));
		System.out.println(Automate.automateFromRegExp(Parser.parseRegExp("((x+y).z)")));

		System.out.println(Automate.automateFromRegExp(Parser.parseRegExp("((((a+b+(c.(x+y+((a.b)*)))+d)*)+e))")));

		
		
		
		HashMap<Integer,List<Transition>> hm = new HashMap<Integer,List<Transition>>();
		List<Transition> s = new ArrayList<>();
		Transition t1 = new Transition(1,"x");
		Transition t2 = new Transition(1,"y");
		s.add(t1);
		s.add(t2);
		hm.put(0, s);
		int[] et = {3};
		
		System.out.println(new Automate(
				hm
				,et
				,Parser.parseRegExp("((x+y).z)")));
		
		
		
		
	}
}
