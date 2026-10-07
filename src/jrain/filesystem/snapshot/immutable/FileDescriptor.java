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

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.UUID;

import jrain.hash.hashInstance.hashes.immutable.Hashes;

/**
 * @author poltergeist0
 *
 * File descriptor.
 * 
 * Extends {@link DirectoryDescriptor}.
 * 
 * Defines additional fields: hashes
 */
public class FileDescriptor extends DirectoryDescriptor implements jrain.filesystem.snapshot.FileDescriptor{

	/**
	 * Hashes calculated for the file
	 */
	private final Hashes hsh;
	
	/**
	 * Constructor.
	 * 
	 * @param uuid is an {@link UUID}
	 * @param name is the name of the descriptor
	 * @param path is the path of the descriptor
	 * @param size is the size of the descriptor
	 * @param creationDateTime is the creation date of the descriptor
	 * @param lastModificationDateTime is the last modification date of the descriptor
	 * @param lastAccessDateTime is the last access date of the descriptor
	 * @param canRead is the readability
	 * @param hashes are the hashes for the file
	 */
	public FileDescriptor(
			UUID uuid,
			String name,
			String path,
			long size,
			LocalDateTime creationDateTime,
			LocalDateTime lastModificationDateTime,
			LocalDateTime lastAccessDateTime,
			Boolean canRead,
			Hashes hashes
			){
		super(uuid,name,path, size,creationDateTime,lastModificationDateTime,lastAccessDateTime,canRead);
		if(hashes==null) hsh=new Hashes(new HashSet<>());
		else hsh=hashes;
	}
	
	/**
	 * Copy constructor.
	 * 
	 * Allows modification of data instead of copying if the additional fields are not null.
	 * 
	 * @param <T> is the type of the descriptor that extends {@link FileDescriptor}
	 * @param original is the original descriptor
	 * @param copyUUID if true copies uuid from original otherwise generate a new uuid
	 * @param name if null copies name from original otherwise assigns the passed name
	 * @param path if null copies path from original otherwise assigns the passed path
	 * @param size if <0 copies size from original otherwise assigns the passed size
	 * @param CreationDateTime if null copies path from original otherwise assigns the passed creation date
	 * @param LastModificationDateTime if null copies path from original otherwise assigns the passed last modification date
	 * @param LastAccessDateTime if null copies path from original otherwise assigns the passed last access date
	 * @param canRead if null copies path from original otherwise assigns the passed readability
	 * @param hashes if null copies hashes from original otherwise assigns the passed hashes
	 */
	public <T extends FileDescriptor> FileDescriptor(
			T original,
			boolean copyUUID,
			String name,
			String path,
			long size,
			LocalDateTime CreationDateTime,
			LocalDateTime LastModificationDateTime,
			LocalDateTime LastAccessDateTime,
			Boolean canRead,
			Hashes hashes
			)
	{
		super(original, copyUUID, name,path, size,CreationDateTime,LastModificationDateTime,LastAccessDateTime,canRead);
		if(hashes==null) hsh=original.getHashes();
		else hsh=hashes;
	}
	
	public Hashes getHashes() {return hsh;}
	
}
