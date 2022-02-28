package jrain.immutableList.utils;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

public class ImmutableListUtils_Test {

	String s0="0";
	String s1="1";
	String s2="2";
	String s3="3";
	String s4="4";
	String s5="5";
	ImmutableList<String> i0=ImmutableList.of(s0, s1);
	ImmutableList<String> i00=ImmutableList.of(s2,s0, s1);
	ImmutableList<String> i09=ImmutableList.of(s0, s1,s2);
	ImmutableList<String> i01=ImmutableList.of(s0, s2, s1);
	ImmutableList<String> i1=ImmutableList.of(s2);
	ArrayList<String> a0=new ArrayList<>(i01);
	
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
	public void testAddImmutableListOfEE() {
		assertEquals(i09, ImmutableListUtils.add(i0, s2));
	}

	@Test
	public void testAddImmutableListOfEEInt() {
		assertEquals(i09, ImmutableListUtils.add(i0, s2,9));
		assertEquals(i00, ImmutableListUtils.add(i0, s2,0));
		assertEquals(i01, ImmutableListUtils.add(i0, s2,1));
	}

	@Test
	public void testConcatenateImmutableListOfEImmutableListOfE() {
		assertEquals(i09, ImmutableListUtils.concatenate(i0,0,i0.size(), i1,0,i1.size()));
	}

	@Test
	public void testRemoveImmutableListOfEE() {
		assertEquals(i0, ImmutableListUtils.remove(i01, s2));
	}

	@Test
	public void testRemoveImmutableListOfEImmutableListOfE() {
		assertEquals(i1, ImmutableListUtils.remove(i01, i0));
	}

	@Test
	public void testToArrayList() {
		assertEquals(a0, ImmutableListUtils.toArrayList(i01));
	}

	@Test
	public void testToImmutableListArrayListOfE() {
		assertEquals(i01, ImmutableListUtils.toImmutableList(a0));
	}

	@Test
	public void testToImmutableListEArray() {
		assertEquals(i01, ImmutableListUtils.toImmutableList(a0.toArray()));
	}

}
