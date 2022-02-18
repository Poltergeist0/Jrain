package jrain.utils;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;

public class ImmutableListUtils{

	/**
	 * add value to the end of an immutablelist
	 * 
	 * @param original
	 * @param data
	 * @return a new immutablelist with the new value added to the end
	 */
	public static <E> ImmutableList<E> add(final ImmutableList<E> original, final E data){
		ImmutableList.Builder<E> b=new ImmutableList.Builder<>();
		b.addAll(original);
		b.add(data);
		return b.build();
	}
	
	/**
	 * add value to an immutablelist
	 * 
	 * @param original
	 * @param data
	 * @param position is the position where data is to be inserted. If position is greater than the length then the data is inserted in the end
	 * @return a new immutablelist with the new value added
	 * @throws IndexOutOfBoundsException if position less than zero
	 */
	public static <E> ImmutableList<E> add(final ImmutableList<E> original, final E data, final int position)throws IndexOutOfBoundsException{
		if(position<0) throw new IndexOutOfBoundsException();
		int pos=original.size();
		int posl=pos;
		if(pos>position)pos=position;
		ImmutableList.Builder<E> b=new ImmutableList.Builder<>();
		if(pos>0)b.addAll(original.subList(0, pos));
		b.add(data);
		if(pos<posl)b.addAll(original.subList(pos,posl));
		return b.build();
	}
	
	/**
	 * concatenate two immutablelist
	 * @param original
	 * @param data
	 * @return
	 */
	public static <E> ImmutableList<E> concatenate(final ImmutableList<E> original, final ImmutableList<E> data){
		ImmutableList.Builder<E> b=new ImmutableList.Builder<>();
		b.addAll(original);
		b.addAll(data);
		return b.build();
	}
	
	public static <E> ImmutableList<E> remove(final ImmutableList<E> original, final E data){
		ImmutableList.Builder<E> b=new ImmutableList.Builder<>();
		UnmodifiableIterator<E> it=original.iterator();
		E i=null;
		while(it.hasNext()){
			i=it.next();
			if(!i.equals(data))b.add(i);
		}
		return b.build();
	}
	
	public static <E> ImmutableList<E> remove(final ImmutableList<E> original, final ImmutableList<E> data){
//		if(data==null)return original;
		ImmutableList.Builder<E> b=new ImmutableList.Builder<>();
		UnmodifiableIterator<E> it=original.iterator();
		E i=null;
		while(it.hasNext()){
			i=it.next();
			if(!data.contains(i))b.add(i);
		}
		return b.build();
	}
	
	public static <E> ArrayList<E> toArrayList(final ImmutableList<E> list){
//		ArrayList<E> a=new ArrayList<>();
//		UnmodifiableIterator<E> it=list.iterator();
//		while(it.hasNext()){
//			a.add(it.next());
//		}
//		return a;
		return new ArrayList<>(list);
	}
	
	public static <E> ImmutableList<E> toImmutableList(final ArrayList<E> list){
		ImmutableList.Builder<E> a=ImmutableList.builder();
		Iterator<E> it=list.iterator();
		while(it.hasNext()){
			a.add(it.next());
		}
		return a.build();
	}
	
	public static <E> ImmutableList<E> toImmutableList(final E[] list){
		ImmutableList.Builder<E> im=ImmutableList.builder();
		for (int i = 0; i < list.length; i++) {
			im.add(list[i]);
		}
		return im.build();
	}
	
}
