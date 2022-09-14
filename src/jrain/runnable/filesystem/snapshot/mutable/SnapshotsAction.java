package jrain.runnable.filesystem.snapshot.mutable;

import jrain.filesystem.snapshot.snapshotTree.immutable.SnapshotTree;
import jrain.identifiable.immutable.Identifiable;



/**
 * @author poltergeist0
 *
 * This class only records actions, never performs any of them.
 */
public class SnapshotsAction {

	public static enum ACTIONTYPE {
		SELECT,
		UNSELECT,
		CHECK,
		UNCHECK,
		COPY,	//copy a file/directory to another location
		MOVE,	//for files/directories, this is implemented as a copy followed by a delete since it provides a way to redo if anything goes wrong
		DELETE,	//permanently deletes a file/directory
		RECYCLE,	//same as delete but deletes to recycle bin if available (requires trash-cli or similar in linux)
		PAUSE,
		RESUME,
		STOP,
		FINISHED
		};
	
	private final ACTIONTYPE act;
	private final Identifiable src;
	private final Identifiable dst;
//	private final SnapshotTree srcNode;
//	private final SnapshotTree dstNode;
	
	public <T extends Identifiable, V extends SnapshotTree> SnapshotsAction(ACTIONTYPE action,T source,V sourceNode,T destination,V destinationNode) {
		act=action;
		if(act==ACTIONTYPE.COPY){
//			if(source==null || destination==null) throw new ExceptionInvalidValue("Source and/or destination can not be null");
			if(source==null || destination==null) throw new NullPointerException("Source and/or destination can not be null");
			src=source;
			dst=destination;
//			srcNode=sourceNode;
//			dstNode=destinationNode;
		}
		else if(act==ACTIONTYPE.DELETE){
			//ignore destination
			if(source==null) throw new NullPointerException("Source can not be null");
			src=source;
			dst=null;
//			srcNode=sourceNode;
//			dstNode=null;
		}
		else if(act==ACTIONTYPE.RECYCLE){
			//ignore destination
			if(source==null) throw new NullPointerException("Source can not be null");
			src=source;
			dst=null;
//			srcNode=sourceNode;
//			dstNode=null;
		}
		else{	//unknown action type
			throw new NullPointerException("Unknown action");
		}
	}
	
	public ACTIONTYPE ActionType(){return act;}
	
	public Identifiable source(){return src;}
	
	public Identifiable destination(){return dst;}
	
//	public SnapshotTree sourceNode(){return srcNode;}
	
//	public SnapshotTree destinationNode(){return dstNode;}
}
