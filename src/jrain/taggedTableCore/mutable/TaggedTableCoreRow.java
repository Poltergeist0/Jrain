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

import java.util.Map.Entry;
import java.util.function.Predicate;

import jrain.indexedSet.mutable.IndexedSet;


/**
 * @author poltergeist0
 *
 * Base row for use with tagged tables.
 * It is composed of columns that have tags/names associated with them. 
 * Those are used to address each column, so no duplicates are allowed. 
 * Columns are described in {@link TaggedTableCoreHeader}.
 * Any column present in the header that does not exist in a row is assumed to 
 * have the default value.
 * Only columns without the default value are stored.
 * If the data type of the tags/names is String, these are case sensitive.
 * The {@code null} value is accepted either as tag/name or value.
 * Column order is not preserved.
 * 
 * @param <TYPE_TAG> is the data type to be used for the tag/name of all columns
 * @param <TYPE_DATA> is the data type to be used for the value of all columns
 */
public abstract class TaggedTableCoreRow<
	TYPE_TAG extends Object,
	TYPE_DATA extends Object
	> 
extends IndexedSet<TYPE_TAG,TYPE_DATA>{

	/**
	 * Separator used in the toString method to separate key from value
	 */
	public final static String SEPARATOR="=";

	/**
	 * Create a new row with no columns
	 */
	public TaggedTableCoreRow(){
		super();
	}
		
	/**
	 * Copy constructor
	 * 
	 * @param <TYPE_ROW> is the data type for the row
	 * @param original is the original row
	 * @param deepCopy if true attempts to perform a deep copy of the rows
	 */
	public <TYPE_ROW extends TaggedTableCoreRow<TYPE_TAG,TYPE_DATA>> 
	TaggedTableCoreRow(TYPE_ROW original,boolean deepCopy){
		super(original,deepCopy);
	}
		
	/**
	 * Filtered copy constructor.
	 * 
	 * @param <TYPE_ROW> is the data type for the row
	 * @param <TYPE_PREDICATE> is the data type of the class that extends {@link Predicate}
	 * @param t is the original row
	 * @param pre is the {@link Predicate} that selects which columns are copied
	 * @param deepCopy if true attempts to perform a deep copy of the columns
	 */
	public <TYPE_ROW extends TaggedTableCoreRow<TYPE_TAG, TYPE_DATA>, TYPE_PREDICATE extends Predicate<Entry<TYPE_TAG,TYPE_DATA>>> 
	TaggedTableCoreRow(
			TYPE_ROW t,
			TYPE_PREDICATE pre,
			boolean deepCopy
			)
	{
		super(t,pre,deepCopy);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		IndexedSet<TYPE_TAG,TYPE_DATA>.IndexedSetIterator it = iterator();
		while(it.hasNext()) {
			Entry<TYPE_TAG, TYPE_DATA> a = it.next();
			result = prime * result + ((a == null) ? 0 : a.getKey().hashCode());
			result = prime * result + ((a == null) ? 0 : a.getValue().hashCode());
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	/**
	 * Check if the current row is identical to a given row.
	 * Rows are identical if they have the same columns with the same values.
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
		TaggedTableCoreRow<TYPE_TAG,TYPE_DATA> other = (TaggedTableCoreRow<TYPE_TAG,TYPE_DATA>) obj;
		if(size()!=other.size()) return false;
		IndexedSet<TYPE_TAG,TYPE_DATA>.IndexedSetIterator it = iterator();
		while(it.hasNext()) {
			Entry<TYPE_TAG, TYPE_DATA> a = it.next();
			if(!other.contains(a.getKey())) return false;//if the given row does not have a certain column the rows are not equal
			TYPE_DATA o = other.value(a.getKey());
			if(a.getValue()!=o) return false;//if the data on the current column of given row does not match the rows are not equal
		}
		return true;
	}

	@Override
	public String separatorValue() {
		return SEPARATOR;
	}

	@Override
	public String separatorCell() {
		return IndexedSet.SEPARATOR;
	}
	
}
