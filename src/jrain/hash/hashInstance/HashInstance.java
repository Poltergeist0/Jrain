package jrain.hash.hashInstance;

import jrain.hash.immutable.Hash;

/**
 * @author poltergeist0
 *
 * Interface that must be implemented by classes that represent a hash instance.
 * 
 * Defines, among others, methods to check if the hash is ready or is still
 * being calculated.
 * 
 * All interfaces/classes extending/implementing this interface must define the
 * following three static methods:
 * 
 * 1) A method to return a set of names of the algorithms that the instance class implements
 * {@code public static Set<String> algorithms()}
 * 
 * 2) A method to evaluate if a given algorithm name is implemented by the instance class
 * {@code public static boolean hasAlgorithm(String a)}
 * 
 * 3) A method to return an instance of the instance class
 * {@code public static PolyHash<?> instance(String a)}
 * 
 */
public interface HashInstance extends Hash{
	
	/**
	 * Empty byte array definition
	 */
	public static final byte[] EMPTYBYTEARRAY=new byte[0];
	
	/**
	 * Definition of the name of the default hash to use when no name is provided
	 * to the constructor of a hash instance.
	 */
	public static final String DEFAULTHASH="";

	/**
	 * @return true if the object has a hash ready to be served and it is not the empty hash
	 */
	public boolean hasHash();

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode();

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj);

	/**
	 * @return true if the hash was calculated. False if it was loaded in the constructor
	 */
	public boolean wasCalculated();

	/**
	 * @return true if a hash is being calculated (the digest method was not yet called)
	 */
	public boolean isActive();

	/**
	 * @return the hash, if it is ready, or the empty hash if it is not ready
	 */
	public byte[] hash();

	/**
	 * @return the length of the hash or zero if the hash is being calculated.
	 */
	public int hashLength();
}
