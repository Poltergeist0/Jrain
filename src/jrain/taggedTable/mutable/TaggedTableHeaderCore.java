package jrain.taggedTable.mutable;

import java.util.Map.Entry;
import java.util.function.Predicate;

import jrain.taggedTableCore.mutable.TaggedTableCoreCell;
import jrain.indexedSet.mutable.IndexedSet;
import jrain.taggedTableSimple.mutable.TaggedTableSimpleHeaderCore;
import jrain.identifiable.immutable.Identifiable;
import jrain.polyType.mutable.BooleanPoly;

/**
 * @author poltergeist0
 *
 * Set of columns for use with {@link TaggedTable}.
 * Requires that the first inserted column has unique values per row since it 
 * will be used as index to identify rows. Because of this, it can not be 
 * removed or deleted and its default value is ignored.
 * There is a virtual "Deleted" column that indicates if a row was deleted.
 * This column can not be removed because it is unnamed and is not really a column.
 * There is also an index for that column that can not be removed.
 * Insertion order is preserved.
 * See {@link TaggedTableColumn} for details about a column.
 * 
 * @param <TYPE_TAG> is the data type to be used for the tag/name of all columns
 * @param <TYPE_DATA> is the data type to be used for the value of all columns
 * @param <TYPE_COLUMN> is the data type to be used for the columns
 */
