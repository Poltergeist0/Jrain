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

import jrain.taggedTableSimple.mutable.TaggedTableSimpleRows;
import jrain.identifiable.immutable.Identifiable;
import jrain.polyType.mutable.PolyType;

/**
 * @author poltergeist0
 *
 * Set of rows for use with {@link TaggedTable}.
 * 
 */
public class TaggedTableRows extends TaggedTableSimpleRows<Identifiable, String, PolyType<?>, TaggedTableRow>{

	/**
	 * Create a new set of rows without rows
	 */
	public TaggedTableRows(){
		super();
	}
		
	/**
	 * Copy constructor
	 * 
	 * @param <TYPE_TABLE> is the data type of the class that extends {@link TaggedTableRows}
	 * @param original is the original {@link TaggedTableRows}
	 * @param deepCopy if true attempts to perform a deep copy of the cells
	 */
	public <TYPE_TABLE extends TaggedTableRows> 
	TaggedTableRows(TYPE_TABLE original,boolean deepCopy){
		super(original,deepCopy);
	}
	
	/**
	 * Filtered copy constructor.
	 * Only copies rows that pass the predicate test.
	 * 
	 * @param <TYPE_TABLE> is the data type of the class that extends {@link TaggedTableRows}
	 * @param <P> is the data type of the class that extends {@link Predicate}
	 * @param t is the original {@link TaggedTableRows}
	 * @param pre is the predicate that selects which rows are copied
	 * @param deepCopy if true attempts to perform a deep copy of the rows
	 */
	public <TYPE_TABLE extends TaggedTableRows, P extends Predicate<Entry<Identifiable,TaggedTableRow>>> 
	TaggedTableRows(
			TYPE_TABLE t,
			P pre,boolean deepCopy
			)
	{
		super(t,pre,deepCopy);
	}
	
	@Override
	public TaggedTableRows deepCopy() {
		return new TaggedTableRows(this, true);
	}
		
	/**
	 * @return an iterator for all the entries in the list.
	 */
	@Override
	public TaggedTableRowsIterator iterator() {
		return new TaggedTableRowsIterator();
	}
	
	public class TaggedTableRowsIterator extends TaggedTableSimpleRowsIterator { 
	      
	    // constructor 
		protected TaggedTableRowsIterator() { 
			super();
	    } 
	      
	} 
}
