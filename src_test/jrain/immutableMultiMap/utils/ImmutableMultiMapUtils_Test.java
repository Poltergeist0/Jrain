package jrain.immutableMultiMap.utils;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;

public class ImmutableMultiMapUtils_Test {

	String s0="0";
	String s1="1";
	String s2="2";
	String s3="3";
	String s4="4";
	String s5="5";
	ImmutableMultimap<String,String> i0=ImmutableMultimap.of(s0, s2, s0, s1, s1, s0,s2,s1);
	ImmutableMultimap<String,String> i1=ImmutableMultimap.of(s0, s2, s0, s1, s1, s0,s2,s1,s2,s2);
	ImmutableMultimap<String,String> i2=ImmutableMultimap.of(s2, s2);
	ImmutableList<String> il1=ImmutableList.of(s1, s0);
	ImmutableList<String> il2=ImmutableList.of(s1, s2);
	ImmutableMultimap<String,String> i3=ImmutableMultimap.of(s0, s2, s0, s1, s1, s0);
	ImmutableMultimap<String,String> i4=ImmutableMultimap.of(s2,s1,s2,s2);
	ImmutableMultimap<String,String> i5=ImmutableMultimap.of(s0, s1, s1, s0);
	ImmutableMultimap<String,String> i6=ImmutableMultimap.of(s0, s1, s1, s0,s2,s1);
	ImmutableMultimap<String,String> i7=ImmutableMultimap.of(s3, s2, s3, s1, s1, s0,s2,s1,s2,s2);
	ImmutableMultimap<String,String> i8=ImmutableMultimap.of(s0, s0, s0, s1, s1, s0,s2,s1,s2,s0);
	ImmutableMultimap<String,String> i9=ImmutableMultimap.of(s0, s3, s0, s1, s1, s0,s3,s1,s3,s3);
	
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
	public void testAddValue() {
		assertEquals(i1, ImmutableMultiMapUtils.addValue(i0, s2, s2));
	}

	@Test
	public void testAddValues() {
		assertEquals(i1, ImmutableMultiMapUtils.addValues(i3, s2, il2));
	}

	@Test
	public void testAddMultimap() {
		assertEquals(i1, ImmutableMultiMapUtils.addMultimap(i3, i4));
	}

	@Test
	public void testRemoveImmutableMultimapOfEEE() {
		assertEquals(i5, ImmutableMultiMapUtils.remove(i1, s2));
	}

	@Test
	public void testRemoveImmutableMultimapOfEEImmutableListOfE() {
		assertEquals(i2, ImmutableMultiMapUtils.remove(i1, il1));
	}

	@Test
	public void testRemoveKey() {
		assertEquals(i3, ImmutableMultiMapUtils.removeKey(i1, s2));
	}

	@Test
	public void testRemoveValueImmutableMultimapOfEFEF() {
		assertEquals(i0, ImmutableMultiMapUtils.removeValue(i1, s2, s2));
	}

	@Test
	public void testRemoveValueImmutableMultimapOfEFF() {
		assertEquals(i6, ImmutableMultiMapUtils.removeValue(i0, s2));
	}

	@Test
	public void testRemoveKeys() {
		assertEquals(i4, ImmutableMultiMapUtils.removeKeys(i1, il1));
	}

	@Test
	public void testSubmapImmutableMultimapOfEFE() {
		assertEquals(i4, ImmutableMultiMapUtils.submap(i1, s2));
	}

	@Test
	public void testSubmapImmutableMultimapOfEFImmutableListOfE() {
		assertEquals(i3, ImmutableMultiMapUtils.submap(i1, il1));
	}

	@Test
	public void testReplaceKey() {
		assertEquals(i7, ImmutableMultiMapUtils.replaceKey(i1, s0, s3));
	}

	@Test
	public void testReplaceData() {
		assertEquals(i8, ImmutableMultiMapUtils.replaceData(i1, s2, s0));
	}

	@Test
	public void testReplaceAll() {
		assertEquals(i9, ImmutableMultiMapUtils.replaceAll(i1, s2, s3));
	}

	@Test
	public void testIsSubMap() {
		assertTrue(ImmutableMultiMapUtils.isSubMap(i1, i0));
	}

	@Test
	public void testIntersect() {
		assertEquals(i5, ImmutableMultiMapUtils.intersect(i1, i9));
	}

	@Test
	public void testCompare() {
		assertEquals(2, ImmutableMultiMapUtils.compare(i1, i9));
		assertEquals(-1, ImmutableMultiMapUtils.compare(i1, i0));
		assertEquals(-2, ImmutableMultiMapUtils.compare(i0, i1));
		assertEquals(0, ImmutableMultiMapUtils.compare(i1, i1));
	}

	@Test
	public void testMatches() {
		assertTrue(ImmutableMultiMapUtils.matches(i1, i1));
		assertFalse(ImmutableMultiMapUtils.matches(i1, i0));
	}

}
