package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import main.*;

public class TestRegExp {
	
	
	// TESTE UNIT POUR getLength()
	
	@Test
	void testAtomgetLength() {
		assertEquals(1, Parser.parseRegExp("a").getLength());
	}
	
	@Test
	void testUniongetLength() {
		assertEquals(2, Parser.parseRegExp("a+b").getLength());
	}	
	
	@Test
	void testConcatenatiogetLength() {
		assertEquals(3, Parser.parseRegExp("a.b.c").getLength());
	}
	
	@Test
	void testConcatenatioOverUniongetLength() {
		assertEquals(6, Parser.parseRegExp("(a+c).(v+r+s).c").getLength());
	}
	
	@Test
	void testUnionOverConcatenatiogetLength() {
		assertEquals(9, Parser.parseRegExp("(q.c.v.d)+(a.d)+(a.b.c)").getLength());
	}
	
	@Test
	void testStargetLength() {
		assertEquals(1, Parser.parseRegExp("(a*)").getLength());
	}
	
	@Test
	void testStarOverConcatenatiogetLength() {
		assertEquals(4, Parser.parseRegExp("((q.c.v.d)*)").getLength());
	}
	
	@Test
	void testStarOverUniongetLength() {
		assertEquals(4, Parser.parseRegExp("((q+c+v+d)*)").getLength());
	}
	
	@Test
	void testManyRegExpgetLength() {
		assertEquals(5, Parser.parseRegExp("((q.c.v.(a+v))*)").getLength());
		assertEquals(5, Parser.parseRegExp("((q.c.v.d)*)+a").getLength());
		assertEquals(15, Parser.parseRegExp("(q.((u+i)*).x). ( (a.(s+w+x+(q.u.e))) + ( ((a.v.(e+s))*) ))").getLength());
		assertEquals(5, Parser.parseRegExp("(a*)+((b+c).(d+e))").getLength());
	}
	
	
	
	// TESTE UNIT POUR getRename
	
	@Test
	void testAtomgetRename() {
		assertEquals("a1", Parser.parseRegExp("a").getRename(1).toString());
	}
	
	@Test
	void testUniongetRename() {
		assertEquals("(a1+b2)", Parser.parseRegExp("a+b").getRename(1).toString());
	}	
	
	@Test
	void testConcatenatiogetRename() {
		assertEquals("(a1.b2.c3)", Parser.parseRegExp("a.b.c").getRename(1).toString());
	}
	
	@Test
	void testConcatenatioOverUniongetRename() {
		assertEquals("((a1+c2).(v3+r4+s5).c6)", Parser.parseRegExp("(a+c).(v+r+s).c").getRename(1).toString());
	}
	
	@Test
	void testUnionOverConcatenatiogetRename() {
		assertEquals("((q1.c2.v3.d4)+(a5.d6)+(a7.b8.c9))", Parser.parseRegExp("(q.c.v.d)+(a.d)+(a.b.c)").getRename(1).toString());
	}
	
	@Test
	void testStargetRename() {
		assertEquals("(a1)*", Parser.parseRegExp("(a*)").getRename(1).toString());
	}
	
	@Test
	void testStarOverConcatenatiogetRename() {
		assertEquals("(q1.c2.v3.d4)*", Parser.parseRegExp("((q.c.v.d)*)").getRename(1).toString());
	}
	
	@Test
	void testStarOverUniongetRename() {
		assertEquals("(q1+c2+v3+d4)*", Parser.parseRegExp("((q+c+v+d)*)").getRename(1).toString());
	}
	
	@Test
	void testManyRegExpgetRename() {

		assertEquals("(q1.c2.v3.(a4+v5))*"
				, Parser.parseRegExp("((q.c.v.(a+v))*)").getRename(1).toString());
		
		assertEquals("((q1.c2.v3.d4)*+a5)"
				, Parser.parseRegExp("((q.c.v.d)*)+a").getRename(1).toString());
		
		assertEquals("((q1.(u2+i3)*.x4).((a5.(s6+w7+x8+(q9.u10.e11)))+(a12.v13.(e14+s15))*))"
				, Parser.parseRegExp("(q.((u+i)*).x). ( (a.(s+w+x+(q.u.e))) + ( ((a.v.(e+s))*) ))").getRename(1).toString());
		
		assertEquals("((a1)*+((b2+c3).(d4+e5)))"
				, Parser.parseRegExp("(a*)+((b+c).(d+e))").getRename(1).toString());
	}
	

	// TESTE UNIT POUR getInitaux()
	
	@Test
	void testAtomgetInitaux() {
		assertEquals(Arrays.asList("a"), Parser.parseRegExp("a").getInitaux());
	}
	
	@Test
	void testUniongetInitaux() {
		assertEquals(Arrays.asList("a","b"), Parser.parseRegExp("a+b").getInitaux());
	}	
	
	@Test
	void testConcatenatiogetInitaux() {
		assertEquals(Arrays.asList("a"), Parser.parseRegExp("a.b.c").getInitaux());
	}
	
	@Test
	void testConcatenatioOverUniongetInitaux() {
		assertEquals(Arrays.asList("a","c"), Parser.parseRegExp("(a+c).(v+r+s).c").getInitaux());
	}
	
	@Test
	void testUnionOverConcatenatiogetInitaux() {
		assertEquals(Arrays.asList("q","a","a"), Parser.parseRegExp("(q.c.v.d)+(a.d)+(a.b.c)").getInitaux());
	}
	
	@Test
	void testStargetInitaux() {
		assertEquals(Arrays.asList("a"), Parser.parseRegExp("(a*)").getInitaux());
	}
	
