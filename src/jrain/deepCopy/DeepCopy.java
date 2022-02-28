package jrain.deepCopy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import java.util.Set;
import java.util.TreeSet;

/**
 * @author poltergeist0
 *
 * Interface implemented by classes that can return a deep copy of themselves.
 * 
 * Also provides several static methods to make deep copies of classes that do 
 * not implement this interface, such as classes of primitive types.
 * 
 * {@code 
 * //Example:
 * 	public static class MyClass implements DeepCopy<MyClass>{
 * 		public MyClass(MyClass original) {//copy constructor used in deepCopy
 * 			...;
 * 		}
 * 		@Override
 * 		public MyClass deepCopy() {
 * 			return new MyClass(this);
 * 		}
 * }

 * @param <TYPE> is the name of the class that implements this interface.
 */
public interface DeepCopy <TYPE extends Object> {

	/**
	 * Deep copy the object that implements this interface.
	 * 
	 * @return a new deep copy instance of the given TYPE
	 */
	public abstract TYPE deepCopy();

	/*==========================================================================
	 * =========================================================================
	 * =========================================================================
	 * =========================================================================
	 * Below this line there are only static auxiliary methods used to deep copy
	 * primitive types and some other java data types that are extensively used.
	 * =========================================================================
	 * =========================================================================
	 * =========================================================================
	 * =======================================================================*/

	/**
	 * Check if a given class extends/implements this interface.
	 * 
	 * @param <TYPE> is the type of the class
	 * @param cls is the class
	 * @return true if the given class extends/implements this interface
	 */
	public static <TYPE extends Object> boolean isDeepCopyable(Class<TYPE> cls) {
		if(DeepCopy.class.isAssignableFrom(cls)) return true;
		return false;
	}

	/**
	 * Check if a given object extends/implements this interface.
	 * 
	 * @param <TYPE> is the type of the object
	 * @param obj is the object to check
	 * @return true if the given object extends/implements this interface
	 */
	public static <TYPE extends Object> boolean isDeepCopyable(TYPE obj) {
		if(obj instanceof DeepCopy<?>) return true;
		return false;
	}

	/**
	 * Check if a given class can be deep copied even if it does not 
	 * extend/implement this interface.
	 * 
	 * @param <TYPE> is the type of the class
	 * @param cls is the class
	 * @return true if the given class can be deep copied
	 */
	public static <TYPE extends Object> boolean canDeepCopy(Class<TYPE> cls) {
		return isDeepCopyable(cls);
	}

	/**
	 * Check if a given object can be deep copied even if it does not 
	 * extend/implement this interface.
	 * 
	 * @param <TYPE> is the type of the object
	 * @param obj is the object to check
	 * @return true if the given object can be deep copied
	 */
	public static <TYPE extends Object> boolean canDeepCopy(TYPE obj) {
		return isDeepCopyable(obj);
	}

	/**
	 * Check if a given {@link String} can be deep copied.
	 * 
	 * @param s is the object to check
	 * @return always true since strings are immutable
	 */
	public static boolean canDeepCopy(String s) {return true;}

	/**
	 * Check if a given {@link Byte} can be deep copied.
	 * 
	 * @param b is the object to check
	 * @return always true
	 */
	public static boolean canDeepCopy(Byte b) {return true;}

	/**
	 * Check if a given {@link Integer} can be deep copied.
	 * 
	 * @param b is the object to check
	 * @return always true
	 */
	public static boolean canDeepCopy(Integer b) {return true;}

	/**
	 * Check if a given {@link Long} can be deep copied.
	 * 
	 * @param b is the object to check
	 * @return always true
	 */
	public static boolean canDeepCopy(Long b) {return true;}

	/**
	 * Check if a given {@link Float} can be deep copied.
	 * 
	 * @param b is the object to check
	 * @return always true
	 */
	public static boolean canDeepCopy(Float b) {return true;}

	/**
	 * Check if a given {@link Double} can be deep copied.
	 * 
	 * @param b is the object to check
	 * @return always true
	 */
	public static boolean canDeepCopy(Double b) {return true;}

