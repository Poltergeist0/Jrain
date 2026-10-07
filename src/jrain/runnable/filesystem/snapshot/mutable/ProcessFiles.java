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
import java.util.ArrayList;
import java.util.Set;

import jrain.NumberWithMultiple.ByteSizeWithMultiple;
import jrain.differentialHistory.immutable.DifferentialHistory;
import jrain.filesystem.snapshot.immutable.FileDescriptor;
import jrain.filesystem.snapshot.snapshotTree.immutable.SnapshotNode;
import jrain.filesystem.snapshot.snapshotTree.immutable.SnapshotTree;
import jrain.identifiable.immutable.Identifiable;
import jrain.runnable.RunnableStepByStep;
import jrain.runnable.RunnableStepByStepStatistics;
import jrain.runnable.RunnableStepByStepStatistics.FIELD;
import jrain.runnable.filesystem.file.FileCopyChecksum;
import jrain.treeIdentifiable.immutable.TreeNode;

/**
 * Calculate hashes for files in the given directory tree.
 * 
 * @author poltergeist0
 *
 */
public class ProcessFiles extends RunnableStepByStep{
	
	private class FileChecksum extends FileCopyChecksum{

		public FileChecksum(File inputfile, File outputFile, boolean copy,
				boolean overwrite, boolean hashOutputFile,
				boolean compareFilesByteByByte, 
				Set<String> hashes,
				ByteSizeWithMultiple byteCount,
				ByteSizeWithMultiple BufferSize)
				throws Exception {
			super(inputfile, outputFile, copy, overwrite, hashOutputFile,
					compareFilesByteByByte, hashes, byteCount, BufferSize);
		}
		
		protected void process(){super.process();}
		
	}
	
	/**
	 * key is the node level
	 * top node is level zero
	 * children have increasing level
	 */
	private ArrayList<TreeNode<SnapshotNode, Identifiable>> processingNodes;
	private TreeNode<SnapshotNode, Identifiable> current=null;
	private final Set<String> ha;
	
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
	 * snapshot used in processing
	 */
	private SnapshotTree sn;
	
	private FileChecksum fcc;
	
	private DifferentialHistory<RunnableStepByStepStatistics>.HistoryMarker mrk;
	
	private final String _pathSeparator;
	private final ByteSizeWithMultiple bufferSize;
	
	public SnapshotTree snapshot() {return (super.running())?null:sn;}
	
	public ProcessFiles(SnapshotTree base,RunnableStepByStepStatistics stat, Set<String> hashes,ByteSizeWithMultiple byteCount,String pathSeparator, ByteSizeWithMultiple BufferSize) throws Exception {
		sn=base;
		processingNodes=new ArrayList<>();
		processingNodes.addAll(sn.getFiles());
		ha=hashes;
		maxByteCount=byteCount;
		fcc=null;
		mrk=null;
		_pathSeparator=pathSeparator;
		bufferSize=BufferSize;
		super.status(stat);
	}
	
	protected void step() throws Exception{
		if(fcc==null) {//no file being processed and there are files waiting
			if(processingNodes.size()<=0) {
				//all nodes have been processed
				super.finish();
				super.statusModify(-1,FIELD.NONE,false,"Done.");
				return;// false;
			}
			current=processingNodes.remove(0);
			try {
				fcc=new FileChecksum(
						new File(current.getData().getFileDescriptor().getFullPath(_pathSeparator)),
						null,
						false,
						false, 
						false,
						false,
						ha,
						maxByteCount,
						bufferSize
						);
				mrk=fcc.marker();
			}catch(IOException e) {//can not read given file
				//skip file data
				super.statusModify(current.getData().getDescriptorSize(),FIELD.PROCESSEDSIZE,false,e.getMessage());
				//add to snapshot tree as unreadable
				sn=new SnapshotTree(
						sn, 
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
			if(mrk.changed()) {
				RunnableStepByStepStatistics st = mrk.getAndUpdate();
				super.statusModify(mrk.reference().getProcessedSize()-st.getProcessedSize(),FIELD.PROCESSEDSIZE,false,st.getAction());
			}
			if(!fcc.running()){	//go to next state
				sn=new SnapshotTree(
						sn, 
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
				mrk=null;
				fcc=null;
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

