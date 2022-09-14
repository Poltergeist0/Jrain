package jrain.runnable.filesystem.snapshot.xml.immutable;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import jrain.filesystem.snapshot.snapshotTree.SnapshotNode.DescriptorType;
import jrain.filesystem.snapshot.snapshotTree.immutable.SnapshotNode;
import jrain.filesystem.snapshot.snapshotTree.immutable.SnapshotTree;
import jrain.exceptions.ExceptionProcessing;
import jrain.identifiable.immutable.Identifiable;
import jrain.treeIdentifiable.immutable.TreeNode;
import jrain.io.OutputStreamProgress;
import jrain.runnable.RunnableStepByStep;
import jrain.runnable.RunnableStepByStepStatistics.FIELD;


/**
 * @author poltergeist0
 *
 * Output a snapshot tree into a XML formated file.
 */
public class SnapshotTreeOutputXML extends RunnableStepByStep implements jrain.identifiable.Identifiable<UUID>{
	
	/**
	 * Internal states of the state machine in method step.
	 */
	private static enum STATES {IDLE,INIT,OPENSNAPSHOTSGROUP, CLOSESNAPSHOTSGROUP, OPENSNAPSHOT, CLOSESNAPSHOT, OPENDIRECTORY, CLOSEDIRECTORY, FILE, FINALIZE};
	
	/**
	 * snapshot used in processing
	 */
	private final SnapshotTree snap;
	
	/**
	 * current state of the state machine
	 */
	private STATES state=STATES.IDLE;
	
	/**
	 * Output stream
	 */
	private OutputStreamProgress p =null;
	
	/**
	 * XML stream
	 */
	private XMLStreamWriter reader =null;
	
	/**
	 * Total number of bytes written to file
	 */
	private long totalSize;
	
	/**
	 * List of parent nodes waiting to be closed.
	 * Every time a node is found that may have children, it is added to the end
	 * of this list so that every following child knows who is their parent.
	 * A parent is removed when it has no more children left to process.
	 */
	private HashMap<Identifiable, ArrayList<Identifiable> > parents=null;
	
    /**
     * Current node being processed.
     */
    private TreeNode<SnapshotNode,Identifiable> sn=null;
    
    /**
     * Current number of TABs for line alignment in XML file
     */
    private String tabbing="";

	/**
	 * Write snapshot tree to file
	 * 
	 * @param st is the snapshot tree to write
	 * @param basePath is the file with path to write to
	 * @param append if true appends to the given file otherwise overwrites
	 * @throws Exception
	 */
	public SnapshotTreeOutputXML(
			SnapshotTree st,
			final String basePath,
			boolean append
			) throws Exception{
		super();
		snap=st;
		totalSize=st.getNodes().size();
	    FileOutputStream fis = new FileOutputStream(basePath,append);
        XMLOutputFactory xmlInFact = XMLOutputFactory.newInstance();
        p = new OutputStreamProgress(fis);
        reader = xmlInFact.createXMLStreamWriter(p);
        parents=new HashMap<>();
		state=STATES.INIT;
		super.go();
	}
	
	/**
	 * Write snapshot tree to output stream
	 * 
	 * @param st is the snapshot tree to write
	 * @param stream is the stream to write to
	 * @throws Exception
	 */
	public SnapshotTreeOutputXML(
			SnapshotTree st,
			final OutputStream stream
			) throws Exception{
		super();
		snap=st;
		totalSize=st.getNodes().size();
        XMLOutputFactory xmlInFact = XMLOutputFactory.newInstance();
        p = new OutputStreamProgress(stream);
        reader = xmlInFact.createXMLStreamWriter(p);
        parents=new HashMap<>();
		state=STATES.INIT;
		super.go();
	}
	
	/**
	 * Internal method used to update the status message
	 */
	private void updateStatus() {
//		super.status(super.status().modify(1, FIELD.PROCESSEDITEMS, false, null));
		super.statusModify(1, FIELD.PROCESSEDITEMS, false, null);
	}
	
