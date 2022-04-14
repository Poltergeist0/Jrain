package jrain.polyType.mutable;

/**
 * @author poltergeist0
 *
 * Polymorphic String type
 * 
 * See {@link PolyType} for more details.
 */
public class StringPoly extends PolyType<String> {
	
	/**
	 * Constructor for a given value
	 * 
	 * @param value is the data to store
	 */
	public StringPoly(String value) { super(value);}
	
	/**
	 * Copy constructor
	 * 
	 * @param poly is the original object to be copied
	 * @param deepCopy if true makes a deep copy of the object, if possible. False makes a shallow copy
	 */
	public StringPoly(StringPoly poly,boolean deepCopy) { super(poly,false);}

	@Override
	public StringPoly deepCopy() {
		return new StringPoly(this,false);
	}
}
