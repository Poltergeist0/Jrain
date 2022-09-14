package jrain.taggedTableHashcode.mutable;

import java.util.Map.Entry;
import java.util.function.Predicate;

import jrain.indexedSet.mutable.IndexedSet;
import jrain.taggedTableCore.mutable.TaggedTableCoreRow;

/**
 * @author poltergeist0
 *
 * Simple row for use with {@link TaggedTableHashcode}.
 * It is composed of columns that have tags/names associated with them. 
 * Those are used to address each column, so no duplicates are allowed. 
 * Columns are described in {@link TaggedTableHashcodeHeader}. Any column present in the
 * {@code TaggedTableHeader} that does not exist in a row is assumed to have the 
 * default value declared in {@code TaggedTableHeader}.
 * If the data type of the tags/names is String, these are case sensitive.
 * The {@code null} value is accepted either as tag/name or value.
 * Column order is not preserved.
 * 
 * @param <TYPE_TAG> is the data type to be used for the tag/name of all columns
 * @param <TYPE_DATA> is the data type to be used for the value of all columns
 */
public class TaggedTableHashcodeRow<TYPE_TAG extends Object,TYPE_DATA extends Object> 
extends TaggedTableCoreRow<TYPE_TAG, TYPE_DATA>{

	/**
	 * Create a new simple row with no columns
	 */
	public TaggedTableHashcodeRow(){
		super();
	}
		
	/**
	 * Copy constructor
	 * 
	 * @param <TYPE_ROW> is the data type for the row
	 * @param original is the original row
	 * @param deepCopy if true attempts to perform a deep copy of the rows
	 */
	public <TYPE_ROW extends TaggedTableHashcodeRow<TYPE_TAG,TYPE_DATA> >
	TaggedTableHashcodeRow(TYPE_ROW original,boolean deepCopy){
		super(original,deepCopy);
	}
		
	/**
	 * Filtered copy constructor.
	 * 
	 * @param <TYPE_ROW> is the data type for the row
	 * @param <TYPE_PREDICATE> is the data type of the class that extends {@link Predicate}
	 * @param t is the original row
	 * @param pre is the {@link Predicate} that selects which columns are copied
	 * @param deepCopy if true attempts to perform a deep copy of the columns
	 */
	public <TYPE_ROW extends TaggedTableHashcodeRow<TYPE_TAG, TYPE_DATA>, TYPE_PREDICATE extends Predicate<Entry<TYPE_TAG,TYPE_DATA>>> 
	TaggedTableHashcodeRow(
			TYPE_ROW t,
			TYPE_PREDICATE pre,
			boolean deepCopy
			)
	{
		super(t,pre,deepCopy);
	}
	
	@Override
	public TaggedTableHashcodeRow<TYPE_TAG, TYPE_DATA> deepCopy() {
		return new TaggedTableHashcodeRow<>(this, true);
	}

	@Override
	protected TYPE_DATA typeDataGetValue(Object v) {
		return (TYPE_DATA) v;
	}
	
}
