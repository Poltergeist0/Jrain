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
package jrain.arrayList.utils;

import java.util.ArrayList;

/**
 * @author poltergeist0
 *
 * Provides several static methods to work with {@code ArrayList}
 */
public class ArrayListUtils {

	/**
	 * Create a new {@link ArrayList} and add a single value.
	 * 
	 * @param <TYPE_DATA> is the data type of the value to add
	 * @param d is the value to add
	 * @return a new {@link ArrayList} with the value
	 */
	public static <TYPE_DATA> ArrayList<TYPE_DATA> init(final TYPE_DATA d) {
		ArrayList<TYPE_DATA> m=new ArrayList<TYPE_DATA>();
		m.add(d);
		return m;
	}

}
