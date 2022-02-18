package jrain.mutable.entry.deepCopy.interfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

import jrain.mutable.entry.Pair;

/**
 * @author poltergeist0
 *
 * Interface that extends {@link jrain.deepCopy.interfaces.DeepCopy} for the 
 * Entry types in this package.
 * 
 * @param <TYPE> is the name of the class that implements this interface.
 */
public interface DeepCopy <TYPE extends Object> extends jrain.deepCopy.interfaces.DeepCopy<TYPE>{

	/**
	 * Deep copy the object that implements this interface.
	 * 
	 * @return a new deep copy instance of the given TYPE
	 */
//	public abstract TYPE deepCopy();

	/**
	 * Perform the deep copy of an {@link Entry} object.
	 * 
	 * May fail silently if either the key or the value of the {@link Entry} are 
	 * not deep copyable themselves.
	 * 
	 * Since the {@link Entry} class is abstract, this method uses a {@link Pair} 
	 * which extends {@link Entry} .
	 * 
	 * @param <TYPE_KEY> is the type of the key of the {@link Entry}
	 * @param <TYPE_DATA> is the type of the value of the {@link Entry}
	 * @param <TYPE_ENTRY> is the type that extends {@link Entry}
	 * @param e is the {@link Entry} to deep copy
	 * @return a new {@link Pair} with a deep copy of the {@link Entry} object if possible
	 */
	public static <TYPE_KEY extends Object,TYPE_DATA extends Object, TYPE_ENTRY extends Entry<TYPE_KEY,TYPE_DATA> > 
	Pair<TYPE_KEY,TYPE_DATA> deepCopy(TYPE_ENTRY e) {
		return new Pair<TYPE_KEY, TYPE_DATA>(jrain.deepCopy.interfaces.DeepCopy.deepCopy(e.getKey()), jrain.deepCopy.interfaces.DeepCopy.deepCopy(e.getValue()));
	}

	/*==========================================================================
	 * =========================================================================
	 * =========================================================================
	 * =========================================================================
	 * Below this line there are static methods that only redirect to methods
	 * belonging to the class that was extended to workaround the limitation
	 * that interfaces can not inherit static methods.
	 * This potentially avoids having to use two interfaces with the same name 
	 * on classes that use this interface.
	 * See {@link jrain.deepCopy.interfaces.DeepCopy.isDeepCopyable} for the
	 * documentation on any of them.
	 * =========================================================================
	 * =========================================================================
	 * =========================================================================
	 * =======================================================================*/

	public static <TYPE extends Object> boolean isDeepCopyable(Class<TYPE> cls) {return jrain.deepCopy.interfaces.DeepCopy.isDeepCopyable(cls);}

	public static <TYPE extends Object> boolean isDeepCopyable(TYPE obj) {return jrain.deepCopy.interfaces.DeepCopy.isDeepCopyable(obj);}

	public static <TYPE extends Object> boolean canDeepCopy(Class<TYPE> cls) {return jrain.deepCopy.interfaces.DeepCopy.canDeepCopy(cls);}

	public static <TYPE extends Object> boolean canDeepCopy(TYPE obj) {return jrain.deepCopy.interfaces.DeepCopy.canDeepCopy(obj);}

	public static boolean canDeepCopy(String s) {return jrain.deepCopy.interfaces.DeepCopy.canDeepCopy(s);}

	public static boolean canDeepCopy(Byte b) {return jrain.deepCopy.interfaces.DeepCopy.canDeepCopy(b);}

	public static boolean canDeepCopy(Integer b) {return jrain.deepCopy.interfaces.DeepCopy.canDeepCopy(b);}

	public static boolean canDeepCopy(Long b) {return jrain.deepCopy.interfaces.DeepCopy.canDeepCopy(b);}

	public static boolean canDeepCopy(Float b) {return jrain.deepCopy.interfaces.DeepCopy.canDeepCopy(b);}

	public static boolean canDeepCopy(Double b) {return jrain.deepCopy.interfaces.DeepCopy.canDeepCopy(b);}

