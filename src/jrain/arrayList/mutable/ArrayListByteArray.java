package jrain.arrayList.mutable;

import java.util.ArrayList;
import java.util.Collection;

import jrain.arrayList.mutable.deepCopy.DeepCopy;

/**
 * @author poltergeist0
 *
 * Alias class for {@link ArrayList} with elements of type {@link Byte} array.
 * 
 * Implements {@link DeepCopy} on the list only, not on accessing the elements.
 */
public class ArrayListByteArray extends ArrayList<Byte[]> implements DeepCopy<ArrayListByteArray>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1558642104465381142L;

	/**
	 * See {@link ArrayList} constructor documentation
	 */
	public ArrayListByteArray() {super();}

	/**
	 * See {@link ArrayList} constructor documentation
	 */
	public ArrayListByteArray(Collection<? extends Byte[] > c) {super(c);}
	
	/**
	 * See {@link ArrayList} constructor documentation
	 */
	public ArrayListByteArray(int initialCapacity) {super(initialCapacity);}
	
	@Override
	public ArrayListByteArray deepCopy() {
		return DeepCopy.deepCopy(this);
	}

}
