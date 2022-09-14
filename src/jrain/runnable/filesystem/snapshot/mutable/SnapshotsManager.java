package jrain.runnable.filesystem.snapshot.mutable;

import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import jrain.filesystem.snapshot.immutable.SnapshotsGroupDescriptor;
import jrain.filesystem.snapshot.snapshotTree.immutable.SnapshotNode;
import jrain.filesystem.snapshot.snapshotTree.immutable.SnapshotTree;
import jrain.hash.hashInstance.hashes.Hashes;
import jrain.runnable.filesystem.snapshot.mutable.SnapshotsAction.ACTIONTYPE;
import jrain.exceptions.ExceptionInvalidValue;
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
 * It contains methods to handle snapshots groups by allowing to add/remove
 * snapshots/directories/files, read/save snapshots groups from/to xml file.
 */
public class SnapshotsManager extends RunnableStepByStep{

	private class SnapMaker extends SnapshotsMaker{

		public SnapMaker(SnapshotsGroupDescriptor snapshotsGroupDescriptor)
				throws Exception {
			super(snapshotsGroupDescriptor);
		}
		
		public SnapMaker(String pathToXML) throws Exception{
			super(pathToXML);
		}
		
		protected void process(){super.process();}
		
//		protected boolean checkStatusChanged(){return super.checkStatusChanged();}
//
//		protected RunnableStepByStepStatistics marker() {return super.marker();}
//		
////		protected void status(RunnableStepByStepStatistics sts) {super.status(sts);}
//		
//		protected RunnableStepByStepStatistics statusDifferences(){return super.statusDifferences();}
	}
	
	private static enum STATES {IDLE,INIT,PROCESS,SCRIPT,FINALIZE};
		
	private ConcurrentHashMap<Identifiable,Entry<SnapMaker,DifferentialHistory<RunnableStepByStepStatistics>.HistoryMarker>> snapshots;
	private HistoryMarkerSet<Identifiable, RunnableStepByStepStatistics> markers;
//	private ConcurrentHashMap<Identifiable,RunnableStepByStepStatistics> snapshotsStatistics;

	private SnapshotsActionsList sal;
	private AtomicBoolean saveToFile;
	private AtomicBoolean saveAppend;
	private String savePath;
	private boolean firstSave;
	private Iterator<SnapshotsAction> salNext;
	private FileWriter scrpt;


	/**
	 * current state of the state machine
	 */
	private STATES state=STATES.IDLE;

	private final Set<String> defaultHashes;
	
	private final int bufferSize;
	
	/**
	 * Initializes a snapshot manager with global settings
	 * 
	 * @param compareFilesByteByByte
	 * @param BufferSize
	 * @throws Exception
	 */
	public SnapshotsManager(
			Set<String> hashes,
			int BufferSize
			) throws Exception {
		super();
		snapshots=new ConcurrentHashMap<>();
		markers=new HistoryMarkerSet<>();
//		snapshotsStatistics=new ConcurrentHashMap<>();
		sal=null;
		defaultHashes=Hashes.validateHashes(hashes);
		bufferSize=BufferSize;
		super.go();
	}

	@Override
	protected void initialize() throws Exception {
		//does nothing
	}

