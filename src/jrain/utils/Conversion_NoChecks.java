package jrain.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Iterator;



/**
 * @author Lu�s Lemos
 *
 * This class implements static methods that can be used to convert from one data
 * type to another.
 */
public class Conversion_NoChecks {

	/**
	 * This method converts a byte array into an integer array. This is required since java does not
	 * support assigning nor casting byte arrays to integer arrays
	 * 
	 * @author Lu�s Lemos
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
	
	public static Byte[] byteArrayToByteArray(byte[] b){
		Byte[]c=new Byte[b.length];
		for (int i = 0; i < c.length; i++) {
			c[i]=b[i];
		}
		return c;
	}
	
	public static byte[] ByteArrayTobyteArray(Byte[] b){
		byte[]c=new byte[b.length];
		for (int i = 0; i < c.length; i++) {
			c[i]=b[i].byteValue();
		}
		return c;
	}
	
	public static byte[] intArrayToByteArray(final int[] a){
		byte[]c=new byte[a.length];
		for (int i = 0; i < c.length; i++) {
			c[i]=(byte) a[i];
		}
		return c;
	}
	
	/**
	 * This method converts a String into a char array. This is required since java does not
	 * have a method to return char arrays out of strings
	 * 
	 * @author Lu�s Lemos
	 * 
	 * @param b is the byte array
	 * @return the integer array
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
	 * This method transforms a string into an integer array so that it can be processed 
	 * by the obfuscation methods.
	 * Basically, each character in the string is turned into an integer
	 * It is the opposite operation of {@link #intArrayToString(int[])}
	 * 
	 * @author Lu�s Lemos
	 * 
	 * @param s is the string to obfuscate
	 * @return is the string represented as an integer array
	 */
	public static int[] stringToIntArray(String s){
		byte[] b=s.getBytes();
		//in java byte arrays can not be directly converted to integer arrays so another array is required
		return byteArrayToIntArray(b);
	}
	
	/**
	 * This method transforms an integer array into a string. It assumes that each array position contains
	 * an integer between zero and 255 (inclusive) or, in other words, an integer corresponding to a byte.
	 * If that is not the case, the method returns an unexpected string.
	 * It is the opposite operation of {@link #stringToIntArray(String)}
	 * 
	 * @author Lu�s Lemos
	 * 
	 * @param a is the integer array with each position holding the integer equivalent of a byte
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
	 * This method converts a string array with integers in each position into an integer 
	 * array (parses an integer from each position of the string array).
	 * It is useful for reading integer arrays from the main method arguments.
	 * 
	 * @author Lu�s Lemos
	 * 
	 * @param s is the string array with the integers in each position
	 * @return an integer array with the integers
	 */
	public static int[] intStringArrayToIntArray(String[] s){
		int[] i=new int[s.length];
		for (int j = 0; j < i.length; j++) {
			i[j]=Integer.parseInt(s[j]);
		}
		return i;
	}
	
	/**
	 * This method concatenates a string array into a string
	 * 
	 * @param array
	 * @return
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
	 * @param str	is the string to be split
	 * @param separator	is the separator
	 * @param start is the start position to begin looking. If start>1 then the first position of the return array is the string up to the start position even if it has the separator in it
	 * @param end is the end position to stop looking. If end<str.length then the last position of the return array is the string from end to the length of the string even if it has the separator in it
	 * @return
	 * @throws IndexOutOfBoundsException if end<start or start<0 or end>length of string
	 */
	public static String[] stringToStringArray(String str,String separator,int start,int end)throws IndexOutOfBoundsException{
		if(end<start || start<0 || end>str.length()) throw new IndexOutOfBoundsException();
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
	 * @param lo
	 * @param longSize
	 * @return
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
	 * @return
	 */
	public static long byteArrayToLong(final byte[] b, final int startIndex,final int longSize){
		long o=0;
		for (int i = 0; i <longSize; i++) {
			o=o*256+((b[startIndex+i]>=0)?b[startIndex+i]:b[startIndex+i]+256);
		}
		return o;
	}
	
	/**
	 * Convert a byte into a bit array. Each bit is stored in a byte so the result is
	 * returned in a byte array.
	 * 
	 * @param b
	 * @return
	 */
	public static byte[] byteToBit(byte b){
		byte[] a=new byte[8];
		a[0]=(byte) (b>>7 & 0x01);
		a[1]=(byte) (b>>6 & 0x01);
		a[2]=(byte) (b>>5 & 0x01);
		a[3]=(byte) (b>>4 & 0x01);
		a[4]=(byte) (b>>3 & 0x01);
		a[5]=(byte) (b>>2 & 0x01);
		a[6]=(byte) (b>>1 & 0x01);
		a[7]=(byte) (b & 0x01);
		return a;
	}
	
	/**
	 * Convert a byte array into a bit array. Bits have the same order as the given bytes.
	 * 
	 * @param b
	 * @return
	 */
	public static byte[] byteArrayToBitArray(byte[] b){
		byte[] a=new byte[b.length*8];
		for (int i = 0; i < b.length; i++) {
			ByteArrayUtils.replace(a, i*8, byteToBit(b[i]), 0, 8);
		}
		return a;
	}
	
	/**
	 * Convert a byte array into a printable string
	 * 
	 * @param i
	 * @param separator
	 * @return
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
	 * This method transforms an integer array into a string with integers so that it can be printed to console
	 * 
	 * @author Lu�s Lemos
	 * 
	 * @param i is the integer array
	 * @return the ready to print string
	 */
	public static String intArrayToStringPrintable(int[] i,String separator){
		String s="";
		for (int j = 0; j < i.length-1; j++) {
			s+=i[j]+separator;
		}
		s+=i[i.length-1];
		return s;
	}
	
	public static <T> String arrayToStringPrintable(T[] i,String separator){
		String s="";
		for (int j = 0; j < i.length-1; j++) {
			s+=i[j]+separator;
		}
		s+=i[i.length-1];
		return s;
	}
		
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
		
	public static Integer[] intArrayToIntegerArray(int[] a) {
		Integer s[]=new Integer[a.length];
		for(int i=0;i<a.length;++i) {s[i]=a[i];}
		return s;
	}

	public static Long[] longArrayToLongArray(long[] a) {
		Long s[]=new Long[a.length];
		for(int i=0;i<a.length;++i) {s[i]=a[i];}
		return s;
	}

	public static Float[] floatArrayToFloatArray(float[] a) {
		Float s[]=new Float[a.length];
		for(int i=0;i<a.length;++i) {s[i]=a[i];}
		return s;
	}

	public static Double[] doubleArrayToDoubleArray(double[] a) {
		Double s[]=new Double[a.length];
		for(int i=0;i<a.length;++i) {s[i]=a[i];}
		return s;
	}

}
