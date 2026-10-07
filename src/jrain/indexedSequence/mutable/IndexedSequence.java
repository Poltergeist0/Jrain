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
package jrain.indexedSequence.mutable;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Map.Entry;
import java.util.function.Predicate;

import jrain.deepCopy.DeepCopy;
import jrain.entry.mutable.Pair;
import jrain.entry.mutable.PairOfArrayList;
import jrain.indexedSet.mutable.IndexedSet;
import jrain.arrayList.mutable.ArrayListEntry;

/**
 * @author poltergeist0
 *
 * Set where the values have an associated key.
 * This set, unlike the standard set, is indexed by key instead of by value.
 * Insertion order is preserved.
 * 
 * @param <TYPE_KEY> is the data type to be used for the key
 * @param <TYPE_DATA> is the data type to be used for the value
*/
public abstract class IndexedSequence<
	TYPE_KEY extends Object,
	TYPE_DATA extends Object
> 
extends IndexedSet<TYPE_KEY,TYPE_DATA>{
	
	/**
	 * List with the insertion order of the values.
	 * It is an array list instead of an HashMap to prevent having to 
	 * recalculate every time a column is removed from the middle.
	 */
	private ArrayList<TYPE_KEY> cellsOrder;
	

	/**
	 * Get the internal data structure that holds order of the data in objects 
	 * of this class.
	 * Useful for classes that extend {@link IndexedSequence}.
	 * 
	 * @return a {@link HashMap}
	 */
	protected ArrayList<TYPE_KEY> internalOrder(){return cellsOrder;}

	@Override
	public boolean key(TYPE_KEY oldKey,TYPE_KEY newKey){
		boolean b=super.key(oldKey, newKey);
		if(b) {
			cellsOrder.remove(oldKey);
			cellsOrder.add(newKey);
		}
		return b;
	}
	
	/**
	 * Get the key associated with a cell insertion position.
	 * Behavior is undefined when the cell does not exist.
	 * 
	 * @param pos is the insertion position of the cell
	 * @return the key of the requested cell
	 */
	public TYPE_KEY key(int pos) {
		return cellsOrder.get(pos);
	}
	
	/**
	 * Get the entry associated with a cell.
	 * Behavior is undefined when the cell does not exist.
	 * This method does not check if the cell exists to avoid duplicate checking.
	 * 
	 * @param pos is the insertion position of the cell
	 * @return the entry of the requested cell
	 */
	public Entry<TYPE_KEY, TYPE_DATA> get(int pos) {
		TYPE_KEY k=cellsOrder.get(pos);
		return new Pair<TYPE_KEY, TYPE_DATA>(k,internal().get(k));
	}
	
	public Entry<TYPE_KEY, TYPE_DATA> first() {
		TYPE_KEY k=cellsOrder.get(0);
		return new Pair<TYPE_KEY, TYPE_DATA>(k,internal().get(k));
	}
	
	public Entry<TYPE_KEY, TYPE_DATA> last() {
		TYPE_KEY k=cellsOrder.get(cellsOrder.size()-1);
		return new Pair<TYPE_KEY, TYPE_DATA>(k,internal().get(k));
	}
	
	/**
	 * Get the data associated with a cell insertion position.
	 * Behavior is undefined when the cell does not exist.
	 * 
	 * @param pos is the insertion position of the cell
	 * @return the data of the requested cell
	 */
	public TYPE_DATA value(int pos) {
		return super.value(cellsOrder.get(pos));
	}
	
	@Override
	public ArrayList<TYPE_KEY> keys(){
		return cellsOrder;
	}
	
	/**
	 * Get the index (insertion position) of the given cell or -1 if it does not exist.
	 *
	 * @return an int holding the index
	 */
	public int indexOf(TYPE_KEY k) {
		return cellsOrder.indexOf(k);		
	}
	
	@Override
	public ArrayList<Entry<TYPE_KEY,TYPE_DATA>> add(TYPE_KEY k,TYPE_DATA v,boolean overwrite){
		ArrayList<Entry<TYPE_KEY,TYPE_DATA>> b=super.add(k,v,overwrite);
		if(b.size()<=0) {//was added or overwritten
			if(!cellsOrder.contains(k)) {//if it was added
				cellsOrder.add(k);
			}//otherwise nothing to change. Entry keeps same position on list
		}
		return b;
	}
	
	@Override
	public PairOfArrayList<TYPE_KEY, Entry<TYPE_KEY, TYPE_DATA>,ArrayList<TYPE_KEY>,ArrayList<Entry<TYPE_KEY,TYPE_DATA>>> remove(TYPE_KEY k){
		PairOfArrayList<TYPE_KEY, Entry<TYPE_KEY, TYPE_DATA>,ArrayList<TYPE_KEY>,ArrayList<Entry<TYPE_KEY,TYPE_DATA>>> b=super.remove(k);
		ArrayList<Entry<TYPE_KEY,TYPE_DATA>> x=b.getValue();
		if(b.getValue().size()>0) cellsOrder.removeAll(ArrayListEntry.getKeys(x));//need only keys from entry
		return b;
	}
			
	/**
	 * Remove a cell by its insertion position.
	 * Behavior is undefined when the cell does not exist.
	 * 
	 * @param k is the position of the cell to remove
	 * @return a pair with a list of keys for the cells that failed to be 
	 * removed as the key, and a list with the removed cells as the value
	 */
	public PairOfArrayList<Integer, Entry<TYPE_KEY, TYPE_DATA>,ArrayList<Integer>,ArrayList<Entry<TYPE_KEY,TYPE_DATA>>> remove(int n) {
		ArrayList<Entry<TYPE_KEY,TYPE_DATA>> suc=new ArrayList<>();//success array
		ArrayList<Integer> fail=new ArrayList<>();//fail array
		if(n<0 || n>=cellsOrder.size()) {
			fail.add(n);
		}
		else {
			TYPE_KEY a=cellsOrder.get(n);
			PairOfArrayList<TYPE_KEY, Entry<TYPE_KEY, TYPE_DATA>,ArrayList<TYPE_KEY>,ArrayList<Entry<TYPE_KEY,TYPE_DATA>>> p = remove(a);
			if(p.getKey().size()>0)fail.add(n);
			else suc.addAll(p.getValue());
    	}
    	return new PairOfArrayList<>(fail,suc);
	}
			
	public PairOfArrayList<Integer, Entry<TYPE_KEY, TYPE_DATA>,ArrayList<Integer>,ArrayList<Entry<TYPE_KEY,TYPE_DATA>>> removeFirst() {
    	return remove(0);
	}
			
	public PairOfArrayList<Integer, Entry<TYPE_KEY, TYPE_DATA>,ArrayList<Integer>,ArrayList<Entry<TYPE_KEY,TYPE_DATA>>> removeLast() {
    	return remove(cellsOrder.size()-1);
	}
			
	/**
	 * Check if a cell exists by its insertion position.
	 * 
	 * @param k is the position for the cell
	 * @return true if the cell exists
	 */
	public boolean contains(int n) {
		if(n<0 || n>=size()) return false;
		return true;
	}
	
	/**
	 * Empty constructor
	 */
	public IndexedSequence(){
		super();
		cellsOrder=new ArrayList<TYPE_KEY>();
	}
	
	/**
	 * Copy constructor from {@link IndexedSet}
	 * 
	 * @param <TYPE_TABLE> is the data type of the class that extends {@link IndexedSet}
	 * @param t is the {@link IndexedSet} to copy
	 * @param deepCopy if true attempts to perform a deep copy of the cells
	 */
	public <TYPE_TABLE extends IndexedSet<TYPE_KEY,TYPE_DATA>> 
	IndexedSequence(TYPE_TABLE t,boolean deepCopy){
		super(t,deepCopy);
		cellsOrder=(deepCopy)?DeepCopy.deepCopy(t.keys()):new ArrayList<TYPE_KEY>(t.keys());
	}
	
	/**
	 * Copy constructor from {@link IndexedSequence}.
	 * 
	 * @param <TYPE_TABLE> is the data type of the class that extends {@link IndexedSequence}
	 * @param t is the {@link IndexedSequence} to copy
	 * @param deepCopy if true attempts to perform a deep copy of the cells
	 */
	public <TYPE_TABLE extends IndexedSequence<TYPE_KEY,TYPE_DATA>> 
	IndexedSequence(TYPE_TABLE t,boolean deepCopy){
		super(t,deepCopy);
		cellsOrder=(deepCopy)?DeepCopy.deepCopy(t.internalOrder()):new ArrayList<TYPE_KEY>(t.internalOrder());
	}
	
	/**
	 * Filtered copy constructor.
	 * 
	 * @param <TYPE_TABLE> is the data type of the class that extends {@link IndexedSequence}
	 * @param <TYPE_PREDICATE> is the data type of the class that extends {@link Predicate}
	 * @param t is the original table
	 * @param pre is the predicate that selects which cells are copied
	 * @param overwrite, if true,indicates the current values are to be replaced 
	 * with the new ones, otherwise the current values are kept.
	 * @param deepCopy if true attempts to perform a deep copy of the cells
	 */
	public <TYPE_TABLE extends IndexedSequence<TYPE_KEY,TYPE_DATA>, TYPE_PREDICATE extends Predicate<Entry<TYPE_KEY,TYPE_DATA>>> 
	IndexedSequence(
			TYPE_TABLE t,
			TYPE_PREDICATE pre,
			boolean overwrite,boolean deepCopy
			)
	{
		super();
		cellsOrder=new ArrayList<TYPE_KEY>();
		add(t, pre, overwrite,deepCopy);
	}
	
	/**
	 * @author poltergeist0
	 *
	 * {@link Iterator} for {@link IndexedSequence}
	 */
	public class IndexedSequenceIterator extends IndexedSetIterator{
		//implements Iterator<Entry<TYPE_KEY,TYPE_DATA>> {
		
		//TODO: actually use IndexedSetIterator for operations not just for return type compatibility
	      
		/**
		 * current iterator
		 */
		private Iterator<TYPE_KEY> it;
		
		/**
		 * Data referenced by the current iterator
		 */
		Entry<TYPE_KEY,TYPE_DATA> ent;
		
		/**
		 * Constructor 
		 */
		protected IndexedSequenceIterator() { 
	        it=cellsOrder.iterator();
	    } 
		
		/**
		 * Useful for classes that extend {@link IndexedSequence}.
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
	    	TYPE_KEY i = it.next();
	    	ent =new Pair<TYPE_KEY,TYPE_DATA>(i, internal().get(i));
	    	return ent;
	    } 
	      
	    /**
	     * Remove the element currently referenced by the iterator
	     */
	    public void remove() { 
	        //save current entry
	    	Entry<TYPE_KEY, TYPE_DATA> a = ent;
	    	//advance current iterator
	    	if(it.hasNext()) {
	    		TYPE_KEY i = it.next();
		    	ent =new Pair<TYPE_KEY,TYPE_DATA>(i, internal().get(i));
	    	}
	    	//removed saved entry
	    	IndexedSequence.this.remove(a.getKey());
	    } 
	} 
	
	/**
	 * @return an iterator for all the entries in the list.
	 */
	public IndexedSequenceIterator iterator() {
		return new IndexedSequenceIterator();
	}
	
	public static <TYPE_KEY,TYPE_HEADER_DATA,TYPE_ROW_DATA,TYPE_HEADER_TABLE extends IndexedSet<TYPE_KEY, TYPE_HEADER_DATA>,TYPE_ROW_TABLE extends IndexedSet<TYPE_KEY, TYPE_ROW_DATA>> 
	StringBuilder toString(StringBuilder s,TYPE_HEADER_TABLE h,TYPE_ROW_TABLE r,String sepCell, String sepValue){
		return r.toString(s, h, sepCell, sepValue);
	}
		
	public String toString(){
		StringBuilder s=new StringBuilder();
//		return toString(s,null,SEPARATOR,TaggedTableCoreCell.SEPARATOR).toString();
		return toString(s,null,separatorCell(),separatorValue()).toString();
	}

}
