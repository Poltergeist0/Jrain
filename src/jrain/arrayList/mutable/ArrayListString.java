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
package jrain.arrayList.mutable;

import java.util.ArrayList;
import java.util.Collection;

import jrain.arrayList.mutable.deepCopy.DeepCopy;

/**
 * @author poltergeist0
 *
 * Alias class for {@link ArrayList} with elements of type {@link String}.
 * 
 * Implements {@link DeepCopy} on the list only, not on accessing the elements.
 */
public class ArrayListString extends ArrayList<String> implements DeepCopy<ArrayListString>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3713113736643580026L;

	/**
	 * See {@link ArrayList} constructor documentation
	 */
	public ArrayListString() {super();}

	/**
	 * See {@link ArrayList} constructor documentation
	 */
	public ArrayListString(Collection<String> c) {super(c);}

	/**
	 * See {@link ArrayList} constructor documentation
	 */
	public ArrayListString(int initialCapacity) {super(initialCapacity);}
	
	@Override
	public ArrayListString deepCopy() {
		return DeepCopy.deepCopy(this);
	}

}
