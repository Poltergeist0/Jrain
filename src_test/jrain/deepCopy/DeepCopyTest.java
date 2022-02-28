package jrain.deepCopy;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import jrain.utils.Conversion_NoChecks;
import jrain.entry.mutable.Pair;
import jrain.test.junit.utils.JunitTestUtils;

class DeepCopyTest {
	
	@Nested
	@DisplayName("Test Inheritance")
	class TestInheritance{
		@DisplayName("Test class extends")
		void testClassExtends() {
			assertFalse(DeepCopy.isDeepCopyable(String.class));
			assertTrue(DeepCopy.isDeepCopyable(BadImplementation.class));
			assertTrue(DeepCopy.isDeepCopyable(GoodImplementation.class));
		}

		@ParameterizedTest
		@DisplayName("Test object not extends")
		@ValueSource(strings={"Identifiable:a76018f5-13fc-4f63-86e9-b7a4118bb314"})
		void testObjectNotExtends(String s) {
			assertFalse(DeepCopy.isDeepCopyable(s));
		}

		@ParameterizedTest
		@DisplayName("Test BadImplementation extends")
		@ValueSource(strings={"Identifiable:a76018f5-13fc-4f63-86e9-b7a4118bb314"})
		void testBadImplementationExtends(String s) {
			BadImplementation imp=new BadImplementation(s);
			assertTrue(DeepCopy.isDeepCopyable(imp));
		}

		@ParameterizedTest
		@DisplayName("Test GoodImplementation extends")
		@ValueSource(strings={"Identifiable:a76018f5-13fc-4f63-86e9-b7a4118bb314"})
		void testGoodImplementationExtends(String s) {
			GoodImplementation imp=new GoodImplementation(s);
			assertTrue(DeepCopy.isDeepCopyable(imp));
		}

		@ParameterizedTest
		@DisplayName("Test BadImplementation")
		@ValueSource(strings={"Identifiable:a76018f5-13fc-4f63-86e9-b7a4118bb314"})
		void testBadImplementation(String s) {
			BadImplementation imp=new BadImplementation(s);
			BadImplementation obj = DeepCopy.deepCopy(imp);
			assertNotEquals(s,obj.toString(), obj.toString() + " should be different from " + imp.toString());
		}

		@ParameterizedTest
		@DisplayName("Test GoodImplementation")
		@ValueSource(strings={"Identifiable:a76018f5-13fc-4f63-86e9-b7a4118bb314"})
		void testGoodImplementation(String s) {
			GoodImplementation imp=new GoodImplementation(s);
			GoodImplementation obj = DeepCopy.deepCopy(imp);
			assertEquals(s,obj.toString(), obj.toString() + " should be equal to " + imp.toString());
		}

	}

	@Nested
	@DisplayName("Test deepcopy basic data types")
	class TestBasicDataTypes{
		@ParameterizedTest
		@DisplayName("Test Integer DeepCopy")
		@ValueSource(ints={13})
		void testIntegerDeepCopy(Integer s) {
			Integer a=DeepCopy.deepCopy(s);
			assertEquals(s,a);
			a+=1;
			assertNotEquals(s,a);
		}

		@ParameterizedTest
		@DisplayName("Test Double DeepCopy")
		@ValueSource(doubles={13.7})
		void testDoubleDeepCopy(Double s) {
			Double a=DeepCopy.deepCopy(s);
			assertEquals(s,a);
			a+=1.2;
			assertNotEquals(s,a);
		}

		@ParameterizedTest
		@DisplayName("Test String DeepCopy")
		@ValueSource(strings={"13.7"})
		void testStringDeepCopy(String s) {
			String a=DeepCopy.deepCopy(s);
			assertEquals(s,a);
			a=a.concat("+1.2");
			assertNotEquals(s,a);
		}

	}
	
	@SuppressWarnings("unused")
	private static ArrayList<int[]> integerArrayData() {
		ArrayList<int[]> a=new ArrayList<>();
		a.add(new int[] {13,6,9,1,7});
		return a;
	}

	@SuppressWarnings("unused")
	private static ArrayList<double[]> doubleArrayData() {
		ArrayList<double[]> a=new ArrayList<>();
		a.add(new double[] {13.7,6.9,1.0,0.7});
		return a;
	}

	/*
	 * JUnit MethodSource interprets String and String[] as Object so, as an
	 * workaround, pass a concatenated string separated by commas and use
	 * {@code Conversion_NoChecks.stringToStringArray} to convert back into a 
	 * String[]
	 * 
	 * @return
	 */
	@SuppressWarnings("unused")
	private static String[] stringArrayData() {return new String[] {"13.7,6,9,1,7"};}

