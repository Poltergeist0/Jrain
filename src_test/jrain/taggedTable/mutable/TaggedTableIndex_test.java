package jrain.taggedTable.mutable;

import java.util.ArrayList;

import jrain.identifiable.immutable.Identifiable;
import jrain.polyType.mutable.PolyType;

public class TaggedTableIndex_test {

	public static void main(String[] args) {
		TaggedTableIndex t=new TaggedTableIndex();
		PolyType<String> qwe = new PolyType<>("qwe");
		PolyType<String> zaq = new PolyType<>("zaq");
		Identifiable id=new Identifiable();
		t.add(qwe, id);
		t.add(qwe, new Identifiable());
		t.add(zaq, new Identifiable());
		t.add(qwe, new Identifiable());
		t.add(qwe, new Identifiable());
		System.out.println(id);
		System.out.println(t.size());
		System.out.println("remove (qwe, id)");
		TaggedTableIndex tt = new TaggedTableIndex(t,true);
		tt.remove(qwe, id);
		System.out.println(t);
		System.out.println(tt);
		System.out.println(tt.size());
		System.out.println("remove id");
		tt = new TaggedTableIndex(t,true);
		tt.remove(id);
		System.out.println(t);
		System.out.println(tt);
		System.out.println(tt.size());
		System.out.println("subset true");
		tt = t.subSet(qwe, true);
		System.out.println(t);
		System.out.println(tt);
		System.out.println(tt.size());
		System.out.println("subset false");
		tt = t.subSet(qwe, false);
		System.out.println(t);
		System.out.println(tt);
		ArrayList<Identifiable> a=new ArrayList<>();
		a.add(id);
		System.out.println(tt.size());
		System.out.println("subsetvalues true");
		tt = t.subSetValues(a, true);
		System.out.println(t);
		System.out.println(tt);
		System.out.println(tt.size());
		System.out.println("subsetvalues false");
		tt = t.subSetValues(a, false);
		System.out.println(t);
		System.out.println(tt);
		System.out.println(tt.size());
	}

}
