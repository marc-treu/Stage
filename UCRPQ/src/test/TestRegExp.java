package test;

import static org.junit.Assert.*;

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
		System.out.println(Parser.parseRegExp("(q.((u+i)*).x). ( (a.(s+w+x+(q.u.e))) + ( ((a.v.(e+s))*) ))").getRename(1));

		assertEquals("(q1.c2.v3.(a4+v5))*"
				, Parser.parseRegExp("((q.c.v.(a+v))*)").getRename(1).toString());
		
		assertEquals("((q1.c2.v3.d4)*+a5)"
				, Parser.parseRegExp("((q.c.v.d)*)+a").getRename(1).toString());
		
		assertEquals("((q1.(u2+i3)*.x4).((a5.(s6+w7+x8+(q9.u10.e11)))+(a12.v13.(e14+s15))*))"
				, Parser.parseRegExp("(q.((u+i)*).x). ( (a.(s+w+x+(q.u.e))) + ( ((a.v.(e+s))*) ))").getRename(1).toString());
		
		assertEquals("((a1)*+((b2+c3).(d4+e5)))"
				, Parser.parseRegExp("(a*)+((b+c).(d+e))").getRename(1).toString());
	}
	
	
	
	
	
}
