package jrain.filesystem.snapshot;

/**
 * @author poltergeist0
 * 
 * Snapshots group descriptor.
 * 
 * Extends {@link BaseSnapshotsDescriptor}.
 * 
 * Defines additional fields: filename (where the group is saved)
 */
public interface SnapshotsGroupDescriptor extends BaseSnapshotsDescriptor{
	
	/**
	 * Default file name
	 */
	public static final String defaultSnapshotsGroupFileName="default.txt";
	
	/**
	 * String used in conjunction with the toString() method to represent a 
	 * {@link SnapshotsGroupDescriptor}.
	 */
	public static final String tagSnapshotsGroup="snapshotsgroup";

	/**
	 * String used in conjunction with the toString() method to represent the
	 * file name part of a {@link SnapshotsGroupDescriptor}.
	 */
	public static final String tagSnapshotsGroupFileName="filename";
	
	/**
	 * @return the filename where this group is saved on disk
	 */
	public String getFilename();

}
