/*******************************************************************************
 * Copyright (C) 2026 poltergeist0
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Any libraries this program depends on have their own Licenses.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * LICENSE file for more details.
 ******************************************************************************/
package jrain.taggedTableSimple.mutable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.function.Predicate;

import jrain.taggedTableCore.mutable.TaggedTableCore;
import jrain.indexedSet.mutable.IndexedSet;
import jrain.entry.mutable.PairOfArrayList;

/**
 * @author poltergeist0
 *
 * Simple table with multiple named (tagged) columns, with rows identified by the
 * data of a column of the row.
 * Requires that the first inserted column has unique values per row since it 
 * will be used as index to identify rows. Because of this, it can not be 
 * removed or deleted and its default value is ignored.
 * Adding a column does not modify any rows.
 * Columns names can not be repeated.
 * Rows can not be repeated.
 * Row data can be repeated as long as the row is not completely identical to 
 * another existing row.
 * Rows can not be added if there are no columns.
 * Row order is not maintained.
 * Rows can be marked as deleted.
 * Nulls are acceptable as values/default values or column name.
 * Rows are immutable so, to change a value of a column in a row, the old row 
 * must be removed and the row with the modification must be added (as 
 * implemented by the friendly method removeRow).
 * To keep the table fast, removing a column only removes it from the header but
 * keeps it in rows otherwise, in the worst case scenario, the entire table 
 * would have to be recreated due since rows are indexed by hash and any 
 * modification to a row changes the hash.
 *
 * @param <TYPE_TAG> is the data type for the column tag
 * @param <TYPE_DATA> is the data type for the row data
 */
