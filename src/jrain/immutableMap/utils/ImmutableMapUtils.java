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
package jrain.immutableMap.utils;

import java.util.Map.Entry;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.UnmodifiableIterator;

/**
 * @author poltergeist0
 *
 * Provides several static methods to work with {@code ImmutableMap}
 */
public class ImmutableMapUtils{

	/**
	 * Add entry.
	 * 
	 * @param <E> is the data type of the key
	 * @param <F> is the data type of the value
	 * @param original is the original map
	 * @param key is the key of the entry to add
	 * @param data is the value of the entry to add
	 * @return a new {@link ImmutableMap} with the entry added
	 */
	public static <E,F> ImmutableMap<E,F> add(final ImmutableMap<E,F> original, final E key, final F data){
		ImmutableMap.Builder<E,F> b=new ImmutableMap.Builder<>();
		b.putAll(original);
		b.put(key, data);
		return b.build();
	}
	
	/**
	 * Add entries.
	 * 
	 * @param <E> is the data type of the key
	 * @param <F> is the data type of the value
	 * @param original is the original map
	 * @param data is map holding the entries to add
	 * @return a new {@link ImmutableMap} with the entries added
	 */
	public static <E,F> ImmutableMap<E,F> add(final ImmutableMap<E,F> original, final ImmutableMap<E,F> data){
		ImmutableMap.Builder<E,F> b=new ImmutableMap.Builder<>();
		b.putAll(original);
		b.putAll(data);
		return b.build();
	}
	
	/**
	 * Remove entry.
	 * 
	 * @param <E> is the data type of the key
	 * @param <F> is the data type of the value
	 * @param original is the original map
	 * @param data is the key of the entry to remove
	 * @return a new {@link ImmutableMap} with the entry removed
	 */
	public static <E,F> ImmutableMap<E,F> remove(final ImmutableMap<E,F> original, final E data){
		ImmutableMap.Builder<E,F> b=new ImmutableMap.Builder<>();
		UnmodifiableIterator<E> it=original.keySet().iterator();
		E i=null;
		while(it.hasNext()){
			i=it.next();
			if(!i.equals(data))b.put(i,original.get(i));
		}
		return b.build();
	}
	
	/**
	 * Remove entries.
	 * 
	 * @param <E> is the data type of the key
	 * @param <F> is the data type of the value
	 * @param original is the original map
	 * @param data is map holding the entries to remove
	 * @return a new {@link ImmutableMap} with the entries removed
	 */
	public static <E,F> ImmutableMap<E,F> remove(final ImmutableMap<E,F> original, final ImmutableList<E> data){
		ImmutableMap.Builder<E,F> b=new ImmutableMap.Builder<>();
		UnmodifiableIterator<E> it=original.keySet().iterator();
		E i=null;
		while(it.hasNext()){
			i=it.next();
			if(!data.contains(i))b.put(i,original.get(i));
		}
		return b.build();
	}
	
	/**
	 * Removes the old data and inserts the new. Same functionality than a call to
	 * the remove method followed by a call to the add method, but slightly faster
	 * since it avoids creating a temporary {@link ImmutableMap}.
	 * 
	 * @param <E> is the data type of the key
	 * @param <F> is the data type of the value
	 * @param original is the original map
	 * @param dataOld is map holding the entries to remove
	 * @param dataNew is map holding the entries to add
	 * @return a new {@link ImmutableMap}
	 */
	public static <E,F> ImmutableMap<E,F> replace(final ImmutableMap<E,F> original, final ImmutableList<E> dataOld, final ImmutableMap<E,F> dataNew){
		ImmutableMap.Builder<E,F> b=new ImmutableMap.Builder<>();
		UnmodifiableIterator<E> it=original.keySet().iterator();
		E i=null;
		while(it.hasNext()){
			i=it.next();
			if(!dataOld.contains(i))
				b.put(i,original.get(i));
		}
		b.putAll(dataNew);
		return b.build();
	}
	
	/**
	 * Removes the old data and inserts the new. Same functionality than a call to
	 * the remove method followed by a call to the add method, but slightly faster
	 * since it avoids creating a temporary {@link ImmutableMap}.
	 * 
	 * @param <E> is the data type of the key
	 * @param <F> is the data type of the value
	 * @param original is the original map
	 * @param keyOld is the entry to remove
	 * @param keyNew is the key of the entry to add
	 * @param dataNew is the value of the entry to add
	 * @return a new {@link ImmutableMap}
	 */
	public static <E,F> ImmutableMap<E,F> replace(final ImmutableMap<E,F> original, final E keyOld, final E keyNew, final F dataNew){
		ImmutableMap.Builder<E,F> b=new ImmutableMap.Builder<>();
		UnmodifiableIterator<E> it=original.keySet().iterator();
		E i=null;
		while(it.hasNext()){
			i=it.next();
			if(!i.equals(keyOld))b.put(i,original.get(i));
		}
		b.put(keyNew, dataNew);
		return b.build();
	}
	
