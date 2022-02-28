package jrain.immutableMultiMap.utils;

import java.util.Map.Entry;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.UnmodifiableIterator;

/**
 * @author poltergeist0
 *
 * Provides several static methods to work with {@code ImmutableMultimap}
 */
public class ImmutableMultiMapUtils{

	/**
	 * Add a value to an existing key.
	 * 
	 * @param <E> is the data type of the key
	 * @param <F> is the data type of the value
	 * @param original is the original map
	 * @param key is the key of the entry to add
	 * @param data is the value of the entry to add
	 * @return a new {@link ImmutableMultimap} with the entry added
	 */
	public static <E,F> ImmutableMultimap<E,F> addValue(final ImmutableMultimap<E,F> original, final E key,final F data){
		ImmutableMultimap.Builder<E,F> b=new ImmutableMultimap.Builder<>();
		b.putAll(original);
		b.put(key,data);
		return b.build();
	}
	
	/**
	 * Add multiple values to an existing key.
	 * 
	 * @param <E> is the data type of the key
	 * @param <F> is the data type of the value
	 * @param original is the original map
	 * @param key is the key of the entry to add
	 * @param data is the list of values to add
	 * @return a new {@link ImmutableMultimap} with the entries added
	 */
	public static <E,F> ImmutableMultimap<E,F> addValues(final ImmutableMultimap<E,F> original, final E key,final ImmutableList<F> data){
		ImmutableMultimap.Builder<E,F> b=new ImmutableMultimap.Builder<>();
		b.putAll(original);
		b.putAll(key,data);
		return b.build();
	}
	
	/**
	 * Add all values of a {@link ImmutableMultimap} to an existing {@link ImmutableMultimap}.
	 * 
	 * @param <E> is the data type of the key
	 * @param <F> is the data type of the value
	 * @param original is the original map
	 * @param data is the map with the values to add
	 * @return a new {@link ImmutableMultimap} with the entries added
	 */
	public static <E,F> ImmutableMultimap<E,F> addMultimap(final ImmutableMultimap<E,F> original, final ImmutableMultimap<E,F> data){
		ImmutableMultimap.Builder<E,F> b=new ImmutableMultimap.Builder<>();
		b.putAll(original);
		b.putAll(data);
		return b.build();
	}
	
	/**
	 * Remove given keyValue from both the keys and the values
	 * 
	 * @param <E> is the data type of the key
	 * @param <F> is the data type of the value
	 * @param original is the original map
	 * @param keyValue is the value to remove
	 * @return a new {@link ImmutableMultimap} with the entries removed
	 */
	public static <E,F> ImmutableMultimap<E,E> remove(final ImmutableMultimap<E,E> original, final E keyValue){
		ImmutableMultimap.Builder<E,E> b=new ImmutableMultimap.Builder<>();
		UnmodifiableIterator<Entry<E,E>> it=original.entries().iterator();
		Entry<E, E> e=null;
		E i=null;
		E o=null;
		while(it.hasNext()){
			e=it.next();
			i=e.getKey();
			o=e.getValue();
			if(!i.equals(keyValue) && !o.equals(keyValue))b.put(e);
		}
		return b.build();
	}
	
	/**
	 * Remove all given keyValue from both the keys and the values
	 * 
	 * @param <E> is the data type of the key
	 * @param <F> is the data type of the value
	 * @param original is the original map
	 * @param keyValue is a list with the values to remove
	 * @return a new {@link ImmutableMultimap} with the entries removed
	 */
	public static <E,F> ImmutableMultimap<E,E> remove(final ImmutableMultimap<E,E> original, final ImmutableList<E> keyValue){
		ImmutableMultimap.Builder<E,E> b=new ImmutableMultimap.Builder<>();
		UnmodifiableIterator<Entry<E,E>> it=original.entries().iterator();
		Entry<E, E> e=null;
		E i=null;
		E o=null;
		while(it.hasNext()){
			e=it.next();
			i=e.getKey();
			o=e.getValue();
			if(!keyValue.contains(i) && !keyValue.contains(o))b.put(e);
		}
		return b.build();
	}
	
	/**
	 * Remove all entries with the given key
	 * 
	 * @param <E> is the data type of the key
	 * @param <F> is the data type of the value
	 * @param original is the original map
	 * @param key is a key to remove
	 * @return a new {@link ImmutableMultimap} with the entries removed
	 */
	public static <E,F> ImmutableMultimap<E,F> removeKey(final ImmutableMultimap<E,F> original, final E key){
		ImmutableMultimap.Builder<E,F> b=new ImmutableMultimap.Builder<>();
		UnmodifiableIterator<E> it=original.keySet().iterator();
		E i=null;
		while(it.hasNext()){
			i=it.next();
			if(!i.equals(key))b.putAll(i,original.get(i));
		}
		return b.build();
	}
	
