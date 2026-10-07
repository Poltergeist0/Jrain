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
package jrain.polyType.mutable;

/**
 * @author poltergeist0
 *
 * Polymorphic Integer type
 * 
 * See {@link PolyType} for more details.
 */
public class IntegerPoly extends PolyType<Integer> {
	
	/**
	 * Constructor for a given value
	 * 
	 * @param value is the data to store
	 */
	public IntegerPoly(Integer value) { super(value);}
	
	/**
	 * Copy constructor
	 * 
	 * @param poly is the original object to be copied
	 * @param deepCopy if true makes a deep copy of the object, if possible. False makes a shallow copy
	 */
	public IntegerPoly(IntegerPoly poly,boolean deepCopy) { super(poly,false);}

	@Override
	public IntegerPoly deepCopy() {
		return new IntegerPoly(this,false);
	}
}
