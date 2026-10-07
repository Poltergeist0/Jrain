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
package jrain.map.mutable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import jrain.deepCopy.DeepCopy;
import jrain.entry.mutable.Pair;

/**
 * @author poltergeist0
 * 
 * Implementation of a map that accepts multiple unique values for the same key.
 * 
 * Does not allow repetition of keys nor repetition of values inside each key.
 * 
 * Based on a {@link TreeMap} of {@link TreeSet}.
 * 
 * @param <TYPE_KEY> is the data type of the key
 * @param <TYPE_DATA> is the data type of the value
 */
/*
 * NOTE: Can not extend TreeMap<TYPE_KEY, TreeSet<TYPE_VALUE> > due to erasure.
 * The put method for a single key-value pair clashes with the put method of the
 * TreeMap that requires a key-Set<value> pair.
 */
public class TreeMultiMap<TYPE_KEY extends Object,TYPE_VALUE extends Object>{
	
	/*
	 * Storage
	 */
	private TreeMap<TYPE_KEY, TreeSet<TYPE_VALUE> > entries;
	
	/*
	 * Optimization.
	 * Stores size to prevent having to count the size of the TreeSet on each key.
	 */
	private int size;
	
	/**
	 * Default empty constructor
	 */
	public TreeMultiMap() {
		entries=new TreeMap<>();
		size=0;
	}
	
	/**
	 * Copy constructor.
	 * 
	 * @param original is the HashMultiMap to be copied
	 * @param deepCopy if true makes a deep copy of the HashMultiMap and its elements, if possible.
	 */
	public TreeMultiMap(TreeMultiMap<TYPE_KEY,TYPE_VALUE> original, boolean deepCopy){
		entries=new TreeMap<>();
		Iterator<Entry<TYPE_KEY, TreeSet<TYPE_VALUE>>> it = original.entries.entrySet().iterator();
		while (it.hasNext()) {
			Entry<TYPE_KEY, TreeSet<TYPE_VALUE>> entry = it.next();
			entries.put(entry.getKey(), (deepCopy)?DeepCopy.deepCopy(entry.getValue()):new TreeSet<>(entry.getValue()));
		}
		size=original.size;
	}
	
	/**
	 * Get the size of the HashMultiMap calculated by adding the size of the
	 * TreeSet of each key.
	 * 
	 * @return an integer holding the size
	 */
	public int size(){
		return size;
	}
	
	/**
	 * Get the size of the given key on the HashMultiMap.
	 * 
	 * @return an integer holding the size
	 */
	public int size(TYPE_KEY key){
		TreeSet<TYPE_VALUE> a = entries.get(key);
		if(a==null){
			return 0;
		}
		return a.size();
	}
	
	/**
	 * @return the entry set
	 */
	public Set<Entry<TYPE_KEY, TreeSet<TYPE_VALUE>>> entrySet(){return entries.entrySet();}
	
	/**
	 * Inefficient method the get all the entries as the list must be built on request.
	 * 
	 * @return a list with entries of all key-value pairs
	 */
	public ArrayList<Entry<TYPE_KEY,TYPE_VALUE>> entries(){
		ArrayList<Entry<TYPE_KEY,TYPE_VALUE>> a=new ArrayList<>();
		Iterator<Entry<TYPE_KEY, TreeSet<TYPE_VALUE>>> it = entries.entrySet().iterator();
		while (it.hasNext()) {
			Entry<TYPE_KEY, TreeSet<TYPE_VALUE>> entry = it.next();
			Iterator<TYPE_VALUE> itl = entry.getValue().iterator();
			while(itl.hasNext()){
				TYPE_VALUE u=itl.next();
				Entry<TYPE_KEY,TYPE_VALUE> e=new Pair<>(entry.getKey(), u);
				a.add(e);
			}
		}
		return a;
	}
	
	/**
	 * @return the key set
	 */
	public Set<TYPE_KEY> keySet(){return entries.keySet();}
	
	/**
	 * Returns the first (lowest) key currently in this map.
	 * 
	 * @return the first (lowest) key currently in this map.
	 */
	public TYPE_KEY firstKey() {return entries.firstKey();}
	
	/**
	 * Returns the first (lowest) key currently in this map.
	 * 
	 * @return the first (lowest) key currently in this map.
	 */
	public TYPE_KEY lastKey() {return entries.lastKey();}
	
	/**
	 * Inefficient as the list must be built on request.
	 * 
	 * @return a {@link TreeSet} with all unique values
	 */
	public TreeSet<TYPE_VALUE> values(){
		TreeSet<TYPE_VALUE> a=new TreeSet<>();
		Iterator<TreeSet<TYPE_VALUE>> it = entries.values().iterator();
		while(it.hasNext()){
			TreeSet<TYPE_VALUE> lst = it.next();
			a.addAll(lst);
		}
		return a;
	}
	
	/**
	 * Check if a key exists
	 * 
	 * @param key is the key
	 * @return true if it exists
	 */
	public boolean containsKey(TYPE_KEY key){
		return entries.containsKey(key);
	}
	
	/**
	 * Check if a value exists
	 * 
	 * @param val is the value
	 * @return true if it exists
	 */
	public boolean containsValue(TYPE_VALUE val){
		Iterator<TYPE_KEY> it = entries.keySet().iterator();
		while(it.hasNext()) {
			TYPE_KEY key=it.next();
			TreeSet<TYPE_VALUE> a = entries.get(key);
			if(a!=null){
				if(a.contains(val)) return true;
			}
		}
		return false;
	}