	/**
	 * Remove the entry with the given key and value
	 * 
	 * @param <E> is the data type of the key
	 * @param <F> is the data type of the value
	 * @param original is the original map
	 * @param key is the key of the entry to remove
	 * @param data is the value to remove
	 * @return a new {@link ImmutableMultimap} with the entries removed
	 */
	public static <E,F> ImmutableMultimap<E,F> removeValue(final ImmutableMultimap<E,F> original, final E key, final F data){
		ImmutableMultimap.Builder<E,F> b=new ImmutableMultimap.Builder<>();
		UnmodifiableIterator<Entry<E, F>> it=original.entries().iterator();
		Entry<E, F> e=null;
		E k=null;
		F d=null;
		while(it.hasNext()){
			e=it.next();
			k=e.getKey();
			d=e.getValue();
			if(!(k.equals(key) && d.equals(data)))b.put(k,d);
		}
		return b.build();
	}
	
	/**
	 * Remove all entries with the given value
	 * 
	 * @param <E> is the data type of the key
	 * @param <F> is the data type of the value
	 * @param original is the original map
	 * @param data is the value to remove
	 * @return a new {@link ImmutableMultimap} with the entries removed
	 */
	public static <E,F> ImmutableMultimap<E,F> removeValue(final ImmutableMultimap<E,F> original, final F data){
		ImmutableMultimap.Builder<E,F> b=new ImmutableMultimap.Builder<>();
		UnmodifiableIterator<Entry<E, F>> it=original.entries().iterator();
		Entry<E, F> e=null;
		E k=null;
		F d=null;
		while(it.hasNext()){
			e=it.next();
			k=e.getKey();
			d=e.getValue();
			if(!d.equals(data))b.put(k,d);
		}
		return b.build();
	}
	
	/**
	 * Remove all entries with the given keys
	 * 
	 * @param <E> is the data type of the key
	 * @param <F> is the data type of the value
	 * @param original is the original map
	 * @param keys is a list of keys to remove
	 * @return a new {@link ImmutableMultimap} with the entries removed
	 */
	public static <E,F> ImmutableMultimap<E,F> removeKeys(final ImmutableMultimap<E,F> original, final ImmutableList<E> keys){
		ImmutableMultimap.Builder<E,F> b=new ImmutableMultimap.Builder<>();
		UnmodifiableIterator<E> it=original.keySet().iterator();
		E i=null;
		while(it.hasNext()){
			i=it.next();
			if(!keys.contains(i))b.putAll(i,original.get(i));
		}
		return b.build();
	}
	
	/**
	 * Get a sub map of a map with a given key.
	 * 
	 * @param <E> is the data type of the key
	 * @param <F> is the data type of the value
	 * @param original is the original map
	 * @param data is the key for the new map
	 * @return a new {@link ImmutableMap} with the given keys
	 */
	public static <E,F> ImmutableMultimap<E,F> submap(final ImmutableMultimap<E,F> original, final E data){
		ImmutableMultimap.Builder<E,F> b=new ImmutableMultimap.Builder<>();
		UnmodifiableIterator<E> it=original.keySet().iterator();
		E i=null;
		while(it.hasNext()){
			i=it.next();
			if(data.equals(i))b.putAll(i,original.get(i));
		}
		return b.build();
	}
	
	/**
	 * Get a sub map of a map with a given list of keys.
	 * 
	 * @param <E> is the data type of the key
	 * @param <F> is the data type of the value
	 * @param original is the original map
	 * @param data is the list of keys for the new map
	 * @return a new {@link ImmutableMap} with the given keys
	 */
	public static <E,F> ImmutableMultimap<E,F> submap(final ImmutableMultimap<E,F> original, final ImmutableList<E> data){
		ImmutableMultimap.Builder<E,F> b=new ImmutableMultimap.Builder<>();
		UnmodifiableIterator<E> it=original.keySet().iterator();
		E i=null;
		while(it.hasNext()){
			i=it.next();
			if(data.contains(i))b.putAll(i,original.get(i));
		}
		return b.build();
	}
	
	/**
	 * Replace the key in the entries with the given new key without changing the values.
	 * If the key does not exist nothing happens.
	 * If an identical key already exists in the map, the entries are grouped.
	 * 
	 * @param <E> is the data type of the key
	 * @param <F> is the data type of the value
	 * @param original is the original map
	 * @param originalKey is the key to replace
	 * @param newKey is the new key for the entries
	 * @return a new {@link ImmutableMap} the the key replaced
	 */
	public static <E,F> ImmutableMultimap<E,F> replaceKey(final ImmutableMultimap<E,F> original, final E originalKey, final E newKey){
		ImmutableMultimap.Builder<E,F> b=new ImmutableMultimap.Builder<>();
		UnmodifiableIterator<E> it=original.keySet().iterator();
		E i=null;
		while(it.hasNext()){
			i=it.next();
			if(originalKey.equals(i)){	//found originalKey
				b.putAll(newKey,original.get(i));	//replace it
			}
			else{
				b.putAll(i,original.get(i));
			}
		}
		return b.build();
	}
	
