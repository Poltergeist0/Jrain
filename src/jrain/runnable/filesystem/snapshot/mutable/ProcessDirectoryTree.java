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
package jrain.runnable.filesystem.snapshot.mutable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import jrain.filesystem.snapshot.immutable.DirectoryDescriptor;
import jrain.filesystem.snapshot.immutable.FileDescriptor;
import jrain.filesystem.snapshot.immutable.FileSystemObjectDescriptor;
import jrain.filesystem.snapshot.immutable.SnapshotDescriptor;
import jrain.filesystem.snapshot.snapshotTree.SnapshotNode.DescriptorType;
import jrain.filesystem.snapshot.snapshotTree.immutable.SnapshotNode;
import jrain.filesystem.snapshot.snapshotTree.immutable.SnapshotTree;
import jrain.hash.hashInstance.hashes.immutable.Hashes;
import jrain.identifiable.immutable.Identifiable;
import jrain.map.mutable.TreeMultiMap;
import jrain.runnable.RunnableStepByStep;
import jrain.runnable.RunnableStepByStepStatistics.FIELD;
import jrain.treeIdentifiable.immutable.TreeNode;

/**
 * Process the given base path to get the directory tree.
 * It is processed depth first.
 *
 * @author poltergeist0
 *
 */
public class ProcessDirectoryTree extends RunnableStepByStep{
	/**
	 * key is the node level
	 * top node is level zero
	 * children have increasing level
	 */
	private TreeMultiMap<Integer,TreeNode<SnapshotNode, Identifiable>> processingNodes;
	private TreeNode<SnapshotNode, Identifiable> current=null;
	private final Integer rl;
	private final Set<String> ha;
	private final Set<Path> filter;

	private final String _pathSeparator;
	
	/**
	 * Default date and time to use when such information can not be read from a file/directory
	 */
	private static LocalDateTime LocalDateTime0;//=LocalDateTime.ofInstant(Instant.ofEpochMilli(0), ZoneId.systemDefault());
	
	/**
	 * snapshot used in processing
	 */
	private SnapshotTree sn;
			
	public SnapshotTree snapshot() {return (super.running())?null:sn;}
	
	/**
	 * Constructor for new snapshots
	 * 
	 * @param base
	 * @param filterOut
	 * @param hashes
	 * @param recursion
	 * @throws Exception
	 */
	public ProcessDirectoryTree(SnapshotNode base,Set<Path> filterOut , Set<String> hashes, Integer recursion,String pathSeparator,LocalDateTime defaultDateTime) throws Exception{
		sn=new SnapshotTree(base);
		processingNodes=new TreeMultiMap<>();
		processingNodes.put(-1,sn.getTopNode());//set the snapshot (which contains the base path) as the first node to process
		if(recursion>=0) rl=recursion;
		else rl=Integer.MAX_VALUE;
		filter=filterOut;
		ha=hashes;
		_pathSeparator=pathSeparator;
		LocalDateTime0=defaultDateTime;
	}
	
	/**
	 * @param filter
	 * @param path
	 * @return true if the path is not on the list of paths to filter
	 */
	private boolean checkPathForFiltering(final Set<Path> filter, final Path path) {
		Iterator<Path> it = filter.iterator();
		while(it.hasNext()) {
			Path a = it.next();
			if(path.startsWith(a)) return false;
		}
		return true;
	}
	
	private String getPath(final TreeNode<SnapshotNode, Identifiable> node) {
		if(current.getData().getDescriptorType()==DescriptorType.SNAPSHOT){
			return current.getData().getSnapshotDescriptor().getBasePath();
		}
		else{
			return current.getData().getDirectoryDescriptor().getFullPath(_pathSeparator);
		}
	}
	
	private void processDirectory(Integer level,File file,LocalDateTime ct,LocalDateTime at,LocalDateTime mt) {
		SnapshotNode data=null;
		//node can be a directory or snapshot
		if(current.getData().getDescriptorType()!=DescriptorType.SNAPSHOT){
			//it is not a snapshot. Snapshots do not have dates
			//replace data of the node in the snapshot tree to add dates
			data= new SnapshotNode(new DirectoryDescriptor(current.identifier(),current.getData().getDescriptorName(), current.getData().getDirectoryDescriptor().getPath(), (long)0, ct, mt, at,null),null,true);
			sn=new SnapshotTree(sn, current, data);
		}
		//check if path has children. Only valid for: 1)snapshot or ; 2)directories that are not beyond the requested recursion level
		if(current.getData().getDescriptorType()==DescriptorType.SNAPSHOT || checkPathForFiltering(filter, new File(getPath(current)).toPath()) || current.getData().getDescriptorType()==DescriptorType.DIRECTORY && level<rl){
			String[] list=file.list();
			if(list!=null && list.length>0){
				for (int i = 0; i < list.length; i++) {
					//add child to the snapshot tree. Use DirectoryDescriptor because it is more generic
					data= new SnapshotNode(new DirectoryDescriptor(null,list[i], file.getPath(), null,LocalDateTime0,LocalDateTime0, LocalDateTime0,null),null,true);
					sn=new SnapshotTree(sn, current, data, 1, 0);
					//add child to the list of nodes to process
//						Set<TreeNode<SnapshotNode, Identifiable>> tmp=new HashSet<TreeNode<SnapshotNode,Identifiable>>();
//						tmp.add(sne.getTopNode());
					processingNodes.put(level+1,new TreeNode<SnapshotNode, Identifiable>(current,data,1,0, new HashSet<Identifiable>()));
				}
			}
			else {//no children
				//remove entry from list of nodes to process
				processingNodes.remove(level, current);
			}
		}
		else {
			//remove entry from list of nodes to process
			processingNodes.remove(level, current);
		}

	}
	