	/**
	 * Main deep copy method. Internally calls the best method to perform the 
	 * deep copy, if possible.
	 * 
	 * Attempt to return a deep copy of the given object.
	 * If the object extends/implements this interface or there is a method
	 * available to perform the deep copy, a deep copy of the object is returned.
	 * 
	 * Otherwise, the same object given is returned.
	 * 
	 * A quick and dirty  way to find if this method really made a deep copy is 
	 * to compare the returned object reference with the given object reference.
	 * If they match, they are the same object and no deep copy was performed.
	 * 
	 * If a deep copy is really required, use one of the 
	 * {@link DeepCopy#isDeepCopyable(Class)}, {@link DeepCopy#isDeepCopyable(Object)}, 
	 * {@link DeepCopy#canDeepCopy(Class)} or {@link DeepCopy#canDeepCopy(Object)}
	 * in advance to check if this method can perform a deep copy. 
	 * 
	 * @param <TYPE> is the type of the object
	 * @param obj is the object
	 * @return a deep copy of the object if possible
	 */
	public static <TYPE extends Object> TYPE deepCopy(TYPE obj) {
		if(isDeepCopyable(obj)) {//TYPE extends DeepCopy
			@SuppressWarnings("unchecked")
			DeepCopy<TYPE> d=(DeepCopy<TYPE>) obj;
			return d.deepCopy();
		}
		if(canDeepCopy(obj)) {//TYPE does not extend DeepCopy but there is a method in this interface to make the deep copy
			return deepCopy(obj);
		}
		return obj;//TYPE does not extend DeepCopy and there is no method in this interface to make the deep copy
	}

	/**
	 * Perform the deep copy of a {@link String} object.
	 * 
	 * @param s is the object to copy
	 * @return a deep copy of the object
	 */
	public static String deepCopy(String s) {return new String(s);}

	/**
	 * Perform the deep copy of a {@link Byte} object.
	 * 
	 * @param b is the object to copy
	 * @return a deep copy of the object
	 */
	public static Byte deepCopy(Byte b) {return b.byteValue();}

	/**
	 * Perform the deep copy of a {@link Integer} object.
	 * 
	 * @param s is the object to copy
	 * @return a deep copy of the object
	 */
	public static Integer deepCopy(Integer s) {return s.intValue();}

	/**
	 * Perform the deep copy of a {@link Long} object.
	 * 
	 * @param s is the object to copy
	 * @return a deep copy of the object
	 */
	public static Long deepCopy(Long s) {return s.longValue();}

	/**
	 * Perform the deep copy of a {@link Float} object.
	 * 
	 * @param s is the object to copy
	 * @return a deep copy of the object
	 */
	public static Float deepCopy(Float s) {return s.floatValue();}

	/**
	 * Perform the deep copy of a {@link Double} object.
	 * 
	 * @param s is the object to copy
	 * @return a deep copy of the object
	 */
	public static Double deepCopy(Double s) {return s.doubleValue();}

	/**
	 * Perform the deep copy of an array holding {@link Byte} objects.
	 * 
	 * @param ba is the array containing the original elements
	 * @return an array where the deep copied elements are stored, if possible
	 */
	public static Byte[] deepCopy(Byte[] ba) {
		Byte[] a=new Byte[ba.length];
		return deepCopy(ba, a);
	}

	/**
	 * Perform the deep copy of an array holding {@link Integer} objects.
	 * 
	 * @param ba is the array containing the original elements
	 * @return an array where the deep copied elements are stored, if possible
	 */
	public static Integer[] deepCopy(Integer[] ba) {
		Integer[] a=new Integer[ba.length];
		return deepCopy(ba, a);
	}

	/**
	 * Perform the deep copy of an array holding {@link Long} objects.
	 * 
	 * @param ba is the array containing the original elements
	 * @return an array where the deep copied elements are stored, if possible
	 */
	public static Long[] deepCopy(Long[] ba) {
		Long[] a=new Long[ba.length];
		return deepCopy(ba, a);
	}

	/**
	 * Perform the deep copy of an array holding {@link Float} objects.
	 * 
	 * @param ba is the array containing the original elements
	 * @return an array where the deep copied elements are stored, if possible
	 */
	public static Float[] deepCopy(Float[] ba) {
		Float[] a=new Float[ba.length];
		return deepCopy(ba, a);
	}

