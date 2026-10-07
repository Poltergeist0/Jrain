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
import java.util.UUID;
import java.util.function.Predicate;

import jrain.taggedTableCore.mutable.TaggedTableCoreCell;
import jrain.indexedSet.mutable.IndexedSet;
import jrain.taggedTableSimple.mutable.TaggedTableSimpleRow;
import jrain.identifiable.immutable.Identifiable;
import jrain.polyType.mutable.PolyType;

/**
 * @author poltergeist0
 *
 * This class extends {@link TaggedTableSimpleRow} with several useful fields.
 * An Identifiable is added so that the row can be easily identified within the {@link TaggedTable}.
 * Also, a flag was added to allow marking the row as deleted instead of just losing the row.
 *  
 */
public class TaggedTableRow extends TaggedTableSimpleRow<String, PolyType<?> > implements jrain.identifiable.Identifiable<UUID>{
	
	Identifiable ky;//key used to identify row
	
	/**
	 * Create a new simple row with no columns
	 */
	public TaggedTableRow(){
		super();
		ky=new Identifiable();
	}
		
	/**
	 * Create a new simple row with no columns but with a given {@link Identifiable}
	 * 
	 * @param <U> is the data type to be used for the {@link Identifiable}
	 * @param id is the {@link Identifiable} to use
	 */
	public <U extends Identifiable> TaggedTableRow(U id){
		super();
		ky=id;
	}
		
	/**
	 * Copy constructor
	 * 
	 * @param <TYPE_ROW> is the data type to be used for the row
	 * @param original is the original row
	 * @param deepCopy if true, makes a deep copy, otherwise makes a shallow copy
	 */
	public <TYPE_ROW extends TaggedTableRow >
	TaggedTableRow(TYPE_ROW original,boolean deepCopy){
		super(original,deepCopy);
		ky=original.ky;
	}
	
	/**
	 * Filtered copy constructor.
	 * Uses a given {@link TaggedTableCore} as source for data, only adds rows 
	 * that pass the predicate test
	 * 
	 * @param <TYPE_TABLE> is the data type to be used for the row
	 * @param <P> is the data type of the class that extends {@link Predicate}
	 * @param t is the original row
	 * @param pre is the predicate that selects which rows are copied
	 * @param deepCopy if true, makes a deep copy, otherwise makes a shallow copy
	 */
	public <TYPE_TABLE extends TaggedTableRow, P extends Predicate<Entry<String, PolyType<?>>>> 
	TaggedTableRow(
			TYPE_TABLE t,
			P pre,
			boolean deepCopy
			)
	{
		super(t,pre,deepCopy);
	}

//	public Identifiable id() {return ky;}
		
	/**
	 * @param <TYPE_HEADER> is the data type to be used for the header
	 * @param ref is the reference header with the columns to keep
	 * @return a new tagged table row without the deleted columns
	 */
	public <TYPE_HEADER extends TaggedTableHeader> 
	TaggedTableRow compact(TYPE_HEADER ref) {
		TaggedTableRow t=new TaggedTableRow();
		IndexedSet<String, TaggedTableColumnData<PolyType<?>>>.IndexedSetIterator it = ref.iterator();
		while(it.hasNext()) {
			Entry<String, TaggedTableColumnData<PolyType<?>>> a = it.next();
			if(!a.getValue().isDeleted() && contains(a.getKey())) t.add(a.getKey(),value(a.getKey()), false);
		}
		return t;
	}
	
	@Override
	public TaggedTableRow deepCopy() {
		return new TaggedTableRow(this, true);
	}
	
	@Override
	public <TYPE_HEADER_DATA,TYPE_HEADER_TABLE extends IndexedSet<String, TYPE_HEADER_DATA> > 
	StringBuilder toString(StringBuilder s,TYPE_HEADER_TABLE h, String sepCell, String sepValue){
		super.toString(s,h,sepCell,sepValue);
		if(sepCell==null) s.append(TaggedTableSimpleRow.SEPARATOR);
		else s.append(sepCell);
		TaggedTableCoreCell.toString(s,"Identifiable",ky,sepValue);
		return s;
	}

	@Override
	public UUID identifier() {
		return ky.identifier();
	}

	@Override
	public String objectType() {
		return ky.objectType();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Identifiable identifiable() {
		return ky;
	}

	@Override
	public <T extends jrain.identifiable.Identifiable<UUID>> boolean matches(T ID) {
		return ky.matches(ID);
	}

	@Override
	public String toString() {
		return ky.toString();
	}
		
}
