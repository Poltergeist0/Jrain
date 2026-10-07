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
package jrain.array.utils;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;

public class ByteArrayUtils_Test {

	private final byte[] b0={0,1,1,0,0,0,0,1};
	private final byte[] b1={0,1,0,1,1,0,0,1};
	private final byte[] b2={0,1,1,0,0,0,0,1,0,1,0,1,1,0,0,1};
	private final long[] l0={-1,0,9,17,25,33,41,49};//keep in mind that each element of b0 is a byte not a bit
	private final long[] l1={-1,0,2,3,4,5,6,7};
	
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
	public void testEqualsByteArrayIntIntByteArrayIntInt() {
		assertTrue(ByteArrayUtils.equals(b0, 0, 2, b1, 2, 4));
	}

	@Test
	public void testEqualsNull() {
		assertTrue(ByteArrayUtils.equalsNull(null, 0, 2, null, 0, 2));
		assertFalse(ByteArrayUtils.equalsNull(null, 0, 2, b1, 0, 2));
	}

	@Test
	public void testReplace() {
		//the replace method modifies the original so a copy is required for testing
		assertArrayEquals(b1, ByteArrayUtils.replace(Arrays.copyOf(b0, b0.length), 2, b0, 0, 3));
	}

	@Test
	public void testConcatenate() {
		assertArrayEquals(b2, ByteArrayUtils.concatenate(b0,0,b0.length-1, b1,0,b1.length-1));
	}

	@Test
	public void testLog2() {
		assertEquals(-1, ByteArrayUtils.log2(b0, 3, 3));
		assertEquals(0, ByteArrayUtils.log2(b0, b0.length-1, b0.length-1));
		for (int i = 0; i < l0.length; i++) {
			assertEquals(l0[i], ByteArrayUtils.log2(b0, 0, i));
		}
	}

	@Test
	public void testLog2b() {
		assertEquals(-1, ByteArrayUtils.log2b(b0, 3, 3));
		assertEquals(0, ByteArrayUtils.log2b(b0, b0.length-1, b0.length-1));
		for (int i = 0; i < l1.length; i++) {
			assertEquals(l1[i], ByteArrayUtils.log2b(b0, 0, i));
		}
	}

}
