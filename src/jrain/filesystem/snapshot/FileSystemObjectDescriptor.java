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


/**
 * @author poltergeist0
 * 
 * Base information descriptor for snapshots.
 * 
 * Defines name and size
 */
public interface FileSystemObjectDescriptor extends BaseDescriptor{
		
	/**
	 * Default path is current directory
	 */
	public static final String defaultPath=".";
	
//	/**
//	 * String used in conjunction with the toString() method to represent the
//	 * name part of a {@link FileSystemObjectDescriptor}.
//	 */
//	public static final String tagName="name";
//
//	/**
//	 * String used in conjunction with the toString() method to represent the
//	 * size part of a {@link FileSystemObjectDescriptor}.
//	 */
//	public static final String tagSize="size";
	
	/**
	 * String used in conjunction with the toString() method to represent a
	 * {@link FileSystemObjectDescriptor}.
	 */
	public static final String tagObject="object";

	/**
	 * String used in conjunction with the toString() method to represent the
	 * path part of a {@link DirectoryDescriptor}.
	 */
	public static final String tagPath="path";

	/**
	 * Default name
	 */
	public static final String defaultName="FILESYSTEMOBJECT";
	
//	/**
//	 * Default value
//	 */
//	public static final Long defaultSize=0L;

	/**
	 * @return the name of the descriptor
	 */
//	public String getDescriptorName();
	
	/**
	 * @return the size of the descriptor
	 */
//	public Long getDescriptorSize();
	
	/**
	 * @return the path of the descriptor
	 */
	public String getPath();
	
	/**
	 * Get the full path.
	 * 
	 * It is composed by the concatenation of path with separator followed by name.
	 * 
	 * @param separator used to separate the different parts of the path
	 * @return the full path of the descriptor
	 */
	public String getFullPath(String separator);

}
