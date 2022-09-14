package jrain.taggedTableHashcode.mutable;

import java.util.Map.Entry;
import java.util.function.Predicate;

import jrain.taggedTableCore.mutable.TaggedTableCore;

/**
 * @author poltergeist0
 *
 * Basic table with multiple named (tagged) columns, with rows identified by 
 * their hashcode ({@link Integer}).
 * Adding a column does not modify any rows.
 * Columns names can not be repeated.
 * Rows can not be repeated.
 * Rows can not be added if there are no columns.
 * Row order is not maintained.
 * Nulls are acceptable as values/default values or column name.
 * Rows are immutable so, to change a value of a column in a row, the old row 
 * must be removed and the row with the modification must be added (as 
 * implemented by the friendly method removeRow).
 * To keep the table fast, removing a column only removes it from the header but
 * keeps it in rows otherwise, in the worst case scenario, the entire table 
 * would have to be recreated since rows are indexed by hash and any 
 * modification to a row changes the hash.
 *
 * @param <TYPE_TAG> is the data type for the column tag
 * @param <TYPE_DATA> is the data type for the row data
 */
public class TaggedTableHashcode
<
TYPE_TAG extends Object,
TYPE_DATA extends Object
> 
extends TaggedTableCore
<
Integer, 
TYPE_TAG, 
TaggedTableHashcodeColumnData<TYPE_DATA>,
TYPE_DATA,
TaggedTableHashcodeHeader<TYPE_TAG, TYPE_DATA>,
TaggedTableHashcodeRow<TYPE_TAG, TYPE_DATA>,
TaggedTableHashcodeRows<Integer, TYPE_TAG,TYPE_DATA,TaggedTableHashcodeRow<TYPE_TAG, TYPE_DATA>>
>{
	
	/**
	 * Create a new simple tagged table with no columns or rows.
	 */
	public TaggedTableHashcode(){
		super(new TaggedTableHashcodeHeader<>());
	}
	
	/**
	 * Create a new simple tagged table with no rows.
	 * 
	 * @param <TYPE_HEADER> is the data type of the class that extends {@link TaggedTableHashcodeHeader}
	 * @param h is the header
	 */
	public <TYPE_HEADER extends TaggedTableHashcodeHeader<TYPE_TAG,TYPE_DATA> >
	TaggedTableHashcode(TYPE_HEADER h){
		super(h);
	}
	
	/**
	 * Copy constructor uses a given {@link TaggedTableHashcode} as source for 
	 * columns but does not add any rows if copyData is false
	 * 
	 * @param <TYPE_TABLE> is the data type of the class that extends {@link TaggedTableHashcode}
	 * @param t is the original table
	 * @param copyRows if true also copies the rows, otherwise only the header is copied
	 * @param deepCopy if true attempts to perform a deep copy of the rows and columns
	 */
	public <TYPE_TABLE extends TaggedTableHashcode<TYPE_TAG,TYPE_DATA>	>
	TaggedTableHashcode(TYPE_TABLE t,boolean copyRows,boolean deepCopy){
		super(t,copyRows,deepCopy);
	}
	
	/**
	 * Filtered copy constructor.
	 * Uses a given {@link TaggedTableHashcode} as source for data, a
	 * {@link TaggedTableHashcodeHeader} as source for columns, and only adds rows 
	 * that pass the predicate test
	 * 
	 * @param <TYPE_HEADER> is the data type of the class that extends {@link TaggedTableHashcodeHeader}
	 * @param <TYPE_TABLE> is the data type of the class that extends {@link TaggedTableHashcode}
	 * @param <P> is the data type of the class that extends {@link Predicate}
	 * @param t is the original table
	 * @param h is the header with the new columns
	 * @param pre is the predicate that selects which rows are copied
	 * @param deepCopy if true attempts to perform a deep copy of the rows and columns
	 */
	public  <
	TYPE_HEADER extends TaggedTableHashcodeHeader<TYPE_TAG,TYPE_DATA>,
	TYPE_TABLE extends TaggedTableHashcode<TYPE_TAG,TYPE_DATA>, 
	P extends Predicate<Entry<Integer,TaggedTableHashcodeRow<TYPE_TAG,TYPE_DATA>>> 
	> 
	TaggedTableHashcode(
			TYPE_TABLE t,
			TYPE_HEADER h,
			P pre,boolean deepCopy
			)
	{
		super(t, h, pre,deepCopy);
	}
		
	@Override
	protected Integer getKey(TaggedTableHashcodeRow<TYPE_TAG, TYPE_DATA> r) {
		return r.hashCode();
	}

	@Override
	public TaggedTableHashcode<TYPE_TAG, TYPE_DATA> deepCopy() {
		return new TaggedTableHashcode<>(this,true,true);
	}
	
	@Override
	protected TaggedTableHashcodeRows<Integer, TYPE_TAG,TYPE_DATA,TaggedTableHashcodeRow<TYPE_TAG, TYPE_DATA>> newInstanceTypeRows() {
		return new TaggedTableHashcodeRows<Integer, TYPE_TAG,TYPE_DATA,TaggedTableHashcodeRow<TYPE_TAG, TYPE_DATA>>();
	}
	
}