	protected void step() throws Exception{
		if(processingNodes.size()<=0) {
			//all nodes have been processed
			super.finish();
			super.statusModify(1,FIELD.PROCESSEDITEMS,false,"Done transversing.");
			return;// false;
		}//if there are nodes left to process
		Integer level = processingNodes.lastKey();//get the deepest level (bigger number)
		//get the highest level first since lower levels (parents) need to be updated with size of children
		TreeSet<TreeNode<SnapshotNode, Identifiable>> lst = processingNodes.get(level);
		current=lst.first();
		if(
				!sn.contains(current) //current was not processed yet
				|| 
				(sn.contains(current) && sn.getChildrenOf(current).size()<=0) //current was processed but does not have children (it is empty directory or file)
			) {//transversing directories
			//update highest level
			//get path
//			String path;
//			if(current.getData().getDescriptorType()==DescriptorType.SNAPSHOT){
//				path=current.getData().getSnapshotDescriptor().getBasePath();
//			}
//			else{
//				path=current.getData().getDirectoryDescriptor().getFullPath(_pathSeparator);
//			}
			String path=getPath(current);
			if(level<=rl){//must always check level zero so that the root is always calculated
				File file = new File(path);
				long size=0;
				if(!Files.isSymbolicLink(file.toPath())){//only process file if it is not a symbolic link
					SnapshotNode data=null;
					LocalDateTime ct=null;
					LocalDateTime at=null;
					LocalDateTime mt=null;
					try {
						BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class,LinkOption.NOFOLLOW_LINKS);
						ct=LocalDateTime.ofInstant(attr.creationTime().toInstant(), ZoneId.systemDefault());
						at=LocalDateTime.ofInstant(attr.lastAccessTime().toInstant(), ZoneId.systemDefault());
						mt=LocalDateTime.ofInstant(attr.lastModifiedTime().toInstant(), ZoneId.systemDefault());
					} catch (IOException e) {
						ct=LocalDateTime0;
						at=LocalDateTime0;
						mt=LocalDateTime0;
					}
					if(file.isDirectory()){//path is a directory
						processDirectory(level, file, ct, at, mt);
					}
					else{// !file.isDirectory()
						if(file.isFile()){//it is a file
							//node was already inserted into the tree as a directory so it has to be replaced as file
							//and the size updated
							size=file.length();
							data=new SnapshotNode(new FileDescriptor(current.identifier(),current.getData().getDescriptorName(), current.getData().getDirectoryDescriptor().getPath(), size, ct, mt, at,null, new Hashes(ha)),null,true);
							sn=new SnapshotTree(sn, current, data);
						}
						else{//not a file nor directory. May be a pipe, link, etc.
							//keep it as a directory in the tree
//							replace it by a file system object
							data=new SnapshotNode(new FileSystemObjectDescriptor(current.identifier(),current.getData().getDescriptorName(), size, current.getData().getDirectoryDescriptor().getPath()),true);
							sn=new SnapshotTree(sn, current, data);
						}
						//remove entry from list of nodes to process
						processingNodes.remove(level, current);
					}
				}
				super.status(super.status().modify(1,FIELD.TOTALITEMS,false,"Processing "+path).modify(size,FIELD.TOTALSIZE,false,null));
			}
			else{//(recursionlevel!=0 && snap.getNodeLevel(current)>recursionlevel)
				//skip node since path depth is greater than recursion level
				//but remove it and its children from tree since they were previously added
				sn=new SnapshotTree(sn, current, true, true);
				super.statusModify(1,FIELD.TOTALITEMS,false,"Processing "+path);
				//remove entry from list of nodes to process
				processingNodes.remove(level, current);
			}
		}
		else {//updating sizes of parents
			//remove entry from list of nodes to process
			processingNodes.remove(level, current);
			if(current.getData().getDescriptorType()!=DescriptorType.FILE){
				//current is not a file. Is directory or snapshot
				//update size in the tree
				//get all children and add their sizes
				Iterator<TreeNode<SnapshotNode, Identifiable>> it = sn.getChildrenOf(current).iterator();
				Long size=(long) 0;
				int cnt=0;
				SnapshotNode e;
				while(it.hasNext()){
					e=it.next().getData();
					size+=e.getDescriptorSize();
					++cnt;
				}
				if(current.getData().getDescriptorType()==DescriptorType.DIRECTORY){
					e=new SnapshotNode(new DirectoryDescriptor(current.getData().getDirectoryDescriptor(), true, null, null, size,null,null,null,null), null,true);
					//replace on tree
					sn=new SnapshotTree(sn, current, e);
					super.statusModify(cnt,FIELD.PROCESSEDITEMS,false,"Updating "+current.getData().getDirectoryDescriptor().getFullPath(_pathSeparator));
				}
				else{// if(current.getNode().getNodeType()==NodeType.SNAPSHOT){
					//snapshot descriptor is the last to be calculated
					e=new SnapshotNode(new SnapshotDescriptor(current.getData().getSnapshotDescriptor(), true, null, null, ha, -1,null,null,size), null,false,true);
					//replace on tree
					sn=new SnapshotTree(sn, current, e);
					super.statusModify(cnt,FIELD.PROCESSEDITEMS,false,"Updating "+current.getData().getSnapshotDescriptor().getBasePath());
				}
			}
		}
	}

	@Override
	protected void process(){super.process();}
	
	@Override
	protected void initialize() throws Exception {}

	@Override
	protected void endStep() throws Exception {}
}

