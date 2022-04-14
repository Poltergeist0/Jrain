package jrain.polyType.mutable;

/**
 * @author poltergeist0
 *
 * Polymorphic Double type
 * 
 * See {@link PolyType} for more details.
 */
public class DoublePoly extends PolyType<Double> {
	
	/**
	 * Constructor for a given value
	 * 
	 * @param value is the data to store
	 */
	public DoublePoly(Double value) { super(value);}
	
	/**
	 * Copy constructor
	 * 
	 * @param poly is the original object to be copied
	 * @param deepCopy if true makes a deep copy of the object, if possible. False makes a shallow copy
	 */
	public DoublePoly(DoublePoly poly,boolean deepCopy) { super(poly,false);}

	@Override
	public DoublePoly deepCopy() {
		return new DoublePoly(this,false);
	}
}
