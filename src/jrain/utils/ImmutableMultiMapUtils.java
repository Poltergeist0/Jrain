package jrain.utils;

import java.util.Map.Entry;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.UnmodifiableIterator;

public class ImmutableMultiMapUtils{

	public static <E,F> ImmutableMultimap<E,F> addValue(final ImmutableMultimap<E,F> original, final E key,final F data){
		ImmutableMultimap.Builder<E,F> b=new ImmutableMultimap.Builder<>();
		b.putAll(original);
		b.put(key,data);
		return b.build();
	}
	
	public static <E,F> ImmutableMultimap<E,F> addValues(final ImmutableMultimap<E,F> original, final E key,final ImmutableList<F> data){
		ImmutableMultimap.Builder<E,F> b=new ImmutableMultimap.Builder<>();
		b.putAll(original);
		b.putAll(key,data);
		return b.build();
	}
	
	public static <E,F> ImmutableMultimap<E,F> addMultimap(final ImmutableMultimap<E,F> original, final ImmutableMultimap<E,F> data){
		ImmutableMultimap.Builder<E,F> b=new ImmutableMultimap.Builder<>();
		b.putAll(original);
		b.putAll(data);
		return b.build();
	}
	
	/**
	 * remove given keyValue from both the keys and the values
	 * 
	 * @param original
	 * @param keyValue
	 * @return
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
	 * remove given keyValue from both the keys and the values
	 * 
	 * @param original
	 * @param keyValue
	 * @return
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
	
	public static <E,F> boolean isSubMap(final ImmutableMultimap<E,F> map,final ImmutableMultimap<E,F> submap){
		if(map.size()>submap.size()){
			if(map.entries().containsAll(submap.entries())) return true;
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
	 * @param map1
	 * @param map2
	 * @return -3 for failure, 0 if identical,-1 if map2 submap of map1, -2 if map1 submap of map2, or number>0 indicating the number of identical entries
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
	
	public static <E,F> boolean matches(final ImmutableMultimap<E,F> map1,final ImmutableMultimap<E,F> map2){
		if(compare(map1, map2)==0)return true;
		return false;
	}
}
