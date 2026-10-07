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
package jrain.runnable.filesystem.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystemException;
import java.security.NoSuchAlgorithmException;
import java.util.Set;

import jrain.NumberWithMultiple.ByteSizeWithMultiple;
import jrain.NumberWithMultiple.Numbers.BinaryMultiples;
import jrain.exceptions.ExceptionNotAFile;
import jrain.hash.hashInstance.hashes.immutable.Hashes;
import jrain.runnable.RunnableStepByStep;
import jrain.runnable.RunnableStepByStepStatistics;
import jrain.runnable.RunnableStepByStepStatistics.FIELD;

/**
 * @author poltergeist0
 *
 * Single threaded Class that:
 * 1) Can take one file and calculate checksums 
 * 2) Can take one file, calculate checksums while copying it, calculate checksums
 * 		of the copy and verify equality between copy and original, and/or verify
 * 		identity by comparing byte by byte
 * 3) Can take two files, calculate checksums and verify equality between them
 * 		and/or verify identity by comparing byte by byte
 * 
 * Two files are considered equal if their checksums are identical.
 * 
 * Two files are identical if their contents are identical.
 */
public class FileCopyChecksum extends RunnableStepByStep{
	
	public static class FileCopyChecksumOptions{
		private boolean overwrite;
		private boolean hashOutputFile;
		private boolean compareFilesByteByByte;
		private Set<String> hashes;
//		private long byteCount;
//		private int bufferSize;
		private ByteSizeWithMultiple byteCount;
		private ByteSizeWithMultiple bufferSize;

		/**
		 * Get a new instance of FileCopyChecksumOptions
		 * 
		 * @param overwrite
		 * @param hashOutputFile
		 * @param compareFilesByteByByte
		 * @param hashes
		 * @param byteCount
		 * @param BufferSize
		 * @return
		 */
		public static FileCopyChecksumOptions instance(
				boolean overwrite,
				boolean hashOutputFile,
				boolean compareFilesByteByByte,
				Set<String> hashes,
				ByteSizeWithMultiple byteCount,
				ByteSizeWithMultiple BufferSize
				) {
			FileCopyChecksumOptions opt=new FileCopyChecksumOptions();
			opt.setHashOutputFile(hashOutputFile);
			opt.setCompareFilesByteByByte(compareFilesByteByByte);
			opt.setHashes(hashes);
			opt.setByteCount(byteCount);
			opt.setBufferSize(BufferSize);
			return opt;
		}
		/*
		 * Constructor with default values.
		 * Calculates all hashes and compares files equality (equal hashes) and identity (byte by byte).
		 * Does not overwrite existing files.
		 * Uses a buffer size of 32MB
		 */
		public FileCopyChecksumOptions() {
			overwrite=false;
			hashOutputFile=true;
			compareFilesByteByByte=true;
//			hashes=null;//will cause to calculate all known algorithms
			hashes=jrain.hash.hashInstance.hashes.Hashes.algorithms();
//			byteCount=0;//will calculate hashes for the entire file
			byteCount=new ByteSizeWithMultiple();//will calculate hashes for the entire file
//			bufferSize=0;//will convert to default value
			bufferSize=DefaultBufferSize;
		}
		
		/**
		 * Copy constructor.
		 * 
		 * @param original
		 */
		public FileCopyChecksumOptions(FileCopyChecksumOptions original) {
			overwrite=original.overwrite;
			hashOutputFile=original.hashOutputFile;
			compareFilesByteByByte=original.compareFilesByteByByte;
//			hashes=null;//will cause to calculate all known algorithms
			hashes=original.hashes;
//			byteCount=0;//will calculate hashes for the entire file
			byteCount=original.byteCount;
//			bufferSize=0;//will convert to default value
			bufferSize=original.bufferSize;
		}
		
