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
package jrain.hash.hashInstance.immutable;

import java.util.Arrays;

import jrain.polyType.hash.immutable.PolyHash;
import jrain.array.utils.ByteArrayUtils;
import jrain.utils.HexadecimalUtils;

/**
 * @author poltergeist0
 * 
 * Class that imitates the interface of MessageDigest but for a larger number of 
 * hashes by accepting an instance of a hash that extends {@link PolyHash}.
 */
public class HashInstance implements jrain.hash.hashInstance.HashInstance{
	
	/*
	 * Indicates if the hash was calculated or loaded
	 */
	private final boolean calculate;
	
	/*
	 * If the hash is calculated, this distinguishes between still being 
	 * calculated (true) or calculation finished (false).
	 */
	private boolean active;
	
	/*
	 * Selected algorithm
	 */
	private final String alg;
	
	/*
	 * Instance of the hash if it is calculated, otherwise null
	 */
	private final PolyHash<?> md;
	
	/*
	 * Calculated/loaded hash
	 */
	private byte[] hsh;

	/**
	 * Calculate a hash
	 * 
	 * @param algorithm is the name of the algorithm
	 * @param instance is the object that calculates the hash
	 */
	public HashInstance(String algorithm,PolyHash<?> instance) {
		alg=algorithm;
		calculate=true;
		active=true;
		md=instance;
		hsh=EMPTYBYTEARRAY;
	}

	/**
	 * Load a hash
	 * 
	 * @param algorithm is the name of the algorithm
	 * @param message is the hash
	 */
	public HashInstance(String algorithm,final byte[] message){
		alg=algorithm;
		calculate=false;
		active=true;
		md=null;
		if(message==null)hsh=HashInstance.EMPTYBYTEARRAY;
		else hsh=Arrays.copyOf(message,message.length);
		active=false;
	}

	public void update(byte[] b, int offset, int len){
		if(!calculate) return;//if it is not being calculated do nothing
		md.update(b,offset,len);
	}

	public byte[] digest(){
		if(active) {//if being calculated
			hsh=md.digest();//finish calculation
			active=false;//stop calculating
		}
		return hsh;
	}

	/**
	 * @return true if at least one hash exists, false otherwise
	 */
	public boolean hasHash(){
		if(!active && hsh!=EMPTYBYTEARRAY) return true;
		return false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (active ? 1231 : 1237);
		result = prime * result + Arrays.hashCode(hsh);
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof HashInstance)) {
			return false;
		}
		HashInstance other = (HashInstance) obj;
		if(active) return false;//if it is currently calculating then can not compare hashes
		if (active != other.active) {
			return false;
		}
		if(ByteArrayUtils.equalsNull(hsh, 0, hsh.length, other.hash(), 0, other.hashLength())) return true;
		return false;
	}

	public boolean wasCalculated(){return calculate;}

	public boolean isActive(){return active;}

	public byte[] hash(){
		if(active) return EMPTYBYTEARRAY;
		return Arrays.copyOf(hsh,hsh.length);
	}

	public int hashLength(){
		return hsh.length;
	}

	public String toString() {
		if(active) return alg+"=null";
		return alg+"="+HexadecimalUtils.convertToHex(hsh);
	}

}

