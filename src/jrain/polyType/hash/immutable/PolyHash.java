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
package jrain.polyType.hash.immutable;

import jrain.hash.immutable.Hash;
import jrain.polyType.immutable.PolyType;

/**
 * @author poltergeist0
 *
 * Base class for polymorphic hash data types.
 * 
 * Allows using a heterogeneous set of hash data types in places they are not normally
 * allowed, such as in standard collections.
 * 
 * @param <T> is the type of the hash instance to store
 */
public abstract class PolyHash<T> extends PolyType<T> implements Hash{

	/**
	 * Constructor for a given hash instance
	 * 
	 * @param value is the hash instance to store
	 */
	public PolyHash(T value) { super(value);}

	/**
	 * Copy constructor
	 * 
	 * @param poly is the original object to be copied
	 * @param deepCopy if true makes a deep copy of the object, if possible. False makes a shallow copy
	 */
	public PolyHash(PolyHash<T> poly,boolean deepCopy) { super(poly,false);}
	
	public abstract void update(byte[] b, int offset, int len);

	public abstract byte[] digest();

}
