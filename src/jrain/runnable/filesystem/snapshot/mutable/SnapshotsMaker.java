package jrain.runnable.filesystem.snapshot.mutable;

import java.io.IOException;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import jrain.filesystem.snapshot.immutable.SnapshotsGroupDescriptor;
import jrain.filesystem.snapshot.snapshotTree.immutable.SnapshotNode;
import jrain.filesystem.snapshot.snapshotTree.immutable.SnapshotTree;
import jrain.hash.hashInstance.hashes.immutable.Hashes;
import jrain.runnable.filesystem.snapshot.xml.immutable.SnapshotTreeInputXML;
import jrain.runnable.filesystem.snapshot.xml.immutable.SnapshotTreeOutputXML;
import jrain.deepCopy.DeepCopy;
import jrain.exceptions.ExceptionProcessing;
import jrain.differentialHistory.immutable.DifferentialHistory;
import jrain.differentialHistory.immutable.DifferentialHistory.HistoryMarkerSet;
import jrain.identifiable.immutable.Identifiable;
import jrain.treeIdentifiable.immutable.TreeNode;
import jrain.entry.mutable.Pair;
import jrain.runnable.RunnableStepByStep;
import jrain.runnable.RunnableStepByStepStatistics;
import jrain.runnable.RunnableStepByStepStatistics.FIELD;

/**
 * @author poltergeist_00dead
 *
 * Each instance of this class corresponds to a snapshots group instance.
 * It contains methods to handle snapshots by allowing to add/remove
 * snapshots/directories/files, read/save snapshots from/to xml file.
 */
public class SnapshotsMaker extends RunnableStepByStep implements jrain.identifiable.Identifiable<UUID>{

	private class TransverseDir extends TransverseDirectories{

		public TransverseDir(String snapshotName, String basePath,
				Set<String> filterOut, 
				Set<String> hashes,
				int recursion, int BufferSize) throws Exception {
			super(snapshotName, basePath, filterOut, hashes, recursion,
					BufferSize);
		}
		
		protected void process(){
//			System.out.print("TransverseDir status="+super.getStatus());
			super.process();
//			System.out.println(" > "+super.getStatus());
		}

//		public boolean statusChanged(){
//			boolean b=super.statusChanged();
//			System.out.println("transverseDir status changed="+b);
//			return b;
//		}
//
//		protected RunnableStepByStepStatistics marker() {return super.marker();}
//		
//		protected void status(RunnableStepByStepStatistics sts) {super.status(sts);}
//		
//		protected RunnableStepByStepStatistics statusDifferences(){return super.statusDifferences();}
	}
	
	private class InputXML extends SnapshotTreeInputXML{

		public InputXML(String basePath) throws Exception {
			super(basePath);
		}
		
		protected void process(){super.process();}
		
//		protected RunnableStepByStepStatistics marker() {return super.marker();}
//		
//		protected void status(RunnableStepByStepStatistics sts) {super.status(sts);}
//		
//		protected RunnableStepByStepStatistics statusDifferences(){return super.statusDifferences();}
	}
	
	private class OutputXML extends SnapshotTreeOutputXML{

		public OutputXML(SnapshotTree tree, OutputStream out) throws Exception {
			super(tree,out);
		}
		
		public OutputXML(SnapshotTree tree, String filePath, boolean append) throws Exception {
			super(tree,filePath,append);
		}
		
		protected void process(){super.process();}
		
//		protected RunnableStepByStepStatistics marker() {return super.marker();}
//		
//		protected void status(RunnableStepByStepStatistics sts) {super.status(sts);}
//		
//		protected RunnableStepByStepStatistics statusDifferences(){return super.statusDifferences();}
	}
	
	private class ProcessDuplicates extends RunnableStepByStep{
//		private AtomicBoolean processDuplicates;
		
		private ArrayList<Identifiable> processingDuplicates;
		/**
		 * used to process only N comparisons at a time since the total amount of 
		 * files to process may be too long and freeze the program for too long
		 */
		private int processingDuplicatesBufferIndex;
		private final int processingDuplicatesBufferSize=1000;
		private Identifiable currentDuplicate;
		private long processingDuplicatesIncrement;
		/**
		 * key is the node level
		 * top node is level zero
		 * children have increasing level
		 */
//		private TreeMultiMap<Integer,TreeNode<SnapshotNode, Identifiable>> processingNodes;
//		private int bl;//biggest level in map
//		private TreeNode<SnapshotNode, Identifiable> current=null;
////		private AtomicBoolean pr;
//		private volatile boolean pr;
//		private final Integer rl;
//		private final Set<String> ha;
		/**
		 * snapshot used in processing
		 */
		private SnapshotTree sn;
		
//		private volatile RunnableStepByStepStatistics sts;
		
//		private FileChecksum fcc;
		
		
//		public boolean processing() {return (pr.get())?true:false;}
		
//		public RunnableStepByStepStatistics statistics() {return sts;}
		