	private void stateInit() throws XMLStreamException, ExceptionProcessing {
//		super.status(super.status().modify(totalSize, FIELD.TOTALITEMS, true, "Writing"));
		super.statusModify(totalSize, FIELD.TOTALITEMS, true, "Writing");
		sn=snap.getTopNode();
		if(sn.getData().getDescriptorType()==DescriptorType.SNAPSHOTSGROUP) {
			reader.writeStartDocument();
			reader.writeCharacters("\n");
	        state=STATES.OPENSNAPSHOTSGROUP;
		}
		else if(sn.getData().getDescriptorType()==DescriptorType.SNAPSHOT) {
			reader.writeStartDocument();
			reader.writeCharacters("\n");
	        state=STATES.OPENSNAPSHOT;
		}
		else throw new ExceptionProcessing("No snapshots group found");
	}
	
	private void stateOpenSnapshotsGroup() throws XMLStreamException, ExceptionProcessing {
        jrain.filesystem.snapshot.xml.SnapshotNodeXML.writeSnapshotNode(reader,sn.getData(),tabbing);
		if(sn.getChildren().size()<=0) {//there are no snapshots
			state=STATES.CLOSESNAPSHOTSGROUP;
		}
		else {
			tabbing+="\t";
			ArrayList<Identifiable> a = new ArrayList<>(sn.getChildren());
			Identifiable child=a.remove(0);
			parents.put(sn.identifiable(),a);
//			updateStatus();
			sn=snap.getNode(child);
			if(sn.getData().getDescriptorType()!=DescriptorType.SNAPSHOT) {
				throw new ExceptionProcessing("No snapshot found");
			}
	        state=STATES.OPENSNAPSHOT;
		}
	}

	private void stateOpenSnapshot() throws XMLStreamException, ExceptionProcessing {
		jrain.filesystem.snapshot.xml.SnapshotNodeXML.writeSnapshotNode(reader,sn.getData(),tabbing);
		if(sn.getChildren().size()<=0) {//there are no files or directories
			state=STATES.CLOSESNAPSHOT;
		}
		else {
			tabbing+="\t";
			ArrayList<Identifiable> a = new ArrayList<>(sn.getChildren());
			Identifiable child=a.remove(0);
			parents.put(sn.identifiable(),a);
//			updateStatus();
			sn=snap.getNode(child);
			if(sn.getData().getDescriptorType()==DescriptorType.DIRECTORY) {
				state=STATES.OPENDIRECTORY;
			}
			else if(sn.getData().getDescriptorType()==DescriptorType.FILE) {
				state=STATES.FILE;
			}
			else {
				throw new ExceptionProcessing("No file or directory found");
			}
		}
	}	
	
	private void stateOpenDirectory() throws XMLStreamException, ExceptionProcessing {
		jrain.filesystem.snapshot.xml.SnapshotNodeXML.writeSnapshotNode(reader,sn.getData(),tabbing);
		if(sn.getChildren().size()<=0) {//there are no files or directories
			state=STATES.CLOSEDIRECTORY;
		}
		else {
			tabbing+="\t";
			ArrayList<Identifiable> a = new ArrayList<>(sn.getChildren());
			Identifiable child=a.remove(0);
			parents.put(sn.identifiable(),a);
//			updateStatus();
			sn=snap.getNode(child);
			if(sn.getData().getDescriptorType()==DescriptorType.DIRECTORY) {
				state=STATES.OPENDIRECTORY;
			}
			else if(sn.getData().getDescriptorType()==DescriptorType.FILE) {
				state=STATES.FILE;
			}
			else {
				throw new ExceptionProcessing("No file or directory found");
			}
		}
	}
	