	/**
	 * Get the values associated with a key
	 * 
	 * @param key is the key
	 * @return a set with the values
	 */
	public TreeSet<TYPE_VALUE> get(TYPE_KEY key){
		TreeSet<TYPE_VALUE> a = entries.get(key);
		if(a==null){
			return new TreeSet<>();
		}
		return new TreeSet<>(a);
	}
	
	/**
	 * Get the keys associated with a value
	 * 
	 * @param value is the value
	 * @return a set with the keys
	 */
	public Set<TYPE_KEY> getKeys(TYPE_VALUE value){
		TreeSet<TYPE_KEY> a=new TreeSet<>();
		Iterator<Entry<TYPE_KEY, TreeSet<TYPE_VALUE>>> it = entries.entrySet().iterator();
		while(it.hasNext()){
			Entry<TYPE_KEY, TreeSet<TYPE_VALUE>> lst = it.next();
			if(lst.getValue().contains(value)) {
				a.add(lst.getKey());
			}
		}
		return a;
	}
	
	/**
	 * Put or replace a key-value pair in the map.
	 * 
	 * @param key is the key
	 * @param value is the value
	 * @return true if insertion succeeded
	 */
	public boolean put(TYPE_KEY key, TYPE_VALUE value){
		TreeSet<TYPE_VALUE> a=entries.get(key);
		if(a==null){//key does not exist
			a=new TreeSet<>();
			a.add(value);
			entries.put(key, a);
			++size;
		}
		else{//key exists and has values
			boolean contained=a.remove(value);//make sure the object does not exist and if it exists remove it
			a.add(value);
			entries.put(key, a);
			if(!contained)++size;
		}
		return true;
	}
	
	/**
	 * Put or replace a collection of key-value pairs in the map.
	 * 
	 * @param key is the key
	 * @param values are the values
	 * @return true if insertion succeeded
	 */
	public boolean putAll(TYPE_KEY key, Collection<TYPE_VALUE> values){
		TreeSet<TYPE_VALUE> a=entries.get(key);
		if(a==null){//key does not exist
			a=new TreeSet<>(values);
			entries.put(key, a);
			size+=values.size();
		}
		else{//key exists and has values
			size-=a.size();
			a.removeAll(values);//make sure the object does not exist and if it exists remove it
			a.addAll(values);
			entries.put(key, a);
			size+=a.size();
		}
		return true;
	}
	
	/**
	 * Put or replace a collection of key-value pairs in the map.
	 * 
	 * @param key is the key
	 * @param values are the values
	 * @return true if insertion succeeded
	 */
	public boolean putAll(TYPE_KEY key, Set<TYPE_VALUE> values){
		TreeSet<TYPE_VALUE> a=entries.get(key);
		if(a==null){//key does not exist
			a=new TreeSet<>(values);
			entries.put(key, a);
			size+=values.size();
		}
		else{//key exists and has values
			size-=a.size();
			a.removeAll(values);//make sure the object does not exist and if it exists remove it
			a.addAll(values);
			entries.put(key, a);
			size+=a.size();
		}
		return true;
	}

	/**
	 * Put or replace a collection of key-value pairs in the map.
	 * 
	 * @param key is the key
	 * @param values are the values
	 * @return true if insertion succeeded
	 */
	public boolean putAll(TreeMultiMap<TYPE_KEY , TYPE_VALUE> values){
		Iterator<Map.Entry<TYPE_KEY, TreeSet<TYPE_VALUE>>> it = values.entries.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<TYPE_KEY, TreeSet<TYPE_VALUE>> entry = it.next();
			putAll(entry.getKey(), entry.getValue());
		}
		return true;
	}
	
	/**
	 * Remove a key.
	 * 
	 * Returns an empty set if the key did not exist.
	 * 
	 * @param key is the key
	 * @return a {@link TreeSet} with the values of the key
	 */
	public TreeSet<TYPE_VALUE> remove(TYPE_KEY key){
		TreeSet<TYPE_VALUE> a = entries.remove(key);
		if(a==null) return new TreeSet<>();
		size-=a.size();
		return new TreeSet<>(a);
	}
	
	/**
	 * Remove a specific value from a given key.
	 * 
	 * @param key is the key
	 * @param value is the value
	 * @return true if the key-value pair existed
	 */
	public boolean remove(TYPE_KEY key, TYPE_VALUE value){
		TreeSet<TYPE_VALUE> a=entries.get(key);
		if(a!=null){//key exists and has values
			boolean exists=a.remove(value);//remove value
			if(a.isEmpty()){
				//remove key when list is empty
				entries.remove(key);
			}
			if(exists) --size;
			return exists;
		}
		return false;
	}
	
	public String toString(){
		StringBuilder s=new StringBuilder();
		Iterator<Map.Entry<TYPE_KEY, TreeSet<TYPE_VALUE>>> it = entries.entrySet().iterator();
		boolean first=true;
		s.append("[ ");
		while(it.hasNext()){
			if(first){
				first=false;
			}
			else{
				s.append(" , ");
			}
			Map.Entry<TYPE_KEY, TreeSet<TYPE_VALUE>> e = it.next();
			s.append(e.getKey());
			s.append(" = [ ");
			TreeSet<TYPE_VALUE> a = e.getValue();
			if(a.size()>0){
				boolean first1=true;
				Iterator<TYPE_VALUE> ita = a.iterator();
				while(ita.hasNext()){
					if(first1){
						first1=false;
					}
					else{
						s.append(" , ");
					}
					s.append(ita.next());
				}
			}
			s.append(" ]");
		}
		s.append(" ]");
		return s.toString();
	}
	
}
