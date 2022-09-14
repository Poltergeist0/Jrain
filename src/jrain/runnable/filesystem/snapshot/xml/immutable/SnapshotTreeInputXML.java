package jrain.runnable.filesystem.snapshot.xml.immutable;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.Map.Entry;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import jrain.filesystem.snapshot.immutable.SnapshotsGroupDescriptor;
import jrain.filesystem.snapshot.snapshotTree.SnapshotNode.DescriptorType;
import jrain.filesystem.snapshot.snapshotTree.immutable.SnapshotNode;
import jrain.filesystem.snapshot.snapshotTree.immutable.SnapshotTree;
import jrain.xml.XML;
import jrain.identifiable.immutable.Identifiable;
import jrain.treeIdentifiable.immutable.TreeNode;
import jrain.io.InputStreamProgress;
import jrain.runnable.RunnableStepByStep;
import jrain.runnable.RunnableStepByStepStatistics.FIELD;


/**
 * @author poltergeist0
 *
 * This class builds a snapshot tree from a file in a base path.
 * It may start with either a snapshots group descriptor or a snapshot descriptor
 * as top node (no snapshots group).
 * A snapshot descriptor may have directories and files as children.
 * All descriptors are assigned new {@link Identifiable} so that collisions are
 * avoided when loading multiple files.
 */
public class SnapshotTreeInputXML extends RunnableStepByStep implements jrain.identifiable.Identifiable<UUID>{
	
	/**
	 * Internal states of the state machine in method step.
	 */
	private static enum STATES {IDLE,INIT,OPENSNAPSHOTSGROUP, CLOSESNAPSHOTSGROUP, OPENSNAPSHOT, CLOSESNAPSHOT, OPENDIRECTORY, CLOSEDIRECTORY,FINALIZE};
	
	/**
	 * snapshot used in processing
	 */
	private AtomicReference<SnapshotTree> snap;
	
	/**
	 * current state of the state machine
	 */
	private STATES state=STATES.IDLE;
	
	/**
	 * Input stream
	 */
	private InputStreamProgress p =null;
	
	/**
	 * XML stream
	 */
	private XMLStreamReader reader =null;
	
	/**
	 * Total size of the file.
	 * Used for status only.
	 */
	private final long totalSize;
	
	/**
	 * List of parent nodes waiting to be closed.
	 * Every time a tag is found that may have children, it is added to the end
	 * of this list so that every following child knows who is their parent.
	 * A parent is removed when its corresponding close tag is found.
	 */
	private ArrayList<Identifiable> parents=null;
	
	/**
	 * Maps between the new IDs (value) and the loaded IDs (key).
	 * Necessary because snapshot duplicates can only be processed after the
	 * entire tree is read from file.
	 */
	private HashMap<Identifiable, Identifiable> newIDs=null;
	
    /**
     * Current node being processed.
     * The node already has a new ID. Its old ID is passed as the key of the entry.
     */
    private Entry<Identifiable, SnapshotNode> sn=null;

	/**
	 * Update duplicates in given node
	 * @param node is the node that will have its duplicates updated
	 * @throws NullPointerException if any method parameter is null
	 */
	private void updateDuplicates(SnapshotNode node) throws NullPointerException{
		//get duplicates list of current node
		Iterator<Identifiable> a = node.getDuplicates().iterator();
		//construct new node without duplicates
		SnapshotNode sn=new SnapshotNode(node);
		while(a.hasNext()){
			//find in newIDs the old ID and add the value (new ID) to the new duplicates list
			sn=new SnapshotNode(sn, newIDs.get(a.next()),true);
		}
		//replace the (node data) duplicates list with the new one
		snap.set(new SnapshotTree(snap.get(), node, sn));
	}
	
	/**
	 * Read snapshot tree from file
	 * 
	 * @param basePath is the path to the file
	 * @throws Exception if any problem occurs reading the file
	 */
	public SnapshotTreeInputXML(
			final String basePath
			) throws Exception{
		super();
		totalSize=(new File(basePath)).length();
	    FileInputStream fis = new FileInputStream(basePath);
        XMLInputFactory xmlInFact = XMLInputFactory.newInstance();
        p = new InputStreamProgress(fis);
        reader = xmlInFact.createXMLStreamReader(p);
        parents=new ArrayList<>();
        newIDs=new HashMap<>();
		state=STATES.INIT;
		//create a dummy snapshot tree just to get a fixed identifier. Tree will be replaced later
		snap=new AtomicReference<SnapshotTree>(new SnapshotTree(new SnapshotNode(new SnapshotsGroupDescriptor(null, "noname", "somefile.snapshot", 0))));
		super.go();
	}
	
