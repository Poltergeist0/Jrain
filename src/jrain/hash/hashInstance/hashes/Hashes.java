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
package jrain.hash.hashInstance.hashes;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import jrain.hash.hashInstance.HashMessageDigest;
import jrain.hash.hashInstance.HashXOR;
import jrain.hash.hashInstance.HashZip;
import jrain.identifiable.Identifiable;

/**
 * @author poltergeist0
 *
 * Interface that must be implemented by classes that represent a set of hash instances.
 * 
 * Provides the following three static methods to aid implementation:
 * 
 * 1) A method to return a set of names of the algorithms that the class implements
 * {@code public static Set<String> algorithms()}
 * 
 * 2) A method to evaluate if a given set of algorithms are implemented by the class
 * by returning only the algorithms that are implemented.
 * {@code public static Set<String> validateHashes(Set<String> hashes)}
 * 
 * 3) A method to evaluate if a given algorithm name is implemented by the class
 * {@code public static boolean hasAlgorithm(String a)}
 * 
 */
public interface Hashes extends Identifiable<UUID>{
	
	/**
	 * @return a set with the names of the hash algorithms implemented
	 */
	public static Set<String> algorithms(){
		HashSet<String> a=new HashSet<>();
		Iterator<String> it = HashXOR.algorithms().iterator();
		while(it.hasNext()) {
			String s = it.next();
			a.add(s);
		}
		it = HashZip.algorithms().iterator();
		while(it.hasNext()) {
			String s = it.next();
			a.add(s);
		}
		it = HashMessageDigest.algorithms().iterator();
		while(it.hasNext()) {
			String s = it.next();
			a.add(s);
		}
		return a;
	}

	/**
	 * Evaluate if a given set of algorithms are implemented by returning only 
	 * the algorithms that are implemented.
	 * 
	 * @param hashes is the set of intended algorithms
	 * @return the set of the given algorithms that are implemented
	 */
	public static Set<String> validateHashes(Set<String> hashes){
		if(hashes==null) return algorithms();
		HashSet<String> a=new HashSet<>();
		Iterator<String> it = hashes.iterator();
		while(it.hasNext()) {
			String s = it.next();
			if(algorithms().contains(s))a.add(s);
		}
		if(a.size()==hashes.size()) return hashes;
		return a;
	}

	/**
	 * @param a is the name of a hash algorithm
	 * @return true if the algorithm is implemented
	 */
	public static boolean hasAlgorithm(String a){return algorithms().contains(a);}

}
