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
package jrain.hash.hashInstance;

import java.util.Set;

import jrain.polyType.hash.immutable.XOR8Poly;

/**
 * @author poltergeist0
 *
 * Interface that must be implemented by classes that represent a XOR 
 * hash instance.
 * 
 * Defines methods to check if a given hash is provided by XOR.
 */
public interface HashXOR extends HashInstance{

	/**
	 * @return a set with the names of the hash algorithms provided by XOR
	 */
	public static Set<String> algorithms(){return Set.of("XOR8");}

	/**
	 * @param a is the name of a hash algorithm
	 * @return true if XOR provides that algorithm
	 */
	public static boolean hasAlgorithm(String a){return algorithms().contains(a);}

	/**
	 * Get a XOR instance corresponding to the requested hash algorithm 
	 * or null if the hash algorithm is not provided by XOR
	 * 
	 * @param s is the name of a hash algorithm
	 * @return a XOR instance or null
	 */
	public static XOR8Poly Instance(String s) {
		Byte b=0;
		return new XOR8Poly(b);
	}
	
}
