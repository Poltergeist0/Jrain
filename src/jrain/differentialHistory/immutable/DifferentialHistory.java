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
package jrain.differentialHistory.immutable;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

import jrain.indexedSequence.mutable.IndexedSequence;
import jrain.indexedSet.mutable.IndexedSet;
import jrain.entry.mutable.PairOfArrayList;

/**
 * @author poltergeist0
 * 
 * Class used to work with lossy historical/log data.
 * It keeps track of all active markers.
 * A marker is a reference to an object.
 * If no markers are requested, it only keeps track of the first and the last markers.
 * This class should be used on the producer side while {@link HistoryMarker} should
 * be used in the consumer side.
 * Threading is supported.
 * 
 * Example:
 * {@code
 * DifferentialHistory<Integer> dh;
 * ...
 * dh.set(1);
 * ...
 * dh.set(2);
 * ...
 * dh.set(3);//at this point dh contains 1 and 3
 * ...
 * HistoryMarker m=dh.marker();//references 3
 * m.changed();//returns false
 * dh.set(4);
 * m.changed();//returns true
 * e=m.getAndUpdate();//updates reference to 4 and returns 3
 * }
 * @param <T> is the data type
 */
public class DifferentialHistory<T> {

	/**
	 * @author poltergeist0
	 * 
	 * List of values to be used internally
	 */
	public class InnerList extends IndexedSequence<T, Integer>{
		
		private long totalChanges=0;

		public InnerList(){
			super();
		}

		private InnerList(InnerList lst){//never used?!
			super(lst,true);
		}

		@Override
		public ArrayList<Entry<T, Integer>> add(T k,Integer v,boolean overwrite){
			totalChanges++;
			return super.add(k, v, overwrite);
		}
		
		@Override
		public <U extends Entry<T, Integer>> ArrayList<Entry<T,Integer>> add(U e,boolean overwrite){
			totalChanges++;
			return super.add(e, overwrite);
		}
		
		@Override
		public <U extends IndexedSet<T, Integer>> 
		ArrayList<Entry<T, Integer>> add(U t,boolean overwrite){
			totalChanges++;
			return super.add(t, overwrite);
		}

		@Override
		public <U extends Entry<T, Integer>, V extends Collection<U> > 
		ArrayList<Entry<T, Integer>> add(V c,boolean overwrite){
			totalChanges++;
			return super.add(c, overwrite);
		}

		@Override
		public <TYPE_TABLE extends IndexedSet<T, Integer>, TYPE_PREDICATE extends Predicate<Entry<T, Integer>>> 
		ArrayList<Entry<T, Integer> > add(
				TYPE_TABLE t,
				TYPE_PREDICATE pre,
				boolean overwrite
				)
		{
			totalChanges++;
			return super.add(t, pre, overwrite);
		}

		@Override
		public <TYPE_TABLE extends IndexedSet<T, Integer>, TYPE_PREDICATE extends Predicate<Entry<T, Integer>>> 
		ArrayList<Entry<T, Integer>> add(
				TYPE_TABLE t,
				TYPE_PREDICATE pre,
				boolean overwrite,boolean deepCopy
				)
		{
			totalChanges++;
			return super.add(t, pre, overwrite, deepCopy);
		}
		
		public long eventCount() {return totalChanges;}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			Iterator<T> it = keys().iterator();
			while(it.hasNext()) {
				T a = it.next();
				result = prime * result + ((a == null) ? 0 : a.hashCode());
			}
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
			if (obj == null || obj.getClass()!= this.getClass()) {
				return false;
			}
			@SuppressWarnings("unchecked")
			InnerList other = (InnerList) obj;//use unbounded to avoid warning about cast type safety
			if(size()!=other.size()) return false;
			if(commonKeys(this, other).size()!=size()) return false;
			return true;
		}

		@Override
		public String separatorValue() {
			return ":";
		}

		@Override
		public String separatorCell() {
			return ";";
		}

		/**
		 * @return an iterator for all the entries in the header.
		 */
		@Override
		public InnerListIterator iterator() {
			return new InnerListIterator();
		}

		/**
		 * @author poltergeist0
		 *
		 * {@link Iterator} for {@link TaggedTableCoreHeader}
		 */
		public class InnerListIterator extends IndexedSequenceIterator { 

