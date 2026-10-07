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
package jrain.filesystem.snapshot.snapshotTree.immutable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import jrain.filesystem.snapshot.snapshotTree.SnapshotNode.DescriptorType;
import jrain.filesystem.snapshot.snapshotTree.immutable.SnapshotNode.REFERENCE;
import jrain.identifiable.immutable.Identifiable;
import jrain.treeIdentifiable.immutable.TreeIdentifiable;
import jrain.treeIdentifiable.immutable.TreeNode;

public class SnapshotTree extends TreeIdentifiable<SnapshotNode> implements jrain.filesystem.snapshot.snapshotTree.SnapshotTree{
	
	public Set<TreeNode<SnapshotNode, Identifiable>> getFiles() {
		HashSet<TreeNode<SnapshotNode, Identifiable>>a=new HashSet<>();
		Iterator<TreeNode<SnapshotNode, Identifiable>> it = super.nodes().values().iterator();
		TreeNode<SnapshotNode, Identifiable> id=null;
		while(it.hasNext()){
			id=it.next();
			if(id.getData().getDescriptorType()==DescriptorType.FILE) a.add(id);
		}
		return a;
	}
	
	/**
	 * Method to validate if parent can have given node as child or be replaced
	 * by it, depending on the flag.
	 * 
	 * @param <T> is the type of the class that extends {@link SnapshotNode}
	 * @param parentNode is either the parent node or the node to be replaced
	 * @param node is either the child node or the replacement node
	 * @param add if true adds node as child, otherwise replace node
	 * @return node
	 */
	private static <T extends SnapshotNode> T validateChild(T parentNode,T node,boolean add){
		if(add){
			if(!parentNode.canHaveChild(node))//return false;
				throw new NullPointerException("Can not set node type "+node.getDescriptorType()+" as child of "+parentNode.getDescriptorType());
		}
		else{
			if(!parentNode.canBeReplacedBy(node)) //return false;
				throw new NullPointerException("Can not set node type "+node.getDescriptorType()+" as replacement of "+parentNode.getDescriptorType());
		}
		return node;
	}
	
	/**
	 * Method to validate if parent can have given child tree.
	 * 
	 * @param <T> is the type of the class that extends {@link SnapshotNode}
	 * @param <V> is the type of the class that extends {@link SnapshotTree}
	 * @param parentNode is the parent node
	 * @param subtree is the tree to be added as child of parent node
	 * @return the subtree
	 */
	private static <T extends SnapshotNode,V extends SnapshotTree> V validateChildTree(T parentNode,V subtree) {
		if(!parentNode.canHaveChild(subtree.getTopNode().getData())) throw new NullPointerException("Can not set node type "+subtree.getTopNode().getData().getDescriptorType()+" as child of "+parentNode.getDescriptorType());
		return subtree;
	}
	
	
	private static 
	<
	T extends HashMap<Identifiable,TreeNode<SnapshotNode, Identifiable>>, 
	V extends TreeNode<SnapshotNode, Identifiable>
	>
	Boolean deleteNode(T original, V node) {
		if(node.getData().isReference().equals(REFERENCE.MARK)) {
			//can not delete nodes marked as reference nor its children
			return false;
		}
		//initialize nodes to delete. Must be before possibly deleting node and/or it's children from tree
		ArrayList<Identifiable> nds = new ArrayList<Identifiable>(node.getChildren());
		Boolean canDelete=true;
		while(nds.size()>0){
			Identifiable i=nds.remove(0);
			TreeNode<SnapshotNode, Identifiable> n = original.get(i);
			if(!deleteNode(original, n)) canDelete=false;//if any child node fails to be deleted, this node can not be deleted
		}
		if(canDelete) {//can delete node
//			if(node.getData().isReference()==REFERENCE.UNMARK) {//if it is not marked as reference
				original.put(node.identifiable(), new TreeNode<SnapshotNode, Identifiable>(node, null, new SnapshotNode(node.getData(), true),0,0,null));
//			}
		}
		return canDelete;
	}
	
	private static 
	<
	T extends HashMap<Identifiable,TreeNode<SnapshotNode, Identifiable>>, 
	V extends TreeNode<SnapshotNode, Identifiable>
	>
	Boolean undeleteNode(T original, V parent, V node, Boolean includeChildren) {
		if(parent.getData().isDeleted()) {
			//can not undelete nodes that are children of deleted nodes
			return false;
		}
		//undelete node
		TreeNode<SnapshotNode, Identifiable> nn = new TreeNode<SnapshotNode, Identifiable>(node, null, new SnapshotNode(node.getData(), false),0,0,null);
		original.put(node.identifiable(), nn);
		if(includeChildren) {
			//initialize child nodes to undelete
			ArrayList<Identifiable> nds = new ArrayList<Identifiable>(nn.getChildren());
			while(nds.size()>0){
				Identifiable i=nds.remove(0);
				TreeNode<SnapshotNode, Identifiable> n = original.get(i);
				undeleteNode(original, nn, n,true);
			}
		}
		return true;
	}
	
