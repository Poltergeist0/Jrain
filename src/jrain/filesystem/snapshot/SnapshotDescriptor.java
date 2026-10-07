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

import java.util.Set;

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
public interface SnapshotDescriptor extends FileSystemObjectDescriptor {
	
	/**
	 * Default base path
	 */
	public static final String defaultBasePath=".";
	
	/**
	 * Default recursion
	 */
	public static final Integer defaultRecursion=0;
	
	/**
	 * String used in conjunction with the toString() method to represent a 
	 * {@link SnapshotDescriptor}.
	 */
	public static final String tagSnapshot="snapshot";
	
	/**
	 * String used in conjunction with the toString() method to represent the
	 * base path part of a {@link SnapshotDescriptor}.
	 */
	public static final String tagSnapshotBasePath="basepath";
	
	/*
	 * This is prepended to the name of the hash
	 */
	public static final String tagSnapshotCalculate="calculate_";

	/**
	 * String used in conjunction with the toString() method to represent the
	 * recursion part of a {@link SnapshotDescriptor}.
	 */
	public static final String tagSnapshotRecursion="recursion";
	
	/**
	 * @return the base path of the snapshot
	 */
	public String getBasePath();

	/**
	 * @return the recursion depth of the snapshot
	 */
	public int getRecursion();

	/**
	 * @return the paths to filter out of the base path
	 */
	public Set<String> getFilterOut();

	/**
	 * @return the paths inside those filtered out to include in the snapshot
	 */
	public Set<String> getFilterIn();
	
}
