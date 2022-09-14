package jrain.taggedTableHashcode.mutable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.function.Predicate;

import jrain.polyType.mutable.IntegerPoly;
import jrain.polyType.mutable.PolyType;
import jrain.polyType.mutable.StringPoly;

public class TaggedTableHashcode_test {
	
	public static class FilterOutColumnCntGreater1Smaller3 implements Predicate<TaggedTableHashcodeRow<String, String>>{
		  
		public boolean test(TaggedTableHashcodeRow<String, String> u){
			Integer i=0;
			if(!u.contains("cnt")) return false;
			try {
				i = Integer.valueOf(u.value("cnt"));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			if(i<=1 || i>=3) return true;
			return false;
		}
	}
	
	public static class FilterOutColumnCntGreater1Smaller3_2 implements Predicate<Entry<Integer,TaggedTableHashcodeRow<PolyType<?>, PolyType<?>>>>{
		  
		public boolean test(Entry<Integer,TaggedTableHashcodeRow<PolyType<?>, PolyType<?>>> u){
			Integer i=0;
			StringPoly x = new StringPoly("cnt");
			if(!u.getValue().contains(x)) return false;
			try {
				i = (Integer) u.getValue().value(x).get();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			if(i<=1 || i>=3) return true;
			return false;
		}
	}
	
	public static void test1(String[] args) {
		System.out.println("******************* test1 start *******************");
		System.out.println("test: adding row before columns exist");
		TaggedTableHashcodeRow<String, String> r=new TaggedTableHashcodeRow<>();
		r.add("cnt", "1", false);
		r.add("something", "42", false);
		TaggedTableHashcode<String, String> t=new TaggedTableHashcode<>();
		ArrayList<Entry<Integer,TaggedTableHashcodeRow<String,String>>> b=t.addRow(r,false);//test adding row on table without columns
		if(b.size()>0) System.out.println("Row correctly not added since none of its columns exist in the table");
		else System.out.println("Row incorrectly added");
		System.out.println("******************* test1 end *******************");
	}

	public static void test2(String[] args) {
		System.out.println("******************* test2 start *******************");
		System.out.println("test: modifying row using source and extra columns in row but not in (String) table");
		TaggedTableHashcode<String, String> t=new TaggedTableHashcode<>();
		t.addColumn(new TaggedTableHashcodeColumn<String, String>("cnt", "0"), false);
		t.addColumn(new TaggedTableHashcodeColumn<String, String>("name", ""), false);
		TaggedTableHashcodeRow<String, String> r=new TaggedTableHashcodeRow<>();
		r.add("cnt", "1", false);
		r.add("extra", "x", false);
		r.add("name", "me", false);
		t.addRow(r,false);
		r.add("cnt","11",true);//test indirectly modify row previously added to table
		if(t.getRow(t.getRowKeys().get(0)).get().value("cnt")=="11") System.out.println("Row correctly modified");
		else System.out.println("Row incorrectly not modified");
		if(t.getRow(t.getRowKeys().get(0)).get().contains("extra")) System.out.println("Row correctly keeps extra columns");
		else System.out.println("Row incorrectly does not keep extra columns");
		System.out.println("******************* test2 end *******************");
	}

	public static void test3(String[] args) {
		System.out.println("******************* test3 start *******************");
		System.out.println("test: modifying row using source and extra columns in row but not in (Integer) table");
		TaggedTableHashcode<String, Integer> t=new TaggedTableHashcode<>();
		t.addColumn(new TaggedTableHashcodeColumn<String, Integer>("cnt", 0), false);
		t.addColumn(new TaggedTableHashcodeColumn<String, Integer>("name", 13), false);
		TaggedTableHashcodeRow<String, Integer> r=new TaggedTableHashcodeRow<>();
		r.add("cnt", 1, false);
		r.add("extra", 32, false);
		r.add("name", 700, false);
		t.addRow(r,false);
		r.add("cnt",11,true);//test indirectly modify row previously added to table
		if(t.getRow(t.getRowKeys().get(0)).get().value("cnt")==11) System.out.println("Row correctly modified");
		else System.out.println("Row incorrectly not modified");
		if(t.getRow(t.getRowKeys().get(0)).get().contains("extra")) System.out.println("Row correctly keeps extra columns");
		else System.out.println("Row incorrectly does not keep extra columns");
		System.out.println("******************* test3 end *******************");
	}

