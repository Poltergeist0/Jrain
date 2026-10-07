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
package jrain.array.utils;

/**
 * @author poltergeist0
 *
 * Provides several static methods to work with {@code byte[]}
 */
public class ByteArrayUtils {

	/**
	 * Check if sections of two byte arrays have the same data.
	 * The start and end positions of each array are inclusive.
	 * 
	 * @param a is the first byte array
	 * @param aStart is the start position in the first byte array
	 * @param aEnd is the end position in the first byte array
	 * @param b is the second byte array
	 * @param bStart is the start position in the second byte array
	 * @param bEnd is the end position in the second byte array
	 * @return true if all the data matches
	 */
	public static boolean equals(final byte[] a,final int aStart,final int aEnd,final byte[] b,final int bStart,final int bEnd){
		if(a!=null && b!=null){
			if(aStart>=0 && aEnd>=0 && aStart<=aEnd && bStart>=0 && bEnd>=0 && bStart<=bEnd && aEnd-aStart==bEnd-bStart){//check size
				for (int i = 0; i < aEnd-aStart; i++) {//compare data
					if(a[aStart+i]!=b[bStart+i]) return false;
				}
				return true;
			}
			//TODO: add reverse equals (bEnd<bStart) so that comparison can be in reverse
		}
		return false;
	}
	
	/**
	 * Check if sections of two byte arrays have the same data.
	 * The same as the equals method but also returns true if both arrays are null.
	 * The start and end positions of each array are inclusive.
	 * 
	 * @param a is the first byte array
	 * @param aStart is the start position in the first byte array
	 * @param aEnd is the end position in the first byte array
	 * @param b is the second byte array
	 * @param bStart is the start position in the second byte array
	 * @param bEnd is the end position in the second byte array
	 * @return true if all the data matches
	 */
	public static boolean equalsNull(final byte[] a,final int aStart,final int aEnd,final byte[] b,final int bStart,final int bEnd){
		if(a==null && b==null) return true;
		return equals(a, aStart, aEnd, b, bStart, bEnd);
	}
	
	/**
	 * Replace data in the replaceIn byte array with the data in the replaceWith byte array.
	 * The original replaceIn data is modified so caution is advised if the original data
	 * is to be kept.
	 * No operation takes place if the replaceWith data extends beyond the end of replaceIn.
	 * The start positions of each array are inclusive.
	 * 
	 * @param replaceIn is the byte array with the data to be replaced
	 * @param replaceInStart is the start position where data will be replaced
	 * @param replaceWith is the byte array with the new data
	 * @param replaceWithStart is the start position of the new data
	 * @param replaceWithEnd is the end position of the new data
	 * @return the modified replaceIn
	 */
	public static byte[] replace(byte[] replaceIn, final int replaceInStart, final byte[] replaceWith, final int replaceWithStart, final int replaceWithEnd){
		if(replaceIn!=null && replaceWith!=null){
			if(replaceInStart>=0 && replaceWithStart>=0 && replaceWithEnd<=replaceWith.length && replaceInStart+replaceWithEnd-replaceWithStart<=replaceIn.length){
				for (int i = 0; i <= replaceWithEnd-replaceWithStart; i++) {
					replaceIn[replaceInStart+i]=replaceWith[replaceWithStart+i];
				}
			}
			//TODO: add reverse replace (bEnd<bStart) so that replacement can be in reverse
		}
		return replaceIn;
	}
	
	/**
	 * Concatenates parts of two byte arrays into a new byte array.
	 * 
	 * @param a is the head byte array
	 * @param aStart is the start position in the head byte array
	 * @param aEnd is the end position in the head byte array
	 * @param b is the tail byte array
	 * @param bStart is the start position in the tail byte array
	 * @param bEnd is the end position in the tail byte array
	 * @return a new byte array
	 */
	public static byte[] concatenate(final byte[] a,final int aStart,final int aEnd,final byte[] b,final int bStart,final int bEnd){
		if(a!=null && b!=null){
			if(aStart>=0 && aEnd>=0 && aStart<=aEnd && bStart>=0 && bEnd>=0 && bStart<=bEnd){//check size
				byte[] c=new byte[aEnd-aStart+bEnd-bStart+2];
				int i=0;
				int j=aStart;
				while (j<=aEnd) {
					c[i]=a[j];
					i++;
					j++;
				}
				j=bStart;
				while(j<=bEnd){
					c[i]=b[j];
					i++;
					j++;
				}
				return c;
			}
			//TODO: add reverse for a (aEnd<aStart) so that insertion can be in reverse
			//TODO: add reverse for b (bEnd<bStart) so that insertion can be in reverse
		}
		return new byte[0];
	}
	
	/**
	 * Calculate the integer base2 logarithm of the given data.
	 * The value is rounded up to the closest integer.
	 * The start and end positions are inclusive.
	 * 
	 * @param data is an array of bytes
	 * @param dataStart is the first element of the array to read
	 * @param dataEnd is the last element of the array to read (inclusive)
	 * @return a long with the result
	 */
	public static long log2(final byte[] data, final int dataStart, final int dataEnd){
		long exp=-1;
		//TODO: add reverse (dataEnd<dataStart) so that reverse endianness can be supported
		int i=dataStart;
//		int dataEnd=i+dataLength-1;
		//find the first non zero byte
		while(data[i]==0){
			i++;
			if(i>dataEnd)return exp;	//number is 0 so return -1
		}
		//Analyze the current byte
		int bitPosition=128;//left most bit of one byte
		while(bitPosition>0){
			int bit=data[i] & bitPosition;//get bit at position
			if(bit!=0) {//if not zero count how many bits remain in the byte
				while(bitPosition>0){
					exp++;
					bitPosition/=2;
				}
				break;
			}
			bitPosition/=2;
		}
		//current byte is the last byte
		if(i>=dataEnd) return exp;
		//else log2 is the number of bits remaining plus one independently of whether they are zero or one
		return exp+1+8*(dataEnd-i);
	}
	
	/**
	 * Calculate the integer base2 logarithm of the given data.
	 * The value is rounded up to the closest integer.
	 * This is the binary version where each byte element of the data contains only a zero
	 * or a one.
	 * 
	 * @param data is an array of bits
	 * @param dataStart is the first element of the array to read
	 * @param dataEnd is the last element of the array to read (inclusive)
	 * @return a long with the result
	 */
	public static long log2b(final byte[] data, final int dataStart, final int dataEnd){
		int i=dataStart;
//		int dataEnd=i+dataLength-1;
		//find the first non zero bit
		while(data[i]==0){
			i++;
			if(i>dataEnd)return -1;	//number is 0 so return -1
		}
		//current bit is one and is the last bit
		if(i>=dataEnd) return 0;
		//else log2 is the number of bits remaining plus one independently of whether they are zero or one
		return dataEnd-i+1;
	}
	
}
