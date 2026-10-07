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
package jrain.hash.hashInstance.hashes.immutable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import jrain.hash.hashInstance.HashMessageDigest;
import jrain.hash.hashInstance.HashXOR;
import jrain.hash.hashInstance.HashZip;
import jrain.hash.hashInstance.immutable.HashInstance;

import java.util.Set;
import java.util.UUID;

import jrain.identifiable.immutable.Identifiable;
import jrain.array.utils.ByteArrayUtils;

/**
 * @author poltergeist0
 *
 * This class provides facilities to calculate and store sets of hashes. 
 * Calculation is simultaneous for all defined hashes.
 * 
 * Requested unknown hash algorithms are ignored.
 */
public class Hashes extends Identifiable implements jrain.hash.hashInstance.hashes.Hashes{
	
	/*
	 * Indicates if the hash was calculated or loaded
	 */
	private final boolean calculate;
	
	/*
	 * If the hash is calculated, this distinguishes between still being 
	 * calculated (true) or calculation finished (false).
	 */
	private boolean active;
	
	/**
	 * Hashes map
	 */
	private HashMap<String,HashInstance> hsh;
	
	/**
	 * Initialize the hashes map with all known algorithms.
	 * 
	 * @return the hashes map
	 */
	private static HashMap<String,HashInstance> initAllHashes(){
		HashMap<String,HashInstance> h=new HashMap<>();
		Iterator<String> it = HashXOR.algorithms().iterator();
		while(it.hasNext()) {
			String a = it.next();
			h.put(a,new jrain.hash.hashInstance.immutable.HashXOR(a));
		}
		it = HashZip.algorithms().iterator();
		while(it.hasNext()) {
			String a = it.next();
			h.put(a,new jrain.hash.hashInstance.immutable.HashZip(a));
		}
		it = HashMessageDigest.algorithms().iterator();
		while(it.hasNext()) {
			String a = it.next();
			h.put(a,new jrain.hash.hashInstance.immutable.HashMessageDigest(a));
		}
		return h;
	}
	
	/**
	 * Initialize the hashes map with given algorithms.
	 * 
	 * @param hashes are the algorithms
	 * @return the hashes map
	 */
	private static HashMap<String,HashInstance> initHashes(Set<String> hashes){
		if(hashes==null) return initAllHashes();
		HashMap<String,HashInstance> h=new HashMap<>();
		Iterator<String> it = hashes.iterator();
		while(it.hasNext()) {
			String a = it.next();
			if(HashXOR.hasAlgorithm(a)) {
				h.put(a,new jrain.hash.hashInstance.immutable.HashXOR(a));
			}
			else if(HashZip.hasAlgorithm(a)) {
				h.put(a,new jrain.hash.hashInstance.immutable.HashZip(a));
			}
			else if(HashMessageDigest.hasAlgorithm(a)) {
				h.put(a,new jrain.hash.hashInstance.immutable.HashMessageDigest(a));
			}
		}
		return h;
	}
	
	/**
	 * Constructor to calculate all hashes
	 */
	public Hashes(){
		super((UUID)null);
		calculate=true;
		hsh=initAllHashes();
		active=true;
	}
	
	/**
	 * Constructor for specific hashes to be calculated
	 * 
	 * @param h is a {@link Set} with the names of the hashes
	 */
	public Hashes(Set<String> h){
		super((UUID)null);
		calculate=true;
		hsh=initHashes(jrain.hash.hashInstance.hashes.Hashes.validateHashes(h));
		active=false;
	}
	
	/**
	 * Constructor to load hashes
	 * 
	 * @param hashes is a map with pairs of hash name and hash data
	 */
	public Hashes(HashMap<String, byte[]> hashes){
		super((UUID)null);
		calculate=false;
		active=true;
		hsh=new HashMap<>();
		Iterator<Entry<String, byte[]>> it = hashes.entrySet().iterator();
		while(it.hasNext()) {
			Entry<String, byte[]> e = it.next();
			String k = e.getKey();
			byte[] v = e.getValue();
			hsh.put(k,new HashInstance(k,v));
		}
		active=false;
	}
	
	/**
	 * Updates the digest using the specified array of bytes, starting at the 
	 * specified offset.
	 * 
	 * Ignores calls if the hashes were loaded.
	 * 
	 * @param b is the array of bytes
	 * @param offset is the offset to start from in the array of bytes
	 * @param len is the number of bytes to use, starting at offset
	 */
	public void update(byte[] b, int offset, int len){
		if(!calculate) return;
		Iterator<Entry<String, HashInstance>> it = hsh.entrySet().iterator();
		while(it.hasNext()) {
			it.next().getValue().update(b,offset,len);
		}
	}
	
