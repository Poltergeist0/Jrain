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
package jrain.taggedTable.mutable;

/**
 * @author poltergeist0
 *
 * Constrain the values acceptable by a single column, including the default 
 * value.
 * More than one constrain can be defined but they are not automatically checked
 * for impossible situations (example: simultaneously constrain to values that 
 * are NULL and values that are not NULL).
 * Must be overridden for the specific use case of the {@link TaggedTable}.
 * 
 * Warning: Right now this is an empty class to be implemented in the future.
 * 
 * @param <TYPE_DATA> is the data type to be used for the value
 */
public class TaggedTableColumnConstrains<TYPE_DATA extends Object> {
	//TODO implement
	
	/**
	 * Check if the value complies with the columns constrains.
	 * 
	 * @param val is the value to check
	 * @return true if the value complies, otherwise false.
	 */
	public boolean comply(TYPE_DATA val) {
		//TODO implement
		return true;
	}
}
