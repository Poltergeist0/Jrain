package jrain.exceptions;

/**
 * @author poltergeist0
 *
 * Exception thrown when an invalid value is used.
 * 
 * Example: being given a negative value when only positive values are acceptable.
 */
public class ExceptionInvalidValue extends Exception {
	
	private static final long serialVersionUID = 8606400685394836002L;

	public ExceptionInvalidValue (String message){
		super(message);
	}
}
