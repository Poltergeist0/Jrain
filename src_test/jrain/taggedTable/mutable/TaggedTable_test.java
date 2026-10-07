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

import jrain.polyType.mutable.IntegerPoly;
import jrain.polyType.mutable.PolyType;
import jrain.polyType.mutable.StringPoly;

public class TaggedTable_test {

	public static void main(String[] args) {
//		TaggedTable t=new TaggedTable();
//		TaggedTablePrinter.read(t,args[0], "\r\n", "\t", false, true,true);
//		
//		TaggedTablePrinter.print(t,-1, -1, "\n", "\t", true, true,null,false);
//		
////		System.out.println("construct no copy");
////		TaggedTable tt=new TaggedTable(t,false);
////		TaggedTablePrinter.print(tt,-1, -1, "\n", "\t", true, true,null,false);
////		
////		System.out.println("construct copy");
////		tt=new TaggedTable(t,true);
////		TaggedTablePrinter.print(tt,-1, -1, "\n", "\t", true, true,null,false);
////		
////		System.out.println("unique");
////		tt=t.uniqueValues("sha512");
////		TaggedTablePrinter.print(tt,-1, -1, "\n", "\t", true, true,null,false);
////		
////		System.out.println("duplicates");
////		tt=t.duplicates("sha512");
////		TaggedTablePrinter.print(tt,-1, -1, "\n", "\t", true, true,null,false);
////		
////		System.out.println("filter and");
//		ArrayList<TaggedTable.FilterParameter> f=new ArrayList<>();
//		TaggedTable.FilterParameter fp=new TaggedTable.FilterParameter("sha512", "e6a3700478803d2eb638c6ea3582eddcfb0a55a1ea4812b3dc1eb0bf745907d44ede1e92d0354fe8ce40056efe058c327aa4dae1cda2e41751804ce8dfe61d24", true);
////		f.add(fp);
////		fp=new TaggedTable.FilterParameter("Deleted", "false", true);
////		f.add(fp);
////		tt=t.filter(f, true);
////		TaggedTablePrinter.print(tt,-1, -1, "\n", "\t", true, true,null,false);
////		UUID id=UUID.fromString("8119f56c-9638-47d5-9664-9c0c824dc721");
////		tt=new TaggedTable(t,true);
////		tt.deleteRow(id);
////		System.out.println("filter and");
////		
////		f=new ArrayList<>();
////		fp=new TaggedTable.FilterParameter("sha512", "e6a3700478803d2eb638c6ea3582eddcfb0a55a1ea4812b3dc1eb0bf745907d44ede1e92d0354fe8ce40056efe058c327aa4dae1cda2e41751804ce8dfe61d24", true);
////		f.add(fp);
////		fp=new TaggedTable.FilterParameter("Deleted", "false", true);
////		f.add(fp);
////		tt=tt.filter(f, true);
////		TaggedTablePrinter.print(tt,-1, -1, "\n", "\t", true, true,null,false);
//		
//		TaggedTable duplicates = t.duplicates("sha512");
//		//filter directories (they do not have hashes)
//		f=new ArrayList<>();
//		fp=new FilterParameter("sha512", "", true);
//		f.add(fp);
//		TaggedTable directories = t.filter(f,false);//get directories (which have empty hashes)
//		TaggedTablePrinter.print(directories,-1, -1, "\n", "\t", true, true,null,false);
//		ArrayList<UUID> dirs = new ArrayList<UUID>();
//		String com="/home/poltergeist0/Desktop/tmp/New3";
//		TaggedTable del =new TaggedTable(duplicates,false);
//		do{
//			f=new ArrayList<>();
//			fp=new FilterParameter(DirectoryDescriptor.PATHTAG, com, true);
//			f.add(fp);
//			fp=new FilterParameter(TaggedTable_I.ROWDELETEDTAG, "false", true);//only get those that were still not deleted
//			f.add(fp);
//			TaggedTable dup = duplicates.filter(f, true);
//			del.addRows(dup);//add files to list of files to delete
//			dirs.addAll(directories.filter(f, true).getRowsUUID(true));//get directories
//			if(dirs.size()>0){
//				UUID id=dirs.remove(0);
//				TaggedTableRow r = directories.getRow(id);
//				com=r.value(DirectoryDescriptor.PATHTAG)+File.separator+r.value(DirectoryDescriptor.NAMETAG);
//			}
//			else{
//				com=null;
//			}
//		}while(com!=null);
//		TaggedTablePrinter.print(del,-1, -1, "\n", "\t", true, true,null,false);
//		TaggedTable ddup = del.uniqueValues("sha512");
//		Iterator<TaggedTableRow> itd=ddup.getRows(true).iterator();
//		while(itd.hasNext()){//for all unique hashes in the list of files to delete
//			String hh = itd.next().value("sha512");
//			f=new ArrayList<>();
//			fp=new FilterParameter("sha512", hh, true);
//			f.add(fp);
//			TaggedTable dup = del.filter(f, true);
//			TaggedTable dupdup = duplicates.filter(f, true);
//			if(dup.getRowCount(true)>=dupdup.getRowCount(true)){//check if their number is smaller than in the list of all duplicates
//				//there are duplicates outside the current list of files to delete so they can all be deleted without checking
//				//otherwise the user must be asked
//				del.deleteRows(dup.getRowsUUID(true));
//			}
//		}
//		TaggedTablePrinter.print(del,-1, -1, "\n", "\t", true, true,null,false);
//		TaggedTable dup = del.duplicates("sha512");
//		//remove duplicates that are all in the same directory
//		ArrayList<TaggedTableRow> un= dup.uniqueValues("sha512").getRows(false);
//		Iterator<TaggedTableRow> ith=un.iterator();
//		while(ith.hasNext()){
//			String hh = ith.next().value("sha512");
//			f=new ArrayList<>();
//			fp=new FilterParameter("sha512", hh, true);
//			f.add(fp);
//			TaggedTable dupdup = dup.filter(f, false);
//			TaggedTable dddup = dupdup.uniqueValues(DirectoryDescriptor.PATHTAG);
//			int dd = dddup.getRowCount(true);
//			if(dd<=1){//there is only one directory for these duplicates
//				//remove them so they are not deleted
//				del.removeRows(dupdup.getRowsUUID(true), true);
//			}
//		}
//		TaggedTablePrinter.print(del,-1, -1, "\n", "\t", true, true,null,false);
//		duplicates.deleteRows(del.getRowsUUID(true));
//		TaggedTablePrinter.print(duplicates,-1, -1, "\n", "\t", true, true,null,false);
//
//		dirs = new ArrayList<UUID>();
//		com="/home/poltergeist0/Desktop/tmp/New2";
//		del =new TaggedTable(duplicates,false);
//		do{
//			f=new ArrayList<>();
//			fp=new FilterParameter(DirectoryDescriptor.PATHTAG, com, true);
//			f.add(fp);
//			fp=new FilterParameter(TaggedTable_I.ROWDELETEDTAG, "false", true);//only get those that were still not deleted
//			f.add(fp);
//			dup = duplicates.filter(f, true);
//			del.addRows(dup);//add files to list of files to delete
//			dirs.addAll(directories.filter(f, true).getRowsUUID(true));//get directories
//			if(dirs.size()>0){
//				UUID id=dirs.remove(0);
//				TaggedTableRow r = directories.getRow(id);
//				com=r.value(DirectoryDescriptor.PATHTAG)+File.separator+r.value(DirectoryDescriptor.NAMETAG);
//			}
//			else{
//				com=null;
//			}
//		}while(com!=null);
//		TaggedTablePrinter.print(del,-1, -1, "\n", "\t", true, true,null,false);
//		ddup = del.uniqueValues("sha512");
//		itd=ddup.getRows(true).iterator();
//		while(itd.hasNext()){//for all unique hashes in the list of files to delete
//			String hh = itd.next().value("sha512");
//			f=new ArrayList<>();
//			fp=new FilterParameter("sha512", hh, true);
//			f.add(fp);
//			dup = del.filter(f, true);
//			TaggedTable dupdup = duplicates.filter(f, true);
//			if(dup.getRowCount(true)>=dupdup.getRowCount(true)){//check if their number is smaller than in the list of all duplicates
//				//there are duplicates outside the current list of files to delete so they can all be deleted without checking
//				//otherwise the user must be asked
//				del.deleteRows(dup.getRowsUUID(true));
//			}
//		}
//		TaggedTablePrinter.print(del,-1, -1, "\n", "\t", true, true,null,false);
//		dup = del.duplicates("sha512");
//		//remove duplicates that are all in the same directory
//		un= dup.uniqueValues("sha512").getRows(false);
//		ith=un.iterator();
//		while(ith.hasNext()){
//			String hh = ith.next().value("sha512");
//			f=new ArrayList<>();
//			fp=new FilterParameter("sha512", hh, true);
//			f.add(fp);
//			TaggedTable dupdup = dup.filter(f, false);
//			TaggedTable dddup = dupdup.uniqueValues(DirectoryDescriptor.PATHTAG);
//			int dd = dddup.getRowCount(true);
//			if(dd<=1){//there is only one directory for these duplicates
//				//remove them so they are not deleted
//				del.removeRows(dupdup.getRowsUUID(true), true);
//			}
//		}
//		TaggedTablePrinter.print(del,-1, -1, "\n", "\t", true, true,null,false);
//		duplicates.deleteRows(del.getRowsUUID(true));
//		TaggedTablePrinter.print(duplicates,-1, -1, "\n", "\t", true, true,null,false);
//
		System.out.println("******************* test start *******************");
		TaggedTable t=new TaggedTable();
		final String colCntn="cnt";
		IntegerPoly colCntv=new IntegerPoly(0);
		final String colSomn="something";
		IntegerPoly colSomv=new IntegerPoly(null);
		final String colExn="extra";
		StringPoly colExv=new StringPoly("null");
		final String colNan="name";
		StringPoly colNav=new StringPoly("");
		t.addColumn(colCntn,new TaggedTableColumnData<PolyType<?>>(colCntv), false);
		t.addColumn(colNan,new TaggedTableColumnData<PolyType<?>>(colNav), false);
		t.addColumn(colSomn,new TaggedTableColumnData<PolyType<?>>(colSomv), false);
		t.addColumn(colExn,new TaggedTableColumnData<PolyType<?>>(colExv), false);
		colCntv=new IntegerPoly(1);
		colSomv=new IntegerPoly(42);
		colExv=new StringPoly("x");
		colNav=new StringPoly("me");
		TaggedTableRow r = new TaggedTableRow();
		r.add(colCntn, colCntv, false);
		r.add(colSomn, colSomv, false);
		r.add(colExn, colExv, false);
		r.add(colNan, colNav, false);
		t.addRow(r,false);
		r=new TaggedTableRow();
		colCntv=new IntegerPoly(2);
		colExv=new StringPoly("t");
		colNav=new StringPoly("it");
		r.add(colCntn, colCntv, false);
		r.add(t.getColumnName(1), colNav, false);//add column by getting column from table by number. "name" is the forth column but numbering starts at zero
		r.add(colExn, colExv, false);
		t.addRow(r,false);
		r=new TaggedTableRow();//try inserting columns in row in different order
		colCntv=new IntegerPoly(3);
		colSomv=new IntegerPoly(42);
		colExv=new StringPoly("r");
		colNav=new StringPoly("Im");
		r.add(colSomn, colSomv, false);
		r.add(colCntn, colCntv, false);
		r.add(colNan, colNav, false);
		r.add(colExn, colExv, false);
		t.addRow(r,false);
		System.out.println(t);
		System.out.println(t.uniqueValues(colNan));
		System.out.println(t.uniqueValues(colSomn));
		System.out.println(t.duplicates(colSomn));
		System.out.println("******************* test end *******************");
	}

}
