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
package jrain.collection.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * @author poltergeist0
 *
 * Provides several static methods to work with {@code Collection}
 */
public class CollectionUtils {


	/**
	 * Get the keys of all {@link Entry} stored in a {@link Collection}.
	 * May contain duplicates.
	 * 
	 * @param <TYPE_KEY> is the data type of the keys of the {@link Entry}
	 * @param <TYPE_DATA> is the data type of the values of the {@link Entry}
	 * @param <TYPE_LIST> is the data type of the {@link Collection}
	 * @param lst is the {@link Collection} of {@link Entry}
	 * @return a new {@link ArrayList} with the keys
	 */
	public static <TYPE_KEY,TYPE_DATA, TYPE_LIST extends Collection<Entry<TYPE_KEY,TYPE_DATA> > > ArrayList<TYPE_KEY> getKeys(TYPE_LIST lst){
		ArrayList<TYPE_KEY> a=new ArrayList<>();
		Iterator<Entry<TYPE_KEY, TYPE_DATA>> it = lst.iterator();
		while(it.hasNext()) {
			Entry<TYPE_KEY, TYPE_DATA> e = it.next();
			a.add(e.getKey());
		}
		return a;
	}

	/**
	 * Get the values of all {@link Entry} stored in a {@link Collection}.
	 * May contain duplicates.
	 * 
	 * @param <TYPE_KEY> is the data type of the keys of the {@link Entry}
	 * @param <TYPE_DATA> is the data type of the values of the {@link Entry}
	 * @param <TYPE_LIST> is the data type of the {@link Collection}
	 * @param lst is the {@link Collection} of {@link Entry}
	 * @return a new {@link ArrayList} with the values
	 */
	public static <TYPE_KEY,TYPE_DATA, TYPE_LIST extends Collection<Entry<TYPE_KEY,TYPE_DATA> > > ArrayList<TYPE_DATA> getValues(TYPE_LIST lst){
		ArrayList<TYPE_DATA> a=new ArrayList<>();
		Iterator<Entry<TYPE_KEY, TYPE_DATA>> it = lst.iterator();
		while(it.hasNext()) {
			Entry<TYPE_KEY, TYPE_DATA> e = it.next();
			a.add(e.getValue());
		}
		return a;
	}

	/**
	 * Convert a collection of Strings to an array of Strings.
	 * 
	 * @param <TYPE_LIST> is the type of the collection
	 * @param lst is the collection
	 * @return an array of Strings
	 */
	public static <TYPE_LIST extends Collection<String> > String[] toArray(TYPE_LIST lst){
		String[] a=new String[lst.size()];
		int i=0;
		Iterator<String> it = lst.iterator();
		while(it.hasNext()) {
			String e = it.next();
			a[i]=e;
			++i;
		}
		return a;
	}

	/**
	 * Compare two collections.
	 * 
	 * @param <TYPE_DATA> is the type of the collection
	 * @param <TYPE_LIST> is the type of the collection
	 * @param lst is the collection
	 * @return an array of Strings
	 */
	public static <TYPE_DATA extends Comparable<? super TYPE_DATA>, TYPE_LIST extends Collection<TYPE_DATA> > int compare(TYPE_LIST lst1, TYPE_LIST lst2){
		if(lst1.size()<lst2.size())return -1;
		if(lst1.size()>lst2.size())return 1;
		Iterator<TYPE_DATA> it1 = lst1.iterator();
		Iterator<TYPE_DATA> it2 = lst2.iterator();
		while(it1.hasNext()) {
			TYPE_DATA d1 = it1.next();
			TYPE_DATA d2 = it2.next();
			int a=d1.compareTo(d2);
			if(a!=0) {
				return a;
			}
		}
		return 0;
	}

}
