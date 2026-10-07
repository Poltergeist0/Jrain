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
package jrain.taggedTableCore.mutable;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.function.Predicate;

import jrain.indexedSet.mutable.IndexedSet;
import jrain.indexedSequence.mutable.IndexedSequence;


/**
 * @author poltergeist0
 *
 * Header (set of columns) for use with tagged tables.
 * Insertion order is preserved.
 * See {@link TaggedTableCoreCell} for details about a column.
 * 
 * @param <TYPE_TAG> is the data type to be used for the tag/name of all columns
 * @param <TYPE_DATA> is the data type to be used for the value of all columns
 * @param <TYPE_COLUMN> is the data type to be used for the columns
 */
public abstract class TaggedTableCoreHeader<
	TYPE_TAG extends Object,
	TYPE_DATA extends Object,
	TYPE_COLUMN extends TaggedTableCoreHeaderData<TYPE_DATA>> 
extends IndexedSequence<TYPE_TAG, TYPE_COLUMN>{

	/**
	 * Create an empty tagged table header
	 */
	public TaggedTableCoreHeader(){
		super();
	}
	
	/**
	 * Copy constructor
	 * 
	 * @param <U> is the data type of the class that extends {@link TaggedTableCoreHeader}
	 * @param t is the original header
	 * @param deepCopy if true attempts to perform a deep copy of the cells
	 */
	public <U extends TaggedTableCoreHeader<TYPE_TAG,TYPE_DATA,TYPE_COLUMN> > TaggedTableCoreHeader(U t,boolean deepCopy){
		super(t,deepCopy);
	}
	
	/**
	 * Filtered copy constructor.
	 * 
	 * @param <TYPE_TABLE> is the data type of the class that extends {@link TaggedTableCoreHeader}
	 * @param <TYPE_PREDICATE> is the data type of the class that extends {@link Predicate}
	 * @param t is the original header
	 * @param pre is the predicate that selects which columns are copied
	 * @param overwrite, if true,indicates the current values are to be replaced 
	 * with the new ones, otherwise the current values are kept.
	 * @param deepCopy if true attempts to perform a deep copy of the columns
	 * 
	 */
	public <TYPE_TABLE extends TaggedTableCoreHeader<TYPE_TAG, TYPE_DATA,TYPE_COLUMN>, TYPE_PREDICATE extends Predicate<Entry<TYPE_TAG,TYPE_COLUMN>>> 
	TaggedTableCoreHeader(
			TYPE_TABLE t,
			TYPE_PREDICATE pre,
			boolean overwrite,boolean deepCopy
			)
	{
		super(t,pre,overwrite,deepCopy);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		Iterator<TYPE_TAG> it = keys().iterator();
		while(it.hasNext()) {
			TYPE_TAG a = it.next();
			result = prime * result + ((a == null) ? 0 : a.hashCode());
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || obj.getClass()!= this.getClass()) {
			return false;
		}
		@SuppressWarnings("unchecked")
		TaggedTableCoreHeader<TYPE_TAG, TYPE_DATA,TYPE_COLUMN> other = (TaggedTableCoreHeader<TYPE_TAG, TYPE_DATA,TYPE_COLUMN>) obj;//use unbounded to avoid warning about cast type safety
		if(size()!=other.size()) return false;
		if(commonKeys(this, other).size()!=size()) return false;
		return true;
	}

	@Override
	public String separatorValue() {
		return TaggedTableCoreCell.SEPARATOR;
	}

	@Override
	public String separatorCell() {
		return IndexedSet.SEPARATOR;
	}

	/**
	 * @return an iterator for all the entries in the header.
	 */
	@Override
	public TaggedTableCoreHeaderIterator iterator() {
		return new TaggedTableCoreHeaderIterator();
	}
	
	/**
	 * @author poltergeist0
	 *
	 * {@link Iterator} for {@link TaggedTableCoreHeader}
	 */
	public class TaggedTableCoreHeaderIterator extends IndexedSequenceIterator { 
	      
		/**
		 * Constructor
		 */
		protected TaggedTableCoreHeaderIterator() { 
			super();
	    } 
	}

}
