package jrain.taggedTable.mutable;

import jrain.taggedTableSimple.mutable.TaggedTableSimpleColumnCore;

/**
 * @author poltergeist0
 *
 * Column for use with {@link TaggedTable}.
 * Columns are only inserted in rows when the value to be inserted in the row is
 * different from the default value declared in the column.
 * If the data type of the tag/name is String, this is case sensitive.
 * The {@code null} value is accepted either as tag/name or default value.
 * Values are stored in the String data type so that any data type can be stored
 * but this requires a conversion to/from String if the data type to be stored
 * is not String.
 * 
 * @param <TYPE_TAG> is the data type to be used for the name of all columns
 * @param <TYPE_DATA> is the data type to be used for the value of all columns
 * @param <TYPE_COLUMN> is the data type to be used for the columns
 */
public class TaggedTableColumnCore<TYPE_TAG extends Object,TYPE_DATA extends Object,TYPE_COLUMN extends TaggedTableColumnData<TYPE_DATA>>
extends TaggedTableSimpleColumnCore<TYPE_TAG,TYPE_DATA, TYPE_COLUMN>{
	
	/**
	 * Create a tagged table column core
	 * 
	 * @param name is the name of the column
	 * @param defaultValue is the default value of the column
	 */
	public TaggedTableColumnCore(TYPE_TAG name,TYPE_COLUMN defaultValue) {//, TaggedTableColumnConstrains constrains){
		super(name,defaultValue);
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
	public <U extends TaggedTableColumnCore<TYPE_TAG,TYPE_DATA, TYPE_COLUMN>> 
	TaggedTableColumnCore(U p, boolean deepCopy){
		super(p,deepCopy);
	}
	
	@Override
	public TaggedTableColumnCore<TYPE_TAG,TYPE_DATA, TYPE_COLUMN> deepCopy() {
		return new TaggedTableColumnCore<TYPE_TAG,TYPE_DATA, TYPE_COLUMN>(this, true);
	}
}