	@Test
	void testStarOverConcatenatiogetInitaux() {
		assertEquals(Arrays.asList("q"), Parser.parseRegExp("((q.c.v.d)*)").getInitaux());
	}
	
	@Test
	void testStarOverUniongetInitaux() {
		assertEquals(Arrays.asList("q","c","v","d"), Parser.parseRegExp("((q+c+v+d)*)").getInitaux());
	}
	
	@Test
	void testManyRegExpgetInitaux() {
		assertEquals(Arrays.asList("q"), Parser.parseRegExp("((q.c.v.(a+v))*)").getInitaux());
		assertEquals(Arrays.asList("q","a"), Parser.parseRegExp("((q.c.v.d)*)+a").getInitaux());
		assertEquals(Arrays.asList("q"), Parser.parseRegExp("(q.((u+i)*).x). ( (a.(s+w+x+(q.u.e))) + ( ((a.v.(e+s))*) ))").getInitaux());
		assertEquals(Arrays.asList("a","b","c"), Parser.parseRegExp("(a*)+((b+c).(d+e))").getInitaux());
		assertEquals(Arrays.asList("a","b","c"), Parser.parseRegExp("(a*).(b*).(c*)").getInitaux());
		assertEquals(Arrays.asList("a","b","a","d","e"), Parser.parseRegExp("((a+b)*)+(((a.c)*).(d+e))").getInitaux());
	}
	
	
	// TESTE UNIT POUR getSuivant()

	@Test
	void testAtomgetSuivant() {
			List<String> array = new ArrayList<>();
			Parser.parseRegExp("a").getSuivant(array,"a");
			assertEquals(new ArrayList<>(), array);
		}
	
	@Test
	void testStarOverAtomgetSuivant() {
		List<String> array = new ArrayList<>();
		Parser.parseRegExp("(a*)").getSuivant(array,"a");
		assertEquals(Arrays.asList("a"), array);
	}
	
	@Test
	void testConcatenationOverAtomgetSuivant() {
		List<String> array = new ArrayList<>();
		Parser.parseRegExp("(a.b.c)").getSuivant(array,"a");
		assertEquals(Arrays.asList("b"), array);

		array.clear();
		Parser.parseRegExp("(a.b.c)").getSuivant(array,"b");
		assertEquals(Arrays.asList("c"), array);
		
		array.clear();
		Parser.parseRegExp("(a.b.c)").getSuivant(array,"c");
		assertEquals(new ArrayList<>(), array);
	}
	
	@Test
	void testStarOverConcatenationOverAtomgetSuivant() {
		List<String> array = new ArrayList<>();
		Parser.parseRegExp("((a.b.c)*)").getSuivant(array,"a");
		assertEquals(Arrays.asList("b"), array);

		array.clear();
		Parser.parseRegExp("((a.b.c)*)").getSuivant(array,"b");
		assertEquals(Arrays.asList("c"), array);
		
		array.clear();
		Parser.parseRegExp("((a.b.c)*)").getSuivant(array,"c");
		assertEquals(Arrays.asList("a"), array);
	}

	
	
	@Test
	void testConcatenationOverStarAndAtomgetSuivant() {
		List<String> array = new ArrayList<>();
		Parser.parseRegExp("(a.(b*).(c*).d )").getSuivant(array,"a");
		assertEquals(Arrays.asList("b","c","d"), array);

		array.clear();
		Parser.parseRegExp("(a.(b*).(c*).d )").getSuivant(array,"b");
		assertEquals(Arrays.asList("b","c","d"), array);
		
		array.clear();
		Parser.parseRegExp("(a.(b*).(c*).d )").getSuivant(array,"c");
		assertEquals(Arrays.asList("c","d"), array);
		
		array.clear();
		Parser.parseRegExp("(a.(b*).(c*).d )").getSuivant(array,"d");
		assertEquals(new ArrayList<>(), array);
	}
	
	@Test
	void testRegExp1getSuivant() {
		List<String> array = new ArrayList<>();
		RegExp r = Parser.parseRegExp("((((a+b)).((((c+d+(e.(f+g+((h.i)*)))+j)*)+k)))*)");
		r.getSuivant(array,"a");
		assertEquals(Arrays.asList("c","d","e","j","k"), array);

		array.clear();
		r.getSuivant(array,"b");
		assertEquals(Arrays.asList("c","d","e","j","k"), array);

		array.clear();
		r.getSuivant(array,"c");
		assertEquals(Arrays.asList("c","d","e","j","a","b"), array);
		
		array.clear();
		r.getSuivant(array,"d");
		assertEquals(Arrays.asList("c","d","e","j","a","b"), array);

		array.clear();
		r.getSuivant(array,"e");
		assertEquals(Arrays.asList("f","g","h"), array);

		array.clear();
		r.getSuivant(array,"f");
		assertEquals(Arrays.asList("c","d","e","j","a","b"), array);

		array.clear();
		r.getSuivant(array,"g");
		assertEquals(Arrays.asList("c","d","e","j","a","b"), array);

		array.clear();
		r.getSuivant(array,"h");
		assertEquals(Arrays.asList("i"), array);

		array.clear();
		r.getSuivant(array,"i");
		assertEquals(Arrays.asList("h","c","d","e","j","a","b"), array);

		array.clear();
		r.getSuivant(array,"j");
		assertEquals(Arrays.asList("c","d","e","j","a","b"), array);

		array.clear();
		r.getSuivant(array,"k");
		assertEquals(Arrays.asList("a","b"), array);		
	}
	
	
}
