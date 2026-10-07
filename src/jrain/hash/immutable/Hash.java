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
package jrain.hash.immutable;


/**
 * @author poltergeist0
 *
 * Interface that must be implemented by classes that calculate hashes.
 * 
 */
public interface Hash{
	
	/**
	 * Updates the digest using the specified array of bytes, starting at the 
	 * specified offset.
	 * 
	 * @param b is the array of bytes
	 * @param offset is the offset to start from in the array of bytes
	 * @param len is the number of bytes to use, starting at offset
	 */
	public void update(byte[] b, int offset, int len);

	/**
	 * Completes the hash computation by performing final operations such as 
	 * padding. The digest is reset after this call is made.
	 * 
	 * @return the array of bytes for the resulting hash value.
	 */
	public byte[] digest();

}
