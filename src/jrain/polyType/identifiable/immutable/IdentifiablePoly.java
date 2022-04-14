package jrain.polyType.identifiable.immutable;

import jrain.polyType.mutable.PolyType;
import jrain.identifiable.immutable.Identifiable;

/**
 * @author poltergeist0
 *
 * Polymorphic Identifiable type
 * 
 * See {@link PolyType} for more details.
 */
public class IdentifiablePoly extends PolyType<Identifiable> {

	/**
	 * Constructor for a given value
	 * 
	 * @param value is the data to store
	 */
	public IdentifiablePoly(Identifiable value) { super(value);}
	
	/**
	 * Copy constructor
	 * 
	 * @param poly is the original object to be copied
	 * @param deepCopy if true makes a deep copy of the object, if possible. False makes a shallow copy
	 */
	public IdentifiablePoly(IdentifiablePoly poly,boolean deepCopy) { super(poly,false);}

	@Override
	public IdentifiablePoly deepCopy() {
		return new IdentifiablePoly(this,false);
	}
}
