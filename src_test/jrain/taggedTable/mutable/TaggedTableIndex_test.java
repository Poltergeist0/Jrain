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