	public static void test4(String[] args) {
		System.out.println("******************* test4 start *******************");
		System.out.println("test: modifying row indirectly in (String) table");
		TaggedTableHashcode<String, String> t=new TaggedTableHashcode<>();
		t.addColumn(new TaggedTableHashcodeColumn<String, String>("cnt", "0"), false);
		t.addColumn(new TaggedTableHashcodeColumn<String, String>("name", ""), false);
		TaggedTableHashcodeRow<String, String> r=new TaggedTableHashcodeRow<>();
		r.add("cnt", "1", false);
		r.add("extra", "x", false);
		r.add("name", "me", false);
		t.addRow(r,false);
		r=t.getRow(t.getRowKeys().get(0)).get();
		r.add("cnt","11",true);//test indirectly modify row previously added to table
		if(t.getRow(t.getRowKeys().get(0)).get().value("cnt")!="11") System.out.println("Row correctly not modified");
		else System.out.println("Row incorrectly modified");
		System.out.println("******************* test4 end *******************");
	}

	public static void test5(String[] args) {
		System.out.println("******************* test5 start *******************");
		System.out.println("test: modifying row indirectly in (Integer) table");
		TaggedTableHashcode<String, Integer> t=new TaggedTableHashcode<>();
		t.addColumn(new TaggedTableHashcodeColumn<String, Integer>("cnt", 0), false);
		t.addColumn(new TaggedTableHashcodeColumn<String, Integer>("name", 13), false);
		TaggedTableHashcodeRow<String, Integer> r=new TaggedTableHashcodeRow<>();
		r.add("cnt", 1, false);
		r.add("extra", 32, false);
		r.add("name", 700, false);
		t.addRow(r,false);
		r=t.getRow(t.getRowKeys().get(0)).get();
		r.add("cnt",11,true);//test indirectly modify row previously added to table
		if(t.getRow(t.getRowKeys().get(0)).get().value("cnt")!=11) System.out.println("Row correctly not modified");
		else System.out.println("Row incorrectly modified");
		System.out.println("******************* test5 end *******************");
	}

	public static void test6(String[] args) {
		System.out.println("******************* test6 start *******************");
		System.out.println("test: add column when rows exist and remove the column");
		TaggedTableHashcode<String, String> t=new TaggedTableHashcode<>();
		t.addColumn(new TaggedTableHashcodeColumn<String, String>("cnt", "0"), false);
		t.addColumn(new TaggedTableHashcodeColumn<String, String>("name", ""), false);
		t.addColumn(new TaggedTableHashcodeColumn<String, String>("something", (String)null), false);
		t.addColumn(new TaggedTableHashcodeColumn<String, String>("extra", (String)null), false);
		TaggedTableHashcodeRow<String, String> r=new TaggedTableHashcodeRow<>();
		r.add("cnt", "1", false);
		r.add("something", "42", false);
		r.add("extra", "x", false);
		r.add("name", "me", false);
		t.addRow(r,false);
		r=new TaggedTableHashcodeRow<>();
		r.add("cnt", "2", false);
		r.add(t.getColumnName(1), "it", false);//add column by getting column from table by number. "name" is the second column but numbering starts at zero
		r.add("extra", "t", false);
		r.add("removeMe", "you wish", false);
		t.addRow(r,false);
		r=new TaggedTableHashcodeRow<>();
		r.add("something", "42", false);
		r.add("cnt", "3", false);
		r.add("name", "Im", false);
		r.add("extra", "r", false);
		t.addRow(r,false);
		t.addColumn(new TaggedTableHashcodeColumn<String, String>("removeMe", (String)null), false);
		if(t.containsColumn("removeMe")) System.out.println("Column correctly added");
		else System.out.println("Column incorrectly not added");
		t.removeColumn("removeMe");
		//check if row cnt=2 still has column
		if(t.getRows(
				new Predicate<Entry<Integer,TaggedTableHashcodeRow<String, String>>>() {
					public boolean test(Entry<Integer,TaggedTableHashcodeRow<String, String>> u){
						if(!u.getValue().contains("removeMe")) return false;
						if(u.getValue().value("removeMe")=="you wish") return true;
						return false;
					}
		}).size()>0) System.out.println("Column correctly exists in row");
		else System.out.println("Column incorrectly does not exist in row");
		System.out.println("******************* test6 end *******************");
	}

