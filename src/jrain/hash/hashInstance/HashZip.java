package jrain.hash.hashInstance;

import java.util.Set;
import java.util.zip.Adler32;
import java.util.zip.CRC32;

import jrain.polyType.hash.immutable.Adler32Poly;
import jrain.polyType.hash.immutable.CRC32Poly;
import jrain.polyType.hash.immutable.PolyHash;

/**
 * @author poltergeist0
 *
 * Interface that must be implemented by classes that represent a java.util.zip 
 * hash instance.
 * 
 * Defines methods to check if a given hash is provided by java.util.zip.
 */
public interface HashZip extends HashInstance{

	/**
	 * @return a set with the names of the hash algorithms provided by java.util.zip
	 */
	public static Set<String> algorithms(){return Set.of("CRC32","Adler32");}

	/**
	 * @param a is the name of a hash algorithm
	 * @return true if java.util.zip provides that algorithm
	 */
	public static boolean hasAlgorithm(String a){return algorithms().contains(a);}

	/**
	 * Get a java.util.zip instance corresponding to the requested hash algorithm 
	 * or null if the hash algorithm is not provided by java.util.zip
	 * 
	 * @param s is the name of a hash algorithm
	 * @return a java.util.zip instance or null
	 */
	public static PolyHash<?> instance(String s) {
		switch (s) {
		case "CRC32":
			return new CRC32Poly(new CRC32());
		case "Adler32":
			return new Adler32Poly(new Adler32());
		default:
			return null;
		}
	}
	
}