	/**
	 * Get the snapshot tree.
	 * The snapshot can only be read after completion (running method returns false).
	 * If an attempt is performed before completion null is returned.
	 * 
	 * @return the snapshot tree or null
	 */
	public SnapshotTree getSnapshot() {
		if(super.running()) return null;
		return snap.get();
	}
	
	private void stateInit() throws XMLStreamException {
//		super.status(super.status().modify(totalSize, FIELD.TOTALSIZE, true, "Processing"));
		super.statusModify(totalSize, FIELD.TOTALSIZE, true, "Processing");
        state=STATES.OPENSNAPSHOTSGROUP;
        XML.advance(reader);
	}
	
	private void stateOpenSnapshotsGroup() throws XMLStreamException {
        //try to get a group
        sn=jrain.filesystem.snapshot.xml.SnapshotNodeXML.readSnapshotNode(reader,snap.get().identifiable());
		if(sn==null) {
			super.setErrorMessage("No snapshots group found");
			state=STATES.FINALIZE;
        	return;
		}
		else {
	        if(sn.getValue().getDescriptorType()!=DescriptorType.SNAPSHOTSGROUP) {
				super.setErrorMessage("No snapshots group found");
				state=STATES.FINALIZE;
	        	return;
	        }
		}
        //create tree
        snap.set(new SnapshotTree(sn.getValue()));
        parents.add(sn.getValue().identifiable());
        newIDs.put(sn.getKey(), sn.getValue().identifiable());
        state=STATES.OPENSNAPSHOT;
//		super.status(super.status().modify(p.byteCount(), FIELD.PROCESSEDSIZE, true, null));
        super.statusModify(p.byteCount(), FIELD.PROCESSEDSIZE, true, null);
	}
	
	private void stateOpenSnapshot() throws XMLStreamException {
		sn=jrain.filesystem.snapshot.xml.SnapshotNodeXML.readSnapshotNode(reader,null);
		if(sn==null) {
			super.setErrorMessage("No snapshot found");
			state=STATES.FINALIZE;
        	return;
		}
		else {
	        if(sn.getValue().getDescriptorType()!=DescriptorType.SNAPSHOT) {
				super.setErrorMessage("No snapshot found");
				state=STATES.FINALIZE;
	        	return;
	        }
		}
		snap.set(new SnapshotTree(snap.get(), parents.get(parents.size()-1), sn.getValue(), 1, 0));
		parents.add(sn.getValue().identifiable());
		newIDs.put(sn.getKey(), sn.getValue().identifiable());
		state=STATES.OPENDIRECTORY;
//		super.status(super.status().modify(p.byteCount(), FIELD.PROCESSEDSIZE, true, null));
		super.statusModify(p.byteCount(), FIELD.PROCESSEDSIZE, true, null);
	}
	
	private void stateOpenDirectory() throws XMLStreamException {
		//read directory or file
		sn=jrain.filesystem.snapshot.xml.SnapshotNodeXML.readSnapshotNode(reader,null);
		if(sn==null) {
			super.setErrorMessage("No directory/file found");
			state=STATES.FINALIZE;
        	return;
		}
		else {
	        if(sn.getValue().getDescriptorType()!=DescriptorType.DIRECTORY && sn.getValue().getDescriptorType()!=DescriptorType.FILE) {
				super.setErrorMessage("No directory/file found");
				state=STATES.FINALIZE;
	        	return;
	        }
		}
		snap.set(new SnapshotTree(snap.get(), parents.get(parents.size()-1), sn.getValue(), 1, 0));
		newIDs.put(sn.getKey(), sn.getValue().identifiable());
		if(sn.getValue().getDescriptorType()==DescriptorType.DIRECTORY) {
			parents.add(sn.getValue().identifiable());
		}
		if(reader.isEndElement()) {
			if(reader.getLocalName().equals(jrain.filesystem.snapshot.xml.SnapshotDescriptorXML.XMLSNAPSHOTTAG)) {
				state=STATES.CLOSESNAPSHOT;
			}
			else if(reader.getLocalName().equals(jrain.filesystem.snapshot.xml.DirectoryDescriptorXML.XMLDIRECTORYTAG)) {
				state=STATES.CLOSEDIRECTORY;
			}
		}
		else {
			//start element so stay in the same state
		}
//		super.status(super.status().modify(p.byteCount(), FIELD.PROCESSEDSIZE, true, null));
		super.statusModify(p.byteCount(), FIELD.PROCESSEDSIZE, true, null);
	}
	
