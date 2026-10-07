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
package jrain.utils;

/**
 * @author poltergeist0
 *
 * Set of methods to deal with white spaces and comments
 */
public class JumpWhiteSpaces {
	
	/**
	 * Check if the given string is a whitespace
	 * @param s is the string to process
	 * @return true if the string contains only a whitespace
	 */
	public static boolean isWhitespace(String s){
		if(" ".equals(s)) return true;
		if("\t".equals(s)) return true;
		if("\r".equals(s)) return true;
		if("\n".equals(s)) return true;
		return false;
	}
	
	/**
	 * Get the new position in the given string starting at the given position and
	 * advancing any whitespace.
	 * If there is no whitespace then the given position is returned.
	 * 
	 * @param s is the string to process
	 * @param currentPosition is the start position to use in processing
	 * @return the new position after any whitespace
	 */
	public static int jumpWhitespaces(String s, int currentPosition){
		while(isWhitespace(s.substring(currentPosition, currentPosition+1))) currentPosition++;
		return currentPosition;
	}
	
	/**
	 * Advance to after any whitespace or XML comment.
	 * 
	 * @param s is the string to process
	 * @param currentPosition is the start position to use in processing
	 * @return the new position after any whitespace or -1 if there is no XML comment closing tag
	 */
	public static int xmlJump(String s, int currentPosition){
		//jump over white spaces and XML comments
		//return new position or -1 if there is no comment closing tag
		int pos=jumpWhitespaces(s, currentPosition);
		//jump comments
		if(s.startsWith("<!--", pos)){
			pos+=4;
			pos=s.indexOf("-->",pos);
			if(pos<0){
				return -1;
			}
			pos+=3;
			pos=JumpWhiteSpaces.jumpWhitespaces(s, pos);
		}
		return pos;
	}
	
}
