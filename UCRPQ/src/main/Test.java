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
		
		System.out.println(new Automate(Parser.parseRegExp("(a)")));	

		System.out.println(new Automate(Parser.parseRegExp("(a*)")));	
		
		System.out.println(new Automate(Parser.parseRegExp("(a.(b+d).e*.c)")));		
		System.out.println(new Automate(Parser.parseRegExp("(a+b+(c.f)+d)")));		
		System.out.println(new Automate(Parser.parseRegExp("(a*)")));		
		System.out.println(new Automate(Parser.parseRegExp("(a.c)+(a.d)")));		

		System.out.println(new Automate(Parser.parseRegExp("((a+b)*)+(((a.c)*).(d+e))")));
		System.out.println(new Automate(Parser.parseRegExp("((((a+b)).((((a+b+(c.(x+y+((a.b)*)))+d)*)+e)))*)")));
		
		System.out.println(new Automate(Parser.parseRegExp("a.(((a.c)+(d.r)))")));
		System.out.println(new Automate(Parser.parseRegExp("((x+y).z)")));

		new Automate(Parser.parseRegExp("((a+a)*)"));
		new Automate(Parser.parseRegExp("((a+b+(a.c)*+e)*)"));
		new Automate(Parser.parseRegExp("((((a+b+(c.(x+y+((a.b)*)))+d)*)+e))"));
 		new Automate(Parser.parseRegExp("((((a+b)).((((a+b+(c.(x+y+((a.b)*)))+d)*)+e)))*)"));

 		Automate teste =		new Automate(Parser.parseRegExp("((((a+b+(c.(x+y+((a.b)*)))+d)*)+e))"));
		new Automate(Parser.parseRegExp("((a+b+(a.c)*+e)*)"));
		new Automate(Parser.parseRegExp("((a+b)*)"));

 		System.out.println(teste);
		System.out.println(teste.brzozowski());
		
		Automate t = new Automate(Parser.parseRegExp("((a.a.b)*)"));
		System.out.println(t.brzozowski().getAutomateComplet().getMonoideTransition());
		System.out.println(t.getAutomateComplet());
		MonoideTransition m = t.brzozowski().getAutomateComplet().getMonoideTransition();
		System.out.println(m.estAperiodique());
		
	}
	
}