	/**
	 * Perform the deep copy of an array holding {@link Double} objects.
	 * 
	 * @param ba is the array containing the original elements
	 * @return an array where the deep copied elements are stored, if possible
	 */
	public static Double[] deepCopy(Double[] ba) {
		Double[] a=new Double[ba.length];
		return deepCopy(ba, a);
	}

	/**
	 * Perform the deep copy of an array holding {@link String} objects.
	 * 
	 * @param ba is the array containing the original elements
	 * @return an array where the deep copied elements are stored, if possible
	 */
	public static String[] deepCopy(String[] ba) {
		String[] a=new String[ba.length];
		return deepCopy(ba, a);
	}

	/**
	 * Perform the deep copy of a {@link Set} holding objects of a given type.
	 * 
	 * Since the {@link Set} class is abstract, this method uses a {@link HashSet} 
	 * which extends {@link Set}
	 * 
	 * May fail silently if the type of the {@link Set} is not deep copyable.
	 * 
	 * @param <TYPE> is the type of the elements of the {@link Set}
	 * @param set is the {@link Set} to deep copy
	 * @return a new {@link Set} with the deep copied elements, if possible
	 */
	public static <TYPE extends Object> Set<TYPE> deepCopy(Set<TYPE> set) {
		HashSet<TYPE> h=new HashSet<TYPE>();
		if(set.isEmpty())return h;
		return deepCopy(set, h);
	}

	/**
	 * Perform the deep copy of a {@link HashSet} holding objects of a given type.
	 * 
	 * May fail silently if the type of the {@link HashSet} is not deep copyable.
	 * 
	 * @param <TYPE> is the type of the elements of the {@link HashSet}
	 * @param hs is the {@link HashSet} to deep copy
	 * @return a new {@link HashSet} with the deep copied elements, if possible
	 */
	public static <TYPE extends Object> HashSet<TYPE> deepCopy(HashSet<TYPE> hs) {
		HashSet<TYPE> h=new HashSet<TYPE>();
		if(hs.isEmpty())return h;
		return deepCopy(hs, h);
	}

	/**
	 * Perform the deep copy of a {@link TreeSet} holding objects of a given type.
	 * 
	 * May fail silently if the type of the {@link TreeSet} is not deep copyable.
	 * 
	 * @param <TYPE> is the type of the elements of the {@link TreeSet}
	 * @param ts is the {@link TreeSet} to deep copy
	 * @return a new {@link TreeSet} with the deep copied elements, if possible
	 */
	public static <TYPE extends Object> TreeSet<TYPE> deepCopy(TreeSet<TYPE> ts) {
		TreeSet<TYPE> t=new TreeSet<TYPE>();
		if(ts.isEmpty())return t;
		return deepCopy(ts, t);
	}

	/**
	 * Perform the deep copy of a {@link ArrayList} holding objects of a given type.
	 * 
	 * May fail silently if the type of the {@link ArrayList} is not deep copyable.
	 * 
	 * @param <TYPE> is the type of the elements of the {@link ArrayList}
	 * @param a is the {@link ArrayList} to deep copy
	 * @return a new {@link ArrayList} with the deep copied elements, if possible
	 */
	public static <TYPE extends Object> ArrayList<TYPE> deepCopy(ArrayList<TYPE> a) {
		ArrayList<TYPE> s=new ArrayList<TYPE>();
		if(a.isEmpty())return s;
		return deepCopy(a, s);
	}

	/**
	 * Perform the deep copy of a {@link Map} holding objects of a given type.
	 * 
	 * Since the {@link Map} class is abstract, this method uses a {@link HashMap} 
	 * which extends {@link Map}
	 * 
	 * May fail silently if either the key or the value of the {@link Map} are 
	 * not deep copyable themselves.
	 * 
	 * @param <TYPE_KEY> is the type of the key of the {@link Map}
	 * @param <TYPE_DATA> is the type of the value of the {@link Map}
	 * @param hm is the {@link Map} to deep copy
	 * @return a new {@link Map} with the deep copied elements, if possible
	 */
	public static <TYPE_KEY extends Object,TYPE_DATA extends Object> Map<TYPE_KEY,TYPE_DATA> deepCopy(Map<TYPE_KEY,TYPE_DATA> hm) {
		HashMap<TYPE_KEY,TYPE_DATA> h=new HashMap<TYPE_KEY,TYPE_DATA>();
		if(hm.isEmpty())return h;
		return deepCopy(hm, h);
	}

