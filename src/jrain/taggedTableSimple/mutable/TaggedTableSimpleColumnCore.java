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

import jrain.taggedTableHashcode.mutable.TaggedTableHashcodeColumnCore;

/**
 * @author poltergeist0
 *
 * Column core functionality for use with {@link TaggedTableSimple}.
 * It is composed of a tag/name, a default value and a deleted flag.
 * Columns should never be used directly. Instead all calls should be made 
 * through {@link TaggedTableSimple}.
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
 * @param <TYPE_COLUMN> is the data type to be used for the columns
 */
public class TaggedTableSimpleColumnCore<TYPE_TAG extends Object,TYPE_DATA extends Object,TYPE_COLUMN extends TaggedTableSimpleColumnData<TYPE_DATA>> 
extends TaggedTableHashcodeColumnCore<TYPE_TAG,TYPE_DATA, TYPE_COLUMN >{
	
	/**
	 * @return true if the column is marked as deleted
	 */
	public boolean isDeleted() {return super.getValue().isDeleted();}
	
	/**
	 * Delete the column
	 */
	public void delete() {
		TaggedTableSimpleColumnData<TYPE_DATA> a = super.getValue();
		a.delete();
	}
	
	/**
	 * Undelete the column
	 */
	public void undelete() {
		TaggedTableSimpleColumnData<TYPE_DATA> a = super.getValue();
		a.undelete();
	}
	
	/**
	 * Create a tagged table column
	 * 
	 * @param name is the name of the column
	 * @param defaultValue is the default value for the column
	 */
	public TaggedTableSimpleColumnCore(TYPE_TAG name,TYPE_COLUMN defaultValue){
		super(name,defaultValue);
	}
	
	/**
	 * Constructor from entry.
	 * 
	 * @param <U> is the data type of the class that extends {@link Entry}
	 * @param e is the entry containing the name and default value
	 */
	public <U extends Entry<TYPE_TAG,TYPE_COLUMN> > 
	TaggedTableSimpleColumnCore(U e) {
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
	 * @param p is the given object
	 * @param deepCopy if true, makes a deep copy, otherwise makes a shallow copy
	 */
	public <U extends TaggedTableSimpleColumnCore<TYPE_TAG,TYPE_DATA,TYPE_COLUMN> > 
	TaggedTableSimpleColumnCore(U p, boolean deepCopy){
		super(p,deepCopy);
	}
	
	@Override
	public TaggedTableSimpleColumnCore<TYPE_TAG, TYPE_DATA,TYPE_COLUMN> deepCopy() {
		return new TaggedTableSimpleColumnCore<>(this, true);
	}

}
