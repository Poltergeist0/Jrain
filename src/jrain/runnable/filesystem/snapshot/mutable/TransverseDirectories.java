package jrain.runnable.filesystem.snapshot.mutable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import jrain.filesystem.snapshot.immutable.DirectoryDescriptor;
import jrain.filesystem.snapshot.immutable.FileDescriptor;
import jrain.filesystem.snapshot.immutable.SnapshotDescriptor;
import jrain.filesystem.snapshot.snapshotTree.SnapshotNode.DescriptorType;
import jrain.filesystem.snapshot.snapshotTree.immutable.SnapshotNode;
import jrain.filesystem.snapshot.snapshotTree.immutable.SnapshotTree;
import jrain.hash.hashInstance.hashes.immutable.Hashes;
import jrain.runnable.filesystem.file.FileCopyChecksum;
import jrain.differentialHistory.immutable.DifferentialHistory;
import jrain.identifiable.immutable.Identifiable;
import jrain.treeIdentifiable.immutable.TreeNode;
import jrain.map.mutable.TreeMultiMap;
import jrain.runnable.RunnableStepByStep;
import jrain.runnable.RunnableStepByStepStatistics;
import jrain.runnable.RunnableStepByStepStatistics.FIELD;


/**
 * @author poltergeist0
 *
 * This class builds a snapshot tree from a base path with a snapshot descriptor
 * as top node (no snapshots group) and directories and files as its children.
 */
public class TransverseDirectories extends RunnableStepByStep implements jrain.identifiable.Identifiable<UUID>{
	
	private static LocalDateTime LocalDateTime0=LocalDateTime.ofInstant(Instant.ofEpochMilli(0), ZoneId.systemDefault());
	
	public static Set<Path> toPath(final String basePath, final Set<String> s){
		Path bp=new File(basePath).toPath().normalize();
		HashSet<Path> p=new HashSet<>();
		Iterator<String> it = s.iterator();
		while(it.hasNext()) {
			String a = it.next();
			File file = new File(a);
			Path pth=file.toPath().normalize();
			if(!pth.isAbsolute()) pth=bp.resolve(pth);//get absolute path
			if(pth.startsWith(bp)) p.add(pth);//do not add if the final path does not start with the base path (contains "..")
		}


		return p;
	}
	
	private class FileChecksum extends FileCopyChecksum{

		public FileChecksum(File inputfile, File outputFile, boolean copy,
				boolean overwrite, boolean hashOutputFile,
				boolean compareFilesByteByByte, 
				Set<String> hashes,
				int BufferSize)
				throws Exception {
			super(inputfile, outputFile, copy, overwrite, hashOutputFile,
					compareFilesByteByByte, hashes, BufferSize);
		}
		
		protected void process(){super.process();}
		
//		protected RunnableStepByStepStatistics statusDifferences(){return super.statusDifferences();}
		
	}
	
	private class ProcessDirectoryTree extends RunnableStepByStep{
		/**
		 * key is the node level
		 * top node is level zero
		 * children have increasing level
		 */
		private TreeMultiMap<Integer,TreeNode<SnapshotNode, Identifiable>> processingNodes;
//		private int level;//biggest level in map
		private TreeNode<SnapshotNode, Identifiable> current=null;
//		private AtomicBoolean pr;
//		private volatile boolean pr;
		private final Integer rl;
		private final Set<String> ha;
		private final Set<String> filter;
		
		/**
		 * snapshot used in processing
		 */
		private SnapshotTree sn;
		
//		private volatile RunnableStepByStepStatistics sts;
		
//		private FileChecksum fcc;
		
		
//		public boolean processing() {return (pr.get())?true:false;}
		
//		public RunnableStepByStepStatistics statistics() {return sts;}
		
//		public SnapshotTree snapshot() {return (pr)?null:sn;}
		public SnapshotTree snapshot() {return (super.running())?null:sn;}
		
