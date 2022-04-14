package jrain.entry.mutable;

import java.util.ArrayList;
import java.util.Map.Entry;

import jrain.arrayList.utils.ArrayListUtils;

/**
 * @author poltergeist0
 *
 * Implements {@link PairOfLists} with {@link ArrayList}.
 * 
 * Due to type erasure, null values are not acceptable for the key/value Lists.
 * Instead, null values act as a selector of which information to store.
 * 
 * @param <TYPE_KEY> is the data type of the key
 * @param <TYPE_DATA> is the data type of the value
 */
public class PairOfArrayList<
	TYPE_KEY extends Object,
	TYPE_DATA extends Object,
	TYPE_KEY_LIST extends ArrayList<TYPE_KEY>,
	TYPE_DATA_LIST extends ArrayList<TYPE_DATA>
> 
extends PairOfLists<TYPE_KEY,TYPE_DATA,ArrayList<TYPE_KEY>,ArrayList<TYPE_DATA>>{

	/**
	 * Empty constructor.
	 */
	public PairOfArrayList() {
		super(new ArrayList<TYPE_KEY>(), new ArrayList<TYPE_DATA>());
    }

	/**
	 * Constructor from single key and value.
	 * 
	 * @param key is the key
	 * @param value is the value
	 */
	public PairOfArrayList(final TYPE_KEY key, final TYPE_DATA value) {
		super((key==null)?null:ArrayListUtils.init(key),(value==null)?null:ArrayListUtils.init(value));
    }

	/**
	 * Constructor from separate lists of key and value.
	 * 
	 * @param key is the key
	 * @param value is the value
	 */
	public PairOfArrayList(final TYPE_KEY_LIST key, final TYPE_DATA_LIST value) {
		super(key,value);
    }

	/**
	 * Constructor from {@link Entry}.
	 * 
	 * @param <U> is the data type that extends {@link Entry}
	 * @param e is the entry containing the key and value
	 */
	public <U extends Entry<ArrayList<TYPE_KEY>,ArrayList<TYPE_DATA>> > 
	PairOfArrayList(U e) {
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
	public <U extends PairOfArrayList<TYPE_KEY,TYPE_DATA,TYPE_KEY_LIST,TYPE_DATA_LIST> > 
	PairOfArrayList(U p,boolean deepCopy){
		super((deepCopy)?p.deepCopy():p);
	}

	@Override
	public PairOfArrayList<TYPE_KEY,TYPE_DATA,TYPE_KEY_LIST,TYPE_DATA_LIST> deepCopy() {
		return new PairOfArrayList<>(this, true);
	}
	
}
