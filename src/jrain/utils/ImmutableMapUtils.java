package jrain.utils;

import java.util.Map.Entry;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.UnmodifiableIterator;

public class ImmutableMapUtils{

	public static <E,F> ImmutableMap<E,F> add(final ImmutableMap<E,F> original, final E key, final F data){
		ImmutableMap.Builder<E,F> b=new ImmutableMap.Builder<>();
		b.putAll(original);
		b.put(key, data);
		return b.build();
	}
	
	public static <E,F> ImmutableMap<E,F> add(final ImmutableMap<E,F> original, final ImmutableMap<E,F> data){
		ImmutableMap.Builder<E,F> b=new ImmutableMap.Builder<>();
		b.putAll(original);
		b.putAll(data);
		return b.build();
	}
	
	public static <E,F> ImmutableMap<E,F> remove(final ImmutableMap<E,F> original, final E data){
		ImmutableMap.Builder<E,F> b=new ImmutableMap.Builder<>();
		UnmodifiableIterator<E> it=original.keySet().iterator();
		E i=null;
		while(it.hasNext()){
			i=it.next();
			if(!i.equals(data))b.put(i,original.get(i));
		}
		return b.build();
	}
	
	public static <E,F> ImmutableMap<E,F> remove(final ImmutableMap<E,F> original, final ImmutableList<E> data){
		ImmutableMap.Builder<E,F> b=new ImmutableMap.Builder<>();
		UnmodifiableIterator<E> it=original.keySet().iterator();
		E i=null;
		while(it.hasNext()){
			i=it.next();
			if(!data.contains(i))b.put(i,original.get(i));
		}
		return b.build();
	}
	
	/**
	 * removes the old data and inserts the new. Same functionality than a 
	 * remove followed by an insert.
	 * 
	 * @param original
	 * @param dataOld
	 * @param dataNew
	 * @return
	 */
	public static <E,F> ImmutableMap<E,F> replace(final ImmutableMap<E,F> original, final ImmutableList<E> dataOld, final ImmutableMap<E,F> dataNew){
		ImmutableMap.Builder<E,F> b=new ImmutableMap.Builder<>();
		UnmodifiableIterator<E> it=original.keySet().iterator();
		E i=null;
		while(it.hasNext()){
			i=it.next();
			if(!dataOld.contains(i))
				b.put(i,original.get(i));
		}
		b.putAll(dataNew);
		return b.build();
	}
	
	public static <E,F> ImmutableMap<E,F> replace(final ImmutableMap<E,F> original, final E keyOld, final E keyNew, final F dataNew){
		ImmutableMap.Builder<E,F> b=new ImmutableMap.Builder<>();
		UnmodifiableIterator<E> it=original.keySet().iterator();
		E i=null;
		while(it.hasNext()){
			i=it.next();
			if(!i.equals(keyOld))b.put(i,original.get(i));
		}
		b.put(keyNew, dataNew);
		return b.build();
	}
	
	/**
	 * get a sub map of a map with a given list of keys
	 * 
	 * @param original
	 * @param data is the list of keys for the new map
	 * @return
	 */
	public static <E,F> ImmutableMap<E,F> submap(final ImmutableMap<E,F> original, final ImmutableList<E> data){
		ImmutableMap.Builder<E,F> b=new ImmutableMap.Builder<>();
		UnmodifiableIterator<E> it=original.keySet().iterator();
		E i=null;
		while(it.hasNext()){
			i=it.next();
			if(data.contains(i))b.put(i,original.get(i));
		}
		return b.build();
	}
	
	public static <E,F> boolean isSubMap(final ImmutableMap<E,F> map,final ImmutableMap<E,F> submap){
		if(map.size()>submap.size()){
			if(map.entrySet().containsAll(submap.entrySet())) return true;
		}
		return false;
	}
	
	/**
	 * return a map that is the intersection entries of two maps.
	 * 
	 * @param map1
	 * @param map2
	 * @return
	 */
	public static <E,F> ImmutableMap<E,F> intersect(final ImmutableMap<E,F> map1,final ImmutableMap<E,F> map2){
		ImmutableSet<Entry<E, F>> e = map1.entrySet();
		UnmodifiableIterator<Entry<E,F>> it = map2.entrySet().iterator();
		Entry<E,F> n=null;
		ImmutableMap.Builder<E, F> m=ImmutableMap.builder();
		while(it.hasNext()){
			n=it.next();
			if(e.contains(n))m.put(n);
		}
		return m.build();
	}
	
	/**
	 * @param map1
	 * @param map2
	 * @return -3 for failure, 0 if identical,-1 if map2 submap of map1, -2 if map1 submap of map2, or number>0 indicating the number of identical entries
	 */
	public static <E,F> int compare(final ImmutableMap<E,F> map1,final ImmutableMap<E,F> map2){
		ImmutableMap<E, F> in = intersect(map1, map2);
		if(in.size()==map1.size()){
			if(in.size()==map2.size()) return 0;
			else{ //map2>in ; can not be bigger since intersections never return bigger than the smallest
				return -2;
			}
		}
		else if(in.size()<map1.size()){
			if(in.size()==map2.size()) return -1;
//			else{// if(in.size()<map2.size()){
//				//neither map1 nor map2 are not submaps
//				return in.size();
//			}
		}
		return in.size();
	}
	
	public static <E,F> boolean matches(final ImmutableMap<E,F> map1,final ImmutableMap<E,F> map2){
		if(compare(map1, map2)==0)return true;
		return false;
	}
}