		/**
		 * @return the overwrite
		 */
		public boolean isOverwrite() {
			return overwrite;
		}
		/**
		 * @param overwrite the overwrite to set
		 */
		public void setOverwrite(boolean overwrite) {
			this.overwrite = overwrite;
		}
		/**
		 * @return the hashOutputFile
		 */
		public boolean isHashOutputFile() {
			return hashOutputFile;
		}
		/**
		 * @param hashOutputFile the hashOutputFile to set
		 */
		public void setHashOutputFile(boolean hashOutputFile) {
			this.hashOutputFile = hashOutputFile;
		}
		/**
		 * @return the compareFilesByteByByte
		 */
		public boolean isCompareFilesByteByByte() {
			return compareFilesByteByByte;
		}
		/**
		 * @param compareFilesByteByByte the compareFilesByteByByte to set
		 */
		public void setCompareFilesByteByByte(boolean compareFilesByteByByte) {
			this.compareFilesByteByByte = compareFilesByteByByte;
		}
		/**
		 * @return the hashes
		 */
		public Set<String> getHashes() {
			return hashes;
		}
		/**
		 * @param hashes the hashes to set
		 */
		public void setHashes(Set<String> hashes) {
			if(hashes==null) {
				this.hashes=jrain.hash.hashInstance.hashes.Hashes.algorithms();
			}
			else {
				this.hashes = hashes;
			}
		}
		/**
		 * @return the byteCount
		 */
		public ByteSizeWithMultiple getByteCount() {
			return byteCount;
		}
		/**
		 *
		 * Maximum byte count for calculating hashes.
		 * If zero, hashes are calculated for the entire size of the file.
		 * If bigger than zero, indicates the maximum number of bytes to use to
		 * calculate the hashes, even if the file is bigger. Corresponds to 
		 * calculating the hashes on the first maxByteCount bytes of the file.
		 * Can not accept values smaller than zero.
		 *
		 * @param byteCount the byteCount to set
		 */
		public void setByteCount(ByteSizeWithMultiple byteCount) {
			if(byteCount.get()>=0 || byteCount.get()<=Long.MAX_VALUE) {
				this.byteCount = byteCount;
			}
			else {//the value is negative. Use zero (for all file)
				byteCount=new ByteSizeWithMultiple();
			}
		}
		/**
		 * @return the bufferSize
		 */
		public ByteSizeWithMultiple getBufferSize() {
			return bufferSize;
		}
		/**
		 * @return the bufferSize
		 */
		public static ByteSizeWithMultiple getMaximumBufferSize() {
			return MaxBufferSize;
		}
		/**
		 * @return the bufferSize
		 */
		public static ByteSizeWithMultiple getDefaultBufferSize() {
			return DefaultBufferSize;
		}
		/**
		 * @param bufferSize the bufferSize to set
		 */
		public void setBufferSize(ByteSizeWithMultiple bufferSize) {
			this.bufferSize = calculateBufferSize(bufferSize);
		}

		public static ByteSizeWithMultiple calculateBufferSize(ByteSizeWithMultiple requestedSize){
			if(requestedSize.get()>0 && requestedSize.get()<=MaxBufferSize.get()){
				return requestedSize;
			}
			return DefaultBufferSize;
		}
		
	}
	/*
	 * Internal possible states
	 */
	private static enum STATES {IDLE,INIT_FI,INIT_FO,INIT_COMPARE,PROCESS_FI,PROCESS_FO,PROCESS_COMPARE,FINALIZE_FI,FINALIZE_FO,VERIFY,FINALIZE,FINALIZE_ERROR};
	
	/*
	 * Maximum file buffer of 1GB
	 */
	public static final ByteSizeWithMultiple MaxBufferSize=ByteSizeWithMultiple.toByteSizeWithMultiple((long) (1024*1024*1024),BinaryMultiples.Ui);

	/*
	 * Default file buffer of 32MB per file
	 */
	public static final ByteSizeWithMultiple DefaultBufferSize=ByteSizeWithMultiple.toByteSizeWithMultiple((long) (32*1024*1024),BinaryMultiples.Ui);
	
	/*
	 * Input file handle
	 */
	private final File fi;
	
	/*
	 * Output file handle
	 */
	private final File fo;
	
	/*
	 * Input file stream
	 */
	private FileInputStream fsi=null;

	/*
	 * Input file stream.
	 * Only used when comparing files byte by byte
	 */
	private FileInputStream fsi2=null;

	/*
	 * Output file stream
	 */
	private FileOutputStream fso=null;
	
	/*
	 * Flag indicating that output file is to be copied from input file
	 */
	private final boolean copyFile;
	
	/*
	 * Flag indicating that hashes are to be calculated
	 */
//	private final boolean hashOutput;
	
