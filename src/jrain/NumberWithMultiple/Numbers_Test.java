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

public class Numbers_Test {

	public static void main(String[] args) {
		System.out.println(Numbers.IntegerMultiples.K);
		System.out.println(Numbers.IntegerMultiples.K.ordinal());
		System.out.println(Numbers.IntegerMultiples.K.exponent());
		System.out.println(Numbers.IntegerMultiples.K.name());
		System.out.println(Numbers.IntegerMultiples.K.toNumber());
		System.out.println(Numbers.IntegerMultiples.K.toString());
//		System.out.println(Numbers.IntegerMultiples.K.);
		System.out.println(Numbers.Multiples.n.toNumber());
		System.out.println(Numbers.BinaryMultiples.Ki);
		System.out.println(Numbers.BinaryMultiples.Ki.ordinal());
		System.out.println(Numbers.BinaryMultiples.Ki.exponent());
		System.out.println(Numbers.BinaryMultiples.Ki.name());
		System.out.println(Numbers.BinaryMultiples.Ki.toNumber());
		System.out.println(Numbers.BinaryMultiples.Ki.toString());
		System.out.println(Numbers.BinaryMultiples.Ui);
		System.out.println(Numbers.BinaryMultiples.Ui.name());
		Numbers.NumbersBase<?> a = Numbers.BinaryMultiples.values()[3];
		System.out.println(a);
		System.out.println(a.getClass());
		NumberWithUnits<Long, ?> num=new NumberWithUnits<>((long)4096, Numbers.BinaryMultiples.Ki, "B");
		System.out.println(num);
//		NumberWithUnits<?, ?> resp=NumberWithUnits.toNumberWithUnits(num, "B");
//		System.out.println(resp);
//		System.out.println(resp.multiple().getClass());
//		System.out.println(Numbers.BinaryMultiples.Ui.);
	}

}