	private boolean processActiveGroups() throws NoSuchAlgorithmException, NullPointerException, IOException{
		Iterator<Entry<Identifiable, Entry<SnapMaker, DifferentialHistory<RunnableStepByStepStatistics>.HistoryMarker>>> i=snapshots.entrySet().iterator();
		boolean processing=false;
		SnapMaker td=null;
		RunnableStepByStepStatistics stsp = super.status();
		while(i.hasNext()){
			Entry<SnapMaker, DifferentialHistory<RunnableStepByStepStatistics>.HistoryMarker> e = i.next().getValue();
			td=e.getKey();
			DifferentialHistory<RunnableStepByStepStatistics>.HistoryMarker mark = e.getValue();
			if(td.running()){
				td.process();
//				super.processedSize(super.processedSize()+td.getProcessedSizeIncrement());
//				super.totalSize(super.totalSize()+td.getTotalSizeIncrement());
//				stsp=stsp.add(td.statusDifferences());
				if(mark.changed()) {
//					RunnableStepByStepStatistics sts = td.status();
//	//				System.out.println(sts+"\t;\t"+snapshotsStatistics.get(td.identifiable()));
//					RunnableStepByStepStatistics stsd = sts.differences(snapshotsStatistics.get(td.identifiable()));
//	//				RunnableStepByStepStatistics stsp = super.status();
//					stsp=stsp.add(stsd);
//					stsp=stsp.add(td.statusDifferences());
					RunnableStepByStepStatistics m = mark.getAndUpdate();
					stsp=stsp.add(mark.reference().differences(m));
//					snapshotsStatistics.put(td.identifiable(), sts);
				}
				if(td.active()) processing=true;
			}
			else{//not running anymore
				snapshots.remove(td.identifiable());
				markers.remove(td.identifiable());
//				snapshotsStatistics.remove(td.identifiable());
				RunnableStepByStepStatistics m = mark.getAndUpdate();
				stsp=stsp.add(mark.reference().differences(m));
			}
		}
		super.status(stsp);
		return processing;
	}
	
	@Override
	protected void step() throws Exception {
		if(state==STATES.IDLE){
			Iterator<Entry<Identifiable, Entry<SnapMaker, DifferentialHistory<RunnableStepByStepStatistics>.HistoryMarker>>> it = snapshots.entrySet().iterator();
			while(it.hasNext()) {
				Entry<SnapMaker, DifferentialHistory<RunnableStepByStepStatistics>.HistoryMarker> e = it.next().getValue();
				SnapMaker a = e.getKey();
				if(a.active()){
//					System.out.println("snapshot manager state status="+a.getStatus());
					state=STATES.INIT;
//					super.action("Processing...");
					super.statusModify(-1, FIELD.NONE, false, "Processing...");
					return;
				}
			}
//			if(saveToFile.get()){
//				saveToFile.set(false);
//				scrpt=new FileWriter(savePath,saveAppend.get());
//				firstSave=true;
//				salNext=sal.actions().iterator();
//				state=STATES.SCRIPT;
//			}
//			else {
				super.pause();	//pause thread since it is not being used
//			}
			return;
		}
		else if(state==STATES.INIT){
			state=STATES.PROCESS;
		}
		else if(state==STATES.PROCESS){
//			System.out.println("snapshot manager PROCESS");
			if(!processActiveGroups()){
				state=STATES.FINALIZE;	//nothing to do go to sleep
			}
		}
		else if(state==STATES.SCRIPT){
//			if(salNext.hasNext()) {
//				SnapshotsAction a = salNext.next();
//				String commandSeparator=";";
//				String copyHead="cp -r ";
//				String copySeparator=" ";
//				String copyTail="";
//				String deleteHead="rm ";
//				String deleteTail="";
//				if(firstSave) {
//					firstSave=false;
//					switch (a.ActionType()) {
//					case COPY:
//						scrpt.append(copyHead);
//						TreeNode<SnapshotNode, Identifiable> nd = getNode(a.source());
//						if(nd.getData().getDescriptorType().equals(jrain.filesystem.snapshot.immutable.snapshotTree.interfaces.SnapshotNode.DescriptorType.FILE)) {
//							scrpt.append(nd.getData().getFileDescriptor().getFullPath(copySeparator));
//						}
//						else if(nd.getData().getDescriptorType().equals(jrain.filesystem.snapshot.immutable.snapshotTree.interfaces.SnapshotNode.DescriptorType.DIRECTORY)) {
//							
//						}
//						else if(nd.getData().getDescriptorType().equals(jrain.filesystem.snapshot.immutable.snapshotTree.interfaces.SnapshotNode.DescriptorType.SNAPSHOT)) {
//							
//						}
//						else if(nd.getData().getDescriptorType().equals(jrain.filesystem.snapshot.immutable.snapshotTree.interfaces.SnapshotNode.DescriptorType.SNAPSHOTSGROUP)) {
//							
//						}
//						scrpt.append(copySeparator);
//						scrpt.append(a.destination());
//						scrpt.append(copyTail);
//						break;
//					case DELETE:
//						break;
//					case RECYCLE://(requires trash-cli or similar in linux)
//						break;
//					default:
//						throw new ExceptionInvalidValue("Unknown action type");
//						break;
//					}
//				}
//				snapshotsSave.process();
//				super.status(snapshotsSave.status());
//			}
//			else {
//				super.status(snapshotsSave.status());
//				saveToFile.set(false);
//				state=STATES.IDLE;//nothing to do go to sleep
//			}
		}
		else if(state==STATES.FINALIZE){	//finalise
//			System.out.println("snapshot manager FINALIZE");
//			super.processedSize(super.totalSize());
//			super.action("Done.");
			super.statusModify(-1, FIELD.NONE, false, "Done.");
			state=STATES.IDLE;
		}
	}

