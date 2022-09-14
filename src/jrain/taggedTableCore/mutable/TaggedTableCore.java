package jrain.taggedTableCore.mutable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.function.Predicate;

import jrain.deepCopy.DeepCopy;
import jrain.entry.mutable.Pair;
import jrain.entry.mutable.PairOfArrayList;
import jrain.map.mutable.HashMultiMap;
import jrain.returnAnswer.mutable.ReturnAnswer;
import jrain.indexedSet.mutable.IndexedSet;

/**
 * @author poltergeist0
 *
 * Tagged table core functionality.
 * Implements a table which supports with multiple named (tagged) columns.
 * Adding a column does not modify any rows.
 * Columns are indexed by their name.
 * Columns names (tags) can not be repeated.
 * Column order is kept.
 * Rows are indexed by their key. This key must be on the first column added to 
 * the table. This column can not be deleted or removed.
 * Rows can not be repeated (same key).
 * Row data can be repeated as long as the row is not completely identical to 
 * another existing row (same row key).
 * Rows can not be added if there are no columns.
 * Row order is not kept.
 * Nulls are acceptable as values/default values or column name.
 * Rows are immutable so, to change a value of a column in a row, the old row 
 * must be removed and the row with the modification must be added (as 
 * implemented by the friendly method removeRow).
 * To keep the table fast, removing a column only removes it from the header but
 * keeps it in rows otherwise, in the worst case scenario, the entire table 
 * would have to be recreated since rows are indexed by hash and any 
 * modification to a row changes the hash.
 *
 * @param <TYPE_KEY> is the data type for the row key
 * @param <TYPE_TAG> is the data type for the column tag
 * @param <TYPE_HEADER_DATA> is the data type for the header data
 * @param <TYPE_ROW_DATA> is the data type for the row data
 * @param <TYPE_HEADER> is the data type for the header, which is dependent on TYPE_TAG, TYPE_ROW_DATA and TYPE_HEADER_DATA
 * @param <TYPE_ROW> is the data type for the row, which is dependent on TYPE_TAG and TYPE_ROW_DATA
 * @param <TYPE_ROWS> is the data type for the collection of rows, which is dependent on TYPE_TAG, TYPE_ROW_DATA and TYPE_HEADER_DATA
 */
