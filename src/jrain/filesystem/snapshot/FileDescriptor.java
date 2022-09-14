package jrain.filesystem.snapshot;

import jrain.hash.hashInstance.hashes.immutable.Hashes;

/**
 * @author poltergeist0
 *
 * File descriptor.
 * 
 * Extends {@link DirectoryDescriptor}.
 * 
 * Defines additional fields: hashes
 */
public interface FileDescriptor extends DirectoryDescriptor{

	/**
	 * String used in conjunction with the toString() method to represent a
	 * {@link FileDescriptor}.
	 */
	public static final String tagFile="file";
	
	/**
	 * @return the hashes that were calculated for the current file
	 */
	public Hashes getHashes();

}
