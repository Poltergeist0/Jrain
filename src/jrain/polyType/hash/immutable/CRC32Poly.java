package jrain.polyType.hash.immutable;

import java.util.zip.CRC32;

import jrain.utils.Conversion_NoChecks;

/**
 * @author poltergeist0
 *
 * Polymorphic hash data type for {@link CRC32} hash instance.
 */
public class CRC32Poly extends PolyHash<CRC32>{

	/**
	 * Constructor for {@link CRC32}
	 * 
	 * @param value is the {@link CRC32} instance
	 */
	public CRC32Poly(CRC32 value) { super(value);}

	/**
	 * Copy constructor
	 * 
	 * @param poly is the original object to be copied
	 * @param deepCopy if true makes a deep copy of the object, if possible. False makes a shallow copy
	 */
	public CRC32Poly(CRC32Poly poly,boolean deepCopy) { super(poly,false);}
	
	@Override
	public CRC32Poly deepCopy() {
		return new CRC32Poly(this,false);
	}
	
	public void update(byte[] b, int offset, int len){
		super.get().update(b,offset,len);
	}

	public byte[] digest(){
		return Conversion_NoChecks.longToByteArray(super.get().getValue(),4);
	}

}
