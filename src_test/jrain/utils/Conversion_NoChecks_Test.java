package jrain.utils;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class Conversion_NoChecks_Test {

	private final String s0="a";
	private final String s1="01100001";
	private final byte[] b0=new byte[] {0,1,1,0,0,0,0,1};
	private final int[] i0=new int[] {0,1,1,0,0,0,0,1};
	private final char[] c0={'0','1','1','0','0','0','0','1'};
	private final long l0=16778243;
	private final byte[] b1={1,0,4,3};
	private final byte[] b2={0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,1};
	private final String[] s2={"0","1","1","0","0","0","0","1"};
	private final int[] i1={48,49,49,48,48,48,48,49};
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testByteArrayToIntArray() {
		assertArrayEquals(i0, Conversion_NoChecks.byteArrayToIntArray(b0));
	}

	@Test
	public void testIntArrayToByteArray() {
		assertArrayEquals(b0, Conversion_NoChecks.intArrayToByteArray(Conversion_NoChecks.byteArrayToIntArray(b0)));
	}

	@Test
	public void testStringToCharArray() {
		assertArrayEquals(c0, Conversion_NoChecks.stringToCharArray(s1));
		//check if the reverse returns the original
//		String s=new String(c0);
//		if(!s.equals(s1)) fail("The char array does not convert back to the original data");
		assertEquals("The char array does not convert back to the original data",s1, new String(c0));
	}

	@Test
	public void testStringToIntArray() {
		assertArrayEquals(i1, Conversion_NoChecks.stringToIntArray(s1));
	}

	@Test
	public void testIntArrayToString() {
		assertEquals(s1, Conversion_NoChecks.intArrayToString(Conversion_NoChecks.stringToIntArray(s1)));
	}

	@Test
	public void testIntStringArrayToIntArray() {
		assertArrayEquals(i0, Conversion_NoChecks.intStringArrayToIntArray(s2));
	}

	@Test
	public void testStringArrayToString() {
		assertEquals(s1, Conversion_NoChecks.stringArrayToString(s2));
	}

	@Test
	public void testStringToStringArray() {
		String ss0="aksdyfg.cduvfjhcbnamsois.h.fvi shbdciwesu.gdcsdhbc";
		String sep=".";
		String[] ss1={"aksdyfg","cduvfjhcbnamsois","h","fvi shbdciwesu","gdcsdhbc"};
		String ss2="";
		String[] ss3={};
		assertArrayEquals(ss1, Conversion_NoChecks.stringToStringArray(ss0, sep, 0, ss0.length()));
		assertArrayEquals(ss3, Conversion_NoChecks.stringToStringArray(ss2, sep, 0, ss2.length()));
	}

	@Test
	public void testLongToByteArray() {
		assertArrayEquals(b1, Conversion_NoChecks.longToByteArray(l0, 4));
	}

	@Test
	public void testByteArrayToLong() {
		assertEquals(l0, Conversion_NoChecks.byteArrayToLong(Conversion_NoChecks.longToByteArray(l0, 4),0,4));
	}

	@Test
	public void testByteToBit() {
		assertArrayEquals(b0, Conversion_NoChecks.byteToBit(s0.getBytes()[0]));
	}

	@Test
	public void testByteArrayToBitArray() {
		assertArrayEquals(b2, Conversion_NoChecks.byteArrayToBitArray(b1));
	}

	@Test
	public void testByteArrayToStringPrintable() {
		assertEquals(s1, Conversion_NoChecks.byteArrayToStringPrintable(b0, ""));
	}

	@Test
	public void testIntArrayToStringPrintable() {
		assertEquals(s1, Conversion_NoChecks.intArrayToStringPrintable(i0, ""));
	}
	
}
