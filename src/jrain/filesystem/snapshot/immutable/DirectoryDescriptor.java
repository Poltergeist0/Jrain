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
import java.util.UUID;

/**
 * @author poltergeist0
 *
 * Directory descriptor.
 * 
 * Extends {@link FileSystemObjectDescriptor}.
 * 
 * Defines additional fields: path, creation/modification/access date and time, 
 * and a flag stating if it is readable from filesystem
 */
public class DirectoryDescriptor extends FileSystemObjectDescriptor implements jrain.filesystem.snapshot.DirectoryDescriptor{
	
//	/**
//	 * logical path of the file on the hard disk (example: "c:\windows" or 
//	 * "/mount/qwerty"). Informative field only that is not/should not be used 
//	 * in any processing
//	 */
//	private final String pth;
//	
	/**
	 * creation date
	 */
	private final LocalDateTime creation;
	
	/**
	 * Last modification date
	 */
	private final LocalDateTime modification;
	
	/**
	 * Last access date
	 */
	private final LocalDateTime access;
	
	/**
	 * Flag that indicates if the directory is readable
	 * Used only for snapshots loaded from file
	 */
	private final boolean readable;
	
	/**
	 * New directory descriptor constructor.
	 * 
	 * @param uuid is an {@link UUID}
	 * @param name is the name of the descriptor
	 * @param path is the path of the descriptor
	 * @param size is the size of the descriptor
	 * @param creationDateTime is the creation date of the descriptor
	 * @param lastModificationDateTime is the last modification date of the descriptor
	 * @param lastAccessDateTime is the last access date of the descriptor
	 * @param canRead is the readability
	 */
	public DirectoryDescriptor(
			UUID uuid,
			String name,
			String path, 
			Long size,
			LocalDateTime creationDateTime,
			LocalDateTime lastModificationDateTime,
			LocalDateTime lastAccessDateTime,
			Boolean canRead
			) {
		super(uuid,name,size,path);
//		pth=(path==null)?defaultPath:path;
		if(creationDateTime==null) creation=defaultCreationDateTime;
		else creation=creationDateTime;
		if(lastModificationDateTime==null) modification=defaultModificationDateTime;
		else modification=lastModificationDateTime;
		if(lastAccessDateTime==null) access=defaultAccessDateTime;
		else access=lastAccessDateTime;
		if(canRead==null) readable=defaultReadability;
		else readable=canRead;
	}
	
	/**
	 * Copy constructor.
	 * 
	 * Allows modification of data instead of copying if the additional fields are not null.
	 * 
	 * @param <T> is the type of the descriptor that extends {@link DirectoryDescriptor}
	 * @param original is the original descriptor
	 * @param copyUUID if true copies uuid from original otherwise generate a new uuid
	 * @param name if null copies name from original otherwise assigns the passed name
	 * @param path if null copies path from original otherwise assigns the passed path
	 * @param size if <0 copies size from original otherwise assigns the passed size
	 * @param CreationDateTime if null copies path from original otherwise assigns the passed creation date
	 * @param LastModificationDateTime if null copies path from original otherwise assigns the passed last modification date
	 * @param LastAccessDateTime if null copies path from original otherwise assigns the passed last access date
	 * @param canRead if null copies path from original otherwise assigns the passed readability
	 */
	public <T extends DirectoryDescriptor> DirectoryDescriptor(
			T original,
			Boolean copyUUID,
			String name,
			String path, 
			Long size,
			LocalDateTime CreationDateTime,
			LocalDateTime LastModificationDateTime,
			LocalDateTime LastAccessDateTime,
			Boolean canRead
			){
		super(original,copyUUID,name,size,path);
//		pth=(path==null)?original.getPath():path;
		if(CreationDateTime==null) creation=original.getCreationDateTime();
		else creation=CreationDateTime;
		if(LastModificationDateTime==null) modification=original.getLastModificationDateTime();
		else modification=LastModificationDateTime;
		if(LastAccessDateTime==null) access=original.getLastAccessDateTime();
		else access=LastAccessDateTime;
		if(canRead==null) readable=original.isReadable();
		else readable=canRead;
	}
	
//	public String getPath(){return pth;}
//	
//	public String getFullPath(String separator){
//		return getPath()+separator+getDescriptorName();
//	}
//	
	public LocalDateTime getCreationDateTime() {return creation;}

	public LocalDateTime getLastModificationDateTime() {return modification;}

	public LocalDateTime getLastAccessDateTime() {return access;}
	
	public Boolean isReadable() {return readable;}

}
