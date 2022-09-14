package jrain.filesystem.snapshot.snapshotTree.immutable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jrain.filesystem.snapshot.BaseSnapshotsDescriptor;
import jrain.filesystem.snapshot.immutable.DirectoryDescriptor;
import jrain.filesystem.snapshot.immutable.FileDescriptor;
import jrain.filesystem.snapshot.immutable.SnapshotDescriptor;
import jrain.filesystem.snapshot.immutable.SnapshotsGroupDescriptor;
import jrain.identifiable.immutable.Identifiable;
import jrain.polyType.immutable.PolyType;

/**
 * @author poltergeist0
 *
 * Stub class for file system snapshot descriptors.
 * Provides common storage for any node type.
 * The implemented methods call the corresponding methods in the file 
 * system descriptors themselves.
 */
public class SnapshotNode extends Identifiable implements jrain.filesystem.snapshot.snapshotTree.SnapshotNode{
	
	/**
	 * Type of descriptor
	 */
	private final DescriptorType tp;
	
	/**
	 * Container for all descriptor types
	 */
	private final PolyType<? extends BaseSnapshotsDescriptor> descriptor;
	
	/**
	 * List of duplicates
	 */
	private final Set<Identifiable> dup;
	
	/**
	 * Processing
	 */
	private final boolean proc;
	
	/**
	 * Online
	 */
	private final boolean onl;
	
	/**
	 * Deleted
	 */
	private final boolean del;
	
	/**
	 * Marked as reference
	 */
	private final REFERENCE ref;
	
	
	public enum REFERENCE {MARK, UNMARK}
	
	/**********************************************
	* override {@link Identifiable} in order to return information from the enclosed 
	* objects instead of this one.
	**********************************************/
	
	public UUID identifier(){
		return descriptor.get().identifier();
	}

	public String objectType(){
		return descriptor.get().objectType();
	}

	@SuppressWarnings("unchecked")//do not get this one!? Identifiable<UUID> to Identifiable<UUID> generates warning!? (copy-paste doubt :P )
	public Identifiable identifiable(){
		return descriptor.get().identifiable();
	}
	
	public <T extends Identifiable> boolean matches(T ID){
		return descriptor.get().matches(ID);
	}
	
	/**********************************************
	* end of overriding {@link Identifiable}
	**********************************************/
	
	/**
	 * @return the container of the descriptors for classes that extend this one
	 */
	protected PolyType<? extends BaseSnapshotsDescriptor> getDescriptor(){return descriptor;}
	
	/**
	 * Constructor for {@link SnapshotsGroupDescriptor}.
	 * Snapshots group do not have duplicates.
	 * 
	 * @param <T> is the type of the class that extends {@link SnapshotsGroupDescriptor}
	 * @param snapshotsGroup is the descriptor
	 */
	public <T extends SnapshotsGroupDescriptor> SnapshotNode(T snapshotsGroup){
		super(snapshotsGroup);
		tp=DescriptorType.SNAPSHOTSGROUP;
		descriptor=new PolyType<BaseSnapshotsDescriptor>(snapshotsGroup);
		dup=new HashSet<>();
		proc=false;
		onl=false;
		del=false;
		ref=REFERENCE.UNMARK;
	}
	
	/**
	 * Constructor for {@link SnapshotDescriptor}.
	 * 
	 * @param <T> is the type of the class that extends {@link SnapshotDescriptor}
	 * @param snapshot is the descriptor
	 * @param duplicateSnapshots is the list of duplicates
	 * @param processing is the flag that marks the node as currently being processed
	 * @param online is the flag that marks the node as currently being online
	 */
	public <T extends SnapshotDescriptor> SnapshotNode(T snapshot,Set<? extends Identifiable> duplicateSnapshots,final boolean processing,final boolean online) {
		super(snapshot);
		tp=DescriptorType.SNAPSHOT;
		descriptor=new PolyType<BaseSnapshotsDescriptor>(snapshot);
		if(duplicateSnapshots==null){
			dup=new HashSet<>();
		}
		else{
			dup=new HashSet<Identifiable>(duplicateSnapshots);
		}
		proc=processing;
		onl=online;
		del=false;
		ref=REFERENCE.UNMARK;
	}
	
	/**
	 * Constructor for {@link DirectoryDescriptor}.
	 * 
	 * @param <T> is the type of the class that extends {@link DirectoryDescriptor}
	 * @param directory is the descriptor
	 * @param duplicateDirectories is the list of duplicates
	 * @param online is the flag that marks the node as currently being online
	 */
	public <T extends DirectoryDescriptor> SnapshotNode(T directory,Set<? extends Identifiable> duplicateDirectories,final boolean online) {
		super(directory);
		tp=DescriptorType.DIRECTORY;
		descriptor=new PolyType<BaseSnapshotsDescriptor>(directory);
		if(duplicateDirectories==null){
			dup=new HashSet<>();
		}
		else{
			dup=new HashSet<Identifiable>(duplicateDirectories);
		}
		proc=false;
		onl=online;
		del=false;
		ref=REFERENCE.UNMARK;
	}
	
