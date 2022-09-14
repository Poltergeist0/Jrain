package jrain.taggedTable.mutable;

import jrain.taggedTableSimple.mutable.TaggedTableSimpleColumnData;
import jrain.identifiable.immutable.Identifiable;
import jrain.polyType.mutable.PolyType;

/**
 * @author poltergeist0
 *
 * Column data for use with {@link TaggedTableColumn}.
 * It is composed of a {@link TaggedTableSimpleColumnData}, an index and column 
 * constraints.
 * Values are not automatically added to the index if rows already exist, because
 * table headers have no knowledge of table rows. Values must be added by 
 * {@link TaggedTable} right after creation.
 * Columns should never be used directly. Instead all calls should be made 
 * through {@link TaggedTable}.
 * Columns are only inserted in rows when the value to be inserted in the row is
 * different from the default value declared in the column. This is a memory
 * optimization but implies that changing the default value means adding columns
 * to all rows, which is time consuming. For this reason it is advised to select
 * an appropriate default value before starting to insert rows and not to 
 * change the default value afterwards unless really required.
 * The {@code null} value is accepted as default value.
 * 
 * @param <TYPE_DATA> is the data type to be used for the value of all columns
 */
public class TaggedTableColumnData<TYPE_DATA extends Object> 
extends TaggedTableSimpleColumnData<TYPE_DATA>{
	
//	private TaggedTableColumnConstrains con;//constrains
	private TaggedTableIndex ind;
	
	/**
	 * Create a tagged table column data
	 * 
	 * @param defaultValue is the default value for the column
	 */
	public TaggedTableColumnData(TYPE_DATA defaultValue){
		super(defaultValue);
		ind=null;
	}
	
	/**
	 * Copy constructor
	 * 
	 * @param <TYPE_TABLE> is the data type of the original table
	 * @param t is the original table
	 * @param deepCopy if true attempts to deep copy the data
	 */
	public <TYPE_TABLE extends TaggedTableColumnData<TYPE_DATA> > TaggedTableColumnData(TYPE_TABLE t,boolean deepCopy){
		super(t,deepCopy);
		ind=(deepCopy && t.index()!=null)?t.index().deepCopy():t.index();
	}
	
	/**
	 * Check if a column index for the values exists.
	 * 
	 * @return true if there is a column index
	 */
	public boolean hasIndex() {return ((ind==null)?false:true);}
	
	/**
	 * Remove the column index.
	 * 
	 * @return true if the index existed and was removed
	 */
	public boolean removeIndex() {
		if(ind==null)return false;
		ind=null;
		return true;
	}
	
	/**
	 * Create a column index for the values.
	 * Does not automatically add values to the index if rows already exist.
	 * 
	 * @return true if the index was created
	 */
	public boolean createIndex() {
		if(ind!=null) return false;
		ind=new TaggedTableIndex();
		return true;
	}
	
	/**
	 * Add value to index
	 * 
	 * @param id is the row {@link Identifiable}
	 * @param data is the value to add to the index
	 * @return true if the value was successfully added
	 */
	public boolean addToIndex(Identifiable id, PolyType<?> data) {
		if(ind==null)return false;
		return ind.add(data, id);
	}
	
	/**
	 * Remove value from index
	 * 
	 * @param id is the row {@link Identifiable}
	 * @return true if the value was successfully removed
	 */
	public boolean removeFromIndex(Identifiable id) {
		if(ind==null)return false;
		return ind.remove(id);
	}
	
	/**
	 * Moves a row from an old value to a new value. The row is added to the new 
	 * value independently of it existing in the old value.
	 * 
	 * @param id is the row {@link Identifiable}
	 * @param dataOld is the old value
	 * @param dataNew is the new value
	 * @return true if the value was successfully moved
	 */
	public boolean updateIndex(Identifiable id, PolyType<?> dataOld, PolyType<?> dataNew) {
		if(ind==null)return false;
		return ind.update(dataOld, dataNew, id);
	}
	
	/**
	 * Get the index. 
	 * For use only inside {@link TaggedTable}. Do not expose!
	 * 
	 * @return the index
	 */
	public TaggedTableIndex index() {return ind;}
	
	@Override
	public TaggedTableColumnData<TYPE_DATA> deepCopy() {
		return new TaggedTableColumnData<TYPE_DATA>(this,true);
	}

}
