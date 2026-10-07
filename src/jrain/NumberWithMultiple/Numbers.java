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

/**
 * @author poltergeist0
 *
 */
public interface Numbers {

	public interface NumbersBase<ENUM>{
	    public double toNumber();
	    public int base();
	    public int exponent();
	    public String toString();
//	    public <TYPE extends NumbersBase> TYPE[] asArray();
	    public ENUM[] asArray();
	    public int ordinal();
	    public String name();
	}
	
	/**
	 * @author poltergeist0
	 * Internal storage for the different multiples enum classes defined below.
	 * 
	 * DO NOT USE DIRECTLY
	 */
	public class MultiplesBase{// implements NumbersBase except toNumber method
	    private final int ex;//exponent
	    private final int ba;
	    protected MultiplesBase(int base,int exponent) {
	    	ba=base;
	        ex = exponent;
	    }

	    /**
	     * Only returns the correct value for positive integer bases with positive integer exponents
	     * @return
	     */
	    public Long toLongNumber() {
	    	return ((Double)Math.pow(ba, ex)).longValue();
	    }

//	    public Double toDoubleNumber() {
	    public Double toNumber() {
	    	return Math.pow(ba, ex);
	    }

	    public int base() {
	        return ba;
	    }
	    
	    public int exponent() {
	        return ex;
	    }
	    
	    public Class<MultiplesBase> type() {return MultiplesBase.class;}
	    
	    /**
	     * Must be overridden by the enum implementing the interface
	     */
//	    public <TYPE extends NumbersBase> TYPE[] asArray() {return null;}
	}
	
	public enum BinaryMultiples implements NumbersBase<BinaryMultiples>{
		Ui(2,0),Ki(2,10),Mi(2,20),Gi(2,30),Ti(2,40),Pi(2,50);

		final MultiplesBase b;
	    private BinaryMultiples(int base,int exponent) {
	        b=new MultiplesBase(base, exponent);
	    }

	    public Long toLongNumber() {
	    	return b.toLongNumber();
	    }

	    public double toNumber() {
//	        return b.toLongNumber();
	    	return b.toNumber();
	    }

	    public int base() {
	        return b.base();
	    }
	    
	    public int exponent() {
	        return b.exponent();
	    }
	    
	    public Class<BinaryMultiples> type() {return BinaryMultiples.class;}
	    
	    public String toString() {
	    	if(this==Ui) return "";//do not print multiplier for unit
	    	return name();
	    }
	    
//	    public BinaryMultiples valueOf(String str) {
//	    	
//	    }
	    
	    public BinaryMultiples[] asArray() {
	    	return BinaryMultiples.values();
	    }
	    
	    public static String[] asStringArray() {
	    	BinaryMultiples[] a = Ui.asArray();
	    	String [] s=new String[a.length];
	    	int pos=0;
	    	while(pos<a.length) {
	    		s[pos]=a[pos].toString();
	    		++pos;
	    	}
	    	return s;
	    }
	    
	    public static String[] asStringArray(String prepend, String append) {
	    	BinaryMultiples[] a = Ui.asArray();
	    	String [] s=new String[a.length];
	    	int pos=0;
	    	while(pos<a.length) {
	    		s[pos]=prepend+a[pos].toString()+append;
	    		++pos;
	    	}
	    	return s;
	    }
	    
	    public static BinaryMultiples fromExponent(int exponent) {
	    	BinaryMultiples[] a = Ui.asArray();
	    	int pos=0;
	    	while(pos<a.length) {
	    		if(a[pos].exponent()==exponent) return a[pos];
	    		++pos;
	    	}
	    	return Ui;
	    }
	}

	public enum IntegerMultiples implements NumbersBase<IntegerMultiples>{
		U(10,0),K(10,3),M(10,6),G(10,9),T(10,12),P(10,15);

		final MultiplesBase b;
	    private IntegerMultiples(int base,int exponent) {
	        b=new MultiplesBase(base, exponent);
	    }

	    public Long toLongNumber() {
	    	return b.toLongNumber();
	    }

	    public double toNumber() {
	        return b.toNumber();
	    }

	    public int base() {
	        return b.base();
	    }
	    
	    public int exponent() {
	        return b.exponent();
	    }
	    
	    public Class<IntegerMultiples> type() {return IntegerMultiples.class;}
	    
	    public String toString() {
	    	if(this==U) return "";//do not print multiplier for unit
	    	return name();
	    }
	    public IntegerMultiples[] asArray() {
	    	return IntegerMultiples.values();
	    }

	    public static String[] asStringArray() {
	    	IntegerMultiples[] a = U.asArray();
	    	String [] s=new String[a.length];
	    	int pos=0;
	    	while(pos<a.length) {
	    		s[pos]=a[pos].toString();
	    		++pos;
	    	}
	    	return s;
	    }
	    
	    public static IntegerMultiples fromExponent(int exponent) {
	    	IntegerMultiples[] a = U.asArray();
	    	int pos=0;
	    	while(pos<a.length) {
	    		if(a[pos].exponent()==exponent) return a[pos];
	    		++pos;
	    	}
	    	return U;
	    }
	}

	public enum Multiples implements NumbersBase<Multiples>{
		p(10,-12),n(10,-9),u(10,-6),m(10,-3),U(10,0),K(10,3),M(10,6),G(10,9),T(10,12),P(10,15);

		final MultiplesBase b;
	    private Multiples(int base,int exponent) {
	        b=new MultiplesBase(base, exponent);
	    }

	    public double toNumber() {
	        return b.toNumber();
	    }

	    public int base() {
	        return b.base();
	    }
	    
	    public int exponent() {
	        return b.exponent();
	    }
	    
	    public Class<Multiples> type() {return Multiples.class;}
	    
	    public String toString() {
	    	if(this==U) return "";//do not print multiplier for unit
	    	return name();
	    }
	    public Multiples[] asArray() {
	    	return Multiples.values();
	    }

	    public static String[] asStringArray() {
	    	Multiples[] a = U.asArray();
	    	String [] s=new String[a.length];
	    	int pos=0;
	    	while(pos<a.length) {
	    		s[pos]=a[pos].toString();
	    		++pos;
	    	}
	    	return s;
	    }
	    
	    public static Multiples fromExponent(int exponent) {
	    	Multiples[] a = U.asArray();
	    	int pos=0;
	    	while(pos<a.length) {
	    		if(a[pos].exponent()==exponent) return a[pos];
	    		++pos;
	    	}
	    	return U;
	    }
	}

}