	@Override
	protected void endStep() throws Exception {
		//does nothing
	}
	
	protected void signal(Object obj) throws Exception{
		//do nothing
	}

	public ArrayList<SnapshotNode> getGroups(){
		ArrayList<SnapshotNode> a = new ArrayList<>();
		Iterator<Entry<Identifiable, Entry<SnapMaker, DifferentialHistory<RunnableStepByStepStatistics>.HistoryMarker>>> it = snapshots.entrySet().iterator();
		Entry<SnapMaker, DifferentialHistory<RunnableStepByStepStatistics>.HistoryMarker> i = null;
		while(it.hasNext()){
			i = it.next().getValue();
//			System.out.println("sM="+i);
//			System.out.println("tree="+i.getSnapshotTree());
//			System.out.println("tree.tn="+i.getSnapshotTree().getTopNode());
//			System.out.println("tree.tn.da="+i.getSnapshotTree().getTopNode().getData());
			SnapshotTree dbgt = i.getKey().getSnapshotTree();
//			TreeNode<SnapshotNode, Identifiable> dbgtn2 = dbgt.getTopNode();
//			TreeNode<SnapshotNode, Identifiable> dbgtn = i.getSnapshotTree().getTopNode();
//			SnapshotNode dbgtnd2 = dbgtn2.getData();
//			SnapshotNode dbgtnd = i.getSnapshotTree().getTopNode().getData();
//			a.add(i.getSnapshotTree().getTopNode().getData());
			a.add(dbgt.getTopNode().getData());
		}
		return a;
	}
	
	/**
	 * Searches for the group (and only the group) with the given ID on the list of snapshots
	 * @param grp
	 * @return
	 */
	public <T extends Identifiable> SnapshotsMaker getGroup(T grp){
		Iterator<Entry<Identifiable, Entry<SnapMaker, DifferentialHistory<RunnableStepByStepStatistics>.HistoryMarker>>> it=snapshots.entrySet().iterator();
		SnapMaker sm=null;
		while(it.hasNext()){
			sm=it.next().getValue().getKey();
			if(sm.matches(grp)) return sm;
		}
		return null;
	}
	
	private <T extends Identifiable> SnapshotsMaker getGroupThatContains(T id) throws NullPointerException{
		Iterator<Entry<Identifiable, Entry<SnapMaker, DifferentialHistory<RunnableStepByStepStatistics>.HistoryMarker>>> it = snapshots.entrySet().iterator();
		SnapMaker s;
		while(it.hasNext()){
			s = it.next().getValue().getKey();
			try {
				if(s.getSnapshotTree().contains(id))return s;//will be bypassed when node does not exist since an ExceptionProcessing is thrown
			}catch(Exception e) {}//ignore exception. Only means that we are looking in the wrong snapshot maker
		}
		throw new NullPointerException("ID="+id+" not found.");//not found in any snapshot
	}
	
