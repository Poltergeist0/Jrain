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
package jrain.filesystem.snapshot.immutable;

import java.util.Set;
import java.util.UUID;

/**
 * @author poltergeist0
 *
 * Snapshot descriptor.
 * 
 * Extends {@link FileSystemObjectDescriptor}.
 * 
 * Defines additional fields: base path, recursion, calculate hashes, and 
 * paths to exclude and include from the base path.
 * 
 * Has two path filters. Filter Out excludes paths/files from the base path. 
 * Filter In includes paths/files from the directories stated in FilterOut. 
 * Example: base path is "user/documents", filter out is "readme.txt" (full path 
 * would be "user/documents/readme.txt") and ".git" (full path would be 
 * "user/documents/.git"), and filter in is ".git/madeUpFolder" (full path would 
 * be "user/documents/.git/madeUpFolder").
 */
public class SnapshotDescriptor extends FileSystemObjectDescriptor implements jrain.filesystem.snapshot.SnapshotDescriptor{
	
	/**
	 * base path
	 */
//	private final String bp;
	
	/**
	 * hashes to calculate
	 */
	private final Set<String> hsh;
	
	/**
	 * recursion
	 */
	private final int rec;
	
	/**
	 * filter out paths
	 */
	private final Set<String> fo;
	
	/**
	 * filter in paths
	 */
	private final Set<String> fi;
	
	/**
	 * Constructor
	 * 
	 * @param uuid is an {@link UUID}
	 * @param name is the name of the descriptor
	 * @param basePath is the base path of the snapshot
	 * @param hashes are the hashes to calculate
	 * @param recursionLevel is the recursion depth
	 * @param filterIn are the paths inside those filtered out to include in the snapshot
	 * @param filterOut are the paths to filter out of the base path
	 * @param Size is the size of the descriptor
	 */
	public SnapshotDescriptor(
			UUID uuid,
			String name, 
			String basePath, 
			Set<String> hashes,
			int recursionLevel,
			Set<String> filterIn,
			Set<String> filterOut, 
			long size
			) {
		super(uuid,name,size,basePath);
//		bp=basePath;
		hsh=hashes;
		if(recursionLevel<=0){
			rec=0;
		}
		else{
			rec=recursionLevel;
		}
		fi=filterIn;
		fo=filterOut;
	}
	
	/**
	 * Copy/modify constructor.
	 * 
	 * If any of the additional values is not null, they will be used, otherwise
	 * they will be copied from the original object.
	 * 
	 * @param <T> is the type of the descriptor that extends {@link SnapshotDescriptor}
	 * @param original is the original descriptor
	 * @param uuid if true a new random UUID is used, otherwise the original is used
	 * @param name is the new name or null
	 * @param basePath is the base path of the snapshot or null
	 * @param hashes are the hashes to calculate or null
	 * @param recursionLevel if <0 copies size from original otherwise assigns the passed recursion
	 * @param filterIn are the paths to include back in the snapshot or null
	 * @param filterOut are the paths to exclude from the snapshot or null
	 * @param size if <0 copies size from original otherwise assigns the passed size
	 */
	public <T extends SnapshotDescriptor> SnapshotDescriptor(
			T original,
			boolean copyUUID,
			String name, 
			String basePath, 
			Set<String> hashes,
			int recursionLevel,
			Set<String> filterIn,
			Set<String> filterOut, 
			long size
			){
		super(original,copyUUID,name,size,basePath);
//		bp=(basePath==null)?original.getBasePath():basePath;
		hsh=(hashes==null)?original.hashes():hashes;
		rec=(recursionLevel<0)?original.getRecursion():recursionLevel;
		fi=(filterIn==null)? original.getFilterIn():filterIn;
		fo=(filterOut==null)?original.getFilterOut():filterOut;
	}
	
	public String getBasePath() {
		return super.getPath();
	}

	public Set<String> hashes(){return hsh;}

	public int getRecursion() {
		return rec;
	}

	public Set<String> getFilterOut() {
		return fo;
	}

	public Set<String> getFilterIn() {
		return fi;
	}
	
}
