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
package jrain.taggedTable.mutable;

import jrain.taggedTableSimple.mutable.TaggedTableSimpleColumn;
import jrain.polyType.mutable.PolyType;

/**
 * @author poltergeist0
 *
 * Column for use with {@link TaggedTable}.
 * It adds an index and column constrains over {@link TaggedTableSimpleColumn}.
 * It is composed of a tag/name, a default value, a deleted flag, an index and a 
 * list of column constrains. 
 * Columns are only inserted in rows when the value to be inserted in the row is
 * different from the default value declared in the column.
 * If the data type of the tag/name is String, this is case sensitive.
 * The {@code null} value is accepted either as tag/name or default value.
 * Values are stored in the String data type so that any data type can be stored
 * but this requires a conversion to/from String if the data type to be stored
 * is not String.
 * 
 */
public class TaggedTableColumn
extends TaggedTableColumnCore<String,PolyType<?>, TaggedTableColumnData<PolyType<?> > >{
	
	/**
	 * Create a tagged table column
	 * 
	 * @param <TYPE_DATA> is the data type to be used for the value of all columns
	 * @param name is the name for the column
	 * @param defaultValue is the default value for the column
	 */
	public <TYPE_DATA extends PolyType<?> >
	TaggedTableColumn(String name,TYPE_DATA defaultValue) {//, TaggedTableColumnConstrains constrains){
		super(name,new TaggedTableColumnData<>(defaultValue));
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
	public <U extends TaggedTableColumn> 
	TaggedTableColumn(U p, boolean deepCopy){
		super(p,deepCopy);
	}
	
	@Override
	public TaggedTableColumn deepCopy() {
		return new TaggedTableColumn(this, true);
	}
}
