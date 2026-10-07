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

import jrain.filesystem.snapshot.snapshotTree.SnapshotNode.DescriptorType;
import jrain.filesystem.snapshot.snapshotTree.immutable.SnapshotNode;
import jrain.taggedTable.mutable.TaggedTable;
import jrain.taggedTable.mutable.TaggedTableHeader;
import jrain.taggedTable.mutable.TaggedTableRow;

/**
 * @author poltergeist0
 * 
 * Methods to convert a {@link SnapshotNode} to a {@link TaggedTable}
 */
public interface SnapshotNodeTaggedTable{
		
	/**
	 * @return a {@link TaggedTableHeader} with the columns of a {@link SnapshotNode}
	 */
	public static TaggedTableHeader taggedTableColumns(){
		TaggedTableHeader a=SnapshotsGroupDescriptorTaggedTable.taggedTableColumns();
		a.add(SnapshotDescriptorTaggedTable.taggedTableColumns(), false);
		a.add(FileDescriptorTaggedTable.taggedTableColumns(), false);
		return a;
	}
	
	/**
	 * Convert the values of a {@link SnapshotNode} into a {@link TaggedTableRow}
	 * 
	 * @param d is the {@link SnapshotNode} to convert
	 * @return a {@link TaggedTableRow} with the values of the given {@link SnapshotNode}
	 */
	public static TaggedTableRow taggedTableRows(SnapshotNode d){
		if(d.getDescriptorType()==DescriptorType.SNAPSHOTSGROUP){
			return SnapshotsGroupDescriptorTaggedTable.taggedTableRows(d.getSnapshotsGroupDescriptor());
		}
		if(d.getDescriptorType()==DescriptorType.SNAPSHOT){
			return SnapshotDescriptorTaggedTable.taggedTableRows(d.getSnapshotDescriptor());
		}
		if(d.getDescriptorType()==DescriptorType.DIRECTORY){
			return DirectoryDescriptorTaggedTable.taggedTableRows(d.getDirectoryDescriptor());
		}
//		if(d.getDescriptorType()==DescriptorType.FILE){
			return FileDescriptorTaggedTable.taggedTableRows(d.getFileDescriptor());
//		}
//		return FileSystemObjectDescriptorTaggedTable.taggedTableRows(d.getObjectDescriptor());
	}
	
	/**
	 * Convert a {@link SnapshotNode} into a {@link TaggedTable}
	 * 
	 * @param d is the {@link SnapshotNode} to convert
	 * @return a {@link TaggedTable} with the given {@link SnapshotNode}
	 */
	public static TaggedTable taggedTable(SnapshotNode d){
		TaggedTable t=new TaggedTable(taggedTableColumns());
		t.addRow(taggedTableRows(d), false);
		return t;
	}
}
