package jrain.arrayList.mutable;

import java.util.ArrayList;
import java.util.Collection;

import jrain.arrayList.mutable.deepCopy.DeepCopy;

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

	/**
	 * See {@link ArrayList} constructor documentation
	 */
	public ArrayListString() {super();}

	/**
	 * See {@link ArrayList} constructor documentation
	 */
	public ArrayListString(Collection<String> c) {super(c);}

	/**
	 * See {@link ArrayList} constructor documentation
	 */
	public ArrayListString(int initialCapacity) {super(initialCapacity);}
	
	@Override
	public ArrayListString deepCopy() {
		return DeepCopy.deepCopy(this);
	}

}
