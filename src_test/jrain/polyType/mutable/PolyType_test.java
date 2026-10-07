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
package jrain.polyType.mutable;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PolyType_test {
	
	@Test
	@DisplayName("Test PolyType")
	void testPolyType() {
		PolyType<?> p1=new IntegerPoly(13);
		assertEquals(13,p1.get());
		DoublePoly a = new DoublePoly(42.7);
		p1=a;
		assertEquals(a.get(),p1.get());
	}
	
	@Test
	@DisplayName("Test standard container")
	void testStandardContainer() {
		HashMap<StringPoly, Integer> h=new HashMap<>();
		StringPoly s1=new StringPoly("cnt");
		StringPoly s2=new StringPoly("c");
		h.put(s1, 20);
		h.put(s2, 10);
		assertTrue(h.containsKey(s1));
		assertTrue(h.containsKey(new StringPoly("cnt")));
		assertEquals((new StringPoly("cnt")).hashCode(),s1.hashCode());
		assertTrue(s1.equals(new StringPoly("cnt")));
	}
	
	@Test
	@DisplayName("Test custom container")
	void testCustomContainer() {
		StringPoly s1=new StringPoly("cnt");
		StringPoly s2=new StringPoly("c");
		Qwerty<PolyType<?>,PolyType<?>> t =new Qwerty<>();
		t.put(s1, new IntegerPoly(20));
		t.put(s2, new IntegerPoly(10));
		assertTrue(t.containsKey(s1));
		assertTrue(t.containsKey(s2));
		assertTrue(t.containsKey(new StringPoly("cnt")));
		assertTrue(t.containsKey(new StringPoly("c")));
		assertEquals(new IntegerPoly(20), t.get(s1));
		assertEquals(new IntegerPoly(10), t.get(s2));
		t.put(s2, new IntegerPoly(300));
		assertEquals(new IntegerPoly(300), t.get(s2));
		t.put(s2, new DoublePoly(66.6));
		assertEquals(new IntegerPoly(300), t.get(s2));
		t.put(s2, new StringPoly("qwerty"));
		assertEquals(new IntegerPoly(300), t.get(s2));
	}
	
	public static class Qwerty<T extends Object, U extends Object>{
		HashMap<T,U> hm;
		Class<?> classT;
		Class<?> classU;
		Qwerty(){hm=new HashMap<>();}
		void put(T a, U b) {
//			System.out.println("instance(a)="+a.getClass()+" ; instance(b)="+b.getClass());
			//disallow putting if the classes don't match unless the map is empty since, obviously, there are no classes yet on an empty map
			if(hm.size()<=0) {
				classT=a.getClass();
				classU=b.getClass();
				hm.put(a, b);
			}
			else {
				if(a.getClass()==classT && b.getClass()==classU) hm.put(a, b);
			}
		}
		boolean containsKey(T a) {return hm.containsKey(a);}
		public String toString() {return hm.toString();}
		public U get(T key) {return hm.get(key);}
	}

}
