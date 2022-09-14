package jrain.taggedTableHashcode.mutable;

import java.util.Map.Entry;

import jrain.taggedTableCore.mutable.TaggedTableCoreCell;

/**
 * @author poltergeist0
 *
 * Column core functionality for use with {@link TaggedTableHashcode}.
 * A column is composed of a tag/name and a default value.
 * Columns should never be used directly. Instead all calls should be made 
 * through {@link TaggedTableHashcode}.
 * Columns are only inserted in rows when the value to be inserted in the row is
 * different from the default value declared in the column. This is a memory
 * optimization but implies that changing the default value means adding columns
 * to all rows, which is time consuming. For this reason it is advised to select
 * an appropriate default value before starting to insert rows and not to 
 * change the default value afterwards unless really required.
 * If the data type of the tag/name is String, this is case sensitive.
 * The {@code null} value is accepted either as tag/name or default value.
 * TYPE_TAG must implement the toString() method.
 * 
 * @param <TYPE_TAG> is the data type to be used for the tag/name of all columns
 * @param <TYPE_DATA> is the data type to be used for the value of all columns
 * @param <TYPE_COLUMN> is the data type to be used for the columns
 */
public class TaggedTableHashcodeColumnCore
<
	TYPE_TAG extends Object,
	TYPE_DATA extends Object, 
	TYPE_COLUMN extends TaggedTableHashcodeColumnData<TYPE_DATA>
> 
extends TaggedTableCoreCell<TYPE_TAG, TYPE_COLUMN >{
	
	/**
	 * Create a tagged table column
	 * 
	 * @param name is the name of the column
	 * @param defaultValue is the default value for the column
	 */
	public TaggedTableHashcodeColumnCore(TYPE_TAG name,TYPE_COLUMN defaultValue){
		super(name,defaultValue);
	}
	
	/**
	 * Constructor from entry.
	 * 
	 * @param <U> is the data type of the class that extends {@link Entry}
	 * @param e is the {@link Entry} containing the name (key) and default value (value)
	 */
	public <U extends Entry<TYPE_TAG,TYPE_COLUMN> > 
	TaggedTableHashcodeColumnCore(U e) {
		super(e);
    }

	/**
	 * Copy constructor.
	 * Can make a shallow or a deep copy of the given object.
	 * If a deep copy is chosen, the key and value of the given object are deep
	 * copied before being assigned to this object, if they support deep copying.
	 * Otherwise, only a reference is copied.
	 * 
	 * @param <U> is the data type of the object that extends this class
	 * @param original is the original object
	 * @param deepCopy if true, makes a deep copy, otherwise makes a shallow copy
	 */
	public <U extends TaggedTableHashcodeColumnCore<TYPE_TAG,TYPE_DATA,TYPE_COLUMN> > 
	TaggedTableHashcodeColumnCore(U original, boolean deepCopy){
		super(original,deepCopy);
	}
	
	@Override
	public TaggedTableHashcodeColumnCore<TYPE_TAG, TYPE_DATA,TYPE_COLUMN> deepCopy() {
		return new TaggedTableHashcodeColumnCore<>(this, true);
	}
	
}
