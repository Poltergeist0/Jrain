package jrain.utils;

public class ByteArrayUtils {

	/**
	 * check if the sections of two byte arrays have the same data
	 * 
	 * @param a
	 * @param aStart
	 * @param aEnd
	 * @param b
	 * @param bStart
	 * @param bEnd
	 * @return
	 */
	public static final boolean equals(final byte[] a,final int aStart,final int aEnd,final byte[] b,final int bStart,final int bEnd){
		if(a!=null && b!=null){
			if(aStart>=0 && aEnd>=0 && aStart<=aEnd && bStart>=0 && bEnd>=0 && bStart<=bEnd && aEnd-aStart==bEnd-bStart){
				for (int i = 0; i < aEnd-aStart; i++) {
					if(a[aStart+i]!=b[bStart+i]) return false;
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * the same as equals but also returns true if both arrays are null
	 * 
	 * @param a
	 * @param aStart
	 * @param aEnd
	 * @param b
	 * @param bStart
	 * @param bEnd
	 * @return
	 */
	public static final boolean equalsNull(final byte[] a,final int aStart,final int aEnd,final byte[] b,final int bStart,final int bEnd){
		if(a==null && b==null) return true;
		return equals(a, aStart, aEnd, b, bStart, bEnd);
	}
	
	/**
	 * Replace data in the replaceIn byte array with the data in the replaceWith byte array.
	 * The original replaceIn data is modified so caution is advised if the original data
	 * is to be kept.
	 * 
	 * @param replaceIn
	 * @param replaceInStart
	 * @param replaceWith
	 * @param replaceWithStart
	 * @param replaceWithLength
	 * @return
	 */
	public static final byte[] replace(byte[] replaceIn, final int replaceInStart, final byte[] replaceWith, final int replaceWithStart, final int replaceWithLength){
		if(replaceIn!=null && replaceWith!=null){
			if(replaceInStart>=0 && replaceInStart+replaceWithLength<=replaceIn.length && replaceWithStart>=0 && replaceWithStart+replaceWithLength<=replaceWith.length){
				for (int i = 0; i < replaceWithLength; i++) {
					replaceIn[replaceInStart+i]=replaceWith[replaceWithStart+i];
				}
			}
		}
		return replaceIn;
	}
	
	/**
	 * Concatenates two byte arrays into a new byte array.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static final byte[] concatenate(final byte[] a, final byte[] b){
		byte[] c=new byte[a.length+b.length];
		int i=0;
		while (i<a.length) {
			c[i]=a[i];
			i++;
		}
		while(i<c.length){
			c[i]=b[i-a.length];
			i++;
		}
		return c;
	}
	
	/**
	 * Calculate the integer base2 logarithm of the given data. The value is rounded up
	 * to the closest integer.
	 * 
	 * @param data is an array of bytes
	 * @param dataStart is the first element of the array to read
	 * @param dataLength is the number of elements of the data to read starting at dataStart
	 * @return a long with the result
	 */
	public static final long log2(final byte[] data, final int dataStart, final int dataLength){
		long exp=-1;
		int i=dataStart;
		int dataEnd=i+dataLength-1;
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
	 * Calculate the integer base2 logarithm of the given data. The value is rounded up
	 * to the closest integer.
	 * This is the binary version where each byte element of the data contains only a zero
	 * or a one
	 * 
	 * @param data is an array of bits
	 * @param dataStart is the first element of the array to read
	 * @param dataLength is the number of elements of the data to read starting at dataStart
	 * @return a long with the result
	 */
	public static final long log2b(final byte[] data, final int dataStart, final int dataLength){
		int i=dataStart;
		int dataEnd=i+dataLength-1;
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