	/**
	 * Maximum byte count for calculating hashes.
	 * If zero, hashes are calculated for the entire size of the file.
	 * If bigger than zero, indicates the maximum number of bytes to use to
	 * calculate the hashes, even if the file is bigger. Corresponds to 
	 * calculating the hashes on the first maxByteCount bytes of the file.
	 * Can not accept values smaller than zero.
	 */
//	private final long maxByteCount;
	
	/*
	 * Flag indicating the files must be compared byte by byte
	 */
//	private final boolean compareByteByByte;
	
	/*
	 * Set with the names of the hashes to calculate
	 */
//	private final Set<String> calc;
	
	/*
	 * File buffer
	 */
	byte b[]=null;

	/*
	 * File buffer.
	 * Only used when comparing two files byte by byte
	 */
	byte b2[]=null;
	
	/*
	 * Hashes for the input file
	 */
	private Hashes hi=null;

	/*
	 * Hashes for the output file
	 */
	private Hashes ho=null;
	
	/*
	 * Chosen file buffer size
	 */
//	private final int bufferSize;
	
	/**
	 * current state of the state machine
	 */
	private STATES state=STATES.IDLE;
	
	/*
	 * Flag indicating if the input file is readable
	 */
	private boolean canReadInput=true;

	/*
	 * Flag indicating if the output file is readable
	 */
	private boolean canReadOutput=true;
	
	private final FileCopyChecksumOptions opt;
	
	public boolean copy() {return copyFile;}
	
//	public boolean hashOutput() {return hashOutput;}
	
//	public boolean compareByteByByte() {return compareByteByByte;}
	
//	public int bufferSize() {return bufferSize;}
	
	/**
	 * copy and/or checksum a file and/or compare two files byte by byte
	 * 
	 * @param inputfile is the file to copy/checksum
	 * @param outputFile is the name of the file to which to copy the input file. If null, only checksum is performed
	 * @param copy if true will copy the input file to output file, otherwise it will only checksum (if outputFile is null) and/or compare the input and output files
	 * @param overwrite if true will overwrite the output file without warning when copying is selected
	 * @param hashOutputFile indicates if it is to checksum the output file and compare with the checksum of the input file
	 * @param compareFilesByteByByte indicates if it is to compare the files byte by byte
	 * @param hashes is a set with the names of the hashes to calculate
	 * @param byteCount is the number of bytes, at the start of the file, that are used to calculate hashes. Limited to a max value of 9223372036854775807, inclusive (64bit signed long) 
	 * @param BufferSize sets the size of the buffer to be used for all reading/checksum/writing operations. Default is 32MB
	 * @throws Exception when files can not be opened/accessed/written
	 */
	public FileCopyChecksum(
			File inputfile,
			File outputFile,
			boolean copy,
			boolean overwrite,
			boolean hashOutputFile,
			boolean compareFilesByteByByte,
			Set<String> hashes,
			ByteSizeWithMultiple byteCount,
			ByteSizeWithMultiple BufferSize
		) throws Exception{
		this(inputfile,outputFile,copy, FileCopyChecksumOptions.instance(overwrite, hashOutputFile, compareFilesByteByByte, hashes, byteCount, BufferSize));
	}
	