	/**
	 * Replace the value in the entries with the given new value without changing the keys.
	 * 
	 * @param <E> is the data type of the key
	 * @param <F> is the data type of the value
	 * @param original is the original map
	 * @param originalData is the value to be replaced
	 * @param newData is the new value
	 * @return a new {@link ImmutableMap} the the data replaced
	 */
	public static <E,F> ImmutableMultimap<E,F> replaceData(final ImmutableMultimap<E,F> original, final F originalData, final F newData){
		ImmutableMultimap.Builder<E,F> b=new ImmutableMultimap.Builder<>();
		UnmodifiableIterator<Entry<E,F>> it=original.entries().iterator();
		Entry<E,F> e=null;
		E i=null;
		F o=null;
		while(it.hasNext()){
			e=it.next();
			i=e.getKey();
			o=e.getValue();
			if(originalData.equals(o)){	//found originalData
				b.put(i,newData);	//replace it
			}
			else{
				b.put(i,o);
			}
		}
		return b.build();
	}
	
	/**
	 * Remove all entries where the given key or value appears.
	 * 
	 * @param <E> is the data type of the key
	 * @param <F> is the data type of the value
	 * @param original is the original map
	 * @param or is the key to remove
	 * @param nw is the value to remove
	 * @return a new {@link ImmutableMultimap} with the entries removed
	 */
	public static <E> ImmutableMultimap<E,E> replaceAll(final ImmutableMultimap<E,E> original, final E or, final E nw){
		ImmutableMultimap.Builder<E,E> b=new ImmutableMultimap.Builder<>();
		UnmodifiableIterator<Entry<E,E>> it=original.entries().iterator();
		Entry<E,E> e=null;
		E i=null;
		E o=null;
		E ni=null;
		E no=null;
		while(it.hasNext()){
			e=it.next();
			i=e.getKey();
			o=e.getValue();
			if(or.equals(i)){	//found originalKey
				ni=nw;	//replace it
			}
			else{
				ni=i;
			}
			if(or.equals(o)){	//found originalData
				no=nw;	//replace it
			}
			else{
				no=o;
			}
			b.put(ni, no);
		}
		return b.build();
	}
	
	/**
	 * Check if a given sub map is a smaller subset of a given map.
	 * 
	 * @param <E> is the data type of the key
	 * @param <F> is the data type of the value
	 * @param map is the main map
	 * @param submap is the sub map to be checked
	 * @return true if all entries of the sub map are present in the map
	 */
	public static <E,F> boolean isSubMap(final ImmutableMultimap<E,F> map,final ImmutableMultimap<E,F> submap){
		if(map.size()>submap.size()){
			if(map.entries().containsAll(submap.entries())) return true;
		}
		return false;
	}
	
	/**
	 * Get a map that is the intersection of the entries of two maps.
	 * Values are ignored for the calculation.
	 * 
	 * @param <E> is the data type of the key
	 * @param <F> is the data type of the value
	 * @param map1 is one map
	 * @param map2 is the other map
	 * @return a new {@link ImmutableMap} with the entries that belong to both maps
	 */
	public static <E,F> ImmutableMultimap<E,F> intersect(final ImmutableMultimap<E,F> map1,final ImmutableMultimap<E,F> map2){
		ImmutableCollection<Entry<E,F>> e = map1.entries();
		UnmodifiableIterator<Entry<E,F>> it = map2.entries().iterator();
		Entry<E,F> n=null;
		ImmutableMultimap.Builder<E, F> m=ImmutableMultimap.builder();
		while(it.hasNext()){
			n=it.next();
			if(e.contains(n))m.put(n);
		}
		return m.build();
	}
	
	/**
	 * Compare two maps and return a number that indicates the likeliness between them.
	 * Values are ignored for the comparison.
	 * Possibly return values:
	 *  - a positive number >0 indicates they are different but have that number of shared entries
	 *  - a zero indicates that the maps are identical (share all entries)
	 *  - a -1 indicates that map2 is a submap of map1
	 *  - a -2 indicates that map1 is a submap of map2
	 *  - a -1000 indicates a failure to compare
	 * 
	 * @param <E> is the data type of the key
	 * @param <F> is the data type of the value
	 * @param map1 is one map
	 * @param map2 is the other map
	 * @return -1000 for failure, 0 if identical,-1 if map2 submap of map1, -2 if map1 submap of map2, or number>0 indicating the number of identical entries
	 */
	public static <E,F> int compare(final ImmutableMultimap<E,F> map1,final ImmutableMultimap<E,F> map2){
		ImmutableMultimap<E, F> in = intersect(map1, map2);
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
	
	/**
	 * Check if two maps are identical, that is, all keys of each map is present in
	 * the other and vice-versa.
	 * Values are ignored for the comparison.
	 * 
	 * @param <E> is the data type of the key
	 * @param <F> is the data type of the value
	 * @param map1 is one map
	 * @param map2 is the other map
	 * @return true if all keys of each map are present in both maps
	 */
	public static <E,F> boolean matches(final ImmutableMultimap<E,F> map1,final ImmutableMultimap<E,F> map2){
		if(compare(map1, map2)==0)return true;
		return false;
	}
}
