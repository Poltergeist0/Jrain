/*******************************************************************************
 * Copyright (C) 2026 poltergeist0
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Any libraries this program depends on have their own Licenses.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * LICENSE file for more details.
 ******************************************************************************/
package jrain.treeIdentifiable;

import java.util.Collection;
import java.util.Set;

import jrain.identifiable.immutable.Identifiable;

/**
 * @author poltergeist0
 *
 * This interface defines methods that must be implemented by a tree class and/or
 * all its descendants.
 *  
 * Each node in the tree is connected to its parent and have a weight associated.
 * The weight is a formula of type N*multiplier+offset where N is either the 
 * weight of the node or the branch that connects it to its parent.
 * 
 * @param <T> is a class that extends {@link Identifiable} and implements the tree nodes
 */
public interface TreeIdentifiable<TYPE_NODE_DATA, TYPE_NODE_LINK extends Identifiable, TYPE_NODE extends TreeNode<TYPE_NODE_DATA, TYPE_NODE_LINK> > {
	
	/**
	 * This method returns the top node of a tree
	 * 
	 * @return the top node
	 */
	public TYPE_NODE getTopNode();
	
	/**
	 * This method returns the parent node of a given node
	 * 
	 * @param node is the node for which the parent is going to be returned
	 * @return the parent node, or null if the given node is the top node
	 */
	public <U extends TYPE_NODE_LINK> TYPE_NODE getParentOf(U node) throws NullPointerException;
	
	/**
	 * This method returns the weight multiplier of a node. The weight is a formula
	 * of type N*multiplier+offset where N is either the weight of the node or the
	 * branch that connects it to its parent, depending on the use of the tree. As
	 * an example, if the tree is used to calculate the optimum travel time of a car
	 * through various streets in a city, N could be the street (tree branch) travel
	 * time without any traffic, multiplier could be a factor that affects travel time
	 * like a delay calculated from average traffic, and offset could be a delay based
	 * on the number of traffic lights (considering each has a fixed closed time
	 * independent from actual traffic).
	 * The neutral multiplier is one (1.0).
	 * 
	 * @param node is the node for which the multiplier is going to be returned
	 * @return the multiplier
	 */
	public <U extends TYPE_NODE_LINK> double getMultiplierOf(U node) throws NullPointerException;
	
	/**
	 * This method returns the weight offset of a node. The weight is a formula
	 * of type N*multiplier+offset where N is either the weight of the node or the
	 * branch that connects it to its parent, depending on the use of the tree. As
	 * an example, if the tree is used to calculate the optimum travel time of a car
	 * through various streets in a city, N could be the street (tree branch) travel
	 * time without any traffic, multiplier could be a factor that affects travel time
	 * like a delay calculated from average traffic, and offset could be a delay based
	 * on the number of traffic lights (considering each has a fixed closed time
	 * independent from actual traffic).
	 * The neutral offset is zero (0.0).
	 * 
	 * @param node is the node for which the offset is going to be returned
	 * @return the offset
	 */
	public <U extends TYPE_NODE_LINK> double getOffsetOf(U node) throws NullPointerException;
	
	/**
	 * This method returns the children of a given node as a list
	 * 
	 * @param node is the node for which the children are to be returned
	 * @return a list with the children or a blank list (not null) if there are no children
	 */
	public <U extends TYPE_NODE_LINK> Set<TYPE_NODE> getChildrenOf(U node) throws NullPointerException;
	
	/**
	 * This method returns the node that matches a given Identifiable
	 * 
	 * @param id
	 * @return the node that matches the given path
	 * @throws ExceptionProcessing 
	 */
	public <U extends TYPE_NODE_LINK> TYPE_NODE getNode(U id) throws NullPointerException;
	
	/**
	 * This method returns the list of nodes that correspond to a list of Identifiable
	 * 
	 * @param nodes
	 * @return
	 * @throws NullPointerException
	 * @throws ExceptionProcessing
	 */
	public <U extends TYPE_NODE_LINK> Set<TYPE_NODE> getNodes(Collection<U> nodes) throws NullPointerException;
	
	/**
	 * @return a list of all nodes
	 */
	public Set<TYPE_NODE> getNodes();
	
	/**
	 * This method returns the list of nodes that are at the corresponding level
	 * 
	 * @param level is the intended level depth to get the nodes from
	 * @return the list of nodes at the corresponding level
	 * @throws NullPointerException
	 * @throws ExceptionProcessing
	 */
	public Set<TYPE_NODE> getNodesAtLevel(int level) throws NullPointerException;
	
	/**
	 * This method returns the leaf nodes of the tree (the nodes without children).
	 * 
	 * @return
	 */
	public Set<TYPE_NODE> getLeaves();
	
	/**
	 * @return how deep in the tree the node is. The top node is zero
	 */
	public <U extends TYPE_NODE_LINK> int getNodeLevel(U node) throws NullPointerException;
	
	/**
	 * @param node
	 * @return true if the tree contains the given node, otherwise return false
	 * @throws NullPointerException
	 * @throws ExceptionProcessing
	 */
	public <U extends TYPE_NODE_LINK> boolean contains(U node) throws NullPointerException;
	
}