	/**
	 * Delete or remove a subtree.
	 * 
	 * This method is used only in the constructors.
	 * 
	 * @param <T> is the type of the class that extends {@link SnapshotTree}
	 * @param <V> is the type of the class that extends {@link Identifiable}
	 * @param original is the original tree
	 * @param node is the node where the tree will be split
	 * @param includeNode if true also deletes/removes the node 
	 * @param remove if true removes, otherwise marks as deleted
	 * @return
	 */
	protected static <T extends SnapshotTree, V extends Identifiable> HashMap<Identifiable,TreeNode<SnapshotNode, Identifiable>> deleteTree(T original, V node, boolean includeNode, boolean remove) {
		if(remove) return original.splitTree(node, includeNode).getKey();
		TreeNode<SnapshotNode, Identifiable> sttn=original.getNode(node);//top node to delete
		TreeNode<SnapshotNode, Identifiable> n=sttn;
		//new main tree. Copy current
		HashMap<Identifiable,TreeNode<SnapshotNode, Identifiable> > mt=new HashMap<Identifiable,TreeNode<SnapshotNode, Identifiable> >(original.nodes());
		//initialize nodes to delete. Must be before possibly deleting node and/or it's children from tree
//		ArrayList<Identifiable> nds = new ArrayList<Identifiable>(n.getChildren());
		if(includeNode){
//			TreeNode<SnapshotNode, Identifiable> p=new TreeNode<SnapshotNode, Identifiable>(n,null, new SnapshotNode(n.getData(), true), 0, 0, null);
//			mt.replace(n,p);
			deleteNode(mt, sttn);
		}
		else {
			ArrayList<Identifiable> nds = new ArrayList<Identifiable>(sttn.getChildren());
			while(nds.size()>0){
				Identifiable i=nds.remove(0);
				n=original.getNode(i);
//				nds.addAll(n.getChildren());
//				TreeNode<SnapshotNode, Identifiable> p=new TreeNode<SnapshotNode, Identifiable>(n,null, new SnapshotNode(n.getData(), true), 0, 0, null);
//				mt.replace(n,p);
				deleteNode(mt, n);
			}
		}
		return mt;
	}
	
	/**
	 * Undelete node or subtree.
	 * 
	 * This method is used only in the constructors.
	 * 
	 * @param <T> is the type of the class that extends {@link SnapshotTree}
	 * @param <V> is the type of the class that extends {@link Identifiable}
	 * @param original is the original tree
	 * @param node is the node where the tree will be split
	 * @param subtree if true undeletes all children of the given node
	 * @param includeNode if true also undeletes the node when subtree is true 
	 * @return
	 */
	protected static <T extends SnapshotTree, V extends Identifiable> HashMap<Identifiable,TreeNode<SnapshotNode, Identifiable>> undeleteTree(T original, V node, boolean subtree, boolean includeNode) {
		TreeNode<SnapshotNode, Identifiable> sttn=original.getNode(node);//top node to delete
		TreeNode<SnapshotNode, Identifiable> n=sttn;
		//new main tree. Copy current
		HashMap<Identifiable,TreeNode<SnapshotNode, Identifiable> > mt=new HashMap<Identifiable,TreeNode<SnapshotNode, Identifiable> >(original.nodes());
		if(subtree) {
			if(includeNode){
				undeleteNode(mt,original.getNode(sttn), sttn,true);
			}
			else {
				ArrayList<Identifiable> nds = new ArrayList<Identifiable>(sttn.getChildren());
				while(nds.size()>0){
					Identifiable i=nds.remove(0);
					n=original.getNode(i);
					undeleteNode(mt,sttn, n,true);
				}
			}
		}
		else {
			undeleteNode(mt,original.getNode(sttn), sttn,false);
		}
		return mt;
	}
	
	private static 
	<
	T extends HashMap<Identifiable,TreeNode<SnapshotNode, Identifiable>>, 
	V extends TreeNode<SnapshotNode, Identifiable>
	>
	Boolean referenceNode(T original, V node) {
		ArrayList<Identifiable> nds = new ArrayList<Identifiable>(node.getChildren());
		while(nds.size()>0){
			Identifiable i=nds.remove(0);
			TreeNode<SnapshotNode, Identifiable> n = original.get(i);
			referenceNode(original, n);
		}
		original.put(node.identifiable(), new TreeNode<SnapshotNode, Identifiable>(node, null, new SnapshotNode(node.getData(), REFERENCE.MARK),0,0,null));
		return true;
	}
	
