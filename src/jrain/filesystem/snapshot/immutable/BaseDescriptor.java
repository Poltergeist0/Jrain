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
package jrain.filesystem.snapshot.immutable;

import java.util.UUID;

import jrain.identifiable.immutable.Identifiable;

/**
 * @author poltergeist0
 *
 * Base snapshot descriptor.
 * A base descriptor has an identifier, a name and a size fields
 */
public class BaseDescriptor extends Identifiable implements jrain.filesystem.snapshot.BaseDescriptor{
	
	/**
	 * name
	 */
	private final String nm;
	
	/**
	 * size in bytes of space occupied in HDD (sum of files and sub-directories)
	 */
	private final Long sz;
	
	/**
	 * Constructor.
	 * 
	 * @param uuid is an {@link UUID}
	 * @param Name is the name of the descriptor
	 * @param size is the size of the descriptor
	 */
	public BaseDescriptor(
			UUID uuid,
			String Name,
			Long size
			) {
		super(uuid);
		nm=Name;
		sz=size;
	}
	
	/**
	 * Copy/modify constructor.
	 * 
	 * If any of the additional values is not null, they will be used, otherwise
	 * they will be copied from the original object.
	 * 
	 * @param <T> is the type of the descriptor that extends {@link BaseDescriptor}
	 * @param original is the original descriptor
	 * @param uuid if true a new random UUID is used, otherwise the original is used
	 * @param name is the new name or null
	 * @param size if <0 copies size from original otherwise assigns the passed size
	 */
	public <T extends BaseDescriptor> BaseDescriptor(
			T original,
			Boolean copyUUID,
			String name,
			Long size
			){
		super((copyUUID)?original:null);
		nm=(name==null)?original.getDescriptorName():name;
		sz=(size<0)?original.getDescriptorSize():size;
	}
	
	public String getDescriptorName(){return nm;}
	
	public Long getDescriptorSize(){return sz;}
	
}
