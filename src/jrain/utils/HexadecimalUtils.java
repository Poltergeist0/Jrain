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
	public static String convertToHex(final List<Byte> data) {
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
	public static List<Byte> convertFromHexToList(final String data) throws ExceptionInvalidValue {
		return convertFromHexHalfByte(convertCharToHalfByteList(Conversion_NoChecks.stringToCharArray(data)));
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
//	public static byte[] byteToBit(byte b){
//		byte[] a=new byte[8];
//		a[0]=(byte) (b>>7 & 0x01);
//		a[1]=(byte) (b>>6 & 0x01);
//		a[2]=(byte) (b>>5 & 0x01);
//		a[3]=(byte) (b>>4 & 0x01);
//		a[4]=(byte) (b>>3 & 0x01);
//		a[5]=(byte) (b>>2 & 0x01);
//		a[6]=(byte) (b>>1 & 0x01);
//		a[7]=(byte) (b & 0x01);
//		return a;
//	}
	public static byte[] convertByteToBinary(final byte data){
//		byte d[]=new byte[8];
//		int b=data;
//		for(int i=7;i>0;i--){
//			d[i]=(byte) (b%2);
//			b=b>>1;
//		}
//		return d;
		return convertByteToBinary(data,(byte) 8);
	}
	public static List<Byte> convertByteToBinaryList(final byte data){
		return convertByteToBinaryList(data,(byte) 8);
	}

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

	public static byte[] convertByteToBinary(final byte data, byte numBits){
		if(numBits<=0 || numBits>8) return null;
		byte d[]=new byte[numBits];
//		int b=data;
//		for(int i=numBits-1;i>=0;i--){
//			d[i]=(byte) (b%2);
//			b=b>>1;
//		}
//		return d;
		return convertByteToBinary(data, d, 0, numBits-1);
	}
	public static List<Byte> convertByteToBinaryList(final byte data, byte numBits){
		if(numBits<=0 || numBits>8) return null;
		ArrayList<Byte> d=new ArrayList<>(numBits);
		for (int i = 0; i < numBits; i++) {
			d.add(null);
		}
		return convertByteToBinary(data, d, 0, numBits-1);
	}

	/**
	 * Convert a byte array into a bit array. Same as {@link #byteToBit(byte)} but
	 * for multiple bytes.
	 * Bits have the same order as the given bytes.
	 * 
	 * @param b is the byte array
	 * @return a bit array
	 */
//	public static byte[] byteArrayToBitArray(byte[] b){
//		byte[] a=new byte[b.length*8];
//		for (int i = 0; i < b.length; i++) {
//			ByteArrayUtils.replace(a, i*8, byteToBit(b[i]), 0, 7);//replace call writes bits to proper location
//		}
//		return a;
//	}
	//TODO: check which of these two (above and below) are faster and remove the other
	public static byte[] convertByteToBinary(final byte[] data){
		byte d[]=new byte[data.length*8];
//		int b;
		for(int j=data.length-1;j>=0;j--){
//			b=data[j];
//			for(int i=7;i>=0;i--){
//				d[j*8+i]=(byte) (b%2);
//				b=b>>1;
//			}
			convertByteToBinary(data[j], d, j*8, j*8+7);
		}
		return d;
	}
	public static List<Byte> convertByteToBinary(final List<Byte> data){
		ArrayList<Byte> d=new ArrayList<Byte>(data.size()*8);
		for(int j=data.size()-1;j>=0;j--){
			convertByteToBinary(data.get(j), d, j*8, j*8+7);
		}
		return d;
	}
	
	public static byte convertBinaryToByte(final byte[] data){
//		if(data.length>8) return (Byte) null;
		byte d=0;
		for(int j=0;j<data.length && j<8;j++){
			d=(byte) (d*2+data[j]);
		}
		return d;
	}
	public static byte convertBinaryToByte(final List<Byte> data){
//		if(data.length>8) return (Byte) null;
		byte d=0;
		for(int j=0;j<data.size() && j<8;j++){
			d=(byte) (d*2+data.get(j));
		}
		return d;
	}
	
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
	public static List<Byte> convertBinaryToByteArray(final List<Byte> data){
//		byte d[]=new byte[(int) Math.ceil(data.length/8.0)];
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
