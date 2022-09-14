package jrain.filesystem.snapshot.snapshotTree;

import java.util.Set;

import jrain.filesystem.snapshot.snapshotTree.immutable.SnapshotNode;
import jrain.identifiable.immutable.Identifiable;
import jrain.treeIdentifiable.immutable.TreeNode;
import jrain.treeIdentifiable.TreeIdentifiable;


/**
 * @author poltergeist0
 *
 * Defines additional methods that a snapshot tree must implement besides the
 * ones from {@link TreeIdentifiable}
 */
public interface SnapshotTree{
	
	/**
	 * This method returns the leaf nodes of the tree that are files
	 * @return
	 */
	public Set<TreeNode<SnapshotNode, Identifiable>> getFiles();

}
