package jrain.returnAnswer.mutable;

/**
 * Simple class that allows functions to return a boolean indicating 
 * success/failure of the operation in addition of the expected result.
 * 
 * @author poltergeist0
 *
 * @param <TYPE_DATA> is the data type of the value that would normally be returned
 */
public class ReturnAnswer<TYPE_DATA>{
	
	private boolean b;
	private TYPE_DATA t;
	
	/**
	 * @return the expected result
	 */
	public TYPE_DATA get() {return t;}
	
	/**
	 * @return true if the operation succeeded
	 */
	public boolean is() {return b;}

	/**
	 * Constructor with independently assigned value and success
	 * 
	 * @param val is the expected result
	 * @param bo is the operation status (success/failure)
	 */
	public ReturnAnswer(boolean bo,TYPE_DATA val){b=bo;t=val;}
	
	/**
	 * Constructor to indicate success/failure status without the expected result
	 * 
	 * @param bo is the operation status (success/failure)
	 */
	public ReturnAnswer(boolean bo){b=bo;t=null;}
	
	/**
	 * Constructor to indicate failure status without the expected result
	 * 
	 * @param bo is the operation status (success/failure)
	 */
	public ReturnAnswer(){b=false;t=null;}
	
	/**
	 * Constructor to indicate success status with the expected result
	 * 
	 * @param val is the expected result
	 */
	public ReturnAnswer(TYPE_DATA val){b=true;t=val;}
}
