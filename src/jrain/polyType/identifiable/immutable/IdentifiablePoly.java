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
package jrain.polyType.identifiable.immutable;

import jrain.polyType.mutable.PolyType;
import jrain.identifiable.immutable.Identifiable;

/**
 * @author poltergeist0
 *
 * Polymorphic Identifiable type
 * 
 * See {@link PolyType} for more details.
 */
public class IdentifiablePoly extends PolyType<Identifiable> {

	/**
	 * Constructor for a given value
	 * 
	 * @param value is the data to store
	 */
	public IdentifiablePoly(Identifiable value) { super(value);}
	
	/**
	 * Copy constructor
	 * 
	 * @param poly is the original object to be copied
	 * @param deepCopy if true makes a deep copy of the object, if possible. False makes a shallow copy
	 */
	public IdentifiablePoly(IdentifiablePoly poly,boolean deepCopy) { super(poly,false);}

	@Override
	public IdentifiablePoly deepCopy() {
		return new IdentifiablePoly(this,false);
	}
}
