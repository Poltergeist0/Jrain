package jrain.polyType.mutable;

/**
 * @author poltergeist0
 *
 * Class for immutable polymorphic data types.
 * 
 * Allows using a heterogeneous set of data types in places they are not normally
 * allowed, such as in standard collections.
 * 
 * Check {@link jrain.polyType.immutable.PolyType} for the immutable version.
 * 
 * Example:
 * {@code
 * PolyType<Integer> i=new PolyType<>(new Integer(13));
 * PolyType<String> s=new PolyType<>(new String("qwerty"));
 * ArrayList<PolyType<?>> arr=new ArrayList<>();
 * arr.add(i);
 * arr.add(s);//allowed :)
 * }
 * 
 * @param <T> is the type of the data to store
 */
public class PolyType<T> extends jrain.polyType.immutable.PolyType<T>{
	
	/**
	 * Constructor for a given value
	 * 
	 * @param value is the data to store
	 */
	public PolyType(T value) { super(value);}
	
	/**
	 * Copy constructor
	 * 
	 * @param poly is the original object to be copied
	 * @param deepCopy if true makes a deep copy of the object, if possible. False makes a shallow copy
	 */
	public PolyType(PolyType<T> poly,boolean deepCopy) { super(poly,deepCopy);}
	
	/**
	 * Set the data stored.
	 */
	public void set(T value) { super.set(value);}
	
}
