package jrain.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jrain.exceptions.ExceptionInvalidValue;

/**
 * @author poltergeist0
 *
 * This class implements static methods that can be used to work with hexadecimals.
 */
public class HexadecimalUtils {

	/**
	 * Convert a byte array to an half byte array so that it is ready to convert to text.
	 * Each input byte is split into an half left part and half right part.
	 * Performs the inverse operation of {@link HexadecimalUtils#convertFromHexHalfByte(byte[])}.
	 * 
	 * @param data is the original array
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
	 * Same as {@link HexadecimalUtils#convertToHexHalfByte(byte[])} but with {@code List<Byte>} instead of a raw array.
	 * 
	 * Converts a {@code List<Byte>} an half byte List so that it is ready to convert to text.
	 * Each input byte is split into an half left part and half right part.
	 * Performs the inverse operation of {@link HexadecimalUtils#convertFromHexHalfByte(List)}.
	 * 
	 * @param data is the original array
	 * @return a byte array with the half bytes
	 */
	public static List<Byte> convertToHexHalfByte(final List<Byte> data) {
		ArrayList<Byte> buf=new ArrayList<Byte>(data.size()*2);
		int i=0;
        for (i = 0; i < data.size(); i++) {
        	buf.add((byte) ((data.get(i) >> 4) & 0x0F));
        	buf.add((byte) (data.get(i) & 0x0F));
        }
        return buf;
    }
	
	/**
	 * Convert a byte array of half bytes to a byte array. Each two input bytes are
	 * concatenated to form a byte. Performs the inverse operation of
	 * {@link HexadecimalUtils#convertToHexHalfByte(byte[])}
	 * 
	 * @param data is a byte array with the half bytes
	 * @return a byte array with the bytes
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
	 * Same as {@link HexadecimalUtils#convertFromHexHalfByte(byte[])} but with {@code List<Byte>} instead of a raw array.
	 * 
	 * Convert a byte List of half bytes to a byte List. Each two input bytes are
	 * concatenated to form a byte. Performs the inverse operation of
	 * {@link HexadecimalUtils#convertToHexHalfByte(byte[])}
	 * 
	 * @param data is a byte List with the half bytes
	 * @return a byte List with the bytes
	 */
	public static List<Byte> convertFromHexHalfByte(final List<Byte> data) {
		ArrayList<Byte> buf = new ArrayList<Byte>(data.size()/2);
		int i=0;
        for (i = 0; i < data.size()/2; i++) {
        	buf.add((byte) (((data.get(i*2) << 4) & 0xF0) | (data.get(i*2+1) & 0x0F)));
        }
        return buf;
    }
	
	/**
	 * Convert an half byte to a char so that it can be user readable.
	 * Performs the inverse operation of {@link HexadecimalUtils#convertCharToHalfByte(char)}
	 * 
	 * @param data is the half byte to convert
	 * @return a char with the corresponding hexadecimal value
	 */
	public static char convertHalfByteToChar(final byte data) {
		if((data>=0) && (data <= 9)) return(char) ('0' + data);
		return (char) ('a' + (data - 10));
    }
	
	/**
	 * Convert a char (user readable) to an half byte.
	 * Performs the inverse operation of {@link HexadecimalUtils#convertHalfByteToChar(byte)}
	 * 
	 * @param data is a char with the corresponding hexadecimal value
	 * @return the half byte
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
	 * @param data is the half byte array
	 * @return a char array with the corresponding hexadecimal values
	 */
	public static char[] convertHalfByteToChar(final byte[] data) {
		char[] c=new char[data.length];
        for (int i = 0; i < data.length; i++) {
            c[i]=convertHalfByteToChar(data[i]);
        }
        return c;
    }

	/**
	 * Same as {@link HexadecimalUtils#convertHalfByteToChar(byte[])} but with {@code List<Byte>} instead of a raw array.
	 * 
	 * Convert an half byte array to a char array so that it can be user readable.
	 * Performs the inverse operation of {@link HexadecimalUtils#convertCharToHalfByte(char[])}
	 * 
	 * @param data is the half byte List
	 * @return a char List with the corresponding hexadecimal values
	 */
	public static char[] convertHalfByteToChar(final List<Byte> data) {
		char[] c=new char[data.size()];
        for (int i = 0; i < data.size(); i++) {
            c[i]=convertHalfByteToChar(data.get(i));
        }
        return c;
    }
	