	@Nested
	@DisplayName("Test deepcopy basic data type arrays")
	class TestBasicDataTypeArrays{

		@ParameterizedTest
		@DisplayName("Test Integer Array DeepCopy")
		@MethodSource(value="jrain.deepCopy.DeepCopyTest#integerArrayData")
		void testIntegerArrayDeepCopy(int[] s) {
			Integer[] q=Conversion_NoChecks.intArrayToIntegerArray(s);
			Integer[] a=DeepCopy.deepCopy(q);
			assertArrayEquals(q,a);
			a[0]+=1;
			JunitTestUtils.assertArrayNotEquals(q,a); 
		}

		@ParameterizedTest
		@DisplayName("Test Double Array DeepCopy")
		@MethodSource(value="jrain.deepCopy.DeepCopyTest#doubleArrayData")
		void testDoubleArrayDeepCopy(double[] s) {
			Double[] q=Conversion_NoChecks.doubleArrayToDoubleArray(s);
			Double[] a=DeepCopy.deepCopy(q);
			assertArrayEquals(q,a);
			a[0]+=1.2;
			JunitTestUtils.assertArrayNotEquals(q,a);
		}

		@ParameterizedTest
		@DisplayName("Test String Array DeepCopy")
		@MethodSource(value="jrain.deepCopy.DeepCopyTest#stringArrayData")
		void testStringArrayDeepCopy(String s) {
			String[] q=Conversion_NoChecks.stringToStringArray(s, ",", 0, s.length());
			String[] a=DeepCopy.deepCopy(q);
			assertArrayEquals(q,a);
			a[0]=a[0].concat("+1.2");
			JunitTestUtils.assertArrayNotEquals(q,a);
		}

	}

	@SuppressWarnings("unused")
	private static ArrayList<ArrayList<Integer>> arrayListIntegerData() {
		ArrayList<ArrayList<Integer>> a=new ArrayList<ArrayList<Integer>>();
		a.add(new ArrayList<Integer>(Arrays.asList(13,6,9,1,7)));
		return a;
	}

	@SuppressWarnings("unused")
	private static ArrayList<ArrayList<String>> arrayListStringData() {
		ArrayList<ArrayList<String>> a=new ArrayList<ArrayList<String>>();
		a.add(new ArrayList<String>(Arrays.asList("13.7","6","9","1","7")));
		return a;
	}

	@Nested
	@DisplayName("Test deepcopy array data types")
	class TestArrayDataTypes{

		@ParameterizedTest
		@DisplayName("Test ArrayList<Integer> DeepCopy")
		@MethodSource(value="jrain.deepCopy.DeepCopyTest#arrayListIntegerData")
		void testArrayListIntegerDeepCopy(ArrayList<Integer> s) {
			ArrayList<Integer> a=DeepCopy.deepCopy(s);
			assertArrayEquals(s.toArray(),a.toArray());
			a.set(0, a.get(0)+1);
			JunitTestUtils.assertArrayNotEquals(s.toArray(),a.toArray()); 
		}

		@ParameterizedTest
		@DisplayName("Test ArrayList<String> DeepCopy")
		@MethodSource(value="jrain.deepCopy.DeepCopyTest#arrayListStringData")
		void testArrayListStringDeepCopy(ArrayList<String> s) {
			ArrayList<String> a=DeepCopy.deepCopy(s);
			assertArrayEquals(s.toArray(),a.toArray());
			a.set(0, a.get(0)+1);
			JunitTestUtils.assertArrayNotEquals(s.toArray(),a.toArray()); 
		}

	}

	@SuppressWarnings("unused")
	private static ArrayList<Set<Integer>> setData() {
		ArrayList<Set<Integer>> a=new ArrayList<>();
		HashSet<Integer> h = new HashSet<>();
		h.add(2);
		a.add(h);
		return a;
	}

	@SuppressWarnings("unused")
	private static ArrayList<HashSet<Integer>> hashSetData() {
		ArrayList<HashSet<Integer>> a=new ArrayList<>();
		HashSet<Integer> h = new HashSet<>();
		h.add(2);
		a.add(h);
		return a;
	}

	@Nested
	@DisplayName("Test deepcopy Set data types")
	class TestSetDataTypes{