	/**
	 * Get the snapshot tree the node with the given ID belongs to.
	 * 
	 * @param id
	 * @return
	 * @throws ExceptionProcessing
	 * @throws NullPointerException
	 */
	public <T extends Identifiable> SnapshotTree getTree(T id) throws NullPointerException{
		Iterator<Entry<Identifiable, Entry<SnapMaker, DifferentialHistory<RunnableStepByStepStatistics>.HistoryMarker>>> it = snapshots.entrySet().iterator();
		SnapMaker s;
		while(it.hasNext()){
			s = it.next().getValue().getKey();
			try {
				if(s.getSnapshotTree().contains(id))return s.getSnapshotTree();//will be bypassed when node does not exist since an ExceptionProcessing is thrown
			}catch(Exception e) {}//ignore exception. Only means that we are looking in the wrong snapshot maker
		}
		throw new NullPointerException("ID="+id+" not found.");//not found in any snapshot
	}
	
	public <T extends Identifiable> TreeNode<SnapshotNode, Identifiable> getNode(T id) throws NullPointerException{
		return getTree(id).getNode(id);
	}
	
	/**
	 * Create a new snapshot group
	 * @param Name
	 * @return the identifiable of the newly created snapshot group
	 * @throws Exception
	 */
	public Identifiable newGroup(final String Name) throws Exception{
		SnapshotsGroupDescriptor sgd=new SnapshotsGroupDescriptor(null, Name, "",0);
		SnapMaker sm = new SnapMaker(sgd);
		snapshots.put(sm.identifiable(),new Pair<>(sm,sm.marker()));
		markers.put(sm.identifiable(), sm.marker());
//		snapshotsStatistics.put(sm.identifiable(), new RunnableStepByStepStatistics());
		return sgd;
	}
	
	/**
	 * Load a snapshot group from file
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	public Identifiable loadGroup(String filename) throws Exception{
		SnapMaker sm=null;
		sm= new SnapMaker(filename);
		snapshots.put(sm.identifiable(),new Pair<>(sm,sm.marker()));
		markers.put(sm.identifiable(), sm.marker());
//		snapshotsStatistics.put(sm.identifiable(), new RunnableStepByStepStatistics());
		go();
		return sm.identifiable();
	}
	
	/**
	 * @param id
	 * @return true if the given group was modified, false otherwise
	 */
	public boolean modifiedGroup(Identifiable id){
		SnapshotsMaker a = getGroup(id);
		if(a==null) return false;
		return a.modified();
	}
	
	/**
	 * Save a snapshot group to file
	 * @param id
	 * @param fileName
	 * @return
	 * @throws ExceptionProcessing 
	 * @throws IOException 
	 * @throws NullPointerException 
	 * @throws Exception
	 */
	public Boolean saveGroup(Identifiable id, final String fileName, boolean append) throws NullPointerException, IOException, ExceptionProcessing{
		return getGroup(id).save(fileName,append);
	}
	
	public <T extends Identifiable> Identifiable newSnapshot(T snapshotsGroupID,final String snapshotName,final String pathToSnapshot, Set<String> hashes,int recursion) throws Exception{
		SnapshotsMaker sm=getGroup(snapshotsGroupID);
		if(sm!=null){
			Identifiable b = sm.addSnapshot(snapshotName, pathToSnapshot, null, (hashes==null)?defaultHashes:hashes, recursion, bufferSize);
			go();
			return b;
		}
		return null;
	}
	
	/**
	 * Delete a node and its children.
	 * If it is the top node delete the snapshotsMaker instead.
	 * 
	 * @param path
	 * @return
	 * @throws ExceptionProcessing 
	 * @throws ExceptionInvalidValue 
	 */
	public <T extends Identifiable> boolean delete(T id){
		//check if the ID belongs to a snapshot maker
		SnapshotsMaker sm;
		try {
			sm = getGroupThatContains(id);
		} catch (NullPointerException e1) {
			// id does not exist, so ignore
			return false;
		}
		if(sm.matches(id)){//it is a snapshots maker
//			sal=new SnapshotsActionsList(sal, new SnapshotsAction(ACTIONTYPE.DELETE, id, sm.getSnapshotTree(), null, null), true);
			//delete the entire tree by deleting individual file/folder inside each snapshot inside the group)
//			vdrfv
//			snapshots.remove(sm.identifiable());
//			snapshotsStatistics.remove(sm.identifiable());
			sm.finish();
			return true;
		}
		else{//not a snapshot maker
			SnapshotTree st = sm.delete(id);
			if(st!=null){
				sal=new SnapshotsActionsList(sal, new SnapshotsAction(ACTIONTYPE.DELETE, id, st, null, null), true);
				return true;
			}
			else{//did not remove anything
				return false;
			}
		}
	}
	
