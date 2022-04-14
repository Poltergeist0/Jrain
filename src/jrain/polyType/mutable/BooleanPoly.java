package jrain.polyType.mutable;

/**
 * @author poltergeist0
 *
 * Polymorphic Boolean type
 * 
 * See {@link PolyType} for more details.
 */
public class BooleanPoly extends PolyType<Boolean> {
	
	/**
	 * Constructor for a given value
	 * 
	 * @param value is the data to store
	 */
	public BooleanPoly(boolean value) { super(value);}
	
	/**
	 * Copy constructor
	 * 
	 * @param poly is the original object to be copied
	 * @param deepCopy if true makes a deep copy of the object, if possible. False makes a shallow copy
	 */
	public BooleanPoly(BooleanPoly poly,boolean deepCopy) { super(poly,false);}

	@Override
	public BooleanPoly deepCopy() {
		return new BooleanPoly(this,false);
	}
}
