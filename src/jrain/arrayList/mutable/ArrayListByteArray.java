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
 * Alias class for {@link ArrayList} with elements of type {@link Byte} array.
 * 
 * Implements {@link DeepCopy} on the list only, not on accessing the elements.
 */
public class ArrayListByteArray extends ArrayList<Byte[]> implements DeepCopy<ArrayListByteArray>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1558642104465381142L;

	/**
	 * See {@link ArrayList} constructor documentation
	 */
	public ArrayListByteArray() {super();}

	/**
	 * See {@link ArrayList} constructor documentation
	 */
	public ArrayListByteArray(Collection<? extends Byte[] > c) {super(c);}
	
	/**
	 * See {@link ArrayList} constructor documentation
	 */
	public ArrayListByteArray(int initialCapacity) {super(initialCapacity);}
	
	@Override
	public ArrayListByteArray deepCopy() {
		return DeepCopy.deepCopy(this);
	}

}
