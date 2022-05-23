package jrain.treeIdentifiable;

import java.util.Set;
import java.util.UUID;

import jrain.identifiable.immutable.Identifiable;

/**
 * @author poltergeist0
 *
 * This interface defines methods that must be implemented by a tree node class 
 * and/or all its descendants.
 * 
 * Nodes are identified by their {@link Identifiable}.
 *  
 * @param <TYPE_NODE_DATA> is a class that implements the data of a tree node
 * @param <TYPE_NODE_LINK> is a class that implements the links between tree nodes
 */
public interface TreeNode<TYPE_NODE_DATA, TYPE_NODE_LINK extends Identifiable> extends jrain.identifiable.Identifiable<UUID>{
	
	/**
	 * @return the {@link Identifiable} of the parent node, or null if there is no parent
	 */
	public TYPE_NODE_LINK getParent();
	
	/**
	 * @return the current node
	 */
	public TYPE_NODE_DATA getData();
	
	/**
	 * @return the multiplier toward its parent node
	 */
	public double getMultiplier();
	
	/**
	 * @return the offset toward its parent node
	 */
	public double getOffset();
	
	/**
	 * @return a set with the {@link Identifiable} of the child nodes
	 */
	public Set<TYPE_NODE_LINK> getChildren();
	
	/**
	 * @return true if there is no parent node
	 */
	public boolean isTopNode();
	
}