package jrain.taggedTableSimple.mutable;

import java.util.Map.Entry;
import java.util.function.Predicate;

import jrain.taggedTableCore.mutable.TaggedTableCoreCell;
import jrain.indexedSet.mutable.IndexedSet;
import jrain.taggedTableHashcode.mutable.TaggedTableHashcodeRow;

/**
 * @author poltergeist0
 *
 * Simple row for use with {@link TaggedTableSimple}.
 * It is composed of columns that have tags/names associated with them. 
 * Those are used to address each column, so no duplicates are allowed. 
 * Columns are described in {@link TaggedTableSimpleHeader}. Any column present in the
 * {@code TaggedTableHeader} that does not exist in a row is assumed to have the 
 * default value declared in {@code TaggedTableHeader}.
 * There is also a virtual "Deleted" column that indicates if the row was deleted.
 * This column can not be removed because it is unnamed and is not really a column.
 * If the data type of the tags/names is String, these are case sensitive.
 * The {@code null} value is accepted either as tag/name or value.
 * Column order is not preserved.
 * 
 * @param <TYPE_TAG> is the data type to be used for the tag/name of all columns
 * @param <TYPE_DATA> is the data type to be used for the value of all columns
 */
public class TaggedTableSimpleRow<TYPE_TAG extends Object,TYPE_DATA extends Object> 
extends TaggedTableHashcodeRow<TYPE_TAG, TYPE_DATA>{

	/**
	 * Flag used to mark the row as deleted
	 */
	private boolean dlt;
	
	/**
	 * Create a new simple row with no columns
	 */
	public TaggedTableSimpleRow(){
		super();
		dlt=false;
	}
		
	/**
	 * Copy constructor
	 * 
	 * @param <TYPE_ROW> is the data type for the row
	 * @param original is the original row
	 * @param deepCopy if true attempts to perform a deep copy of the rows
	 */
	public <TYPE_ROW extends TaggedTableSimpleRow<TYPE_TAG,TYPE_DATA> >
	TaggedTableSimpleRow(TYPE_ROW original,boolean deepCopy){
		super(original,deepCopy);
		dlt=original.isDeleted();
	}
	
	/**
	 * Filtered copy constructor.
	 * 
	 * @param <TYPE_ROW> is the data type for the row
	 * @param <P> is the data type of the class that extends {@link Predicate}
	 * @param t is the original row
	 * @param pre is the {@link Predicate} that selects which rows are copied
	 * @param deepCopy if true attempts to perform a deep copy of the rows
	 */
	public <TYPE_TABLE extends TaggedTableSimpleRow<TYPE_TAG, TYPE_DATA>, P extends Predicate<Entry<TYPE_TAG,TYPE_DATA>>> 
	TaggedTableSimpleRow(
			TYPE_TABLE t,
			P pre,
			boolean deepCopy
			)
	{
		super(t,pre,deepCopy);
	}
	
	/**
	 * @return true if the row is marked as deleted
	 */
	public boolean isDeleted() {return dlt;}
	
	/**
	 * Delete the row
	 */
	public void delete() {dlt=true;}
	
	/**
	 * Undelete the row
	 */
	public void undelete() {dlt=false;}

	/**
	 * @return a new tagged table row without the deleted rows
	 */
	public <TYPE_HEADER extends TaggedTableSimpleHeader<TYPE_TAG,TYPE_DATA> > 
	TaggedTableSimpleRow<TYPE_TAG,TYPE_DATA> compact(TYPE_HEADER ref) {
		TaggedTableSimpleRow<TYPE_TAG,TYPE_DATA> t=new TaggedTableSimpleRow<>();
		IndexedSet<TYPE_TAG, TaggedTableSimpleColumnData<TYPE_DATA>>.IndexedSetIterator it = ref.iterator();
		while(it.hasNext()) {
			Entry<TYPE_TAG, TaggedTableSimpleColumnData<TYPE_DATA>> a = it.next();
			if(!a.getValue().isDeleted() && contains(a.getKey())) t.add(a.getKey(),value(a.getKey()), false);
		}
		return t;
	}
	
	public static <TYPE_TAG,TYPE_HEADER_DATA,TYPE_ROW_DATA,TYPE_HEADER_TABLE extends TaggedTableSimpleHeader<TYPE_TAG, TYPE_HEADER_DATA>,TYPE_ROW_TABLE extends TaggedTableSimpleRow<TYPE_TAG, TYPE_ROW_DATA>> 
	StringBuilder toString(StringBuilder s,TYPE_HEADER_TABLE h,TYPE_ROW_TABLE r, String sepCell, String sepValue){
		return r.toString(s, h, sepCell, sepValue);
	}
		
	public <TYPE_HEADER_DATA,TYPE_HEADER_TABLE extends IndexedSet<TYPE_TAG, TYPE_HEADER_DATA> > 
	StringBuilder toString(StringBuilder s,TYPE_HEADER_TABLE h, String sepCell, String sepValue){
		super.toString(s,h,sepCell,sepValue);
		if(sepCell==null) s.append(SEPARATOR);
		else s.append(sepCell);
		TaggedTableCoreCell.toString(s,TaggedTableSimpleColumnData.DELETED,((dlt)?TaggedTableSimpleColumnData.DELETEDTRUE:TaggedTableSimpleColumnData.DELETEDFALSE),sepValue);
		return s;
	}
		
	public String toString(){
		StringBuilder s=new StringBuilder();
		return toString(s,null,separatorCell(),separatorValue()).toString();
	}
		
	@Override
	public TaggedTableSimpleRow<TYPE_TAG, TYPE_DATA> deepCopy() {
		return new TaggedTableSimpleRow<>(this, true);
	}
	
}