	private void stateCloseDirectory() throws XMLStreamException {
		parents.remove(parents.size()-1);
		XML.advancePast(reader,jrain.filesystem.snapshot.xml.DirectoryDescriptorXML.XMLDIRECTORYTAG);
		XML.advance(reader);
		if(reader.isEndElement()) {//can close snapshot or directory
			if(reader.getLocalName().equals(jrain.filesystem.snapshot.xml.SnapshotDescriptorXML.XMLSNAPSHOTTAG)) {
				state=STATES.CLOSESNAPSHOT;
			}
			else if(!reader.getLocalName().equals(jrain.filesystem.snapshot.xml.DirectoryDescriptorXML.XMLDIRECTORYTAG)) {
				super.setErrorMessage("No closing snapshot found");
				state=STATES.FINALIZE;
	        	return;
			}
		}
		else {//start element
			state=STATES.OPENDIRECTORY;
		}
	}
	
	private void stateCloseSnapshot() throws XMLStreamException {
		parents.remove(parents.size()-1);
		XML.advancePast(reader,jrain.filesystem.snapshot.xml.SnapshotDescriptorXML.XMLSNAPSHOTTAG);
		XML.advance(reader);
		if(reader.isEndElement()) {//can close snapshot group only
			if(reader.getLocalName().equals(jrain.filesystem.snapshot.xml.SnapshotsGroupDescriptorXML.XMLSNAPSHOTSGROUPTAG)) {
				state=STATES.CLOSESNAPSHOTSGROUP;
			}
			else {
				super.setErrorMessage("No closing snapshots group found");
				state=STATES.FINALIZE;
	        	return;
			}
		}
		else {//start element
			state=STATES.OPENSNAPSHOT;
		}
	}
	
	private void stateCloseSnapshotsGroup() {
        //update duplicates of all nodes by their new IDs
        Iterator<TreeNode<SnapshotNode, Identifiable>> it = snap.get().getNodes().iterator();
        while(it.hasNext()) {
        	TreeNode<SnapshotNode, Identifiable> n = it.next();
        	updateDuplicates(n.getData());
        }
		state=STATES.FINALIZE;
	}
	
	private void stateFinalize() {
//		super.status(super.status().modify(-1, FIELD.NONE, true, "Done"));
		super.statusModify(-1, FIELD.NONE, true, "Done");
		if(super.errorMessage()==null) super.finish();
		else super.abort();
	}
	
	protected void step() throws Exception{
		switch (state) {
		case IDLE:break;
		case INIT:stateInit(); break;
		case OPENSNAPSHOTSGROUP:stateOpenSnapshotsGroup(); break;
		case OPENSNAPSHOT:stateOpenSnapshot(); break;
		case OPENDIRECTORY:stateOpenDirectory(); break;
		case CLOSEDIRECTORY:stateCloseDirectory(); break;
		case CLOSESNAPSHOT:stateCloseSnapshot(); break;
		case CLOSESNAPSHOTSGROUP:stateCloseSnapshotsGroup(); break;
		default://finalize
			stateFinalize();break;
		}
	}

	@Override
	public void initialize() throws Exception {
		//does nothing
	}

	@Override
	public void endStep() throws Exception {
		if(snap!=null){
			super.validate();
		}
	}

	@Override
	public UUID identifier() {
		return snap.get().identifier();
	}

	@Override
	public String objectType() {
		return snap.get().objectType();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Identifiable identifiable() {
		return snap.get().identifiable();
	}

	@Override
	public <T extends jrain.identifiable.Identifiable<UUID> > boolean matches(T ID) {
		return snap.get().matches(ID);
	}

}
