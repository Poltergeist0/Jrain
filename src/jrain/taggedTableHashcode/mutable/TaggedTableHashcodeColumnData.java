package jrain.taggedTableHashcode.mutable;

import jrain.taggedTableCore.mutable.TaggedTableCoreHeaderData;

/**
 * @author poltergeist0
 *
 * Column data for use with {@link TaggedTableHashcode}.
 * It is composed of a tag/name and a default value.
 * Columns should never be used directly. Instead all calls should be made 
 * through {@link TaggedTableHashcode}.
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
public class TaggedTableHashcodeColumnData<TYPE_DATA extends Object> 
extends TaggedTableCoreHeaderData<TYPE_DATA>{
	
	/**
	 * Create a tagged table column data
	 * 
	 * @param defaultValue is the default value for the column data
	 */
	public TaggedTableHashcodeColumnData(TYPE_DATA defaultValue){
		super(defaultValue);
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
	public <U extends TaggedTableHashcodeColumnData<TYPE_DATA> > 
	TaggedTableHashcodeColumnData(U p, boolean deepCopy){
		super(p,deepCopy);
	}
	
	@Override
	public TaggedTableHashcodeColumnData<TYPE_DATA> deepCopy() {
		return new TaggedTableHashcodeColumnData<>(this,true);
	}

}
