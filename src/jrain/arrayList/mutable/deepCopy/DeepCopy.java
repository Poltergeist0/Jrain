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
package jrain.arrayList.mutable.deepCopy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import jrain.arrayList.mutable.ArrayListByteArray;
import jrain.arrayList.mutable.ArrayListEntry;
import jrain.arrayList.mutable.ArrayListString;

/**
 * @author poltergeist0
 *
 * Interface that extends {@link jrain.deepCopy.DeepCopy} for the 
 * arrayList types in this package.
 * 
 * @param <TYPE> is the name of the class that implements this interface.
 */
public interface DeepCopy <TYPE extends Object> extends jrain.deepCopy.DeepCopy<TYPE>{

	/**
	 * Perform the deep copy of a {@link ArrayListByteArray}.
	 * 
	 * @param a is the {@link ArrayListByteArray} to deep copy
	 * @return a new {@link ArrayListByteArray} with the deep copied elements, if possible
	 */
	public static ArrayListByteArray deepCopy(ArrayListByteArray a) {
		ArrayListByteArray s=new ArrayListByteArray();
		if(a==null || a.isEmpty())return s;
		return jrain.deepCopy.DeepCopy.deepCopy(a, s);
	}

	/**
	 * Perform the deep copy of a {@link ArrayListString}.
	 * 
	 * @param a is the {@link ArrayListString} to deep copy
	 * @return a new {@link ArrayListString} with the deep copied elements, if possible
	 */
	public static ArrayListString deepCopy(ArrayListString a) {
		ArrayListString s=new ArrayListString();
		if(a==null || a.isEmpty())return s;
		return jrain.deepCopy.DeepCopy.deepCopy(a, s);
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
		if(a==null || a.isEmpty())return s;
		return jrain.deepCopy.DeepCopy.deepCopy(a, s);
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