public class TaggedTableHeaderCore<TYPE_TAG extends Object,TYPE_DATA extends Object,TYPE_COLUMN extends TaggedTableColumnData<TYPE_DATA>>
extends TaggedTableSimpleHeaderCore<TYPE_TAG,TYPE_DATA, TYPE_COLUMN>{
	
	/**
	 * Special index for deleted columns 
	 */
	private TaggedTableIndex indexDeleted;
	
//	protected TaggedTableIndex indexUUID;
	
	/**
	 * @return the "Deleted" index
	 */
	protected TaggedTableIndex get() {return indexDeleted;}
	
	/**
	 * Add or update a value in the "Deleted" index
	 * @param id is the row identifiable
	 * @param val is true for a deleted row
	 * @return true if the operation succeeded
	 */
	protected Boolean addUpdateDeleted(Identifiable id, Boolean val) {
		return indexDeleted.add(new BooleanPoly(val), id);
	}
	
	/**
	 * Remove a value from the "Deleted" index
	 * @param id is the row identifiable
	 * @return true if the operation succeeded
	 */
	protected Boolean removeDeleted(Identifiable id) {
		return indexDeleted.remove(id);
	}
	
	@Override
	public <TYPE_HEADER_DATA,TYPE_HEADER extends IndexedSet<TYPE_TAG, TYPE_HEADER_DATA> > 
	StringBuilder toString(StringBuilder s,TYPE_HEADER h,String sepCell, String sepValue){
		super.toString(s, h, sepCell, sepValue);
		if(sepCell==null) s.append(SEPARATOR);
		else s.append(sepCell);
//		TaggedTableCoreCell.toString(s,TaggedTableSimpleColumnData.DELETED,TaggedTableSimpleColumnData.DELETEDFALSE,sepValue);
//		if(sepCell==null) s.append(SEPARATOR);
//		else s.append(sepCell);
		TaggedTableCoreCell.toString(s,"Identifiable","",sepValue);
		return s;
	}
		
	/**
	 * Check if a column has an index associated
	 * 
	 * @param a is the name of the column
	 * @return true if there is an index
	 */
	public boolean hasIndex(String a) {
//		if(TaggedTableSimpleColumnData.DELETED.equals(a)) return true;
//		if("Identifiable".equals(a)) return true;
		if(!super.internal().containsKey(a)) return false;
		return super.internal().get(a).hasIndex();
	}
	
	/**
	 * Remove the index associated with a column
	 * 
	 * @param a is the name of the column
	 * @return true if there was an index and was removed
	 */
	public boolean removeIndex(String a) {
//		if(TaggedTableSimpleColumnData.DELETED.equals(a)) return false;
//		if("Identifiable".equals(a)) return false;
		if(!super.internal().containsKey(a)) return false;
		return super.internal().get(a).removeIndex();
	}
	
	/**
	 * Create an index on a column if it does not have one
	 * 
	 * @param a is the name of the column
	 * @return true if the index did not exist and was created
	 */
	public boolean createIndex(String a) {
//		if(TaggedTableSimpleColumnData.DELETED.equals(a)) return false;
//		if("Identifiable".equals(a)) return false;
		if(!super.internal().containsKey(a)) return false;
		return super.internal().get(a).createIndex();
	}
	    
	/**
	 * @author poltergeist0
	 * 
	 * Predicate used to compact the header by removing all deleted columns
	 *
 * @param <TYPE_TAG> is the data type to be used for the tag/name of all columns
 * @param <TYPE_DATA> is the data type to be used for the value of all columns
 * @param <TYPE_COLUMN> is the data type to be used for the columns
	 */
	protected static class PredicateCompact<TYPE_TAG, TYPE_DATA,TYPE_COLUMN extends TaggedTableColumnData<TYPE_DATA>> extends TaggedTableSimpleHeaderCore.PredicateCompact<TYPE_TAG, TYPE_DATA, TYPE_COLUMN>{//implements Predicate<Entry<TYPE_TAG, TYPE_COLUMN>>{
	}
	
	/**
	 * @return a new tagged table header without the deleted columns
	 */
	public TaggedTableHeaderCore<TYPE_TAG,TYPE_DATA, TYPE_COLUMN> compact() {
		return new TaggedTableHeaderCore<TYPE_TAG,TYPE_DATA, TYPE_COLUMN>(this, new PredicateCompact<TYPE_TAG, TYPE_DATA, TYPE_COLUMN>(), false, false);
	}
	
	/**
	 * Create a tagged table header
	 * 
	 */
	public TaggedTableHeaderCore() {
		super();
//		super.add(TaggedTableSimpleColumnData.DELETED, new TaggedTableColumnData<PolyType<?>>(new BooleanPoly(false)), false);
//		indexUUID=new TaggedTableIndex();
		indexDeleted=new TaggedTableIndex();
	}
		
	/**
	 * Copy constructor
	 * 
	 * @param <TYPE_HEADER> is the data type of the object that extends this class
	 * @param t is the original
	 * @param deepCopy if true, makes a deep copy, otherwise makes a shallow copy
	 */
	public <TYPE_HEADER extends TaggedTableHeaderCore<TYPE_TAG,TYPE_DATA, TYPE_COLUMN>>
	TaggedTableHeaderCore(TYPE_HEADER t,boolean deepCopy){
		super(t,deepCopy);
//		super.add(TaggedTableSimpleColumnData.DELETED, t.value(TaggedTableSimpleColumnData.DELETED), false);
//		indexUUID=new TaggedTableIndex(t.indexUUID,deepCopy);
		indexDeleted=new TaggedTableIndex(t.get(),deepCopy);
	}
	
	/**
	 * Filtered copy constructor.
	 * Uses a given {@link TaggedTableCore} as source for data, a
	 * {@link TaggedTableHashcodeHeader} as source for columns, and only adds rows 
	 * that pass the predicate test
	 * 
	 * @param <TYPE_HEADER> is the data type of the object that extends this class
	 * @param <P> is the data type of the class that extends {@link Predicate}
	 * @param t is the original
	 * @param pre is the predicate that selects which columns are copied
	 * @param overwrite, if true,indicates the current values are to be replaced 
	 * with the new ones, otherwise the current values are kept.
	 * @param deepCopy if true, makes a deep copy, otherwise makes a shallow copy
	 */
	public <TYPE_HEADER extends TaggedTableHeaderCore<TYPE_TAG,TYPE_DATA, TYPE_COLUMN>, P extends Predicate<Entry<TYPE_TAG, TYPE_COLUMN>>> 
	TaggedTableHeaderCore(
			TYPE_HEADER t,
			P pre,
			boolean overwrite,boolean deepCopy
			)
	{
		super(t,pre,overwrite,deepCopy);
		indexDeleted=new TaggedTableIndex(t.get(),deepCopy);
	}
		
	@Override
	public TaggedTableHeaderCore<TYPE_TAG,TYPE_DATA, TYPE_COLUMN> deepCopy() {
		return new TaggedTableHeaderCore<TYPE_TAG,TYPE_DATA, TYPE_COLUMN>(this, true);
	}
	
	/**
	 * @return an iterator for all the entries in the list.
	 */
	@Override
	public TaggedTableHeaderIterator iterator() {
		return new TaggedTableHeaderIterator();
	}
	
	/**
	 * @author poltergeist0
	 *
	 * Iterator for all columns except the virtual "Deleted" column
	 */
	public class TaggedTableHeaderIterator extends TaggedTableSimpleHeaderIterator { 
	      
	    // constructor 
		protected TaggedTableHeaderIterator() { 
	        //it=cells.entrySet().iterator();
			super();
	    } 
	} 

}