	/**
	 * Delete a node and its children.
	 * If it is the top node delete the snapshotsMaker instead.
	 * 
	 * @param path
	 * @return
	 * @throws ExceptionProcessing 
	 * @throws ExceptionInvalidValue 
	 */
	public <T extends Identifiable> boolean remove(T id){
		//check if the ID belongs to a snapshot maker
		SnapshotsMaker sm;
		try {
			sm = getGroupThatContains(id);
		} catch (NullPointerException e1) {
			// id does not exist, so ignore
			return false;
		}
		if(sm.matches(id)){//it is a snapshots maker
//			sal=new SnapshotsActionsList(sal, new SnapshotsAction(ACTIONTYPE.DELETE, id, sm.getSnapshotTree(), null, null), true);
//			snapshots.remove(sm.identifiable());
//			snapshotsStatistics.remove(sm.identifiable());
			sm.finish();
			return true;
		}
		else{//not a snapshot maker
			SnapshotTree st = sm.remove(id);
			if(st!=null){
//				sal=new SnapshotsActionsList(sal, new SnapshotsAction(ACTIONTYPE.DELETE, id, st, null, null), true);
				return true;
			}
			else{//did not remove anything
				return false;
			}
		}
	}
	
	/**
	 * Copy/move node to parent node
	 * 
	 * @param parentNode
	 * @param node
	 * @param copy if true copies, else moves (deletes from original after copy)
	 * @return
	 */
	public <T extends Identifiable> boolean copyMoveNode(T parentNode, T node, boolean copy){
		SnapshotsMaker sm1;
		SnapshotsMaker sm2;
		TreeNode<SnapshotNode, Identifiable> sn1;
		TreeNode<SnapshotNode, Identifiable> sn2;
		sm1 = getGroupThatContains(parentNode);
		sm2=getGroupThatContains(node);
		sn1=getNode(parentNode);
		sn2=getNode(node);
		if(sn1.getData().canHaveChild(sn2.getData())){
			if(sm1.add(parentNode, new SnapshotTree(sm2.getSnapshotTree(), sn2))){
				sal=new SnapshotsActionsList(sal, new SnapshotsAction(ACTIONTYPE.COPY, parentNode, new SnapshotTree(sn1.getData()), node, new SnapshotTree(sn2.getData())), true);
				if(!copy){	//remove
					if(sm2.remove(node)==null) return false;
					sal=new SnapshotsActionsList(sal, new SnapshotsAction(ACTIONTYPE.DELETE, node, new SnapshotTree(sn2.getData()), null, null), true);
				}
				return true;
			}
		}
		return false;
	}
	
	public void generateScript(
			String copyFileBefore, String copyFileAfter,
			String moveFileBefore, String moveFileAfter,
			String deleteFileBefore, String deleteFileAfter,
			String copyDirectoryBefore, String copyDirectoryAfter,
			String moveDirectoryBefore, String moveDirectoryAfter,
			String deleteDirectoryBefore, String deleteDirectoryAfter,
			String fileHeader,
			String filename,
			Boolean append
		) 
	{
		if(filename==null) throw new NullPointerException("File name is null");
		savePath=filename;
		saveToFile.set(true);
		saveAppend.set(append);
		super.go();

	}
	
	/**
	 * Public method to end the thread at the end of the current processing step.
	 * It will NOT perform cleanup
	 */
	public void kill(){
		Iterator<Entry<Identifiable, Entry<SnapMaker, DifferentialHistory<RunnableStepByStepStatistics>.HistoryMarker>>> it = snapshots.entrySet().iterator();
		while(it.hasNext()){
			SnapMaker a = it.next().getValue().getKey();
			a.kill();
		}
		super.kill();
	}
	
