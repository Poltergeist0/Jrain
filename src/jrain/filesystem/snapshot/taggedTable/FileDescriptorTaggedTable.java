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

import jrain.filesystem.snapshot.immutable.FileDescriptor;
import jrain.taggedTable.hashes.TaggedTableHashes;
import jrain.taggedTable.mutable.TaggedTable;
import jrain.taggedTable.mutable.TaggedTableHeader;
import jrain.taggedTable.mutable.TaggedTableRow;

/**
 * @author poltergeist0
 * 
 * Methods to convert a {@link FileDescriptor} to a {@link TaggedTable}
 */
public interface FileDescriptorTaggedTable{
		
	/**
	 * @return a {@link TaggedTableHeader} with the columns of a {@link FileDescriptor}
	 */
	public static TaggedTableHeader taggedTableColumns(){
		TaggedTableHeader a=DirectoryDescriptorTaggedTable.taggedTableColumns();
		a.add(TaggedTableHashes.taggedTableColumns(), false);
		return a;
	}
	
	/**
	 * Convert the values of a {@link FileDescriptor} into a {@link TaggedTableRow}
	 * 
	 * @param d is the {@link FileDescriptor} to convert
	 * @return a {@link TaggedTableRow} with the values of the given {@link FileDescriptor}
	 */
	public static TaggedTableRow taggedTableRows(FileDescriptor d){
		TaggedTableRow b = new TaggedTableRow(d);
		b.add(DirectoryDescriptorTaggedTable.taggedTableRows(d),false);
		b.add(TaggedTableHashes.taggedTableRows(d.getHashes()),false);
		return b;
	}
	
	/**
	 * Convert a {@link FileDescriptor} into a {@link TaggedTable}
	 * 
	 * @param d is the {@link FileDescriptor} to convert
	 * @return a {@link TaggedTable} with the given {@link FileDescriptor}
	 */
	public static TaggedTable taggedTable(FileDescriptor d){
		TaggedTable t=new TaggedTable(taggedTableColumns());
		t.addRow(taggedTableRows(d), false);
		return t;
	}
}
