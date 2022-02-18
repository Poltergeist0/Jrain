package jrain.exceptions;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * @author poltergeist0
 * 
 * Exception thrown when a path passed to a {@link File} is not a file.
 * 
 * This extends {@link java.io.FileNotFoundException} to distinguish between the path not
 * being a file and errors reading/accessing a file.
 */
public class ExceptionNotAFile extends FileNotFoundException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8395230177298745936L;

	public ExceptionNotAFile(String path){
		super(path+" is not a file.");
	}
}
