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
package jrain.polyType.hash.immutable;


/**
 * @author poltergeist0
 *
 * Polymorphic hash data type for XOR hash instance.
 */
public class XOR8Poly extends PolyHash<Byte>{

	/**
	 * Constructor for XOR
	 * 
	 * @param value is the XOR instance
	 */
	public XOR8Poly(Byte value) { super(value);}

	/**
	 * Copy constructor
	 * 
	 * @param poly is the original object to be copied
	 * @param deepCopy if true makes a deep copy of the object, if possible. False makes a shallow copy
	 */
	public XOR8Poly(XOR8Poly poly,boolean deepCopy) { super(poly,false);}
	
	@Override
	public XOR8Poly deepCopy() {
		return new XOR8Poly(this,false);
	}
	
	public void update(byte[] b, int offset, int len){
		byte mdxor=super.get();
		for (int i = offset; i < b.length; i++) {
			mdxor^=b[i];
		}
		super.set(mdxor);
	}

	public byte[] digest(){
		byte[] a=new byte[1];
		a[0]=super.get();
		return a;
	}

}
