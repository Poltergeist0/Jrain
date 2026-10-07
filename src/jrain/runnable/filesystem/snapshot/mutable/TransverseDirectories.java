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
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import jrain.filesystem.snapshot.immutable.SnapshotDescriptor;
import jrain.filesystem.snapshot.snapshotTree.immutable.SnapshotNode;
import jrain.filesystem.snapshot.snapshotTree.immutable.SnapshotTree;
import jrain.NumberWithMultiple.ByteSizeWithMultiple;
import jrain.differentialHistory.immutable.DifferentialHistory;
import jrain.identifiable.immutable.Identifiable;
import jrain.runnable.RunnableStepByStep;
import jrain.runnable.RunnableStepByStepStatistics;
import jrain.runnable.RunnableStepByStepStatistics.FIELD;


/**
 * @author poltergeist0
 *
 * This class builds a snapshot tree from a base path with a snapshot descriptor
 * as top node (no snapshots group) and directories and files as its children.
 * It is single threaded.
 */
public class TransverseDirectories extends RunnableStepByStep implements jrain.identifiable.Identifiable<UUID>{
	
	/**
	 * Default date and time to use when such information can not be read from a file/directory
	 */
	private static LocalDateTime LocalDateTime0=LocalDateTime.ofInstant(Instant.ofEpochMilli(0), ZoneId.systemDefault());
	
	/**
	 * Convert the given list of paths to absolute paths that start with the given base path.
	 * Any path that is already absolute is dismissed.
	 * 
	 * @param basePath is the base path
	 * @param paths are the paths to convert into absolute paths
	 * @return a list with the paths converted
	 */
	public static Set<Path> toPath(final String basePath, final Set<String> paths){
		Path bp=new File(basePath).toPath().normalize();//remove redundant name elements
		HashSet<Path> p=new HashSet<>();
		Iterator<String> it = paths.iterator();
		while(it.hasNext()) {
			String a = it.next();
			File file = new File(a);
			Path pth=file.toPath().normalize();
			if(!pth.isAbsolute()) pth=bp.resolve(pth);//get absolute path of pth by joining with bp
			if(pth.startsWith(bp)) p.add(pth);//do not add if the final path does not start with the base path (contains "..") because the previous resolve may return pth if it is already an absolute path
		}
		return p;
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
	
	private final String _pathSeparator;
	private final ByteSizeWithMultiple bufferSize;
	
	/**
	 * current state of the state machine
	 */
	private STATES state=STATES.IDLE;
	
	private final Set<String> calc;
	
	/**
	 * Maximum byte count for calculating hashes.
	 * If zero, hashes are calculated for the entire size of the file.
	 * If bigger than zero, indicates the maximum number of bytes to use to
	 * calculate the hashes, even if the file is bigger. Corresponds to 
	 * calculating the hashes on the first maxByteCount bytes of the file.
	 * Can not accept values smaller than zero.
	 */
	private final ByteSizeWithMultiple maxByteCount;
	
	/**
	 * Constructor for a new snapshot.
	 * 
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
			final Set<Path> filterOut,
			final Set<String> hashes,
			ByteSizeWithMultiple byteCount,
			final int recursion,
			final ByteSizeWithMultiple BufferSize
			) throws Exception{
		super();
		name=snapshotName;
		_basePath=basePath;
		_pathSeparator="/";
		calc=jrain.hash.hashInstance.hashes.Hashes.validateHashes(hashes);
		maxByteCount=(byteCount==null)?new ByteSizeWithMultiple():byteCount;
//		bufferSize=FileCopyChecksum.calculateBufferSize(BufferSize);
		bufferSize=BufferSize;
		SnapshotDescriptor sd=new SnapshotDescriptor(null,name, _basePath, calc, 0,null,null,0);
		pdt=new ProcessDirectoryTree(new SnapshotNode(sd, null, false, true),(filterOut==null)?new HashSet<>():filterOut, calc, (recursion>=0)?recursion:Integer.MAX_VALUE,_pathSeparator,LocalDateTime0);
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
	
	protected void step() throws Exception{
		if(state==STATES.IDLE)return;
		if(state==STATES.INIT){
			pdt.go();
			state=STATES.PROCESS_DIRECTORY;
		}
		else if(state==STATES.PROCESS_DIRECTORY){	//process each directory
			pdt.process();
			if(pdt.running()) {
				if(mrk.changed()) super.status(mrk.getAndUpdate());
			}
			else{	//go to next state
				super.status(mrk.getAndUpdate());
				snap=pdt.snapshot();
				pdt=null;//no longer needed. Let memory be freed
				state=STATES.GET_FILES;
			}
		}
		else if(state==STATES.UPDATE_SIZES){	//update sizes of nodes
			state=STATES.GET_FILES;//go to next state
		}
		else if(state==STATES.GET_FILES){	//checksum each file
			if(calc==null || calc.size()<=0){
				//no checksum to calculate
				state=STATES.FINALIZE;
			}
			else{
				pf=new ProcessFiles(snap, super.status(), calc,maxByteCount, _pathSeparator, bufferSize);
				mrk=pf.marker();
				pf.go();
				state=STATES.PROCESS_CHECKSUM;
			}
		}
		else if(state==STATES.PROCESS_CHECKSUM){
			pf.process();
			if(pf.running()) {
				if(mrk.changed()) super.status(mrk.getAndUpdate());
			}
			else{	//go to next state
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
			
}
