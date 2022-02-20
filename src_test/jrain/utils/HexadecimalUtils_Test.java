package jrain.utils;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import jrain.exceptions.ExceptionInvalidValue;

public class HexadecimalUtils_Test {

	private final String s0="a0137f";
	private final byte[] b0={(byte) 160,19,127};
	private final byte[] b1={0,9,10,15};
	private final byte[] b2={0,9,10,15,10,15};
	private final char[] c0={'0','9','a','f'};
	private final char[] c1={'0','9','a','f','A','F'};
	private final byte[] hb0={10,0,1,3,7,15};
	private final char[] c2={'0','9','a','t'};
	
//	@Rule
//	public ExpectedException exception = ExpectedException.none();
	
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
	public void testConvertToHexHalfByte() {
		assertArrayEquals(hb0, HexadecimalUtils.convertToHexHalfByte(b0));
	}

	@Test
	public void testConvertFromHexHalfByte() {
		assertArrayEquals(b0, HexadecimalUtils.convertFromHexHalfByte(HexadecimalUtils.convertToHexHalfByte(b0)));
	}

	@Test
	public void testConvertHalfByteToCharByte(){
		for (int i = 0; i < c0.length; i++) {
			assertEquals(c1[i], HexadecimalUtils.convertHalfByteToChar(b2[i]));
		}
	}

	@Test
	public void testConvertCharToHalfByteChar() throws ExceptionInvalidValue {
		//test with ok parameters
		try {
			for (int i = 0; i < c0.length; i++) {
				assertEquals(b1[i], HexadecimalUtils.convertCharToHalfByte(HexadecimalUtils.convertHalfByteToChar(b1[i])));
			}
		} catch (ExceptionInvalidValue e) {
			e.printStackTrace();
			fail("Failed with ok parameters");
		}
		//test exception
//		exception.expect(ExceptionInvalidValue.class);
//		HexadecimalUtils.convertCharToHalfByte(c2[c2.length-1]);
		Assert.assertThrows("", ExceptionInvalidValue.class, ()-> HexadecimalUtils.convertCharToHalfByte(c2[c2.length-1]));
	}

	@Test
	public void testConvertHalfByteToCharByteArray() {
		assertArrayEquals(c0, HexadecimalUtils.convertHalfByteToChar(b1));
	}

	@Test
	public void testConvertCharToHalfByteCharArray() throws ExceptionInvalidValue {
		try {
			assertArrayEquals(b1, HexadecimalUtils.convertCharToHalfByte(HexadecimalUtils.convertHalfByteToChar(b1)));
		} catch (ExceptionInvalidValue e) {
			e.printStackTrace();
			fail("Failed with ok parameters");
		}
		//test exception
//		exception.expect(ExceptionInvalidValue.class);
//		HexadecimalUtils.convertCharToHalfByte(c2);
		Assert.assertThrows("", ExceptionInvalidValue.class, ()-> HexadecimalUtils.convertCharToHalfByte(c2));
	}

	@Test
	public void testConvertCharToHalfByteNoError() {
		assertArrayEquals(b1, HexadecimalUtils.convertCharToHalfByteNoError(HexadecimalUtils.convertHalfByteToChar(b1)));
	}

	@Test
	public void testConvertToHex() {
		assertEquals(s0, HexadecimalUtils.convertToHex(b0));
	}

	@Test
	public void testConvertFromHex() throws ExceptionInvalidValue {
		//test with ok parameters
		try {
			assertArrayEquals(b0, HexadecimalUtils.convertFromHex(HexadecimalUtils.convertToHex(b0)));
		} catch (ExceptionInvalidValue e) {
			fail("Failed with in condition parameters");
		}
		//test for exception with not ok parameters
//		exception.expect(ExceptionInvalidValue.class);
//		HexadecimalUtils.convertFromHex("s0");
		Assert.assertThrows("", ExceptionInvalidValue.class, ()-> HexadecimalUtils.convertFromHex("s0"));
	}

	@Test
	public void testConvertFromHexNoError() {
		assertArrayEquals(b0, HexadecimalUtils.convertFromHexNoError(HexadecimalUtils.convertToHex(b0)));
	}

}
