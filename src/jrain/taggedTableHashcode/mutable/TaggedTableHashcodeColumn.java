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

/**
 * @author poltergeist0
 *
 * Column for use with {@link TaggedTableHashcode}.
 * It is composed of a tag/name and a default value.
 * Columns should never be used directly. Instead all calls should be made 
 * through {@link TaggedTableHashcode}.
 * Columns are only inserted in rows when the value to be inserted in the row is
 * different from the default value declared in the column. This is a memory
 * optimization but implies that changing the default value means adding columns
 * to all rows, which is time consuming. For this reason it is advised to select
 * an appropriate default value before starting to insert rows and not to 
 * change the default value afterwards unless really required.
 * If the data type of the tag/name is String, this is case sensitive.
 * The {@code null} value is accepted either as tag/name or default value.
 * TYPE_TAG must implement the toString() method.
 * 
 * @param <TYPE_TAG> is the data type to be used for the tag/name of all columns
 * @param <TYPE_DATA> is the data type to be used for the value of all columns
 */
public class TaggedTableHashcodeColumn
<
	TYPE_TAG extends Object,
	TYPE_DATA extends Object
> 
extends TaggedTableHashcodeColumnCore<TYPE_TAG, TYPE_DATA, TaggedTableHashcodeColumnData<TYPE_DATA>>{
	
	/**
	 * Create a tagged table column
	 * 
	 * @param name
	 * @param defaultValue
	 */
	public TaggedTableHashcodeColumn(TYPE_TAG name,TYPE_DATA defaultValue){
		super(name,new TaggedTableHashcodeColumnData<TYPE_DATA>(defaultValue));
	}
	
	/**
	 * Create a tagged table column
	 * 
	 * @param name
	 * @param defaultValue
	 */
	protected TaggedTableHashcodeColumn(TYPE_TAG name,TaggedTableHashcodeColumnData<TYPE_DATA> defaultValue){
		super(name,new TaggedTableHashcodeColumnData<TYPE_DATA>(defaultValue,false));
	}
	
	/**
	 * Constructor from entry.
	 * 
	 * @param <U> is the data type of the class that extends {@link Entry}
	 * @param e is the {@link Entry} containing the key and value
	 */
	public <U extends Entry<TYPE_TAG,TaggedTableHashcodeColumnData<TYPE_DATA>> > 
	TaggedTableHashcodeColumn(U e) {
		super(e);
    }

	/**
	 * Copy constructor.
	 * Can make a shallow or a deep copy of the given object.
	 * If a deep copy is chosen, the key and value of the given object are deep
	 * copied before being assigned to this object, if they support deep copying.
	 * Otherwise, only a reference is copied.
	 * 
	 * @param <U> is the data type of the object that extends this class
	 * @param original is the given object
	 * @param deepCopy if true, makes a deep copy, otherwise makes a shallow copy
	 */
	public <U extends TaggedTableHashcodeColumn<TYPE_TAG,TYPE_DATA> > 
	TaggedTableHashcodeColumn(U original, boolean deepCopy){
		super(original,deepCopy);
	}
	
}
