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
package jrain.hash.hashInstance.immutable;


/**
 * @author poltergeist0
 * 
 * java.util.zip hash instance class.
 */
public class HashZip extends HashInstance implements jrain.hash.hashInstance.HashZip{
	
	/**
	 * Calculate a hash
	 * 
	 * @param algorithm is the name of the algorithm
	 */
	public HashZip(String algorithm) {
		super(algorithm,jrain.hash.hashInstance.HashZip.instance(algorithm));
	}

	/**
	 * Load a hash
	 * 
	 * @param algorithm is the name of the algorithm
	 * @param message is the hash
	 */
	public HashZip(String algorithm,final byte[] message){
		super(algorithm,message);
	}
}

