package jrain.polyType.mutable;

/**
 * @author poltergeist0
 *
 * Polymorphic Float type
 * 
 * See {@link PolyType} for more details.
 */
public class FloatPoly extends PolyType<Float> {
	
	/**
	 * Constructor for a given value
	 * 
	 * @param value is the data to store
	 */
	public FloatPoly(Float value) { super(value);}
	
	/**
	 * Copy constructor
	 * 
	 * @param poly is the original object to be copied
	 * @param deepCopy if true makes a deep copy of the object, if possible. False makes a shallow copy
	 */
	public FloatPoly(FloatPoly poly,boolean deepCopy) { super(poly,false);}

	@Override
	public FloatPoly deepCopy() {
		return new FloatPoly(this,false);
	}
}
