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
package jrain.taggedTableSimple.mutable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.function.Predicate;

import jrain.taggedTableCore.mutable.TaggedTableCore;
import jrain.taggedTableCore.mutable.TaggedTableCoreRows;
import jrain.indexedSet.mutable.IndexedSet;
import jrain.taggedTableHashcode.mutable.TaggedTableHashcodeHeader;
import jrain.entry.mutable.PairOfArrayList;

/**
 * @author poltergeist0
 *
 * Set of rows for use with {@link TaggedTableSimple}.
 * It is composed of columns that have tags/names associated with them. 
 * Those are used to address each column, so no duplicates are allowed. 
 * Columns are described in {@link TaggedTableHashcodeHeader}. Any column present in the
 * {@code TaggedTableHeader} that does not exist in a row is assumed to have the 
 * default value declared in {@code TaggedTableHeader}.
 * If the data type of the tags/names is String, these are case sensitive.
 * The {@code null} value is accepted either as tag/name or value.
 * Column order is not preserved.
 * 
 * @param <TYPE_KEY> is the data type to be used for the key of rows
 * @param <TYPE_TAG> is the data type to be used for the tag/name of all columns
 * @param <TYPE_DATA> is the data type to be used for the value of all columns
 * @param <TYPE_ROW> is the data type to be used for the rows
 */
