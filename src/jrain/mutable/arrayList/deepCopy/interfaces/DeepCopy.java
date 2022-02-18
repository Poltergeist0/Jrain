package jrain.mutable.arrayList.deepCopy.interfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import jrain.mutable.arrayList.ArrayListByteArray;
import jrain.mutable.arrayList.ArrayListEntry;
import jrain.mutable.arrayList.ArrayListString;

/**
 * @author poltergeist0
 *
 * Interface that extends {@link jrain.deepCopy.interfaces.DeepCopy} for the 
 * arrayList types in this package.
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
	 * Perform the deep copy of a {@link ArrayListByteArray}.
	 * 
	 * @param a is the {@link ArrayListByteArray} to deep copy
	 * @return a new {@link ArrayListByteArray} with the deep copied elements, if possible
	 */
	public static ArrayListByteArray deepCopy(ArrayListByteArray a) {
		ArrayListByteArray s=new ArrayListByteArray();
		if(a.isEmpty())return s;
		return jrain.deepCopy.interfaces.DeepCopy.deepCopy(a, s);
	}

	/**
	 * Perform the deep copy of a {@link ArrayListString}.
	 * 
	 * @param a is the {@link ArrayListString} to deep copy
	 * @return a new {@link ArrayListString} with the deep copied elements, if possible
	 */
	public static ArrayListString deepCopy(ArrayListString a) {
		ArrayListString s=new ArrayListString();
		if(a.isEmpty())return s;
		return jrain.deepCopy.interfaces.DeepCopy.deepCopy(a, s);
	}

	/**
	 * Perform the deep copy of a {@link ArrayListEntry}.
	 * 
	 * @param <TYPE_KEY> is the type of the key of the {@link ArrayListEntry}
	 * @param <TYPE_DATA> is the type of the value of the {@link ArrayListEntry}
	 * @param a is the {@link ArrayListEntry} to deep copy
	 * @return a new {@link ArrayListEntry} with the deep copied elements, if possible
	 */
	public static <TYPE_KEY extends Object,TYPE_DATA extends Object> ArrayListEntry<TYPE_KEY,TYPE_DATA> deepCopy(ArrayListEntry<TYPE_KEY,TYPE_DATA> a) {
		ArrayListEntry<TYPE_KEY,TYPE_DATA> s=new ArrayListEntry<>(a.size());
		if(a.isEmpty())return s;
		return jrain.deepCopy.interfaces.DeepCopy.deepCopy(a, s);
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
