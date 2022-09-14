package jrain.taggedTable.mutable;

/**
 * @author poltergeist0
 *
 * Constrain the values acceptable by a single column, including the default 
 * value.
 * More than one constrain can be defined but they are not automatically checked
 * for impossible situations (example: simultaneously constrain to values that 
 * are NULL and values that are not NULL).
 * Must be overridden for the specific use case of the {@link TaggedTable}.
 * 
 * Warning: Right now this is an empty class to be implemented in the future.
 * 
 * @param <TYPE_DATA> is the data type to be used for the value
 */
public class TaggedTableColumnConstrains<TYPE_DATA extends Object> {
	//TODO implement
	
	/**
	 * Check if the value complies with the columns constrains.
	 * 
	 * @param val is the value to check
	 * @return true if the value complies, otherwise false.
	 */
	public boolean comply(TYPE_DATA val) {
		//TODO implement
		return true;
	}
}