public class TaggedTableSimple<TYPE_TAG extends Object,TYPE_DATA extends Object> 
extends TaggedTableCore
<
TYPE_DATA, 
TYPE_TAG, 
TaggedTableSimpleColumnData<TYPE_DATA>,
TYPE_DATA,
TaggedTableSimpleHeader<TYPE_TAG, TYPE_DATA>,
TaggedTableSimpleRow<TYPE_TAG, TYPE_DATA>,
TaggedTableSimpleRows<TYPE_DATA, TYPE_TAG, TYPE_DATA, TaggedTableSimpleRow<TYPE_TAG, TYPE_DATA>>
>
{
	
	/**
	 * Check if a column has been marked as deleted.
	 * 
     * @param columnName is the name of the column
	 * @return a pair with a list of keys for the columns that failed to be checked 
	 * marked as the key, and a list with the marked columns as the value
     */
	public PairOfArrayList<TYPE_TAG, TYPE_TAG,ArrayList<TYPE_TAG>,ArrayList<TYPE_TAG>> isDeletedColumn(TYPE_TAG columnName){
		return super.getColumns().isDeleted(columnName);
	}
			
	/**
	 * Check if a column has been marked as deleted.
	 * 
     * @param n is the insertion position of the column
	 * @return a pair with a list of keys for the columns that failed to be checked 
	 * marked as the key, and a list with the marked columns as the value
     */
	public PairOfArrayList<Integer, TYPE_TAG,ArrayList<Integer>,ArrayList<TYPE_TAG>> isDeletedColumn(int n) {
		return super.getColumns().isDeleted(n);
	}
			
	/**
	 * Mark a column as deleted if it exists.
	 * 
     * @param columnName is the name of the column
	 * @return a pair with a list of keys for the columns that failed to be 
	 * marked as the key, and a list with the marked columns as the value
     */
	public PairOfArrayList<TYPE_TAG, TYPE_TAG,ArrayList<TYPE_TAG>,ArrayList<TYPE_TAG>> deleteColumn(TYPE_TAG columnName){
		return super.getColumns().delete(columnName);
	}
			
	/**
	 * Mark a column as deleted if it exists.
	 * 
     * @param n is the insertion position of the column
	 * @return a pair with a list of keys for the columns that failed to be 
	 * marked as the key, and a list with the marked columns as the value
     */
	public PairOfArrayList<TYPE_TAG, TYPE_TAG,ArrayList<TYPE_TAG>,ArrayList<TYPE_TAG>> deleteColumn(int n) {
		return super.getColumns().delete(super.getColumns().key(n));
	}
			
	/**
	 * Mark a column as not deleted if it exists.
	 * 
     * @param columnName is the name of the column
	 * @return a pair with a list of keys for the columns that failed to be 
	 * unmarked as the key, and a list with the unmarked columns as the value
     */
	public PairOfArrayList<TYPE_TAG, TYPE_TAG,ArrayList<TYPE_TAG>,ArrayList<TYPE_TAG>> undeleteColumn(TYPE_TAG columnName){
		return super.getColumns().undelete(columnName);
	}
			
	/**
	 * Mark a column as not deleted if it exists.
	 * 
     * @param n is the insertion position of the column
	 * @return a pair with a list of keys for the columns that failed to be 
	 * unmarked as the key, and a list with the unmarked columns as the value
     */
	public PairOfArrayList<TYPE_TAG, TYPE_TAG,ArrayList<TYPE_TAG>,ArrayList<TYPE_TAG>> undeleteColumn(int n) {
		return super.getColumns().undelete(super.getColumns().key(n));
	}
			
	/**
	 * Remove a column by name
	 * 
	 * @param columnName is the name of the column to remove
	 * @return a pair with a list of names of the columns that failed to be 
	 * removed as the key, and a list with the removed columns as the value
	 */
	public PairOfArrayList<TYPE_TAG, Entry<TYPE_TAG, TaggedTableSimpleColumnData<TYPE_DATA>>,ArrayList<TYPE_TAG>,ArrayList<Entry<TYPE_TAG, TaggedTableSimpleColumnData<TYPE_DATA>>>> removeColumn(TYPE_TAG columnName){
		return super.getColumns().remove(columnName);
	}
			
	/**
	 * Remove a column by insertion position
	 * 
	 * @param n is the position of the column to remove
	 * @return a pair with a list of positions of the columns that failed to be 
	 * removed as the key, and a list with the removed columns as the value
	 */
	public PairOfArrayList<Integer,Entry<TYPE_TAG,TaggedTableSimpleColumnData<TYPE_DATA>>,ArrayList<Integer>,ArrayList<Entry<TYPE_TAG, TaggedTableSimpleColumnData<TYPE_DATA>>>> removeColumn(int n) {
		return super.getColumns().remove(n);
	}
			
	/**
	 * Check if a row has been marked as deleted.
	 * 
     * @param row is the key of the row
	 * @return a pair with a list of keys for the rows that failed to be checked 
	 * marked as the key, and a list with the marked rows as the value
     */
	public PairOfArrayList<TYPE_DATA,TYPE_DATA,ArrayList<TYPE_DATA>,ArrayList<TYPE_DATA>> isDeletedRow(TYPE_DATA row){
		return super.getRows().isDeleted(row);
	}
		
	/**
	 * Check if rows have been marked as deleted.
	 * 
     * @param r is a collection of row keys
	 * @return a pair with a list of keys for the rows that have not been 
	 * marked as the key, and a list with the marked rows as the value
     */
	public PairOfArrayList<TYPE_DATA,TYPE_DATA,ArrayList<TYPE_DATA>,ArrayList<TYPE_DATA>>
	isDeletedRows(Collection<TYPE_DATA> r){
		PairOfArrayList<TYPE_DATA,TYPE_DATA,ArrayList<TYPE_DATA>,ArrayList<TYPE_DATA>> b=new PairOfArrayList<>();
		Iterator<TYPE_DATA> it = r.iterator();
		while(it.hasNext()){
			TYPE_DATA k = it.next();
			PairOfArrayList<TYPE_DATA,TYPE_DATA,ArrayList<TYPE_DATA>,ArrayList<TYPE_DATA>> bb=isDeletedRow(k);
			b.add(bb);
		}
		return b;
	}
	
	/**
	 * Check if rows have been marked as deleted.
	 * 
	 * @param <TYPE_TABLE> is the data type of the class that extends this class
	 * @param table is a table with the rows to be checked
	 * @return a pair with a list of keys for the rows that have not been 
	 * marked as the key, and a list with the marked rows as the value
	 */
	public <TYPE_TABLE extends TaggedTableCore<TYPE_DATA, TYPE_TAG, TaggedTableSimpleColumnData<TYPE_DATA>,TYPE_DATA,TaggedTableSimpleHeader<TYPE_TAG, TYPE_DATA>,TaggedTableSimpleRow<TYPE_TAG, TYPE_DATA>,TaggedTableSimpleRows<TYPE_DATA, TYPE_TAG, TYPE_DATA, TaggedTableSimpleRow<TYPE_TAG, TYPE_DATA>> > >
	PairOfArrayList<TYPE_DATA,TYPE_DATA,ArrayList<TYPE_DATA>,ArrayList<TYPE_DATA>>
	isDeletedRows(TYPE_TABLE table){
		PairOfArrayList<TYPE_DATA,TYPE_DATA,ArrayList<TYPE_DATA>,ArrayList<TYPE_DATA>> b=new PairOfArrayList<>();
		IndexedSet<TYPE_DATA, TaggedTableSimpleRow<TYPE_TAG, TYPE_DATA>>.IndexedSetIterator it = table.rowsIterator();
		while(it.hasNext()){
			Entry<TYPE_DATA, TaggedTableSimpleRow<TYPE_TAG, TYPE_DATA>> k = it.next();
			PairOfArrayList<TYPE_DATA,TYPE_DATA,ArrayList<TYPE_DATA>,ArrayList<TYPE_DATA>> bb=isDeletedRow(k.getKey());
			b.add(bb);
		}
		return b;
	}
	
	/**
	 * Check if rows that pass the {@link Predicate} test have been marked as deleted.
	 * 
	 * @param <P> is the data type of the class that extends {@link Predicate}
	 * @param pre is the {@link Predicate}
	 * @return a pair with a list of keys for the rows that have not been 
	 * marked as the key, and a list with the marked rows as the value
	 */
	public <P extends Predicate<TaggedTableSimpleRow<TYPE_TAG, TYPE_DATA>> >
	PairOfArrayList<TYPE_DATA,TYPE_DATA,ArrayList<TYPE_DATA>,ArrayList<TYPE_DATA>>
	isDeletedRows(P pre){
		PairOfArrayList<TYPE_DATA,TYPE_DATA,ArrayList<TYPE_DATA>,ArrayList<TYPE_DATA>> b=new PairOfArrayList<>();
		IndexedSet<TYPE_DATA, TaggedTableSimpleRow<TYPE_TAG, TYPE_DATA>>.IndexedSetIterator it = super.rowsIterator();
		while(it.hasNext()){
			Entry<TYPE_DATA, TaggedTableSimpleRow<TYPE_TAG, TYPE_DATA>> k = it.next();
			if(pre.test(k.getValue())) {//passed test
				PairOfArrayList<TYPE_DATA,TYPE_DATA,ArrayList<TYPE_DATA>,ArrayList<TYPE_DATA>> bb=isDeletedRow(k.getKey());
				b.add(bb);
			}
		}
		return b;
	}
	
	/**
	 * Mark a given row as deleted
	 * 
	 * @param row is the row to delete
	 * @return a pair with a list of keys for the rows that failed to be 
	 * marked as the key, and a list with the marked rows as the value
	 */
	public PairOfArrayList<TYPE_DATA,TYPE_DATA,ArrayList<TYPE_DATA>,ArrayList<TYPE_DATA>> deleteRow(TYPE_DATA row){
		return super.getRows().delete(row);
	}
	
	/**
	 * Mark rows as deleted.
	 * 
	 * @param r is a {@link Collection} of rows to delete
	 * @return a pair with a list of keys for the rows that failed to be 
	 * marked as the key, and a list with the marked rows as the value
	 */
	public PairOfArrayList<TYPE_DATA,TYPE_DATA,ArrayList<TYPE_DATA>,ArrayList<TYPE_DATA>>
	deleteRows(Collection<TYPE_DATA> r){
		PairOfArrayList<TYPE_DATA,TYPE_DATA,ArrayList<TYPE_DATA>,ArrayList<TYPE_DATA>> b=new PairOfArrayList<>();
		Iterator<TYPE_DATA> it = r.iterator();
		while(it.hasNext()){
			TYPE_DATA k = it.next();
			PairOfArrayList<TYPE_DATA,TYPE_DATA,ArrayList<TYPE_DATA>,ArrayList<TYPE_DATA>> bb=deleteRow(k);
			b.add(bb);
		}
		return b;
	}
	
	/**
	 * Mark rows as deleted.
	 * 
	 * @param <TYPE_TABLE> is the data type of the class that extends this class
	 * @param table is a table with the rows to be marked as deleted
	 * @return a pair with a list of keys for the rows that failed to be 
	 * marked as the key, and a list with the marked rows as the value
	 */
	public <TYPE_TABLE extends TaggedTableCore<TYPE_DATA, TYPE_TAG, TaggedTableSimpleColumnData<TYPE_DATA>,TYPE_DATA,TaggedTableSimpleHeader<TYPE_TAG, TYPE_DATA>,TaggedTableSimpleRow<TYPE_TAG, TYPE_DATA>,TaggedTableSimpleRows<TYPE_DATA, TYPE_TAG, TYPE_DATA, TaggedTableSimpleRow<TYPE_TAG, TYPE_DATA>> > >
	PairOfArrayList<TYPE_DATA,TYPE_DATA,ArrayList<TYPE_DATA>,ArrayList<TYPE_DATA>>
	deleteRows(TYPE_TABLE table){
		PairOfArrayList<TYPE_DATA,TYPE_DATA,ArrayList<TYPE_DATA>,ArrayList<TYPE_DATA>> b=new PairOfArrayList<>();
		IndexedSet<TYPE_DATA, TaggedTableSimpleRow<TYPE_TAG, TYPE_DATA>>.IndexedSetIterator it = table.rowsIterator();
		while(it.hasNext()){
			Entry<TYPE_DATA, TaggedTableSimpleRow<TYPE_TAG, TYPE_DATA>> k = it.next();
			PairOfArrayList<TYPE_DATA,TYPE_DATA,ArrayList<TYPE_DATA>,ArrayList<TYPE_DATA>> bb=deleteRow(k.getKey());
			b.add(bb);
		}
		return b;
	}
	
	/**
	 * Mark rows that pass the predicate test as deleted.
	 * 
	 * @param <P> is the data type of the class that extends {@link Predicate}
	 * @param pre is the {@link Predicate}
	 * @return a pair with a list of keys for the rows that failed to be 
	 * marked as the key, and a list with the marked rows as the value
	 */
	public <P extends Predicate<TaggedTableSimpleRow<TYPE_TAG, TYPE_DATA>> >
	PairOfArrayList<TYPE_DATA,TYPE_DATA,ArrayList<TYPE_DATA>,ArrayList<TYPE_DATA>>
	deleteRows(P pre){
		PairOfArrayList<TYPE_DATA,TYPE_DATA,ArrayList<TYPE_DATA>,ArrayList<TYPE_DATA>> b=new PairOfArrayList<>();
		IndexedSet<TYPE_DATA, TaggedTableSimpleRow<TYPE_TAG, TYPE_DATA>>.IndexedSetIterator it = super.rowsIterator();
		while(it.hasNext()){
			Entry<TYPE_DATA, TaggedTableSimpleRow<TYPE_TAG, TYPE_DATA>> k = it.next();
			if(pre.test(k.getValue())) {//passed test
				PairOfArrayList<TYPE_DATA,TYPE_DATA,ArrayList<TYPE_DATA>,ArrayList<TYPE_DATA>> bb=deleteRow(k.getKey());
				b.add(bb);
			}
		}
		return b;
	}
	
	/**
	 * Mark a row as not deleted if it exists.
	 * 
	 * @param row is the row to undelete
	 * @return a pair with a list of keys for the rows that failed to be 
	 * unmarked as the key, and a list with the unmarked rows as the value
	 */
	public PairOfArrayList<TYPE_DATA,TYPE_DATA,ArrayList<TYPE_DATA>,ArrayList<TYPE_DATA>> undeleteRow(TYPE_DATA row){
		return super.getRows().undelete(row);
	}
	
	/**
	 * Mark rows as not deleted.
	 * 
	 * @param r is a {@link Collection} of the rows to undelete
	 * @return a pair with a list of keys for the rows that failed to be 
	 * unmarked as the key, and a list with the unmarked rows as the value
	 */
	public PairOfArrayList<TYPE_DATA,TYPE_DATA,ArrayList<TYPE_DATA>,ArrayList<TYPE_DATA>>
	undeleteRows(Collection<TYPE_DATA> r){
		PairOfArrayList<TYPE_DATA,TYPE_DATA,ArrayList<TYPE_DATA>,ArrayList<TYPE_DATA>> b=new PairOfArrayList<>();
		Iterator<TYPE_DATA> it = r.iterator();
		while(it.hasNext()){
			TYPE_DATA k = it.next();
			PairOfArrayList<TYPE_DATA,TYPE_DATA,ArrayList<TYPE_DATA>,ArrayList<TYPE_DATA>> bb=undeleteRow(k);
			b.add(bb);
		}
		return b;
	}
	
	/**
	 * Mark rows as not deleted.
	 * 
	 * @param <TYPE_TABLE> is the data type of the class that extends this class
	 * @param table is a table of the rows to undelete
	 * @return a pair with a list of keys for the rows that failed to be 
	 * unmarked as the key, and a list with the unmarked rows as the value
	 */
	public <TYPE_TABLE extends TaggedTableCore<TYPE_DATA, TYPE_TAG, TaggedTableSimpleColumnData<TYPE_DATA>,TYPE_DATA,TaggedTableSimpleHeader<TYPE_TAG, TYPE_DATA>,TaggedTableSimpleRow<TYPE_TAG, TYPE_DATA>,TaggedTableSimpleRows<TYPE_DATA, TYPE_TAG, TYPE_DATA, TaggedTableSimpleRow<TYPE_TAG, TYPE_DATA>> > >
	PairOfArrayList<TYPE_DATA,TYPE_DATA,ArrayList<TYPE_DATA>,ArrayList<TYPE_DATA>>
	undeleteRows(TYPE_TABLE table){
		PairOfArrayList<TYPE_DATA,TYPE_DATA,ArrayList<TYPE_DATA>,ArrayList<TYPE_DATA>> b=new PairOfArrayList<>();
		IndexedSet<TYPE_DATA, TaggedTableSimpleRow<TYPE_TAG, TYPE_DATA>>.IndexedSetIterator it = table.rowsIterator();
		while(it.hasNext()){
			Entry<TYPE_DATA, TaggedTableSimpleRow<TYPE_TAG, TYPE_DATA>> k = it.next();
			PairOfArrayList<TYPE_DATA,TYPE_DATA,ArrayList<TYPE_DATA>,ArrayList<TYPE_DATA>> bb=undeleteRow(k.getKey());
			b.add(bb);
		}
		return b;
	}
	
	/**
	 * Mark all rows that pass the predicate test as not deleted.
	 * 
	 * @param <P> is the data type of the class that extends {@link Predicate}
	 * @param pre is the {@link Predicate}
	 * @return a pair with a list of keys for the rows that failed to be 
	 * unmarked as the key, and a list with the unmarked rows as the value
	 */
	public <P extends Predicate<TaggedTableSimpleRow<TYPE_TAG, TYPE_DATA>> >
	PairOfArrayList<TYPE_DATA,TYPE_DATA,ArrayList<TYPE_DATA>,ArrayList<TYPE_DATA>>
	undeleteRows(P pre){
		PairOfArrayList<TYPE_DATA,TYPE_DATA,ArrayList<TYPE_DATA>,ArrayList<TYPE_DATA>> b=new PairOfArrayList<>();
		IndexedSet<TYPE_DATA, TaggedTableSimpleRow<TYPE_TAG, TYPE_DATA>>.IndexedSetIterator it = super.rowsIterator();
		while(it.hasNext()){
			Entry<TYPE_DATA, TaggedTableSimpleRow<TYPE_TAG, TYPE_DATA>> k = it.next();
			if(pre.test(k.getValue())) {//passed test
				PairOfArrayList<TYPE_DATA,TYPE_DATA,ArrayList<TYPE_DATA>,ArrayList<TYPE_DATA>> bb=undeleteRow(k.getKey());
				b.add(bb);
			}
		}
		return b;
	}
	
	/**
	 * Create a new simple tagged table with no columns or rows.
	 */
	public TaggedTableSimple(){
		super(new TaggedTableSimpleHeader<>());
	}
	
	/**
	 * Create a new simple tagged table with no columns or rows.
	 * 
	 * @param <TYPE_HEADER> is the data type of the class that extends {@link TaggedTableSimpleHeader}
	 * @param h is the header for the table
	 */
	public <TYPE_HEADER extends TaggedTableSimpleHeader<TYPE_TAG,TYPE_DATA> >
	TaggedTableSimple(TYPE_HEADER h) {
		super(h);
	}
	
	/**
	 * Copy constructor uses a given table as source for columns but does not 
	 * add any rows if copyData is false.
	 * 
	 * @param <TYPE_TABLE> is the data type of the class that extends this class
	 * @param t is the original table
	 * @param copyRows if true copies all rows to the new table. Otherwise, no rows are copied
	 * @param deepCopy if true attempts to perform a deep copy of the rows and columns
	 * @param includeDeleted if true also copy rows marked as deleted
	 */
	public <TYPE_TABLE extends TaggedTableSimple<TYPE_TAG,TYPE_DATA> >
	TaggedTableSimple(TYPE_TABLE t,boolean copyRows,boolean deepCopy, boolean includeDeleted){
		super(new TaggedTableSimpleHeader<>(t.getColumns(),false));
		if(copyRows) {
			if(includeDeleted) super.addRows(((deepCopy)?t.deepCopy():t), false);
			else super.addRows(((deepCopy)?t.deepCopy():t), new FilterOutDeletedRows<TYPE_TAG, TYPE_DATA>(), false);
		}
	}

	/**
	 * Filtered copy constructor.
	 * Uses a given {@link TaggedTableSimple} as source for data, a
	 * {@link TaggedTableSimpleHeader} as source for columns, and only adds rows 
	 * that pass the predicate test
	 * 
	 * @param <TYPE_HEADER> is the data type of the class that extends {@link TaggedTableSimpleHeader}
	 * @param <TYPE_TABLE> is the data type of the class that extends this class
	 * @param <P> is the data type of the class that extends {@link Predicate}
	 * @param t is the original table
	 * @param h is the header with the new columns
	 * @param pre is the predicate that selects which rows are copied
	 * @param deepCopy if true attempts to perform a deep copy of the rows and columns
	 */
	public <
	TYPE_HEADER extends TaggedTableSimpleHeader<TYPE_TAG,TYPE_DATA>,
	TYPE_TABLE extends TaggedTableSimple<TYPE_TAG,TYPE_DATA>, 
	P extends Predicate<Entry<TYPE_DATA,TaggedTableSimpleRow<TYPE_TAG,TYPE_DATA>>> 
	> 
	TaggedTableSimple(
			TYPE_TABLE t,
			TYPE_HEADER h,
			P pre,
			boolean deepCopy
			)
	{
		super(t, h, pre,deepCopy);
	}

	/**
	 * get all rows as a list of rows
	 * 
	 * @param includeDeleted
	 * @return all rows
	 */
	public TaggedTableSimpleRows<TYPE_DATA,TYPE_TAG,TYPE_DATA,TaggedTableSimpleRow<TYPE_TAG,TYPE_DATA>> getRows(boolean includeDeleted){
		if(includeDeleted) return super.getRows().deepCopy();
		return super.getRows(new FilterOutDeletedRows<TYPE_TAG, TYPE_DATA>()).deepCopy();
	}
		
	/**
	 * get all rows that contain (or not) a given value on a given column.
	 * The given column can not be the row UUID column nor the column indicating
	 * if the row is deleted ("Deleted" column) since those are special (they are
	 * not real columns)
	 * 
	 * @param includeDeleted
	 * @return all rows that match the criterion
	 */
	public TaggedTableSimple<TYPE_TAG,TYPE_DATA> getRows(TYPE_TAG column, TYPE_DATA value,boolean contains, boolean includeDeleted){
		TaggedTableSimple<TYPE_TAG,TYPE_DATA> t=new TaggedTableSimple<TYPE_TAG,TYPE_DATA>();
		if(super.getHeader().contains(column)) {//contains column
			t.addRows(super.getRows(new FilterRowsByValue<TYPE_TAG, TYPE_DATA>(column, value, super.getHeader().value(column).value(), contains)), false);
			if(!includeDeleted) t.removeRows(new FilterOutDeletedRows<TYPE_TAG, TYPE_DATA>());
		}
		return t;
	}
		
	@Override
	protected TYPE_DATA getKey(TaggedTableSimpleRow<TYPE_TAG, TYPE_DATA> r) {
		return r.value(super.getColumnName(0));
	}
	
	/**
	 * Get a new table with deleted information removed.
	 * 
	 * @return a new tagged table without the deleted columns/rows
	 */
	public TaggedTableSimple<TYPE_TAG,TYPE_DATA> compact() {
		TaggedTableSimple<TYPE_TAG,TYPE_DATA> t=new TaggedTableSimple<TYPE_TAG,TYPE_DATA>(super.getColumns().compact());
		IndexedSet<TYPE_DATA, TaggedTableSimpleRow<TYPE_TAG, TYPE_DATA>>.IndexedSetIterator it = getRows(new FilterOutDeletedRows<>()).iterator();
		while(it.hasNext()) {
			Entry<TYPE_DATA,TaggedTableSimpleRow<TYPE_TAG,TYPE_DATA>> a = it.next();
			if(!a.getValue().isDeleted()) t.addRow(a.getValue().compact(t.getColumns()), false);
		}
		return t;
	}
	
	/**
	 * @author poltergeist0
	 * 
	 * Class implementing {@link Predicate} used to filter rows that have a 
	 * specified value in a specific column.
	 *
	 * @param <TYPE_TAG> is the data type for the column tag
	 * @param <TYPE_DATA> is the data type for the row data
	 */
	public static class FilterRowsByValue<TYPE_TAG,TYPE_DATA> implements Predicate<Entry<TYPE_DATA, TaggedTableSimpleRow<TYPE_TAG, TYPE_DATA>>>{
		private TYPE_TAG col;//column
		private TYPE_DATA val;//value
		private TYPE_DATA defval;//column default value
		private boolean con;//get rows that contain (true) or do not contain (false) the given value
		public FilterRowsByValue(TYPE_TAG column, TYPE_DATA value, TYPE_DATA columnDefaultValue, boolean contains) {
			col=column;
			val=value;
			defval=columnDefaultValue;//this is required for when the row does not have the column, which implies that it exists and equals the default value for the column
			con=contains;
		}
		  
		public boolean test(Entry<TYPE_DATA, TaggedTableSimpleRow<TYPE_TAG, TYPE_DATA>> u){
			if(con) {//test if contains value
				if(u.getValue().contains(col)) {//column exists in the row
					if(u.getValue().value(col).equals(val)) return true;//contains value
					else return false;
				}
				else {//column does not exist in the row
					if(val.equals(defval)) {//the column implicitly exists with a value that equals the default value for the column
						return true;
					}
					else return false;//does not contain column
				}
			}
			else {//test if does not contain value
				if(u.getValue().contains(col)) {//column exists in the row
					if(!u.getValue().value(col).equals(val)) return true;//does not contain value
					else return false;
				}
				else {//column does not exist in the row
					if(val.equals(defval)) {//the column implicitly exists with a value that equals the default value for the column
						return false;
					}
					else return true;//does not contain column
				}
			}
		}
	}

	/**
	 * @author poltergeist0
	 *
	 * Class implementing {@link Predicate} used to filter rows that have been 
	 * marked as deleted.
	 * 
	 * @param <TYPE_TAG> is the data type for the column tag
	 * @param <TYPE_DATA> is the data type for the row data
	 */
	public static class FilterOutDeletedRows<TYPE_TAG, TYPE_DATA> implements Predicate<Entry<TYPE_DATA, TaggedTableSimpleRow<TYPE_TAG, TYPE_DATA>>>{
		  
		public boolean test(Entry<TYPE_DATA, TaggedTableSimpleRow<TYPE_TAG, TYPE_DATA>> u){
			if(u.getValue().isDeleted()) return false;
			return true;
		}
	}

	@Override
	public TaggedTableSimple<TYPE_TAG,TYPE_DATA> deepCopy() {
		return new TaggedTableSimple<>(this,true,true,true);
	}

	@Override
	protected TaggedTableSimpleRows<TYPE_DATA, TYPE_TAG, TYPE_DATA, TaggedTableSimpleRow<TYPE_TAG, TYPE_DATA>> newInstanceTypeRows() {
		return new TaggedTableSimpleRows<TYPE_DATA, TYPE_TAG, TYPE_DATA, TaggedTableSimpleRow<TYPE_TAG, TYPE_DATA>>();
	}
	
}
