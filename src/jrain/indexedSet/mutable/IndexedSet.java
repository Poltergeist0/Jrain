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
package jrain.indexedSet.mutable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

import jrain.deepCopy.DeepCopy;
import jrain.entry.mutable.Pair;
import jrain.entry.mutable.PairOfArrayList;

/**
 * @author poltergeist0
 *
 * Set where the values have an associated key.
 * This set, unlike the standard set, is indexed by key instead of by value.
 * Insertion order is not preserved.
 * 
 * @param <TYPE_KEY> is the data type to be used for the key
 * @param <TYPE_DATA> is the data type to be used for the value
*/
public abstract class IndexedSet<
	TYPE_KEY extends Object,
	TYPE_DATA extends Object
> implements Iterable<Entry<TYPE_KEY,TYPE_DATA> >,DeepCopy<IndexedSet<TYPE_KEY,TYPE_DATA>>{
	
	/**
	 * Get the separator, used in the toString method to separate the key and 
	 * value, via a method so that it can be overridden in classes that extend 
	 * this one.
	 * 
	 * @return the separator
	 */
	public abstract String separatorValue();
	
	/**
	 * Get the separator, used in the toString method to separate different cells,
	 * via a method so that it can be overridden in classes that extend this one.
	 * 
	 * @return the separator
	 */
	public abstract String separatorCell();
	
	/**
	 * Rename a key.
	 * 
	 * @param oldKey is the current key of the cell
	 * @param newKey is the new key for the cell
	 * @return true is the key was changed. False if the cell does not exist or 
	 * a cell already exists with the new key.
	 */
	public boolean key(TYPE_KEY oldKey,TYPE_KEY newKey){
		if(!cells.containsKey(oldKey)) return false;
		if(cells.containsKey(newKey)) return false;
		TYPE_DATA a=cells.remove(oldKey);
		cells.put(newKey, a);
		return true;
	}
	
	/**
	 * Get the entry associated with a cell.
	 * Behavior is undefined when the cell does not exist.
	 * This method does not check if the cell exists to avoid double checking.
	 * 
	 * @param k is the key of the cell
	 * @return the entry of the requested cell
	 */
	public Entry<TYPE_KEY, TYPE_DATA> get(TYPE_KEY k) {
		return new Pair<TYPE_KEY, TYPE_DATA>(k,cells.get(k));
	}
	
	/**
	 * Get the value associated with a cell.
	 * Behavior is undefined when the cell does not exist.
	 * This method does not check if the cell exists to avoid duplicate checking.
	 * 
	 * @param k is the key of the cell
	 * @return the value of the requested cell
	 */
	public TYPE_DATA value(TYPE_KEY k) {
		return cells.get(k);
	}
	
	/**
	 * Change the value of a cell.
	 * 
	 * @param v is the new value for the cell
	 * @return true if value was changed or false if the cell does not exist
	 */
	public boolean value(TYPE_KEY k,TYPE_DATA v){
		if(!cells.containsKey(k)) return false;
		cells.put(k, v);
		return true;
	}
	
	/**
	 * @return the number of cells
	 */
	public int size(){return cells.size();}
	
	/**
	 * @return the list of keys of the cells
	 */
	public ArrayList<TYPE_KEY> keys(){
		return new ArrayList<>(cells.keySet());
	}
	
	/**
	 * @return an iterator for all the entries in the list.
	 */
	public IndexedSetIterator iterator() {
		return new IndexedSetIterator();
	}
	
	/**
	 * Adds a cell, if it does not exist.
	 * If the cell exists and overwrite is true, replaces the value, 
	 * otherwise ignores.
	 * 
	 * @param cellName is the key of the cell
	 * @param cellValue is the value of the cell
	 * @param overwrite , if true,indicates the current values are to be replaced 
	 * with the new ones, otherwise the current values are kept.
	 * @return a list with the cells that failed to be added/replaced
	 */
	public ArrayList<Entry<TYPE_KEY,TYPE_DATA>> add(TYPE_KEY k,TYPE_DATA v,boolean overwrite){
		ArrayList<Entry<TYPE_KEY,TYPE_DATA> > fail=new ArrayList<>();
		if(cells.containsKey(k)){
			if(overwrite){
				cells.put(k, v);
			}
			else {
				fail.add(new Pair<>(k,v));
			}
		}
		else{
			cells.put(k, v);
		}
		return fail;
	}
	
	/**
	 * Adds a cell, if it does not exist.
	 * If the cell exists and overwrite is true, replaces the value, 
	 * otherwise ignores.
	 * 
	 * @param <U> is the data type of the class that extends {@link Entry}
	 * @param e is the cell to add
	 * @param overwrite , if true,indicates the current values are to be replaced 
	 * with the new ones, otherwise the current values are kept.
	 * @return a list with the cells that failed to be added/replaced
	 */
	public <U extends Entry<TYPE_KEY,TYPE_DATA>> ArrayList<Entry<TYPE_KEY,TYPE_DATA>> add(U e,boolean overwrite){
		return add(e.getKey(),e.getValue(),overwrite);
	}
	
	/**
	 * Adds cells, that do not exist.
	 * If a cell exists and overwrite is true, replaces the value, 
	 * otherwise ignores.
	 * 
	 * @param <U> is the data type of the class that extends {@link IndexedSet}
	 * @param t are the cells to add
	 * @param overwrite, if true,indicates the current values are to be replaced 
	 * with the new ones, otherwise the current values are kept.
	 * @return a list of the cells that could not be added/replaced
	 */
	public <U extends IndexedSet<TYPE_KEY,TYPE_DATA>> 
	ArrayList<Entry<TYPE_KEY,TYPE_DATA>> add(U t,boolean overwrite){
		ArrayList<Entry<TYPE_KEY,TYPE_DATA>> fail=new ArrayList<>();
		IndexedSetIterator it = t.iterator();
		while(it.hasNext()){
			Entry<TYPE_KEY, TYPE_DATA> i = it.next();
			fail.addAll(add(i, overwrite));
		}
		return fail;
	}
		
	/**
	 * Adds a {@link Collection} of cells.
	 * If a cell exists and overwrite is true, replaces the value, 
	 * otherwise ignores.
	 * 
	 * @param <U> is the data type of the class that extends {@link Entry}
	 * @param <V> is the data type of the class that extends {@link Collection}
	 * @param c are the cells to add
	 * @param overwrite, if true,indicates the current values are to be replaced 
	 * with the new ones, otherwise the current values are kept.
	 * @return a list of the cells that could not be added/replaced
	 */
	public <U extends Entry<TYPE_KEY,TYPE_DATA>, V extends Collection<U> > 
	ArrayList<Entry<TYPE_KEY,TYPE_DATA>> add(V c,boolean overwrite){
		ArrayList<Entry<TYPE_KEY,TYPE_DATA>> fail=new ArrayList<>();
		Iterator<U> it = c.iterator();
		while(it.hasNext()){
			U i = it.next();
			fail.addAll(add(i, overwrite));
		}
		return fail;
	}
		
	/**
	 * Filtered addition of cells from a {@link IndexedSet}.
	 * Only attempts to add cells that pass the {@link Predicate}.
	 * If the cell exists and overwrite is true, replaces the value, 
	 * otherwise ignores.
	 * 
	 * @param <TYPE_TABLE> is the data type of the class that extends this class
	 * @param <TYPE_PREDICATE> is the data type of the class that extends {@link Predicate}
	 * @param t is the {@link IndexedSet}
	 * @param pre is the {@link Predicate}
	 * @param overwrite , if true,indicates the current values are to be replaced 
	 * with the new ones, otherwise the current values are kept.
	 * @return a list of the cells that passed the test but could not be added/replaced
	 */
	public <TYPE_TABLE extends IndexedSet<TYPE_KEY,TYPE_DATA>, TYPE_PREDICATE extends Predicate<Entry<TYPE_KEY,TYPE_DATA>>> 
	ArrayList<Entry<TYPE_KEY,TYPE_DATA> > add(
			TYPE_TABLE t,
			TYPE_PREDICATE pre,
			boolean overwrite
			)
	{
		return add(t,pre,overwrite,false);
	}
	
	/**
	 * Filtered addition of cells from a {@link IndexedSet}.
	 * Only attempts to add cells that pass the {@link Predicate}.
	 * If the cell exists and overwrite is true, replaces the value, 
	 * otherwise ignores.
	 * 
	 * @param <TYPE_TABLE> is the data type of the class that extends this class
	 * @param <TYPE_PREDICATE> is the data type of the class that extends {@link Predicate}
	 * @param t is the {@link IndexedSet}
	 * @param pre is the {@link Predicate}
	 * @param overwrite, if true,indicates the current values are to be replaced 
	 * with the new ones, otherwise the current values are kept.
	 * @param deepCopy if true attempts to perform a deep copy of the cells
	 * @return a list of the cells that passed the test but could not be added/replaced
	 */
	public <TYPE_TABLE extends IndexedSet<TYPE_KEY,TYPE_DATA>, TYPE_PREDICATE extends Predicate<Entry<TYPE_KEY,TYPE_DATA>>> 
	ArrayList<Entry<TYPE_KEY,TYPE_DATA>> add(
			TYPE_TABLE t,
			TYPE_PREDICATE pre,
			boolean overwrite,boolean deepCopy
			)
	{
		ArrayList<Entry<TYPE_KEY,TYPE_DATA>> fail=new ArrayList<>();
		IndexedSetIterator it = t.iterator();
		while(it.hasNext()){
			Entry<TYPE_KEY, TYPE_DATA> i = it.next();
			if(pre==null || pre.test(i)) {
				fail.addAll(add((deepCopy)?DeepCopy.deepCopy(i):i, overwrite));
			}
		}
		return fail;
	}
	
	/**
	 * Remove a cell
	 * 
	 * @param k is the key of the cell to remove
	 * @return a pair with a list of keys for the cells that failed to be 
	 * removed as the key, and a list with the removed cells as the value
	 */
	public PairOfArrayList<TYPE_KEY, Entry<TYPE_KEY,TYPE_DATA>,ArrayList<TYPE_KEY>,ArrayList<Entry<TYPE_KEY,TYPE_DATA>> > remove(TYPE_KEY k){
		PairOfArrayList<TYPE_KEY, Entry<TYPE_KEY,TYPE_DATA>,ArrayList<TYPE_KEY>,ArrayList<Entry<TYPE_KEY,TYPE_DATA>> > r=new PairOfArrayList<>();
		if(!cells.containsKey(k)) {
			r.add(k, null);
		}
		else {
			r.add(null,new Pair<>(k, cells.remove(k)));
		}
		return r;
	}
			
	/**
	 * Remove cells given by a {@link Collection}.
	 * Returns a pair with a list of keys for the cells that failed to be 
	 * removed as the key, and a list with the removed cells as the value.
	 * 
	 * @param <U> is the data type of the class that extends {@link Collection}
	 * @param c is the list of cells to remove
	 * @return a pair with a list of keys for the cells that failed to be 
	 * removed as the key, and a list with the removed cells as the value
	 */
	public <U extends Collection<TYPE_KEY> > PairOfArrayList<TYPE_KEY, Entry<TYPE_KEY,TYPE_DATA>,ArrayList<TYPE_KEY>,ArrayList<Entry<TYPE_KEY,TYPE_DATA>> > remove(U c){
		PairOfArrayList<TYPE_KEY, Entry<TYPE_KEY,TYPE_DATA>,ArrayList<TYPE_KEY>,ArrayList<Entry<TYPE_KEY,TYPE_DATA>> > r=new PairOfArrayList<>();
		Iterator<TYPE_KEY> it = c.iterator();
		while(it.hasNext()){
			TYPE_KEY k = it.next();
			PairOfArrayList<TYPE_KEY, Entry<TYPE_KEY,TYPE_DATA>,ArrayList<TYPE_KEY>,ArrayList<Entry<TYPE_KEY,TYPE_DATA>> > bb=remove(k);
			r.add(bb);
		}
		return r;
	}
	
	/**
	 * Remove cells given by a {@link IndexedSet}.
	 * Returns a pair with a list of keys for the cells that failed to be 
	 * removed as the key, and a list with the removed cells as the value.
	 * 
	 * @param <TYPE_TABLE> is the data type of the class that extends {@link IndexedSet}
	 * @param table is the list of cells to remove
	 * @return a pair with a list of keys for the cells that failed to be 
	 * removed as the key, and a list with the removed cells as the value
	 */
	public <TYPE_TABLE extends IndexedSet<TYPE_KEY,TYPE_DATA> > 
	PairOfArrayList<TYPE_KEY, Entry<TYPE_KEY,TYPE_DATA>,ArrayList<TYPE_KEY>,ArrayList<Entry<TYPE_KEY,TYPE_DATA>> > remove(TYPE_TABLE table){
		PairOfArrayList<TYPE_KEY, Entry<TYPE_KEY,TYPE_DATA>,ArrayList<TYPE_KEY>,ArrayList<Entry<TYPE_KEY,TYPE_DATA>> > r=new PairOfArrayList<>();
		IndexedSetIterator it = table.iterator();
		while(it.hasNext()){
			Entry<TYPE_KEY, TYPE_DATA> n = it.next();
			TYPE_KEY k = n.getKey();
			PairOfArrayList<TYPE_KEY, Entry<TYPE_KEY,TYPE_DATA>,ArrayList<TYPE_KEY>,ArrayList<Entry<TYPE_KEY,TYPE_DATA>> > bb=remove(k);
			r.add(bb);
		}
		return r;
	}
	
	/**
	 * Remove rows that pass a {@link Predicate}.
	 * 
	 * @param <TYPE_PREDICATE> is the data type of the class that extends {@link Predicate}
	 * @param pre is the {@link Predicate}
	 * @return a pair with a list of keys for the cells that failed to be 
	 * removed as the key, and a list with the removed cells as the value
	 */
	public <TYPE_PREDICATE extends Predicate<Entry<TYPE_KEY,TYPE_DATA>>> 
	PairOfArrayList<TYPE_KEY, Entry<TYPE_KEY,TYPE_DATA>,ArrayList<TYPE_KEY>,ArrayList<Entry<TYPE_KEY,TYPE_DATA>> > remove(TYPE_PREDICATE pre){
		ArrayList<TYPE_KEY> r=new ArrayList<>();
		IndexedSetIterator it = iterator();
		while(it.hasNext()){
			Entry<TYPE_KEY, TYPE_DATA> k = it.next();
			if(pre.test(k)) {//passed test
				r.add(k.getKey());//add to list to remove
			}
		}
		return remove(r);//remove rows with given keys and return the rows
	}
	
	/**
	 * Check if a cell exists.
	 * 
	 * @param k is the key for the cell
	 * @return true if the cell exists
	 */
	public boolean contains(TYPE_KEY k){
		return cells.containsKey(k);
	}

	/**
	 * Get the common keys between two lists of cells, with the default values 
	 * given by the base.
	 * If base is null returns an empty header.
	 * If base is not null and additional is null returns an empty header.
	 * Otherwise returns a copy of the common cells between base and additional.
	 * 
	 * @param <TYPE_KEY> is the data type to be used for the tag/name of all cells
	 * @param <TYPE_DATA> is the data type to be used for the value of all cells
	 * @param <TYPE_CELL> is the data type of the class that extends {@link Pair}
	 * @param <TYPE_TABLE> is the data type of the class that extends {@link IndexedSet}
	 * @param base is the header used as source of default values
	 * @param additional is the header used to search for common cells
	 * @return a new header with the common cells
	 */
	public static <TYPE_KEY extends Object, TYPE_DATA extends Object,TYPE_CELL extends Pair<TYPE_KEY,TYPE_DATA>, TYPE_TABLE extends IndexedSet<TYPE_KEY,TYPE_DATA> > 
	ArrayList<TYPE_KEY> commonKeys(
			TYPE_TABLE base,
			TYPE_TABLE additional
			)
	{
		ArrayList<TYPE_KEY> h=new ArrayList<TYPE_KEY>();
		if(base!=null) {
			if(additional==null) {
				//nothing to do
			}
			else {
				Iterator<TYPE_KEY> it = base.keys().iterator();
				while(it.hasNext()) {
					TYPE_KEY i = it.next();
					if(additional.contains(i)) {
						h.add(DeepCopy.deepCopy(i));
					}
				}
			}
		}
		return h;
	}
	
	/**
	 * Create an empty {@link IndexedSet}
	 */
	public IndexedSet(){
		cells=new HashMap<TYPE_KEY,TYPE_DATA>();
	}
	
	/**
	 * Copy constructor.
	 * 
	 * @param <TYPE_SET> is the data type of the class that extends {@link IndexedSet}
	 * @param t is the original {@link IndexedSet}
	 * @param deepCopy if true attempts to perform a deep copy of the cells
	 */
	public <TYPE_SET extends IndexedSet<TYPE_KEY,TYPE_DATA>> 
	IndexedSet(TYPE_SET t,boolean deepCopy){
		cells=(deepCopy)?DeepCopy.deepCopy(t.internal()):new HashMap<TYPE_KEY,TYPE_DATA>(t.internal());
	}
	
	/**
	 * Filtered copy constructor.
	 * Uses a given {@link IndexedSet} as source for data and only adds 
	 * cells that pass the predicate test
	 * 
	 * @param <TYPE_SET> is the data type of the class that extends {@link IndexedSet}
	 * @param <TYPE_PREDICATE> is the data type of the class that extends {@link Predicate}
	 * @param t is the original {@link IndexedSet}
	 * @param pre is the predicate that selects which rows are copied
	 * @param deepCopy if true attempts to perform a deep copy of the cells
	 */
	public <
		TYPE_SET extends IndexedSet<TYPE_KEY,TYPE_DATA>, 
		TYPE_PREDICATE extends Predicate<Entry<TYPE_KEY,TYPE_DATA> >
	> 
	IndexedSet(
			TYPE_SET t,
			TYPE_PREDICATE pre,
			boolean deepCopy
			)
	{
		cells=new HashMap<TYPE_KEY,TYPE_DATA>();
		add(t, pre, false,deepCopy);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public abstract int hashCode();

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public abstract boolean equals(Object obj);
	
	public static <TYPE_KEY,TYPE_HEADER_DATA,TYPE_ROW_DATA,TYPE_HEADER_TABLE extends IndexedSet<TYPE_KEY, TYPE_HEADER_DATA>,TYPE_ROW_TABLE extends IndexedSet<TYPE_KEY, TYPE_ROW_DATA>> 
	StringBuilder toString(StringBuilder s,TYPE_HEADER_TABLE h,TYPE_ROW_TABLE r, String sepCell, String sepValue){
		return r.toString(s, h, sepCell, sepValue);
	}
		
	/**
	 * 
	 * @param <TYPE_HEADER_DATA> must be either a primitive value or a class extending TYPE_DATA
	 * @param <TYPE_HEADER_CELL>
	 * @param <TYPE_HEADER_TABLE>
	 * @param s is a {@link StringBuilder}
	 * @param h
	 * @param sepCell
	 * @param sepValue
	 * @return the given {@link StringBuilder}
	 */
	public <TYPE_HEADER_DATA,TYPE_HEADER_TABLE extends IndexedSet<TYPE_KEY, TYPE_HEADER_DATA> > 
	StringBuilder toString(StringBuilder s,TYPE_HEADER_TABLE h, String sepCell, String sepValue){
		boolean first=true;
		if(h==null) {
			Iterator<Entry<TYPE_KEY, TYPE_DATA>> it = iterator();
			while(it.hasNext()) {
				if(first) {
					first=false;
				}
				else {
					if(sepCell==null) s.append(SEPARATOR);
					else s.append(sepCell);
				}
				Entry<TYPE_KEY, TYPE_DATA> a = it.next();
				Pair.toString(s,a.getKey(),a.getValue(),sepValue);
			}
		}
		else {
			Iterator<Entry<TYPE_KEY, TYPE_HEADER_DATA>> it = h.iterator();
			while(it.hasNext()) {
				if(first) {
					first=false;
				}
				else {
					if(sepCell==null) s.append(SEPARATOR);
					else s.append(sepCell);
				}
				Entry<TYPE_KEY, TYPE_HEADER_DATA> a = it.next();
				if(contains(a.getKey())) {
					Pair.toString(s,a.getKey(),value(a.getKey()),sepValue);
				}
				else {
					TYPE_HEADER_DATA v = a.getValue();
//					if(Value.class.isInstance(v)) {//TYPE_HEADER_DATA is a class
//						@SuppressWarnings("unchecked")
//						Value<Object> vv=(Value<Object>) v;
//						Pair.toString(s,a.getKey(),vv.value(),sepValue);//call its value() method
//					}
//					else {//TYPE_HEADER_DATA is a value
//						Pair.toString(s,a.getKey(),v,sepValue);//use directly
//					}
					Pair.toString(s,a.getKey(),typeDataGetValue(v),sepValue);
				}
			}
		}
		return s;
	}
		
	public String toString(){
		StringBuilder s=new StringBuilder();
		return toString(s,null,separatorCell(),separatorValue()).toString();
	}
		
	/**
	 * @author poltergeist0
	 *
	 * {@link Iterator} for {@link IndexedSet}
	 */
	public class IndexedSetIterator implements Iterator<Entry<TYPE_KEY,TYPE_DATA>> { 
	      
		/**
		 * current iterator
		 */
		private Iterator<Entry<TYPE_KEY,TYPE_DATA>> it;
		
		/**
		 * Data referenced by the current iterator
		 */
		Entry<TYPE_KEY,TYPE_DATA> ent;
		
		/**
		 * Constructor 
		 */
		protected IndexedSetIterator() { 
	        it=cells.entrySet().iterator();
	    } 
		
		/**
		 * Useful for classes that extend {@link IndexedSet}.
		 * 
		 * @return the data referenced by the current iterator
		 */
		protected Entry<TYPE_KEY,TYPE_DATA> current() {return ent;}
	      
	    /**
	     * Check if the next element exists 
	     */
	    public boolean hasNext() {
	    	return it.hasNext();
	    } 
	      
	    /**
	     * Move the iterator to the next
	     */
	    public Entry<TYPE_KEY,TYPE_DATA> next() {
	    	if(!it.hasNext()) throw new NoSuchElementException();
	    	ent = it.next();
	    	return ent;
	    } 
	      
	    /**
	     * Remove the element currently referenced by the iterator
	     */
	    public void remove() { 
	        //save current entry
	    	Entry<TYPE_KEY, TYPE_DATA> a = ent;
	    	//advance current iterator
	    	if(it.hasNext()) ent=it.next();
	    	//removed saved entry
	    	IndexedSet.this.remove(a.getKey());
	    } 
	} 
	
	/**
	 * Get the internal data structure that holds all the data in objects of this class
	 * Useful for classes that extend {@link IndexedSet}.
	 * 
	 * @return a {@link HashMap}
	 */
	protected HashMap<TYPE_KEY,TYPE_DATA > internal(){return cells;}

	/**
	 * Define how to get TYPE_DATA from the given object.
	 * The given object must extend TYPE_DATA.
	 * 
	 * Example for simple data:
	 * {@code
	 * 		...
	 * 		IndexedSet<Integer, Integer> bla;
	 * 		...
	 * 		SomeMethod(){
	 * 			...
	 * 			Entry<Integer, Integer> a=get(someKey);
	 * 			...
	 * 			typeDataGetValue(a.value());
	 * 			...
	 * 		}
	 * 		...
	 * 		protected Integer typeDataGetValue(Object v){return (Integer)v;}
	 * 		...
	 * }
	 * 
	 * TODO: review this example. It is confusing. Entry does not extend integer.
	 * Example for complex data:
	 * {@code
	 * 		...
	 * 		IndexedSet<Integer, Entry<Byte,Integer> > bla;
	 * 		...
	 * 		SomeMethod(){
	 * 			...
	 * 			Entry<Integer, Entry<Byte,Integer> > a=get(someKey);
	 * 			...
	 * 			typeDataGetValue(a.value());
	 * 			...
	 * 		}
	 * 		...
	 * 		protected Integer typeDataGetValue(Object v){return ((Entry<Byte,Integer>)v).value();}
	 * 		...
	 * }
	 * 
	 * @param v is the object from which to take the value
	 * @return the value
	 */
	protected abstract TYPE_DATA typeDataGetValue(Object v);
	
	/**
	 * Separator used in the toString method to separate cells.
	 */
	public final static String SEPARATOR=";";

	/**
	 * List of cells.
	 */
	private HashMap<TYPE_KEY,TYPE_DATA > cells;
	
}
