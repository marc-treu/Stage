package test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;

import main.Atom;
import main.RegExp;

class AtomTest {

	@Test
	void testAtomString() {
		Atom t = new Atom("a");
		assertNotEquals(null, t);
	}

	@Test
	void testAtomStringBoolean() {
		Atom t = new Atom("a",false);
		assertNotEquals(null, t);
		Atom t1 = new Atom("a",true);
		assertNotEquals(null,t1);
	}

	@Test
	void testType() {
		Atom t1 = new Atom("a");
		Atom t2 = new Atom("b",false);
		assertEquals("t1 mauvais type", RegExp.Type.Atom, t1.type());
		assertEquals("t2 mauvais type", RegExp.Type.Atom, t2.type());
	}

	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Test
	void testChildren() {
		Atom t1 = new Atom("a");
		exception.expect(UnsupportedOperationException.class);
		t1.children();
	}

	@Test
	void testToString() {
		fail("Not yet implemented");
	}

	@Test
	void testToCypher() {
		fail("Not yet implemented");
	}

	@Test
	void testGetDirection() {
		fail("Not yet implemented");
	}

	@Test
	void testGetEtiquette() {
		fail("Not yet implemented");
	}

	@Test
	void testIsCypherable() {
		fail("Not yet implemented");
	}

	@Test
	void testFlatten() {
		fail("Not yet implemented");
	}

}
