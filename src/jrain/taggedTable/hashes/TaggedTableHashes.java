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
package jrain.taggedTable.hashes;

import java.util.Iterator;

import jrain.hash.hashInstance.HashMessageDigest;
import jrain.hash.hashInstance.HashXOR;
import jrain.hash.hashInstance.HashZip;
import jrain.hash.hashInstance.hashes.immutable.Hashes;
import jrain.hash.hashInstance.immutable.HashInstance;
import jrain.taggedTable.mutable.TaggedTable;
import jrain.taggedTable.mutable.TaggedTableColumnData;
import jrain.taggedTable.mutable.TaggedTableHeader;
import jrain.taggedTable.mutable.TaggedTableRow;
import jrain.polyType.mutable.PolyType;
import jrain.polyType.mutable.StringPoly;
import jrain.utils.HexadecimalUtils;

/**
 * @author poltergeist0
 *
 * Methods to convert a {@link Hashes} to a {@link TaggedTable}
 */
public interface TaggedTableHashes{
	
	/**
	 * Used in the {@link SnapshotDescriptor} to indicate the hashes to calculate.
	 * 
	 * @param prependName is the {@link String} to prepend to the names of the columns
	 * @param defaultValue is the default value for the columns
	 * @return a {@link TaggedTableHeader} with the columns of a {@link Hashes} prepended by a {@link String}
	 */
	public static TaggedTableHeader taggedTableColumns(String prependName,PolyType<?> defaultValue){
		TaggedTableHeader a=new TaggedTableHeader();
		Iterator<String> it = HashXOR.algorithms().iterator();
		while(it.hasNext()) {
			String s = it.next();
			a.add(prependName+s,new TaggedTableColumnData<PolyType<?>>(defaultValue),false);
		}
		it = HashZip.algorithms().iterator();
		while(it.hasNext()) {
			String s = it.next();
			a.add(prependName+s,new TaggedTableColumnData<PolyType<?>>(defaultValue),false);
		}
		it = HashMessageDigest.algorithms().iterator();
		while(it.hasNext()) {
			String s = it.next();
			a.add(prependName+s,new TaggedTableColumnData<PolyType<?>>(defaultValue),false);
		}
		return a;
	}

	/**
	 * @return a {@link TaggedTableHeader} with the columns of a {@link Hashes}
	 */
	public static TaggedTableHeader taggedTableColumns(){
		return taggedTableColumns("", new StringPoly(HashInstance.DEFAULTHASH));
	}

	/**
	 * Convert the values of a {@link Hashes} into a {@link TaggedTableRow}
	 * 
	 * @param d is the {@link Hashes} to convert
	 * @return a {@link TaggedTableRow} with the values of the given {@link Hashes}
	 */
	public static TaggedTableRow taggedTableRows(Hashes h) {
		TaggedTableRow a=new TaggedTableRow(h);
		Iterator<String> it = h.hashes().iterator();
		while(it.hasNext()) {
			String e = it.next();
			a.add(e,new StringPoly(HexadecimalUtils.convertToHex(h.hash(e))),false);
		}
		return a;
	}

	/**
	 * Convert a {@link Hashes} into a {@link TaggedTable}
	 * 
	 * @param d is the {@link Hashes} to convert
	 * @return a {@link TaggedTable} with the given {@link Hashes}
	 */
	public static TaggedTable taggedTable(Hashes h) {
		TaggedTable t=new TaggedTable(taggedTableColumns());
		t.addRow(taggedTableRows(h),false);
		return t;
	}
}

