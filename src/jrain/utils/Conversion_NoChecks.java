package jrain.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author poltergeist0
 *
 * This class implements static methods that can be used to convert from one data
 * type to another.
 */
public class Conversion_NoChecks {

	/**
	 * This method converts a byte array into an integer array. This is required since java does not
	 * support assigning nor casting byte arrays to integer arrays
	 * 
	 * @param b is the byte array
	 * @return the integer array
	 */
	public static int[] byteArrayToIntArray(byte[] b){
		int[]c=new int[b.length];
		for (int i = 0; i < c.length; i++) {
			c[i]=b[i];
		}
		return c;
	}
	
	/**
	 * Convert a byte array into a {@link Byte} array.
	 * 
	 * @param b is the byte array
	 * @return the {@link Byte} array
	 */
	public static Byte[] byteArrayToByteArray(byte[] b){
		Byte[]c=new Byte[b.length];
		for (int i = 0; i < c.length; i++) {
			c[i]=b[i];
		}
		return c;
	}
	
	/**
	 * Convert a {@link Byte} array into a byte array.
	 * 
	 * @param b is the {@link Byte} array
	 * @return the byte array
	 */
	public static byte[] ByteArrayTobyteArray(Byte[] b){
		byte[]c=new byte[b.length];
		for (int i = 0; i < c.length; i++) {
			c[i]=b[i].byteValue();
		}
		return c;
	}
	
	/**
	 * Convert an int array into a {@link Byte} array.
	 * WARNING: values are truncated if the are outside the limits supported by {@link Byte}
	 * 
	 * @param b is the int array
	 * @return the {@link Byte} array
	 */
	public static byte[] intArrayToByteArray(final int[] a){
		byte[]c=new byte[a.length];
		for (int i = 0; i < c.length; i++) {
			c[i]=(byte) a[i];
		}
		return c;
	}
	
	/**
	 * This method converts a String into a char array.
	 * This is required since java does not have a method to return char arrays 
	 * out of strings, only byte arrays.
	 * 
	 * @param b is the {@link String}
	 * @return the char array
	 */
	public static char[] stringToCharArray(String s){
		byte[] b=s.getBytes();
		char[]c=new char[b.length];
		for (int i = 0; i < b.length; i++) {
			c[i]=(char) b[i];
		}
		return c;
	}
	
	/**
	 * Transforms a string into an int array.
	 * 
	 * @param s is the {@link String}
	 * @return is the int array
	 */
	public static int[] stringToIntArray(String s){
		byte[] b=s.getBytes();
		//in java byte arrays can not be directly converted to integer arrays so another array is required
		return byteArrayToIntArray(b);
	}
	
	/**
	 * This method transforms an integer array into a string.
	 * It assumes that each array position contains an integer between zero and 
	 * 255 (inclusive) or, in other words, an integer corresponding to an unsigned
	 * byte.
	 * If that is not the case, values are truncated and, the method returns an 
	 * unexpected string.
	 * It is the opposite operation of {@link #stringToIntArray(String)}
	 * 
	 * @param a is the int array with each position holding the integer equivalent of a byte
	 * @return the string
	 */
	public static String intArrayToString(int[] a){
		byte[] b=new byte[a.length];
		for (int i = 0; i < b.length; i++) {
			b[i]=(byte) a[i];	//must explicitly cast integer to byte. This is were data loss may occur 
		}
		return new String(b);
	}
	
	/**
	 * This method converts a string array with (textual) integers in each 
	 * position into an integer array (parses an integer from each position of 
	 * the string array).
	 * It is useful for reading integer arrays from the main method arguments or
	 * other textual inputs.
	 *  
	 * @param s is the string array with the integers in each position
	 * @return an int array
	 */
	public static int[] intStringArrayToIntArray(String[] s){
		int[] i=new int[s.length];
		for (int j = 0; j < i.length; j++) {
			i[j]=Integer.parseInt(s[j]);
		}
		return i;
	}
	
	/**
	 * Concatenate a string array into a string.
	 * 
	 * @param array is the string array
	 * @return a string that is the result of concatenation
	 */
	public static String stringArrayToString(String[] array){
		String s=array[0];
		for (int i = 1; i < array.length; i++) {
			s+=array[i];
		}
		return s;
	}
	
	/**
	 * Non regex method to split a string as opposed to the {@link String#split(String)} method
	 * 
	 * @param str is the string to be split
	 * @param separator	is the separator
	 * @param start is the start position to begin looking. If start>1 then the first position of the return array is the string up to the start position even if it has the separator in it
	 * @param end is the end position to stop looking. If end<str.length then the last position of the return array is the string from end to the length of the string even if it has the separator in it
	 * @return a string array
	 * @throws IndexOutOfBoundsException if end<start or start<0 or end>length of string
	 */
	public static String[] stringToStringArray(String str,String separator,int start,int end) {//throws IndexOutOfBoundsException{
//		if(end<start || start<0 || end>str.length()) throw new IndexOutOfBoundsException();
		ArrayList<String> a=new ArrayList<>();
		if(!"".equals(str)){
			int prev=start;
			int pos=0;
			while (prev<end) {
				//find the next separator
				pos=str.indexOf(separator, prev);
				if(pos<0){
					//there are no more separators so break;
					break;
				}
				//if pos>0 add the previous string (from prev to pos) to the array
				a.add(str.substring(prev, pos));
				//increment the counter for the next search
				prev=pos+separator.length();
			}
			//add the final part of the string
			a.add(str.substring(prev));
		}
		String[] aa=new String[0];
		return a.toArray(aa);
	}
	