		public ProcessDirectoryTree(SnapshotNode base,Set<String> filterOut , Set<String> hashes, Integer recursion) throws Exception{
			sn=new SnapshotTree(base);
			processingNodes=new TreeMultiMap<>();
//			level=-1;//Integer.MIN_VALUE;
//			ArrayList<TreeNode<SnapshotNode, Identifiable>> a=new ArrayList<>();
//			a.add(snap.getTopNode());
			processingNodes.put(-1,sn.getTopNode());
			if(recursion>=0) rl=recursion;
			else rl=Integer.MAX_VALUE;
			filter=filterOut;
			ha=hashes;
//			fcc=null;
//			sts=new RunnableStepByStepStatistics();
//			pr=new AtomicBoolean(true);
//			pr=true;
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
		
		protected void step() throws Exception{
			if(processingNodes.size()<=0) {
				//all nodes have been processed
//				pr.set(false);
//				pr=false;
				super.finish();
//				sts=sts.modify(1,FIELD.PROCESSEDITEMS,false,"Done transversing.");
				super.statusModify(1,FIELD.PROCESSEDITEMS,false,"Done transversing.");
				return;// false;
			}
			Integer level = processingNodes.lastKey();
			//get the highest level first since lower levels (parents) need to be updated with size of children
//			current=processingNodes.get(level).first();
			TreeSet<TreeNode<SnapshotNode, Identifiable>> lst = processingNodes.get(level);
//			if(lst.size()<=0) {//no more nodes at this level
//				lst = processingNodes.get(level);
//			}
			current=lst.first();
			if(!sn.contains(current) || (sn.contains(current) && sn.getChildrenOf(current).size()<=0)) {//transversing directories
				//update highest level
//				bl=level;
				//get path
				String path;
				if(current.getData().getDescriptorType()==DescriptorType.SNAPSHOT){
					path=current.getData().getSnapshotDescriptor().getBasePath();
				}
				else{
					path=current.getData().getDirectoryDescriptor().getFullPath(_pathSeparator);
				}
//				if(rl==0 || level<rl){//must always check level zero so that the root is always calculated
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
						if(file.isDirectory()){
							//path is a directory
							//node can be a directory or snapshot
							if(current.getData().getDescriptorType()!=DescriptorType.SNAPSHOT){
								//it is not a snapshot. Snapshots do not have dates
								//replace data of the node in the snapshot tree to add dates
								data= new SnapshotNode(new DirectoryDescriptor(current.identifier(),current.getData().getDescriptorName(), current.getData().getDirectoryDescriptor().getPath(), size, ct, mt, at,null),null,true);
								sn=new SnapshotTree(sn, current, data);
							}
							//check if path has children. Only valid for: 1)snapshot or ; 2)directories that are not beyond the requested recursion level
//							if(current.getData().getDescriptorType()==DescriptorType.SNAPSHOT || current.getData().getDescriptorType()==DescriptorType.DIRECTORY && (rl==0 || level+1<rl)){
							if(current.getData().getDescriptorType()==DescriptorType.SNAPSHOT || current.getData().getDescriptorType()==DescriptorType.DIRECTORY && level<rl){
								String[] list=file.list();
								if(list!=null && list.length>0){
									for (int i = 0; i < list.length; i++) {
										//add child to the snapshot tree. Use DirectoryDescriptor because it is more generic
										data= new SnapshotNode(new DirectoryDescriptor(null,list[i], file.getPath(), null,LocalDateTime0,LocalDateTime0, LocalDateTime0,null),null,true);
										sn=new SnapshotTree(sn, current, data, 1, 0);
										//add child to the list of nodes to process
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
						else{// !file.isDirectory()
							if(file.isFile()){//it is a file
								//node was already inserted into the tree as a directory so it has to be replaced as file
								//and the size updated
								size=file.length();
								data=new SnapshotNode(new FileDescriptor(current.identifier(),current.getData().getDescriptorName(), current.getData().getDirectoryDescriptor().getPath(), size, ct, mt, at,null, new Hashes(ha)),null,true);
//								current=new TreeNode<SnapshotNode, Identifiable>(current, null, data, 0, 0, null);
								sn=new SnapshotTree(sn, current, data);
							}
							else{//not a file nor directory. May be a pipe, link, etc.
								//keep it as a directory in the tree
							}
							//remove entry from list of nodes to process
							processingNodes.remove(level, current);
						}
					}
//					return new Pair<String, Long>(path, Long.valueOf(size));
//					sts=sts.modify(1,FIELD.TOTALITEMS,false,"Processing "+path);
//					sts=sts.modify(size,FIELD.TOTALSIZE,false,null);
//					super.statusModify(1,FIELD.TOTALITEMS,false,"Processing "+path);
//					super.statusModify(size,FIELD.TOTALSIZE,false,null);
					super.status(super.status().modify(1,FIELD.TOTALITEMS,false,"Processing "+path).modify(size,FIELD.TOTALSIZE,false,null));
//					return true;
				}
				else{//(recursionlevel!=0 && snap.getNodeLevel(current)>recursionlevel)
					//skip node since path depth is greater than recursion level
					//but remove it and its children from tree since they were previously added
					sn=new SnapshotTree(sn, current, true, true);
//					return new Pair<String, Long>(path, Long.valueOf(0));
//					sts=sts.modify(1,FIELD.TOTALITEMS,false,"Processing "+path);
					super.statusModify(1,FIELD.TOTALITEMS,false,"Processing "+path);
					//remove entry from list of nodes to process
					processingNodes.remove(level, current);
//					return true;
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
//						sts=sts.modify(cnt,FIELD.PROCESSEDITEMS,false,"Updating "+current.getData().getDirectoryDescriptor().getFullPath(_pathSeparator));
						super.statusModify(cnt,FIELD.PROCESSEDITEMS,false,"Updating "+current.getData().getDirectoryDescriptor().getFullPath(_pathSeparator));
//						return true;
					}
					else{// if(current.getNode().getNodeType()==NodeType.SNAPSHOT){
						//snapshot descriptor is the last to be calculated
						e=new SnapshotNode(new SnapshotDescriptor(current.getData().getSnapshotDescriptor(), true, null, null, calc, -1,null,null,size), null,false,true);
						//replace on tree
						sn=new SnapshotTree(sn, current, e);
//						sts=sts.modify(cnt,FIELD.PROCESSEDITEMS,false,"Updating "+current.getData().getSnapshotDescriptor().getBasePath());
						super.statusModify(cnt,FIELD.PROCESSEDITEMS,false,"Updating "+current.getData().getSnapshotDescriptor().getBasePath());
//						return true;
					}
				}
			}
//			return true;
		}

		@Override
		protected void process(){super.process();}
		
		@Override
		protected void initialize() throws Exception {}

		@Override
		protected void endStep() throws Exception {}
	}

	private class ProcessFiles extends RunnableStepByStep{
		/**
		 * key is the node level
		 * top node is level zero
		 * children have increasing level
		 */
		private ArrayList<TreeNode<SnapshotNode, Identifiable>> processingNodes;
		private TreeNode<SnapshotNode, Identifiable> current=null;
//		private volatile boolean pr;
		private final Set<String> ha;
		/**
		 * snapshot used in processing
		 */
		private SnapshotTree sn;
		
//		private RunnableStepByStepStatistics sts;
		
		private FileChecksum fcc;
		
		private DifferentialHistory<RunnableStepByStepStatistics>.HistoryMarker mrk;
		
//		public boolean processing() {return (pr.get())?true:false;}
		
//		public RunnableStepByStepStatistics statistics() {return sts;}
		
		public SnapshotTree snapshot() {return (super.running())?null:sn;}
		
		public ProcessFiles(SnapshotTree base,RunnableStepByStepStatistics stat, Set<String> hashes) throws Exception {
			sn=base;
			processingNodes=new ArrayList<>();
			processingNodes.addAll(sn.getFiles());
			ha=hashes;
			fcc=null;
			mrk=null;
			super.status(stat);
//			sts=stat;
//			pr=true;
		}
		
		protected void step() throws Exception{
//			if(processingNodes.size()<=0) {
//				//all nodes have been processed
//				pr=false;
//				sts.modify(fcc.statusDifferences().getProcessedSize(),FIELD.PROCESSEDSIZE,false,"Done");
//				return false;
//			}
//			else{
				if(fcc==null) {//no file being processed and there are files waiting
					if(processingNodes.size()<=0) {
						//all nodes have been processed
//						pr=false;
						super.finish();
//						sts.modify(-1,FIELD.NONE,false,"Done");
						super.statusModify(-1,FIELD.NONE,false,"Done.");
						return;// false;
					}
					current=processingNodes.remove(0);
//					System.out.println(current.getData().getFileDescriptor().getFullPath(_pathSeparator));
					try {
						fcc=new FileChecksum(
								new File(current.getData().getFileDescriptor().getFullPath(_pathSeparator)),
								null,
								false,
								false, 
								false,
								false,
								ha,
								bufferSize
								);
						mrk=fcc.marker();
					}catch(IOException e) {//can not read given file
						//skip file data
						super.statusModify(current.getData().getDescriptorSize(),FIELD.PROCESSEDSIZE,false,e.getMessage());
						//add to snapshot tree as unreadable
						snap=new SnapshotTree(
								snap, 
								current, 
								new SnapshotNode(
										new FileDescriptor(
												current.getData().getFileDescriptor(), 
												true, 
												null, 
												null, 
												-1, 
												null, 
												null, 
												null, 
												false,
												null
												),
												null,
												true
										)
								);
					}
				}
				else {
					fcc.process();
//					sts=sts.add(sts, fcc.statusDifferences());
//					sts=sts.modify(fcc.statusDifferences().getProcessedSize(),FIELD.PROCESSEDSIZE,false,fcc.statusDifferences().getAction());
//					if(fcc.statusChanged()) super.statusAdd(fcc.statusDifferences());
//					if(fcc.statusChanged()) {
					if(mrk.changed()) {
//						RunnableStepByStepStatistics st = fcc.statusDifferences();
						RunnableStepByStepStatistics st = mrk.getAndUpdate();
//						super.statusModify(st.getProcessedSize(),FIELD.PROCESSEDSIZE,false,(st.getAction().equals(""))?null:st.getAction());
						super.statusModify(mrk.reference().getProcessedSize()-st.getProcessedSize(),FIELD.PROCESSEDSIZE,false,st.getAction());
					}
					if(!fcc.running()){	//go to next state
						snap=new SnapshotTree(
								snap, 
								current, 
								new SnapshotNode(
										new FileDescriptor(
												current.getData().getFileDescriptor(), 
												true, 
												null, 
												null, 
												-1, 
												null, 
												null, 
												null, 
												fcc.canReadInputFile(),
												fcc.getInputFile()
												),
												null,
												true
										)
								);
//						sts=sts.modify(fcc.statusDifferences().getProcessedSize(),FIELD.PROCESSEDSIZE,false,fcc.statusDifferences().getAction());
						mrk=null;
						fcc=null;
					}
				}
//			}
//			return true;
		}

		@Override
		protected void process(){super.process();}
		
		@Override
		protected void initialize() throws Exception {}

		@Override
		protected void endStep() throws Exception {}
	}

	private static enum STATES {IDLE,INIT,PROCESS_DIRECTORY,UPDATE_SIZES,GET_FILES,INIT_CHECKSUM,PROCESS_CHECKSUM,INIT_UPDATE,PROCESS_UPDATE,FINALIZE};
	
	private final String name;
	
	/**
	 * base path to process
	 */
	private final String _basePath;
	
	/**
	 * snapshot used in processing
	 */
	private SnapshotTree snap=null;
	private ProcessDirectoryTree pdt;
	private ProcessFiles pf;
	private DifferentialHistory<RunnableStepByStepStatistics>.HistoryMarker mrk;
	
	/**
	 * dummy snapshot that is returned while thread is processing
	 */
	private final SnapshotTree snapDummy;
	
//	private final Integer _recursionlevel;
	private final String _pathSeparator;
	private final int bufferSize;
	
	/**
	 * current state of the state machine
	 */
	private STATES state=STATES.IDLE;
	
//	private ArrayList<TreeNode<SnapshotNode, Identifiable>> processingNodes;
//	private TreeNode<SnapshotNode, Identifiable> current=null;
	
//	private FileChecksum fcc=null;
	private final Set<String> calc;
	
	/**
	 * @param snapshotName
	 * @param basePath
	 * @param filterOut
	 * @param recursion >0 walks down the folder path until its depth equals recursion, otherwise ignores path depth
	 * @param BufferSize
	 * @throws Exception
	 */
	public TransverseDirectories(
			final String snapshotName,
			final String basePath,
			final Set<String> filterOut,
			final Set<String> hashes,
			final int recursion,
			final int BufferSize
			) throws Exception{
		super();
		name=snapshotName;
		_basePath=basePath;
//		if(recursion>=0)_recursionlevel=recursion;
//		else _recursionlevel=Integer.MAX_VALUE;
		_pathSeparator="/";
		calc=jrain.hash.hashInstance.hashes.Hashes.validateHashes(hashes);
		bufferSize=FileCopyChecksum.calculateBufferSize(BufferSize);
		SnapshotDescriptor sd=new SnapshotDescriptor(null,name, _basePath, calc, 0,null,null,0);
//		snap=new SnapshotTree(new SnapshotNode(sd, null, false, true));
		pdt=new ProcessDirectoryTree(new SnapshotNode(sd, null, false, true),(filterOut==null)?new HashSet<>():filterOut, calc, (recursion>=0)?recursion:Integer.MAX_VALUE);
		pf=null;
		mrk=pdt.marker();
		snapDummy=new SnapshotTree(new SnapshotNode(sd, null, true, true));
		state=STATES.INIT;
		super.go();
	}
	
	/**
	 * the snapshot can only be read after completion (running method returns false)
	 * If an attempt is performed before completion a tree with only the 
	 * snapshot node is returned
	 * 
	 * @return
	 * @throws ExceptionInvalidValue 
	 * @throws NullPointerException 
	 * @throws ExceptionProcessing 
	 */
	public SnapshotTree getSnapshot() throws NullPointerException{
		if(super.running()) return snapDummy;
		return snap;
	}
	
//	private long processDirectory() throws NullPointerException{
////		System.out.println(snap.getNodes());
//		current=processingNodes.remove(0);
////		System.out.println(snap.getNodeLevel(current)+" "+current.getNode().getDescriptorName());
//		int level=snap.getNodeLevel(current);
//		if(_recursionlevel==0 || level<_recursionlevel){
//			String path;
//			if(current.getData().getDescriptorType()==DescriptorType.SNAPSHOT){
//				path=current.getData().getSnapshotDescriptor().getBasePath();
//			}
//			else{
//				path=current.getData().getDirectoryDescriptor().getFullPath(_pathSeparator);
//			}
////			System.out.println(current+" ; "+path);
//	//		System.out.println(SnapshotTree.printTreeSimple(snap));
//			super.action("Processing "+path);
//			File file = new File(path);
//			long size=0;
//			if(!Files.isSymbolicLink(file.toPath())){//only process file if it is not a symbolic link
//				SnapshotNode data=null;
//				LocalDateTime ct=null;
//				LocalDateTime at=null;
//				LocalDateTime mt=null;
//				try {
//					BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class,LinkOption.NOFOLLOW_LINKS);
//					ct=LocalDateTime.ofInstant(attr.creationTime().toInstant(), ZoneId.systemDefault());
//					at=LocalDateTime.ofInstant(attr.lastAccessTime().toInstant(), ZoneId.systemDefault());
//					mt=LocalDateTime.ofInstant(attr.lastModifiedTime().toInstant(), ZoneId.systemDefault());
//				} catch (IOException e) {
//					ct=LocalDateTime0;
//					at=LocalDateTime0;
//					mt=LocalDateTime0;
//				}
//				if(file.isDirectory()){
//					if(current.getData().getDescriptorType()==DescriptorType.SNAPSHOT){
//	//					data= new SnapshotNode(new DirectoryDescriptor(null,current.getNode().getDescriptorName(), current.getNode().getDirectoryDescriptor().getPath(), size, ct, mt, at),null,true);
//					}
//					else{
//						data= new SnapshotNode(new DirectoryDescriptor(current.identifier(),current.getData().getDescriptorName(), current.getData().getDirectoryDescriptor().getPath(), size, ct, mt, at,null),null,true);
//						snap=new SnapshotTree(snap, current, data);
//					}
//					if(current.getData().getDescriptorType()==DescriptorType.SNAPSHOT || current.getData().getDescriptorType()==DescriptorType.DIRECTORY && (_recursionlevel==0 || level+1<_recursionlevel)){
//						//only list children if they have a level smaller than the selected recursion level
//						String[] list=file.list();
//						if(list!=null){
//							for (int i = 0; i < list.length; i++) {
//								data= new SnapshotNode(new DirectoryDescriptor(null,list[i], file.getPath(), 0,LocalDateTime0,LocalDateTime0, LocalDateTime0,null),null,true);
//								processingNodes.add(new TreeNode<SnapshotNode, Identifiable>(current,data,1,0, new HashSet<Identifiable>()));
//								snap=new SnapshotTree(snap, current, data, 1, 0);
//				//				System.out.println(SnapshotTree.printTreeSimple(snap));
//							}
//						}
//					}
//				}
//				else{
//					if(file.isFile()){//it is a file
//						//node was already inserted into the tree as a directory so it has to be replaced as file
//						size=file.length();
//						data=new SnapshotNode(new FileDescriptor(current.identifier(),current.getData().getDescriptorName(), current.getData().getDirectoryDescriptor().getPath(), size, ct, mt, at,null, new Hashes(calc)),null,true);
//						snap=new SnapshotTree(snap, current, data);
//					}
//					else{//not a file nor directory. May be a pipe, link, etc.
//						//keep it as a directory in the tree
//					}
//				}
//			}
//	//		System.out.println(snap.getNodes());
//			return size;
//		}
//		else{//(_recursionlevel!=0 && snap.getNodeLevel(current)>_recursionlevel)
//			//skip node since path depth is greater than recursion level
//			//but remove it and its children from tree since they were previously added
//			snap=new SnapshotTree(snap, current, true, true);
//		}
//		return 0;
//	}
	
	protected void step() throws Exception{
		if(state==STATES.IDLE)return;
		if(state==STATES.INIT){
//			System.out.println("Processing");
//			super.action("Processing");
//			super.totalSize(0);
////			processingNodes=new ArrayList<>();
////			processingNodes.add(snap.getTopNode());
//			super.processedSize(0);
//			super.statusModify(-1, FIELD.NONE, true, "Processing");
			pdt.go();
			state=STATES.PROCESS_DIRECTORY;
		}
		else if(state==STATES.PROCESS_DIRECTORY){	//process each directory
//			System.out.println(SnapshotTree.print(snap, snap.getTopNode()));
			pdt.process();
			if(pdt.running()) {
//				if(pdt.statusChanged()) super.status(pdt.getStatus());
				if(mrk.changed()) super.status(mrk.getAndUpdate());
			}
			else{	//go to next state
//				super.status(pdt.getStatus());
				super.status(mrk.getAndUpdate());
				snap=pdt.snapshot();
				pdt=null;//no longer needed. Let memory be freed
//				state=STATES.UPDATE_SIZES;
				state=STATES.GET_FILES;
//				System.out.println(snap.getNodes());
//				System.out.println(SnapshotTree.print(snap, snap.getTopNode()));
//				System.out.println((new SnapshotTreeXML(snap)).getXML());
			}
		}
		else if(state==STATES.UPDATE_SIZES){	//update sizes of nodes
//			if(processingNodes.isEmpty()){
//				super.processedSize(0);
//				processingNodes=new ArrayList<>(snap.getFiles());
//			}
//			else{
//				current=processingNodes.remove(0);
//				Identifiable n = current.getParent();
//				Iterator<TreeNode<SnapshotNode, Identifiable>> it=null;
//				SnapshotNode e=null;
//				if(n!=null){
//					TreeNode<SnapshotNode, Identifiable> p=snap.getNode(n);	//get parent
//					if(!processingNodes.contains(p)){//add to the list
//						processingNodes.add(p);	//add in order to update its parent
//					}
//				}
//				if(current.getData().getDescriptorType()==DescriptorType.FILE){
//				}
//				else{//current is not a file. Is directory or snapshot
//					//update size in the tree
//					//get all children and add their sizes
//					it=snap.getChildrenOf(current).iterator();
//					int size=0;
//					while(it.hasNext()){
//						e=it.next().getData();
//						size+=e.getDescriptorSize();
//					}
//					if(current.getData().getDescriptorType()==DescriptorType.DIRECTORY){
//						e=new SnapshotNode(new DirectoryDescriptor(current.getData().getDirectoryDescriptor(), true, null, null, size,null,null,null,null), null,true);
//						
//					}
//					else{// if(current.getNode().getNodeType()==NodeType.SNAPSHOT){
//						//snapshot descriptor is the last to be calculated
//						e=new SnapshotNode(new SnapshotDescriptor(current.getData().getSnapshotDescriptor(), true, null, null, calc, -1,null,null,size), null,false,true);
//						state=STATES.GET_FILES;//go to next state
//					}
//					//replace on tree
//					snap=new SnapshotTree(snap, current, e);
//				}
//			}
			state=STATES.GET_FILES;//go to next state
		}
		else if(state==STATES.GET_FILES){	//checksum each file
			if(calc==null || calc.size()<=0){
				//no checksum to calculate
				state=STATES.FINALIZE;
			}
			else{
//				super.processedSize(0);
//				processingNodes=new ArrayList<>(snap.getFiles());
				pf=new ProcessFiles(snap, super.status(), calc);
				mrk=pf.marker();
				pf.go();
//				state=STATES.INIT_CHECKSUM;
				state=STATES.PROCESS_CHECKSUM;
			}
//			System.out.println(processingNodes);
		}
//		else if(state==STATES.INIT_CHECKSUM){
//			if(processingNodes.isEmpty()){
//				super.action("Done.");
//				super.processedSize(super.getTotalSize());
//				state=STATES.FINALIZE;
//			}
//			else{
////				System.out.println("current="+current);
//				current=processingNodes.remove(0);
////				System.out.println("checksum="+current);
////				System.out.println(processingNodes);
//					fcc=new FileChecksum(
//							new File(current.getData().getFileDescriptor().getFullPath(_pathSeparator)),
//							null,
//							false,
//							false, 
//							false,
//							false,
//							calc,
//							bufferSize
//							);
//					state=STATES.PROCESS_CHECKSUM;
//			}
//		}
		else if(state==STATES.PROCESS_CHECKSUM){
//			fcc.process();
//			if(fcc.processedSizeChanged()){
//				super.processedSize(super.processedSize()+fcc.getProcessedSizeIncrement());
//			}
//			if(fcc.currentActionChanged()){
//				super.action(fcc.getCurrentAction());
//			}
//			if(!fcc.running()){	//go to next state
//				snap=new SnapshotTree(
//						snap, 
//						current, 
//						new SnapshotNode(
//								new FileDescriptor(
//										current.getData().getFileDescriptor(), 
//										true, 
//										null, 
//										null, 
//										-1, 
//										null, 
//										null, 
//										null, 
//										fcc.canReadInputFile(),
//										fcc.getInputFile()
//										),
//										null,
//										true
//								)
//						);
//				state=STATES.INIT_CHECKSUM;	//checksum the next file
//			}
			pf.process();
			if(pf.running()) {
//				if(pf.statusChanged()) super.status(pf.status());
				if(mrk.changed()) super.status(mrk.getAndUpdate());
			}
			else{	//go to next state
//				super.status(pf.status());
				super.status(mrk.getAndUpdate());
				snap=pf.snapshot();
				pf=null;//no longer needed. Let memory be freed
				state=STATES.FINALIZE;
			}
		}
		else if(state==STATES.FINALIZE){	//finalize
			super.statusModify(-1, FIELD.NONE, true, "Done");
			super.finish();
		}
	}

	@Override
	protected void initialize() throws Exception {
		//does nothing
	}

	@Override
	protected void endStep() throws Exception {
		if(snap!=null){
			super.validate();
		}
	}

	/* (non-Javadoc)
	 * @see jrain.dataTypes.immutable.Identifiable_I#getUUID()
	 * 
	 * Return the information from the Snapshot not the SnapshotsGroup
	 */
	@Override
	public UUID identifier() {
		if(super.running()) return snapDummy.identifier();
		return snap.identifier();
	}

	/* (non-Javadoc)
	 * @see jrain.dataTypes.immutable.Identifiable_I#getObjectType()
	 * 
	 * Return the information from the Snapshot not the SnapshotsGroup
	 */
	@Override
	public String objectType() {
		if(super.running()) return snapDummy.objectType();
		return snap.objectType();
	}

	/* (non-Javadoc)
	 * @see jrain.dataTypes.immutable.Identifiable_I#getID()
	 * 
	 * Return the information from the Snapshot not the SnapshotsGroup
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Identifiable identifiable() {
		if(super.running()) return snapDummy.identifiable();
		return snap.identifiable();
	}

	/* (non-Javadoc)
	 * @see jrain.dataTypes.immutable.Identifiable_I#matches(jrain.dataTypes.immutable.Identifiable)
	 * 
	 * Return the information from the Snapshot not the SnapshotsGroup
	 */
	@Override
	public <T extends jrain.identifiable.Identifiable<UUID> > boolean matches(T ID) {
		if(super.running()) return snapDummy.matches(ID);
		return snap.matches(ID);
	}

//	public TaggedTable asTaggedTable() {
//		if(super.running()){
//			return snapDummy.asTaggedTable();
//		}
//		TaggedTable s = snap.get().asTaggedTable();
//		//update hashes calculate columns
//		Iterator<String> it = snap.get().getTopNode().getData().getSnapshotDescriptor().hashes().iterator();
//		while(it.hasNext()) {
//			String n = it.next();
//			TaggedTableCoreCell<String, TaggedTableColumnData<PolyType<?>>> c = s.getColumn(SnapshotDescriptor.SNAPSHOTCALCULATETAG+n);
//			TaggedTableColumnData<PolyType<?>> d = c.getValue();
//			d.value(new BooleanPoly(true));
//			s.addColumn(SnapshotDescriptor.SNAPSHOTCALCULATETAG+n, d, true);
//		}
//		return s;
//	}
	
//	@Override
//	public String toString() {
//		return asTaggedTable().toString();
//	}
	
			
}
