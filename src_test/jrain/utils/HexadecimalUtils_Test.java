/*******************************************************************************
 * Copyright (C) 2026 poltergeist0
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Any libraries this program depends on have their own Licenses.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * LICENSE file for more details.
 ******************************************************************************/
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
	private final byte[] bn0={(byte) 1,0,1,0,0,0,0,0};
	private final byte[] bn1={(byte) 1,0,1,0,0,0,0,0,0,0,0,1,0,0,1,1,0,1,1,1,1,1,1,1};
	private final byte[] bn2={(byte) 1,0,1,0};
	
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

	@Test
	public void testConvertByteToBinaryDeclared() {
		byte[] bb=new byte[8];
//		for (int i = 0; i < bb.length; i++) {bb[i]=0;}
		HexadecimalUtils.convertByteToBinary(b0[0],bb,0,7);
		assertArrayEquals(bn0, bb);
		byte[] bb1=new byte[4];
//		for (int i = 0; i < bb.length; i++) {bb[i]=0;}//reset
		HexadecimalUtils.convertByteToBinary(b1[2],bb1,0,3);
		assertArrayEquals(bn2, bb1);
	}

	@Test
	public void testConvertByteToBinary() {
		assertArrayEquals(bn0, HexadecimalUtils.convertByteToBinary(b0[0]));
	}

	@Test
	public void testConvertBinaryToByte() {
		assertEquals(b0[0],(byte) HexadecimalUtils.convertBinaryToByte(bn0));
	}

	@Test
	public void testConvertByteArrayToBinary() {
		assertArrayEquals(bn1, HexadecimalUtils.convertByteToBinary(b0));
	}

	@Test
	public void testConvertBinaryToByteArray() {
		assertArrayEquals(b0, HexadecimalUtils.convertBinaryToByteArray(bn1));
		byte[] bb=new byte[1];
		bb[0]=hb0[0];
		assertArrayEquals(bb, HexadecimalUtils.convertBinaryToByteArray(bn2));
	}

}