	public static void test7(String[] args) {
		System.out.println("******************* test7 start *******************");
		TaggedTableHashcodeRow<PolyType<?>, PolyType<?>> r=new TaggedTableHashcodeRow<>();
		final StringPoly colCntn=new StringPoly("cnt");
		IntegerPoly colCntv=new IntegerPoly(1);
		final StringPoly colSomn=new StringPoly("something");
		IntegerPoly colSomv=new IntegerPoly(42);
		final StringPoly colExn=new StringPoly("extra");
		StringPoly colExv=new StringPoly("x");
		final StringPoly colNan=new StringPoly("name");
		StringPoly colNav=new StringPoly("me");
		r.add(colCntn, colCntv, false);
		r.add(colSomn, colSomv, false);
		r.add(colExn, colExv, false);
		r.add(colNan, colNav, false);
		TaggedTableHashcode<PolyType<?>, PolyType<?>> t=new TaggedTableHashcode<>();
		ArrayList<Entry<Integer,TaggedTableHashcodeRow<PolyType<?>,PolyType<?>>>> b=t.addRow(r,false);//test adding row on table without columns
		if(b.size()>0) System.out.println("Row correctly not added since none of its columns exist in the table");
		colCntv=new IntegerPoly(0);
		colSomv=new IntegerPoly(null);
		colExv=new StringPoly("null");
		colNav=new StringPoly("");
		t.addColumn(new TaggedTableHashcodeColumn<PolyType<?>, PolyType<?>>(colCntn, colCntv), false);
		t.addColumn(new TaggedTableHashcodeColumn<PolyType<?>, PolyType<?>>(colNan, colNav), false);
		t.addColumn(new TaggedTableHashcodeColumn<PolyType<?>, PolyType<?>>(colSomn, colSomv), false);
		t.addColumn(new TaggedTableHashcodeColumn<PolyType<?>, PolyType<?>>(colExn, colExv), false);
		t.addRow(r,false);
		colCntv=new IntegerPoly(11);
		r.add(colCntn, colCntv,true);//test indirectly modify row previously added to table
		r=new TaggedTableHashcodeRow<>();
		colCntv=new IntegerPoly(2);
//		col2v=new IntegerPoly(null);
		colExv=new StringPoly("t");
		colNav=new StringPoly("it");
		r.add(colCntn, colCntv, false);
//		r.addColumn("name", "it", false);
		r.add(t.getColumnName(1), colNav, false);//add column by getting column from table by number. "name" is the forth column but numbering starts at zero
		r.add(colExn, colExv, false);
		t.addRow(r,false);
		r=new TaggedTableHashcodeRow<>();//try inserting columns in row in different order
		colCntv=new IntegerPoly(3);
		colSomv=new IntegerPoly(42);
		colExv=new StringPoly("r");
		colNav=new StringPoly("Im");
		r.add(colSomn, colSomv, false);
		r.add(colCntn, colCntv, false);
		r.add(colNan, colNav, false);
		r.add(colExn, colExv, false);
		t.addRow(r,false);
		t.addColumn(new TaggedTableHashcodeColumn<PolyType<?>, PolyType<?>>(new StringPoly("removeMe"), new StringPoly(null)), false);
		TaggedTableHashcodeHeader<PolyType<?>, PolyType<?>> h=t.getHeader();
		h.remove(new StringPoly("removeMe"));
//		TaggedTableCoreRows<Integer,PolyType<?>,PolyType<?>,TaggedTableHashcodeRow<PolyType<?>,PolyType<?>>> rs = t.getRows();//test start: test indirectly modify row previously added to table
		Iterator<Entry<Integer, TaggedTableHashcodeRow<PolyType<?>, PolyType<?>>>> it = t.rowsIterator();
		if(it.hasNext()) {
			TaggedTableHashcodeRow<PolyType<?>, PolyType<?>> rr=it.next().getValue();
			System.out.println(rr.hashCode());
			rr.add(colCntn, new IntegerPoly(100), true);
			System.out.println(rr.hashCode());
		}//test end
		System.out.println("Table:");
		System.out.println(t);
		System.out.println("Table:");
		System.out.println((new TaggedTableHashcode<PolyType<?>, PolyType<?>>(t,h, new FilterOutColumnCntGreater1Smaller3_2(),false)).toString());
		System.out.println("******************* test7 end *******************");
	}

	public static void main(String[] args) {
		test1(args);
		test2(args);
		test3(args);
		test4(args);
		test5(args);
		test6(args);
		test7(args);
		
//		HashMap<StringPoly, Integer> h=new HashMap<>();
//		StringPoly s1=new StringPoly("cnt");
//		StringPoly s2=new StringPoly("c");
//		h.put(s1, 0);
//		h.put(s2, 0);
//		System.out.println(h.containsKey(s1));
////		Object s="cnt";
//		System.out.println(h.containsKey(new StringPoly("cnt")));
//		System.out.println(s1.hashCode());
//		System.out.println((new StringPoly("cnt")).hashCode());
//		System.out.println(s1.equals(new StringPoly("cnt")));
////		System.out.println(s.equals("cnt"));
	}
	
}