public abstract class TaggedTableCore
<
TYPE_KEY extends Object,
TYPE_TAG extends Object,
TYPE_HEADER_DATA extends TaggedTableCoreHeaderData<TYPE_ROW_DATA>,
TYPE_ROW_DATA extends Object,
TYPE_HEADER extends TaggedTableCoreHeader<TYPE_TAG,TYPE_ROW_DATA,TYPE_HEADER_DATA>,
TYPE_ROW extends TaggedTableCoreRow<TYPE_TAG,TYPE_ROW_DATA>,
TYPE_ROWS extends TaggedTableCoreRows<TYPE_KEY, TYPE_TAG,TYPE_ROW_DATA,TYPE_ROW>
>
implements DeepCopy<TaggedTableCore<TYPE_KEY,TYPE_TAG,TYPE_HEADER_DATA,TYPE_ROW_DATA,TYPE_HEADER,TYPE_ROW,TYPE_ROWS> >{
	
	/**
	 * collection of all columns in order of insertion
	 */
	private TYPE_HEADER cols;
	
	/**
	 * collection of all rows
	 */
	private TYPE_ROWS rows;

	/**
	 * separator used in the toString method
	 */
	public final static String SEPARATOR="\n";

	/**
	 * Method to get the row key.
	 * Must be overridden by the class that implements a specific tagged table.
	 * 
	 * @param r is the row
	 * @return the key of the given row
	 */
	protected abstract TYPE_KEY getKey(TYPE_ROW r);
	
	/**
	 * Method to get an empty instance of a collection of rows.
	 * Must be overridden by the class that implements a specific tagged table.
	 * 
	 * @return a rows instance
	 */
	protected abstract TYPE_ROWS newInstanceTypeRows();
	
	/**
	 * Create a new simple tagged table with no rows.
	 * 
	 * @param h is the header for the table
	 */
	public TaggedTableCore(TYPE_HEADER h){
		cols=h;
		rows=newInstanceTypeRows();
	}
	
	/**
	 * Copy constructor uses a given {@link TaggedTableCore} as source for 
	 * columns but does not add any rows if copyRows is false
	 * 
	 * @param <TYPE_TABLE> is the data type of the tagged table
	 * @param t is the original table
	 * @param copyRows if true copies all rows to the new table. Otherwise, no rows are copied
	 * @param deepCopy if true attempts to perform a deep copy of the rows and columns
	 */
	public <TYPE_TABLE extends TaggedTableCore<TYPE_KEY,TYPE_TAG,TYPE_HEADER_DATA,TYPE_ROW_DATA,TYPE_HEADER,TYPE_ROW,TYPE_ROWS> > 
	TaggedTableCore(TYPE_TABLE t,boolean copyRows,boolean deepCopy){
		cols=((deepCopy)?DeepCopy.deepCopy(t.getColumns()):t.getColumns());
		if(copyRows){
			rows=newInstanceTypeRows();
			rows.add(t.getRows(), null, false, deepCopy);
		}
		else {
			rows=newInstanceTypeRows();
		}
	}
	
	/**
	 * Filtered copy constructor.
	 * Uses a given {@link TaggedTableCore} as source for data, a
	 * {@link TaggedTableCoreHeader} as source for columns, and only adds rows 
	 * that pass the predicate test
	 * 
	 * @param <TYPE_TABLE> is the data type of the tagged table
	 * @param <TYPE_PREDICATE> is the data type of the predicate
	 * @param t is the original table
	 * @param h is the header with the new columns
	 * @param pre is the predicate that selects which rows are copied
	 * @param deepCopy if true attempts to perform a deep copy of the rows
	 */
	public 
	<
	TYPE_TABLE extends TaggedTableCore<TYPE_KEY,TYPE_TAG,TYPE_HEADER_DATA,TYPE_ROW_DATA,TYPE_HEADER,TYPE_ROW,TYPE_ROWS>, 
	TYPE_PREDICATE extends Predicate<Entry<TYPE_KEY,TYPE_ROW>> 
	> 
	TaggedTableCore(
			TYPE_TABLE t,
			TYPE_HEADER h,
			TYPE_PREDICATE pre,boolean deepCopy
			)
	{
		cols=((deepCopy)?DeepCopy.deepCopy(h):h);
		rows=newInstanceTypeRows();
		rows.add(t.getRows(), pre, false, deepCopy);
	}
	
	/**
	 * @return the number of columns in this table
	 */
	public int ncols(){return cols.size();}
	
	/**
	 * @return the number of rows in this table
	 */
	public int nrows(){return rows.size();}
	
	/**
	 * Get direct access to the header.
	 * For use only in classes that extend this one.
	 * 
	 * @return the table header that contains all column names and default values 
	 */
	protected TYPE_HEADER getColumns(){
		return cols;
	}
	
	/**
	 * @return a deep copy of the header
	 */
	public TYPE_HEADER getHeader(){
		return DeepCopy.deepCopy(cols);
	}
	
	/**
	 * Get a column by name
	 * 
	 * @param name is the name of the column to return
	 * @return the named column
	 */
	public TYPE_HEADER_DATA getColumn(TYPE_TAG name){
		return cols.value(name);
	}
	
	/**
	 * Get a column by number
	 * 
	 * @param n is the insertion position of the column to return
	 * @return the named column
	 */
	public TYPE_HEADER_DATA getColumn(int n){
		return cols.value(n);
	}
	
	/**
	 * Get a column name by number
	 * 
	 * @param n is the insertion position of the column to return
	 * @return the named column
	 */
	public TYPE_TAG getColumnName(int n){
		return cols.key(n);
	}
	
	/**
	 * Change the name of an existing column
	 * 
	 * @param columnName is the name of the existing column
	 * @param newName is the new name for the column
	 * @return false if the column does not exist or the operation failed. Otherwise, true.
	 */
	public boolean renameColumn(TYPE_TAG columnName,TYPE_TAG newName){
		boolean b=cols.key(columnName, newName);
		if(b) rows.renameColumn(columnName, newName);
		return b;
	}
	
	/**
	 * Adds a column, if it does not exist.
	 * If the column exists and overwrite is true, replaces the value, otherwise 
	 * ignores.
	 * 
	 * @param name is the name of the column
	 * @param defaultValue is the default value of the column
	 * @param overwrite indicates the current values are to be replaced with 
	 * the new ones, if true, otherwise, the current values are kept.
	 * @return a list of the columns that could not be added/replaced
	 */
	public ArrayList<Entry<TYPE_TAG,TYPE_HEADER_DATA>> addColumn(TYPE_TAG name,TYPE_HEADER_DATA defaultValue,boolean overwrite){
		return cols.add(name,defaultValue, overwrite);
	}
	
	/**
	 * Adds a column, if it does not exist.
	 * If the column exists and overwrite is true, replaces the value, otherwise 
	 * ignores.
	 * 
	 * @param <TYPE_ENTRY> is the data type of the class that extends {@link Entry}
	 * @param t is the column to add
	 * @param overwrite indicates the current values are to be replaces with 
	 * the new ones, if true, otherwise, the current values are kept.
	 * @return a list of the columns that could not be added/replaced
	 */
	public <TYPE_ENTRY extends Entry<TYPE_TAG,TYPE_HEADER_DATA>> 
	ArrayList<Entry<TYPE_TAG,TYPE_HEADER_DATA>> addColumn(TYPE_ENTRY t,boolean overwrite){
		return cols.add(t, overwrite);
	}
	
	/**
	 * Adds a column, if it does not exist.
	 * If the column exists and overwrite is true, replaces the value, 
	 * otherwise ignores.
	 * 
	 * @param t is the column to add
	 * @param overwrite indicates the current values are to be replaces with 
	 * the new ones, if true, otherwise, the current values are kept.
	 * @return a list of the columns that could not be added/replaced
	 */
	public ArrayList<Entry<TYPE_TAG,TYPE_HEADER_DATA>> addColumn(TaggedTableCoreCell<TYPE_TAG,TYPE_HEADER_DATA> t,boolean overwrite){
		return cols.add(t,overwrite);
	}
	
	/**
	 * Adds columns, that do not exist.
	 * If the column exists and overwrite is true, replaces the value, otherwise 
	 * ignores.
	 * 
	 * @param t are the columns to add
	 * @param overwrite indicates the current values are to be replaces with 
	 * the new ones, if true, otherwise, the current values are kept.
	 * @return a list of the columns that could not be added/replaced
	 */
	public ArrayList<Entry<TYPE_TAG,TYPE_HEADER_DATA>> addColumns(TYPE_HEADER columns,boolean overwrite){
		return cols.add(columns, overwrite);
	}
	
	/**
	 * Adds columns, that do not exist.
	 * If the column exists and overwrite is true, replaces the value, otherwise 
	 * ignores.
	 * 
	 * @param <TYPE_ENTRY> is the data type of the class that extends {@link Entry}
	 * @param columns are the columns to add
	 * @param overwrite indicates the current values are to be replaces with 
	 * the new ones, if true, otherwise, the current values are kept.
	 * @return true if the column was added
	 */
	public <TYPE_ENTRY extends Entry<TYPE_TAG,TYPE_HEADER_DATA>> 
	ArrayList<Entry<TYPE_TAG,TYPE_HEADER_DATA>> addColumns(Collection<TYPE_ENTRY> columns,boolean overwrite){
		return cols.add(columns, overwrite);
	}
	
	/**
	 * Adds columns, if they do not exist.
	 * If a column exists and overwrite is true, replaces the value, 
	 * otherwise ignores.
	 * 
	 * @param <TYPE_PREDICATE> is the data type of the class that extends {@link Predicate}
	 * @param t are the columns to add
	 * @param pre is the {@link Predicate} that selects the columns to add
	 * @param overwrite , if true,indicates the current values are to be replaced 
	 * with the new ones, otherwise the current values are kept.
	 * @return a list of the cells that passed the test but could not be added/replaced
	 */
	public <TYPE_PREDICATE extends Predicate<Entry<TYPE_TAG,TYPE_HEADER_DATA>>> 
	ArrayList<Entry<TYPE_TAG,TYPE_HEADER_DATA>> addColumns(
			TYPE_HEADER t,
			TYPE_PREDICATE pre,
			boolean overwrite
			)
	{
		return cols.add(t,pre,overwrite,false);
	}
	
	/**
	 * Remove a column by name
	 * 
	 * @param columnName is the name of the column to remove
	 * @return a pair with a list of names of the columns that failed to be 
	 * removed as the key, and a list with the removed columns as the value
	 */
	public PairOfArrayList<TYPE_TAG,Entry<TYPE_TAG,TYPE_HEADER_DATA>,ArrayList<TYPE_TAG>,ArrayList<Entry<TYPE_TAG,TYPE_HEADER_DATA>>> removeColumn(TYPE_TAG columnName){
		return cols.remove(columnName);
	}
			
	/**
	 * Remove a column by insertion position
	 * 
	 * @param columnName is the position of the column to remove
	 * @return a pair with a list of names of the columns that failed to be 
	 * removed as the key, and a list with the removed columns as the value
	 */
	public PairOfArrayList<Integer,Entry<TYPE_TAG,TYPE_HEADER_DATA>,ArrayList<Integer>,ArrayList<Entry<TYPE_TAG,TYPE_HEADER_DATA>>> removeColumn(int n) {
		return cols.remove(n);
	}
			
	/**
	 * Remove columns given by a {@link Collection}.
	 * Returns a pair with a list of keys for the cells that failed to be 
	 * removed as the key, and a list with the removed cells as the value.
	 * 
	 * @param r is the list of columns to remove
	 * @return a pair with a list of names of the columns that failed to be 
	 * removed as the key, and a list with the removed columns as the value
	 */
	public PairOfArrayList<TYPE_TAG,Entry<TYPE_TAG,TYPE_HEADER_DATA>,ArrayList<TYPE_TAG>,ArrayList<Entry<TYPE_TAG,TYPE_HEADER_DATA>>> removeColumns(Collection<TYPE_TAG> r){
		return cols.remove(r);
	}
	
	/**
	 * Remove columns.
	 * Returns a pair with a list of keys for the cells that failed to be 
	 * removed as the key, and a list with the removed cells as the value.
	 * 
	 * @param <TYPE_TABLE> is the data type of the class that extends {@link TaggedTableCoreHeader}
	 * @param table is the list of columns to remove
	 * @return a pair with a list of names of the columns that failed to be 
	 * removed as the key, and a list with the removed columns as the value
	 */
	public PairOfArrayList<TYPE_TAG,Entry<TYPE_TAG,TYPE_HEADER_DATA>,ArrayList<TYPE_TAG>,ArrayList<Entry<TYPE_TAG,TYPE_HEADER_DATA>>> removeColumns(TYPE_HEADER table){
		return cols.remove(table);
	}
	
	/**
	 * Remove columns that pass a predicate test.
	 * 
	 * @param <TYPE_PREDICATE> is the data type of the class that extends {@link Predicate}
	 * @param pre is the {@link Predicate} that selects the columns to remove
	 * @return a pair with a list of names of the columns that failed to be 
	 * removed as the key, and a list with the removed columns as the value
	 */
	public <TYPE_PREDICATE extends Predicate<Entry<TYPE_TAG,TYPE_HEADER_DATA>>> 
	PairOfArrayList<TYPE_TAG,Entry<TYPE_TAG,TYPE_HEADER_DATA>,ArrayList<TYPE_TAG>,ArrayList<Entry<TYPE_TAG,TYPE_HEADER_DATA>>> removeColumns(TYPE_PREDICATE pre){
		return cols.remove(pre);//remove rows with given keys and return the rows
	}
	
	/**
	 * @param columnName is the name of the column to verify
	 * @return true if the column exists
	 */
	public boolean containsColumn(TYPE_TAG columnName){
		return cols.contains(columnName);
	}
	
	/**
	 * @return an {@link Iterator} for columns
	 */
	public TYPE_HEADER.IndexedSetIterator columnsIterator() {return cols.iterator();}
		
	/**
	 * Add a row.
	 * Non existing columns will be ignored and not added to the table. This 
	 * works as a column filter.
	 * The row actually added may differ from the one passed to the method
	 * since columns that do not exist in the table may not be added.
	 * 
	 * @param row is the row to be added
	 * @param overwrite if true and the rows exists, overwrites it. Otherwise keeps the original.
	 * @return a list with the rows that failed to be added/replaced
	 */
	public ArrayList<Entry<TYPE_KEY,TYPE_ROW>> addRow(TYPE_ROW row,boolean overwrite){
		if(cols.size()<=0) {
			ArrayList<Entry<TYPE_KEY,TYPE_ROW>> a=new ArrayList<>();
			a.add(new Pair<>(getKey(row),row));
			return a;
		}
		return rows.add(getKey(row),row, overwrite);
	}
	
	/**
	 * Add multiple rows.
	 * Non existing columns will be ignored and not added to the table. This 
	 * works as a column filter.
	 * The row actually added may differ from the one passed to the method
	 * since columns that do not exist in the table are not added.
	 * 
	 * @param <TYPE_TABLE> is the data type of the table
	 * @param table with the rows to be added
	 * @param overwrite if true and the rows exists, overwrites it. Otherwise keeps the original.
	 * @return a list with the rows that failed to be added/replaced 
	 */
	public <TYPE_TABLE extends TaggedTableCore<TYPE_KEY,TYPE_TAG,TYPE_HEADER_DATA,TYPE_ROW_DATA,TYPE_HEADER,TYPE_ROW,TYPE_ROWS> >
	ArrayList<Entry<TYPE_KEY,TYPE_ROW>> addRows(TYPE_TABLE table,boolean overwrite) {
		ArrayList<Entry<TYPE_KEY,TYPE_ROW>> fail=new ArrayList<>();
		TYPE_ROWS.IndexedSetIterator itr = table.rowsIterator();
		while(itr.hasNext()){
			Entry<TYPE_KEY, TYPE_ROW> a = itr.next();
			fail.addAll(addRow(a.getValue(),overwrite));
		}
		return fail;
	}
	
	/**
	 * Add multiple rows.
	 * Non existing columns will be ignored and not added to the table. This 
	 * works as a column filter.
	 * The row actually added may differ from the one passed to the method
	 * since columns that do not exist in the table are not added.
	 * 
	 * @param table with the rows to be added
	 * @param overwrite if true and the rows exists, overwrites it. Otherwise keeps the original.
	 * @return a list with the rows that failed to be added/replaced 
	 */
	public ArrayList<Entry<TYPE_KEY,TYPE_ROW>> addRows(TYPE_ROWS table,boolean overwrite) {
		ArrayList<Entry<TYPE_KEY,TYPE_ROW>> fail=new ArrayList<>();
		TYPE_ROWS.IndexedSetIterator itr = table.iterator();
		while(itr.hasNext()){
			Entry<TYPE_KEY, TYPE_ROW> a = itr.next();
			fail.addAll(addRow(a.getValue(),overwrite));
		}
		return fail;
	}
	
	/**
	 * Add multiple rows.
	 * Non existing columns will be ignored and not added to the table. This 
	 * works as a column filter.
	 * The row actually added may differ from the one passed to the method
	 * since columns that do not exist in the table are not added.
	 * 
	 * @param table with the rows to be added
	 * @param overwrite if true and the rows exists, overwrites it. Otherwise keeps the original.
	 * @return a list with the rows that failed to be added/replaced 
	 */
	public ArrayList<Entry<TYPE_KEY,TYPE_ROW>> addRows(Collection<TYPE_ROW> table,boolean overwrite) {
		ArrayList<Entry<TYPE_KEY,TYPE_ROW>> ok=new ArrayList<>();
		Iterator<TYPE_ROW> it = table.iterator();
		while(it.hasNext()){
			ok.addAll(addRow(it.next(),overwrite));
		}
		return ok;
	}
	
	/**
	 * Add multiple rows from another table using a predicate.
	 * 
	 * @param <TYPE_TABLE> is the data type of the table
	 * @param <TYPE_PREDICATE> is the data type of the class that extends {@link Predicate}
	 * @param table with the rows to be added
	 * @param pre is the {@link Predicate}
	 * @param overwrite if true and the rows exists, overwrites it. Otherwise keeps the original.
	 * @return a list with the rows that failed to be added/replaced 
	 */
	public <
	TYPE_TABLE extends TaggedTableCore<TYPE_KEY,TYPE_TAG,TYPE_HEADER_DATA,TYPE_ROW_DATA,TYPE_HEADER,TYPE_ROW,TYPE_ROWS>, 
	TYPE_PREDICATE extends Predicate<Entry<TYPE_KEY,TYPE_ROW>> 
	>
	ArrayList<Entry<TYPE_KEY,TYPE_ROW>> addRows(
			TYPE_TABLE table,
			TYPE_PREDICATE pre,
			boolean overwrite
			)
	{
		ArrayList<Entry<TYPE_KEY,TYPE_ROW>> fail=new ArrayList<>();
		TYPE_ROWS.IndexedSetIterator itr = table.rowsIterator();
		while(itr.hasNext()){
			Entry<TYPE_KEY, TYPE_ROW> a = itr.next();
			if(pre.test(a)) fail.addAll(addRow(a.getValue(),overwrite));
		}
		return fail;
	}
	
	/**
	 * @return a copy of the list of all the row keys
	 */
	public ArrayList<TYPE_KEY> getRowKeys(){
		return DeepCopy.deepCopy(rows.keys());
	}
	
	/**
	 * @return all rows
	 */
	protected TYPE_ROWS getRows(){
		return rows;
	}
		
	/**
	 * @param <TYPE_PREDICATE> is the data type of the class that extends {@link Predicate}
	 * @param pre is the {@link Predicate}
	 * @return the rows that pass the predicate test
	 */
	public <TYPE_PREDICATE extends Predicate<Entry<TYPE_KEY,TYPE_ROW>> >
	TYPE_ROWS getRows(TYPE_PREDICATE pre){
		TYPE_ROWS a = newInstanceTypeRows();
		a.add(rows,pre,true);
		return a;
	}
		
	/**
	 * Get a row by its key.
	 * 
	 * @param k is the key that identifies the row
	 * @return a {@link ReturnAnswer} with the result
	 */
	public ReturnAnswer<TYPE_ROW> getRow(TYPE_KEY k){
		if(!rows.contains(k)) return new ReturnAnswer<TYPE_ROW>(false);
		return new ReturnAnswer<TYPE_ROW>(DeepCopy.deepCopy(rows.value(k)));
	}
	
	/**
	 * Get rows by their keys.
	 * 
	 * @param r is a {@link Collection} of row keys
	 * @return the rows that exist
	 */
	public TYPE_ROWS getRows(Collection<TYPE_KEY> r){
		TYPE_ROWS s=newInstanceTypeRows();
		Iterator<TYPE_KEY> it = r.iterator();
		while(it.hasNext()){
			TYPE_KEY k = it.next();
			ReturnAnswer<TYPE_ROW> bb=getRow(k);
			if(bb.is()) {//success
				s.add(k,bb.get(), false);
			}
		}
		return s;
	}
	
	/**
	 * Remove a row
	 * 
	 * @param k is the key of the row to remove
	 * @return a pair with a list of keys for the rows that failed to be 
	 * removed as the key, and a list with the removed rows as the value
	 */
	public PairOfArrayList<TYPE_KEY,Entry<TYPE_KEY,TYPE_ROW>,ArrayList<TYPE_KEY>,ArrayList<Entry<TYPE_KEY,TYPE_ROW>>> removeRow(TYPE_KEY row){
		return rows.remove(row);
	}
	
	/**
	 * Remove a row
	 * 
	 * @param k is the row to remove
	 * @return a pair with a list of keys for the rows that failed to be 
	 * removed as the key, and a list with the removed rows as the value
	 */
	public PairOfArrayList<TYPE_KEY,Entry<TYPE_KEY,TYPE_ROW>,ArrayList<TYPE_KEY>,ArrayList<Entry<TYPE_KEY,TYPE_ROW>>> removeRow(TYPE_ROW row){
		return rows.remove(getKey(row));
	}
	
	/**
	 * Remove rows.
	 * 
	 * @param r is a {@link Collection} of rows' keys to remove
	 * @return a pair with a list of keys for the rows that failed to be 
	 * removed as the key, and a list with the removed rows as the value
	 */
	public PairOfArrayList<TYPE_KEY,Entry<TYPE_KEY,TYPE_ROW>,ArrayList<TYPE_KEY>,ArrayList<Entry<TYPE_KEY,TYPE_ROW>>> removeRows(Collection<TYPE_KEY> r){
		return rows.remove(r);
	}
	
	/**
	 * Remove rows.
	 * 
	 * @param <TYPE_TABLE> is the data type of the class that extends {@link TaggedTableCore}
	 * @param table is the table with the rows to remove
	 * @return a pair with a list of keys for the rows that failed to be 
	 * removed as the key, and a list with the removed rows as the value
	 */
	public <TYPE_TABLE extends TaggedTableCore<TYPE_KEY,TYPE_TAG,TYPE_HEADER_DATA,TYPE_ROW_DATA,TYPE_HEADER,TYPE_ROW,TYPE_ROWS> >
	PairOfArrayList<TYPE_KEY,Entry<TYPE_KEY,TYPE_ROW>,ArrayList<TYPE_KEY>,ArrayList<Entry<TYPE_KEY,TYPE_ROW>>> removeRows(TYPE_TABLE table){
		return rows.remove(table.getRows());
	}
	
	/**
	 * Remove all rows that pass the predicate test.
	 * 
	 * @param <TYPE_PREDICATE> is the data type of the class that extends {@link Predicate}
	 * @param pre is the {@link Predicate}
	 * @return a pair with a list of keys for the rows that failed to be 
	 * removed as the key, and a list with the removed rows as the value
	 */
	public <TYPE_PREDICATE extends Predicate<Entry<TYPE_KEY,TYPE_ROW>> >
	PairOfArrayList<TYPE_KEY,Entry<TYPE_KEY,TYPE_ROW>,ArrayList<TYPE_KEY>,ArrayList<Entry<TYPE_KEY,TYPE_ROW>>> removeRows(TYPE_PREDICATE pre){
		return rows.remove(pre);
	}
	
	/**
	 * Replace a row with another.
	 * This is just a friendly method that removes the old row and adds the new
	 * row and may not be used at all.
	 * 
	 * @param row is the old row to remove
	 * @param newRow is the new row to be added
	 * @return true if adding the row succeeded
	 */
	public boolean replaceRow(
			TYPE_KEY row,
			TYPE_ROW newRow
			) 
	{
		return rows.value(row, newRow);
	}	
	
	/**
	 * @return an {@link Iterator} for rows
	 */
	public TYPE_ROWS.IndexedSetIterator rowsIterator() {return rows.iterator();}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cols.hashCode();
		result = prime * result + rows.hashCode();
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	/**
	 * Check if the current table is identical to a given table.
	 * Tables are identical if they have the same columns with the same rows.
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
		TaggedTableCore<TYPE_KEY,TYPE_TAG,TYPE_HEADER_DATA,TYPE_ROW_DATA,TYPE_HEADER,TYPE_ROW,TYPE_ROWS> other = (TaggedTableCore<TYPE_KEY,TYPE_TAG,TYPE_HEADER_DATA,TYPE_ROW_DATA,TYPE_HEADER,TYPE_ROW,TYPE_ROWS>) obj;
		if(cols.size()!=other.cols.size()) return false;
		if(cols.size()!=TaggedTableCoreHeader.commonKeys(cols, other.cols).size()) return false;//does not have the same columns
		if(rows.size()!=other.rows.size()) return false;
		TYPE_ROWS.IndexedSetIterator it = rows.iterator();
		while(it.hasNext()) {
			Entry<TYPE_KEY, TYPE_ROW> a = it.next();
			if(!other.rows.contains(a.getKey())) return false;//if the current row does exist in the given table, they are not equal
			if(!a.getValue().equals(other.rows.value(a.getKey()))) return false;//rows (data) are different
		}
		return true;
	}
	
	/**
	 * Get the separator, used in the toString method to separate the key and 
	 * value, via a method so that it can be overridden in classes that extend 
	 * this one.
	 * 
	 * @return the separator
	 */
	public String separatorValue() {
		return TaggedTableCoreRow.SEPARATOR;
	}

	/**
	 * Get the separator, used in the toString method to separate different cells,
	 * via a method so that it can be overridden in classes that extend this one.
	 * 
	 * @return the separator
	 */
	public String separatorCell() {
		return IndexedSet.SEPARATOR;
	}
	
	/**
	 * Get the separator, used in the toString method to separate different rows,
	 * via a method so that it can be overridden in classes that extend this one.
	 * 
	 * @return the separator
	 */
	public String separatorRow() {
		return SEPARATOR;
	}
	
	/**
	 * Convert a table to string
	 * 
	 * @param <TYPE_KEY>
	 * @param <TYPE_TAG>
	 * @param <TYPE_HEADER_DATA>
	 * @param <TYPE_ROW_DATA>
	 * @param <TYPE_HEADER>
	 * @param <TYPE_ROW>
	 * @param <TYPE_ROWS>
	 * @param <TYPE_TABLE>
	 * @param s
	 * @param table
	 * @param sepCell
	 * @param sepValue
	 * @param sepRow
	 * @param hideDefaultValuesInRows
	 * @return
	 */
	public static 
	<
	TYPE_KEY extends Object,TYPE_TAG extends Object,
	TYPE_HEADER_DATA extends TaggedTableCoreHeaderData<TYPE_ROW_DATA>,TYPE_ROW_DATA extends Object,
	TYPE_HEADER extends TaggedTableCoreHeader<TYPE_TAG,TYPE_ROW_DATA,TYPE_HEADER_DATA>,
	TYPE_ROW extends TaggedTableCoreRow<TYPE_TAG,TYPE_ROW_DATA>,
	TYPE_ROWS extends TaggedTableCoreRows<TYPE_KEY, TYPE_TAG,TYPE_ROW_DATA,TYPE_ROW>,
	TYPE_TABLE extends TaggedTableCore<TYPE_KEY,TYPE_TAG,TYPE_HEADER_DATA,TYPE_ROW_DATA,TYPE_HEADER,TYPE_ROW,TYPE_ROWS>
	>
	StringBuilder toString(StringBuilder s,TYPE_TABLE table, String sepCell, String sepValue, String sepRow,boolean hideDefaultValuesInRows){
		return table.toString(s, sepCell, sepValue, sepRow,hideDefaultValuesInRows);
	}
		
	public StringBuilder toString(StringBuilder s, String sepCell, String sepValue, String sepRow,boolean hideDefaultValuesInRows){
		cols.toString(s,cols,sepCell,sepValue);
		s.append(sepRow);
		rows.toString(s, (hideDefaultValuesInRows)?null:cols, sepCell, sepValue,sepRow);
		return s;
	}
		
	public String toString(boolean hideDefaultValuesInRows){
		StringBuilder s=new StringBuilder();
		return toString(s,this,separatorCell(),separatorValue(),separatorRow(),hideDefaultValuesInRows).toString();
	}

	public String toString(){
		return toString(false);
	}

	/**
	 * Get the unique values of a given column.
	 * 
	 * @param colName is the column name
	 * @return a {@link HashMultiMap} of all unique values for the given column 
	 * as keys, and a list of row keys that have that value as values of the map
	 */
	public HashMultiMap<TYPE_ROW_DATA, TYPE_KEY> uniqueValues(TYPE_TAG colName){
		HashMultiMap<TYPE_ROW_DATA, TYPE_KEY> u=new HashMultiMap<>();//unique values and their count
		if(cols.contains(colName)){
			Iterator<Entry<TYPE_KEY,TYPE_ROW>> it=rows.iterator();
			//go through all rows and get the unique values
			while(it.hasNext()){
				Entry<TYPE_KEY, TYPE_ROW> r=it.next();
				TYPE_ROW rr = r.getValue();
				TYPE_ROW_DATA s;
				if(rr.contains(colName)) {
					s = rr.value(colName);
				}
				else{
					//column does not exist in the current row so use the default value
					s=cols.value(colName).value();
				}
				u.put(s, getKey(rr));
			}
		}
		return u;
	}

	/**
	 * Create a new Tagged Table that contains the rows for which the given 
	 * column has identical values.
	 * The original table is not modified.
	 * 
	 * @param colName is the name of the column to compare
	 * @return a Tagged Table with the rows with duplicate values
	 */
	public HashMultiMap<TYPE_ROW_DATA, TYPE_KEY> duplicates(TYPE_TAG colName){
		//get list of unique column values
		HashMultiMap<TYPE_ROW_DATA,TYPE_KEY> uv = uniqueValues(colName);
		//remove column default value
		uv.remove(cols.value(colName).value());
		//if count > 1 then there are duplicates so add to the table
		Iterator<Entry<TYPE_ROW_DATA, HashSet<TYPE_KEY>>> it = uv.entrySet().iterator();
		while(it.hasNext()){
			Entry<TYPE_ROW_DATA, HashSet<TYPE_KEY>> e=it.next();
			if(e.getValue().size()<=1){
				it.remove();
			}
		}
		return uv;
	}
	
}
