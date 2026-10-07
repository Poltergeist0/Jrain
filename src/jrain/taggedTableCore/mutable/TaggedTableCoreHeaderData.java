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
 * Base class for columns.
 * Columns are only inserted in rows when the value to be inserted in the row is
 * different from the default value declared in the column. This is a memory
 * optimization but implies that changing the default value means adding columns
 * to all rows, which is time consuming. For this reason it is advised to select
 * an appropriate default value before starting to insert rows and not to 
 * change the default value afterwards unless really required.
 * The {@code null} value is accepted as default value.
 * 
 * @param <TYPE_DATA> is the data type of the column data
 */
public class TaggedTableCoreHeaderData<TYPE_DATA extends Object> 
implements TaggedTableCoreListHeader<TYPE_DATA>{
	

	/**
	 * Column data
	 */
	private TYPE_DATA dt;
	
	/**
	 * Create a tagged table column data
	 * 
	 * @param defaultValue is the value of the column data
	 */
	public TaggedTableCoreHeaderData(TYPE_DATA defaultValue){
		dt=defaultValue;
	}
	
	/**
	 * Copy constructor.
	 * Can make a shallow or a deep copy of the given object.
	 * If a deep copy is chosen, the key and value of the given object are deep
	 * copied before being assigned to this object, if they support deep copying.
	 * Otherwise, only a reference is copied.
	 * 
	 * @param <U> is the data type of the object that extends this class
	 * @param p is the given object
	 * @param deepCopy if true, makes a deep copy, otherwise makes a shallow copy
	 */
	public <U extends TaggedTableCoreHeaderData<TYPE_DATA> > 
	TaggedTableCoreHeaderData(U p, boolean deepCopy){
		this(((deepCopy)?DeepCopy.deepCopy(p.value()):p.value()));
	}
	
	/**
	 * Modify the column data
	 * 
	 * @param defaultValue is the new value for the column data
	 * @return the previous value for the column data
	 */
	public TYPE_DATA value(TYPE_DATA defaultValue){
		TYPE_DATA a=dt;
		dt=defaultValue;
		return a;
	}
	
	/**
	 * @return the value for the column data
	 */
	public TYPE_DATA value() {return dt;}
	
	public String toString(){
		if(dt==null) return "null";
		return dt.toString();
	}
		
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return dt.hashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || obj.getClass()!= this.getClass()) {
			return false;
		}
		@SuppressWarnings("unchecked")
		TaggedTableCoreHeaderData<TYPE_DATA> other = (TaggedTableCoreHeaderData<TYPE_DATA>) obj;//use unbounded to avoid warning about cast type safety
		return dt==other.dt;
	}

	@Override
	public TaggedTableCoreHeaderData<TYPE_DATA> deepCopy() {
		return new TaggedTableCoreHeaderData<>(this,true);
	}

}