	/**
	 * Constructor for {@link FileDescriptor}
	 * 
	 * @param <T> is the type of the class that extends {@link FileDescriptor}
	 * @param file is the descriptor
	 * @param duplicateFiles is the list of duplicates
	 * @param online is the flag that marks the node as currently being online
	 */
	public <T extends FileDescriptor> SnapshotNode(T file,Set<? extends Identifiable> duplicateFiles,final boolean online) {
		super(file);
		tp=DescriptorType.FILE;
		descriptor=new PolyType<BaseSnapshotsDescriptor>(file);
		if(duplicateFiles==null){
			dup=new HashSet<>();
		}
		else{
			dup=new HashSet<>(duplicateFiles);
		}
		proc=false;
		onl=online;
		del=false;
		ref=REFERENCE.UNMARK;
	}
	
	/**
	 * Constructor to add/remove a duplicate.
	 * When removing, if it does not exist, ignores
	 * 
	 * @param <T> is the type of the class that extends {@link SnapshotNode}
	 * @param <V> is the type of the class that extends {@link Identifiable}
	 * @param original is the node from which to add/remove the duplicate
	 * @param duplicate is the {@link Identifiable} of the duplicate
	 * @param add if true adds the duplicate else removes
	 */
	public <T extends SnapshotNode,V extends Identifiable> SnapshotNode(T original, V duplicate, boolean add) {
		super(original);
		tp=original.getDescriptorType();
		descriptor=original.getDescriptor();
		proc=original.processing();
		onl=original.online();
		if(add){
			HashSet<Identifiable> d = new HashSet<Identifiable>(original.getDuplicates());
			if(!d.contains(duplicate)) d.add(duplicate);
			dup=d;
		}
		else{
			HashSet<Identifiable> d = new HashSet<Identifiable>(original.getDuplicates());
			d.remove(duplicate);
			dup=d;
		}
		del=original.isDeleted();
		ref=original.isReference();
	}
	
	/**
	 * Constructor to add/remove a list of duplicates.
	 * When removing, if it does not exist, ignores
	 * 
	 * @param <T> is the type of the class that extends {@link SnapshotNode}
	 * @param original is the node from which to add/remove the duplicates
	 * @param duplicates is the list of {@link Identifiable} of the duplicates
	 * @param add if true adds the duplicates else removes
	 */
	public <T extends SnapshotNode> SnapshotNode(T original, Set<? extends Identifiable> duplicates, boolean add) {
		super(original);
		tp=original.getDescriptorType();
		descriptor=original.getDescriptor();
		proc=original.processing();
		onl=original.online();
		if(add){
			HashSet<Identifiable> d = new HashSet<Identifiable>(original.getDuplicates());
			d.addAll(duplicates);
			dup=d;
		}
		else{
			HashSet<Identifiable> d = new HashSet<Identifiable>(original.getDuplicates());
			d.removeAll(duplicates);
			dup=d;
		}
		del=original.isDeleted();
		ref=isReference();
	}
	
	/**
	 * Constructor to replace a duplicate ID.
	 * When replacing, if the original duplicate does not exist, ignores
	 * 
	 * @param <T> is the type of the class that extends {@link SnapshotNode}
	 * @param <V> is the type of the class that extends {@link Identifiable}
	 * @param original is the node from which to replace the duplicate
	 * @param oldID is the original {@link Identifiable} of the duplicate
	 * @param newID is the new {@link Identifiable} of the duplicate
	 */
	public <T extends SnapshotNode, V extends Identifiable> SnapshotNode(T original, V oldID, V newID) {
		super(original);
		tp=original.getDescriptorType();
		descriptor=original.getDescriptor();
		proc=original.processing();
		onl=original.online();
		HashSet<Identifiable> d = new HashSet<Identifiable>(original.getDuplicates());
		if(d.remove(oldID)) {
			//if it gets there then the oldID existed and was removed so add the newID
			d.add(newID);
		}
		dup=d;
		del=isDeleted();
		ref=isReference();
	}
	
	/**
	 * Constructor to make a copy of a node without any duplicates so that they can be added later
	 * 
	 * @param <T> is the type of the class that extends {@link SnapshotNode}
	 * @param original is the node to copy
	 */
	public <T extends SnapshotNode> SnapshotNode(T original){
		super(original);
		tp=original.getDescriptorType();
		descriptor=original.getDescriptor();
		proc=original.processing();
		onl=original.online();
		dup=new HashSet<Identifiable>();
		del=isDeleted();
		ref=isReference();
	}
	
