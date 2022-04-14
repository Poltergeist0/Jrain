package jrain.polyType.mutable;

/**
 * @author poltergeist0
 *
 * Polymorphic Integer type
 * 
 * See {@link PolyType} for more details.
 */
public class IntegerPoly extends PolyType<Integer> {
	
	/**
	 * Constructor for a given value
	 * 
	 * @param value is the data to store
	 */
	public IntegerPoly(Integer value) { super(value);}
	
	/**
	 * Copy constructor
	 * 
	 * @param poly is the original object to be copied
	 * @param deepCopy if true makes a deep copy of the object, if possible. False makes a shallow copy
	 */
	public IntegerPoly(IntegerPoly poly,boolean deepCopy) { super(poly,false);}

	@Override
	public IntegerPoly deepCopy() {
		return new IntegerPoly(this,false);
	}
}
