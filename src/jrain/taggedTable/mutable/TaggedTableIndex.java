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
package jrain.taggedTable.mutable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import jrain.deepCopy.DeepCopy;
import jrain.identifiable.immutable.Identifiable;
import jrain.map.mutable.HashMultiMap;
import jrain.polyType.mutable.PolyType;

/**
 * @author poltergeist0
 *
 * TaggedTableIndex class.
 * 
 * Reverse lookup of row UUIDs over column values for a given column
 */
public class TaggedTableIndex implements DeepCopy<TaggedTableIndex>{//<TYPE_TAG extends Object>  {
	
	/**
	 * reverse lookup table (value, row id)
	 */
	private HashMultiMap<PolyType<?>, Identifiable> index;
	
	/**
	 * Direct lookup table (row id, value)
	 */
	private HashMap<Identifiable,PolyType<?>> index2;
	
	/**
	 * Default constructor
	 */
	public TaggedTableIndex() {
		index=new HashMultiMap<>();
		index2=new HashMap<>();
	}
	
	/**
	 * Copy constructor
	 * 
	 * @param ind is the original index
	 * @param deepCopy if true attempts to deep copy the data
	 */
	public TaggedTableIndex(final TaggedTableIndex ind,boolean deepCopy) {
		index=new HashMultiMap<>(ind.index,deepCopy);
		index2=new HashMap<>(((deepCopy)?DeepCopy.deepCopy(ind.index2):ind.index2));
	}
	
	/**
	 * Get the reverse lookup part of the index
	 * 
	 * @return a {@link HashMultiMap}
	 */
	public HashMultiMap<PolyType<?>,Identifiable> get(){
		return index;
	}
	
	/**
	 * Get rows that contain or not the given value
	 * 
	 * @param key is the value to look for
	 * @param contains if true returns the rows that contain the given value.If false, return all rows that do not contain the given value
	 * @return an {@link ArrayList} with the row {@link Identifiable}
	 */
	public ArrayList<Identifiable> get(PolyType<?> key,boolean contains){
		if(contains){
			return new ArrayList<>(index.get(key));
		}
		//else does not contain
		ArrayList<Identifiable> a=new ArrayList<>();
		Iterator<Entry<PolyType<?>, HashSet<Identifiable>>> it = index.entrySet().iterator();
		while(it.hasNext()){
			Entry<PolyType<?>, HashSet<Identifiable>> s=it.next();
			if(!s.getKey().equals(key)){
				a.addAll(s.getValue());
			}
		}
		return a;
	}
	
	/**
	 * get a subset of an index.
	 * 
	 * @param key is the value to look for
	 * @param contains if true returns the rows that contain the given value, otherwise returns the rows that do not contain the given value.
	 * @return an index with the subset values or an empty index if the subset is empty
	 */
	public TaggedTableIndex subSet(PolyType<?> key,boolean contains) {
		TaggedTableIndex ind=new TaggedTableIndex();//create new index
		if(contains){
			Set<Identifiable> set = index.get(key);
			if(set.size()>0){
				ind.index.putAll(key, set);
				Iterator<Identifiable> its = set.iterator();
				while(its.hasNext()){
					Identifiable id = its.next();
					ind.index2.put(id, key);
				}
			}
		}
		else{// does not contain
			Iterator<Entry<PolyType<?>, HashSet<Identifiable>>> it = index.entrySet().iterator();
			while(it.hasNext()){
				Entry<PolyType<?>, HashSet<Identifiable>> s=it.next();
				if(!s.getKey().equals(key)){
					HashSet<Identifiable> set = s.getValue();
					if(set.size()>0){
						ind.index.putAll(s.getKey(), set);
						Iterator<Identifiable> its = set.iterator();
						while(its.hasNext()){
							Identifiable id = its.next();
							ind.index2.put(id, s.getKey());
						}
					}

				}
			}
		}
		return ind;
	}
	
