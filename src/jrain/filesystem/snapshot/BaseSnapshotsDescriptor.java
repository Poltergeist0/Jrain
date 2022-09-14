package jrain.filesystem.snapshot;

import java.util.UUID;

import jrain.identifiable.Identifiable;

/**
 * @author poltergeist0
 * 
 * Base information descriptor for snapshots.
 * 
 * Defines name and size
 */
public interface BaseSnapshotsDescriptor extends Identifiable<UUID>{
		

	/**
	 * String used in conjunction with the toString() method to represent the
	 * name part of a {@link BaseSnapshotsDescriptor}.
	 */
	public static final String tagName="name";

	/**
	 * String used in conjunction with the toString() method to represent the
	 * size part of a {@link BaseSnapshotsDescriptor}.
	 */
	public static final String tagSize="size";
	
	/**
	 * Default name
	 */
	public static final String defaultName="DEFAULT";
	
	/**
	 * Default value
	 */
	public static final Long defaultSize=0L;

	/**
	 * @return the name of the descriptor
	 */
	public String getDescriptorName();
	
	/**
	 * @return the size of the descriptor
	 */
	public Long getDescriptorSize();
	
}
