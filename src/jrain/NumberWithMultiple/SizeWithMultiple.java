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

import jrain.NumberWithMultiple.Numbers.NumbersBase;
import jrain.entry.mutable.Pair;

/**
 * @author poltergeist0
 *
 */
public class SizeWithMultiple<TYPE_MULTIPLE extends NumbersBase<TYPE_MULTIPLE>> extends NumberWithUnits<Long, TYPE_MULTIPLE>{

	public SizeWithMultiple(Long number, TYPE_MULTIPLE multiple,String unit) {
		super(number,multiple,unit);
	}

	/**
	 * Convert number to the biggest possible multiple without rounding.
	 * Uses difference between consecutive exponents so that it can accommodate non-linear scales.
	 * @param number
	 * @param multiple
	 * @return a pair with the converted number and multiple
	 */
	public static <TYPE_MULTIPLE extends NumbersBase<TYPE_MULTIPLE>> SizeWithMultiple<TYPE_MULTIPLE> toSizeWithMultiple(Long number,TYPE_MULTIPLE multiple,String unit) {
		Pair<Long, TYPE_MULTIPLE> a = NumberWithUnits.toNumberWithUnits(number, multiple);
		SizeWithMultiple<TYPE_MULTIPLE> resp=new SizeWithMultiple<TYPE_MULTIPLE>(a.getKey(), a.getValue(),unit);
		return resp;
	}

}