		public SnapshotTree snapshot() {return (super.running())?null:sn;}
		
		public ProcessDuplicates(SnapshotTree base) throws Exception{
			sn=new SnapshotTree(base);
//			pr=true;
			processingDuplicates=new ArrayList<>(sn.getFiles());
			processingDuplicatesBufferIndex=0;
			processingDuplicatesIncrement=(long) ((Math.pow(processingDuplicates.size(), 2.0)-processingDuplicates.size())/2);
			super.statusModify(processingDuplicatesIncrement,FIELD.TOTALITEMS,true,"Processing duplicates...");
			super.go();
		}
		
		/**
		 * Makes triangular comparison of files to find duplicates (first compares 
		 * to second and beyond, second compares to third and beyond, etc).
		 * 
		 * @return false if there is nothing to process
		 */
		protected void step(){
			if(processingDuplicates.size()<=0){
				//nothing to do. go to sleep
//				pr=false;
				super.finish();
				super.statusModify(-1,FIELD.NONE,false,"Done processing duplicates.");
				return;
			}
			if(processingDuplicatesBufferIndex<=0) {
				currentDuplicate = processingDuplicates.remove(0);
			}
			int endIndex=processingDuplicatesBufferIndex+processingDuplicatesBufferSize;
			if(endIndex>processingDuplicates.size()) endIndex=processingDuplicates.size();
			int dupInd=processingDuplicatesBufferIndex;
			while(dupInd<endIndex) {//process buffer
				Identifiable isn1 = currentDuplicate;
				Identifiable isn2=processingDuplicates.get(dupInd);
				SnapshotNode sn1=sn.getNode(isn1).getData();
				SnapshotNode sn2=sn.getNode(isn2).getData();
				Hashes h1=sn1.getFileDescriptor().getHashes();
				Hashes h2=sn2.getFileDescriptor().getHashes();
				if(h1.hasHashes() && h2.hasHashes()){
					if(h1.equals(h2)){
						//they are duplicates
						//replace sn1
						SnapshotNode snn=new SnapshotNode(sn1, sn2,true);
						sn=new SnapshotTree(sn, sn1, snn);
						//replace sn2
						snn=new SnapshotNode(sn2,snn,true);
						sn=new SnapshotTree(sn, sn2, snn);
					}
				}
				++dupInd;
			}
//			super.processedSize(super.processedSize()+processingDuplicatesIncrement*(endIndex-processingDuplicatesBufferIndex));
			super.statusModify(endIndex-processingDuplicatesBufferIndex,FIELD.PROCESSEDITEMS,false,null);
			if(endIndex>=processingDuplicates.size()) processingDuplicatesBufferIndex=0;
			else processingDuplicatesBufferIndex=endIndex;
//			return true;
		}

		protected void process(){super.process();}
		
//		public boolean statusChanged(){
//			boolean b=super.statusChanged();
//			System.out.println("duplicates status changed="+b);
//			return b;
//		}
//
//		protected RunnableStepByStepStatistics marker() {return super.marker();}
//		
//		protected void status(RunnableStepByStepStatistics sts) {super.status(sts);}
//		
//		protected RunnableStepByStepStatistics statusDifferences(){return super.statusDifferences();}
		
		@Override
		protected void initialize() throws Exception {}

		@Override
		protected void endStep() throws Exception {}
	}

	private static enum STATES {IDLE,INIT_SNAPSHOTS,INIT_DUPLICATES,PROCESS_SNAPSHOTS,PROCESS_DUPLICATES,LOAD,SAVE,FINALIZE};
		
	private ConcurrentHashMap<Identifiable,Entry<TransverseDir,DifferentialHistory<RunnableStepByStepStatistics>.HistoryMarker>> snapshots;
	private HistoryMarkerSet<Identifiable, RunnableStepByStepStatistics> markers;
//	private ConcurrentHashMap<Identifiable,RunnableStepByStepStatistics> snapshotsStatistics;
	
	private CopyOnWriteArrayList<Identifiable> processingSnapshots;
	
	private AtomicBoolean processDuplicates;
	
	private AtomicBoolean saveToFile;
	private AtomicBoolean saveAppend;
	
	private InputXML snapshotsLoad;
	private OutputXML snapshotsSave;
	
//	private AtomicBoolean processingDuplicates;
//	private ArrayList<Identifiable> processingDuplicates;
	private ProcessDuplicates duplicates;
	
	private DifferentialHistory<RunnableStepByStepStatistics>.HistoryMarker mrk;//marker for load/save/process duplicates
	
	/**
	 * used to process only N comparisons at a time since the total amount of 
	 * files to process may be too long and freeze the program for too long
	 */
//	private int processingDuplicatesBufferIndex;
//	private final int processingDuplicatesBufferSize=1000;
//	private Identifiable currentDuplicate;
//	private long processingDuplicatesIncrement;
	
