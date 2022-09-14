package jrain.filesystem.snapshot.snapshotTree;

import java.util.Set;

import jrain.filesystem.snapshot.BaseSnapshotsDescriptor;
import jrain.filesystem.snapshot.immutable.DirectoryDescriptor;
import jrain.filesystem.snapshot.immutable.FileDescriptor;
import jrain.filesystem.snapshot.immutable.SnapshotDescriptor;
import jrain.filesystem.snapshot.immutable.SnapshotsGroupDescriptor;
import jrain.identifiable.immutable.Identifiable;

/**
 * @author poltergeist0
 *
 * Stub class for file system snapshot descriptors.
 * Provides common storage for any node type.
 * The implemented methods call the corresponding methods in the file 
 * system descriptors themselves.
 */
public interface SnapshotNode extends BaseSnapshotsDescriptor{
	
	/**
	 * Types of file system descriptors.
	 * Currently are supported:
	 * 	- snapshots group
	 * 	- snapshot
	 * 	- directory
	 * 	- file
	 */
	public static enum DescriptorType {SNAPSHOTSGROUP,SNAPSHOT,DIRECTORY,FILE};
	
	/**
	 * Check if the current node can have the specified node as child.
	 * 
	 * Example: a directory descriptor can have a file descriptor as child but 
	 * not a snapshot.
	 * 
	 * @param <T> is the type of the class that extends {@link SnapshotNode}
	 * @param node is the given node to check
	 * @return true if the given node can be a child of the this node
	 */
	public <T extends SnapshotNode> boolean canHaveChild(T node);
	
	/**
	 * Check if the current node can be replaced by the given node.
	 * 
	 * Example: a directory descriptor can be replaced by a file descriptor but
	 * not by a snapshot.
	 * 
	 * @param <T> is the type of the class that extends {@link SnapshotNode}
	 * @param node is the given node
	 * @return true if the given node can replace this node
	 */
	public <T extends SnapshotNode> boolean canBeReplacedBy(T node);
	
	/**
	 * @return the node type as one of {@link DescriptorType}
	 */
	public DescriptorType getDescriptorType();
		
	/**
	 * @return the descriptor name
	 */
	public String getDescriptorName();
	
	/**
	 * @return the descriptor size
	 */
	public Long getDescriptorSize();
	
	/**
	 * @return the snapshot group descriptor or null if this node does not hold one
	 */
	public SnapshotsGroupDescriptor getSnapshotsGroupDescriptor();
	
	/**
	 * @return the snapshot descriptor or null if this node does not hold one
	 */
	public SnapshotDescriptor getSnapshotDescriptor();
	
	/**
	 * @return the directory descriptor or null if this node does not hold one
	 */
	public DirectoryDescriptor getDirectoryDescriptor();
	
	/**
	 * @return the file descriptor or null if this node does not hold one
	 */
	public FileDescriptor getFileDescriptor();
	
	/**
	 * @return the list of duplicates of this node
	 */
	public Set<Identifiable> getDuplicates();

	/**
	 * @return true if this node has been marked as currently being processed
	 */
	public boolean processing();
	
	/**
	 * @return true if this node has been marked as being online (new snapshot
	 * as opposed to having been loaded from a file)
	 */
	public boolean online();
	
	/**
	 * @return true if this node has been marked as deleted
	 */
	public boolean isDeleted();
	
}
