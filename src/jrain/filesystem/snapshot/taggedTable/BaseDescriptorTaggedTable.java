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
package jrain.filesystem.snapshot.taggedTable;

import jrain.filesystem.snapshot.immutable.BaseDescriptor;
import jrain.filesystem.snapshot.immutable.FileSystemObjectDescriptor;
import jrain.taggedTable.identifiable.TaggedTableIdentifiable;
import jrain.taggedTable.mutable.TaggedTable;
import jrain.taggedTable.mutable.TaggedTableColumnData;
import jrain.taggedTable.mutable.TaggedTableHeader;
import jrain.taggedTable.mutable.TaggedTableRow;
import jrain.polyType.mutable.LongPoly;
import jrain.polyType.mutable.PolyType;
import jrain.polyType.mutable.StringPoly;

/**
 * @author poltergeist0
 * 
 * Methods to convert a {@link FileSystemObjectDescriptor} to a {@link TaggedTable}
 */
public interface BaseDescriptorTaggedTable{
		
	/**
	 * @return a {@link TaggedTableHeader} with the columns of a {@link FileSystemObjectDescriptor}
	 */
	public static TaggedTableHeader taggedTableColumns(){
		TaggedTableHeader a=TaggedTableIdentifiable.taggedTableColumns();
		a.add(jrain.filesystem.snapshot.FileSystemObjectDescriptor.tagName,new TaggedTableColumnData<PolyType<?>>(new PolyType<String>(jrain.filesystem.snapshot.FileSystemObjectDescriptor.defaultName)),false);
		a.add(jrain.filesystem.snapshot.FileSystemObjectDescriptor.tagSize,new TaggedTableColumnData<PolyType<?>>(new PolyType<Long>(jrain.filesystem.snapshot.FileSystemObjectDescriptor.defaultSize)),false);
		return a;
	}
	
	/**
	 * Convert the values of a {@link FileSystemObjectDescriptor} into a {@link TaggedTableRow}
	 * 
	 * @param d is the {@link FileSystemObjectDescriptor} to convert
	 * @return a {@link TaggedTableRow} with the values of the given {@link FileSystemObjectDescriptor}
	 */
	public static TaggedTableRow taggedTableRows(BaseDescriptor d){
		TaggedTableRow b = new TaggedTableRow(d);
		b.add(TaggedTableIdentifiable.asTaggedTableRow(d.identifiable()),false);//add parent values
		b.add(jrain.filesystem.snapshot.FileSystemObjectDescriptor.tagName,new StringPoly(d.getDescriptorName()),true);
		b.add(jrain.filesystem.snapshot.FileSystemObjectDescriptor.tagSize,new LongPoly(d.getDescriptorSize()),true);
		return b;
	}
	
	/**
	 * Convert a {@link FileSystemObjectDescriptor} into a {@link TaggedTable}
	 * 
	 * @param d is the {@link FileSystemObjectDescriptor} to convert
	 * @return a {@link TaggedTable} with the given {@link FileSystemObjectDescriptor}
	 */
	public static TaggedTable taggedTable(BaseDescriptor d){
		TaggedTable t=new TaggedTable(taggedTableColumns());
		t.addRow(taggedTableRows(d), false);
		return t;
	}
}
