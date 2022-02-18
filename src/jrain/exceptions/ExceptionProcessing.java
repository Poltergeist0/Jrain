package jrain.exceptions;

/**
 * @author poltergeist0
 *
 * Exception thrown when the programmer forgot something. Can be used as a 
 * safeguard.
 * 
 * Should never be thrown while running in production, even if the code that 
 * throws it is present.
 * 
 * Example: Safeguard against forgetting a case in a switch after adding new 
 * functionality to a class
 * {@code
 * 	switch(something){
 * 	case 0: doThis();break;
 * 	case 2: doThat();break;
 * 	//forgot case:7
 * 	default: throw new ExceptionProcessing("Forgot case in switch in class YouShould in method notForgetThis");
 * 	}
 * }
 */
public class ExceptionProcessing extends Exception {
	
	private static final long serialVersionUID = 6137902288788102051L;

	public ExceptionProcessing (String message){
		super(message);
	}
}
