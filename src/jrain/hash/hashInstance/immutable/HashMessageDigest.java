package jrain.hash.hashInstance.immutable;


/**
 * @author poltergeist0
 * 
 * {@link MessageDigest} hash instance class.
 */
public class HashMessageDigest extends HashInstance implements jrain.hash.hashInstance.HashMessageDigest{

	/**
	 * Calculate a hash
	 * 
	 * @param algorithm is the name of the algorithm
	 */
	public HashMessageDigest(String algorithm) {
		super(algorithm,jrain.hash.hashInstance.HashMessageDigest.instance(algorithm));
	}

	/**
	 * Load a hash
	 * 
	 * @param algorithm is the name of the algorithm
	 * @param message is the hash
	 */
	public HashMessageDigest(String algorithm,final byte[] message){
		super(algorithm,message);
	}

}

