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

import jrain.taggedTableCore.mutable.TaggedTableCoreRows;

/**
 * @author poltergeist0
 *
 * Set of rows for use with {@link TaggedTableHashcode}.
 * It is composed of columns that have tags/names associated with them. 
 * Those are used to address each column, so no duplicates are allowed. 
 * Columns are described in {@link TaggedTableHashcodeHeader}. Any column present in the
 * {@code TaggedTableHeader} that does not exist in a row is assumed to have the 
 * default value declared in {@code TaggedTableHeader}.
 * If the data type of the tags/names is String, these are case sensitive.
 * The {@code null} value is accepted either as tag/name or value.
 * Column order is not preserved.
 * 
 * @param <TYPE_KEY> is the data type to be used to index rows
 * @param <TYPE_TAG> is the data type to be used for the tag/name of all columns
 * @param <TYPE_DATA> is the data type to be used for the value of all columns
 * @param <TYPE_ROW> is the data type to be used for the rows
 */
public class TaggedTableHashcodeRows
<
TYPE_KEY extends Object,TYPE_TAG extends Object,TYPE_DATA extends Object,
TYPE_ROW extends TaggedTableHashcodeRow<TYPE_TAG,TYPE_DATA>
> 
extends TaggedTableCoreRows<TYPE_KEY, TYPE_TAG, TYPE_DATA, TYPE_ROW>{

	/**
	 * Create a new simple row set
	 */
	public TaggedTableHashcodeRows(){
		super();
	}
		
	/**
	 * Copy constructor
	 * 
	 * @param <TYPE_TABLE> is the data type of the class that extends {@link TaggedTableHashcodeRows}
	 * @param original is the original {@link TaggedTableHashcodeRows}
	 * @param deepCopy if true attempts to perform a deep copy of the cells
	 */
	public <TYPE_TABLE extends TaggedTableHashcodeRows<TYPE_KEY,TYPE_TAG,TYPE_DATA,TYPE_ROW>> 
	TaggedTableHashcodeRows(TYPE_TABLE original,boolean deepCopy){
		super(original,deepCopy);
	}
	
	/**
	 * @author poltergeist0
	 *
	 * Adaptation class that adds the key template parameter to the {@link Predicate}.
	 * Required since the predicates for {@link TaggedTableCore} accept only a row
	 * parameter and the {@link TaggedTableCoreRows} predicate also needs a key 
	 * template parameter.
	 *  
	 * @param <TYPE_KEY> is the data type for the row key
	 * @param <TYPE_ROW> is the data type for the row
	 */
	private static class PredicateAddKey<TYPE_KEY,TYPE_ROW> implements Predicate<Entry<TYPE_KEY,TYPE_ROW>>{
		private Predicate<TYPE_ROW> p;
		
		public PredicateAddKey(Predicate<TYPE_ROW> pre) {p=pre;}
		
		@Override
		public boolean test(Entry<TYPE_KEY,TYPE_ROW> u) {
			return p.test(u.getValue());
		}
	}
	
	/**
	 * Filtered copy constructor.
	 * Only copies rows that pass the predicate test.
	 * 
	 * @param <TYPE_TABLE> is the data type of the class that extends {@link TaggedTableHashcodeRows}
	 * @param <P> is the data type of the class that extends {@link Predicate}
	 * @param t is the original {@link TaggedTableHashcodeRows}
	 * @param pre is the predicate that selects which rows are copied
	 * @param deepCopy if true attempts to perform a deep copy of the rows
	 */
	public <TYPE_TABLE extends TaggedTableHashcodeRows<TYPE_KEY,TYPE_TAG,TYPE_DATA,TYPE_ROW>, P extends Predicate<TYPE_ROW>> 
	TaggedTableHashcodeRows(
			TYPE_TABLE t,
			P pre,boolean deepCopy
			)
	{
		super(t,new PredicateAddKey<>(pre),deepCopy);
	}
	
	@Override
	public TaggedTableHashcodeRows<TYPE_KEY, TYPE_TAG, TYPE_DATA, TYPE_ROW> deepCopy() {
		return new TaggedTableHashcodeRows<>(this, true);
	}
		
	@Override
	public TaggedTableBasicRowsIterator iterator() {
		return new TaggedTableBasicRowsIterator();
	}
	
	/**
	 * @author poltergeist0
	 *
	 * {@link Iterator} for {@link TaggedTableHashcodeRows}
	 */
	public class TaggedTableBasicRowsIterator extends IndexedSetIterator { 
	      
	    // constructor 
		protected TaggedTableBasicRowsIterator() { 
			super();
	    } 
	}

	@Override
	protected TYPE_ROW typeDataGetValue(Object v) {
		return (TYPE_ROW) v;
	} 
}
