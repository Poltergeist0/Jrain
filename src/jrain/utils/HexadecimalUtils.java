package jrain.utils;

import java.util.Arrays;

import jrain.exceptions.ExceptionInvalidValue;

public class HexadecimalUtils {

	/**
	 * Convert a byte array to an half byte array. Each input byte is split into
	 * an half left part and half right part. Performs the inverse operation of
	 * {@link HexadecimalUtils#convertFromHexHalfByte(byte[])}
	 * 
	 * @param data
	 * @return a byte array with the half bytes
	 */
	public static byte[] convertToHexHalfByte(final byte[] data) {
		byte[] buf=new byte[data.length*2];
		int i=0;
        for (i = 0; i < data.length; i++) {
            buf[2*i]=(byte) ((data[i] >> 4) & 0x0F);
            buf[2*i+1]=(byte) (data[i] & 0x0F);
        }
        return buf;
    }
	
	/**
	 * Convert a byte array of half bytes to a byte array. Each two input bytes is
	 * concatenated to form a byte. Performs the inverse operation of
	 * {@link HexadecimalUtils#convertToHexHalfByte(byte[])}
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] convertFromHexHalfByte(final byte[] data) {
		byte[] buf = new byte[data.length/2];
		int i=0;
        for (i = 0; i < data.length/2; i++) {
        	buf[i]=(byte) (((data[i*2] << 4) & 0xF0) | (data[i*2+1] & 0x0F));
        }
        return buf;
    }
	
	/**
	 * Convert an half byte to a char so that it can be user readable.
	 * Performs the inverse operation of {@link HexadecimalUtils#convertCharToHalfByte(char)}
	 * 
	 * @param data
	 * @return
	 */
	public static char convertHalfByteToChar(final byte data) {
		if((data>=0) && (data <= 9)) return(char) ('0' + data);
		return (char) ('a' + (data - 10));
    }
	
	/**
	 * Convert a char (user readable) to an half byte.
	 * Performs the inverse operation of {@link HexadecimalUtils#convertHalfByteToChar(byte)}
	 * 
	 * @param data
	 * @return
	 * @throws ExceptionInvalidValue if the char does not belong to the range [0-9] or [a-f] or [A-F]
	 */
	public static byte convertCharToHalfByte(final char data) throws ExceptionInvalidValue {
		if(data>='0' && data <= '9') return (byte) (data-'0');
		if(data>='a' && data<='f') return (byte) (data-'a'+10);
		if(data>='A' && data<='F') return (byte) (data-'A'+10);
		throw new ExceptionInvalidValue("Invalid char ("+data+")");
    }
	
	/**
	 * Convert an half byte array to a char array so that it can be user readable.
	 * Performs the inverse operation of {@link HexadecimalUtils#convertCharToHalfByte(char[])}
	 * 
	 * @param data
	 * @return
	 */
	public static char[] convertHalfByteToChar(final byte[] data) {
		char[] c=new char[data.length];
        for (int i = 0; i < data.length; i++) {
            c[i]=convertHalfByteToChar(data[i]);
        }
        return c;
    }
	
	/**
	 * Convert a char (user readable) array to an half byte array.
	 * Performs the inverse operation of {@link HexadecimalUtils#convertHalfByteToChar(byte[])}
	 * 
	 * @param data
	 * @return
	 * @throws ExceptionInvalidValue if any the char does not belong to the range [0-9] or [a-f] or [A-F]
	 */
	public static byte[] convertCharToHalfByte(final char[] data) throws ExceptionInvalidValue {
		byte[] d=new byte[data.length];
		for (int i = 0; i < data.length; i++) {
            d[i]=convertCharToHalfByte(data[i]);
        }
        return d;
    }
	
	/**
	 * Performs the same operation as {@link #convertCharToHalfByte(char[])} but does
	 * not throw any exception. Instead it just ignores any char that can not be converted.
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] convertCharToHalfByteNoError(final char[] data){
		byte[] d=new byte[data.length];
		int di=0;
		for (int i = 0; i < data.length; i++) {
            try {
				d[di]=convertCharToHalfByte(data[i]);
				di++;
			} catch (ExceptionInvalidValue e) {
				//do nothing; this is used only to skip incrementing di
			}
        }
        return Arrays.copyOf(d, di);
    }
	
//	public static String convertToHex(final byte[] data,final int grouping,final String separator) {
//		char[] c=convertHalfByteToChar(convertToHexHalfByte(data));
//		StringBuffer buf = new StringBuffer();
//		int i=0;
//		int g=1;
//		for (i = 0; i < c.length; i++) {
//            buf.append(c[i]);
//            if(g==grouping){
//            	buf.append(separator);
//            	g=1;
//            }
//            else{
//            	g++;
//            }
//        }
//        return buf.toString();
//    }
	
	/**
	 * Convert a byte array to an hexadecimal string.
	 * 
	 * @param data
	 * @return
	 */
	public static String convertToHex(final byte[] data) {
		return new String(convertHalfByteToChar(convertToHexHalfByte(data)));
    }
	
	/**
	 * Convert an hexadecimal string to a byte array.
	 * 
	 * @param data
	 * @return
	 * @throws ExceptionInvalidValue if any the character does not belong to the range [0-9] or [a-f] or [A-F]
	 */
	public static byte[] convertFromHex(final String data) throws ExceptionInvalidValue {
		return convertFromHexHalfByte(convertCharToHalfByte(Conversion_NoChecks.stringToCharArray(data)));
    }
	
	/**
	 * Performs the same operation as {@link #convertFromHex(String)} but does
	 * not throw any exception. Instead it just ignores any character that can 
	 * not be converted.
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] convertFromHexNoError(final String data) {
		return convertFromHexHalfByte(convertCharToHalfByteNoError(Conversion_NoChecks.stringToCharArray(data)));
    }
	
	public static byte[] convertToBinary(final byte data){
		byte d[]=new byte[8];
		int b=data;
		for(int i=7;i>0;i--){
			d[i]=(byte) (b%2);
			b=b>>1;
		}
		return d;
	}
	
	public static byte[] convertToBinary(final byte[] data){
		byte d[]=new byte[data.length*8];
		int b;
		for(int j=data.length-1;j>=0;j--){
			b=data[j];
			for(int i=7;i>=0;i--){
				d[data.length*8+i]=(byte) (b%2);
				b=b>>1;
			}
		}
		return d;
	}
	
}