	private void stateFile() throws XMLStreamException, ExceptionProcessing {
		jrain.filesystem.snapshot.xml.SnapshotNodeXML.writeSnapshotNode(reader,sn.getData(),tabbing);
		updateStatus();
		Identifiable p=sn.getParent();//get parent. May be a snapshot or directory
		if(parents.get(p).size()<=0) {//parent has no more children left to process
			sn=snap.getNode(p);
			if(sn.getData().getDescriptorType()==DescriptorType.SNAPSHOT) {
				state=STATES.CLOSESNAPSHOT;
			}
			else if(sn.getData().getDescriptorType()==DescriptorType.DIRECTORY) {
				state=STATES.CLOSEDIRECTORY;
			}
			else {
				throw new ExceptionProcessing("Bad descriptor found");
			}
		}
		else {
			sn=snap.getNode(parents.get(p).remove(0));//get another child of snapshot or directory. May be a directory or file
			if(sn.getData().getDescriptorType()==DescriptorType.DIRECTORY) {
				state=STATES.OPENDIRECTORY;
			}
			else if(sn.getData().getDescriptorType()==DescriptorType.FILE) {
				state=STATES.FILE;
			}
			else {
				throw new ExceptionProcessing("Bad descriptor found");
			}
		}
	}

	
	private void stateCloseDirectory() throws XMLStreamException, ExceptionProcessing {
		if(sn.getChildren().size()>0) {
			tabbing=tabbing.substring(0, tabbing.length()-1);
		}
		reader.writeCharacters(tabbing);
		reader.writeEndElement();
		reader.writeCharacters("\n");
		updateStatus();
		Identifiable p=sn.getParent();//get parent. May be a snapshot or directory
		if(parents.get(p).size()<=0) {//parent has no more children left to process
//			tabbing=tabbing.substring(0, tabbing.length()-1);
			sn=snap.getNode(p);
			if(sn.getData().getDescriptorType()==DescriptorType.SNAPSHOT) {
				state=STATES.CLOSESNAPSHOT;
			}
			else if(sn.getData().getDescriptorType()==DescriptorType.DIRECTORY) {
				state=STATES.CLOSEDIRECTORY;
			}
			else {
				throw new ExceptionProcessing("Bad descriptor found");
			}
		}
		else {
			sn=snap.getNode(parents.get(p).remove(0));//get another child of snapshot or directory. May be a directory or file
			if(sn.getData().getDescriptorType()==DescriptorType.DIRECTORY) {
				state=STATES.OPENDIRECTORY;
			}
			else if(sn.getData().getDescriptorType()==DescriptorType.FILE) {
				state=STATES.FILE;
			}
			else {
				throw new ExceptionProcessing("Bad descriptor found");
			}
		}
	}
	
	private void stateCloseSnapshot() throws XMLStreamException, ExceptionProcessing {
		if(sn.getChildren().size()>0) {
			tabbing=tabbing.substring(0, tabbing.length()-1);
		}
		reader.writeCharacters(tabbing);
		reader.writeEndElement();
		reader.writeCharacters("\n");
		updateStatus();
		Identifiable p=sn.getParent();//get snapshots group
		if(p==null) {//no snapshots group
			state=STATES.FINALIZE;
		}
		else {
			if(parents.get(p).size()<=0) {//snapshots group has no more children left to process
//				tabbing=tabbing.substring(0, tabbing.length()-1);
				state=STATES.CLOSESNAPSHOTSGROUP;
			}
			else {
				sn=snap.getNode(parents.get(p).remove(0));
				if(sn.getData().getDescriptorType()!=DescriptorType.SNAPSHOT) {
					throw new ExceptionProcessing("No snapshot found");
				}
		        state=STATES.OPENSNAPSHOT;
			}
		}
	}
	
	private void stateCloseSnapshotsGroup() throws XMLStreamException {
		reader.writeEndElement();
		updateStatus();
		state=STATES.FINALIZE;
	}

	private void stateFinalize() throws XMLStreamException, IOException {
		reader.writeEndDocument();
		reader.close();
		p.close();
//		super.status(super.status().modify(-1, FIELD.NONE, true, "Done"));
		super.statusModify(-1, FIELD.NONE, true, "Done");
		super.finish();
	}
		
	protected void step() throws Exception{
		switch (state) {
		case IDLE:break;
		case INIT:stateInit(); break;
		case OPENSNAPSHOTSGROUP:stateOpenSnapshotsGroup(); break;
		case OPENSNAPSHOT:stateOpenSnapshot(); break;
		case OPENDIRECTORY:stateOpenDirectory(); break;
		case FILE:stateFile();break;
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
		super.validate();
	}

	@Override
	public UUID identifier() {
		return snap.identifier();
	}

	@Override
	public String objectType() {
		return snap.objectType();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Identifiable identifiable() {
		return snap.identifiable();
	}

	@Override
	public <T extends jrain.identifiable.Identifiable<UUID> > boolean matches(T ID) {
		return snap.matches(ID);
	}

}
