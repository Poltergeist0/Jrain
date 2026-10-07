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

import jrain.NumberWithMultiple.Numbers.BinaryMultiples;
import jrain.entry.mutable.Pair;

/**
 * @author poltergeist0
 *
 */
public class ByteSizeWithMultiple extends NumberWithUnits<Long, BinaryMultiples>{

	public ByteSizeWithMultiple(long number, BinaryMultiples multiple) {
		super(number,multiple,"B");
	}
	
	public ByteSizeWithMultiple(long number) {
		super(number,BinaryMultiples.Ui,"B");
	}
	
	public ByteSizeWithMultiple() {
		super((long)0, BinaryMultiples.Ui,"B");
	}

	/**
	 * @return the number with the multiple
	 */
	//	public double get() {return n.doubleValue()*m.toNumber();}
	public int toInt() {
		return ((Long)(number()*multiple().toLongNumber())).intValue();
	}

	/**
	 * @return the number with the multiple
	 */
	public Long toLong() {
		return number()*multiple().toLongNumber();
	}


	/**
	 * Convert number to the biggest possible multiple without rounding.
	 * Uses difference between consecutive exponents so that it can accommodate non-linear scales.
	 * @param number
	 * @param multiple
	 * @return a pair with the converted number and multiple
	 */
	public static ByteSizeWithMultiple toByteSizeWithMultiple(Long number,BinaryMultiples multiple) {
		Pair<Long, BinaryMultiples> a = NumberWithUnits.toNumberWithUnits(number, multiple);
		ByteSizeWithMultiple resp=new ByteSizeWithMultiple(a.getKey(), a.getValue());
		return resp;
	}

}
