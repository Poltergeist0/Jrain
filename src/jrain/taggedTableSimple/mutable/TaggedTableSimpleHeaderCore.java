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
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.function.Predicate;

import jrain.taggedTableCore.mutable.TaggedTableCoreCell;
import jrain.indexedSet.mutable.IndexedSet;
import jrain.taggedTableHashcode.mutable.TaggedTableHashcodeHeaderCore;
import jrain.entry.mutable.PairOfArrayList;

/**
 * @author poltergeist0
 *
 * Set of columns for use with {@link TaggedTableSimple}.
 * Requires that the first inserted column has unique values per row since it 
 * will be used as index to identify rows. Because of this, it can not be 
 * removed or deleted and its default value is ignored.
 * There is a virtual "Deleted" column that indicates if a row was deleted.
 * This column can not be removed because it is unnamed and is not really a column.
 * Insertion order is preserved.
 * See {@link TaggedTableSimpleColumn} for details about a column.
 * 
 * @param <TYPE_TAG> is the data type to be used for the tag/name of all columns
 * @param <TYPE_DATA> is the data type to be used for the value of all columns
 * @param <TYPE_COLUMN> is the data type to be used for the columns
 */
public class TaggedTableSimpleHeaderCore<TYPE_TAG extends Object,TYPE_DATA extends Object,TYPE_COLUMN extends TaggedTableSimpleColumnData<TYPE_DATA>> 
extends TaggedTableHashcodeHeaderCore<TYPE_TAG,TYPE_DATA, TYPE_COLUMN >{

	/**
	 * Create a tagged table header
	 */
	public TaggedTableSimpleHeaderCore(){
		super();
	}
	
	/**
	 * Copy constructor
	 * 
	 * @param <TYPE_HEADER> is the data type of the class that extends {@link TaggedTableSimpleHeaderCore}
	 * @param t is the original header
	 * @param deepCopy if true attempts to perform a deep copy of the cells
	 */
	public <TYPE_HEADER extends TaggedTableSimpleHeaderCore<TYPE_TAG,TYPE_DATA,TYPE_COLUMN> >
	TaggedTableSimpleHeaderCore(TYPE_HEADER t,boolean deepCopy){
		super(t,deepCopy);
	}
	
	/**
	 * Filtered copy constructor.
	 * 
	 * @param <TYPE_TABLE> is the data type of the class that extends {@link TaggedTableHashcodeHeaderCore}
	 * @param <P> is the data type of the class that extends {@link Predicate}
	 * @param t is the original header
	 * @param pre is the predicate that selects which columns are copied
	 * @param overwrite, if true,indicates the current values are to be replaced 
	 * with the new ones, otherwise the current values are kept.
	 * @param deepCopy if true attempts to perform a deep copy of the columns
	 */
	public <TYPE_TABLE extends TaggedTableSimpleHeaderCore<TYPE_TAG, TYPE_DATA,TYPE_COLUMN>, P extends Predicate<Entry<TYPE_TAG,TYPE_COLUMN>>> 
	TaggedTableSimpleHeaderCore(
			TYPE_TABLE t,
			P pre,
			boolean overwrite,boolean deepCopy
			)
	{
		super(t,pre,overwrite,deepCopy);
	}
	
	/**
	 * @author poltergeist0
	 * 
	 * {@link Predicate} that tests if a column is marked as deleted so it can 
	 * be filtered when copying the header
	 *
	 * @param <TYPE_TAG> is the data type to be used for the tag/name of all columns
	 * @param <TYPE_DATA> is the data type to be used for the value of all columns
	 * @param <TYPE_COLUMN> is the data type to be used for the columns
	 */
	protected static class PredicateCompact<TYPE_TAG, TYPE_DATA,TYPE_COLUMN extends TaggedTableSimpleColumnData<TYPE_DATA>> implements Predicate<Entry<TYPE_TAG, TYPE_COLUMN>>{
		@Override
		public boolean test(Entry<TYPE_TAG, TYPE_COLUMN> u) {
			return !(u.getValue().isDeleted());
		}
	}
	
	/**
	 * @return a new tagged table header without the deleted columns
	 */
	public TaggedTableSimpleHeaderCore<TYPE_TAG,TYPE_DATA,TYPE_COLUMN> compact() {
		return new TaggedTableSimpleHeaderCore<TYPE_TAG,TYPE_DATA,TYPE_COLUMN>(this, new PredicateCompact<>(), false, false);
	}
	
	@Override
	public <TYPE_HEADER_DATA,TYPE_HEADER extends IndexedSet<TYPE_TAG, TYPE_HEADER_DATA> > 
	StringBuilder toString(StringBuilder s,TYPE_HEADER h,String sepCell, String sepValue){
		super.toString(s, h, sepCell, sepValue);
		if(sepCell==null) s.append(SEPARATOR);
		else s.append(sepCell);
		TaggedTableCoreCell.toString(s,TaggedTableSimpleColumnData.DELETED,TaggedTableSimpleColumnData.DELETEDFALSE,sepValue);
		return s;
	}
		
	@Override
	public TaggedTableSimpleHeaderCore<TYPE_TAG, TYPE_DATA,TYPE_COLUMN> deepCopy() {
		return new TaggedTableSimpleHeaderCore<>(this, true);
	}
	
	/**
	 * Check if a column has been marked as deleted.
	 * 
     * @param a is the name of the column
	 * @return a pair with a list of keys for the columns that failed to be checked 
	 * marked as the key, and a list with the marked columns as the value
     */
    public PairOfArrayList<TYPE_TAG,TYPE_TAG, ArrayList<TYPE_TAG>, ArrayList<TYPE_TAG> > isDeleted(TYPE_TAG a) {
    	PairOfArrayList<TYPE_TAG,TYPE_TAG, ArrayList<TYPE_TAG>, ArrayList<TYPE_TAG>> b=new PairOfArrayList<>();
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
	 * Mark a column as deleted if it exists.
	 * 
     * @param a is the name of the column
	 * @return a pair with a list of keys for the columns that failed to be 
	 * marked as the key, and a list with the marked columns as the value
     */
    public PairOfArrayList<TYPE_TAG,TYPE_TAG, ArrayList<TYPE_TAG>, ArrayList<TYPE_TAG>> delete(TYPE_TAG a) {
    	PairOfArrayList<TYPE_TAG,TYPE_TAG, ArrayList<TYPE_TAG>, ArrayList<TYPE_TAG>> b=new PairOfArrayList<>();
		if(key(0).equals(a)) {//can not delete the first column
			b.add(a,null);
		}
		else {
			if(super.contains(a)) {
				super.value(a).delete();
				b.add(null,a);
			}
			else {
				b.add(a,null);
			}
    	}
    	return b;
    }

	/**
	 * Mark a column as not deleted if it exists.
	 * 
     * @param a is the name of the column
	 * @return a pair with a list of keys for the columns that failed to be 
	 * unmarked as the key, and a list with the unmarked columns as the value
     */
    public PairOfArrayList<TYPE_TAG,TYPE_TAG, ArrayList<TYPE_TAG>, ArrayList<TYPE_TAG>> undelete(TYPE_TAG a) {
    	PairOfArrayList<TYPE_TAG,TYPE_TAG, ArrayList<TYPE_TAG>, ArrayList<TYPE_TAG>> b=new PairOfArrayList<>();
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
	 * Check if a column has been marked as deleted.
	 * 
     * @param a is the insertion position of the column
	 * @return a pair with a list of keys for the columns that failed to be checked 
	 * marked as the key, and a list with the marked columns as the value
     */
    public PairOfArrayList<Integer,TYPE_TAG, ArrayList<Integer>, ArrayList<TYPE_TAG>> isDeleted(int a) {
    	PairOfArrayList<Integer,TYPE_TAG, ArrayList<Integer>, ArrayList<TYPE_TAG>> b=new PairOfArrayList<>();
		if(super.contains(a)) {
			if(super.value(a).isDeleted()) {
				b.add(null,super.key(a));
			}
		}
		else {
			b.add(a,null);
		}
    	return b;
    }

	/**
	 * Mark a column as deleted if it exists.
	 * 
     * @param a is the insertion position of the column
	 * @return a pair with a list of keys for the columns that failed to be 
	 * marked as the key, and a list with the marked columns as the value
     */
    public PairOfArrayList<Integer,TYPE_TAG, ArrayList<Integer>, ArrayList<TYPE_TAG>> delete(int a) {
    	PairOfArrayList<Integer,TYPE_TAG, ArrayList<Integer>, ArrayList<TYPE_TAG>> b=new PairOfArrayList<>();
		if(a==0) {//can not delete the first column
			b.add(a,null);
		}
		else {
			if(super.contains(a)) {
				super.value(a).delete();
				b.add(null,super.key(a));
			}
			else {
				b.add(a,null);
			}
    	}
    	return b;
    }

	/**
	 * Mark a column as not deleted if it exists.
	 * 
     * @param a is the insertion position of the column
	 * @return a pair with a list of keys for the columns that failed to be 
	 * unmarked as the key, and a list with the unmarked columns as the value
     */
    public PairOfArrayList<Integer,TYPE_TAG, ArrayList<Integer>, ArrayList<TYPE_TAG>> undelete(int a) {
    	PairOfArrayList<Integer,TYPE_TAG, ArrayList<Integer>, ArrayList<TYPE_TAG>> b=new PairOfArrayList<>();
		if(super.contains(a)) {
			super.value(a).undelete();
			b.add(null,super.key(a));
		}
		else {
			b.add(a,null);
		}
    	return b;
    }
    
	/**
	 * Remove a column
	 * 
     * @param a is the name of the column
	 * @return a pair with a list of keys for the columns that failed to be 
	 * removed as the key, and a list with the removed columns as the value
     */
    public PairOfArrayList<TYPE_TAG, Entry<TYPE_TAG, TYPE_COLUMN>, ArrayList<TYPE_TAG>, ArrayList<Entry<TYPE_TAG, TYPE_COLUMN>>> remove(TYPE_TAG a) {
    	PairOfArrayList<TYPE_TAG, Entry<TYPE_TAG, TYPE_COLUMN>, ArrayList<TYPE_TAG>, ArrayList<Entry<TYPE_TAG, TYPE_COLUMN>>> b=new PairOfArrayList<>();
		if(key(0).equals(a)) {//can not remove the first column
			b.add(a, null);
		}
		else {
			PairOfArrayList<TYPE_TAG, Entry<TYPE_TAG, TYPE_COLUMN>, ArrayList<TYPE_TAG>, ArrayList<Entry<TYPE_TAG, TYPE_COLUMN>>> p = super.remove(a);
			b.add(p);
    	}
    	return b;
    }

	/**
	 * Remove a column
	 * 
     * @param a is the name of the column
	 * @return a pair with a list of keys for the columns that failed to be 
	 * removed as the key, and a list with the removed columns as the value
     */
    public PairOfArrayList<Integer, Entry<TYPE_TAG, TYPE_COLUMN>, ArrayList<Integer>, ArrayList<Entry<TYPE_TAG, TYPE_COLUMN>>> remove(int a) {
    	PairOfArrayList<Integer, Entry<TYPE_TAG, TYPE_COLUMN>, ArrayList<Integer>, ArrayList<Entry<TYPE_TAG, TYPE_COLUMN>>> b=new PairOfArrayList<>();
		if(a==0) {//can not remove the first column
			b.add(a, null);
		}
		else {
			PairOfArrayList<Integer, Entry<TYPE_TAG, TYPE_COLUMN>, ArrayList<Integer>, ArrayList<Entry<TYPE_TAG, TYPE_COLUMN>>> p = super.remove(a);
			b.add(p);
    	}
    	return b;
    }

	/**
	 * @return an iterator for all columns except the virtual "Deleted" column.
	 */
	@Override
	public TaggedTableSimpleHeaderIterator iterator() {
		return new TaggedTableSimpleHeaderIterator();
	}
	
	/**
	 * @author poltergeist0
	 *
	 * {@link Iterator} for {@link TaggedTableSimpleHeader}
	 */
	public class TaggedTableSimpleHeaderIterator extends TaggedTableHashcodeHeaderIterator { 
	      
	    // constructor 
		protected TaggedTableSimpleHeaderIterator() { 
			super();
	    } 
	      
		/**
		 * @return true if the column is marked as deleted
		 */
	    public void isDeleted() {
	    	super.current().getValue().isDeleted();
	    }

		/**
		 * Delete the column
		 */
	    public void delete() {
	    	super.current().getValue().delete();
	    }

		/**
		 * Undelete the column
		 */
	    public void undelete() {
	    	super.current().getValue().undelete();
	    }
	}

	@Override
	protected TYPE_COLUMN typeDataGetValue(Object v) {
		return (TYPE_COLUMN)v;
	}

//	@Override
//	public Iterator<Entry<TYPE_TAG, TYPE_COLUMN>> iterator() {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
