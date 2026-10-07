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
public interface FileDescriptor extends DirectoryDescriptor{

	/**
	 * String used in conjunction with the toString() method to represent a
	 * {@link FileDescriptor}.
	 */
	public static final String tagFile="file";
	
	/**
	 * @return the hashes that were calculated for the current file
	 */
	public Hashes getHashes();

}
