package test;

import static org.junit.Assert.*;

import org.junit.jupiter.api.Test;

import main.*;

public class TestRegExp {
	
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
	
	
	
	
}
