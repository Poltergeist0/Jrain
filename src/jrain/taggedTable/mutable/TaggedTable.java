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
package jrain.taggedTable.mutable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.function.Predicate;

import jrain.taggedTable.mutable.TaggedTableRows.TaggedTableRowsIterator;
import jrain.taggedTableCore.mutable.TaggedTableCore;
import jrain.identifiable.immutable.Identifiable;
import jrain.entry.mutable.PairOfArrayList;
import jrain.polyType.mutable.BooleanPoly;
import jrain.polyType.mutable.PolyType;

/**
 * @author poltergeist0
 *
 * Table with multiple named columns.
 * Columns names are case sensitive and can not be repeated.
 * Requires that the first inserted column has unique values per row since it 
 * will be used as index to identify rows. Because of this, it can not be 
 * removed or deleted and its default value is ignored.
 * There is a virtual "Deleted" column that indicates if a row was deleted.
 * This column can not be removed because it is unnamed and is not really a column.
 * There is also an index for that column that can not be removed.
 * Row data can be repeated as long as the RowUUID is different.
 * Nulls are acceptable as values/default values or column name.
 */
public class TaggedTable 
extends TaggedTableCore<Identifiable, String, TaggedTableColumnData<PolyType<?> >,PolyType<?>,TaggedTableHeader,TaggedTableRow, TaggedTableRows>{
	
	@Override
	protected Identifiable getKey(TaggedTableRow r) {
		return r.identifiable();
	}
	
	/**
	 * Add to deleted row index. 
	 * Needs special handling since rows do not really have this column but the header does.
	 * 
	 * @param <U> is the data type of the row
	 * @param r is the row
	 */
	private <U extends TaggedTableRow > void internalAddDeleteInIndex(U r) {
		getColumns().addUpdateDeleted(r.identifiable(),r.isDeleted());
	}
	
	/**
	 * Add or update index of given column except for the deleted column
	 * 
	 * @param <U> is the data type of the row
	 * @param col is the name of the column
	 * @param c is the column data that will be indexed
	 * @param r is the row
	 */
	private <U extends TaggedTableRow > void internalAddToIndex(String col, TaggedTableColumnData<PolyType<?>> c, U r) {
		if(c.hasIndex()) {
			c.removeFromIndex(r.identifiable());
			if(r.contains(col))	c.addToIndex(r.identifiable(), r.value(col));//add row's column value
			else c.addToIndex(r.identifiable(), c.value());//add default value
		}
	}
	
	/**
	 * Add or update all indexes with the data of the columns of the given row.
	 * 
	 * @param <U> is the data type of the row
	 * @param r is the row
	 */
	private <U extends TaggedTableRow > void internalAddToIndexes(U r) {
		//add to deleted row index. Needs special handling since rows do not really have this column but the header does
		internalAddDeleteInIndex(r);
		//go through all columns and add to each index if it exists
		Iterator<Entry<String, TaggedTableColumnData<PolyType<?>>>> it = getColumns().iterator();
		while(it.hasNext()) {
			Entry<String, TaggedTableColumnData<PolyType<?>>> a = it.next();
//			if(a.getKey()!=TaggedTableSimpleColumnData.DELETED) {//the current column is not the deleted row column
				internalAddToIndex(a.getKey(), a.getValue(), r);
//			}
		}
	}
	
	/**
	 * Remove the given row from all indexes.
	 * 
	 * @param <U> is the data type of the row
	 * @param r is the row
	 */
	private <U extends TaggedTableRow > void internalRemoveFromIndexes(Identifiable r) {
		//go through all columns and remove from each index if it exists
		Iterator<Entry<String, TaggedTableColumnData<PolyType<?>>>> it = getColumns().iterator();
		while(it.hasNext()) {
			Entry<String, TaggedTableColumnData<PolyType<?>>> a = it.next();
			TaggedTableColumnData<PolyType<?>> c=a.getValue();
			if(c.hasIndex()) {
				c.removeFromIndex(r);
			}
		}
		getColumns().removeDeleted(r);
	}

	/**
	 * Create a new simple tagged table with no columns or rows.
	 */
	public TaggedTable(){
		super(new TaggedTableHeader());
	}
	
	/**
	 * Create a new simple tagged table with no rows.
	 *
	 * @param <TYPE_HEADER> is the data type of the header
	 * @param h is the header for the table
	 */
	public <TYPE_HEADER extends TaggedTableHeader >
	TaggedTable(TYPE_HEADER h) {
		super(h);
	}
	
	/**
	 * Copy constructor uses a given {@link TaggedTable} as source for 
	 * columns but does not add any rows if copyData is false
	 * 
	 * @param <TYPE_TABLE> is the data type of the table
	 * @param t is the original table
	 * @param copyRows if true copies all rows to the new table. Otherwise, no rows are copied
	 * @param deepCopy if true tries to perform a deep copy of all the data
	 * @param includeDeleted if true also copies deleted rows
	 */
	public <TYPE_TABLE extends TaggedTable >
	TaggedTable(TYPE_TABLE t,boolean copyRows,boolean deepCopy, boolean includeDeleted){
		super(new TaggedTableHeader(t.getColumns(),false));
		if(copyRows) {
			if(includeDeleted) {
				super.addRows(((deepCopy)?t.deepCopy():t), false);
			}
			else {
				super.addRows(((deepCopy)?t.deepCopy():t), new FilterOutDeletedRows(), false);
			}
			TaggedTableRowsIterator it = getRows().iterator();
			while(it.hasNext()) {
				Entry<Identifiable, TaggedTableRow> a = it.next();
				internalAddToIndexes(a.getValue());
			}
		}
	}

	/**
	 * Filtered copy constructor.
	 * Uses a given {@link TaggedTable} as source for data, a
	 * {@link TaggedTableHeader} as source for columns, and only adds rows 
	 * that pass the predicate test
	 * 
	 * @param <TYPE_HEADER> is the data type of the header
	 * @param <TYPE_TABLE> is the data type of the tagged table
	 * @param <P> is the data type of the predicate
	 * @param t is the original table
	 * @param h is the header with the new columns
	 * @param pre is the predicate that selects which rows are copied
	 * @param deepCopy if true tries to perform a deep copy of all the data
	 */
	public <
	TYPE_HEADER extends TaggedTableHeader,
	TYPE_TABLE extends TaggedTable, 
	P extends Predicate<Entry<Identifiable,TaggedTableRow>> 
	> 
	TaggedTable(
			TYPE_TABLE t,
			TYPE_HEADER h,
			P pre,boolean deepCopy
			)
	{
		super(t, h, pre,deepCopy);
		TaggedTableRowsIterator it = getRows().iterator();
		while(it.hasNext()) {
			Entry<Identifiable, TaggedTableRow> a = it.next();
			internalAddToIndexes(a.getValue());
		}
	}
	
	/**
	 * Get all rows as a list of rows
	 * 
	 * @param includeDeleted if true also returns deleted rows
	 * @return the rows
	 */
	public TaggedTableRows getRows(boolean includeDeleted){
		if(includeDeleted) return super.getRows().deepCopy();
		return super.getRows(getColumns().get().get(new BooleanPoly(false), true));
	}
		
	/**
	 * Get all rows that contain (or not) a given value on a given column.
	 * The given column can not be the row UUID column nor the column indicating
	 * if the row is deleted ("Deleted" column) since those are special (they are
	 * not real columns)
	 * 
	 * @param column is the column to search for the value
	 * @param value is the value
	 * @param contains if true returns rows that contain the requested value, otherwise returns rows that do not contain the value
	 * @param includeDeleted if true also returns deleted rows
	 * @return the rows that comply with the search
	 */
	public TaggedTable getRows(String column, PolyType<?> value,boolean contains, boolean includeDeleted){
		TaggedTable t=new TaggedTable();
		if(super.getHeader().contains(column)) {//contains column
			if(super.getHeader().hasIndex(column)) {//has index
				ArrayList<Identifiable> a = super.getHeader().value(column).index().get(value, contains);
				t.addRows(super.getRows(a),false);
				if(!includeDeleted) t.removeRows(getHeader().get().get(new PolyType<Boolean>(true), true));
			}
			else {//no index. Use predicate for linear search
				t.addRows(super.getRows(new FilterRowsByValue(column, value, super.getHeader().value(column).value(), contains)), false);
				if(!includeDeleted) t.removeRows(new FilterOutDeletedRows());
			}
		}
		return t;
	}
		
	/**
	 * Get all rows UUIDs as a list
	 * 
	 * @param includeDeleted if true also returns deleted rows
	 * @return all rows
	 */
	public ArrayList<Identifiable> getRowsUUID(boolean includeDeleted){
		TaggedTableRows r=getRows(includeDeleted);
		ArrayList<Identifiable> a=new ArrayList<>();
		Iterator<Entry<Identifiable, TaggedTableRow>> it = r.iterator();
		while(it.hasNext()){
			Identifiable id=it.next().getKey();
			a.add(id);
		}
		return a;
	}
		
	/**
	 * Add or replace a row.
	 * Non existing columns will be ignored and not added to the table. This 
	 * works as a column filter.
	 * The row actually added may differ from the one passed to the method
	 * since columns that do not exist in the table may not be added.
	 * 
	 * @param row is the row to be added
	 * @param overwrite if true and the rows exists, overwrites it. Otherwise keeps the original.
	 * @return a list with the rows that failed to be added/replaced
	 */
	public ArrayList<Entry<Identifiable,TaggedTableRow>> addRow(TaggedTableRow row,boolean overwrite) {
		ArrayList<Entry<Identifiable,TaggedTableRow>> b = super.addRow(row, overwrite);//add or replace row
		if(b.isEmpty()) {
			internalAddToIndexes(row);
		}
		return b;
	}
	
	/**
	 * Add multiple rows including deleted.
	 * Non existing columns will be ignored.
	 * 
	 * @param <TYPE_TABLE> is the data type of the tagged table
	 * @param table is a table with the rows to add
	 * @param overwrite if true and the rows exists, overwrites it. Otherwise keeps the original.
	 * @param includeDeleted if true also adds deleted rows
	 * @return a list with the rows that failed to be added/replaced
	 */
	public <TYPE_TABLE extends TaggedTable >
	ArrayList<Entry<Identifiable,TaggedTableRow>> addRows(TYPE_TABLE table,boolean overwrite, boolean includeDeleted) {
//		boolean ok=true;
		ArrayList<Entry<Identifiable,TaggedTableRow>> ok=new ArrayList<>();
		Iterator<Entry<Identifiable, TaggedTableRow>> itr = table.getRows().iterator();
		while(itr.hasNext()){
			Entry<Identifiable, TaggedTableRow> a = itr.next();
			TaggedTableRow r = a.getValue().deepCopy();
			if(includeDeleted) {
//				boolean b=addRow(r,overwrite);
//				if(!b)ok=false;
				ok.addAll(addRow(r,overwrite));
			}
			else {
				if(!r.isDeleted()) {
//					boolean b=addRow(r,overwrite);
//					if(!b)ok=false;
					ok.addAll(addRow(r,overwrite));
				}
			}
		}
		return ok;
	}
	
	/**
	 * Add multiple rows including deleted.
	 * Non existing columns will be ignored.
	 * 
	 * @param <TYPE_TABLE> is the data type of the tagged table
	 * @param table is a table with the rows to add
	 * @param overwrite if true and the rows exists, overwrites it. Otherwise keeps the original.
	 * @param includeDeleted if true also adds deleted rows
	 * @return a list with the rows that failed to be added/replaced
	 */
	public <TYPE_TABLE extends TaggedTableRows>
	ArrayList<Entry<Identifiable,TaggedTableRow>> addRows(TYPE_TABLE table,boolean overwrite, boolean includeDeleted) {
//		boolean ok=true;
		ArrayList<Entry<Identifiable,TaggedTableRow>> ok=new ArrayList<>();
		Iterator<Entry<Identifiable, TaggedTableRow>> itr = table.iterator();
		while(itr.hasNext()){
			Entry<Identifiable, TaggedTableRow> a = itr.next();
			TaggedTableRow r = a.getValue().deepCopy();
			if(includeDeleted) {
//				boolean b=addRow(r,overwrite);
//				if(!b)ok=false;
				ok.addAll(addRow(r,overwrite));
			}
			else {
				if(!r.isDeleted()) {
//					boolean b=addRow(r,overwrite);
//					if(!b)ok=false;
					ok.addAll(addRow(r,overwrite));
				}
			}
		}
		return ok;
	}
	
	/**
	 * Delete a given row
	 * 
	 * @param row is the row to remove
	 * @return true if the row was removed
	 */
	public PairOfArrayList<Identifiable,Identifiable,ArrayList<Identifiable>,ArrayList<Identifiable>> deleteRow(Identifiable row){
//		return super.getRows().delete(row);
		PairOfArrayList<Identifiable,Identifiable,ArrayList<Identifiable>,ArrayList<Identifiable>> b = getRows().delete(row);//add or replace row
		if(!b.getValue().isEmpty()) {
			internalAddDeleteInIndex(getRow(row).get());
		}
		return b;
	}
	
	/**
	 * Delete a set of rows.
	 * 
	 * @param r is the list of rows to delete
	 * @return false if any row failed to delete. Otherwise true
	 */
	public PairOfArrayList<Identifiable,Identifiable,ArrayList<Identifiable>,ArrayList<Identifiable>> deleteRows(Collection<Identifiable> r){
//		return super.getRows().deleteRows(r);
		PairOfArrayList<Identifiable,Identifiable,ArrayList<Identifiable>,ArrayList<Identifiable>> b=new PairOfArrayList<>();
		Iterator<Identifiable> it = r.iterator();
		while(it.hasNext()){
			Identifiable k = it.next();
			PairOfArrayList<Identifiable,Identifiable,ArrayList<Identifiable>,ArrayList<Identifiable>> bb=deleteRow(k);
			b.add(bb);
		}
		return b;
	}
	
	/**
	 * Delete rows.
	 * 
	 * @param r is the list of rows to delete
	 * @return false if any row failed to delete. Otherwise true
	 */
	public <TYPE_TABLE extends TaggedTable>
	PairOfArrayList<Identifiable,Identifiable,ArrayList<Identifiable>,ArrayList<Identifiable>> deleteRows(TYPE_TABLE table){
		PairOfArrayList<Identifiable,Identifiable,ArrayList<Identifiable>,ArrayList<Identifiable>> b=new PairOfArrayList<>();
		Iterator<Entry<Identifiable,TaggedTableRow>> it = table.getRows().iterator();
		while(it.hasNext()){
			Entry<Identifiable, TaggedTableRow> k = it.next();
			PairOfArrayList<Identifiable,Identifiable,ArrayList<Identifiable>,ArrayList<Identifiable>> bb=deleteRow(k.getKey());
			b.add(bb);
		}
		return b;
	}
	
	/**
	 * Delete all rows that pass the predicate test.
	 * 
	 * @param pre
	 * @return an hashset with the deleted rows
	 */
	public <P extends Predicate<Entry<Identifiable,TaggedTableRow>> >
	PairOfArrayList<Identifiable,Identifiable,ArrayList<Identifiable>,ArrayList<Identifiable>> deleteRows(P pre){
		PairOfArrayList<Identifiable,Identifiable,ArrayList<Identifiable>,ArrayList<Identifiable>> b=new PairOfArrayList<>();
		Iterator<Entry<Identifiable,TaggedTableRow>> it = getRows().iterator();
		while(it.hasNext()){
			Entry<Identifiable, TaggedTableRow> k = it.next();
			if(pre.test(k)) {//passed test
				PairOfArrayList<Identifiable,Identifiable,ArrayList<Identifiable>,ArrayList<Identifiable>> bb=deleteRow(k.getKey());
				b.add(bb);
			}
		}
		return b;
	}
	
	public PairOfArrayList<Identifiable,Entry<Identifiable,TaggedTableRow>,ArrayList<Identifiable>,ArrayList<Entry<Identifiable,TaggedTableRow>>> removeRow(Identifiable uuid){
//		return rows.remove(uuid);
		PairOfArrayList<Identifiable,Entry<Identifiable,TaggedTableRow>,ArrayList<Identifiable>,ArrayList<Entry<Identifiable,TaggedTableRow>>> b = super.removeRow(uuid);				
		if(!b.getValue().isEmpty()){
			internalRemoveFromIndexes(uuid);
		}
		return b;
	}
	
	public PairOfArrayList<Identifiable,Entry<Identifiable,TaggedTableRow>,ArrayList<Identifiable>,ArrayList<Entry<Identifiable,TaggedTableRow>>> removeRows(Collection<Identifiable> uuid){
		PairOfArrayList<Identifiable,Entry<Identifiable,TaggedTableRow>,ArrayList<Identifiable>,ArrayList<Entry<Identifiable,TaggedTableRow>>> b=new PairOfArrayList<>();
		Iterator<Identifiable> it = uuid.iterator();
		while(it.hasNext()){
			Identifiable id=it.next();
			PairOfArrayList<Identifiable,Entry<Identifiable,TaggedTableRow>,ArrayList<Identifiable>,ArrayList<Entry<Identifiable,TaggedTableRow>>> bb = removeRow(id);				
			b.add(bb);
		}
		return b;
	}
	
	/**
	 * Remove rows.
	 * 
	 * @param r is the list of rows to delete
	 * @return false if any row failed to delete. Otherwise true
	 */
	public <TYPE_TABLE extends TaggedTable>
	PairOfArrayList<Identifiable,Entry<Identifiable,TaggedTableRow>,ArrayList<Identifiable>,ArrayList<Entry<Identifiable,TaggedTableRow>>> removeRows(TYPE_TABLE table){
		PairOfArrayList<Identifiable,Entry<Identifiable,TaggedTableRow>,ArrayList<Identifiable>,ArrayList<Entry<Identifiable,TaggedTableRow>>> b=new PairOfArrayList<>();
		Iterator<Entry<Identifiable,TaggedTableRow>> it = table.getRows().iterator();
		while(it.hasNext()){
			Entry<Identifiable, TaggedTableRow> k = it.next();
			PairOfArrayList<Identifiable,Entry<Identifiable,TaggedTableRow>,ArrayList<Identifiable>,ArrayList<Entry<Identifiable,TaggedTableRow>>> bb=removeRow(k.getKey());
			b.add(bb);
		}
		return b;
	}
	
	/**
	 * Remove all rows that pass the predicate test.
	 * 
	 * @param pre
	 * @return an hashset with the deleted rows
	 */
	public <P extends Predicate<Entry<Identifiable,TaggedTableRow>> >
	PairOfArrayList<Identifiable,Entry<Identifiable,TaggedTableRow>,ArrayList<Identifiable>,ArrayList<Entry<Identifiable,TaggedTableRow>>> removeRows(P pre){
		PairOfArrayList<Identifiable,Entry<Identifiable,TaggedTableRow>,ArrayList<Identifiable>,ArrayList<Entry<Identifiable,TaggedTableRow>>> b=new PairOfArrayList<>();
		Iterator<Entry<Identifiable,TaggedTableRow>> it = getRows().iterator();
		while(it.hasNext()){
			Entry<Identifiable, TaggedTableRow> k = it.next();
			if(pre.test(k)) {//passed test
				PairOfArrayList<Identifiable,Entry<Identifiable,TaggedTableRow>,ArrayList<Identifiable>,ArrayList<Entry<Identifiable,TaggedTableRow>>> bb=removeRow(k.getKey());
				b.add(bb);
			}
		}
		return b;
	}
	
	public int getRowCount(boolean includeDeleted){
		if(includeDeleted) return super.nrows();
		return getRows(includeDeleted).size();
	}
	
	public boolean hasIndex(String colName) {
		if(!super.containsColumn(colName)) return false;
		return super.getColumns().value(colName).hasIndex();
	}

	public boolean removeIndex(String colName) {
		if(!super.containsColumn(colName)) return false;
		return super.getColumns().value(colName).removeIndex();
	}

	public boolean createIndex(String colName) {
		if(!super.containsColumn(colName)) return false;
//		if(super.getColumn(colName).value().hasIndex()) return false;
		TaggedTableColumnData<PolyType<?>> c = super.getColumns().value(colName);
		boolean b = c.createIndex();
		if(b) {
			Iterator<Entry<Identifiable, TaggedTableRow>> it = getRows().iterator();
			while(it.hasNext()) {
				TaggedTableRow r = it.next().getValue();
//				if(colName!=TaggedTableSimpleColumnData.DELETED) 
					internalAddToIndex(colName, c, r);
//				else internalAddDeleteInIndex(r);
			}
		}
		return b;
	}
		
	/**
	 * Delete a column
	 * 
	 * @param columnName is the name of the column to remove
	 * @return a pair with a list of keys for the columns that failed to be 
	 * marked as the key, and a list with the marked columns as the value
	 */
	public PairOfArrayList<String,String,ArrayList<String>,ArrayList<String>> deleteColumn(String columnName){
		return super.getColumns().delete(columnName);
	}
			
	/**
	 * Delete a column
	 * 
	 * @param n is the position of the column to remove
	 * @return a pair with a list of keys for the columns that failed to be 
	 * marked as the key, and a list with the marked columns as the value
	 */
	public PairOfArrayList<Integer,String,ArrayList<Integer>,ArrayList<String>> deleteColumn(int n){
		return super.getColumns().delete(n);
	}
			
	/**
	 * @return a new tagged table without the deleted columns/rows
	 */
	public TaggedTable compact() {
		TaggedTable t=new TaggedTable(super.getColumns().compact().deepCopy());
		Iterator<Entry<Identifiable, TaggedTableRow>> it = getRows(new FilterOutDeletedRows()).iterator();
		while(it.hasNext()) {
			Entry<Identifiable, TaggedTableRow> a = it.next();
			if(!a.getValue().isDeleted()) t.addRow(a.getValue().compact(t.getColumns()).deepCopy(), false);
		}
		return t;
	}
	
	public static class FilterRowsByValue implements Predicate<Entry<Identifiable,TaggedTableRow>>{
		private String col;
		private PolyType<?> val;
		private PolyType<?> defval;
		private boolean con;
		public FilterRowsByValue(String column, PolyType<?> value, PolyType<?> columnDefaultValue, boolean contains) {
			col=column;
			val=value;
			defval=columnDefaultValue;//this is required for when the row does not have the column, which implies that it exists and equals the default value for the column
			con=contains;
		}
		  
		public boolean test(Entry<Identifiable,TaggedTableRow> u){
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

	public static class FilterOutDeletedRows implements Predicate<Entry<Identifiable,TaggedTableRow>>{
		  
		public boolean test(Entry<Identifiable,TaggedTableRow> u){
			if(u.getValue().isDeleted()) return false;
			return true;
		}
	}

	@Override
	public TaggedTable deepCopy() {
		return new TaggedTable(this,true,true,true);
	}

	@Override
	protected TaggedTableRows newInstanceTypeRows() {
		return new TaggedTableRows();
	}
	
}