	/**
	 * This method converts a long into a byte array. It extends the java data stream
	 * method {@link DataOutputStream#writeLong(long)} to return longs of arbitrary
	 * size but it returns them instead of actually writing them to a stream.
	 * If longSize is not big enough to hold the long, the number will be cropped.
	 * If longSize is bigger than required to hold the long, it will be filled with
	 * zeros to the left.
	 * This method performs the reverse operation of {@link #byteArrayToLong(byte[], int, int)}.
	 * 
	 * @param lo is the long to split into bytes
	 * @param longSize is the intended number of bytes
	 * @return a byte array with the decomposition of the long in bytes
	 */
	public static byte[] longToByteArray(final long lo,final int longSize){
		byte[] b=new byte[longSize];
		long o=lo;
		long a=0;
		for (int i = longSize-1; i >= 0; i--) {
			a=o%256;
			if(a>=128)a=a-256;
			b[i]=(byte) a;
			o=o/256;
		}
		return b;
	}
	
	/**
	 * This method performs the reverse operation of {@link #longToByteArray(long, int)}.
	 * It extends the java data stream method {@link DataInputStream#readLong()} to 
	 * return longs of arbitrary size but it returns them instead of actually reading 
	 * them from a stream.
	 * 
	 * @see {@link #longToByteArray(long, int)}
	 * 
	 * @param b is the byte array that contains the long and, possibly, other data
	 * @param startIndex is the position in the array of the leftmost byte of the long
	 * @param longSize is the number of bytes to convert to a long
	 * @return a long value
	 */
	public static long byteArrayToLong(final byte[] b, final int startIndex,final int longSize){
		long o=0;
		for (int i = 0; i <longSize; i++) {
			o=o*256+((b[startIndex+i]>=0)?b[startIndex+i]:b[startIndex+i]+256);
		}
		return o;
	}
	
	/**
	 * Convert a byte array into a printable string by converting each numeric
	 * byte into its text equivalent.
	 * 
	 * @param i is the byte array
	 * @param separator is the separator to use between each byte
	 * @return the string
	 */
	public static String byteArrayToStringPrintable(byte[] i,String separator){
		String s="";
		for (int j = 0; j < i.length-1; j++) {
			s+=i[j]+separator;
		}
		s+=i[i.length-1];
		return s;
	}
	
	/**
	 * Transform an int array into a string with integers.
	 * 
	 * @param i is the integer array
	 * @param separator is the separator to use between each int
	 * @return the string
	 */
	public static String intArrayToStringPrintable(int[] i,String separator){
		String s="";
		for (int j = 0; j < i.length-1; j++) {
			s+=i[j]+separator;
		}
		s+=i[i.length-1];
		return s;
	}
	
	/**
	 * Transform a generic array into a printable string by converting the number 
	 * in each position of the array into its text equivalent.
	 * 
	 * @param i is the array
	 * @param separator is the separator to use between each position of the array
	 * @return the string
	 */
	public static <T> String arrayToStringPrintable(T[] i,String separator){
		String s="";
		for (int j = 0; j < i.length-1; j++) {
			s+=i[j]+separator;
		}
		s+=i[i.length-1];
		return s;
	}
		
	/**
	 * Transform an {@link Iterable} into a printable string by converting the number 
	 * in each position into its text equivalent.
	 * 
	 * @param i is the {@link Iterable} object
	 * @param separator is the separator to use between each position of the array
	 * @return the string
	 */
	public static <T> String iterableToStringPrintable(Iterable<T> i,String separator){
		String s="";
		boolean b=true;
		Iterator<T> j = i.iterator();
		while(j.hasNext()) {
			if(b)b=false;
			else s+=separator;
			s+=j.next();
		}
		return s;
	}
		
	/**
	 * Convert an int array into a {@link Integer} array.
	 * 
	 * @param b is the array to convert
	 * @return the converted array
	 */
	public static Integer[] intArrayToIntegerArray(int[] a) {
		Integer s[]=new Integer[a.length];
		for(int i=0;i<a.length;++i) {s[i]=a[i];}
		return s;
	}

	/**
	 * Convert a long array into a {@link Long} array.
	 * 
	 * @param b is the array to convert
	 * @return the converted array
	 */
	public static Long[] longArrayToLongArray(long[] a) {
		Long s[]=new Long[a.length];
		for(int i=0;i<a.length;++i) {s[i]=a[i];}
		return s;
	}

	/**
	 * Convert a float array into a {@link Float} array.
	 * 
	 * @param b is the array to convert
	 * @return the converted array
	 */
	public static Float[] floatArrayToFloatArray(float[] a) {
		Float s[]=new Float[a.length];
		for(int i=0;i<a.length;++i) {s[i]=a[i];}
		return s;
	}

	/**
	 * Convert a double array into a {@link Double} array.
	 * 
	 * @param b is the array to convert
	 * @return the converted array
	 */
	public static Double[] doubleArrayToDoubleArray(double[] a) {
		Double s[]=new Double[a.length];
		for(int i=0;i<a.length;++i) {s[i]=a[i];}
		return s;
	}

}