	/**
	 * Get a sub map of a map with a given list of keys.
	 * 
	 * @param <E> is the data type of the key
	 * @param <F> is the data type of the value
	 * @param original is the original map
	 * @param data is the list of keys for the new map
	 * @return a new {@link ImmutableMap} with the given keys
	 */
	public static <E,F> ImmutableMap<E,F> submap(final ImmutableMap<E,F> original, final ImmutableList<E> data){
		ImmutableMap.Builder<E,F> b=new ImmutableMap.Builder<>();
		UnmodifiableIterator<E> it=original.keySet().iterator();
		E i=null;
		while(it.hasNext()){
			i=it.next();
			if(data.contains(i))b.put(i,original.get(i));
		}
		return b.build();
	}
	
	/**
	 * Check if a given sub map is a smaller subset of a given map.
	 * 
	 * @param <E> is the data type of the key
	 * @param <F> is the data type of the value
	 * @param map is the main map
	 * @param submap is the sub map to be checked
	 * @return true if all entries of the sub map are present in the map
	 */
	public static <E,F> boolean isSubMap(final ImmutableMap<E,F> map,final ImmutableMap<E,F> submap){
		if(map.size()>submap.size()){
			if(map.entrySet().containsAll(submap.entrySet())) return true;
		}
		return false;
	}
	
	/**
	 * Get a map that is the intersection of the entries of two maps.
	 * Values are ignored for the calculation.
	 * 
	 * @param <E> is the data type of the key
	 * @param <F> is the data type of the value
	 * @param map1 is one map
	 * @param map2 is the other map
	 * @return a new {@link ImmutableMap} with the entries that belong to both maps
	 */
	public static <E,F> ImmutableMap<E,F> intersect(final ImmutableMap<E,F> map1,final ImmutableMap<E,F> map2){
		ImmutableSet<Entry<E, F>> e = map1.entrySet();
		UnmodifiableIterator<Entry<E,F>> it = map2.entrySet().iterator();
		Entry<E,F> n=null;
		ImmutableMap.Builder<E, F> m=ImmutableMap.builder();
		while(it.hasNext()){
			n=it.next();
			if(e.contains(n))m.put(n);
		}
		return m.build();
	}
	
	/**
	 * Compare two maps and return a number that indicates the likeliness between them.
	 * Values are ignored for the comparison.
	 * Possibly return values:
	 *  - a positive number >0 indicates they are different but have that number of shared entries
	 *  - a zero indicates that the maps are identical (share all entries)
	 *  - a -1 indicates that map2 is a submap of map1
	 *  - a -2 indicates that map1 is a submap of map2
	 *  - a -1000 indicates a failure to compare
	 * 
	 * @param <E> is the data type of the key
	 * @param <F> is the data type of the value
	 * @param map1 is one map
	 * @param map2 is the other map
	 * @return -1000 for failure, 0 if identical,-1 if map2 submap of map1, -2 if map1 submap of map2, or number>0 indicating the number of identical entries
	 */
	public static <E,F> int compare(final ImmutableMap<E,F> map1,final ImmutableMap<E,F> map2){
		ImmutableMap<E, F> in = intersect(map1, map2);
		if(in.size()==map1.size()){
			if(in.size()==map2.size()) return 0;
			else{ //map2>in ; can not be bigger since intersections never return bigger than the smallest
				return -2;
			}
		}
		else if(in.size()<map1.size()){
			if(in.size()==map2.size()) return -1;
//			else{// if(in.size()<map2.size()){
//				//neither map1 nor map2 are not submaps
//				return in.size();
//			}
		}
		return in.size();
	}
	
	/**
	 * Check if two maps are identical, that is, all keys of each map is present in
	 * the other and vice-versa.
	 * Values are ignored for the comparison.
	 * 
	 * @param <E> is the data type of the key
	 * @param <F> is the data type of the value
	 * @param map1 is one map
	 * @param map2 is the other map
	 * @return true if all keys of each map are present in both maps
	 */
	public static <E,F> boolean matches(final ImmutableMap<E,F> map1,final ImmutableMap<E,F> map2){
		if(compare(map1, map2)==0)return true;
		return false;
	}
}
