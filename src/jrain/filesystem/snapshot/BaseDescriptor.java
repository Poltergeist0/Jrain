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
package jrain.filesystem.snapshot;

import java.util.UUID;

import jrain.identifiable.Identifiable;

/**
 * @author poltergeist0
 * 
 * Base information descriptor for snapshots.
 * 
 * Defines name and size
 */
public interface BaseDescriptor extends Identifiable<UUID>{
		

	/**
	 * String used in conjunction with the toString() method to represent the
	 * name part of a {@link BaseDescriptor}.
	 */
	public static final String tagName="name";

	/**
	 * String used in conjunction with the toString() method to represent the
	 * size part of a {@link BaseDescriptor}.
	 */
	public static final String tagSize="size";
	
	/**
	 * String used in conjunction with the toString() method to represent a
	 * {@link BaseDescriptor}.
	 */
	public static final String tagBase="base";

	/**
	 * Default name
	 */
	public static final String defaultName="BASEDESCRIPTOR";
	
	/**
	 * Default value
	 */
	public static final Long defaultSize=0L;

	/**
	 * @return the name of the descriptor
	 */
	public String getDescriptorName();
	
	/**
	 * @return the size of the descriptor
	 */
	public Long getDescriptorSize();
	
}