public class TaggedTableSimpleRows
<
TYPE_KEY extends Object,TYPE_TAG extends Object,TYPE_DATA extends Object,
TYPE_ROW extends TaggedTableSimpleRow<TYPE_TAG,TYPE_DATA>
> 
extends TaggedTableCoreRows<TYPE_KEY, TYPE_TAG, TYPE_DATA, TYPE_ROW>{

	/**
	 * Create a new simple row set
	 */
	public TaggedTableSimpleRows(){
		super();
	}
		
	/**
	 * Copy constructor
	 * 
	 * @param <TYPE_TABLE> is the data type of the class that extends {@link TaggedTableCoreRows}
	 * @param original is the original {@link TaggedTableCoreRows}
	 * @param deepCopy if true attempts to perform a deep copy of the cells
	 */
	public <TYPE_TABLE extends TaggedTableSimpleRows<TYPE_KEY,TYPE_TAG,TYPE_DATA,TYPE_ROW>> 
	TaggedTableSimpleRows(TYPE_TABLE original,boolean deepCopy){
		super(original,deepCopy);
	}
	
	/**
	 * Filtered copy constructor.
	 * Only copies rows that pass the predicate test.
	 * 
	 * @param <TYPE_TABLE> is the data type of the class that extends {@link TaggedTableSimpleRows}
	 * @param <P> is the data type of the class that extends {@link Predicate}
	 * @param t is the original {@link TaggedTableSimpleRows}
	 * @param pre is the predicate that selects which rows are copied
	 * @param deepCopy if true attempts to perform a deep copy of the rows
	 */
	public <TYPE_TABLE extends TaggedTableSimpleRows<TYPE_KEY,TYPE_TAG,TYPE_DATA,TYPE_ROW>, P extends Predicate<Entry<TYPE_KEY,TYPE_ROW>>> 
	TaggedTableSimpleRows(
			TYPE_TABLE t,
			P pre,boolean deepCopy
			)
	{
		super(t,pre,deepCopy);
	}
	
	/**
	 * Check if a row has been marked as deleted.
	 * 
     * @param a is the key of the row
	 * @return a pair with a list of keys for the rows that failed to be checked 
	 * marked as the key, and a list with the marked rows as the value
     */
    public PairOfArrayList<TYPE_KEY,TYPE_KEY,ArrayList<TYPE_KEY>,ArrayList<TYPE_KEY>> isDeleted(TYPE_KEY a) {
    	PairOfArrayList<TYPE_KEY,TYPE_KEY,ArrayList<TYPE_KEY>,ArrayList<TYPE_KEY>> b=new PairOfArrayList<>();
		if(super.contains(a)) {
			if(super.value(a).isDeleted()) {
				b.add(null,a);
			}
		}
		else {
			b.add(a,null);
		}
    	return b;
    }

	/**
	 * Mark a row as deleted if it exists.
	 * 
     * @param a is the key of the row
	 * @return a pair with a list of keys for the rows that failed to be 
	 * marked as the key, and a list with the marked rows as the value
     */
    public PairOfArrayList<TYPE_KEY,TYPE_KEY,ArrayList<TYPE_KEY>,ArrayList<TYPE_KEY>> delete(TYPE_KEY a) {
    	PairOfArrayList<TYPE_KEY,TYPE_KEY,ArrayList<TYPE_KEY>,ArrayList<TYPE_KEY>> b=new PairOfArrayList<>();
		if(super.contains(a)) {
			super.value(a).delete();
			b.add(null,a);
		}
		else {
			b.add(a,null);
		}
    	return b;
    }

	/**
	 * Delete rows.
	 * 
	 * @param r is the list of rows to delete
	 * @return a pair with a list of keys for the rows that failed to be 
	 * marked as the key, and a list with the marked rows as the value
	 */
	public PairOfArrayList<TYPE_KEY,TYPE_KEY,ArrayList<TYPE_KEY>,ArrayList<TYPE_KEY>>
	deleteRows(Collection<TYPE_KEY> r){
    	PairOfArrayList<TYPE_KEY,TYPE_KEY,ArrayList<TYPE_KEY>,ArrayList<TYPE_KEY>> b=new PairOfArrayList<>();
		Iterator<TYPE_KEY> it = r.iterator();
		while(it.hasNext()){
			TYPE_KEY k = it.next();
			PairOfArrayList<TYPE_KEY, TYPE_KEY,ArrayList<TYPE_KEY>,ArrayList<TYPE_KEY>> bb = delete(k);
			b.add(bb);
		}
		return b;
	}
	
	/**
	 * Delete rows.
	 * 
	 * @param r is the list of rows to delete
	 * @return a pair with a list of keys for the rows that failed to be 
	 * marked as the key, and a list with the marked rows as the value
	 */
	public <TYPE_TABLE extends TaggedTableCore<TYPE_KEY, TYPE_TAG, TaggedTableSimpleColumnData<TYPE_DATA>,TYPE_DATA,TaggedTableSimpleHeader<TYPE_TAG, TYPE_DATA>,TaggedTableSimpleRow<TYPE_TAG, TYPE_DATA>,TaggedTableSimpleRows<TYPE_KEY, TYPE_TAG, TYPE_DATA, TaggedTableSimpleRow<TYPE_TAG, TYPE_DATA>> > >
	PairOfArrayList<TYPE_KEY,TYPE_KEY,ArrayList<TYPE_KEY>,ArrayList<TYPE_KEY>>
	deleteRows(TYPE_TABLE table){
    	PairOfArrayList<TYPE_KEY,TYPE_KEY,ArrayList<TYPE_KEY>,ArrayList<TYPE_KEY>> b=new PairOfArrayList<>();
		IndexedSet<TYPE_KEY, TaggedTableSimpleRow<TYPE_TAG, TYPE_DATA>>.IndexedSetIterator it = table.rowsIterator();
		while(it.hasNext()){
			Entry<TYPE_KEY, TaggedTableSimpleRow<TYPE_TAG, TYPE_DATA>> k = it.next();
			PairOfArrayList<TYPE_KEY, TYPE_KEY,ArrayList<TYPE_KEY>,ArrayList<TYPE_KEY>> bb = delete(k.getKey());
			b.add(bb);
		}
		return b;
	}
	
	/**
	 * Delete all rows that pass the predicate test.
	 * 
	 * @param pre
	 * @return a pair with a list of keys for the rows that failed to be 
	 * marked as the key, and a list with the marked rows as the value
	 */
	public <P extends Predicate<TaggedTableSimpleRow<TYPE_TAG, TYPE_DATA>> >
	PairOfArrayList<TYPE_KEY,TYPE_KEY,ArrayList<TYPE_KEY>,ArrayList<TYPE_KEY>>
	deleteRows(P pre){
    	PairOfArrayList<TYPE_KEY,TYPE_KEY,ArrayList<TYPE_KEY>,ArrayList<TYPE_KEY>> b=new PairOfArrayList<>();
		TaggedTableSimpleRowsIterator it = iterator();
		while(it.hasNext()){
			Entry<TYPE_KEY, TYPE_ROW> k = it.next();
			if(pre.test(k.getValue())) {//passed test
				PairOfArrayList<TYPE_KEY, TYPE_KEY,ArrayList<TYPE_KEY>,ArrayList<TYPE_KEY>> bb = delete(k.getKey());
				b.add(bb);
			}
		}
		return b;
	}
	
	/**
	 * Mark a row as not deleted if it exists.
	 * 
     * @param a is the key of the row
	 * @return a pair with a list of keys for the rows that failed to be 
	 * unmarked as the key, and a list with the unmarked rows as the value
     */
    public PairOfArrayList<TYPE_KEY,TYPE_KEY,ArrayList<TYPE_KEY>,ArrayList<TYPE_KEY>> undelete(TYPE_KEY a) {
    	PairOfArrayList<TYPE_KEY,TYPE_KEY,ArrayList<TYPE_KEY>,ArrayList<TYPE_KEY>> b=new PairOfArrayList<>();
		if(super.contains(a)) {
			super.value(a).undelete();
			b.add(null,a);
		}
		else {
			b.add(a,null);
		}
    	return b;
    }
    
	/**
	 * Undelete rows.
	 * 
	 * @param r is the list of rows to undelete
	 * @return a pair with a list of keys for the rows that failed to be 
	 * marked as the key, and a list with the marked rows as the value
	 */
	public PairOfArrayList<TYPE_KEY,TYPE_KEY,ArrayList<TYPE_KEY>,ArrayList<TYPE_KEY>>
	undeleteRows(Collection<TYPE_KEY> r){
    	PairOfArrayList<TYPE_KEY,TYPE_KEY,ArrayList<TYPE_KEY>,ArrayList<TYPE_KEY>> b=new PairOfArrayList<>();
		Iterator<TYPE_KEY> it = r.iterator();
		while(it.hasNext()){
			TYPE_KEY k = it.next();
			PairOfArrayList<TYPE_KEY, TYPE_KEY,ArrayList<TYPE_KEY>,ArrayList<TYPE_KEY>> bb = undelete(k);
			b.add(bb);
		}
		return b;
	}
	
	/**
	 * Undelete rows.
	 * 
	 * @param r is the list of rows to undelete
	 * @return a pair with a list of keys for the rows that failed to be 
	 * marked as the key, and a list with the marked rows as the value
	 */
	public <TYPE_TABLE extends TaggedTableCore<TYPE_KEY, TYPE_TAG, TaggedTableSimpleColumnData<TYPE_DATA>,TYPE_DATA,TaggedTableSimpleHeader<TYPE_TAG, TYPE_DATA>,TaggedTableSimpleRow<TYPE_TAG, TYPE_DATA>,TaggedTableSimpleRows<TYPE_KEY, TYPE_TAG, TYPE_DATA, TaggedTableSimpleRow<TYPE_TAG, TYPE_DATA>> > >
	PairOfArrayList<TYPE_KEY,TYPE_KEY,ArrayList<TYPE_KEY>,ArrayList<TYPE_KEY>>
	undeleteRows(TYPE_TABLE table){
    	PairOfArrayList<TYPE_KEY,TYPE_KEY,ArrayList<TYPE_KEY>,ArrayList<TYPE_KEY>> b=new PairOfArrayList<>();
		IndexedSet<TYPE_KEY, TaggedTableSimpleRow<TYPE_TAG, TYPE_DATA>>.IndexedSetIterator it = table.rowsIterator();
		while(it.hasNext()){
			Entry<TYPE_KEY, TaggedTableSimpleRow<TYPE_TAG, TYPE_DATA>> k = it.next();
			PairOfArrayList<TYPE_KEY, TYPE_KEY,ArrayList<TYPE_KEY>,ArrayList<TYPE_KEY>> bb = undelete(k.getKey());
			b.add(bb);
		}
		return b;
	}
	
	/**
	 * Undelete all rows that pass the predicate test.
	 * 
	 * @param pre
	 * @return a pair with a list of keys for the rows that failed to be 
	 * marked as the key, and a list with the marked rows as the value
	 */
	public <P extends Predicate<TaggedTableSimpleRow<TYPE_TAG, TYPE_DATA>> >
	PairOfArrayList<TYPE_KEY,TYPE_KEY,ArrayList<TYPE_KEY>,ArrayList<TYPE_KEY>>
	undeleteRows(P pre){
    	PairOfArrayList<TYPE_KEY,TYPE_KEY,ArrayList<TYPE_KEY>,ArrayList<TYPE_KEY>> b=new PairOfArrayList<>();
		TaggedTableSimpleRowsIterator it = iterator();
		while(it.hasNext()){
			Entry<TYPE_KEY, TYPE_ROW> k = it.next();
			if(pre.test(k.getValue())) {//passed test
				PairOfArrayList<TYPE_KEY, TYPE_KEY,ArrayList<TYPE_KEY>,ArrayList<TYPE_KEY>> bb = undelete(k.getKey());
				b.add(bb);
			}
		}
		return b;
	}
	
	@Override
	public TaggedTableSimpleRows<TYPE_KEY, TYPE_TAG, TYPE_DATA, TYPE_ROW> deepCopy() {
		return new TaggedTableSimpleRows<>(this, true);
	}
		
	@Override
	public TaggedTableSimpleRowsIterator iterator() {
		return new TaggedTableSimpleRowsIterator();
	}
	
	/**
	 * @author poltergeist0
	 *
	 * {@link Iterator} for {@link TaggedTableSimpleRows}
	 */
	public class TaggedTableSimpleRowsIterator extends IndexedSetIterator { 
	      
	    // constructor 
		protected TaggedTableSimpleRowsIterator() { 
			super();
	    } 
	      
	    public boolean isDeleted() {
	    	return super.current().getValue().isDeleted();
	    }
	    public void delete() {
	    	super.current().getValue().delete();
	    }
	    public void undelete() {
	    	super.current().getValue().undelete();
	    }
	}

	@Override
	protected TYPE_ROW typeDataGetValue(Object v) {
		return (TYPE_ROW)v;
	} 
}
