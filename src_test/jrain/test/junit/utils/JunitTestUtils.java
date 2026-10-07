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
package jrain.test.junit.utils;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author poltergeist0
 * 
 * Implementation of missing assert methods in junit.
 * 
 */
public class JunitTestUtils {

	/**
	 * Implementation of missing assert in junit.
	 * 
	 * Asserts when the arrays are equal.
	 * 
	 * @param <T> is data type of the array
	 * @param expected is the expected array
	 * @param actual is the array being evaluated
	 */
	public static <T> void assertArrayNotEquals(T[] expected, T[] actual) {
	    try {
	        assertArrayEquals(expected, actual);
	    } catch (AssertionError e) {
	        return;
	    }
	    fail("Arrays should be different");
	}

	/**
	 * Implementation of missing assert in junit.
	 * 
	 * Asserts when the arrays are equal.
	 * 
	 * @param <T> is data type of the array
	 * @param expected is the expected array
	 * @param actual is the array being evaluated
	 */
	public static void assertArrayNotEquals(double[] expected, double[] actual) {
	    try {
	        assertArrayEquals(expected, actual);
	    } catch (AssertionError e) {
	        return;
	    }
	    fail("Arrays should be different");
	}

	/**
	 * Implementation of missing assert in junit.
	 * 
	 * Asserts when the arrays are equal.
	 * 
	 * @param <T> is data type of the array
	 * @param expected is the expected array
	 * @param actual is the array being evaluated
	 */
	public static void assertArrayNotEquals(float[] expected, float[] actual) {
	    try {
	        assertArrayEquals(expected, actual);
	    } catch (AssertionError e) {
	        return;
	    }
	    fail("Arrays should be different");
	}
	
	/**
	 * Implementation of missing assert in junit.
	 * 
	 * Asserts when the arrays are equal.
	 * 
	 * @param <T> is data type of the array
	 * @param expected is the expected array
	 * @param actual is the array being evaluated
	 */
	public static void assertArrayNotEquals(long[] expected, long[] actual) {
	    try {
	        assertArrayEquals(expected, actual);
	    } catch (AssertionError e) {
	        return;
	    }
	    fail("Arrays should be different");
	}
	
	/**
	 * Implementation of missing assert in junit.
	 * 
	 * Asserts when the arrays are equal.
	 * 
	 * @param <T> is data type of the array
	 * @param expected is the expected array
	 * @param actual is the array being evaluated
	 */
	public static void assertArrayNotEquals(int[] expected, int[] actual) {
	    try {
	        assertArrayEquals(expected, actual);
	    } catch (AssertionError e) {
	        return;
	    }
	    fail("Arrays should be different");
	}
	
	/**
	 * Implementation of missing assert in junit.
	 * 
	 * Asserts when the arrays are equal.
	 * 
	 * @param <T> is data type of the array
	 * @param expected is the expected array
	 * @param actual is the array being evaluated
	 */
	public static void assertArrayNotEquals(byte[] expected, byte[] actual) {
	    try {
	        assertArrayEquals(expected, actual);
	    } catch (AssertionError e) {
	        return;
	    }
	    fail("Arrays should be different");
	}
	
	/**
	 * Implementation of missing assert in junit.
	 * 
	 * Asserts when the arrays are equal.
	 * 
	 * @param <T> is data type of the array
	 * @param expected is the expected array
	 * @param actual is the array being evaluated
	 */
	public static void assertArrayNotEquals(char[] expected, char[] actual) {
	    try {
	        assertArrayEquals(expected, actual);
	    } catch (AssertionError e) {
	        return;
	    }
	    fail("Arrays should be different");
	}
	
	/**
	 * Implementation of missing assert in junit.
	 * 
	 * Asserts when the arrays are equal.
	 * 
	 * @param <T> is data type of the array
	 * @param expected is the expected array
	 * @param actual is the array being evaluated
	 */
	public static void assertArrayNotEquals(boolean[] expected, boolean[] actual) {
	    try {
	        assertArrayEquals(expected, actual);
	    } catch (AssertionError e) {
	        return;
	    }
	    fail("Arrays should be different");
	}
}
