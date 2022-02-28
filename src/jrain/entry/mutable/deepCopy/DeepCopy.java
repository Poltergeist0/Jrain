package jrain.entry.mutable.deepCopy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

import jrain.entry.mutable.Pair;

/**
 * @author poltergeist0
 *
 * Interface that extends {@link jrain.deepCopy.DeepCopy} for the 
 * Entry types in this package.
 * 
 * @param <TYPE> is the name of the class that implements this interface.
 */
public interface DeepCopy <TYPE extends Object> extends jrain.deepCopy.DeepCopy<TYPE>{

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
		return new Pair<TYPE_KEY, TYPE_DATA>(jrain.deepCopy.DeepCopy.deepCopy(e.getKey()), jrain.deepCopy.DeepCopy.deepCopy(e.getValue()));
	}

	/*==========================================================================
	 * =========================================================================
	 * =========================================================================
	 * =========================================================================
	 * Below this line there are static methods that only redirect to methods
	 * belonging to the class that was extended to workaround the limitation
	 * that interfaces can not inherit static methods.
	 * This potentially avoids having to use two or more interfaces with the 
	 * same name on classes that use this interface.
	 * See {@link jrain.deepCopy.DeepCopy} for the documentation on 
	 * any of them.
	 * =========================================================================
	 * =========================================================================
	 * =========================================================================
	 * =======================================================================*/

	public static <TYPE extends Object> boolean isDeepCopyable(Class<TYPE> cls) {return jrain.deepCopy.DeepCopy.isDeepCopyable(cls);}

	public static <TYPE extends Object> boolean isDeepCopyable(TYPE obj) {return jrain.deepCopy.DeepCopy.isDeepCopyable(obj);}

	public static <TYPE extends Object> boolean canDeepCopy(Class<TYPE> cls) {return jrain.deepCopy.DeepCopy.canDeepCopy(cls);}

	public static <TYPE extends Object> boolean canDeepCopy(TYPE obj) {return jrain.deepCopy.DeepCopy.canDeepCopy(obj);}

	public static boolean canDeepCopy(String s) {return jrain.deepCopy.DeepCopy.canDeepCopy(s);}

	public static boolean canDeepCopy(Byte b) {return jrain.deepCopy.DeepCopy.canDeepCopy(b);}

	public static boolean canDeepCopy(Integer b) {return jrain.deepCopy.DeepCopy.canDeepCopy(b);}

	public static boolean canDeepCopy(Long b) {return jrain.deepCopy.DeepCopy.canDeepCopy(b);}

	public static boolean canDeepCopy(Float b) {return jrain.deepCopy.DeepCopy.canDeepCopy(b);}

	public static boolean canDeepCopy(Double b) {return jrain.deepCopy.DeepCopy.canDeepCopy(b);}

	public static <TYPE extends Object> TYPE deepCopy(TYPE obj) {return jrain.deepCopy.DeepCopy.deepCopy(obj);}

	public static String deepCopy(String s) {return jrain.deepCopy.DeepCopy.deepCopy(s);}

	public static Byte deepCopy(Byte b) {return jrain.deepCopy.DeepCopy.deepCopy(b);}

	public static Integer deepCopy(Integer s) {return jrain.deepCopy.DeepCopy.deepCopy(s);}

	public static Long deepCopy(Long s) {return jrain.deepCopy.DeepCopy.deepCopy(s);}

	public static Float deepCopy(Float s) {return jrain.deepCopy.DeepCopy.deepCopy(s);}

	public static Double deepCopy(Double s) {return jrain.deepCopy.DeepCopy.deepCopy(s);}

	public static Byte[] deepCopy(Byte[] ba) {return jrain.deepCopy.DeepCopy.deepCopy(ba);}

	public static Integer[] deepCopy(Integer[] ba) {return jrain.deepCopy.DeepCopy.deepCopy(ba);}

	public static Long[] deepCopy(Long[] ba) {return jrain.deepCopy.DeepCopy.deepCopy(ba);}

	public static Float[] deepCopy(Float[] ba) {return jrain.deepCopy.DeepCopy.deepCopy(ba);}

	public static Double[] deepCopy(Double[] ba) {return jrain.deepCopy.DeepCopy.deepCopy(ba);}

	public static String[] deepCopy(String[] ba) {return jrain.deepCopy.DeepCopy.deepCopy(ba);}

	public static <TYPE extends Object> Set<TYPE> deepCopy(Set<TYPE> set) {return jrain.deepCopy.DeepCopy.deepCopy(set);}

	public static <TYPE extends Object> HashSet<TYPE> deepCopy(HashSet<TYPE> hs) {return jrain.deepCopy.DeepCopy.deepCopy(hs);}

	public static <TYPE extends Object> TreeSet<TYPE> deepCopy(TreeSet<TYPE> ts) {return jrain.deepCopy.DeepCopy.deepCopy(ts);}

	public static <TYPE extends Object> ArrayList<TYPE> deepCopy(ArrayList<TYPE> a) {return jrain.deepCopy.DeepCopy.deepCopy(a);}

	public static <TYPE_KEY extends Object,TYPE_DATA extends Object> Map<TYPE_KEY,TYPE_DATA> deepCopy(Map<TYPE_KEY,TYPE_DATA> hm) {return jrain.deepCopy.DeepCopy.deepCopy(hm);}

	public static <TYPE_KEY extends Object,TYPE_DATA extends Object> HashMap<TYPE_KEY,TYPE_DATA> deepCopy(HashMap<TYPE_KEY,TYPE_DATA> hm) {return jrain.deepCopy.DeepCopy.deepCopy(hm);}

}
