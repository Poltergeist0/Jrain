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
package jrain.treeIdentifiable.immutable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import jrain.identifiable.immutable.Identifiable;
import jrain.entry.mutable.Pair;

/**
 * @author poltergeist0
 *
 * Implementation of an immutable tree whose nodes are uniquely identified by {@link Identifiable}
 */
public class TreeIdentifiable<TYPE_NODE_DATA> 
extends Identifiable
implements jrain.treeIdentifiable.TreeIdentifiable<TYPE_NODE_DATA, Identifiable, TreeNode<TYPE_NODE_DATA, Identifiable> >{
	
	/**
	 * top node
	 */
	private final Identifiable tn;
	
	/**
	 * nodes list(including top node)
	 */
	private final HashMap<Identifiable,TreeNode<TYPE_NODE_DATA, Identifiable> > nd;

	@Override
	public TreeNode<TYPE_NODE_DATA, Identifiable> getTopNode() {
		return nd.get(tn);
	}

	@Override
	public <U extends Identifiable> TreeNode<TYPE_NODE_DATA, Identifiable> getParentOf(U node) throws NullPointerException{
		return getNode(getNode(node).getParent());
	}

	@Override
	public <U extends Identifiable> double getMultiplierOf(U node) throws NullPointerException{
		return getNode(node).getMultiplier();
	}

	@Override
	public <U extends Identifiable> double getOffsetOf(U node) throws NullPointerException {
		return getNode(node).getOffset();
	}

	@Override
	public <U extends Identifiable> Set<TreeNode<TYPE_NODE_DATA, Identifiable>> getChildrenOf(U node) throws NullPointerException {
		return getNodes(getNode(node).getChildren());
	}

	@Override
	public <U extends Identifiable> TreeNode<TYPE_NODE_DATA, Identifiable> getNode(U id) throws NullPointerException {
		if(id==null || !nd.containsKey(id)) throw new NullPointerException();
		TreeNode<TYPE_NODE_DATA, Identifiable> a=nd.get(id);
		return a;
	}
	
	@Override
	public Set<TreeNode<TYPE_NODE_DATA, Identifiable>> getNodes() {
		return new HashSet<TreeNode<TYPE_NODE_DATA, Identifiable>>(nd.values());
	}
	
	protected HashMap<Identifiable, TreeNode<TYPE_NODE_DATA, Identifiable>> nodes(){return nd;}

	protected static <TYPE_NODE_DATA> Set<TreeNode<TYPE_NODE_DATA, Identifiable>> getLeaves(HashMap<Identifiable,TreeNode<TYPE_NODE_DATA, Identifiable> > nd) {
		HashSet<TreeNode<TYPE_NODE_DATA, Identifiable>>a=new HashSet<>();
		Iterator<TreeNode<TYPE_NODE_DATA, Identifiable>> it = nd.values().iterator();
		TreeNode<TYPE_NODE_DATA, Identifiable> id=null;
		while(it.hasNext()){
			id=it.next();
			if(id.getChildren().size()<=0) a.add(id);
		}
		return a;
	}

	@Override
	public Set<TreeNode<TYPE_NODE_DATA, Identifiable>> getLeaves() {
//		HashSet<TreeNode<TYPE_NODE_DATA, Identifiable>>a=new HashSet<>();
//		Iterator<TreeNode<TYPE_NODE_DATA, Identifiable>> it = nd.values().iterator();
//		TreeNode<TYPE_NODE_DATA, Identifiable> id=null;
//		while(it.hasNext()){
//			id=it.next();
//			if(id.getChildren().size()<=0) a.add(id);
//		}
//		return a;
		return getLeaves(nd);
	}

	@Override
	public <U extends Identifiable> Set<TreeNode<TYPE_NODE_DATA, Identifiable>> getNodes(Collection<U> nodes) throws NullPointerException {
		HashSet<TreeNode<TYPE_NODE_DATA, Identifiable>>a=new HashSet<>();
		Iterator<U> it = nodes.iterator();
		Identifiable id=null;
		while(it.hasNext()){
			id=it.next();
			a.add(getNode(id));
		}
		return a;
	}

	@Override
	public final <U extends Identifiable> int getNodeLevel(U node) throws NullPointerException{
		if(node==null) throw new NullPointerException();
		TreeNode<TYPE_NODE_DATA, Identifiable> n=getNode(node);
		if(n.matches(tn)) return 0;
		n=getNode(n.getParent());
		int i=1;
		while(!n.matches(tn)){
			n=getNode(n.getParent());
			i++;
		}
		return i;
	}
		
	/**
	 * Do the actual processing to find the nodes at a level.
	 * Receives the nodes of the previous level and creates a new list with their
	 * children.
	 * 
	 * @param wantedLevel is the level from which the nodes are to be found
	 * @param currentLevel is the level depth currently being processed
	 * @param processNodes are the nodes of the previous level
	 * @return the list of nodes at the corresponding level
	 * @throws NullPointerException
	 */
	private Set<Identifiable> getNodesAtLevel(final int wantedLevel,final int currentLevel,final HashSet<Identifiable> processNodes) throws NullPointerException {
		HashSet<Identifiable> a=new HashSet<>();
		Iterator<Identifiable> it = processNodes.iterator();
		while(it.hasNext()) {
			Identifiable n=it.next();
			a.addAll(getNode(n).getChildren());
		}
		if(wantedLevel==currentLevel) return a;
		return getNodesAtLevel(wantedLevel,currentLevel+1,a);
	}

	@Override
	public Set<TreeNode<TYPE_NODE_DATA, Identifiable>> getNodesAtLevel(final int level) throws NullPointerException {
		HashSet<Identifiable> a=new HashSet<>();
		a.add(tn);
		if(level==0) {//it is at the intended level
			return getNodes(a);
		}
		return getNodes(getNodesAtLevel(level,1,a));
	}

	/**
	 * method to replace the parent of a node.
	 * 
	 * @param node is the new that will have its parent replaced
	 * @param newParent is the new parent for the node
	 * @return a new node with the modified parent except if the node was the top node, in which case nothing is done and the top node is returned unmodified.
	 */
	protected TreeNode<TYPE_NODE_DATA, Identifiable> replaceParent(TreeNode<TYPE_NODE_DATA, Identifiable> node, Identifiable newParent) {
		if(node.isTopNode()) return null;//top node has no parent to replace. Needs special constructor to give new multiplier and offset
		return new TreeNode<TYPE_NODE_DATA, Identifiable>(node,newParent, null, 0, 0, null);
	}
	
	/**
	 * Method to add/remove/replace a child in a node.
	 * Adds when oldChild is null.
	 * Removes when newChild is null.
	 * Does nothing if oldChild and newChild are null.
	 * Otherwise replaces.
	 * 
	 * @param node is the node that will have its child added/removed/replaced
	 * @param oldChild is the {@link Identifiable} of the old child
	 * @param newChild is the {@link Identifiable} of the new child
	 * @return the unaltered node if oldChild equal to newChild, the new node if the child is added/removed/replaced, or null if both oldChild and newChild are null
	 */
	protected TreeNode<TYPE_NODE_DATA, Identifiable> modifyChildren(TreeNode<TYPE_NODE_DATA, Identifiable> node,Identifiable oldChild,Identifiable newChild){
		if(oldChild==newChild) return node;//nothing to change
		Set<Identifiable> c = node.getChildren();
		if(oldChild==null) {
			if(newChild!=null) {
				c.add(newChild);
				if(node.isTopNode()) return new TreeNode<TYPE_NODE_DATA, Identifiable>(node, null, c);
				else return new TreeNode<TYPE_NODE_DATA, Identifiable>(node, null, null, 0, 0, c);
			}
		}
		else {//oldChild!=null
			c.remove(oldChild);
			if(newChild!=null) {
				c.add(newChild);
			}
			if(node.isTopNode()) return new TreeNode<TYPE_NODE_DATA, Identifiable>(node, null, c);
			else return new TreeNode<TYPE_NODE_DATA, Identifiable>(node, null, null, 0, 0, c);
		}
		return null;//if both oldChild and newChild are null return null to signal a problem
	}
	
	/**
	 * Method to add/remove/replace children in a node.
	 * Adds when oldChild is null.
	 * Removes when newChild is null.
	 * Does nothing if oldChild and newChild are null.
	 * Otherwise replaces.
	 * 
	 * @param node is the node that will have its children added/removed/replaced
	 * @param oldChild is a {@link Collection} of {@link Identifiable} of the old children
	 * @param newChild is a {@link Collection} of {@link Identifiable} of the new children
	 * @return the new node if the child is added/removed/replaced, or null if both oldChild and newChild are null
	 */
	protected TreeNode<TYPE_NODE_DATA, Identifiable> modifyChildren(TreeNode<TYPE_NODE_DATA, Identifiable> node,Collection<Identifiable> oldChild,Collection<Identifiable> newChild){
		Set<Identifiable> c = node.getChildren();
		if(oldChild==null) {
			if(newChild!=null) {
				c.addAll(newChild);
				if(node.isTopNode()) return new TreeNode<TYPE_NODE_DATA, Identifiable>(node, null, c);
				else return new TreeNode<TYPE_NODE_DATA, Identifiable>(node, null, null, 0, 0, c);
			}
		}
		else {//oldChild!=null
			c.removeAll(oldChild);
			if(newChild!=null) {
				c.addAll(newChild);
			}
			if(node.isTopNode()) return new TreeNode<TYPE_NODE_DATA, Identifiable>(node, null, c);
			else return new TreeNode<TYPE_NODE_DATA, Identifiable>(node, null, null, 0, 0, c);
		}
		return null;//if both oldChild and newChild are null return null to signal a problem
	}
	
	/**
	 * Method to replace node data.
	 * 
	 * @param node is the node that will have its data replaced
	 * @param newNode is the new node data
	 * @return a new node with the new data
	 */
	protected TreeNode<TYPE_NODE_DATA, Identifiable> replaceNodeData(TreeNode<TYPE_NODE_DATA, Identifiable> node, TYPE_NODE_DATA newNode) {
		if(node.isTopNode()) return new TreeNode<TYPE_NODE_DATA, Identifiable>(node,newNode, null);
		return new TreeNode<TYPE_NODE_DATA, Identifiable>(node,null,newNode,0,0,null);
	}
	
	/**
	 * Get the nodes belonging to a subtree identified a given node as top node.
	 * 
	 * @param <T> is the data type of the node
	 * @param node is the node that is the top node of the subtree
	 * @param includeNode if true, include the given node in the list of nodes
	 * @return the list of nodes belonging to the subtree
	 */
	protected <T extends Identifiable> HashMap<Identifiable,TreeNode<TYPE_NODE_DATA, Identifiable> > getSubTreeNodes(T node,boolean includeNode){
		TreeNode<TYPE_NODE_DATA, Identifiable> n=getNode(node);
		HashMap<Identifiable,TreeNode<TYPE_NODE_DATA, Identifiable> > st=new HashMap<Identifiable,TreeNode<TYPE_NODE_DATA, Identifiable> >();
		if(includeNode){
			n=new TreeNode<TYPE_NODE_DATA, Identifiable>(n,null,null);//regenerate node as top node
			st.put(n, n);
		}
		ArrayList<Identifiable> nds = new ArrayList<Identifiable>(n.getChildren());
		while(nds.size()>0){
			Identifiable i=nds.remove(0);
			n=getNode(i);
			st.put(n, n);
			nds.addAll(n.getChildren());
		}
		return st;
	}
	
	/**
	 * Split a tree into two, a tree that holds the original tree top node and a
	 * subtree that may or may not include the given node.
	 * 
	 * @param <T> is the data type of the node
	 * @param node is the node to use to split the tree
	 * @param includeNode if true includes the node in the subtree
	 * @return pair with original, without the nodes corresponding to the split subtree, and subtree
	 */
	protected <T extends Identifiable> Pair<HashMap<Identifiable,TreeNode<TYPE_NODE_DATA, Identifiable> >,HashMap<Identifiable,TreeNode<TYPE_NODE_DATA, Identifiable> > > splitTree(T node, boolean includeNode) {
		if(node.equals(tn)) new Pair<>(new HashMap<Identifiable,TreeNode<TYPE_NODE_DATA, Identifiable> >(),nd);//entire tree gets moved
		TreeNode<TYPE_NODE_DATA, Identifiable> sttn=getNode(node);//sub tree top node
		TreeNode<TYPE_NODE_DATA, Identifiable> n=sttn;
		//new main tree. Copy current
		HashMap<Identifiable,TreeNode<TYPE_NODE_DATA, Identifiable> > mt=new HashMap<Identifiable,TreeNode<TYPE_NODE_DATA, Identifiable> >(nd);
		//empty subtree
		HashMap<Identifiable,TreeNode<TYPE_NODE_DATA, Identifiable> > st=new HashMap<Identifiable,TreeNode<TYPE_NODE_DATA, Identifiable> >();
		//initialize nodes to move to subtree. Must be before possibly removing node and/or it's children from main tree
		ArrayList<Identifiable> nds = new ArrayList<Identifiable>(n.getChildren());
		if(includeNode){//...in the subtree
			//regenerate node as top node
			TreeNode<TYPE_NODE_DATA, Identifiable> nn=new TreeNode<TYPE_NODE_DATA, Identifiable>(n,null,null);
			st.put(nn, nn);//add new top node of subtree
			//remove from children of parent on original tree
			TreeNode<TYPE_NODE_DATA, Identifiable> p=mt.remove(n.getParent());
			p=modifyChildren(p, n, null);
			mt.put(p, p);
			mt.remove(n);
		}
		else {// !includeNode
			mt.remove(n);
			//regenerate node without children
			TreeNode<TYPE_NODE_DATA, Identifiable> nn=new TreeNode<TYPE_NODE_DATA, Identifiable>(n,null,null,0,0,new HashSet<>());
			mt.put(nn, nn);//reinsert node to main tree
		}
		while(nds.size()>0){
			Identifiable i=nds.remove(0);
			n=getNode(i);
			st.put(n, n);
			nds.addAll(n.getChildren());
			mt.remove(n);
		}
		return new Pair<>(mt,st);
	}
	
	/**
	 * Split a tree into two, a tree that holds the original tree top node and a
	 * subtree.
	 * The given node is the top node of the subtree.
	 * 
	 * @param <T> is the data type of the node
	 * @param node is the node to use to split the tree
	 * @return pair with original, without the nodes corresponding to the split subtree, and subtree
	 */
	public <T extends Identifiable> Pair<TreeIdentifiable<TYPE_NODE_DATA>,TreeIdentifiable<TYPE_NODE_DATA> > splitTree(T node) {
		Pair<HashMap<Identifiable, TreeNode<TYPE_NODE_DATA, Identifiable>>, HashMap<Identifiable, TreeNode<TYPE_NODE_DATA, Identifiable>>> a = splitTree(node, true);
		return new Pair<>(
				new TreeIdentifiable<>(getNode(tn),a.getKey()),
				new TreeIdentifiable<>(getNode(node),a.getValue())
				);
	}
	
	/**
	 * constructor to add child node to parent node
	 * 
	 * @param <T> is the data type of the tree
	 * @param <V> is the data type of the parent node
	 * @param <U> is the data type of the child node
	 * @param original is the original tree
	 * @param parentNode is the id of the parent node
	 * @param node is the data of the new node to be added
	 * @param multiplier is the multiplier of the connection to the parent node
	 * @param offset is the offset of the connection to the parent node
	 * @throws NullPointerException
	 */
	public <T extends TreeIdentifiable<TYPE_NODE_DATA>, V extends Identifiable, U extends TYPE_NODE_DATA> TreeIdentifiable(
			T original,
			V parentNode,
			U node,
			double multiplier, double offset
		)throws NullPointerException{
		super(original);
		if(original==null || parentNode==null || node==null) throw new NullPointerException();
		HashMap<Identifiable,TreeNode<TYPE_NODE_DATA, Identifiable>> nds=original.nodes();
		//remove parent as it will be modified
		TreeNode<TYPE_NODE_DATA, Identifiable> p=nds.remove(parentNode);
		//new node without children from node data
		TreeNode<TYPE_NODE_DATA, Identifiable> n=new TreeNode<TYPE_NODE_DATA, Identifiable>(parentNode, node, multiplier, offset, null);
		//update parent node children to add new node
		p=modifyChildren(p, null, n);
		//add the new node
		nds.put(n, n);
		//add the updated parent node in the same position it was removed from
		nds.put(p, p);
		nd=nds;
		tn=original.getTopNode();
	}

	/**
	 * constructor to replace node data
	 * 
	 * @param <T> is the data type of the tree
	 * @param <V> is the data type of the parent node
	 * @param <U> is the data type of the child node
	 * @param original is the original tree
	 * @param node is the id of the node
	 * @param data is the new node data
	 * @throws NullPointerException
	 */
	public <T extends TreeIdentifiable<TYPE_NODE_DATA>, V extends Identifiable, U extends TYPE_NODE_DATA> TreeIdentifiable(
			T original,
			V node,
			U data
		)throws NullPointerException{
		super(original);
		if(original==null || node==null || data==null) throw new NullPointerException();
		HashMap<Identifiable,TreeNode<TYPE_NODE_DATA, Identifiable>> nds=original.nodes();
		//remove node to be modified
		TreeNode<TYPE_NODE_DATA, Identifiable> n=nds.remove(node);
		n=replaceNodeData(n, data);
		//add the updated node in the same position it was removed from
		nds.put(n,n);
		if(!n.isTopNode()) {
			//remove parent
			TreeNode<TYPE_NODE_DATA, Identifiable> p=nds.remove(n.getParent());
			p=modifyChildren(p, node, n);//this is necessary to cover any identifiable changes
			//add the updated node in the same position it was removed from
			nds.put(p,p);
		}
		nd=nds;
		tn=original.getTopNode();
	}

	/**
	 * constructor to remove subtree.
	 * 
	 * @param <T> is the data type of the tree
	 * @param <V> is the data type of the parent node
	 * @param original is the original tree
	 * @param node is the id of the node
	 * @param includeNode if true also removes the given node
	 * @throws NullPointerException
	 */
	public <T extends TreeIdentifiable<TYPE_NODE_DATA>, V extends Identifiable> TreeIdentifiable(
			T original,
			V node,
			boolean includeNode
		) throws NullPointerException{
		super(original);
		tn=original.getTopNode();
		nd=original.splitTree(node, includeNode).getKey();
	}
	
	/**
	 * constructor to get a new tree from the subtree of the original tree
	 * 
	 * @param <T> is the data type of the tree
	 * @param <V> is the data type of the parent node
	 * @param original is the original tree
	 * @param parentNode is the top node of the subtree
	 * @throws NullPointerException
	 */
	public <T extends TreeIdentifiable<TYPE_NODE_DATA>, V extends Identifiable> TreeIdentifiable(
			T original,
			V parentNode
			) throws NullPointerException{
		super(original);
		tn=original.getNode(parentNode);
		nd=original.splitTree(parentNode, true).getValue();
	}
	
	/**
	 * construct a new tree given a set of nodes
	 * 
	 * @param topNode is the top node. It can not be null
	 * @param nodes is all the nodes in the tree including the top node
	 */
	protected TreeIdentifiable(TreeNode<TYPE_NODE_DATA, Identifiable> topNode,HashMap<Identifiable,TreeNode<TYPE_NODE_DATA, Identifiable> > nodes) throws NullPointerException{
		super(topNode);//if topNode extends Identifiable use it's ID otherwise generate one
		tn=topNode;
		nd=nodes;
	}
	
	/**
	 * construct a new tree
	 * 
	 * @param <T> is the data type of the top node data
	 * @param topNode is the top node data. It can not be null
	 * 
	 * @throws NullPointerException if topNode is null
	 */
	public <T extends TYPE_NODE_DATA> TreeIdentifiable(T topNode) throws NullPointerException{
		super(topNode);//if topNode extends Identifiable use it's ID otherwise generate one
		if(topNode==null) throw new NullPointerException();
		TreeNode<TYPE_NODE_DATA, Identifiable> n = new TreeNode<TYPE_NODE_DATA, Identifiable>(topNode, null);
		HashMap<Identifiable,TreeNode<TYPE_NODE_DATA, Identifiable>> nds = new HashMap<Identifiable,TreeNode<TYPE_NODE_DATA, Identifiable>>();
		nds.put(n.identifiable(),n);
		tn=n.identifiable();
		nd=nds;
	}
	
	/**
	 * Copy construct a new tree (shallow copy)
	 * 
	 * @param <T> is the data type of the node data
	 * @param tree is the original tree. It can not be null
	 * @throws NullPointerException if tree is null
	 */
	public <T extends TYPE_NODE_DATA> TreeIdentifiable(TreeIdentifiable<TYPE_NODE_DATA> tree) throws NullPointerException{
		super(tree);//if topNode extends Identifiable use it's ID otherwise generate one
		if(tree==null) throw new NullPointerException();
		tn=tree.tn;
		nd=new HashMap<Identifiable,TreeNode<TYPE_NODE_DATA, Identifiable>>(tree.nd);
	}
	
	/**
	 * Constructor to add subtree to parent node.
	 * Replaces existing nodes.
	 * 
	 * @param <T> is the data type of the tree
	 * @param <V> is the data type of the parent node
	 * @param original is the original tree. It can not be null
	 * @param parentNode is the node of the original tree where the subtree will be added
	 * @param subtree is the subtree to add
	 * @param multiplier is the multiplier of the connection to the parent node
	 * @param offset is the offset of the connection to the parent node
	 * @throws NullPointerException
	 */
	public <T extends TreeIdentifiable<TYPE_NODE_DATA>, V extends Identifiable> TreeIdentifiable(
			T original,
			V parentNode,
			T subtree,
			double multiplier, double offset
		) throws NullPointerException{
		super(original);
		if(parentNode==null) throw new NullPointerException("Parent node can not be null");
		if(subtree.equals(original)) throw new NullPointerException("Can not add tree to itself");
		//add original tree nodes
		HashMap<Identifiable,TreeNode<TYPE_NODE_DATA,Identifiable>> nds = original.nodes();
		//remove parent node
		TreeNode<TYPE_NODE_DATA,Identifiable> p=nds.remove(parentNode);
		//add nodes of subtree
		nds.putAll(subtree.nodes());//this replaces existing nodes
		//remove previous top node of subtree
		TreeNode<TYPE_NODE_DATA, Identifiable> tnsb = subtree.getTopNode();
		nds.remove(tnsb);
		//add parent node to top node of subtree
		tnsb=new TreeNode<TYPE_NODE_DATA, Identifiable>(tnsb,parentNode, null, multiplier, offset, null);
		nds.put(tnsb,tnsb);
		//add the top node of subtree as child node of parent
		p=modifyChildren(p, null, tnsb);
		//add the new parent
		nds.put(p,p);
		nd=nds;
		if(original.getTopNode().equals(parentNode)) tn=parentNode;
		else tn=original.getTopNode();
	}
	
	@Override
	public <U extends Identifiable> boolean contains(U node) throws NullPointerException{
		if(getNode(node)!=null) return true;
		return false;
	}

//	public String toString(boolean all) {
//		if(!all) {//return only identifiable
//			return tn.toString();
//		}
//		return nd.toString();
//	}
}
