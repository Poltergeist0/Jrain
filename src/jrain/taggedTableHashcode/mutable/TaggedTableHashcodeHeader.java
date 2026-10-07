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
package jrain.taggedTableHashcode.mutable;

import java.util.Map.Entry;
import java.util.function.Predicate;

/**
 * @author poltergeist0
 *
 * Set of columns for use with {@link TaggedTableHashcode}.
 * Insertion order is preserved.
 * See {@link TaggedTableHashcodeColumn} for details about a column.
 * 
 * @param <TYPE_TAG> is the data type to be used for the tag/name of all columns
 * @param <TYPE_DATA> is the data type to be used for the value of all columns
*/
public class TaggedTableHashcodeHeader<TYPE_TAG extends Object,TYPE_DATA extends Object> 
extends TaggedTableHashcodeHeaderCore<TYPE_TAG, TYPE_DATA, TaggedTableHashcodeColumnData<TYPE_DATA> >{

	/**
	 * Create an empty tagged table header
	 */
	public TaggedTableHashcodeHeader(){
		super();
	}
	
	/**
	 * Copy constructor
	 * 
	 * @param <TYPE_HEADER> is the data type of the class that extends {@link TaggedTableHashcodeHeader}
	 * @param t is the original header
	 * @param deepCopy if true attempts to perform a deep copy of the cells
	 */
	public <TYPE_HEADER extends TaggedTableHashcodeHeader<TYPE_TAG,TYPE_DATA> >
	TaggedTableHashcodeHeader(TYPE_HEADER t,boolean deepCopy){
		super(t,deepCopy);
	}

	/**
	 * Filtered copy constructor.
	 * 
	 * @param <TYPE_TABLE> is the data type of the class that extends {@link TaggedTableHashcodeHeaderCore}
	 * @param <TYPE_PREDICATE> is the data type of the class that extends {@link Predicate}
	 * @param t is the original header
	 * @param pre is the predicate that selects which columns are copied
	 * @param overwrite, if true,indicates the current values are to be replaced 
	 * with the new ones, otherwise the current values are kept.
	 * @param deepCopy if true attempts to perform a deep copy of the columns
	 * 
	 */
	public <TYPE_TABLE extends TaggedTableHashcodeHeader<TYPE_TAG, TYPE_DATA>, TYPE_PREDICATE extends Predicate<Entry<TYPE_TAG,TaggedTableHashcodeColumnData<TYPE_DATA> >>> 
	TaggedTableHashcodeHeader(
			TYPE_TABLE t,
			TYPE_PREDICATE pre,
			boolean overwrite,boolean deepCopy
			)
	{
		super(t,pre,overwrite,deepCopy);
	}
	
	@Override
	public TaggedTableHashcodeHeader<TYPE_TAG, TYPE_DATA> deepCopy() {
		return new TaggedTableHashcodeHeader<>(this, true);
	}

	@Override
	protected TaggedTableHashcodeColumnData<TYPE_DATA> typeDataGetValue(Object v) {
		return (TaggedTableHashcodeColumnData<TYPE_DATA>)v;
	}
	
}