	private static 
	<
	T extends HashMap<Identifiable,TreeNode<SnapshotNode, Identifiable>>, 
	V extends TreeNode<SnapshotNode, Identifiable>
	>
	Boolean unreferenceNode(T original, V parent, V node, Boolean includeChildren) {
		if(parent.getData().isReference().equals(REFERENCE.MARK)) {
			//can not unmark nodes who's parent is marked as reference
			return false;//??does not work if parent is not marked first but parent can only be unmarked if all children are too
		}
		TreeNode<SnapshotNode, Identifiable> nn = new TreeNode<SnapshotNode, Identifiable>(node, null, new SnapshotNode(node.getData(), REFERENCE.UNMARK),0,0,null);
		original.put(node.identifiable(), nn);
		if(includeChildren) {
			//initialize nodes to mark. Must be before possibly marking node and/or it's children from tree
			ArrayList<Identifiable> nds = new ArrayList<Identifiable>(nn.getChildren());
			while(nds.size()>0){
				Identifiable i=nds.remove(0);
				TreeNode<SnapshotNode, Identifiable> n = original.get(i);
				unreferenceNode(original,nn, n,true);
			}
		}
		return true;
	}
	
	/**
	 * Mark a subtree as reference.
	 * 
	 * This method is used only in the constructors.
	 * 
	 * @param <T> is the type of the class that extends {@link SnapshotTree}
	 * @param <V> is the type of the class that extends {@link Identifiable}
	 * @param original is the original tree
	 * @param node is the node where the tree will be split
	 * @param mark if true marks, otherwise unmarks as reference
	 * @return
	 */
	protected static <T extends SnapshotTree, V extends Identifiable> HashMap<Identifiable,TreeNode<SnapshotNode, Identifiable>> referenceTree(T original, V node, boolean includeNode) {
		TreeNode<SnapshotNode, Identifiable> sttn=original.getNode(node);//top node to delete
		TreeNode<SnapshotNode, Identifiable> n=sttn;
		//new main tree. Copy current
		HashMap<Identifiable,TreeNode<SnapshotNode, Identifiable> > mt=new HashMap<Identifiable,TreeNode<SnapshotNode, Identifiable> >(original.nodes());
		if(includeNode){
			referenceNode(mt, sttn);
		}
		else {
			ArrayList<Identifiable> nds = new ArrayList<Identifiable>(sttn.getChildren());
			while(nds.size()>0){
				Identifiable i=nds.remove(0);
				n=original.getNode(i);
				referenceNode(mt, n);
			}
		}
		return mt;
	}
	
	/**
	 * Unmark a subtree as reference.
	 * 
	 * This method is used only in the constructors.
	 * 
	 * @param <T> is the type of the class that extends {@link SnapshotTree}
	 * @param <V> is the type of the class that extends {@link Identifiable}
	 * @param original is the original tree
	 * @param node is the node where the tree will be split
	 * @param mark if true marks, otherwise unmarks as reference
	 * @return
	 */
	protected static <T extends SnapshotTree, V extends Identifiable> HashMap<Identifiable,TreeNode<SnapshotNode, Identifiable>> unreferenceTree(T original, V node, boolean subtree, boolean includeNode) {
		TreeNode<SnapshotNode, Identifiable> sttn=original.getNode(node);//top node to delete
		TreeNode<SnapshotNode, Identifiable> n=sttn;
		//new main tree. Copy current
		HashMap<Identifiable,TreeNode<SnapshotNode, Identifiable> > mt=new HashMap<Identifiable,TreeNode<SnapshotNode, Identifiable> >(original.nodes());
		if(subtree) {
			if(includeNode){
				unreferenceNode(mt,original.getNode(sttn.getParent()), sttn,true);
			}
			else {
				ArrayList<Identifiable> nds = new ArrayList<Identifiable>(sttn.getChildren());
				while(nds.size()>0){
					Identifiable i=nds.remove(0);
					n=original.getNode(i);
					unreferenceNode(mt,sttn, n,true);
				}
			}
		}
		else {
			unreferenceNode(mt,original.getNode(sttn.getParent()), sttn,false);
		}
		return mt;
	}
	
	/**
	 * constructor to add child node to parent node
	 * 
	 * @param <T> is the type of the class that extends {@link SnapshotTree}
	 * @param <V> is the type of the class that extends {@link Identifiable}
	 * @param <U> is the type of the class that extends {@link SnapshotNode}
	 * @param original is the original tree
	 * @param parentNode is the id of the parent node
	 * @param node is the data of the new node to be added
	 * @param multiplier is the multiplier for the link connecting node to its parent
	 * @param offset is the offset for the link connecting node to its parent
	 */
	public <T extends SnapshotTree, V extends Identifiable, U extends SnapshotNode> SnapshotTree(
			T original,
			V parentNode,
			U node,
			double multiplier, double offset
		){
		super(original,parentNode,validateChild(original.getNode(parentNode).getData(), node, true),multiplier,offset);
	}

