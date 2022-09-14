package jrain.filesystem.snapshot;

import java.util.Set;

/**
 * @author poltergeist0
 *
 * Snapshot descriptor.
 * 
 * Extends {@link BaseSnapshotsDescriptor}.
 * 
 * Defines additional fields: base path, recursion, calculate hashes, and 
 * paths to exclude and include from the base path.
 * 
 * Has two path filters. Filter Out excludes paths/files from the base path. 
 * Filter In includes paths/files from the directories stated in FilterOut. 
 * Example: base path is "user/documents", filter out is "readme.txt" (full path 
 * would be "user/documents/readme.txt") and ".git" (full path would be 
 * "user/documents/.git"), and filter in is ".git/madeUpFolder" (full path would 
 * be "user/documents/.git/madeUpFolder").
 */
public interface SnapshotDescriptor extends BaseSnapshotsDescriptor {
	
	/**
	 * Default base path
	 */
	public static final String defaultBasePath=".";
	
	/**
	 * Default recursion
	 */
	public static final Integer defaultRecursion=0;
	
	/**
	 * String used in conjunction with the toString() method to represent a 
	 * {@link SnapshotDescriptor}.
	 */
	public static final String tagSnapshot="snapshot";
	
	/**
	 * String used in conjunction with the toString() method to represent the
	 * base path part of a {@link SnapshotDescriptor}.
	 */
	public static final String tagSnapshotBasePath="basepath";
	
	/*
	 * This is prepended to the name of the hash
	 */
	public static final String tagSnapshotCalculate="calculate_";

	/**
	 * String used in conjunction with the toString() method to represent the
	 * recursion part of a {@link SnapshotDescriptor}.
	 */
	public static final String tagSnapshotRecursion="recursion";
	
	/**
	 * @return the base path of the snapshot
	 */
	public String getBasePath();

	/**
	 * @return the recursion depth of the snapshot
	 */
	public int getRecursion();

	/**
	 * @return the paths to filter out of the base path
	 */
	public Set<String> getFilterOut();

	/**
	 * @return the paths inside those filtered out to include in the snapshot
	 */
	public Set<String> getFilterIn();
	
}