	private String savePath;
	
	/**
	 * snapshot used in processing
	 */
	private AtomicReference<SnapshotTree> snap=null;
	
	/**
	 * current state of the state machine
	 */
	private STATES state;
	
	private AtomicBoolean mdf;//true if the tree was modified since last save
	
	/**
	 * construct a new snapshots group tree
	 * 
	 * @param basePath
	 * @param filterOut
	 * @param recursion
	 * @param BufferSize
	 * @throws Exception 
	 */
	public SnapshotsMaker(final SnapshotsGroupDescriptor snapshotsGroupDescriptor) throws Exception{
		super();
		mdf=new AtomicBoolean(true);
		snapshots=new ConcurrentHashMap<>();
		markers=new HistoryMarkerSet<>();
//		snapshotsStatistics=new ConcurrentHashMap<>();
		processingSnapshots=new CopyOnWriteArrayList<>();
		processDuplicates=new AtomicBoolean(false);
//		processingDuplicates=new AtomicBoolean(false);
		duplicates=null;
		snap=new AtomicReference<SnapshotTree>(new SnapshotTree(new SnapshotNode(snapshotsGroupDescriptor)));
		savePath=null;
		saveToFile=new AtomicBoolean(false);
		saveAppend=new AtomicBoolean(false);
		state=STATES.IDLE;
		super.go();
	}
	
	/**
	 * construct a snapshots group tree from an xml file
	 * 
	 * @param pathToXML
	 * @throws Exception 
	 */
	public SnapshotsMaker(String pathToXML) throws Exception{
		super();
		mdf=new AtomicBoolean(false);
		snapshots=new ConcurrentHashMap<>();
		markers=new HistoryMarkerSet<>();
//		snapshotsStatistics=new ConcurrentHashMap<>();
		processingSnapshots=new CopyOnWriteArrayList<>();
		processDuplicates=new AtomicBoolean(false);
//		processingDuplicates=new AtomicBoolean(false);
		duplicates=null;
		saveToFile=new AtomicBoolean(false);
		saveAppend=new AtomicBoolean(false);
		savePath=pathToXML;
		if(savePath==null) throw new NullPointerException("File name is null");
		//TODO change following code to read file in chunks instead of all at once
//		File f=new File(savePath);
//		FileReader fo=new FileReader(f);
//		char[] b=new char[(int)f.length()];
//		fo.read(b);
//		SnapshotTreeInputXML sx=new SnapshotTreeInputXML(new String(b,offset,((int)f.length())-offset));
//		snap=new AtomicReference<SnapshotTree>(sx.getSnapshot());
//		fo.close();
		snapshotsLoad=new InputXML(savePath);
		mrk=snapshotsLoad.marker();
//		snap=null;
		//create a dummy snapshot tree just to get a fixed identifier. Tree will be replaced later when loading is finished
		snap=new AtomicReference<SnapshotTree>(new SnapshotTree(new SnapshotNode(new SnapshotsGroupDescriptor(snapshotsLoad.identifier(), "noname", pathToXML, 0))));
		state=STATES.LOAD;
		super.go();
	}
	
	/**
	 * Save the current snapshot tree to a file
	 * @param pathToXML
	 * @return the filename the snapshot tree was effectively written to
	 * 
	 * @throws ExceptionProcessing 
	 * @throws ExceptionInvalidValue 
	 * @throws NullPointerException 
	 * @throws IOException 
	 */
	public Boolean save(String pathToXML, boolean append) throws NullPointerException, IOException, ExceptionProcessing{
		TreeNode<SnapshotNode, Identifiable> t = snap.get().getTopNode();
		SnapshotNode n = t.getData();
		SnapshotsGroupDescriptor d = n.getSnapshotsGroupDescriptor();
		String a = null;
		if(pathToXML==null){
			if(savePath!=null) {//use the path set on this class
				a=savePath;
			}
			else { //save on the file indicated on the group
				a = d.getFilename();
				if(a==null)throw new NullPointerException("File name is null");
			}
		}
		else{
			//save to pathToXML
			a=pathToXML;
			//save the new path to the group
			snap.set(new SnapshotTree(snap.get(), t,new SnapshotNode(new SnapshotsGroupDescriptor(d, true, null, a, -1))));
		}
		savePath=a;
//		FileWriter fo=new FileWriter(a,append);
//		fo.write((new SnapshotTreeInputXML(snap.get())).getXML());
//		fo.close();
//		mdf.set(false);
//		return a;
		saveToFile.set(true);
		saveAppend.set(append);
		super.go();
		return true;
	}
	
	public String toString(){
//		try {
//			return (new SnapshotTreeInputXML(snap.get())).getXML();
//		} catch (ExceptionProcessing e) {
//			e.printStackTrace();
//		}
		//on error fall back to the default
		return snap.toString();
	}
	
