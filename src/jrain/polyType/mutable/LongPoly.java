package jrain.polyType.mutable;

/**
 * @author poltergeist0
 *
 * Polymorphic Long type
 * 
 * See {@link PolyType} for more details.
 */
public class LongPoly extends PolyType<Long> {
	
	/**
	 * Constructor for a given value
	 * 
	 * @param value is the data to store
	 */
	public LongPoly(Long value) { super(value);}
	
	/**
	 * Copy constructor
	 * 
	 * @param poly is the original object to be copied
	 * @param deepCopy if true makes a deep copy of the object, if possible. False makes a shallow copy
	 */
	public LongPoly(LongPoly poly,boolean deepCopy) { super(poly,false);}

	@Override
	public LongPoly deepCopy() {
		return new LongPoly(this,false);
	}
}
