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

import java.util.UUID;

/**
 * @author poltergeist0
 * 
 * Snapshots group descriptor.
 * 
 * Extends {@link FileSystemObjectDescriptor}.
 * 
 * Defines additional fields: filename (where the group is saved)
 */
public class SnapshotsGroupDescriptor extends FileSystemObjectDescriptor implements jrain.filesystem.snapshot.SnapshotsGroupDescriptor{
	
	/**
	 * File name
	 */
//	private final String fn;
	
	/**
	 * @return the filename
	 */
//	public String getFilename() {return fn;}
	
	/**
	 * Constructor.
	 * 
	 * @param uuid is an {@link UUID}
	 * @param name is the name of the descriptor
	 * @param fileName is the file name where to save
	 * @param size is the size of the descriptor
	 */
	public SnapshotsGroupDescriptor(UUID uuid,String name, String path,long size) {
		super(uuid,name,size,path);
//		if(fileName==null)fn=SnapshotsGroupDescriptor.defaultSnapshotsGroupFileName;
//		else fn=fileName;
	}
	
	/**
	 * Copy/modify constructor.
	 * 
	 * If any of the additional values is not null, they will be used, otherwise
	 * they will be copied from the original object.
	 * 
	 * @param <T> is the type of the descriptor that extends {@link SnapshotsGroupDescriptor}
	 * @param original is the original descriptor
	 * @param uuid if true a new random UUID is used, otherwise the original is used
	 * @param name is the new name or null
	 * @param fileName is the file name where to save or null
	 * @param size if <0 copies size from original otherwise assigns the passed size
	 */
	public SnapshotsGroupDescriptor(SnapshotsGroupDescriptor original,boolean copyUUID,String name, String path,long size){
		super(original, copyUUID, name, size,path);
//		fn=(fileName==null)?original.getFilename():fileName;
	}
	
}
