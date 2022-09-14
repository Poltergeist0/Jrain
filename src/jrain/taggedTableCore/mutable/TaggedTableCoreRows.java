package jrain.taggedTableCore.mutable;

import java.util.Map.Entry;
import java.util.function.Predicate;

import jrain.indexedSet.mutable.IndexedSet;

/**
 * @author poltergeist0
 *
 * Collection of rows for use with tagged tables.
 * 
 * @param <TYPE_KEY> is the data type to be used as key for rows
 * @param <TYPE_TAG> is the data type to be used for the tag/name of all columns
 * @param <TYPE_DATA> is the data type to be used for the value of all columns
 * @param <TYPE_ROW> is the data type to be used for rows
 */
public abstract class TaggedTableCoreRows
<
TYPE_KEY extends Object,TYPE_TAG extends Object,TYPE_DATA extends Object,
TYPE_ROW extends TaggedTableCoreRow<TYPE_TAG,TYPE_DATA>
> 
extends IndexedSet<TYPE_KEY,TYPE_ROW>{


	/**
	 * Separator used in the toString method to separate rows
	 */
	public final static String SEPARATOR="\n";

	/**
	 * Create a new empty row collection with no columns
	 */
	public TaggedTableCoreRows(){
		super();
	}
		
	/**
	 * Copy constructor
	 * 
	 * @param <TYPE_TABLE> is the data type of the class that extends {@link TaggedTableCoreRows}
	 * @param original is the original {@link TaggedTableCoreRows}
	 * @param deepCopy if true attempts to perform a deep copy of the cells
	 */
	public <TYPE_TABLE extends TaggedTableCoreRows<TYPE_KEY,TYPE_TAG,TYPE_DATA,TYPE_ROW>> 
	TaggedTableCoreRows(TYPE_TABLE original,boolean deepCopy){
		super(original,deepCopy);
	}
	
	/**
	 * Filtered copy constructor.
	 * Only copies rows that pass the predicate test.
	 * 
	 * @param <TYPE_TABLE> is the data type of the class that extends {@link TaggedTableCoreRows}
	 * @param <TYPE_PREDICATE> is the data type of the class that extends {@link Predicate}
	 * @param t is the original {@link TaggedTableCoreRows}
	 * @param pre is the predicate that selects which rows are copied
	 * @param deepCopy if true attempts to perform a deep copy of the rows
	 */
	public <TYPE_TABLE extends TaggedTableCoreRows<TYPE_KEY,TYPE_TAG,TYPE_DATA,TYPE_ROW>, TYPE_PREDICATE extends Predicate<Entry<TYPE_KEY,TYPE_ROW>>> 
	TaggedTableCoreRows(
			TYPE_TABLE t,
			TYPE_PREDICATE pre,boolean deepCopy
			)
	{
		super(t,pre,deepCopy);
	}
	
	/**
	 * Change the name of an existing column
	 * 
	 * @param columnName is the original name
	 * @param newName is the new name
	 * @return false if the column does not exist or the operation failed. Otherwise, true.
	 */
	public boolean renameColumn(TYPE_TAG columnName,TYPE_TAG newName){
		IndexedSet<TYPE_KEY, TYPE_ROW>.IndexedSetIterator it = iterator();
		while(it.hasNext()) {
			TaggedTableCoreRow<TYPE_TAG,TYPE_DATA> a = it.next().getValue();
			a.key(columnName, newName);
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		IndexedSet<TYPE_KEY, TYPE_ROW>.IndexedSetIterator it = iterator();
		while(it.hasNext()) {
			TaggedTableCoreRow<TYPE_TAG,TYPE_DATA> a = it.next().getValue();
			result = prime * result + a.hashCode();
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	/**
	 * Check if the current row is identical to a given row.
	 * Rows are identical if they have the same columns with the same values.
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
		TaggedTableCoreRows<TYPE_KEY,TYPE_TAG,TYPE_DATA,TYPE_ROW> other = (TaggedTableCoreRows<TYPE_KEY,TYPE_TAG,TYPE_DATA,TYPE_ROW>) obj;
		if(size()!=other.size()) return false;
		IndexedSet<TYPE_KEY, TYPE_ROW>.IndexedSetIterator it = iterator();
		while(it.hasNext()) {
			Entry<TYPE_KEY, TYPE_ROW> a = it.next();
			if(!other.contains(a.getKey())) return false;//if the current row does exist in the given table, they are not equal
		}
		return true;
	}

	@Override
	public String separatorValue() {
		return TaggedTableCoreRow.SEPARATOR;
	}

	@Override
	public String separatorCell() {
		return IndexedSet.SEPARATOR;
	}
	
	/**
	 * @return the string used to separate rows
	 */
	public String separatorRow() {
		return SEPARATOR;
	}
	
	public static <
	TYPE_KEY extends Object,TYPE_TAG extends Object,TYPE_DATA extends Object,
	TYPE_HEADER_DATA extends TaggedTableCoreHeaderData<TYPE_DATA>,
	TYPE_HEADER extends TaggedTableCoreHeader<TYPE_TAG,TYPE_DATA,TYPE_HEADER_DATA>,
	TYPE_ROW extends TaggedTableCoreRow<TYPE_TAG,TYPE_DATA>, 
	TYPE_TABLE extends TaggedTableCoreRows<TYPE_KEY,TYPE_TAG,TYPE_DATA,TYPE_ROW> 
	> 
	StringBuilder toString(StringBuilder s,TYPE_HEADER h,TYPE_TABLE rs, String sepCell, String sepValue, String sepRow){
		return rs.toString(s, h, sepCell, sepValue, sepRow);
	}
		
	public <
	TYPE_HEADER_DATA extends TaggedTableCoreHeaderData<TYPE_DATA>,
	TYPE_HEADER extends TaggedTableCoreHeader<TYPE_TAG,TYPE_DATA,TYPE_HEADER_DATA>
	> 
	StringBuilder toString(StringBuilder s,TYPE_HEADER h, String sepCell, String sepValue, String sepRow){
		boolean first=true;
		IndexedSet<TYPE_KEY, TYPE_ROW>.IndexedSetIterator it = iterator();
		while(it.hasNext()) {
			if(first) {
				first=false;
			}
			else {
				if(sepRow==null) s.append(SEPARATOR);
				else s.append(sepRow);
			}
			Entry<TYPE_KEY, TYPE_ROW> a = it.next();
			TYPE_ROW r=a.getValue();
			r.toString(s,h,sepCell,sepValue);
		}
		return s;
	}
		
	public String toString(){
		StringBuilder s=new StringBuilder();
		return toString(s,null,this,separatorCell(),separatorValue(),separatorRow()).toString();
	}
		
}
