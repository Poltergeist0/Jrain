package jrain.entry.mutable;

import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

/**
 * @author poltergeist0
 *
 * Implements {@link Pair} of {@link List}s.
 * 
 * Due to type erasure, null values are not acceptable for the key/value Lists.
 * Instead, null values act as a selector of which information to store.
 * 
 * @param <TYPE_KEY> is the data type of the key
 * @param <TYPE_DATA> is the data type of the value
 * @param <TYPE_KEY_LIST> is the data type of the key list
 * @param <TYPE_DATA_LIST> is the data type of the data list
 */
public class PairOfLists<
	TYPE_KEY extends Object,
	TYPE_DATA extends Object,
	TYPE_KEY_LIST extends List<TYPE_KEY>,
	TYPE_DATA_LIST extends List<TYPE_DATA>
> 
extends Pair<TYPE_KEY_LIST,TYPE_DATA_LIST>{

	/**
	 * Add non-null data to key and/or value lists.
	 * Null data is ignored and does not add anything so that addition can occur
	 * only to the key or value list.
	 * 
	 * @param k is the data to add to the key list
	 * @param v is the data to add to the value list
	 */
	public void add(TYPE_KEY k, TYPE_DATA v) {
		if(k!=null) super.getKey().add(k);
		if(v!=null) super.getValue().add(v);
	}
	
	/**
	 * Add data in {@link Collection}s to key and/or value lists.
	 * Null {@link Collection} is ignored and does not add anything.
	 * 
	 * @param <TYPE_KEY_COLLECTION> is the data type of the key list that extends {@link Collection}
	 * @param <TYPE_DATA_COLLECTION> is the data type of the value list that extends {@link Collection}
	 * @param k is the data to add to the key list
	 * @param v is the data to add to the value list
	 */
	public <TYPE_KEY_COLLECTION extends Collection<TYPE_KEY>, TYPE_DATA_COLLECTION extends Collection<TYPE_DATA> >
	void add(TYPE_KEY_COLLECTION k, TYPE_DATA_COLLECTION v) {
		if(k!=null) super.getKey().addAll(k);
		if(v!=null) super.getValue().addAll(v);
	}
	
	/**
	 * Add data in {@link Collection}s to key and/or value lists.
	 * Null {@link Collection} is ignored and does not add anything.
	 * The {@link Collection}s are wrapped in a class that extends {@link Entry}.
	 * 
	 * @param <TYPE_KEY_COLLECTION> is the data type of the key list that extends {@link Collection}
	 * @param <TYPE_DATA_COLLECTION> is the data type of the data list that extends {@link Collection}
	 * @param <TYPE_ENTRY> is the data type that extends {@link Entry}
	 * @param e is the {@link Entry} with the data to add
	 */
	public <TYPE_KEY_COLLECTION extends Collection<TYPE_KEY>, TYPE_DATA_COLLECTION extends Collection<TYPE_DATA>, TYPE_ENTRY extends Entry<TYPE_KEY_COLLECTION, TYPE_DATA_COLLECTION> >
	void add(TYPE_ENTRY e) {
		if(e.getKey()!=null) super.getKey().addAll(e.getKey());
		if(e.getValue()!=null) super.getValue().addAll(e.getValue());
	}
	
	/**
	 * Add data in {@link PairOfLists} to key and/or value lists.
	 * 
	 * @param <U> is the data type of the object that extends this class
	 * @param p are the collections to add
	 */
	public <U extends PairOfLists<TYPE_KEY,TYPE_DATA,TYPE_KEY_LIST,TYPE_DATA_LIST> >
	void add(U p) {
		add(p.getKey(),p.getValue());
	}
	
	/**
	 * Remove non-null data from key and/or value lists.
	 * Null data is ignored and does not remove anything.
	 * 
	 * @param k is the data to remove from the key list
	 * @param v is the data to remove from the value list
	 */
	public void remove(TYPE_KEY k, TYPE_DATA v) {
		if(k!=null) super.getKey().remove(k);
		if(v!=null) super.getValue().remove(v);
	}
	
	/**
	 * Remove data in {@link Collection}s from key and/or value lists.
	 * Null in {@link Collection} is ignored and does not remove anything.
	 * 
	 * @param <TYPE_KEY_COLLECTION> is the data type of the key list that extends {@link Collection}
	 * @param <TYPE_DATA_COLLECTION> is the data type of the data list that extends {@link Collection}
	 * @param k is the data to remove from the key list
	 * @param v is the data to remove from the value list
	 */
	public <TYPE_KEY_COLLECTION extends Collection<TYPE_KEY>, TYPE_DATA_COLLECTION extends Collection<TYPE_DATA> >
	void remove(TYPE_KEY_COLLECTION k, TYPE_DATA_COLLECTION v) {
		if(k!=null) super.getKey().removeAll(k);
		if(v!=null) super.getValue().removeAll(v);
	}
	
	/**
	 * Remove data in {@link Collection}s from key and/or value lists.
	 * Null {@link Collection} is ignored and does not remove anything.
	 * The {@link Collection}s are wrapped in a class that extends {@link Entry}.
	 * 
	 * @param <TYPE_KEY_COLLECTION> is the data type of the key list that extends {@link Collection}
	 * @param <TYPE_DATA_COLLECTION> is the data type of the data list that extends {@link Collection}
	 * @param <TYPE_ENTRY> is the data type that extends {@link Entry}
	 * @param e is the {@link Entry} with the data to remove
	 */
	public <TYPE_KEY_COLLECTION extends Collection<TYPE_KEY>, TYPE_DATA_COLLECTION extends Collection<TYPE_DATA>, TYPE_ENTRY extends Entry<TYPE_KEY_COLLECTION, TYPE_DATA_COLLECTION> >
	void remove(TYPE_ENTRY e) {
		if(e.getKey()!=null) super.getKey().removeAll(e.getKey());
		if(e.getValue()!=null) super.getValue().removeAll(e.getValue());
	}
	
	
	/**
	 * Constructor from separate key and value.
	 * 
	 * @param key is the key
	 * @param value is the value
	 */
	public PairOfLists(final TYPE_KEY_LIST key, final TYPE_DATA_LIST value) {
		super(key,value);
    }

	/**
	 * Constructor from {@link Entry}.
	 * 
	 * @param <U> is the data type that extends {@link Entry}
	 * @param e is the entry containing the key and value
	 */
	public <U extends Entry<TYPE_KEY_LIST,TYPE_DATA_LIST> > 
	PairOfLists(U e) {
        super(e);
    }

	/**
	 * Copy constructor.
	 * Can make a shallow or a deep copy of the given object.
	 * If a deep copy is chosen, the key and value of the given object are deep
	 * copied before being assigned to this object, if they support deep copying.
	 * Otherwise, only a reference is copied.
	 * 
	 * @param <U> is the data type of the object that extends this class
	 * @param p is the given object
	 * @param deepCopy if true, makes a deep copy, otherwise makes a shallow copy
	 */
	public <U extends PairOfLists<TYPE_KEY,TYPE_DATA,TYPE_KEY_LIST,TYPE_DATA_LIST> > 
	PairOfLists(U p,boolean deepCopy){
		super(p);
	}

	@Override
	public PairOfLists<TYPE_KEY,TYPE_DATA,TYPE_KEY_LIST,TYPE_DATA_LIST> deepCopy() {
		return new PairOfLists<>(this,true);
	}

}
