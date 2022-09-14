package jrain.polyType.hash.immutable;

import java.security.MessageDigest;


/**
 * @author poltergeist0
 *
 * Polymorphic hash data type for {@link MessageDigest} hash instance.
 */
public class MessageDigestPoly extends PolyHash<MessageDigest> {

	/**
	 * Constructor for {@link MessageDigest}
	 * 
	 * @param value is the {@link MessageDigest} instance
	 */
	public MessageDigestPoly(MessageDigest value) { super(value);}
	
	/**
	 * Copy constructor
	 * 
	 * @param poly is the original object to be copied
	 * @param deepCopy if true makes a deep copy of the object, if possible. False makes a shallow copy
	 */
	public MessageDigestPoly(MessageDigestPoly poly,boolean deepCopy) { super(poly,false);}

	@Override
	public MessageDigestPoly deepCopy() {
		return new MessageDigestPoly(this,false);
	}

	public void update(byte[] b, int offset, int len){
		super.get().update(b,offset,len);
	}

	public byte[] digest(){
		return super.get().digest();
	}
	
}
