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

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.function.Predicate;

import jrain.taggedTableCore.mutable.TaggedTableCoreHeader;

/**
 * @author poltergeist0
 *
 * Set of columns for use with {@link TaggedTableHashcodeHeader}.
 * Insertion order is preserved.
 * See {@link TaggedTableHashcodeColumn} for details about a column.
 * 
 * @param <TYPE_TAG> is the data type to be used for the tag/name of all columns
 * @param <TYPE_DATA> is the data type to be used for the value of all columns
 * @param <TYPE_COLUMN> is the data type to be used for the columns
 */
public abstract class TaggedTableHashcodeHeaderCore<
	TYPE_TAG extends Object,
	TYPE_DATA extends Object,
	TYPE_COLUMN extends TaggedTableHashcodeColumnData<TYPE_DATA>
> 
extends TaggedTableCoreHeader<TYPE_TAG,TYPE_DATA, TYPE_COLUMN >{

	/**
	 * Create an empty tagged table header
	 */
	public TaggedTableHashcodeHeaderCore(){
		super();
	}
	
	/**
	 * Copy constructor
	 * 
	 * @param <TYPE_HEADER> is the data type of the class that extends {@link TaggedTableHashcodeHeaderCore}
	 * @param h is the original header
	 * @param deepCopy if true attempts to perform a deep copy of the cells
	 */
	public <TYPE_HEADER extends TaggedTableHashcodeHeaderCore<TYPE_TAG,TYPE_DATA,TYPE_COLUMN> >
	TaggedTableHashcodeHeaderCore(TYPE_HEADER h,boolean deepCopy){
		super(h,deepCopy);
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
	public <TYPE_TABLE extends TaggedTableHashcodeHeaderCore<TYPE_TAG, TYPE_DATA,TYPE_COLUMN>, TYPE_PREDICATE extends Predicate<Entry<TYPE_TAG,TYPE_COLUMN >>> 
	TaggedTableHashcodeHeaderCore(
			TYPE_TABLE t,
			TYPE_PREDICATE pre,
			boolean overwrite,boolean deepCopy
			)
	{
		super(t,pre,overwrite,deepCopy);
	}
	
//	@Override
//	public TaggedTableHashcodeHeaderCore<TYPE_TAG, TYPE_DATA,TYPE_COLUMN,TYPE_CELL> deepCopy() {
//		return new TaggedTableHashcodeHeaderCore<>(this, true);
//	}

//	@Override
//	protected TYPE_COLUMN typeDataGetValue(Object v) {
//		return (TYPE_COLUMN)v;
//	}
	
	@Override
	public TaggedTableHashcodeHeaderIterator iterator() {
		return new TaggedTableHashcodeHeaderIterator();
	}
	
	/**
	 * @author poltergeist0
	 *
	 * {@link Iterator} for {@link TaggedTableHashcodeHeader}
	 */
	public class TaggedTableHashcodeHeaderIterator extends TaggedTableCoreHeaderIterator { 
	      
	    // constructor 
		protected TaggedTableHashcodeHeaderIterator() { 
			super();
	    } 
	}

}