	/**
	 * Convert a char (user readable) array to an half byte array.
	 * Performs the inverse operation of {@link HexadecimalUtils#convertHalfByteToChar(byte[])}
	 * 
	 * @param data is a char array with the corresponding hexadecimal value
	 * @return a half byte array
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
	 * Same as {@link HexadecimalUtils#convertCharToHalfByte(byte[])} but with {@code List<Byte>} instead of a raw array.
	 * 
	 * Convert a char (user readable) array to an half byte array.
	 * Performs the inverse operation of {@link HexadecimalUtils#convertHalfByteToChar(byte[])}
	 * 
	 * @param data is a char array with the corresponding hexadecimal value
	 * @return a half byte array
	 * @throws ExceptionInvalidValue if any the char does not belong to the range [0-9] or [a-f] or [A-F]
	 */
	public static List<Byte> convertCharToHalfByteList(final char[] data) throws ExceptionInvalidValue {
		ArrayList<Byte> d=new ArrayList<Byte>(data.length);
		for (int i = 0; i < data.length; i++) {
            d.add(convertCharToHalfByte(data[i]));
        }
        return d;
    }
	
	/**
	 * Performs the same operation as {@link #convertCharToHalfByte(char[])} but does
	 * not throw any exception. Instead it just ignores any char that can not be converted.
	 * 
	 * @param data is a char array with the corresponding hexadecimal value
	 * @return a half byte array
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

	/**
	 * Performs the same operation as {@link #convertCharToHalfByteNoError(char[])} but does
	 * not throw any exception. Instead it just ignores any char that can not be converted.
	 * 
	 * @param data is a char List with the corresponding hexadecimal value
	 * @return a half byte List
	 */
	public static List<Byte> convertCharToHalfByteListNoError(final char[] data){
		ArrayList<Byte> d=new ArrayList<Byte>(data.length);
		for (int i = 0; i < data.length; i++) {
            try {
				d.add(convertCharToHalfByte(data[i]));
			} catch (ExceptionInvalidValue e) {
				//do nothing; this is used only to skip incrementing di
			}
        }
        return d;
    }
	
	/**
	 * Convert a byte array to an hexadecimal string.
	 * 
	 * @param data is a byte array
	 * @return a String with a readable version of the input
	 */
	public static String convertToHex(final byte[] data) {
		return new String(convertHalfByteToChar(convertToHexHalfByte(data)));
    }
	
	/**
	 * Convert a byte List to an hexadecimal string.
	 * 
	 * @param data is a byte List
	 * @return a String with a readable version of the input
	 */
	public static String convertToHex(final List<Byte> data) {
		return new String(convertHalfByteToChar(convertToHexHalfByte(data)));
    }
	
	/**
	 * Convert an hexadecimal string to a byte array.
	 * 
	 * @param data contains an hexadecimal string
	 * @return a byte array containing the converted string
	 * @throws ExceptionInvalidValue if any the character does not belong to the range [0-9] or [a-f] or [A-F]
	 */
	public static byte[] convertFromHex(final String data) throws ExceptionInvalidValue {
		return convertFromHexHalfByte(convertCharToHalfByte(Conversion_NoChecks.stringToCharArray(data)));
    }
	
	/**
	 * Convert an hexadecimal string to a byte List.
	 * 
	 * @param data contains an hexadecimal string
	 * @return a byte List containing the converted string
	 * @throws ExceptionInvalidValue if any the character does not belong to the range [0-9] or [a-f] or [A-F]
	 */
	public static List<Byte> convertFromHexToList(final String data) throws ExceptionInvalidValue {
		return convertFromHexHalfByte(convertCharToHalfByteList(Conversion_NoChecks.stringToCharArray(data)));
    }
	
