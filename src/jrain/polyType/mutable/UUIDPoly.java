package jrain.polyType.mutable;

import java.util.UUID;

/**
 * @author poltergeist0
 *
 * Polymorphic {@link UUID} type
 * 
 * See {@link PolyType} for more details.
 */
public class UUIDPoly extends PolyType<UUID> {
	
	/**
	 * Constructor for a given value
	 * 
	 * @param value is the data to store
	 */
	public UUIDPoly(UUID value) { super(value);}
	
	/**
	 * Copy constructor
	 * 
	 * @param poly is the original object to be copied
	 * @param deepCopy if true makes a deep copy of the object, if possible. False makes a shallow copy
	 */
	public UUIDPoly(UUIDPoly poly,boolean deepCopy) { super(poly,false);}

	@Override
	public UUIDPoly deepCopy() {
		return new UUIDPoly(this,false);
	}
}
