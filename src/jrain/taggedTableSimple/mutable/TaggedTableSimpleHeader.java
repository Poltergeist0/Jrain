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

import java.util.Map.Entry;
import java.util.function.Predicate;

import jrain.taggedTableHashcode.mutable.TaggedTableHashcodeHeaderCore;

/**
 * @author poltergeist0
 *
 * Set of columns for use with {@link TaggedTableSimple}.
 * There is a virtual "Deleted" column that indicates if a row was deleted.
 * This column can not be removed because it is unnamed and is not really a column.
 * Insertion order is preserved.
 * See {@link TaggedTableSimpleColumn} for details about a column.
 * 
 * @param <TYPE_TAG> is the data type to be used for the tag/name of all columns
 * @param <TYPE_DATA> is the data type to be used for the value of all columns
*/
public class TaggedTableSimpleHeader<TYPE_TAG extends Object,TYPE_DATA extends Object> 
extends TaggedTableSimpleHeaderCore<TYPE_TAG,TYPE_DATA, TaggedTableSimpleColumnData<TYPE_DATA> >{

	/**
	 * Create a tagged table header
	 */
	public TaggedTableSimpleHeader(){
		super();
	}
	
	/**
	 * Copy constructor
	 * 
	 * @param <TYPE_HEADER> is the data type of the class that extends {@link TaggedTableSimpleHeader}
	 * @param t is the original header
	 * @param deepCopy if true attempts to perform a deep copy of the cells
	 */
	public <TYPE_HEADER extends TaggedTableSimpleHeader<TYPE_TAG,TYPE_DATA> >
	TaggedTableSimpleHeader(TYPE_HEADER t,boolean deepCopy){
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
	public <TYPE_TABLE extends TaggedTableSimpleHeader<TYPE_TAG, TYPE_DATA>, P extends Predicate<Entry<TYPE_TAG,TaggedTableSimpleColumnData<TYPE_DATA>>>> 
	TaggedTableSimpleHeader(
			TYPE_TABLE t,
			P pre,
			boolean overwrite,boolean deepCopy
			)
	{
		super(t,pre,overwrite,deepCopy);
	}
	
	public TaggedTableSimpleHeader<TYPE_TAG,TYPE_DATA> compact() {
		return new TaggedTableSimpleHeader<>(this, new PredicateCompact<>(), false, false);
	}
		
	@Override
	public TaggedTableSimpleHeader<TYPE_TAG, TYPE_DATA> deepCopy() {
		return new TaggedTableSimpleHeader<>(this, true);
	}

	@Override
	public TaggedTableSimpleHeaderIterator iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected TaggedTableSimpleColumnData<TYPE_DATA> typeDataGetValue(Object v) {
		return (TaggedTableSimpleColumnData<TYPE_DATA>)v;
	}
	
}
