package jrain.mutable.arrayList;

import java.util.ArrayList;
import java.util.Collection;

import jrain.mutable.arrayList.deepCopy.interfaces.DeepCopy;

/**
 * @author poltergeist0
 *
 * Alias class for {@link ArrayList} with elements of type {@link String}.
 * 
 * Implements {@link DeepCopy} on the list only, not on accessing the elements.
 */
public class ArrayListString extends ArrayList<String> implements DeepCopy<ArrayListString>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3713113736643580026L;

	public ArrayListString() {super();}
	public ArrayListString(Collection<String> c) {super(c);}
	public ArrayListString(int initialCapacity) {super(initialCapacity);}
	
	@Override
	public ArrayListString deepCopy() {
		return DeepCopy.deepCopy(this);
	}

}
