package jrain.hash.immutable;


/**
 * @author poltergeist0
 *
 * Interface that must be implemented by classes that calculate hashes.
 * 
 */
public interface Hash{
	
	/**
	 * Updates the digest using the specified array of bytes, starting at the 
	 * specified offset.
	 * 
	 * @param b is the array of bytes
	 * @param offset is the offset to start from in the array of bytes
	 * @param len is the number of bytes to use, starting at offset
	 */
	public void update(byte[] b, int offset, int len);

	/**
	 * Completes the hash computation by performing final operations such as 
	 * padding. The digest is reset after this call is made.
	 * 
	 * @return the array of bytes for the resulting hash value.
	 */
	public byte[] digest();

}
