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

import java.util.Map.Entry;
import java.util.function.Predicate;

import jrain.polyType.mutable.PolyType;

/**
 * @author poltergeist0
 *
 * Set of columns for use with {@link TaggedTable}.
 * Requires that the first inserted column has unique values per row since it 
 * will be used as index to identify rows. Because of this, it can not be 
 * removed or deleted and its default value is ignored.
 * There is a virtual "Deleted" column that indicates if a row was deleted.
 * This column can not be removed because it is unnamed and is not really a column.
 * There is also an index for that column that can not be removed.
 * Insertion order is preserved.
 * See {@link TaggedTableColumn} for details about a column.
 * 
 */
public class TaggedTableHeader
extends TaggedTableHeaderCore<String, PolyType<?>, TaggedTableColumnData<PolyType<?>> >{
	
	public TaggedTableHeader compact() {
		return new TaggedTableHeader(this, new PredicateCompact<String, PolyType<?>, TaggedTableColumnData<PolyType<?>>>(), false, false);
	}
	
	/**
	 * Create a tagged table header
	 */
	public TaggedTableHeader() {
		super();
//		super.add(TaggedTableSimpleColumnData.DELETED, new TaggedTableColumnData<PolyType<?>>(new BooleanPoly(false)), false);
//		indexDeleted=new TaggedTableIndex();
	}
	
	/**
	 * Copy constructor
	 * 
	 * @param <TYPE_HEADER> is the data type of the object that extends this class
	 * @param t is the original
	 * @param deepCopy if true, makes a deep copy, otherwise makes a shallow copy
	 */
	public <TYPE_HEADER extends TaggedTableHeader>
	TaggedTableHeader(TYPE_HEADER t,boolean deepCopy){
		super(t,deepCopy);
//		super.add(TaggedTableSimpleColumnData.DELETED, t.value(TaggedTableSimpleColumnData.DELETED), false);
//		indexDeleted=new TaggedTableIndex(t.indexDeleted,deepCopy);
	}
	
	/**
	 * Filtered copy constructor.
	 * Uses a given  as source for data, a
	 *  as source for columns, and only adds rows 
	 * that pass the predicate test
	 * 
	 * @param <TYPE_HEADER> is the data type of the object that extends this class
	 * @param <P> is the data type of the class that extends {@link Predicate}
	 * @param t is the original
	 * @param pre is the predicate that selects which columns are copied
	 * @param overwrite, if true,indicates the current values are to be replaced 
	 * with the new ones, otherwise the current values are kept.
	 * @param deepCopy if true, makes a deep copy, otherwise makes a shallow copy
	 */
	public <TYPE_HEADER extends TaggedTableHeader, P extends Predicate<Entry<String, TaggedTableColumnData<PolyType<?>>>>> 
	TaggedTableHeader(
			TYPE_HEADER t,
			P pre,
			boolean overwrite,boolean deepCopy
			)
	{
		super(t,pre,overwrite,deepCopy);
	}
		
	@Override
	public TaggedTableHeader deepCopy() {
		return new TaggedTableHeader(this, true);
	}
	
}