	/**
	 * Get a subset of an index using row {@link Identifiable} instead of the 
	 * values to select the entries that are included, or not, in the subset
	 * 
	 * @param values is a list of row {@link Identifiable}
	 * @param contains if true returns the subset that contains the given rows, otherwise returns the subset that does not contain the given rows.
	 * @return an index with the subset rows or an empty index if the subset is empty
	 */
	public TaggedTableIndex subSetValues(ArrayList<Identifiable> values,boolean contains) {
		TaggedTableIndex ind=new TaggedTableIndex();//create new index
		if(contains){
			Iterator<Identifiable> it = values.iterator();
			while(it.hasNext()){
				Identifiable id = it.next();
				PolyType<?> e = index2.get(id);
				if(e!=null){
					ind.index.put(e, id);
					ind.index2.put(id,e);
				}
			}
		}
		else{//does not contain values
			Iterator<Map.Entry<Identifiable, PolyType<?>>> it = index2.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry<Identifiable, PolyType<?>> id = it.next();
				if(!values.contains(id.getKey())){
					ind.index.put(id.getValue(), id.getKey());
					ind.index2.put(id.getKey(),id.getValue());
				}
			}
		}
		return ind;
	}
	
	/**
	 * Add a row to a given value
	 * 
	 * @param key is the value
	 * @param id is the row {@link Identifiable}
	 */
	public boolean add(PolyType<?> key, Identifiable id) {
		boolean b=index.put(key, id);
		index2.put(id, key);
		return b;
	}

	/**
	 * Add an index to the current index
	 * 
	 * @param ind is the index to add
	 * @return true if the index was successfully added
	 */
	public boolean add(TaggedTableIndex ind) {
		boolean b=index.putAll(ind.index);
		index2.putAll(ind.index2);
		return b;
	}

	/**
	 * Remove a row with a given value
	 * 
	 * @param key is the value
	 * @param id is the row {@link Identifiable}
	 */
	public boolean remove(PolyType<?> key, Identifiable id) {
		boolean b=index.remove(key, id);
		index2.remove(id);
		return b;
	}

	/**
	 * Remove all row-value pairs given in the passed index
	 * 
	 * @param ind is the index with all the row-value pairs to remove
	 */
	public void remove(TaggedTableIndex ind) {
		Iterator<Entry<PolyType<?>, Identifiable>> it = ind.index.entries().iterator();
		while(it.hasNext()){
			Entry<PolyType<?>,Identifiable> a = it.next();
			PolyType<?> s=a.getKey();
			Identifiable u=a.getValue();
			index.remove(s,u);
			index2.remove(u,s);
		}
	}

	/**
	 * Remove row from index independently of the value
	 * 
	 * @param id is the row {@link Identifiable}
	 * @return true if the row existed and was removed
	 */
	public boolean remove(Identifiable id) {
		PolyType<?> s=index2.remove(id);
		if(s!=null){
			index.remove(s, id);
			return true;
		}
		return false;
	}

	/**
	 * Moves a row from an old value to a new value. 
	 * The row is added to the new value independently of it existing in the old 
	 * value.
	 * 
	 * @param keyOld is the old value
	 * @param keyNew is the new value
	 * @param id is the row {@link Identifiable}
	 * @return true if the row was added to the new value
	 */
	public boolean update(PolyType<?> keyOld, PolyType<?> keyNew, Identifiable id) {
		//TODO return false when value does not exist
		index.remove(keyOld, id);
		index.put(keyNew, id);
		index2.put(id, keyNew);
		return true;
	}

	/**
	 * @return the size of the index
	 */
	public int size(){
		return index.size();
	}

	/**
	 * Get the size of the subset of the index composed of the given value
	 * 
	 * @param key is the value
	 * @return the size of the subset
	 */
	public int size(PolyType<?> key){
		return index.get(key).size();
	}

	/**
	 * @return a list with all unique values
	 */
	public ArrayList<PolyType<?>> keys(){
		return new ArrayList<PolyType<?>>(index.keySet());
	}

	/**
	 * @return a list with all unique row {@link Identifiable}
	 */
	public ArrayList<Identifiable> values(){
		return new ArrayList<Identifiable>(index.values());
	}

	public String toString(){return index.toString();}

	@Override
	public TaggedTableIndex deepCopy() {
		return new TaggedTableIndex(this,true);
	}
}