	/**
	 * Constructor using options instead of individual parameters.
	 * 
	 * @param inputfile
	 * @param outputFile
	 * @param copy
	 * @param options
	 * @throws Exception
	 */
	public FileCopyChecksum(
			File inputfile,
			File outputFile,
			boolean copy,
			FileCopyChecksumOptions options
		) throws Exception{
//		this(inputfile,outputFile,copy,options.isOverwrite(),options.isHashOutputFile(),options.isCompareFilesByteByByte(),options.getHashes(),options.getByteCount(),options.getBufferSize());
		super();
		if(inputfile==null) throw new ExceptionNotAFile("NULL");
		if(!inputfile.exists()) throw new FileNotFoundException(inputfile.getAbsolutePath());
		if(!inputfile.isFile()) throw new ExceptionNotAFile(inputfile.getAbsolutePath());
		fi=inputfile;
		if(fi.canRead()) canReadInput=true;
		else throw new AccessDeniedException("Can not read file "+fi.getAbsolutePath());
		copyFile=copy;
		FileCopyChecksumOptions optns=new FileCopyChecksumOptions(options);//this is required because options can be changed when there is no output file
		if(copyFile){
			if(outputFile==null){	//write to file with "(copy)" appended before the extension
//				hashOutput=hashOutputFile;
				fo=new File(
						inputfile.getAbsolutePath().substring(
								0, 
								inputfile.getAbsolutePath().lastIndexOf(".")-1
							)+
						"(copy)"+
						inputfile.getAbsolutePath().substring(
								inputfile.getAbsolutePath().lastIndexOf("."),
								inputfile.getAbsolutePath().length()
							)
					);
//				compareByteByByte=compareFilesByteByByte;
			}
			else{	//write to given file
//				hashOutput=hashOutputFile;
				fo=outputFile;
//				compareByteByByte=compareFilesByteByByte;
			}
			if(!fo.exists()){	//output file does not exist
				File f=fo.getParentFile();	//temporary assignment
				if(!f.exists()){//parent directory does not exist
					//attempt to create directory tree
					if(!f.mkdirs()) throw new FileSystemException("Can not create parent directories");
				}
				if(!fo.createNewFile()) throw new FileSystemException("Can not create output file");
			}
			else{	//output file exists
				if(!fo.isFile()) throw new ExceptionNotAFile(fo.getAbsolutePath());
				if(!optns.isOverwrite()) throw new FileAlreadyExistsException(fo.getAbsolutePath());
			}
//			hashOutput=hashOutputFile;
//			compareByteByByte=compareFilesByteByByte;
		}
		else{	//do not copy
			if(outputFile==null){//do not compare
				fo=null;
				optns.setHashOutputFile(false);
				optns.setCompareFilesByteByByte(false);
			}
			else{//compare
				fo=outputFile;
//				hashOutput=hashOutputFile;
//				compareByteByByte=compareFilesByteByByte;
			}
		}
//		calc=jrain.hash.hashInstance.hashes.Hashes.validateHashes(hashes);
//		maxByteCount=(byteCount.get()<=0 || byteCount.get()>=Long.MAX_VALUE)?0:byteCount.get().longValue();
//		bufferSize=calculateBufferSize(BufferSize).get().intValue();
		b=new byte[optns.getBufferSize().toInt()];
		state=STATES.INIT_FI;
		if(!copy && fo==null) canReadOutput=false;
		else if(copy && !fo.canRead()) throw new AccessDeniedException("Can not read file "+fo.getAbsolutePath());
		else canReadOutput=true;
		opt=optns;
		super.status(new RunnableStepByStepStatistics(0,0, "Getting "+fi.getAbsolutePath()));
		super.go();
	}
	
//	public static ByteSizeWithMultiple calculateBufferSize(ByteSizeWithMultiple requestedSize){
//		if(requestedSize.get()>0 && requestedSize.get()<=MaxBufferSize.get()){
//			return requestedSize;
//		}
//		return DefaultBufferSize;
//	}
	
	/**
	 * Read _bufferSize bytes of data from the input file and, optionally, calculate any
	 * hash and write the data to an output file
	 * @throws IOException 
	 */
	private void processBuffer() throws IOException{
		int avail=fsi.available();
		if(avail>opt.getBufferSize().toInt()) avail=opt.getBufferSize().toInt();
		int rd = fsi.read(b);
//		System.out.println("in="+rd);
		if(opt.getByteCount().toLong()>0 && super.status().getProcessedSize()+rd>opt.getByteCount().toLong())rd=(int) (opt.getByteCount().toLong()-super.status().getProcessedSize());
		ho.update(b, 0, rd);
		if(fso!=null) fso.write(b, 0, rd);
//		super.processedSize(super.processedSize()+avail);
		super.statusModify(rd, FIELD.PROCESSEDSIZE, false, null);
	}
	
	private int compareByByte() throws IOException {
		int avail=fsi.available();
		int avail2=fsi2.available();
		if(avail>opt.getBufferSize().toInt()) avail=opt.getBufferSize().toInt();
		if(avail2>opt.getBufferSize().toInt()) avail2=opt.getBufferSize().toInt();
		if(avail!=avail2) {//throw new ExceptionProcessing("Files sizes do not match");
			if(avail<avail2) return 1;
			else return -1;
		}
		fsi.read(b);
		fsi2.read(b2);
		for (int i = 0; i < b.length; i++) {
			if(b[i]!=b2[i]) {//return false;
				if(b[i]<b2[i]) return 1;
				else return -1;
			}
		}
//		super.processedSize(super.processedSize()+avail);
		super.statusModify(avail, FIELD.PROCESSEDSIZE, false, null);
		return 0;
	}
	