	public static <TYPE extends Object> TYPE deepCopy(TYPE obj) {return jrain.deepCopy.interfaces.DeepCopy.deepCopy(obj);}

	public static String deepCopy(String s) {return jrain.deepCopy.interfaces.DeepCopy.deepCopy(s);}

	public static Byte deepCopy(Byte b) {return jrain.deepCopy.interfaces.DeepCopy.deepCopy(b);}

	public static Integer deepCopy(Integer s) {return jrain.deepCopy.interfaces.DeepCopy.deepCopy(s);}

	public static Long deepCopy(Long s) {return jrain.deepCopy.interfaces.DeepCopy.deepCopy(s);}

	public static Float deepCopy(Float s) {return jrain.deepCopy.interfaces.DeepCopy.deepCopy(s);}

	public static Double deepCopy(Double s) {return jrain.deepCopy.interfaces.DeepCopy.deepCopy(s);}

	public static Byte[] deepCopy(Byte[] ba) {return jrain.deepCopy.interfaces.DeepCopy.deepCopy(ba);}

	public static Integer[] deepCopy(Integer[] ba) {return jrain.deepCopy.interfaces.DeepCopy.deepCopy(ba);}

	public static Long[] deepCopy(Long[] ba) {return jrain.deepCopy.interfaces.DeepCopy.deepCopy(ba);}

	public static Float[] deepCopy(Float[] ba) {return jrain.deepCopy.interfaces.DeepCopy.deepCopy(ba);}

	public static Double[] deepCopy(Double[] ba) {return jrain.deepCopy.interfaces.DeepCopy.deepCopy(ba);}

	public static String[] deepCopy(String[] ba) {return jrain.deepCopy.interfaces.DeepCopy.deepCopy(ba);}

	public static <TYPE extends Object> Set<TYPE> deepCopy(Set<TYPE> set) {return jrain.deepCopy.interfaces.DeepCopy.deepCopy(set);}

	public static <TYPE extends Object> HashSet<TYPE> deepCopy(HashSet<TYPE> hs) {return jrain.deepCopy.interfaces.DeepCopy.deepCopy(hs);}

	public static <TYPE extends Object> TreeSet<TYPE> deepCopy(TreeSet<TYPE> ts) {return jrain.deepCopy.interfaces.DeepCopy.deepCopy(ts);}

	public static <TYPE extends Object> ArrayList<TYPE> deepCopy(ArrayList<TYPE> a) {return jrain.deepCopy.interfaces.DeepCopy.deepCopy(a);}

	public static <TYPE_KEY extends Object,TYPE_DATA extends Object> Map<TYPE_KEY,TYPE_DATA> deepCopy(Map<TYPE_KEY,TYPE_DATA> hm) {return jrain.deepCopy.interfaces.DeepCopy.deepCopy(hm);}

	public static <TYPE_KEY extends Object,TYPE_DATA extends Object> HashMap<TYPE_KEY,TYPE_DATA> deepCopy(HashMap<TYPE_KEY,TYPE_DATA> hm) {return jrain.deepCopy.interfaces.DeepCopy.deepCopy(hm);}

//	public static <TYPE extends Object> TYPE[] deepCopy(TYPE[] source, TYPE[] destination) {return jrain.deepCopy.interfaces.DeepCopy.deepCopy(source, destination);}
//
//	public static <TYPE extends Object, TYPE_COLLECTION extends Collection<TYPE> > TYPE_COLLECTION deepCopy(TYPE_COLLECTION source, TYPE_COLLECTION destination) {return jrain.deepCopy.interfaces.DeepCopy.deepCopy(source, destination);}
//
//	public static <TYPE_KEY extends Object,TYPE_DATA extends Object, TYPE_MAP extends Map<TYPE_KEY,TYPE_DATA> > TYPE_MAP deepCopy(TYPE_MAP source, TYPE_MAP destination) {return jrain.deepCopy.interfaces.DeepCopy.deepCopy(source, destination);}

}
