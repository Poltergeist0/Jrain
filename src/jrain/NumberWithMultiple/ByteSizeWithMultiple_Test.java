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
package jrain.NumberWithMultiple;

public class ByteSizeWithMultiple_Test {

	public static void main(String[] args) {
		ByteSizeWithMultiple a=new ByteSizeWithMultiple(13, Numbers.BinaryMultiples.Ui);
		System.out.println(a);
		System.out.println(a.get());
		System.out.println(a.toString());
		System.out.println(a.number());
		System.out.println(a.multiple());
//		System.out.println(a.);
		a=new ByteSizeWithMultiple(1, Numbers.BinaryMultiples.Mi);
		System.out.println(a);
		System.out.println(a.get());
		System.out.println(a.toString());
		System.out.println(a.number());
		System.out.println(a.multiple());
//		System.out.println(a.);
	}

}