	/**
	 * Constructor to mark a node as deleted or undelete it.
	 * If the node is marked as reference for comparisons and it is requested to
	 * be marked as deleted, nothing will happen.
	 * 
	 * @param <T> is the type of the class that extends {@link SnapshotNode}
	 * @param original is the node to delete or undelete
	 * @param delete if true marks the node as deleted, otherwise undeletes it 
	 */
	public <T extends SnapshotNode> SnapshotNode(T original, boolean delete){
		super(original);
		tp=original.getDescriptorType();
		descriptor=original.getDescriptor();
		proc=original.processing();
		onl=original.online();
		dup=original.getDuplicates();
		del=(isReference().equals(REFERENCE.MARK))?false:delete;
		ref=isReference();
	}
	
	/**
	 * Constructor to mark a node as reference node for comparisons.
	 * Marked nodes can not be deleted so it will be marked as not deleted if it
	 * was previously marked as deleted.
	 * 
	 * @param <T> is the type of the class that extends {@link SnapshotNode}
	 * @param original is the node to mark or unmark
	 * @param ref if true marks the node as deleted, otherwise undeletes it 
	 */
	public <T extends SnapshotNode> SnapshotNode(T original, REFERENCE reference){
		super(original);
		tp=original.getDescriptorType();
		descriptor=original.getDescriptor();
		proc=original.processing();
		onl=original.online();
		dup=original.getDuplicates();
		del=(reference.equals(REFERENCE.MARK))?false:original.isDeleted();
		ref=reference;
	}
	
	public <T extends jrain.filesystem.snapshot.snapshotTree.SnapshotNode> boolean canHaveChild(T node){
		if(tp==DescriptorType.SNAPSHOTSGROUP && node.getDescriptorType()==DescriptorType.SNAPSHOT)return true;
		if(tp==DescriptorType.SNAPSHOT && node.getDescriptorType()==DescriptorType.DIRECTORY)return true;
		if(tp==DescriptorType.SNAPSHOT && node.getDescriptorType()==DescriptorType.FILE)return true;
		if(tp==DescriptorType.DIRECTORY && node.getDescriptorType()==DescriptorType.DIRECTORY)return true;
		if(tp==DescriptorType.DIRECTORY && node.getDescriptorType()==DescriptorType.FILE)return true;
		return false;
	}
	
	public <T extends jrain.filesystem.snapshot.snapshotTree.SnapshotNode> boolean canBeReplacedBy(T node) {
		if(tp==DescriptorType.SNAPSHOTSGROUP && node.getDescriptorType()==DescriptorType.SNAPSHOTSGROUP)return true;
		if(tp==DescriptorType.SNAPSHOT && node.getDescriptorType()==DescriptorType.SNAPSHOT)return true;
		if(tp==DescriptorType.DIRECTORY && node.getDescriptorType()==DescriptorType.DIRECTORY)return true;
		if(tp==DescriptorType.DIRECTORY && node.getDescriptorType()==DescriptorType.FILE)return true;	//files can be initially added as directories and later changed to files
		if(tp==DescriptorType.FILE && node.getDescriptorType()==DescriptorType.FILE)return true;
		return false;
	}
	
	public DescriptorType getDescriptorType(){return tp;}
		
	public String getDescriptorName(){
		return descriptor.get().getDescriptorName();
	}
	
	public Long getDescriptorSize(){
		return descriptor.get().getDescriptorSize();
	}
	
	public SnapshotsGroupDescriptor getSnapshotsGroupDescriptor(){
		if(tp==DescriptorType.SNAPSHOTSGROUP){
			return (SnapshotsGroupDescriptor) descriptor.get();
		}
		return null;
	}
	
	public SnapshotDescriptor getSnapshotDescriptor(){
		if(tp==DescriptorType.SNAPSHOT){
			return (SnapshotDescriptor) descriptor.get();
		}
		return null;
	}
	
	public DirectoryDescriptor getDirectoryDescriptor(){
		if(tp==DescriptorType.DIRECTORY){
			return (DirectoryDescriptor) descriptor.get();
		}
		return null;
	}
	
	public FileDescriptor getFileDescriptor(){
		if(tp==DescriptorType.FILE){
			return (FileDescriptor) descriptor.get();
		}
		return null;
	}
	
	public Set<Identifiable> getDuplicates(){return new HashSet<>(dup);}

	public boolean processing(){return proc;}
	
	public boolean online(){return onl;}
	
	public boolean isDeleted(){return del;}

	public REFERENCE isReference() {
		return ref;
	}
		
//	public String toString() {
//		return "{"+pn.toString()+" *"+m+"+"+o+" "+nd.toString()+";"+ch.toString()+"}";
//	}
}
