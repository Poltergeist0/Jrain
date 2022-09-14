package jrain.hash.hashInstance.immutable;


/**
 * @author poltergeist0
 * 
 * XOR hash instance class.
 */
public class HashXOR extends HashInstance implements jrain.hash.hashInstance.HashXOR{
	
	/**
	 * Calculate a hash
	 * 
	 * @param algorithm is the name of the algorithm
	 */
	public HashXOR(String algorithm) {
		super(algorithm,jrain.hash.hashInstance.HashXOR.Instance(algorithm));
	}

	/**
	 * Load a hash
	 * 
	 * @param algorithm is the name of the algorithm
	 * @param message is the hash
	 */
	public HashXOR(String algorithm,final byte[] message){
		super(algorithm,message);
	}
}

