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
package jrain.taggedTableSimple.mutable;

import jrain.taggedTableHashcode.mutable.TaggedTableHashcodeColumnData;

/**
 * @author poltergeist0
 *
 * Column data for use with {@link TaggedTableSimple}.
 * They can be marked as deleted.
 * Columns should never be used directly. Instead all calls should be made 
 * through {@link TaggedTableSimple}.
 * Columns are only inserted in rows when the value to be inserted in the row is
 * different from the default value declared in the column. This is a memory
 * optimization but implies that changing the default value means adding columns
 * to all rows, which is time consuming. For this reason it is advised to select
 * an appropriate default value before starting to insert rows and not to 
 * change the default value afterwards unless really required.
 * The {@code null} value is accepted either as default value.
 * 
 * @param <TYPE_DATA> is the data type to be used for the value of all columns
 */
public class TaggedTableSimpleColumnData<TYPE_DATA extends Object> 
extends TaggedTableHashcodeColumnData<TYPE_DATA> 
{
	
	/**
	 * Separator used in the toString method to separate different data values
	 */
	public final static String SEPARATOR="$";
	
	/**
	 * Tag for the deleted flag
	 */
	public final static String DELETED="Deleted";
	
	/**
	 * Tag used when the deleted flag is true
	 */
	public final static String DELETEDTRUE="deleted";

	/**
	 * Tag used when the deleted flag is false
	 */
	public final static String DELETEDFALSE="kept";
	
	/**
	 * Flag used to mark the column as deleted
	 */
	private boolean dlt=false;
	
	/**
	 * Create a tagged table column data
	 * 
	 * @param defaultValue
	 */
	public TaggedTableSimpleColumnData(TYPE_DATA defaultValue){
		super(defaultValue);
		dlt=false;
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
	public <U extends TaggedTableSimpleColumnData<TYPE_DATA> > 
	TaggedTableSimpleColumnData(U p, boolean deepCopy){
		super(p,deepCopy);
		dlt=p.isDeleted();
	}
	
	/**
	 * @return true if the column is marked as deleted
	 */
	public boolean isDeleted() {return dlt;}
	
	/**
	 * Delete the column
	 */
	public void delete() {dlt=true;}
	
	/**
	 * Undelete the column
	 */
	public void undelete() {dlt=false;}
	
	/**
	 * Get the separator, used in the toString method to separate the values, 
	 * via a method so that it can be overridden in classes that extend this one.
	 * 
	 * @return the separator
	 */
	public String separatorValue() {
		return SEPARATOR;
	}

	public static <TYPE_DATA extends Object, TYPE_COLUMN extends TaggedTableSimpleColumnData<TYPE_DATA> > 
	StringBuilder toString(StringBuilder s,TYPE_COLUMN c, String sep){
		return c.toString(s, sep);
	}
		
	public StringBuilder toString(StringBuilder s, String sep){
		s.append(super.toString());
		if(sep==null) s.append(SEPARATOR);
		else s.append(sep);
		if(dlt) s.append(DELETEDTRUE);
		else s.append(DELETEDFALSE);
		return s;
	}
		
	public String toString(){
		StringBuilder s=new StringBuilder();
		return toString(s,separatorValue()).toString();
	}
		
	@Override
	public TaggedTableSimpleColumnData<TYPE_DATA> deepCopy() {
		return new TaggedTableSimpleColumnData<>(this,true);
	}
}