		@ParameterizedTest
		@DisplayName("Test Set<Integer> DeepCopy")
		@MethodSource(value="jrain.deepCopy.DeepCopyTest#setData")
		void testSetDeepCopy(Set<Integer> s) {
			Set<Integer> a=DeepCopy.deepCopy(s);
			assertEquals(s,a);
			a.add(99);
			assertNotEquals(s,a); 
		}

		@ParameterizedTest
		@DisplayName("Test HashSet<Integer> DeepCopy")
		@MethodSource(value="jrain.deepCopy.DeepCopyTest#hashSetData")
		void testHashSetDeepCopy(HashSet<Integer> s) {
			HashSet<Integer> a=DeepCopy.deepCopy(s);
			assertEquals(s,a);
			a.add(99);
			assertNotEquals(s,a); 
		}

	}

	@SuppressWarnings("unused")
	private static ArrayList<Map<Integer,Integer>> mapData() {
		ArrayList<Map<Integer,Integer>> a=new ArrayList<>();
		HashMap<Integer,Integer> h = new HashMap<>();
		h.put(2,13);
		a.add(h);
		return a;
	}

	@SuppressWarnings("unused")
	private static ArrayList<HashMap<Integer,Integer>> hashMapData() {
		ArrayList<HashMap<Integer,Integer>> a=new ArrayList<>();
		HashMap<Integer,Integer> h = new HashMap<>();
		h.put(2,13);
		a.add(h);
		return a;
	}

	@Nested
	@DisplayName("Test deepcopy Map data types")
	class TestMapDataTypes{

		@ParameterizedTest
		@DisplayName("Test Map<Integer> DeepCopy")
		@MethodSource(value="jrain.deepCopy.DeepCopyTest#mapData")
		void testSetDeepCopy(Map<Integer,Integer> s) {
			Map<Integer,Integer> a=DeepCopy.deepCopy(s);
			assertEquals(s,a);
			a.put(99,99);
			assertNotEquals(s,a); 
		}

		@ParameterizedTest
		@DisplayName("Test HashMap<Integer> DeepCopy")
		@MethodSource(value="jrain.deepCopy.DeepCopyTest#hashMapData")
		void testHashSetDeepCopy(HashMap<Integer,Integer> s) {
			HashMap<Integer,Integer> a=DeepCopy.deepCopy(s);
			assertEquals(s,a);
			a.put(99,99);
			assertNotEquals(s,a); 
		}

	}

	@SuppressWarnings("unused")
	private static ArrayList<Entry<Integer,Integer>> entryData() {
		ArrayList<Entry<Integer,Integer>> a=new ArrayList<>();
		a.add(new Pair<>(2, 13));
		return a;
	}

	@Nested
	@DisplayName("Test deepcopy Entry data types")
	class TestEntryDataTypes{

		@ParameterizedTest
		@DisplayName("Test Entry<Integer,Integer> DeepCopy")
		@MethodSource(value="jrain.deepCopy.DeepCopyTest#entryData")
		void testEntryDeepCopy(Entry<Integer,Integer> s) {
			Entry<Integer, Integer> a=DeepCopy.deepCopy(s);
			assertEquals(s,a);
			Pair<Integer, Integer> p=new Pair<>(s);
			assertEquals(s,p);
			p.setValue(p.getValue()+1);
			assertNotEquals(s,p); 
		}

	}

	private static class Identifiable{
		String s;
		public Identifiable(Identifiable ID){
			s=ID.s;
		}
		
		public Identifiable(String ID){
			s=ID;
		}
		
		public Identifiable(){
			s="";
		}
		
		public String toString() {return s;}
	}
	
	public static class BadImplementation extends Identifiable implements DeepCopy<BadImplementation>{
		
		@Override
		public BadImplementation deepCopy() {
			return new BadImplementation();
		}
		
		public BadImplementation(String ID){
			super(ID);
		}
		
		public BadImplementation(){
			super();
		}
	}
	
	public static class GoodImplementation extends Identifiable implements DeepCopy<GoodImplementation>{
		
		/**
		 * A good implementation of {@link DeepCopy} requires a copy constructor
		 * or factory even if it is private
		 * @param original is the object with the values to copy
		 */
		protected GoodImplementation(GoodImplementation original) {
			super(original);
		}
		
		@Override
		public GoodImplementation deepCopy() {
			return new GoodImplementation(this);
		}
		
		public GoodImplementation(String ID){
			super(ID);
		}
		
		public GoodImplementation(){
			super();
		}
	}

}
