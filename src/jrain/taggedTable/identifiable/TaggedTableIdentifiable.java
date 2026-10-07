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
package jrain.taggedTable.identifiable;

import jrain.identifiable.immutable.Identifiable;
import jrain.polyType.mutable.PolyType;
import jrain.polyType.mutable.StringPoly;
import jrain.polyType.mutable.UUIDPoly;
import jrain.taggedTable.mutable.TaggedTable;
import jrain.taggedTable.mutable.TaggedTableColumnData;
import jrain.taggedTable.mutable.TaggedTableHeader;
import jrain.taggedTable.mutable.TaggedTableRow;
import jrain.polyType.identifiable.immutable.IdentifiablePoly;

/**
 * @author poltergeist0
 * 
 * Collection of methods to convert an {@link Identifiable} to a {@link TaggedTable}.
 * 
 */
public interface TaggedTableIdentifiable{
	
	/**
	 * @return a {@link TaggedTableHeader} with the columns of a {@link Identifiable}
	 */
	public static TaggedTableHeader taggedTableColumns(){
		TaggedTableHeader a=new TaggedTableHeader();
		a.add(Identifiable.IDENTIFIABLETAG,new TaggedTableColumnData<PolyType<?>>(null),false);
		a.add(Identifiable.OBJECTTYPETAG,new TaggedTableColumnData<PolyType<?>>(new StringPoly(Identifiable.OBJECTUNKNOWN)),false);
		a.add(Identifiable.IDENTIFIERTAG,new TaggedTableColumnData<PolyType<?>>(null),false);
		return a;
	}
	
	/**
	 * Convert the values of a {@link Identifiable} into a {@link TaggedTableRow}
	 * 
	 * @param d is the {@link Identifiable} to convert
	 * @return a {@link TaggedTableRow} with the values of the given {@link Identifiable}
	 */
	public static TaggedTableRow asTaggedTableRow(Identifiable id) {
		TaggedTableRow b = new TaggedTableRow(id);
		b.add(Identifiable.IDENTIFIABLETAG, new IdentifiablePoly(id),true);
		b.add(Identifiable.OBJECTTYPETAG,new StringPoly(id.objectType()),true);
		b.add(Identifiable.IDENTIFIERTAG,new UUIDPoly(id.identifier()),true);
		return b;
	}
	
	/**
	 * Convert a {@link Identifiable} into a {@link TaggedTable}
	 * 
	 * @param d is the {@link Identifiable} to convert
	 * @return a {@link TaggedTable} with the given {@link Identifiable}
	 */
	public static TaggedTable asTaggedTable(Identifiable id) {
		TaggedTable t=new TaggedTable(taggedTableColumns());
		t.addRow(asTaggedTableRow(id), false);
		return t;
	}

}
