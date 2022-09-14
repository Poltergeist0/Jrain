package jrain.polyType.hash.immutable;

import java.util.zip.Adler32;

import jrain.utils.Conversion_NoChecks;


/**
 * @author poltergeist0
 *
 * Polymorphic hash data type for {@link Adler32} hash instance.
 */
public class Adler32Poly extends PolyHash<Adler32> {

	/**
	 * Constructor for {@link Adler32}
	 * 
	 * @param value is the {@link Adler32} instance
	 */
	public Adler32Poly(Adler32 value) { super(value);}

	/**
	 * Copy constructor
	 * 
	 * @param poly is the original object to be copied
	 * @param deepCopy if true makes a deep copy of the object, if possible. False makes a shallow copy
	 */
	public Adler32Poly(Adler32Poly poly,boolean deepCopy) { super(poly,false);}
	
	@Override
	public Adler32Poly deepCopy() {
		return new Adler32Poly(this,false);
	}

	public void update(byte[] b, int offset, int len){
		super.get().update(b,offset,len);
	}

	public byte[] digest(){
		return Conversion_NoChecks.longToByteArray(super.get().getValue(),4);
	}

}
