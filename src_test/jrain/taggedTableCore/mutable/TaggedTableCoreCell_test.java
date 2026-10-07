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
package jrain.taggedTableCore.mutable;

import jrain.deepCopy.DeepCopy;

public class TaggedTableCoreCell_test {
	
	public static class StringMy implements DeepCopy<StringMy>{
		public String s;
		
		public StringMy() {s="";}
		
		public StringMy(StringMy sm) {s=sm.s;}

		public StringMy(String sm) {s=sm;}

		@Override
		public StringMy deepCopy() {
			return new StringMy(s);//string are already immutable
		}
		
		@Override
		public String toString() {
			return s.toString();
		}
	}
	public static class TestTaggedTableCoreCell extends TaggedTableCoreCell<String, String>{
		
		public TestTaggedTableCoreCell(String name,String value) {
			super(name, value);
		}
		
		@Override
		public int hashCode() {
			return ((super.getKey() == null) ? 0 : super.getKey().hashCode());
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null || obj.getClass()!= this.getClass()) {
				return false;
			}
			TestTaggedTableCoreCell other = (TestTaggedTableCoreCell) obj;//use unbounded to avoid warning about cast type safety
			if (super.getKey() == null) {
				if (other.getKey() != null) {
					return false;
				}
			} else if (!super.getKey().equals(other.getKey())) {
				return false;
			}
			return true;
		}

		@Override
		public String separatorValue() {
			return TaggedTableCoreCell.SEPARATOR;
		}
	}
	
	public static class ClassA implements DeepCopy<ClassA>{
		int a=0;
		public String toString() {return String.valueOf(a);}
		@Override
		public ClassA deepCopy() {
			ClassA c=new ClassA();
			c.a=a;
			return c;
		}
	}
	
	public static class ClassB implements DeepCopy<ClassB>{
		double a=0.1;
		public String toString() {return String.valueOf(a);}

		@Override
		public ClassB deepCopy() {
			ClassB c=new ClassB();
			c.a=a;
			return c;
		}
	}
	
	public static class TestTaggedTableCoreCell2 extends TaggedTableCoreCell<ClassA,ClassB>{
		
		public TestTaggedTableCoreCell2(ClassA name,ClassB value) {
			super(name, value);
		}
		
		public TestTaggedTableCoreCell2(TestTaggedTableCoreCell2 e) {
			super(e);
		}
		
		@Override
		public int hashCode() {
			return ((super.getKey() == null) ? 0 : super.getKey().hashCode());
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null || obj.getClass()!= this.getClass()) {
				return false;
			}
			TestTaggedTableCoreCell2 other = (TestTaggedTableCoreCell2) obj;//use unbounded to avoid warning about cast type safety
			if (super.getKey() == null) {
				if (other.getKey() != null) {
					return false;
				}
			} else if (!super.getKey().equals(other.getKey())) {
				return false;
			}
			return true;
		}
	}
	
	public static void test1(String[] args) {
		System.out.println("******************* test1 start *******************");
		TestTaggedTableCoreCell r=new TestTaggedTableCoreCell("cnt", "1");
		System.out.println(r);
		String a=r.getValue();
		a="2";
		System.out.println(r);
		r.setValue(a);
		System.out.println(r);
		System.out.println("******************* test1 end *******************");
	}
	
	public static void test2(String[] args) {
		System.out.println("******************* test2 start *******************");
		ClassB cb = new ClassB();
		TestTaggedTableCoreCell2 r=new TestTaggedTableCoreCell2(new ClassA(), cb);
		System.out.println(r);
		ClassB a=r.getValue();
		a.a=2.1;//this will modify r since it is a reference to the original
		TestTaggedTableCoreCell2 r1=new TestTaggedTableCoreCell2(r);//deep copy
		ClassB b=r.getValue();
		b.a=4.2;//this will no longer change the original (r) but will change r1 instead
		System.out.println(r1);
//		r.value(a);
		System.out.println(r);
		System.out.println("******************* test2 end *******************");
	}
	
	public static void main(String[] args) {
		test1(args);
		test2(args);
	}
	
}
