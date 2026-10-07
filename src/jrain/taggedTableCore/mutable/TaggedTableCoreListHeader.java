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
package jrain.taggedTableCore.mutable;

import jrain.deepCopy.DeepCopy;

/**
 * @author poltergeist0
 *
 * @param <TYPE_DATA> is the data type of the column data
 */
public interface TaggedTableCoreListHeader<TYPE_DATA extends Object> 
extends DeepCopy<TaggedTableCoreListHeader<TYPE_DATA> >{
	

	/**
	 * Modify the column data
	 * 
	 * @param defaultValue is the new value for the column data
	 * @return the previous value for the column data
	 */
	public TYPE_DATA value(TYPE_DATA defaultValue);
	
	/**
	 * @return the value for the column data
	 */
	public TYPE_DATA value();
	
	public String toString();
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode();

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj);

}