	/**
	 * Performs the same operation as {@link #convertFromHex(String)} but does
	 * not throw any exception. Instead it just ignores any character that can 
	 * not be converted.
	 * 
	 * @param data contains an hexadecimal string
	 * @return a byte array containing the converted string
	 */
	public static byte[] convertFromHexNoError(final String data) {
		return convertFromHexHalfByte(convertCharToHalfByteNoError(Conversion_NoChecks.stringToCharArray(data)));
    }

	/**
	 * Performs the same operation as {@link #convertFromHexToList(String)} but does
	 * not throw any exception. Instead it just ignores any character that can 
	 * not be converted.
	 * 
	 * @param data contains an hexadecimal string
	 * @return a byte List containing the converted string
	 */
	public static List<Byte> convertFromHexToListNoError(final String data) {
		return convertFromHexHalfByte(convertCharToHalfByteListNoError(Conversion_NoChecks.stringToCharArray(data)));
    }
	
	/**
	 * Convert a byte into a bit array.
	 * Each bit is stored in a byte so the result is returned in a byte array.
	 * 
	 * @param b is the byte to convert
	 * @return is the bit array
	 */
	public static byte[] convertByteToBinary(final byte data){
		return convertByteToBinary(data,(byte) 8);
	}
	
	/**
	 * Convert a byte into a bit List.
	 * Each bit is stored in a byte so the result is returned in a byte List.
	 * 
	 * @param b is the byte to convert
	 * @return is the bit List
	 */
	public static List<Byte> convertByteToBinaryList(final byte data){
		return convertByteToBinaryList(data,(byte) 8);
	}

	/**
	 * Convert a byte into an existing bit array.
	 * Each bit is stored in a byte so the result is returned in a byte array.
	 * 
	 * @param data is the byte to convert
	 * @param dest is the destination bit array
	 * @param destMSB is the position of the most significant bit of the given byte in the destination bit array
	 * @param destLSB is the position of the least significant bit of the given byte in the destination bit array
	 * @return the destination array
	 */
	public static byte[] convertByteToBinary(final byte data, byte[] dest, int destMSB, int destLSB){
		if(Math.abs(destMSB-destLSB)>=8) return null;
		int b=data;
		if(destMSB>=destLSB) {//MSB stored in a higher index than LSB
			//must go from index LSB to MSB
			for(int i=destLSB;i<=destMSB;i++){
				dest[i]=(byte) Math.abs(b%2);
				b=b>>1;
			}
		}
		else {
			for(int i=destLSB;i>=destMSB;i--){
				dest[i]=(byte) Math.abs(b%2);
				b=b>>1;
			}
		}
		return dest;
	}

	/**
	 * Convert a byte into an existing bit List.
	 * Each bit is stored in a byte so the result is returned in a byte List.
	 * 
	 * @param data is the byte to convert
	 * @param dest is the destination bit List
	 * @param destMSB is the position of the most significant bit of the given byte in the destination bit List
	 * @param destLSB is the position of the least significant bit of the given byte in the destination bit List
	 * @return the destination List
	 */
	public static List<Byte> convertByteToBinary(final byte data, List<Byte> dest, int destMSB, int destLSB){
		if(Math.abs(destMSB-destLSB)>=8) return null;
		int b=data;
		if(destMSB>=destLSB) {//MSB stored in a higher index than LSB
			//must go from index LSB to MSB
			for(int i=destLSB;i<=destMSB;i++){
				dest.set(i, (byte) Math.abs(b%2));
				b=b>>1;
			}
		}
		else {
			for(int i=destLSB;i>=destMSB;i--){
				dest.set(i, (byte) Math.abs(b%2));
				b=b>>1;
			}
		}
		return dest;
	}

	/**
	 * Convert a portion of a byte into a bit array.
	 * Each bit is stored in a byte so the result is returned in a byte array.
	 * 
	 * @param data is the byte to convert
	 * @param numBits is the number of bits to convert
	 * @return the destination array
	 */
	public static byte[] convertByteToBinary(final byte data, byte numBits){
		//TODO refactor to indicate which bits. Maybe MSB/LSB position
		if(numBits<=0 || numBits>8) return null;
		byte d[]=new byte[numBits];
		return convertByteToBinary(data, d, 0, numBits-1);
	}

