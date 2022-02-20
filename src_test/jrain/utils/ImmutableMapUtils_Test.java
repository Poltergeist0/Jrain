package jrain.utils;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class ImmutableMapUtils_Test {

	String s0="0";
	String s1="1";
	String s2="2";
	String s3="3";
	String s4="4";
	String s5="5";
	ImmutableMap<String,String> i0=ImmutableMap.of(s0, s0, s1, s1);
	ImmutableMap<String,String> i1=ImmutableMap.of(s0, s0, s1, s1,s2,s2);
	ImmutableMap<String,String> i2=ImmutableMap.of(s2, s2);
	ImmutableList<String> il=ImmutableList.of(s0, s1);
	ImmutableMap<String,String> i3=ImmutableMap.of(s0, s1, s1, s0,s2,s2);
//	ImmutableMap<String,String> i4=ImmutableMap.of(s2,s2);
	
//	ArrayList<String> a0=new ArrayList<>(i01);
	
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
	public void testAddImmutableMapOfEFEF() {
		assertEquals(i1, ImmutableMapUtils.add(i0, s2, s2));
	}

	@Test
	public void testAddImmutableMapOfEFImmutableMapOfEF() {
		assertEquals(i1, ImmutableMapUtils.add(i0, i2));
	}

	@Test
	public void testRemoveImmutableMapOfEFE() {
		assertEquals(i0, ImmutableMapUtils.remove(i1, s2));
	}

	@Test
	public void testRemoveImmutableMapOfEFImmutableListOfE() {
		assertEquals(i2, ImmutableMapUtils.remove(i1, il));
	}

	@Test
	public void testReplaceImmutableMapOfEFImmutableMapOfEFImmutableMapOfEF() {
		assertEquals(i1, ImmutableMapUtils.replace(i3, il, i0));
	}

	@Test
	public void testReplaceImmutableMapOfEFEEF() {
		assertEquals(i1, ImmutableMapUtils.replace(ImmutableMapUtils.replace(i3, s1, s1,s1), s0, s0,s0));
	}

	@Test
	public void testSubmap() {
		assertEquals(i0, ImmutableMapUtils.submap(i1, il));
	}

	@Test
	public void testIsSubMap() {
		assertTrue(ImmutableMapUtils.isSubMap(i1, i0));
	}

	@Test
	public void testIntersect() {
		assertEquals(i0, ImmutableMapUtils.intersect(i1, i0));
		assertEquals(i0, ImmutableMapUtils.intersect(i0, i1));
		assertEquals(i2, ImmutableMapUtils.intersect(i1, i2));
	}

	@Test
	public void testCompare() {
		assertEquals(0, ImmutableMapUtils.compare(i0, i0));
		assertEquals(-1, ImmutableMapUtils.compare(i1, i0));
		assertEquals(-2, ImmutableMapUtils.compare(i0, i1));
		assertEquals(1, ImmutableMapUtils.compare(i1, i3));
	}

	@Test
	public void testMatches() {
		assertTrue(ImmutableMapUtils.matches(i0, i0));
	}

}
