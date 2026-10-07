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