	public Identifiable addSnapshot(
			final String snapshotName,
			final String basePath,
			final Set<String> filterOut,
			Set<String> hashes,
			final int recursion,
			final int BufferSize
			) throws Exception{
		TransverseDir t=new TransverseDir(snapshotName, basePath, filterOut, hashes, recursion, BufferSize);
//		ConcurrentHashMap<Identifiable,TransverseDir> a = snapshots;
//		a.put(t.identifiable(),t);
//		snapshots.set(a);
		snapshots.put(t.identifiable(),new Pair<>(t, t.marker()));
		markers.put(t.identifiable(), t.marker());
//		snapshotsStatistics.put(t.identifiable(), new RunnableStepByStepStatistics());
		processingSnapshots.add(t.identifiable());
		mdf.set(true);
		t.go();
		super.go();	//restart thread if it was paused. Multiple calls will have no increased effect over one call
		snap.set(new SnapshotTree(snap.get(), snap.get().getTopNode(), t.getSnapshot(),1,0));
		return t.getSnapshot();
	}
		
	public SnapshotTree getSnapshotTree(){
		return snap.get();
	}
	
	public boolean add(Identifiable parent, SnapshotTree subtree) throws NullPointerException{
		SnapshotNode sn1=snap.get().getNode(parent).getData();
//		SnapshotNode sn2=snap.get().getNode(subtree.getTopNode().getNode()).getNode();
		SnapshotNode sn2=subtree.getTopNode().getData();
		if(sn1.canHaveChild(sn2)){
			snap.set(new SnapshotTree(snap.get(), sn1, subtree,1,0));
			mdf.set(true);
			return true;
		}
		return false;
	}
	
