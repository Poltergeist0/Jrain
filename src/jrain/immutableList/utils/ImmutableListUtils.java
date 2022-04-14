package jrain.immutableList.utils;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;

/**
 * @author poltergeist0
 *
 * Provides several static methods to work with {@code ImmutableList}
 */
public class ImmutableListUtils{

	/**
	 * Add value to the end of an {@link ImmutableList}
	 * 
	 * @param <E> is the data type of the value
	 * @param original is the original list
	 * @param data is the value to add
	 * @return a new {@link ImmutableList} with the new value added
	 */
	public static <E> ImmutableList<E> add(final ImmutableList<E> original, final E data){
		ImmutableList.Builder<E> b=new ImmutableList.Builder<>();
		b.addAll(original);
		b.add(data);
		return b.build();
	}
	
	/**
	 * Add value to a given position of an {@link ImmutableList}.
	 * If position is greater than the length then the value is inserted in the end.
	 * 
	 * @param <E> is the data type of the value
	 * @param original is the original list
	 * @param data is the value to add
	 * @param position is the position where value is to be inserted
	 * @return a new {@link ImmutableList} with the new value added
	 * @throws IndexOutOfBoundsException if position is less than zero
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
	 * Concatenate parts of two {@link ImmutableList} into a new list.
	 * 
	 * @param <E> is the data type of the value
	 * @param a is the first list
	 * @param aStart is the start position in the first byte array
	 * @param aEnd is the end position in the first byte array (exclusive)
	 * @param b is the second list
	 * @param bStart is the start position in the second byte array
	 * @param bEnd is the end position in the second byte array (exclusive)
	 * @return as a new {@link ImmutableList} with the concatenation
	 */
	public static <E> ImmutableList<E> concatenate(final ImmutableList<E> a, int aStart, int aEnd, final ImmutableList<E> b, int bStart, int bEnd){
		ImmutableList.Builder<E> r=new ImmutableList.Builder<>();
		r.addAll(a.subList(aStart, aEnd));
		r.addAll(b.subList(bStart, bEnd));
		return r.build();
	}
	
	/**
	 * Remove the first occurrence of a given value, if it exists in the list.
	 * 
	 * @param <E> is the data type of the value
	 * @param original is the original list
	 * @param data is the value to remove
	 * @return a new {@link ImmutableList} with the value removed
	 */
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
	
	/**
	 * Remove the first occurrence of each of the values given, if they exist in the list.
	 * 
	 * @param <E> is the data type of the value
	 * @param original is the original list
	 * @param data is list with the values to remove
	 * @return a new {@link ImmutableList} with the values removed
	 */
	public static <E> ImmutableList<E> remove(final ImmutableList<E> original, final ImmutableList<E> data){
		ImmutableList.Builder<E> b=new ImmutableList.Builder<>();
		UnmodifiableIterator<E> it=original.iterator();
		E i=null;
		while(it.hasNext()){
			i=it.next();
			if(!data.contains(i))b.add(i);
		}
		return b.build();
	}
	
	/**
	 * Convert an {@link ImmutableList} into a mutable {@link ArrayList}.
	 * The values may still be immutable.
	 * 
	 * @param <E> is the data type of the values in the list
	 * @param list is the {@link ImmutableList}
	 * @return an {@link ArrayList} with the values
	 */
	public static <E> ArrayList<E> toArrayList(final ImmutableList<E> list){
		return new ArrayList<>(list);
	}
	
	/**
	 * Convert an {@link ArrayList} into an {@link ImmutableList}.
	 * The values may still be mutable.
	 * 
	 * @param <E> is the data type of the values in the list
	 * @param list is the {@link ArrayList}
	 * @return an {@link ImmutableList} with the values
	 */
	public static <E> ImmutableList<E> toImmutableList(final ArrayList<E> list){
		ImmutableList.Builder<E> a=ImmutableList.builder();
		Iterator<E> it=list.iterator();
		while(it.hasNext()){
			a.add(it.next());
		}
		return a.build();
	}
	
	/**
	 * Convert an array into an {@link ImmutableList}.
	 * The values may still be mutable.
	 * 
	 * @param <E> is the data type of the values in the list
	 * @param list is the array
	 * @return an {@link ImmutableList} with the values
	 */
	public static <E> ImmutableList<E> toImmutableList(final E[] list){
		ImmutableList.Builder<E> im=ImmutableList.builder();
		for (int i = 0; i < list.length; i++) {
			im.add(list[i]);
		}
		return im.build();
	}
	
}
