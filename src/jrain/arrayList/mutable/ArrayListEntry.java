package jrain.arrayList.mutable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;

import jrain.arrayList.mutable.deepCopy.DeepCopy;

/**
 * @author poltergeist0
 *
 * Alias class for {@link ArrayList} with elements of type {@link Entry}.
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
	
	/**
	 * See {@link ArrayList} constructor documentation
	 */
	public ArrayListEntry() {super();}

	/**
	 * See {@link ArrayList} constructor documentation
	 */
	public ArrayListEntry(Collection<? extends Entry<TYPE_KEY,TYPE_DATA> > c) {super(c);}

	/**
	 * See {@link ArrayList} constructor documentation
	 */
	public ArrayListEntry(int initialCapacity) {super(initialCapacity);}
	
	@Override
	public ArrayListEntry<TYPE_KEY, TYPE_DATA> deepCopy() {
		return DeepCopy.deepCopy(this);
	}

	/**
	 * Get the keys of all {@link Entry}s in the {@link ArrayList}
	 * @return a list with the keys
	 */
	public static <TYPE_KEY extends Object,TYPE_DATA extends Object,TYPE_LIST extends ArrayList<Entry<TYPE_KEY,TYPE_DATA>>> 
	ArrayList<TYPE_KEY> getKeys(TYPE_LIST lst){
		ArrayList<TYPE_KEY> a=new ArrayList<>();
		Iterator<Entry<TYPE_KEY, TYPE_DATA>> it = lst.iterator();
		while(it.hasNext()) {
			Entry<TYPE_KEY, TYPE_DATA> e = it.next();
			a.add(e.getKey());
		}
		return a;
	}

	/**
	 * Get the values of all {@link Entry}s in the {@link ArrayList}
	 * @return a list with the values
	 */
	public static <TYPE_KEY extends Object,TYPE_DATA extends Object,TYPE_LIST extends ArrayList<Entry<TYPE_KEY,TYPE_DATA>>> 
	ArrayList<TYPE_DATA> getValues(TYPE_LIST lst){
		ArrayList<TYPE_DATA> a=new ArrayList<>();
		Iterator<Entry<TYPE_KEY, TYPE_DATA>> it = lst.iterator();
		while(it.hasNext()) {
			Entry<TYPE_KEY, TYPE_DATA> e = it.next();
			a.add(e.getValue());
		}
		return a;
	}

	/**
	 * Get the keys of all {@link Entry}s in the {@link ArrayList}
	 * @return a list with the keys
	 */
	public ArrayList<TYPE_KEY> getKeys(){
		return getKeys(this);
	}

	/**
	 * Get the values of all {@link Entry}s in the {@link ArrayList}
	 * @return a list with the values
	 */
	public ArrayList<TYPE_DATA> getValues(){
		return getValues(this);
	}
}