	/**
	 * constructor to replace node data
	 * 
	 * @param <T> is the type of the class that extends {@link SnapshotTree}
	 * @param <V> is the type of the class that extends {@link Identifiable}
	 * @param <U> is the type of the class that extends {@link SnapshotNode}
	 * @param original is the original tree
	 * @param node is the id of the node
	 * @param data is the data
	 * @throws NullPointerException if any method parameter is null
	 */
	public <T extends SnapshotTree, V extends Identifiable, U extends SnapshotNode> SnapshotTree(
			T original,
			V node,
			U data
		)throws NullPointerException{
		super(original,node,data);
	}
	
	/**
	 * constructor to remove or delete subtree.
	 * 
	 * Remove will remove its nodes while delete only marks them as deleted.
	 * Nodes can not be marked as deleted if they are marked as reference.
	 * 
	 * @param <T> is the type of the class that extends {@link SnapshotTree}
	 * @param <V> is the type of the class that extends {@link Identifiable}
	 * @param original
	 * @param node
	 * @param includeNode
	 * @param remove
	 * @throws NullPointerException
	 */
	public <T extends SnapshotTree, V extends Identifiable> SnapshotTree(
			T original,
			V node,
			boolean includeNode,
			boolean remove
		) throws NullPointerException{
		super(original.getTopNode(),deleteTree(original,node, includeNode,remove));
	}
	
	/**
	 * Constructor to mark subtree as reference.
	 * A subtree can only be unmarked if the parent node is not marked.
	 * 
	 * @param <T> is the type of the class that extends {@link SnapshotTree}
	 * @param <V> is the type of the class that extends {@link Identifiable}
	 * @param original
	 * @param node
	 * @param remove
	 * @throws NullPointerException
	 */
	public <T extends SnapshotTree, V extends Identifiable> SnapshotTree(
			T original,
			V node,
			boolean includeNode
		) throws NullPointerException{
		super(original.getTopNode(),referenceTree(original,node,includeNode));
	}
	
	/**
	 * constructor to undelete or unmark a node or subtree.
	 * 
	 * @param <T> is the type of the class that extends {@link SnapshotTree}
	 * @param <V> is the type of the class that extends {@link Identifiable}
	 * @param original
	 * @param node
	 * @param undelete if true, otherwise unmarks
	 * @param subtree
	 * @param includeNode
	 * @throws NullPointerException
	 */
	public <T extends SnapshotTree, V extends Identifiable> SnapshotTree(
			T original,
			V node,
			boolean undelete,
			boolean subtree,
			boolean includeNode
		) throws NullPointerException{
		super(original.getTopNode(),(undelete)?undeleteTree(original,node, subtree, includeNode):unreferenceTree(original, node, subtree, includeNode));
	}
	
	/**
	 * constructor to get a new tree from the subtree of the original tree
	 * 
	 * @param <T> is the type of the class that extends {@link SnapshotTree}
	 * @param <V> is the type of the class that extends {@link Identifiable}
	 * @param original
	 * @param parentNode
	 * @throws NullPointerException
	 */
	public <T extends SnapshotTree, V extends Identifiable> SnapshotTree(
			T original,
			V parentNode
			) throws NullPointerException{
		super(original,parentNode);
	}
	
	/**
	 * construct a new tree
	 * 
	 * @param <T> is the type of the class that extends {@link SnapshotNode}
	 * @param topNode is the top node. It can not be null
	 * 
	 * @throws NullPointerException if topNode is null
	 */
	public <T extends SnapshotNode> SnapshotTree(T data) throws NullPointerException{
		super(data);
	}
	
	/**
	 * Copy construct a new tree
	 * 
	 * @param <T> is the type of the class that extends {@link SnapshotNode}
	 * @param data is the original tree. It can not be null
	 * 
	 * @throws NullPointerException if topNode is null
	 */
	public <T extends SnapshotNode> SnapshotTree(SnapshotTree data) throws NullPointerException{
		super(data);
	}
	
	/**
	 * constructor to add subtree to parent node.
	 * 
	 * @param <T> is the type of the class that extends {@link SnapshotTree}
	 * @param <V> is the type of the class that extends {@link Identifiable}
	 * @param original is the original tree
	 * @param parentNode is the id of the parent node
	 * @param subtree is the subtree to add
	 * @param multiplier is the multiplier for the link connecting node to its parent
	 * @param offset is the offset for the link connecting node to its parent
	 * @throws NullPointerException
	 */
	public <T extends SnapshotTree, V extends Identifiable> SnapshotTree(
			T original,
			V parentNode,
			T subtree,
			double multiplier, double offset
		) throws NullPointerException{
		super(original,parentNode,validateChildTree(original.getNode(parentNode).getData(), subtree),multiplier,offset);
	}
	
}