	/**
	 * Completes the hash computation by performing final operations such as 
	 * padding. The digest is reset after this call is made.
	 * 
	 * Ignores calls if the hashes were loaded.
	 * 
	 * @return the array of bytes for the resulting hash value.
	 */
	public void digest(){
		if(!calculate) return;
		Iterator<Entry<String, HashInstance>> it = hsh.entrySet().iterator();
		while(it.hasNext()) {
			it.next().getValue().digest();
		}
		active=false;
	}
	
	/**
	 * @return true if at least one hash exists, false otherwise
	 */
	public boolean hasHashes(){
		if(hsh.size()>0) return true;
		return false;
	}
	
	/**
	 * @return a set with the names of the hashes present in this object
	 */
	public Set<String> hashes(){return hsh.keySet();}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (active ? 1231 : 1237);
		Iterator<Entry<String, HashInstance>> it = hsh.entrySet().iterator();
		while(it.hasNext()) {
			result = prime * result + it.next().getValue().hashCode();
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(active) return false;//if it is currently calculating then can not compare hashes
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Hashes)) {
			return false;
		}
		Hashes other = (Hashes) obj;
		if (active != other.active) {
			return false;
		}
		if(hsh.size()!=other.hsh.size()) return false;
		Iterator<Entry<String, HashInstance>> it = hsh.entrySet().iterator();
		while(it.hasNext()) {
			Entry<String, HashInstance> a = it.next();
			String k = a.getKey();
			HashInstance v = a.getValue();
			if(!ByteArrayUtils.equalsNull(v.hash(), 0, v.hashLength(), other.hash(k), 0, other.hashLength(k))) return false;
		}
		return true;
	}

	/**
	 * Compare all common hashes for equality.
	 * Returns false if there are no hashes in common.
	 * Returns true if there is at least one hash in common and they are identical.
	 * Returns false if there is at least one hash in common that is not identical.
	 * 
	 * @param obj
	 * @return
	 */
	public boolean compare(Hashes obj) {
		if(active) return false;//if it is currently calculating then can not compare hashes
		if(obj == null) return false; //can not compare
		if(obj.active) return false;//if it is currently calculating then can not compare hashes
		boolean res=false;//initialize to the value of no hashes in common
		Iterator<Entry<String, HashInstance>> it = hsh.entrySet().iterator();
		while(it.hasNext()) {
			Entry<String, HashInstance> a = it.next();
			String k = a.getKey();
			HashInstance v = a.getValue();
			if(obj.hsh.containsKey(k)) {//this is a common hash 
				HashInstance u = obj.hsh.get(k);
				if(!ByteArrayUtils.equalsNull(v.hash(), 0, v.hashLength(), u.hash(), 0, u.hashLength())) return false;
				else res=true;//if no more common hashes or no different common hashes, this is the return value
			}
		}
		return res;
	}

	/**
	 * @return true if the hashes were calculated or false if they were loaded
	 */
	public boolean wasCalculated(){return calculate;}
	
	/**
	 * @return true if hashes are being calculated and the digest method has not called
	 */
	public boolean isActive(){return active;}
	
	/**
	 * Get the hash corresponding to the given hashing algorithm name.
	 * If the algorithm is not present (either by being calculated or loaded), an
	 * empty hash is returned.
	 * 
	 * @param h is the hash name
	 * @return the hash data
	 */
	public byte[] hash(String h){
		if(!hsh.containsKey(h)) return HashInstance.EMPTYBYTEARRAY;
		HashInstance a = hsh.get(h);
		return Arrays.copyOf(a.hash(), a.hash().length);
	}

	/**
	 * Get the length of the hash corresponding to the given hashing algorithm name.
	 * If the algorithm is not present (either by being calculated or loaded), a
	 * zero is returned.
	 * 
	 * @param h is the hash name
	 * @return the hash data length
	 */
	public int hashLength(String h){
		if(!hsh.containsKey(h)) return 0;
		HashInstance a = hsh.get(h);
		return a.hashLength();
	}

	public String toString() {
		if(active) return "=null";
		StringBuilder s=new StringBuilder();
		boolean first=true;
		s.append("{ ");
		Iterator<Entry<String, HashInstance>> it = hsh.entrySet().iterator();
		while(it.hasNext()) {
			Entry<String, HashInstance> e = it.next();
			if(first) first=false;
			else s.append(" ; ");
			s.append(e.getValue().toString());
		}
		s.append(" }");
		return s.toString();
	}

}

