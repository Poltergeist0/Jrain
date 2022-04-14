package jrain.polyType.mutable;

import java.time.LocalDateTime;

/**
 * @author poltergeist0
 *
 * Polymorphic {@link LocalDateTime} type
 * 
 * See {@link PolyType} for more details.
 */
public class LocalDateTimePoly extends PolyType<LocalDateTime> {
	
	/**
	 * Constructor for a given value
	 * 
	 * @param value is the data to store
	 */
	public LocalDateTimePoly(LocalDateTime value) { super(value);}
	
	/**
	 * Copy constructor
	 * 
	 * @param poly is the original object to be copied
	 * @param deepCopy if true makes a deep copy of the object, if possible. False makes a shallow copy
	 */
	public LocalDateTimePoly(LocalDateTimePoly poly,boolean deepCopy) { super(poly,false);}

	@Override
	public LocalDateTimePoly deepCopy() {
		return new LocalDateTimePoly(this,false);
	}
}
