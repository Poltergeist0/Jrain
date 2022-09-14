package jrain.taggedTableSimple.mutable;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.function.Predicate;

import jrain.exceptions.ExceptionInvalidValue;
import jrain.polyType.mutable.IntegerPoly;
import jrain.polyType.mutable.PolyType;
import jrain.polyType.mutable.StringPoly;

public class TaggedTableSimple_test {
	
	public static class FilterOutColumnCntGreater1Smaller3 implements Predicate<Entry<String,TaggedTableSimpleRow<String, String>>>{
		  
		public boolean test(Entry<String,TaggedTableSimpleRow<String, String>> u){
			Integer i=0;
			if(!u.getValue().contains("cnt")) return false;
			try {
				i = Integer.valueOf(u.getValue().value("cnt"));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			if(i<=1 || i>=3) return true;
			return false;
		}
	}
	
	public static class FilterOutColumnCntGreater1Smaller3_2 implements Predicate<Entry<PolyType<?>,TaggedTableSimpleRow<PolyType<?>, PolyType<?>>>>{
		  
		public boolean test(Entry<PolyType<?>,TaggedTableSimpleRow<PolyType<?>, PolyType<?>>> u){
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
	
	public static void test1(String[] args) throws ExceptionInvalidValue {
		System.out.println("******************* test1 start *******************");
		TaggedTableSimpleRow<String, String> r=new TaggedTableSimpleRow<>();
		r.add("cnt", "1", false);
		r.add("something", "42", false);
		r.add("extra", "x", false);
		r.add("name", "me", false);
		TaggedTableSimple<String, String> t=new TaggedTableSimple<>();//new TaggedTableSimpleColumn<String, String>("cnt", "0"));
//		PairOfArrayList<Integer, Entry<String, TaggedTableSimpleColumnData<String>>> a = t.removeColumn(0);
//		PairOfArrayList<String, Entry<String, TaggedTableSimpleColumnData<String>>> b = t.removeColumn(TaggedTableSimpleColumnData.DELETED);
//		boolean b=t.addRow(r);//test adding row on table without columns
//		if(!b) System.out.println("Row correctly not added since none of its columns exist in the table");
		t.addColumn(new TaggedTableSimpleColumn<String, String>("cnt", "0"), false);
		t.addColumn(new TaggedTableSimpleColumn<String, String>("name", ""), false);
		t.addColumn(new TaggedTableSimpleColumn<String, String>("something", null), false);
		t.addColumn(new TaggedTableSimpleColumn<String, String>("extra", null), false);
		t.addRow(r,false);
		r.add("cnt","11",true);//test indirectly modify row previously added to table
		r=new TaggedTableSimpleRow<>();
		r.add("cnt", "2", false);
//		r.addColumn("name", "it", false);
		r.add(t.getColumnName(1), "it", false);//add column by getting column from table by number. "name" is the second column but numbering starts at zero
		r.add("extra", "t", false);
		t.addRow(r,false);
		r=new TaggedTableSimpleRow<>();
		r.add("something", "42", false);
		r.add("cnt", "3", false);
		r.add("name", "Im", false);
		r.add("extra", "r", false);
		t.addRow(r,false);
		t.addColumn(new TaggedTableSimpleColumn<String, String>("removeMe", null), false);
		TaggedTableSimpleHeader<String,String> h=t.getHeader();
		h.remove("removeMe");
//		TaggedTableCoreRows<String,String,String,TaggedTableSimpleRow<String,String>> rs = t.getRows();//test start: test indirectly modify row previously added to table
		Iterator<Entry<String, TaggedTableSimpleRow<String, String>>> it = t.rowsIterator();
		if(it.hasNext()) {
			TaggedTableSimpleRow<String, String> rr=it.next().getValue();
			System.out.println(rr.hashCode());
			rr.add("cnt", "100", true);
			System.out.println(rr.hashCode());
		}//test end
		System.out.println(t);
		System.out.println((new TaggedTableSimple<String, String>(t,h, new FilterOutColumnCntGreater1Smaller3(),false)).toString());
//		System.out.println((new TaggedTableSimple<String, String>(t,t.getHeader(), new FilterOutColumnCntGreater1Smaller3(),false)).toString());
		t.deleteRow("3");
		System.out.println(t);
		System.out.println("******************* test1 end *******************");
	}

	public static void test2(String[] args) throws ExceptionInvalidValue {
		System.out.println("******************* test2 start *******************");
		TaggedTableSimpleRow<PolyType<?>, PolyType<?>> r=new TaggedTableSimpleRow<>();
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
		colCntv=new IntegerPoly(0);
		colSomv=new IntegerPoly(null);
		colExv=new StringPoly("null");
		colNav=new StringPoly("");
		TaggedTableSimple<PolyType<?>, PolyType<?>> t=new TaggedTableSimple<>();
//		PairOfArrayList<Integer, Entry<PolyType<?>, TaggedTableSimpleColumnData<PolyType<?>>>> a = t.removeColumn(0);
//		PairOfArrayList<PolyType<?>, Entry<PolyType<?>, TaggedTableSimpleColumnData<PolyType<?>>>> b = t.removeColumn(new StringPoly(TaggedTableSimpleColumnData.DELETED));
//		boolean b=t.addRow(r);//test adding row on table without columns
//		if(!b) System.out.println("Row correctly not added since none of its columns exist in the table");
		t.addColumn(new TaggedTableSimpleColumn<PolyType<?>, PolyType<?>>(colCntn, colCntv), false);
		t.addColumn(new TaggedTableSimpleColumn<PolyType<?>, PolyType<?>>(colNan, colNav), false);
		t.addColumn(new TaggedTableSimpleColumn<PolyType<?>, PolyType<?>>(colSomn, colSomv), false);
		t.addColumn(new TaggedTableSimpleColumn<PolyType<?>, PolyType<?>>(colExn, colExv), false);
		t.addRow(r,false);
		colCntv=new IntegerPoly(11);
		r.add(colCntn, colCntv,true);//test indirectly modify row previously added to table
		r=new TaggedTableSimpleRow<>();
		colCntv=new IntegerPoly(2);
//		col2v=new IntegerPoly(null);
		colExv=new StringPoly("t");
		colNav=new StringPoly("it");
		r.add(colCntn, colCntv, false);
//		r.addColumn("name", "it", false);
		r.add(t.getColumnName(1), colNav, false);//add column by getting column from table by number. "name" is the forth column but numbering starts at zero
		r.add(colExn, colExv, false);
		t.addRow(r,false);
		r=new TaggedTableSimpleRow<>();//try inserting columns in row in different order
		colCntv=new IntegerPoly(3);
		colSomv=new IntegerPoly(42);
		colExv=new StringPoly("r");
		colNav=new StringPoly("Im");
		r.add(colSomn, colSomv, false);
		r.add(colCntn, colCntv, false);
		r.add(colNan, colNav, false);
		r.add(colExn, colExv, false);
		t.addRow(r,false);
		t.addColumn(new TaggedTableSimpleColumn<PolyType<?>, PolyType<?>>(new StringPoly("removeMe"), null), false);
		TaggedTableSimpleHeader<PolyType<?>, PolyType<?>> h=t.getHeader();
		h.delete(new StringPoly("removeMe"));
//		TaggedTableCoreRows<PolyType<?>,PolyType<?>,PolyType<?>,TaggedTableSimpleRow<PolyType<?>,PolyType<?>>> rs = t.getRows();//test start: test indirectly modify row previously added to table
		Iterator<Entry<PolyType<?>, TaggedTableSimpleRow<PolyType<?>, PolyType<?>>>> it = t.rowsIterator();
		if(it.hasNext()) {
			TaggedTableSimpleRow<PolyType<?>, PolyType<?>> rr=it.next().getValue();
			System.out.println(rr.hashCode());
			rr.add(colCntn, new IntegerPoly(100), true);
			System.out.println(rr.hashCode());
		}//test end
		System.out.println(t);
		System.out.println((new TaggedTableSimple<PolyType<?>, PolyType<?>>(t,h, new FilterOutColumnCntGreater1Smaller3_2(),false)).toString());
		System.out.println("******************* test2 end *******************");
	}

	public static void main(String[] args) throws ExceptionInvalidValue {
		test1(args);
		test2(args);
		
//		HashMap<TypeString, Integer> h=new HashMap<>();
//		TypeString s1=new TypeString("cnt");
//		TypeString s2=new TypeString("c");
//		h.put(s1, 0);
//		h.put(s2, 0);
//		System.out.println(h.containsKey(s1));
////		Object s="cnt";
//		System.out.println(h.containsKey(new TypeString("cnt")));
//		System.out.println(s1.hashCode());
//		System.out.println((new TypeString("cnt")).hashCode());
//		System.out.println(s1.equals(new TypeString("cnt")));
////		System.out.println(s.equals("cnt"));
	}
	
}
