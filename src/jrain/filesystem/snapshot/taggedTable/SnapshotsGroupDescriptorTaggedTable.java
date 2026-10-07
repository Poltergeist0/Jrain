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

import jrain.filesystem.snapshot.immutable.SnapshotsGroupDescriptor;
import jrain.taggedTable.mutable.TaggedTable;
import jrain.taggedTable.mutable.TaggedTableColumnData;
import jrain.taggedTable.mutable.TaggedTableHeader;
import jrain.taggedTable.mutable.TaggedTableRow;
import jrain.polyType.mutable.PolyType;
import jrain.polyType.mutable.StringPoly;

/**
 * @author poltergeist0
 * 
 * Methods to convert a {@link SnapshotsGroupDescriptor} to a {@link TaggedTable}
 */
public interface SnapshotsGroupDescriptorTaggedTable{
		
	/**
	 * @return a {@link TaggedTableHeader} with the columns of a {@link SnapshotsGroupDescriptor}
	 */
	public static TaggedTableHeader taggedTableColumns(){
		TaggedTableHeader a=new TaggedTableHeader();
		a.add(BaseDescriptorTaggedTable.taggedTableColumns(), false);
		a.add(jrain.filesystem.snapshot.SnapshotsGroupDescriptor.defaultPath, new TaggedTableColumnData<PolyType<?>>(new StringPoly(jrain.filesystem.snapshot.SnapshotsGroupDescriptor.defaultSnapshotsGroupFileName)), false);
		return a;
	}
	
	/**
	 * Convert the values of a {@link SnapshotsGroupDescriptor} into a {@link TaggedTableRow}
	 * 
	 * @param d is the {@link SnapshotsGroupDescriptor} to convert
	 * @return a {@link TaggedTableRow} with the values of the given {@link SnapshotsGroupDescriptor}
	 */
	public static TaggedTableRow taggedTableRows(SnapshotsGroupDescriptor d){
		TaggedTableRow b = new TaggedTableRow(d);
		b.add(BaseDescriptorTaggedTable.taggedTableRows(d),false);
		b.add(jrain.filesystem.snapshot.SnapshotsGroupDescriptor.defaultPath,new StringPoly(d.getPath()),true);
		return b;
	}
	
	/**
	 * Convert a {@link SnapshotsGroupDescriptor} into a {@link TaggedTable}
	 * 
	 * @param d is the {@link SnapshotsGroupDescriptor} to convert
	 * @return a {@link TaggedTable} with the given {@link SnapshotsGroupDescriptor}
	 */
	public static TaggedTable taggedTable(SnapshotsGroupDescriptor d){
		TaggedTable t=new TaggedTable(taggedTableColumns());
		t.addRow(taggedTableRows(d), false);
		return t;
	}
}
