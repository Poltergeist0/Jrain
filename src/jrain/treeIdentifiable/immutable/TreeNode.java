package jrain.treeIdentifiable.immutable;

import java.util.HashSet;
import java.util.Set;

import jrain.identifiable.immutable.Identifiable;

/**
 * @author poltergeist0
 *
 * Implementation of a tree node
 * 
 * Nodes are identified by their {@link Identifiable}.
 * 
 * @param <TYPE_NODE_DATA> is a class that extends {@link Identifiable} and implements the data of a tree node
 * @param <TYPE_NODE_LINK> is a class that implements the links between tree nodes
 */
public class TreeNode<TYPE_NODE_DATA, TYPE_NODE_LINK extends Identifiable>
extends Identifiable 
implements jrain.treeIdentifiable.TreeNode<TYPE_NODE_DATA,TYPE_NODE_LINK>
{
	private final TYPE_NODE_LINK pn;	//parent node
	private final TYPE_NODE_DATA nd;	//node
	private final double m;	//upstream edge multiplier
	private final double o;	//upstream edge offset
	private final HashSet<TYPE_NODE_LINK> ch;	//children
	
	/**
	 * Modify node.
	 * If parentNode is null keeps the original value.
	 * If children is null keeps the original value.
	 * If both multiplier and offset are zero keeps the original value.
	 * 
	 * @param <V> is the data type of the link between nodes
	 * @param node is the node to modify
	 * @param parentNode is the new parent for the node or null to leave it unchanged
	 * @param data is the new data for the node or null to leave it unchanged
	 * @param multiplier is the new multiplier for the node
	 * @param offset is the new offset for the node
	 * @param children are the new children for the node or null to leave it unchanged
	 */
	public <V extends TYPE_NODE_LINK> TreeNode(TreeNode<TYPE_NODE_DATA, TYPE_NODE_LINK> node,V parentNode,TYPE_NODE_DATA data,double multiplier, double offset,Set<V> children){
		super((data==null)? node.getData():data);
		if(parentNode==null) pn=node.getParent();
		else pn=parentNode;
		nd=((data==null)? node.getData():data);
		if(multiplier==0 && offset==0) {
			m=node.getMultiplier();
			o=node.getOffset();
		}
		else {
			m=multiplier;
			o=offset;
		}
		if(children==null) ch=new HashSet<TYPE_NODE_LINK>(node.getChildren());
		else ch=new HashSet<TYPE_NODE_LINK>(children);
	}
	
	/**
	 * Modify top node.
	 * If parentNode is  null keeps the original value.
	 * If children is  null keeps the original value.
	 * If both multiplier and offset are zero keeps the original value.
	 * 
	 * @param <V> is the data type of the link between nodes
	 * @param node is the node to modify
	 * @param data is the new data for the node or null to leave it unchanged
	 * @param children are the new children for the node or null to leave it unchanged
	 */
	public <V extends TYPE_NODE_LINK> TreeNode(TreeNode<TYPE_NODE_DATA, TYPE_NODE_LINK> node,TYPE_NODE_DATA data,Set<V> children){
		super(((data==null)? node.getData():data));
		pn=null;
		nd=((data==null)? node.getData():data);
		m=0;
		o=0;
		if(children==null) ch=new HashSet<TYPE_NODE_LINK>(node.getChildren());
		else ch=new HashSet<TYPE_NODE_LINK>(children);
	}
	
	/**
	 * constructor to create new node.
	 * If one of the following occurs, nothing is added:
	 * 	- node is null
	 * 	- both multiplier and offset are zero
	 * 
	 * @param <V> is the data type of the link between nodes
	 * @param parentNode is the new parent for the node or null to leave it unchanged
	 * @param node is the new data for the node or null to leave it unchanged
	 * @param multiplier is the new multiplier for the node
	 * @param offset is the new offset for the node
	 * @param children are the new children for the node or null to leave it unchanged
	 */
	public <V extends TYPE_NODE_LINK> TreeNode(V parentNode,TYPE_NODE_DATA node,double multiplier, double offset,Set<V> children){
		super(node);
		if(parentNode==null || node==null) throw new NullPointerException();
		if(multiplier==0 && offset==0) throw new NullPointerException("The multiplier and offset can not be both zero");
		pn=parentNode;
		nd=node;
		m=multiplier;
		o=offset;
		if(children==null) ch=new HashSet<TYPE_NODE_LINK>();
		else ch=new HashSet<TYPE_NODE_LINK>(children);
	}
	
	/**
	 * top node constructor
	 * 
	 * @param <V> is the data type of the link between nodes
	 * @param node is the new data for the node or null to leave it unchanged
	 * @param children are the new children for the node or null to leave it unchanged
	 */
	public <V extends TYPE_NODE_LINK> TreeNode(TYPE_NODE_DATA node,Set<V> children){
		super(node);
		if(node==null) throw new NullPointerException();
		pn=null;
		nd=node;
		m=0;
		o=0;
		if(children==null) ch=new HashSet<TYPE_NODE_LINK>();
		else ch=new HashSet<TYPE_NODE_LINK>(children);
	}
	
	public TYPE_NODE_LINK getParent(){return pn;}
	
	public TYPE_NODE_DATA getData(){return nd;}
	
	public double getMultiplier(){return m;}
	
	public double getOffset(){return o;}
	
	public Set<TYPE_NODE_LINK> getChildren(){return new HashSet<TYPE_NODE_LINK>(ch);}
	
	public boolean isTopNode(){
		if(pn==null)return true;
		return false;
	}

//	public String toString() {
//		return "{"+((pn==null)?"NULL":pn.identifiable().toString())+" *"+m+"+"+o+" "+nd.toString()+";"+ch.toString()+"}";
//	}
}