			/**
			 * Constructor
			 */
			protected InnerListIterator() { 
				super();
			} 
		}

		@Override
		public IndexedSequence<T, Integer> deepCopy() {//never used?!
			return new InnerList(this);
		}

		@Override
		protected Integer typeDataGetValue(Object v) {
			return (Integer) v;
		}

	}
	
	/**
	 * Lock used when threads are involved
	 */
	private volatile Object lockInnerList;
	
	/**
	 * @author poltergeist0
	 * History marker stores a reference to an object.
	 * Such object should be immutable.
	 * It allows for the historic data consumer to keep track of changes in history.
	 * Implements {@link AutoCloseable} so that resources can be freed.
	 */
	public class HistoryMarker implements AutoCloseable{
		private T ref;//reference to object to track
		private Boolean lastRead;//when ref==last indicates if it was already read
		
		/*
		 * Note: always use equals instead of == because an update (using his.add) 
		 * can change the address of the entry but not the data
		 */
		
		/**
		 * Construct a new marker and update the count of markers using a reference
		 * @param original is the reference to use
		 */
		protected HistoryMarker(HistoryMarker original) {
			synchronized(lockInnerList) {
				Entry<T, Integer> a = his.get(original.ref);
				his.add(a.getKey(), a.getValue()+1, true);
				ref=a.getKey();
			}
			lastRead=false;
		}
		
		/**
		 * Construct a new marker for the last object in {@link DifferentialHistory}
		 */
		protected HistoryMarker() {
			synchronized(lockInnerList) {
				Entry<T, Integer> a = his.last();
				his.add(a.getKey(), a.getValue()+1, true);
				ref=a.getKey();
			}
			lastRead=false;
		}
		
		/**
		 * Check if a more recent object exists.
		 * 
		 * @return true if a more recent object exists
		 */
		public boolean changed() {
			T a=null;
			synchronized(lockInnerList) {
				a=his.last().getKey();
			}
			if(ref.equals(a)) {
				if(lastRead) return false;
				else return true;
			}
			else {
				lastRead=false;
				return true;
			}
		}
		
		/**
		 * Get the amount of total history events, even those before the creation of the current marker.
		 * 
		 * @return a long with the number of events
		 */
		public Long eventCount() {
			Long a=null;
			synchronized(lockInnerList) {
				a=his.eventCount();
			}
			return a;
		}
		
		/**
		 * @return the reference stored in this marker
		 */
		public T reference() {return ref;}
		
		/**
		 * Update a reference and, if this marker is the last one to reference it,
		 * remove the reference from history.
		 * 
		 * @param ref is the reference to update
		 * @param last is the last reference in history (for comparison only)
		 */
		private void update(T ref, T last) {
			synchronized(lockInnerList) {
				Entry<T, Integer> a = his.get(ref);
				if(a.getValue()<=1 && his.size()>2 && !ref.equals(last)) his.remove(a.getKey());
				else his.add(a.getKey(), a.getValue()-1, true);
			}
		}
		
		/**
		 * Update the marker to reference the most recent history entry and return
		 * the previously held reference.
		 * 
		 * @return the previous reference associated with this marker
		 */
		public T getAndUpdate() {
			Entry<T, Integer> last=null;
			T tmp=null;
			synchronized(lockInnerList) {
				last = his.last();
				if(last.getKey().equals(ref)) {
					lastRead=true;
					return ref;
				}
				else {
					lastRead=false;
					his.add(last.getKey(), last.getValue()+1, true);
				}
				tmp=ref;
				update(ref,last.getKey());
			}
			ref=last.getKey();
			return tmp;
		}
		
		/**
		 * @return a copy of this marker
		 */
		public HistoryMarker copy() {
			return new HistoryMarker(this);
		}
		
		/**
		 * Close the current marker so that its resources can be freed
		 */
		public void close() {
			synchronized(lockInnerList) {
				update(ref,his.last().getKey());
			}
		}
	}
	
	/**
	 * @author poltergeist0
	 * 
	 * Set of {@link HistoryMarker}s.
	 *
	 * @param <TYPE_KEY> is the data type of the key to be used to identify entries
	 * @param <TYPE_DATA> is the data type of the referenced objects
	 */
	public static class HistoryMarkerSet<TYPE_KEY, TYPE_DATA>{
		
		ConcurrentHashMap<TYPE_KEY, DifferentialHistory<TYPE_DATA>.HistoryMarker> lst;//the set
		
		/**
		 * Constructor for a new set
		 */
		public HistoryMarkerSet() {
			lst=new ConcurrentHashMap<>();
		}
		
		/**
		 * Add a new marker to the set.
		 * If a marker already exists the set is not modified.
		 * 
		 * @param key is the key that identifies the marker
		 * @param data is the marker to add
		 * @return true if the adding succeeded. False if the entry already exists or there was another problem adding.
		 */
		public boolean put(TYPE_KEY key, DifferentialHistory<TYPE_DATA>.HistoryMarker data) {
			if(lst.containsKey(key)) return false;
			lst.put(key, data);
			return true;
		}
		
		/**
		 * Remove a marker.
		 * Does nothing if the marker does not exist on the list.
		 * 
		 * @param key is the key that identifies the marker
		 * @return the marker corresponding to the given key.
		 */
		public DifferentialHistory<TYPE_DATA>.HistoryMarker remove(TYPE_KEY key) {
			return lst.remove(key);
		}
		
		/**
		 * Check if the marker with the given key has changed.
		 * See {@link HistoryMarker#changed()}.
		 * 
		 * @param key is the key that identifies the marker
		 * @return true if the marker has changed
		 */
		public boolean changed(TYPE_KEY key) {
			if(lst.containsKey(key)) {
				return lst.get(key).changed();
			}
			return false;
		}
		
		/**
		 * Update the marker with the given key to reference the most recent 
		 * history entry and return the previously held reference.
		 * See {@link HistoryMarker#getAndUpdate()}.
		 * 
		 * @param key is the key that identifies the marker
		 * @return the previous reference associated with this marker
		 */
		public TYPE_DATA getAndUpdate(TYPE_KEY key) {
			if(lst.containsKey(key)) {
				return lst.get(key).getAndUpdate();
			}
			return null;
		}
		
		/**
		 * Get a copy of the marker associated to the given key.
		 * 
		 * @param key is the key that identifies the marker
		 * @return a copy of the reference associated with the marker
		 */
		public DifferentialHistory<TYPE_DATA>.HistoryMarker copy(TYPE_KEY key) {
			return lst.get(key).copy();
		}
	}

	InnerList his;//history list. second parameter is a counter of viewers targeting that position

	/**
	 * Constructor
	 */
	public DifferentialHistory() {
		his=new InnerList();
		lockInnerList=new Object();
	}

	/**
	 * Add an entry to history.
	 * 
	 * @param h is the object to be added
	 * @return true (currently always succeeds)
	 */
	public boolean set(final T h) {
		synchronized(lockInnerList) {
//			System.out.println("\nDF: "+h.toString()+"->"+his.toString());
			while(his.size()>=2 && his.last().getValue()<=0) {
				//last position not being used so remove it
				PairOfArrayList<Integer, Entry<T, Integer>,ArrayList<Integer>,ArrayList<Entry<T, Integer>>> e = his.removeLast();
				if(e.getKey().size()>0) System.out.println("ERROR on removeLast: "+e.getKey());
			}
			if(his.size()>=1 && his.first().getValue()<=0) {//also check the first position for removal
				PairOfArrayList<Integer, Entry<T, Integer>,ArrayList<Integer>,ArrayList<Entry<T, Integer>>> e = his.removeFirst();
				if(e.getKey().size()>0) System.out.println("ERROR on removeFirst: "+e.getKey());
			}
			his.add(h, 0, false);
//			System.out.println("DFs: "+his.toString());
		}
		return true;
	}
	
	/**
	 * Get the object that was most recently added.
	 * This method is provided to allow this class to be extended. Use
	 * {@link HistoryMarker#marker()} for processing history.
	 * 
	 * @return the object that was most recently added
	 */
	public T last() {
		synchronized(lockInnerList) {
			return his.last().getKey();
		}
	}
	
	/**
	 * Get a marker referencing the latest object added to history.
	 * 
	 * @return a history marker
	 */
	public HistoryMarker marker() {return new HistoryMarker();}
	
	public String toString() {
		synchronized(lockInnerList) {
			return his.toString();
		}
	}
}
