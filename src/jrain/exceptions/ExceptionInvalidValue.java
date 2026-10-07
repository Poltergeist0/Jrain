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
