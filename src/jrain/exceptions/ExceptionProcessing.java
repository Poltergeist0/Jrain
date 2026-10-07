/*******************************************************************************
 * Copyright (C) 2026 poltergeist0
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Any libraries this program depends on have their own Licenses.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * LICENSE file for more details.
 ******************************************************************************/
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
