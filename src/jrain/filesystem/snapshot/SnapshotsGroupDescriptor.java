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
 * Snapshots group descriptor.
 * 
 * Extends {@link FileSystemObjectDescriptor}.
 * 
 * Defines additional fields: filename (where the group is saved)
 */
public interface SnapshotsGroupDescriptor extends FileSystemObjectDescriptor{
	
	/**
	 * Default file name
	 */
	public static final String defaultSnapshotsGroupFileName="default.txt";
	
	/**
	 * String used in conjunction with the toString() method to represent a 
	 * {@link SnapshotsGroupDescriptor}.
	 */
	public static final String tagSnapshotsGroup="snapshotsgroup";

	/**
	 * String used in conjunction with the toString() method to represent the
	 * file name part of a {@link SnapshotsGroupDescriptor}.
	 */
//	public static final String tagSnapshotsGroupFileName="filename";
	
	/**
	 * @return the filename where this group is saved on disk
	 */
//	public String getFilename();

}
