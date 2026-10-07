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

import java.security.MessageDigest;


/**
 * @author poltergeist0
 *
 * Polymorphic hash data type for {@link MessageDigest} hash instance.
 */
public class MessageDigestPoly extends PolyHash<MessageDigest> {

	/**
	 * Constructor for {@link MessageDigest}
	 * 
	 * @param value is the {@link MessageDigest} instance
	 */
	public MessageDigestPoly(MessageDigest value) { super(value);}
	
	/**
	 * Copy constructor
	 * 
	 * @param poly is the original object to be copied
	 * @param deepCopy if true makes a deep copy of the object, if possible. False makes a shallow copy
	 */
	public MessageDigestPoly(MessageDigestPoly poly,boolean deepCopy) { super(poly,false);}

	@Override
	public MessageDigestPoly deepCopy() {
		return new MessageDigestPoly(this,false);
	}

	public void update(byte[] b, int offset, int len){
		super.get().update(b,offset,len);
	}

	public byte[] digest(){
		return super.get().digest();
	}
	
}
