package jrain.polyType.hash.immutable;

import jrain.hash.immutable.Hash;
import jrain.polyType.immutable.PolyType;

/**
 * @author poltergeist0
 *
 * Base class for polymorphic hash data types.
 * 
 * Allows using a heterogeneous set of hash data types in places they are not normally
 * allowed, such as in standard collections.
 * 
 * @param <T> is the type of the hash instance to store
 */
public abstract class PolyHash<T> extends PolyType<T> implements Hash{

	/**
	 * Constructor for a given hash instance
	 * 
	 * @param value is the hash instance to store
	 */
	public PolyHash(T value) { super(value);}

	/**
	 * Copy constructor
	 * 
	 * @param poly is the original object to be copied
	 * @param deepCopy if true makes a deep copy of the object, if possible. False makes a shallow copy
	 */
	public PolyHash(PolyHash<T> poly,boolean deepCopy) { super(poly,false);}
	
	public abstract void update(byte[] b, int offset, int len);

	public abstract byte[] digest();

}
