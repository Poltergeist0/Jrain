package jrain.mutable.arrayList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;

import jrain.mutable.arrayList.deepCopy.interfaces.DeepCopy;

/**
 * @author poltergeist0
 *
 * Alias class for {@link ArrayList} with elements of type {@link Entry} array.
 * 
 * Implements {@link DeepCopy} on the list only, not on accessing the elements.
 *
 * @param <TYPE_KEY> is the type of the key of the {@link Entry} elements
 * @param <TYPE_DATA> is the type of the value of the {@link Entry} elements
 */
public class ArrayListEntry<TYPE_KEY,TYPE_DATA> extends ArrayList<Entry<TYPE_KEY,TYPE_DATA> > implements DeepCopy<ArrayListEntry<TYPE_KEY,TYPE_DATA>>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1854305245934535043L;
	
	public ArrayListEntry() {super();}
	public ArrayListEntry(Collection<? extends Entry<TYPE_KEY,TYPE_DATA> > c) {super(c);}
	public ArrayListEntry(int initialCapacity) {super(initialCapacity);}
	
	@Override
	public ArrayListEntry<TYPE_KEY, TYPE_DATA> deepCopy() {
		return DeepCopy.deepCopy(this);
	}

	public ArrayList<TYPE_KEY> getKeys(){
		ArrayList<TYPE_KEY> a=new ArrayList<>();
		Iterator<Entry<TYPE_KEY, TYPE_DATA>> it = iterator();
		while(it.hasNext()) {
			Entry<TYPE_KEY, TYPE_DATA> e = it.next();
			a.add(e.getKey());
		}
		return a;
	}

	public ArrayList<TYPE_DATA> getValues(){
		ArrayList<TYPE_DATA> a=new ArrayList<>();
		Iterator<Entry<TYPE_KEY, TYPE_DATA>> it = iterator();
		while(it.hasNext()) {
			Entry<TYPE_KEY, TYPE_DATA> e = it.next();
			a.add(e.getValue());
		}
		return a;
	}
}
