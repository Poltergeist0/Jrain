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
 * Interface for implementation of a number with a multiple.
 * Example: sizes of files with B/KB/MB/GB multiple.
 * 
 */
public class NumberWithUnits<TYPE_NUMBER extends Number, TYPE_MULTIPLE extends NumbersBase<TYPE_MULTIPLE> > {

	private final String u;//unit
	private final TYPE_NUMBER n;//number
	private final TYPE_MULTIPLE m;//multiple
	
//	public static <TYPE_NUMBER extends Number, TYPE_ENUM, TYPE_MULTIPLE extends NumbersBase<TYPE_ENUM> > NumberWithUnits<TYPE_NUMBER, TYPE_ENUM, TYPE_MULTIPLE> toNumberWithUnits(TYPE_NUMBER number, TYPE_MULTIPLE multiple) {
//		TYPE_ENUM[] a = multiple.asArray();
//		int ex=multiple.exponent();
//		TYPE_NUMBER n=number;
//		while(((Number)n)%Math.round(Math.pow(multiple.base(), ex))) {
//			
//		}
//	}
	/**
	 * Convert number to the biggest possible multiple without rounding.
	 * 
	 * @param <TYPE_ENUM>
	 * @param <TYPE_MULTIPLE>
	 * @param number
	 * @param unit
	 * @return
	 */
//	public static <TYPE_MULTIPLE extends NumbersBase<TYPE_MULTIPLE>, TYPE_RETURN extends NumberWithUnits<Long, TYPE_MULTIPLE> > TYPE_RETURN toNumberWithUnits(TYPE_RETURN number, String unit) {
//		NumbersBase<TYPE_MULTIPLE>[] arr = number.multiple().asArray();
//		int pos=number.multiple().ordinal();
//		int ex=arr[pos].exponent();
//		int df=arr[pos+1].exponent()-ex;
//		int bs=number.multiple().base();
//		Long n=number.number();
//		while(n%Math.round(Math.pow(bs, df))==0) {
//			n=n/Math.round(Math.pow(bs,df));
//			pos+=1;
//			ex=arr[pos].exponent();
//			df=arr[pos+1].exponent()-ex;
//		}
//		NumberWithUnits<Long, TYPE_MULTIPLE> resp=new NumberWithUnits<Long, TYPE_MULTIPLE>(n, (TYPE_MULTIPLE) arr[pos], unit);
//		return resp;
//	}
//
	/**
	 * Convert number to the biggest possible multiple without rounding.
	 * Uses difference between consecutive exponents so that it can accommodate non-linear scales.
	 * @param <TYPE_MULTIPLE>
	 * @param number
	 * @param multiple
	 * @return a pair with the converted number and multiple
	 */
	public static <TYPE_MULTIPLE extends NumbersBase<TYPE_MULTIPLE> > Pair<Long, TYPE_MULTIPLE> toNumberWithUnits(Long number,TYPE_MULTIPLE multiple) {
		TYPE_MULTIPLE[] arr = multiple.asArray();
		Pair<Long, TYPE_MULTIPLE> resp;
		if(number==0) {
			resp=new Pair<Long,TYPE_MULTIPLE>((long)0, arr[0]);
		}
		else {
			int pos=multiple.ordinal();//position of current exponent
			int ex=arr[pos].exponent();//current exponent
			int df=arr[pos+1].exponent()-ex;//difference between current exponent and next exponent
			int bs=multiple.base();//base
			Long n=number;
			while(n%Math.round(Math.pow(bs, df))==0) {//if remainder of division of number by the difference between exponents is zero
				n=n/Math.round(Math.pow(bs,df));//perform division
				pos+=1;//get next set of exponents
				ex=arr[pos].exponent();
				df=arr[pos+1].exponent()-ex;
			}
			resp=new Pair<Long,TYPE_MULTIPLE>(n, arr[pos]);
		}
		return resp;
	}