	@Override
	protected void initialize() throws Exception {
		//does nothing		
	}
	
	/**
	 * use this method to run the copy/checksum/compare on non threaded applications.
	 * It must be called while the object is active
	 * 
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	public void step() throws IOException{
		if(state==STATES.IDLE){
//			System.out.println("fcc idel");
			return;
		}
		if(state==STATES.INIT_FI){	//initialize
//			System.out.println("fcc init fi");
//			super.totalSize(fi.length());
//			super.processedSize(0);
//			super.action("Processing "+fi.getAbsolutePath());
			super.statusModify(fi.length(), FIELD.TOTALSIZE, true, "Calculating checksums for "+fi.getAbsolutePath());
			//calculate only given hashes or none if set is empty
			ho=new Hashes(opt.getHashes());//processBuffer() only uses ho. Later switch results to hi
//			if(fi.canRead()){
				fsi=new FileInputStream(fi);
				if(!copyFile){
					fso=null;
				}
				else{
					fso=new FileOutputStream(fo);
				}
				state=STATES.PROCESS_FI;
//			}
//			else{//can not read file
//				System.out.println("WARNING: Can not read file"+fi.getAbsolutePath());
//				canReadInput=false;
//				hi=new Hashes(new HashSet<>());
//				super.processedSize(super.processedSize()+fi.length());
//				state=STATES.FINALIZE;
//			}
		}
		else if(state==STATES.PROCESS_FI){	//copy checksum fi
//			System.out.println("fcc proc fi");
			processBuffer();
//			System.out.println(super.status().getProcessedSize()+"=="+super.status().getTotalSize());
//			if(super.getStatus().getProcessedSize().equals(super.getStatus().getTotalSize())){	//go to next state
			if(
					opt.getByteCount().toInt()==0 && super.status().getProcessedSize().equals(super.status().getTotalSize())
					||
					opt.getByteCount().toInt()>0 && super.status().getProcessedSize().equals(opt.getByteCount().toInt())
				){	//go to next state
				state=STATES.FINALIZE_FI;
			}
		}
		else if(state==STATES.FINALIZE_FI){	//checksum fo
//			System.out.println("fcc finalize fi");
//			super.action("Calculating checksums for "+fi.getAbsolutePath());
//			super.status(super.status().modify(-1, FIELD.NONE, false, "Done"));
			if(fsi!=null)fsi.close();
			if(fso!=null)fso.close();//may be open if was copying
			ho.digest();
			hi=ho;//switch results to hi
			if(opt.isHashOutputFile()){	//calc hash of output file
				state=STATES.INIT_FO;
			}
			else if(opt.isCompareFilesByteByByte()){
				state=STATES.INIT_COMPARE;
			}
			else{
				super.statusModify(-1, FIELD.NONE, false, "Done calculating checksums for "+fi.getAbsolutePath());
				super.validate();
				state=STATES.FINALIZE;
			}
		}
		else if(state==STATES.INIT_FO){	//checksum fo
//			System.out.println("fcc init fo");
//			super.processedSize(0);
//			super.totalSize(fo.length());
//			super.action("Processing "+fo.getAbsolutePath());
			super.status(new RunnableStepByStepStatistics(0,fo.length(), "Calculating checksums for "+fo.getAbsolutePath()));
			fsi=new FileInputStream(fo);
			fso=null;
//			if(calc==null) ho=new Hashes();//calculate all hashes
//			else  
			ho=new Hashes(opt.getHashes());//calculate only given hashes or none if set is empty
			state=STATES.PROCESS_FO;
		}
		else if(state==STATES.PROCESS_FO){
//			System.out.println("fcc proc fo");
			processBuffer();
//			if(super.status().getProcessedSize().equals(super.status().getTotalSize())){	//go to next state
			if(
					opt.getByteCount().toInt()==0 && super.status().getProcessedSize().equals(super.status().getTotalSize())
					||
					opt.getByteCount().toInt()>0 && super.status().getProcessedSize().equals(opt.getByteCount().toInt())
				){	//go to next state
				state=STATES.FINALIZE_FO;
			}
		}
		else if(state==STATES.FINALIZE_FO){
//			System.out.println("fcc finalize fo");
//			super.action("Calculating checksums for "+fo.getAbsolutePath());
//			super.status(super.status().modify(-1, FIELD.NONE, false, "Calculating checksums for "+fo.getAbsolutePath()));
			fsi.close();
			ho.digest();
			state=STATES.VERIFY;
		}
		else if(state==STATES.VERIFY){	//verify checksum
//			System.out.println("fcc verify");
			if(hi.compare(ho)) {
				if(opt.isCompareFilesByteByByte()){
					state=STATES.INIT_COMPARE;
				}
				else{
					super.validate();
//					super.action("Checksum verification completed successfuly.");
					super.statusModify(-1, FIELD.NONE, false, "Checksum verification completed successfuly.");
					state=STATES.FINALIZE;
				}
			}
			else {
//				super.action("Checksum verification completed in error. Hashes are different.");
				super.statusModify(-1, FIELD.NONE, false, "Checksum verification completed in error. Hashes are different.");
				state=STATES.FINALIZE_ERROR;
			}
		}
		else if(state==STATES.INIT_COMPARE){	//verify byte by byte
//			System.out.println("fcc init comp");
//			super.processedSize(0);
//			super.totalSize(fi.length());
//			super.action("Comparing "+fi.getAbsolutePath()+" and "+fo.getAbsolutePath()+" byte by byte");
			super.status(new RunnableStepByStepStatistics(0,fi.length(), "Comparing by byte "+fi.getAbsolutePath()+" and "+fo.getAbsolutePath()));
			fsi=new FileInputStream(fi);
			fsi2=new FileInputStream(fo);
			b2=new byte[opt.getBufferSize().toInt()];
			state=STATES.PROCESS_COMPARE;
		}
		else if(state==STATES.PROCESS_COMPARE){
//			System.out.println("fcc proc comp");
//			try {
				if(compareByByte()!=0){
//					super.action("Comparison by byte completed unsuccessfuly.");
					super.statusModify(-1, FIELD.NONE, false, "Byte by byte verification completed in error.");
					state=STATES.FINALIZE_ERROR;
				}
				else{
					if(super.status().getProcessedSize().equals(super.status().getTotalSize())){	//go to next state
						fsi.close();
						fsi2.close();
						super.statusModify(-1, FIELD.NONE, false, "Byte by byte verification completed successfuly.");
						super.validate();
						state=STATES.FINALIZE;
					}
				}
//			} catch (ExceptionProcessing e) {
//				super.action("Comparison by byte terminated due to unexpected different input and output files sizes.");
//				state=STATES.FINALIZE;
//			}
		}
		else if(state==STATES.FINALIZE){	//finalize
//			System.out.println("fcc finalize");
//			state=STATES.IDLE;	//send to IDLE to be safe just in case the thread takes too long to execute the kill() command
			super.finish();
		}
		else if(state==STATES.FINALIZE_ERROR){	//finalize with error
//			System.out.println("fcc finalize");
//			state=STATES.IDLE;	//send to IDLE to be safe just in case the thread takes too long to execute the kill() command
			super.abort();
		}
	}
	
	@Override
	protected void endStep() throws Exception {
		if(super.status().getProcessedSize().equals(super.status().getTotalSize())){	//go to next state
			super.validate();
		}
	}
	
	/* (non-Javadoc)
	 * @see unifiedLibrary.runnable.RunnableStepByStep#process()
	 */
//	protected void process(){super.process();}
	
	/**
	 * hashes can only be read after completion (isActive returns false)
	 * 
	 * @return
	 */
	public Hashes getInputFile(){
		if(super.running()) return null;
		return hi;
	}
	
	/**
	 * hashes can only be read after completion (isActive returns false)
	 * 
	 * @return
	 */
	public Hashes getOutputFile(){
		if(super.running()) return null;
		return ho;
	}

	public boolean canReadInputFile(){
//		if(super.running()) return true;
		return canReadInput;
	}

	public boolean canReadOutputFile(){
//		if(super.running()) return true;
		return canReadOutput;
	}
}
