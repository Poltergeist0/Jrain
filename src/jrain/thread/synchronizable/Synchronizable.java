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
package jrain.thread.synchronizable;

/**
 * @author poltergeist0
 *
 * Simple class that automatically synchronizes (locks) access to the stored 
 * data to avoid contention.
 * 
 * It should be extended to add the methods implemented by the data type stored
 * so that the get method can be hidden, since the data returned by it is no 
 * longer protected by the synchronization lock.
 * 
 * @param <T> is the data type of the stored data
 */
public class Synchronizable<T> {

	private T dt;//data
	
	/**
	 * Constructor for null value
	 */
	public Synchronizable() {
		dt=null;
	}

	/**
	 * Constructor for given value
	 * 
	 * @param data is the value to store
	 */
	public Synchronizable(T data) {
		dt=data;
	}

	/**
	 * Copy constructor
	 * 
	 * @param data is the original
	 */
	public Synchronizable(Synchronizable<T> data) {
		dt=data.get();
	}

	/**
	 * @return the stored value
	 */
	protected synchronized T get() {
		return dt;
	}

	/**
	 * Set the stored value
	 * @param data is the new value to store
	 * @return this so that the method can be chained
	 */
	protected synchronized Synchronizable<T> set(T data) {
		dt=data;
		return this;
	}
	
	public synchronized String toString() {
		if(dt==null) return "null";
		return dt.toString();
	}
	
	@Override
	public synchronized int hashCode() {
		if(dt==null) return 0;
		return dt.hashCode();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public synchronized boolean equals(Object obj) {
		if (obj == null) return false;
		if (this == obj) return true;
		if (!(obj instanceof Synchronizable)) {//obj is not Synchronizable
			//check to see if it is type T
			return dt.equals(obj);
		}
		return dt.equals(((Synchronizable<T>)obj).dt);
	}

}