	/**
	 * Perform the deep copy of a {@link HashMap} holding objects of a given type.
	 * 
	 * May fail silently if either the key or the value of the {@link HashMap} are 
	 * not deep copyable themselves.
	 * 
	 * @param <TYPE_KEY> is the type of the key of the {@link HashMap}
	 * @param <TYPE_DATA> is the type of the value of the {@link HashMap}
	 * @param hm is the {@link HashMap} to deep copy
	 * @return a new {@link HashMap} with the deep copied elements, if possible
	 */
	public static <TYPE_KEY extends Object,TYPE_DATA extends Object> HashMap<TYPE_KEY,TYPE_DATA> deepCopy(HashMap<TYPE_KEY,TYPE_DATA> hm) {
		HashMap<TYPE_KEY,TYPE_DATA> h=new HashMap<TYPE_KEY,TYPE_DATA>();
		if(hm.isEmpty())return h;
		return deepCopy(hm, h);
	}

	/**
	 * WARNING: Auxiliary method for use in the deepCopy interface only or other
	 * methods that extend deepCopy for data types not in this file.
	 * 
	 * Perform the deep copy of an array of a given type, if possible.
	 * 
	 * The destination array must have been allocated in advance to be of the 
	 * same size than the source array.
	 * 
	 * May fail silently if the type is not deep copyable.
	 * 
	 * @param <TYPE> is the type of the elements of the array
	 * @param source is the array containing the original elements
	 * @param destination is array where the deep copied elements are stored
	 * @return the destination array
	 */
	public static <TYPE extends Object> TYPE[] deepCopy(TYPE[] source, TYPE[] destination) {
		for(int i=0; i<source.length;++i) {
			destination[i]=deepCopy(source[i]);
		}
		return destination;
	}

	/**
	 * WARNING: Auxiliary method for use in the deepCopy interface only or other
	 * methods that extend deepCopy for data types not in this file.
	 * 
	 * Perform the deep copy of a collection of a given type, if possible.
	 * 
	 * The destination collection must have been allocated in advance to be of the 
	 * same size than the source collection.
	 * 
	 * May fail silently if the type is not deep copyable.
	 * 
	 * @param <TYPE> is the type of the elements of the array
	 * @param <TYPE_COLLECTION> is the type of the collection
	 * @param source is the collection containing the original elements
	 * @param destination is collection where the deep copied elements are stored
	 * @return the destination collection
	 */
	public static <TYPE extends Object, TYPE_COLLECTION extends Collection<TYPE> > TYPE_COLLECTION deepCopy(TYPE_COLLECTION source, TYPE_COLLECTION destination) {
		Iterator<TYPE> it = source.iterator();
		while(it.hasNext()) {
			TYPE t=it.next();
			destination.add(deepCopy(t));
		}
		return destination;
	}

	/**
	 * WARNING: Auxiliary method for use in the deepCopy interface only or other
	 * methods that extend deepCopy for data types not in this file.
	 * 
	 * Perform the deep copy of a map type collection of a given type, if possible.
	 * 
	 * The destination collection must have been allocated in advance to be of the 
	 * same size than the source collection.
	 * 
	 * May fail silently if the type is not deep copyable.
	 * 
	 * @param <TYPE_KEY> is the type of the key
	 * @param <TYPE_DATA> is the type of the value
	 * @param <TYPE_MAP> is the type of the map collection
	 * @param source is the collection containing the original elements
	 * @param destination is collection where the deep copied elements are stored
	 * @return the destination collection
	 */
	public static <TYPE_KEY extends Object,TYPE_DATA extends Object, TYPE_MAP extends Map<TYPE_KEY,TYPE_DATA> > TYPE_MAP deepCopy(TYPE_MAP source, TYPE_MAP destination) {
		Iterator<Entry<TYPE_KEY, TYPE_DATA>> it = source.entrySet().iterator();
		while(it.hasNext()) {//process remaining
			Entry<TYPE_KEY, TYPE_DATA> e = it.next();
			TYPE_KEY k=e.getKey();
			TYPE_DATA d=e.getValue();
			destination.put(deepCopy(k), deepCopy(d));
		}
		return destination;
	}
}
