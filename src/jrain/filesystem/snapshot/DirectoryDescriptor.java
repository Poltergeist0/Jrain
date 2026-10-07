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
package jrain.filesystem.snapshot;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

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
public interface DirectoryDescriptor extends FileSystemObjectDescriptor{
		
//	/**
//	 * Default path is current directory
//	 */
//	public static final String defaultPath=".";
//	
	/**
	 * Default creation date time
	 */
	public static final LocalDateTime defaultCreationDateTime=LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
	
	/**
	 * Default modification date time
	 */
	public static final LocalDateTime defaultModificationDateTime=defaultCreationDateTime;
	
	/**
	 * Default access date time
	 */
	public static final LocalDateTime defaultAccessDateTime=defaultCreationDateTime;

	/**
	 * Default readability
	 */
	public static final Boolean defaultReadability=true;

	/**
	 * String used in conjunction with the toString() method to represent a
	 * {@link DirectoryDescriptor}.
	 */
	public static final String tagDirectory="directory";

//	/**
//	 * String used in conjunction with the toString() method to represent the
//	 * path part of a {@link DirectoryDescriptor}.
//	 */
//	public static final String tagPath="path";
//
	/**
	 * String used in conjunction with the toString() method to represent the
	 * creation date part of a {@link DirectoryDescriptor}.
	 */
	public static final String tagCreationDateTime="creationdate";

	/**
	 * String used in conjunction with the toString() method to represent the
	 * last modification date part of a {@link DirectoryDescriptor}.
	 */
	public static final String tagModificationDateTime="lastmodificationdate";

	/**
	 * String used in conjunction with the toString() method to represent the
	 * last access date part of a {@link DirectoryDescriptor}.
	 */
	public static final String tagAccessDateTime="lastaccessdate";

	/**
	 * String used in conjunction with the toString() method to represent the
	 * readability part of a {@link DirectoryDescriptor}.
	 */
	public static final String tagReadability="isReadable";
	
//	/**
//	 * @return the path of the descriptor
//	 */
//	public String getPath();
//	
//	/**
//	 * Get the full path.
//	 * 
//	 * It is composed by the concatenation of path with separator followed by name.
//	 * 
//	 * @param separator used to separate the different parts of the path
//	 * @return the full path of the descriptor
//	 */
//	public String getFullPath(String separator);
	
	/**
	 * @return the creation date
	 */
	public LocalDateTime getCreationDateTime();

	/**
	 * @return the last modification date
	 */
	public LocalDateTime getLastModificationDateTime();

	/**
	 * @return the last access date
	 */
	public LocalDateTime getLastAccessDateTime();
	
	/**
	 * @return the readability
	 */
	public Boolean isReadable();

}