	/**
	 * Public method to abort the thread at the end of the current processing step.
	 * It will perform any cleanup
	 */
	public void abort(){
		Iterator<Entry<Identifiable, Entry<SnapMaker, DifferentialHistory<RunnableStepByStepStatistics>.HistoryMarker>>> it = snapshots.entrySet().iterator();
		while(it.hasNext()){
			SnapMaker a = it.next().getValue().getKey();
			a.abort();
		}
		super.abort();
	}
	
	/**
	 * Pause the thread momentarily. It will sleep for sleepTime at a time before
	 * checking for new commands
	 */
	public void pause(){
		Iterator<Entry<Identifiable, Entry<SnapMaker, DifferentialHistory<RunnableStepByStepStatistics>.HistoryMarker>>> it = snapshots.entrySet().iterator();
		while(it.hasNext()){
			SnapMaker a = it.next().getValue().getKey();
			a.pause();
		}
		super.pause();
	}
	
	/**
	 * Resume the thread after a pause
	 */
	public void go(){
		Iterator<Entry<Identifiable, Entry<SnapMaker, DifferentialHistory<RunnableStepByStepStatistics>.HistoryMarker>>> it = snapshots.entrySet().iterator();
		while(it.hasNext()){
			SnapMaker a = it.next().getValue().getKey();
			a.go();
		}
		super.go();
	}
	
//	public Set<Identifiable> getActiveSnapshots(){
//		return DeepCopy.deepCopy(snapshots.keySet());
//	}
	
	/**
	 * use this method to check for changes in the evolution of the processing.
	 * if this returns false then the messages are still the same as the ones read the previous time
	 * 
	 * @return
	 */
	public boolean statusChanged(final Identifiable activeSnapshotID){
//		Entry<SnapMaker, DifferentialHistory<RunnableStepByStepStatistics>.HistoryMarker> e = snapshots.get(activeSnapshotID);
//		if(e!=null) {
//			DifferentialHistory<RunnableStepByStepStatistics>.HistoryMarker m = e.getValue();
//			return m.changed();
//		}
//		return false;
		return markers.changed(activeSnapshotID);
	}
	
	public String getStatus(final Identifiable activeSnapshotID){
//		Entry<SnapMaker, DifferentialHistory<RunnableStepByStepStatistics>.HistoryMarker> e = snapshots.get(activeSnapshotID);
//		if(e!=null) {
//			DifferentialHistory<RunnableStepByStepStatistics>.HistoryMarker m = e.getValue();
//			return m.getAndUpdate().getStatus();
//		}
		RunnableStepByStepStatistics a = markers.getAndUpdate(activeSnapshotID);
		if(a!=null) return a.getStatus();
		return "No such active snapshot";
	}
	
	public void kill(final Identifiable activeSnapshotID){
		Entry<SnapMaker, DifferentialHistory<RunnableStepByStepStatistics>.HistoryMarker> e = snapshots.get(activeSnapshotID);
		if(e!=null) {
			SnapMaker t = e.getKey();
			t.kill();
		}
	}
	
	/**
	 * @param activeSnapshotID
	 * @return true if operation succeeded, false otherwise
	 */
	public boolean abort(final Identifiable activeSnapshotID){
		Entry<SnapMaker, DifferentialHistory<RunnableStepByStepStatistics>.HistoryMarker> e = snapshots.get(activeSnapshotID);
		if(e!=null) {
			SnapMaker t = e.getKey();
			t.abort();
			return true;
		}
		return false;
	}
	
	public void pause(final Identifiable activeSnapshotID){
		Entry<SnapMaker, DifferentialHistory<RunnableStepByStepStatistics>.HistoryMarker> e = snapshots.get(activeSnapshotID);
		if(e!=null) {
			SnapMaker t = e.getKey();
			t.pause();
		}
	}
	
	public void go(final Identifiable activeSnapshotID){
		Entry<SnapMaker, DifferentialHistory<RunnableStepByStepStatistics>.HistoryMarker> e = snapshots.get(activeSnapshotID);
		if(e!=null) {
			SnapMaker t = e.getKey();
			t.go();
		}
	}


}