	public static <TYPE_MULTIPLE extends NumbersBase<TYPE_MULTIPLE> > Pair<Double, TYPE_MULTIPLE> toNumberWithUnits(Double number,TYPE_MULTIPLE multiple) {
		TYPE_MULTIPLE[] arr = multiple.asArray();
		int pos=multiple.ordinal();//position of current exponent
		int ex=arr[pos].exponent();//current exponent
		int df=arr[pos+1].exponent()-ex;//difference between current exponent and next exponent
		int bs=multiple.base();//base
		Double n=number;
		while(n%Math.round(Math.pow(bs, df))==0) {//if remainder of division of number by the difference between exponents is zero
			n=n/Math.round(Math.pow(bs,df));//perform division
			pos+=1;//get next set of exponents
			ex=arr[pos].exponent();
			df=arr[pos+1].exponent()-ex;
		}
		Pair<Double, TYPE_MULTIPLE> resp=new Pair<Double,TYPE_MULTIPLE>(n, arr[pos]);
		return resp;
	}

//	public static <TYPE_ENUM, TYPE_MULTIPLE extends NumbersBase<TYPE_ENUM> > NumberWithUnits<Double, TYPE_ENUM, TYPE_MULTIPLE> toNumberWithUnits(NumberWithUnits<Double, TYPE_ENUM, TYPE_MULTIPLE> number, String unit) {
//		NumbersBase<TYPE_ENUM>[] arr = number.multiple().asArray();
//		int pos=number.multiple().ordinal();
//		int ex=arr[pos].exponent();
//		int df=arr[pos+1].exponent()-ex;
//		int bs=number.multiple().base();
//		Double n=number.number();
//		while(n%Math.round(Math.pow(bs, df))==0) {
//			n=n/Math.round(Math.pow(bs,df));
//			pos+=1;
//			ex=arr[pos].exponent();
//			df=arr[pos+1].exponent()-ex;
//		}
//		NumberWithUnits<Double, TYPE_ENUM, TYPE_MULTIPLE> resp=new NumberWithUnits<Double, TYPE_ENUM, TYPE_MULTIPLE>(n, (TYPE_MULTIPLE) arr[pos], unit);
//		return resp;
//	}

//	public static <TYPE_MULTIPLE extends NumbersBase<TYPE_MULTIPLE> > NumberWithUnits<Double, TYPE_MULTIPLE> toNumberWithUnits(Double number,TYPE_MULTIPLE multiple, String unit) {
//		NumbersBase<TYPE_MULTIPLE>[] arr = multiple.asArray();
//		int pos=multiple.ordinal();
//		int ex=arr[pos].exponent();
//		int df=arr[pos+1].exponent()-ex;
//		int bs=multiple.base();
//		Double n=number;
//		while(n%Math.round(Math.pow(bs, df))==0) {
//			n=n/Math.round(Math.pow(bs,df));
//			pos+=1;
//			ex=arr[pos].exponent();
//			df=arr[pos+1].exponent()-ex;
//		}
//		NumberWithUnits<Double, TYPE_MULTIPLE> resp=new NumberWithUnits<Double, TYPE_MULTIPLE>(n, (TYPE_MULTIPLE) arr[pos], unit);
//		return resp;
//	}

	public NumberWithUnits(TYPE_NUMBER number, TYPE_MULTIPLE multiple,String unit) {
		u=unit;
		n=number;
		m=multiple;
	}
	
	/**
	 * @return the number without the multiplier
	 */
	public TYPE_NUMBER number() {return n;}
	
	/**
	 * @return the multiple
	 */
	public TYPE_MULTIPLE multiple() {return m;}
	
	/**
	 * @return the unit
	 */
	public String unit() {return u;}
	
	/**
	 * @return the number with the multiple
	 */
//	public double get() {return n.doubleValue()*m.toNumber();}
	public Double get() {
//		if(Integer.class.equals(n.getClass())) {
//			return n.intValue()*m.toNumber();
//		}
//		return n.doubleValue()*m.toNumber();
		return n.doubleValue()*m.toNumber();
	}

	public String toString() {
		return n.toString()+m.toString()+u;
	}
}