	/**
	 * Convert a portion of a byte into a bit List.
	 * Each bit is stored in a byte so the result is returned in a byte List.
	 * 
	 * @param data is the byte to convert
	 * @param numBits is the number of bits to convert
	 * @return the destination List
	 */
	public static List<Byte> convertByteToBinaryList(final byte data, byte numBits){
		if(numBits<=0 || numBits>8) return null;
		ArrayList<Byte> d=new ArrayList<>(numBits);
		for (int i = 0; i < numBits; i++) {
			d.add(null);
		}
		return convertByteToBinary(data, d, 0, numBits-1);
	}

	/**
	 * Convert a byte array into a bit array. Same as {@link #convertByteToBinary(byte)} but
	 * for multiple bytes.
	 * Bits have the same order as the given bytes.
	 * 
	 * @param b is the byte array
	 * @return a bit array
	 */
	public static byte[] convertByteToBinary(final byte[] data){
		byte d[]=new byte[data.length*8];
		for(int j=data.length-1;j>=0;j--){
			convertByteToBinary(data[j], d, j*8, j*8+7);
		}
		return d;
	}

	/**
	 * Convert a byte List into a bit List. Same as {@link #convertByteToBinaryList(byte)} but
	 * for multiple bytes.
	 * Bits have the same order as the given bytes.
	 * 
	 * @param b is the byte List
	 * @return a bit List
	 */
	public static List<Byte> convertByteToBinary(final List<Byte> data){
		ArrayList<Byte> d=new ArrayList<Byte>(data.size()*8);
		for(int j=data.size()-1;j>=0;j--){
			convertByteToBinary(data.get(j), d, j*8, j*8+7);
		}
		return d;
	}
	
	/**
	 * Reverse operation of {@link #convertByteToBinary(byte)}.
	 * 
	 * @param data the bit array with the bits of the byte
	 * @return the byte
	 */
	public static byte convertBinaryToByte(final byte[] data){
		byte d=0;
		for(int j=0;j<data.length && j<8;j++){
			d=(byte) (d*2+data[j]);
		}
		return d;
	}

	/**
	 * Reverse operation of {@link #convertByteToBinaryList(byte)}.
	 * 
	 * @param data the bit List with the bits of the byte
	 * @return the byte
	 */
	public static byte convertBinaryToByte(final List<Byte> data){
		byte d=0;
		for(int j=0;j<data.size() && j<8;j++){
			d=(byte) (d*2+data.get(j));
		}
		return d;
	}
	
	/**
	 * Reverse operation of {@link #convertByteToBinary(byte[])}.
	 * 
	 * @param data the bit array with the bits of the bytes
	 * @return an array with the bytes
	 */
	public static byte[] convertBinaryToByteArray(final byte[] data){
		byte d[]=new byte[(int) Math.ceil(data.length/8.0)];
		int cnt=data.length-1;
		for(int j=d.length-1;j>=0;j--){
			for(int i=0;i<8 && cnt>=0 ;i++){
				d[j]=(byte) (d[j]+data[cnt]*Math.pow(2, i));
				--cnt;
			}
		}
		return d;
	}

	/**
	 * Reverse operation of {@link #convertByteToBinary(List)}.
	 * 
	 * @param data the bit List with the bits of the bytes
	 * @return a List with the bytes
	 */
	public static List<Byte> convertBinaryToByteArray(final List<Byte> data){
		ArrayList<Byte> d=new ArrayList<>((int) Math.ceil(data.size()/8.0));
		for (int i = 0; i < (int) Math.ceil(data.size()/8.0); i++) {
			d.add((byte) 0);
		}
		int cnt=data.size()-1;
		for(int j=d.size()-1;j>=0;j--){
			for(int i=0;i<8 && cnt>=0 ;i++){
				d.set(j, (byte) (d.get(j)+data.get(cnt)*Math.pow(2, i)));
				--cnt;
			}
		}
		return d;
	}
	
}
