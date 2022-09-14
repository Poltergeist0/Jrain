package jrain.hash.hashInstance.immutable;


/**
 * @author poltergeist0
 * 
 * java.util.zip hash instance class.
 */
public class HashZip extends HashInstance implements jrain.hash.hashInstance.HashZip{
	
	/**
	 * Calculate a hash
	 * 
	 * @param algorithm is the name of the algorithm
	 */
	public HashZip(String algorithm) {
		super(algorithm,jrain.hash.hashInstance.HashZip.instance(algorithm));
	}

	/**
	 * Load a hash
	 * 
	 * @param algorithm is the name of the algorithm
	 * @param message is the hash
	 */
	public HashZip(String algorithm,final byte[] message){
		super(algorithm,message);
	}
}

