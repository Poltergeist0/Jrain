package jrain.map.mutable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import jrain.deepCopy.DeepCopy;
import jrain.entry.mutable.Pair;

import java.util.Set;

/**
 * @author poltergeist0
 * 
 * Implementation of a map that accepts multiple unique values for the same key.
 * 
 * Does not allow repetition of keys nor repetition of values inside each key.
 * 
 * Based on a {@link HashMap} of {@link HashSet}.
 * 
 * @param <TYPE_KEY> is the data type of the key
 * @param <TYPE_DATA> is the data type of the value
 */
/*
 * NOTE: Can not extend HashMap<TYPE_KEY, HashSet<TYPE_VALUE> > due to erasure.
 * The put method for a single key-value pair clashes with the put method of the
 * HashMap that requires a key-Set<value> pair.
 */
public class HashMultiMap<TYPE_KEY extends Object,TYPE_VALUE extends Object>{
	
	/*
	 * Storage
	 */
	private HashMap<TYPE_KEY, HashSet<TYPE_VALUE> > entries;
	
	/*
	 * Optimization.
	 * Stores size to prevent having to count the size of the HashSet on each key.
	 */
	private int size;
	
	/**
	 * Default empty constructor
	 */
	public HashMultiMap() {
		entries=new HashMap<>();
		size=0;
	}
	
	/**
	 * Copy constructor.
	 * 
	 * @param original is the HashMultiMap to be copied
	 * @param deepCopy if true makes a deep copy of the HashMultiMap and its elements, if possible.
	 */
	public HashMultiMap(HashMultiMap<TYPE_KEY,TYPE_VALUE> original, boolean deepCopy){
		entries=new HashMap<>();
		Iterator<Entry<TYPE_KEY, HashSet<TYPE_VALUE>>> it = original.entries.entrySet().iterator();
		while (it.hasNext()) {
			Entry<TYPE_KEY, HashSet<TYPE_VALUE>> entry = it.next();
			entries.put(entry.getKey(), (deepCopy)?DeepCopy.deepCopy(entry.getValue()):new HashSet<>(entry.getValue()));
		}
		size=original.size;
	}
	
	/**
	 * Get the size of the HashMultiMap calculated by adding the size of the
	 * HashSet of each key.
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
		HashSet<TYPE_VALUE> a = entries.get(key);
		if(a==null){
			return 0;
		}
		return a.size();
	}
	
	/**
	 * @return the entry set
	 */
	public Set<Entry<TYPE_KEY, HashSet<TYPE_VALUE>>> entrySet(){return entries.entrySet();}
	
	/**
	 * Inefficient method the get all the entries as the list must be built on request.
	 * 
	 * @return a list with entries of all key-value pairs
	 */
	public ArrayList<Entry<TYPE_KEY,TYPE_VALUE>> entries(){
		ArrayList<Entry<TYPE_KEY,TYPE_VALUE>> a=new ArrayList<>();
		Iterator<Entry<TYPE_KEY, HashSet<TYPE_VALUE>>> it = entries.entrySet().iterator();
		while (it.hasNext()) {
			Entry<TYPE_KEY, HashSet<TYPE_VALUE>> entry = it.next();
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
	 * Inefficient as the list must be built on request.
	 * 
	 * @return a {@link HashSet} with all unique values
	 */
	public Set<TYPE_VALUE> values(){
		HashSet<TYPE_VALUE> a=new HashSet<>();
		Iterator<HashSet<TYPE_VALUE>> it = entries.values().iterator();
		while(it.hasNext()){
			HashSet<TYPE_VALUE> lst = it.next();
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
			HashSet<TYPE_VALUE> a = entries.get(key);
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
	public Set<TYPE_VALUE> get(TYPE_KEY key){
		HashSet<TYPE_VALUE> a = entries.get(key);
		if(a==null){
			return new HashSet<>();
		}
		return new HashSet<>(a);
	}
	
	/**
	 * Get the keys associated with a value
	 * 
	 * @param value is the value
	 * @return a set with the keys
	 */
	public Set<TYPE_KEY> getKeys(TYPE_VALUE value){
		HashSet<TYPE_KEY> a=new HashSet<>();
		Iterator<Entry<TYPE_KEY, HashSet<TYPE_VALUE>>> it = entries.entrySet().iterator();
		while(it.hasNext()){
			Entry<TYPE_KEY, HashSet<TYPE_VALUE>> lst = it.next();
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
		HashSet<TYPE_VALUE> a=entries.get(key);
		if(a==null){//key does not exist
			a=new HashSet<>();
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
		HashSet<TYPE_VALUE> a=entries.get(key);
		if(a==null){//key does not exist
			a=new HashSet<>(values);
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
		HashSet<TYPE_VALUE> a=entries.get(key);
		if(a==null){//key does not exist
			a=new HashSet<>(values);
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
	public boolean putAll(HashMultiMap<TYPE_KEY , TYPE_VALUE> values){
		Iterator<Map.Entry<TYPE_KEY, HashSet<TYPE_VALUE>>> it = values.entries.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<TYPE_KEY, HashSet<TYPE_VALUE>> entry = it.next();
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
	 * @return a {@link HashSet} with the values of the key
	 */
	public Set<TYPE_VALUE> remove(TYPE_KEY key){
		HashSet<TYPE_VALUE> a = entries.remove(key);
		if(a==null) return new HashSet<>();
		size-=a.size();
		return new HashSet<>(a);
	}
	
	/**
	 * Remove a specific value from a given key.
	 * 
	 * @param key is the key
	 * @param value is the value
	 * @return true if the key-value pair existed
	 */
	public boolean remove(TYPE_KEY key, TYPE_VALUE value){
		HashSet<TYPE_VALUE> a=entries.get(key);
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
		Iterator<Map.Entry<TYPE_KEY, HashSet<TYPE_VALUE>>> it = entries.entrySet().iterator();
		boolean first=true;
		s.append("[ ");
		while(it.hasNext()){
			if(first){
				first=false;
			}
			else{
				s.append(" , ");
			}
			Map.Entry<TYPE_KEY, HashSet<TYPE_VALUE>> e = it.next();
			s.append(e.getKey());
			s.append(" = [ ");
			HashSet<TYPE_VALUE> a = e.getValue();
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