	/**
	 * @param node
	 * @return null if nothing is done, or a tree with the nodes of the deleted subtree
	 */
	public SnapshotTree delete(Identifiable node){
		//if anything goes wrong do not delete and just return
		try {
			//do not delete the top node. Let the caller delete this object instead
			if(!node.matches(snap.get().getTopNode())){
				//get the subtree of the nodes to delete
				SnapshotTree t;
				t = new SnapshotTree(snap.get(), node);
				//delete the subtree
				snap.set(new SnapshotTree(snap.get(), node, true,false));
				mdf.set(true);
				return t;
			}
		} catch (NullPointerException e) {
//			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @param node
	 * @return null if nothing is done, or a tree with the nodes of the deleted subtree
	 */
	public SnapshotTree remove(Identifiable node){
		//if anything goes wrong do not delete and just return
		try {
			//do not delete the top node. Let the caller delete this object instead
			if(!node.matches(snap.get().getTopNode())){
				//get the subtree of the nodes to delete
				SnapshotTree t;
				t = new SnapshotTree(snap.get(), node);
				//delete the subtree
				snap.set(new SnapshotTree(snap.get(), node, true,true));
				mdf.set(true);
				return t;
			}
		} catch (NullPointerException e) {
//			e.printStackTrace();
		}
		return null;
	}
	
	private boolean processActiveSnapshots() throws NoSuchAlgorithmException, NullPointerException, IOException{
//		System.out.println("snap size="+activeSnapshots.size());
		if(processingSnapshots.size()<=0){
			//nothing to do. go to sleep
			return false;
		}
		Identifiable sn = processingSnapshots.remove(0);
		boolean processing=false;
		TransverseDir td=null;
		Entry<TransverseDir, DifferentialHistory<RunnableStepByStepStatistics>.HistoryMarker> e = snapshots.get(sn);
		td=e.getKey();
		DifferentialHistory<RunnableStepByStepStatistics>.HistoryMarker mark = e.getValue();
		if(td.running()){
			td.process();
//			super.processedSize(super.processedSize()+td.getProcessedSizeIncrement());
//			super.totalSize(super.totalSize()+td.getTotalSizeIncrement());
//			RunnableStepByStepStatistics sts = td.status();
//			RunnableStepByStepStatistics stsd = sts.differences(snapshotsStatistics.get(sn));
////			System.out.println(sn+"="+stsd);
//			RunnableStepByStepStatistics stsp = super.status();
//			super.status(stsp.add(stsd));
////			boolean stsc=super.statusChanged();
////			System.out.println(stsp+" > "+super.status()+" ("+(
//////					(stsd.getProcessedItems()==0 && stsd.getTotalItems()==0 
//////					&& stsd.getProcessedSize()==0 && stsd.getTotalSize()==0
//////					&& (stsd.getAction()==null || stsd.getAction()=="")
//////					)?
//////							false:true
////					stsd.getProcessedItems()+";"+stsd.getTotalItems()+";"+ 
////					stsd.getProcessedSize()+";"+stsd.getTotalSize()+";"+stsd.getAction()
////					)+") = "+stsc);
//			snapshotsStatistics.put(sn, sts);
//			if(td.statusChanged()) super.statusAdd(td.statusDifferences());
			if(mark.changed()) {
				RunnableStepByStepStatistics m = mark.getAndUpdate();
				super.statusAdd(mark.reference().differences(m));
			}
			processing=true;
			processingSnapshots.add(sn);//add to end of list to continue processing in the future
		}
		else{
//			if(mark.changed()) super.statusAdd(mark.getAndUpdate());
			if(mark.changed()) {
				RunnableStepByStepStatistics m = mark.getAndUpdate();
				super.statusAdd(mark.reference().differences(m));
			}
			if(td.valid()){
				//before adding the complete snapshot remove the old incomplete one
//				snap.set(new SnapshotTree(snap.get(), td.getSnapshot().getTopNode(), true,true));
//				snap.set(new SnapshotTree(snap.get(), snap.get().getTopNode(), td.getSnapshot(),1,0));
//				//update snapshotsGroupDescriptor size
//				SnapshotNode a = snap.get().getTopNode().getData();
//				snap.set(
//						new SnapshotTree(
//								snap.get(), 
//								snap.get().getTopNode(), 
//								new SnapshotNode(
//										new SnapshotsGroupDescriptor(
//												a.getSnapshotsGroupDescriptor(), 
//												true, 
//												null, 
//												null, 
//												a.getDescriptorSize()+td.getSnapshot().getTopNode().getData().getDescriptorSize()
//												)
//										)
//								)
//						);
				SnapshotTree tmp=new SnapshotTree(snap.get(), td.getSnapshot().getTopNode(), true,true);
				tmp=new SnapshotTree(tmp, tmp.getTopNode(), td.getSnapshot(),1,0);
				SnapshotNode a = tmp.getTopNode().getData();
				tmp=
						new SnapshotTree(
								tmp, 
								tmp.getTopNode(), 
								new SnapshotNode(
										new SnapshotsGroupDescriptor(
												a.getSnapshotsGroupDescriptor(), 
												true, 
												null, 
												null, 
												a.getDescriptorSize()+td.getSnapshot().getTopNode().getData().getDescriptorSize()
												)
										)
								)
						;
				snap.set(tmp);
				snapshots.remove(sn);
				markers.remove(sn);
//				snapshotsStatistics.remove(sn);
			}
			else {
				System.out.println("invalid "+td.toString());
			}
		}
//		try {
//			Thread.sleep(100);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		return processing;
	}
	
//	/**
//	 * Makes triangular comparison of files to find duplicates (first compares 
//	 * to second and beyond, second compares to third and beyond, etc).
//	 * 
//	 * @return false if there is nothing to process
//	 */
//	private boolean processDuplicates(){
//		if(processingDuplicates.get().size()<=0){
//			//nothing to do. go to sleep
//			return false;
//		}
//		if(processingDuplicatesBufferIndex<=0) {
//			currentDuplicate = processingDuplicates.get().remove(0);
//		}
//		int endIndex=processingDuplicatesBufferIndex+processingDuplicatesBufferSize;
//		if(endIndex>processingDuplicates.get().size()) endIndex=processingDuplicates.get().size();
////		processingDuplicatesBuffer=processingDuplicates.subList(processingDuplicatesBufferIndex, endIndex);
//		boolean processing=true;
//		int dupInd=processingDuplicatesBufferIndex;
//		while(dupInd<endIndex) {//process buffer
//			Identifiable isn1 = currentDuplicate;
//			Identifiable isn2=processingDuplicates.get().get(dupInd);
//			SnapshotNode sn1=snap.get().getNode(isn1).getData();
//			SnapshotNode sn2=snap.get().getNode(isn2).getData();
//			Hashes h1=sn1.getFileDescriptor().getHashes();
//			Hashes h2=sn2.getFileDescriptor().getHashes();
//			if(h1.hasHashes() && h2.hasHashes()){
//				if(h1.equals(h2)){
//					//they are duplicates
//					//replace sn1
//					SnapshotNode snn=new SnapshotNode(sn1, sn2,true);
//					snap.set(new SnapshotTree(snap.get(), sn1, snn));
//					//replace sn2
//					snn=new SnapshotNode(sn2,snn,true);
//					snap.set(new SnapshotTree(snap.get(), sn2, snn));
//				}
//			}
//			++dupInd;
//		}
//		super.processedSize(super.processedSize()+processingDuplicatesIncrement*(endIndex-processingDuplicatesBufferIndex));
//		if(endIndex>=processingDuplicates.get().size()) processingDuplicatesBufferIndex=0;
//		else processingDuplicatesBufferIndex=endIndex;
//		return processing;
//	}
	
	protected void step() throws Exception{
		if(state==STATES.IDLE){
			if(processingSnapshots.size()>0){
				state=STATES.INIT_SNAPSHOTS;
//				super.action("Processing snapshots...");
				super.statusModify(-1,FIELD.NONE,false,"Processing snapshots...");
			}
			else if(processDuplicates.get()){
				processDuplicates.set(false);
//				System.out.println("Processing duplicates...");
				state=STATES.INIT_DUPLICATES;
//				super.action("Processing duplicates...");
//				super.status(super.status().modify(-1,FIELD.NONE,false,"Processing duplicates..."));
			}
			else if(saveToFile.get()){
				snapshotsSave=new OutputXML(snap.get(), savePath,saveAppend.get());
				mrk=snapshotsSave.marker();
				state=STATES.SAVE;
			}
			else{
				super.pause();	//pause thread since it is not being used
			}
			return;
		}
		else if(state==STATES.INIT_SNAPSHOTS){
//			Iterator<Entry<Identifiable, TransverseDir>> it=snapshots.entrySet().iterator();
//			TransverseDir td=null;
//			while(it.hasNext()){
//				td=it.next().getValue();
//				//if the tree does not already contain the current snapshot then add it 
//				if(!snap.get().getChildrenOf(snap.get().getTopNode()).contains(td.getSnapshot().getTopNode())){
//					snap.set(new SnapshotTree(snap.get(), snap.get().getTopNode(), td.getSnapshot().getTopNode().getData(),1,0));
//				}
//			}
			state=STATES.PROCESS_SNAPSHOTS;
		}
		else if(state==STATES.INIT_DUPLICATES){
			duplicates=new ProcessDuplicates(snap.get());
			mrk=duplicates.marker();
//			processingDuplicatesBufferIndex=0;
//			super.processedSize(0);
//			super.totalSize(snap.get().getTopNode().getData().getDescriptorSize());
//			processingDuplicatesIncrement=Math.round(Math.pow(processingDuplicates.get().size(), 2.0)/2.0);
			state=STATES.PROCESS_DUPLICATES;
		}
		else if(state==STATES.PROCESS_SNAPSHOTS){
//			System.out.println("snapshot maker PROCESS");
			if(!processActiveSnapshots()){
				if(processingSnapshots.size()<=0) {
//					super.processedSize(super.totalSize());
//					super.action("Done.");
//					super.statusModify(mrk.getAndUpdate().getTotalSize(), FIELD.PROCESSEDSIZE, true, "Done.");
					super.statusModify(-1, FIELD.NONE, true, "Done.");
				}
				mdf.set(true);
				state=STATES.IDLE;	//nothing to do go to sleep
			}
//			else {
//				//update status
//				super.status(super.status().modify(super.status().getTotalSize(), FIELD.PROCESSEDSIZE, true, "Done."));
//			}
		}
		else if(state==STATES.PROCESS_DUPLICATES){
//			System.out.println("snapshot maker PROCESS");
//			if(!processDuplicates()){
			duplicates.process();
				if(duplicates.running()) {
//					super.processedSize(super.totalSize());
//					super.action("Done.");
//					super.status(duplicates.statistics());
				}
				else {
					//before adding the complete snapshot remove the old incomplete one
//					snap.set(new SnapshotTree(snap.get(), snap.get().getTopNode(), duplicates.snapshot(),1,0));
					snap.set(duplicates.snapshot());
					mdf.set(true);
					state=STATES.IDLE;	//nothing to do go to sleep
				}
//				super.status(duplicates.getStatus());
				super.status(mrk.getAndUpdate());
//			}
		}
		else if(state==STATES.LOAD){
			if(snapshotsLoad.running()) {
				snapshotsLoad.process();
//				System.out.println("snapshot maker LOAD="+snapshotsLoad.status().getStatus());
//				super.status(snapshotsLoad.marker());
				super.status(mrk.getAndUpdate());
			}
			else {
//				super.status(snapshotsLoad.marker());
				super.status(mrk.getAndUpdate());
				snap=new AtomicReference<>(snapshotsLoad.getSnapshot());
				state=STATES.IDLE;//nothing to do go to sleep
			}
		}
		else if(state==STATES.SAVE){
			if(snapshotsSave.running()) {
				snapshotsSave.process();
//				super.status(snapshotsSave.marker());
				super.status(mrk.getAndUpdate());
			}
			else {
//				super.status(snapshotsSave.marker());
				super.status(mrk.getAndUpdate());
				saveToFile.set(false);
				mdf.set(false);
				state=STATES.IDLE;//nothing to do go to sleep
			}
		}
		else{// if(state==STATES.FINALIZE){	//finalize
			super.abort();
		}
	}
	
	public Set<Identifiable> getActiveSnapshots(){
		return DeepCopy.deepCopy(snapshots.keySet());
	}
	
	public Set<TreeNode<SnapshotNode, Identifiable>> getSnapshots(){
		return DeepCopy.deepCopy(snap.get().getChildrenOf(snap.get().getTopNode()));
	}
	
//	public boolean statusChanged(){
//		boolean b=super.statusChanged();
//		System.out.println("maker status changed="+b);
//		return b;
//	}

	/**
	 * use this method to check for changes in the evolution of the processing.
	 * if this returns false then the messages are still the same as the ones read the previous time
	 * 
	 * @return
	 */
	public boolean statusChanged(final Identifiable activeSnapshotID){
//		if(snapshots.containsKey(activeSnapshotID)){
//			return snapshots.get(activeSnapshotID).statusChanged();
//		}
//		Entry<TransverseDir, DifferentialHistory<RunnableStepByStepStatistics>.HistoryMarker> e = snapshots.get(activeSnapshotID);
////		TransverseDir td = e.getKey();
////		if(td!=null) return td.statusChanged();
//		if(e!=null) {
//			DifferentialHistory<RunnableStepByStepStatistics>.HistoryMarker m = e.getValue();
//			return m.changed();
//		}
////		System.out.println("status: "+activeSnapshotID+" not found");
//		return false;
		return markers.changed(activeSnapshotID);
	}
	
//	public boolean totalSizeChanged(final Identifiable activeSnapshotID){
//		if(snapshots.containsKey(activeSnapshotID)){
//			return snapshots.get(activeSnapshotID).totalSizeChanged();
//		}
//		return false;
//	}
	
//	public long getTotalSize(final Identifiable activeSnapshotID){
//		if(snapshots.containsKey(activeSnapshotID)){
//			return snapshots.get(activeSnapshotID).getTotalSize();
//		}
//		return -1;
//	}
//	
//	public boolean processedSizeChanged(final Identifiable activeSnapshotID){
//		if(snapshots.get().containsKey(activeSnapshotID)){
//			return snapshots.get().get(activeSnapshotID).processedSizeChanged();
//		}
//		return false;
//	}
//	
//	public long getProcessedSize(final Identifiable activeSnapshotID){
//		if(snapshots.get().containsKey(activeSnapshotID)){
//			return snapshots.get().get(activeSnapshotID).getProcessedSize();
//		}
//		return -1;
//	}
//	
//	public long getProcessedSizeIncrement(final Identifiable activeSnapshotID){
//		if(snapshots.get().containsKey(activeSnapshotID)){
//			return snapshots.get().get(activeSnapshotID).getProcessedSizeIncrement();
//		}
//		return -1;
//	}
//	
//	public double getProcessedPercentage(final Identifiable activeSnapshotID){
//		if(snapshots.get().containsKey(activeSnapshotID)){
//			return snapshots.get().get(activeSnapshotID).getProcessedPercentage();
//		}
//		return -1.0;
//	}
//	
//	public boolean currentActionChanged(final Identifiable activeSnapshotID){
//		if(snapshots.get().containsKey(activeSnapshotID)){
//			return snapshots.get().get(activeSnapshotID).currentActionChanged();
//		}
//		return false;
//	}
//	
//	public String getCurrentAction(final Identifiable activeSnapshotID){
//		if(snapshots.get().containsKey(activeSnapshotID)){
//			return snapshots.get().get(activeSnapshotID).getCurrentAction();
//		}
//		return "No such active snapshot";
//	}
	
	public String getStatus(final Identifiable activeSnapshotID){
//		if(snapshots.containsKey(activeSnapshotID)){
//			return snapshots.get(activeSnapshotID).getStatus();
//		}
//		TransverseDir td = snapshots.get(activeSnapshotID);
//		if(td!=null) return td.getStatus().getStatus();
//		Entry<TransverseDir, DifferentialHistory<RunnableStepByStepStatistics>.HistoryMarker> e = snapshots.get(activeSnapshotID);
//		if(e!=null) {
//			DifferentialHistory<RunnableStepByStepStatistics>.HistoryMarker m = e.getValue();
//			return m.getAndUpdate().getStatus();
//		}
		RunnableStepByStepStatistics a = markers.getAndUpdate(activeSnapshotID);
		if(a!=null) return a.getStatus();
		return "No such active snapshot";
	}
	
	public DifferentialHistory<RunnableStepByStepStatistics>.HistoryMarker getNewMarker(final Identifiable activeSnapshotID){
		return markers.copy(activeSnapshotID);
	}
	
	public void kill(final Identifiable activeSnapshotID){
//		if(snapshots.containsKey(activeSnapshotID)){
//			snapshots.get(activeSnapshotID).kill();
//		}
//		TransverseDir td = snapshots.get(activeSnapshotID);
//		if(td!=null) td.kill();
		Entry<TransverseDir, DifferentialHistory<RunnableStepByStepStatistics>.HistoryMarker> e = snapshots.get(activeSnapshotID);
		if(e!=null) {
			TransverseDir t = e.getKey();
			t.kill();
		}
	}
	
	/**
	 * @param activeSnapshotID
	 * @return true if operation succeeded, false otherwise
	 */
	public boolean abort(final Identifiable activeSnapshotID){
//		if(snapshots.containsKey(activeSnapshotID)){
//			snapshots.get(activeSnapshotID).abort();
//			return true;
//		}
//		TransverseDir td = snapshots.get(activeSnapshotID);
//		if(td!=null) {
//			td.abort();
//			return true;
//		}
		Entry<TransverseDir, DifferentialHistory<RunnableStepByStepStatistics>.HistoryMarker> e = snapshots.get(activeSnapshotID);
		if(e!=null) {
			TransverseDir t = e.getKey();
			t.abort();
			return true;
		}
		return false;
	}
	
	public void pause(final Identifiable activeSnapshotID){
//		if(snapshots.containsKey(activeSnapshotID)){
//			snapshots.get(activeSnapshotID).pause();;
//		}
//		TransverseDir td = snapshots.get(activeSnapshotID);
//		if(td!=null) td.pause();
		Entry<TransverseDir, DifferentialHistory<RunnableStepByStepStatistics>.HistoryMarker> e = snapshots.get(activeSnapshotID);
		if(e!=null) {
			TransverseDir t = e.getKey();
			t.pause();
		}
	}
	
	public void go(final Identifiable activeSnapshotID){
//		if(snapshots.containsKey(activeSnapshotID)){
//			snapshots.get(activeSnapshotID).go();
//		}
//		TransverseDir td = snapshots.get(activeSnapshotID);
//		if(td!=null) td.go();
		Entry<TransverseDir, DifferentialHistory<RunnableStepByStepStatistics>.HistoryMarker> e = snapshots.get(activeSnapshotID);
		if(e!=null) {
			TransverseDir t = e.getKey();
			t.go();
		}
	}

	/**
	 * Public method to end the thread at the end of the current processing step.
	 * It will NOT perform cleanup
	 */
	public void kill(){
		Iterator<Entry<Identifiable, Entry<TransverseDir, DifferentialHistory<RunnableStepByStepStatistics>.HistoryMarker>>> it = snapshots.entrySet().iterator();
		while(it.hasNext()){
			TransverseDir a = it.next().getValue().getKey();
			if(a!=null) a.kill();
		}
		super.kill();
	}
	
	/**
	 * Public method to abort the thread at the end of the current processing step.
	 * It will perform any cleanup
	 */
	public void abort(){
		Iterator<Entry<Identifiable, Entry<TransverseDir, DifferentialHistory<RunnableStepByStepStatistics>.HistoryMarker>>> it = snapshots.entrySet().iterator();
		while(it.hasNext()){
			TransverseDir a = it.next().getValue().getKey();
			if(a!=null) a.abort();
		}
		super.abort();
	}
	
	/**
	 * Pause the thread momentarily. It will sleep for sleepTime at a time before
	 * checking for new commands
	 */
	public void pause(){
		Iterator<Entry<Identifiable, Entry<TransverseDir, DifferentialHistory<RunnableStepByStepStatistics>.HistoryMarker>>> it = snapshots.entrySet().iterator();
		while(it.hasNext()){
			TransverseDir a = it.next().getValue().getKey();
			if(a!=null) a.pause();
		}
		super.pause();
	}
	
	/**
	 * Resume the thread after a pause
	 */
	public void go(){
		Iterator<Entry<Identifiable, Entry<TransverseDir, DifferentialHistory<RunnableStepByStepStatistics>.HistoryMarker>>> it = snapshots.entrySet().iterator();
		while(it.hasNext()){
			TransverseDir a = it.next().getValue().getKey();
			if(a!=null) a.go();
		}
		super.go();
	}
	
	@Override
	protected void initialize() throws Exception {
		//does nothing
	}

	@Override
	protected void endStep() throws Exception {
		super.validate();
	}
	
//	protected void signal(Object obj) throws Exception{
//		//do nothing
//	}

	@Override
	public UUID identifier() {
		return snap.get().getTopNode().identifier();
	}

	@Override
	public String objectType() {
		return snap.get().getTopNode().objectType();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Identifiable identifiable() {
		return snap.get().getTopNode().identifiable();
	}

	public boolean setProcessDuplicates(){
		processDuplicates.set(true);
		super.go();
		return true;
	}

//	public boolean resetProcessDuplicates(){
//		processDuplicates.set(false);
//		if(processingDuplicates.get().size()>0) return false;//already processing
//		return true;
//	}

	@Override
	public <T extends jrain.identifiable.Identifiable<UUID>> boolean matches(T ID) {
//		System.out.println("id="+ID);
//		System.out.println("snap="+snap.get());
//		System.out.println("snap.tn="+snap.get().getTopNode());
//		System.out.println("snap.tn.id="+snap.get().identifiable());
		return snap.get().getTopNode().identifiable().matches(ID);
	}

//	@Override
//	public TaggedTable asTaggedTable() {
//		return snap.get().asTaggedTable();
//	}
	
	public boolean modified(){return mdf.get();}
}
