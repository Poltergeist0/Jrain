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
package jrain.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import jrain.filesystem.snapshot.snapshotTree.SnapshotNode.DescriptorType;
import jrain.filesystem.snapshot.snapshotTree.immutable.SnapshotNode;
import jrain.filesystem.snapshot.snapshotTree.immutable.SnapshotTree;
import jrain.identifiable.immutable.Identifiable;
import jrain.treeIdentifiable.immutable.TreeNode;


public class XML_Test {

//	public XML_Test() {
//	}
	
//	public static String readString(XMLStreamReader reader,String tag) throws XMLStreamException {
//		//assume that reader is already at a START_ELEMENT
////		System.out.print("name="+reader.getLocalName());
//		String s=reader.getLocalName();
//		if(s.equals(tag)) {
//			String t=reader.getElementText();
////			System.out.println(" ; text="+t);
////			Identifiable id=new Identifiable(type+unifiedLibrary.immutable.identifiable.interfaces.Identifiable.SEPARATOR+t);
//			return t;
//		}
//		return null;
//	}

	public static boolean readSnapshotsGroup(XMLStreamReader reader) throws XMLStreamException {
		//assume that reader is already at a START_ELEMENT
//		System.out.print("name="+reader.getLocalName());
		String s=reader.getLocalName();
		if(s.equals("snapshotsgroup")) {
			return true;
		}
		return false;
	}
	
	public static void parse() throws XMLStreamException, IOException {
	    try (FileInputStream fis = new FileInputStream("/home/poltergeist0/Desktop/Untitled_merged2.snapshots")) {
	        XMLInputFactory xmlInFact = XMLInputFactory.newInstance();
	        XMLStreamReader reader = xmlInFact.createXMLStreamReader(fis);
	        while(reader.hasNext()) {
	            int a = reader.next(); // do something here
	            String s;
				switch (a) {
//				case XMLStreamConstants.START_DOCUMENT:
//					break;
				case XMLStreamConstants.START_ELEMENT:
//					Identifiable id=jrain.xml.identifiable.immutable.IdentifiableXML.readIdentifiable(reader,"tobedisclosed");
//					if(id!=null) {
//						System.out.println("id="+id);
//					}
//					BaseSnapshotsDescriptor bd=jrain.filesystem.snapshot.immutable.xml.interfaces.BaseSnapshotsDescriptorXML_I.readBaseSnapshotsDescriptor(reader, "tobedisclosed");
//					if(bd!=null) {
//						System.out.println("bd={id="+bd.identifiable()+" ; name="+bd.getDescriptorName()+" ; size="+bd.getDescriptorSize()+"}");
//					}
					
//			        SnapshotsGroupDescriptor sg = SnapshotsGroupDescriptorXML_I.readSnapshotsGroupDescriptor(reader);
//			        if(sg!=null) {//no group found
//			        	System.out.println("sgd={id="+sg.identifiable()+" ; name="+sg.getDescriptorName()+" ; size="+sg.getDescriptorSize()+" ; file name="+sg.getFilename()+"}");
//			        }
//					SnapshotDescriptor sd=jrain.filesystem.snapshot.immutable.xml.interfaces.SnapshotDescriptorXML_I.readSnapshotDescriptor(reader);
//					if(sd!=null) {
//						System.out.println("sd={id="+sd.identifiable()+" ; name="+sd.getDescriptorName()+" ; size="+sd.getDescriptorSize()+" ; base path="+sd.getBasePath()+" ; recursion="+sd.getRecursion()+" ; hashes="+sd.hashes()+"}");
//					}
//					DirectoryDescriptor di=jrain.filesystem.snapshot.immutable.xml.interfaces.DirectoryDescriptorXML_I.readDirectoryDescriptor(reader);
//					if(di!=null) {
//						System.out.println("dd={id="+di.identifiable()+" ; name="+di.getDescriptorName()+" ; path="+di.getPath()+" ; size="+di.getDescriptorSize()+" ; creation date="+di.getCreationDateTime()+" ; modification date="+di.getLastModificationDateTime()+" ; access date="+di.getLastAccessDateTime()+" ; is readable="+di.isReadable()+"}");
//					}
//					FileDescriptor fi=jrain.filesystem.snapshot.immutable.xml.interfaces.FileDescriptorXML_I.readFileDescriptor(reader);
//					if(fi!=null) {
//						System.out.println("fd={id="+fi.identifiable()+" ; name="+fi.getDescriptorName()+" ; path="+fi.getPath()+" ; size="+fi.getDescriptorSize()+" ; creation date="+fi.getCreationDateTime()+" ; modification date="+fi.getLastModificationDateTime()+" ; access date="+fi.getLastAccessDateTime()+" ; is readable="+fi.isReadable()+" ; hashes="+fi.getHashes()+"}");
//					}
					
//					Hashes hs=HashesXML_I.readBaseSnapshotsDescriptor(reader);
//					if(hs!=null) {
//						System.out.println("hs="+hs.toString());
//					}
					s=reader.getLocalName();
					if(
//							s.equals("id")
//							||
//								s.equals("name")
//								||
//								s.equals("size")
//								||
							s.equals("filename")
							||
							s.equals("path")
							||
							s.equals("creationdate")
							||
							s.equals("lastmodificationdate")
							||
							s.equals("lastaccessdate")
							||
							s.equals("isReadable")
							||
							s.equals("duplicate")
//							||
//							s.equals("name")
//							||
//							s.equals("name")
//							||
//							s.equals("name")
						) System.out.println("type="+a+" : "+s+" = "+reader.getElementText());
					else System.out.println("type="+a+" : "+s);
//						System.out.println("type="+a+" : "+s);
					break;
//				case XMLStreamConstants.CHARACTERS:
//					break;
				case XMLStreamConstants.END_ELEMENT:
					System.out.println("end type="+a);
					break;
//				case XMLStreamConstants.START_DOCUMENT:
//					break;
				default:
					System.out.println("type="+a);
					break;
				}
	        }
	    }
	    catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private enum STATE {OPENSNAPSHOTSGROUP, CLOSESNAPSHOTSGROUP, OPENSNAPSHOT, CLOSESNAPSHOT, OPENDIRECTORY, CLOSEDIRECTORY,FINALIZE};

	private static void updateDuplicates(SnapshotTree st, HashMap<Identifiable, Identifiable> newIDs, SnapshotNode node) throws NullPointerException{
		//get duplicates list of current node
		Iterator<Identifiable> a = node.getDuplicates().iterator();
		//construct new node without duplicates
		SnapshotNode sn=new SnapshotNode(node);
		while(a.hasNext()){
			//find in newIDs the old ID and add the value (new ID) to the new duplicates list
			sn=new SnapshotNode(sn, newIDs.get(a.next()),true);
		}
		//replace the (node data) duplicates list with the new one
		st=new SnapshotTree(st, node, sn);
//		return tree;
	}
	
//	public static interface InputStreamListener {
//	    void onBytesRead(long totalBytes);
//	}
	
	public static class PublishingInputStream extends FilterInputStream {
//	    private final InputStreamListener;
	    private long totalBytes = 0;

	    public PublishingInputStream(InputStream in) {
	       super(in);
	    }

	    @Override
	    public int read() throws IOException {
	       int bt = super.read();
	       if(bt>=0)totalBytes++;
	       return bt;
	    }

	    @Override
	    public int read(byte[] b) throws IOException {
	       int count = super.read(b);
	       if(count>=0)totalBytes += count;
	       return count;
	    }

	    @Override
	    public int read(byte[] b, int off, int len) throws IOException {
	       int count = super.read(b,off,len);
	       if(count>=0)totalBytes += count;
	       return count;
	    }

	    @Override
	    public long skip(long n) throws IOException {
	       long count = super.skip(n);
	       if(count>=0)totalBytes += count;
	       return count;
	    }

	    public byte[] readAllBytes() throws IOException{
	    	byte[] count = super.readAllBytes();
	    	if(count.length>=0)totalBytes += count.length;
	    	return count;
	    }
	    
//	    public byte[] readNBytes​(int len) throws IOException{
//	    	byte[] count = super.readNBytes​(len);
//	    	if(count.length>=0)totalBytes += count.length;
//	    	return count;
//	    }
//	    
//	    public int readNBytes​(byte[] b, int off, int len) throws IOException{
//	    	byte[] count = super.readNBytes​(b,off,len);
//	    	if(count.length>=0)totalBytes += count.length;
//	    	return count;
//	    }
	    
	    public long readByteCount() {return totalBytes;}
	}
	
	public static void parse2() throws XMLStreamException, IOException {
		long totalSize=(new File("testFiles/deleteMe.snapshots")).length();
	    try (FileInputStream fis = new FileInputStream("testFiles/deleteMe.snapshots")) {
	        XMLInputFactory xmlInFact = XMLInputFactory.newInstance();
	        PublishingInputStream p = new PublishingInputStream(fis);
	        XMLStreamReader reader = xmlInFact.createXMLStreamReader(p);
	        SnapshotTree st=null;
	        ArrayList<Identifiable> parents=new ArrayList<>();
	        STATE state=STATE.OPENSNAPSHOTSGROUP;
	        XML.advance(reader);
	        Entry<Identifiable, SnapshotNode> sn=null;
	    	/**
	    	 * maps between the new ids (value) and the loaded ids (key).
	    	 * necessary because snapshot duplicates can only be processed after the
	    	 * entire tree is read from file.
	    	 */
	    	HashMap<Identifiable, Identifiable> newIDs=new HashMap<>();
	        do {
	        	switch (state) {
				case OPENSNAPSHOTSGROUP:
			        //try to get a group
			        sn=jrain.filesystem.snapshot.xml.SnapshotNodeXML.readSnapshotNode(reader);
					if(sn==null) {
			        	System.out.println("no group found");
			        	return;
					}
					else {
				        if(sn.getValue().getDescriptorType()!=DescriptorType.SNAPSHOTSGROUP) {
				        	System.out.println("no group found");
				        	return;
				        }
					}
		        	System.out.println("sgd={id="+sn.getValue().getSnapshotsGroupDescriptor().identifiable()+" ; name="+sn.getValue().getSnapshotsGroupDescriptor().getDescriptorName()+" ; size="+sn.getValue().getSnapshotsGroupDescriptor().getDescriptorSize()+" ; file name="+sn.getValue().getSnapshotsGroupDescriptor().getPath()+"}");
			        //create tree
			        st=new SnapshotTree(sn.getValue());
			        parents.add(sn.getValue().identifiable());
			        newIDs.put(sn.getKey(), sn.getValue().identifiable());
			        state=STATE.OPENSNAPSHOT;
					break;
				case OPENSNAPSHOT:
					sn=jrain.filesystem.snapshot.xml.SnapshotNodeXML.readSnapshotNode(reader);
					if(sn==null) {
			        	System.out.println("no snapshot found");
			        	return;
					}
					else {
				        if(sn.getValue().getDescriptorType()!=DescriptorType.SNAPSHOT) {
				        	System.out.println("no snapshot found");
				        	return;
				        }
					}
					System.out.println("parent="+parents.get(parents.size()-1)+" ; sd={id="+sn.getValue().getSnapshotDescriptor().identifiable()+" ; name="+sn.getValue().getSnapshotDescriptor().getDescriptorName()+" ; size="+sn.getValue().getSnapshotDescriptor().getDescriptorSize()+" ; base path="+sn.getValue().getSnapshotDescriptor().getBasePath()+" ; recursion="+sn.getValue().getSnapshotDescriptor().getRecursion()+" ; hashes="+sn.getValue().getSnapshotDescriptor().hashes()+" ; duplicates="+sn.getValue().getDuplicates()+"}");
					st=new SnapshotTree(st, parents.get(parents.size()-1), sn.getValue(), 1, 0);
					parents.add(sn.getValue().identifiable());
					newIDs.put(sn.getKey(), sn.getValue().identifiable());
					state=STATE.OPENDIRECTORY;
					break;
				case OPENDIRECTORY:
					//read directory or file
					sn=jrain.filesystem.snapshot.xml.SnapshotNodeXML.readSnapshotNode(reader);
					if(sn==null) {
			        	System.out.println("no directory/file found");
			        	return;
					}
					else {
				        if(sn.getValue().getDescriptorType()!=DescriptorType.DIRECTORY && sn.getValue().getDescriptorType()!=DescriptorType.FILE) {
				        	System.out.println("no directory/file found");
				        	return;
				        }
					}
					st=new SnapshotTree(st, parents.get(parents.size()-1), sn.getValue(), 1, 0);
					newIDs.put(sn.getKey(), sn.getValue().identifiable());
					if(sn.getValue().getDescriptorType()==DescriptorType.DIRECTORY) {
						System.out.println("parent="+parents.get(parents.size()-1)+" ; dd={id="+sn.getValue().getDirectoryDescriptor().identifiable()+" ; name="+sn.getValue().getDirectoryDescriptor().getDescriptorName()+" ; path="+sn.getValue().getDirectoryDescriptor().getPath()+" ; size="+sn.getValue().getDirectoryDescriptor().getDescriptorSize()+" ; creation date="+sn.getValue().getDirectoryDescriptor().getCreationDateTime()+" ; modification date="+sn.getValue().getDirectoryDescriptor().getLastModificationDateTime()+" ; access date="+sn.getValue().getDirectoryDescriptor().getLastAccessDateTime()+" ; is readable="+sn.getValue().getDirectoryDescriptor().isReadable()+" ; duplicates="+sn.getValue().getDuplicates()+"}");
						parents.add(sn.getValue().identifiable());
					}
					else if(sn.getValue().getDescriptorType()==DescriptorType.FILE) {
						System.out.println("parent="+parents.get(parents.size()-1)+" ; fd={id="+sn.getValue().getFileDescriptor().identifiable()+" ; name="+sn.getValue().getFileDescriptor().getDescriptorName()+" ; path="+sn.getValue().getFileDescriptor().getPath()+" ; size="+sn.getValue().getFileDescriptor().getDescriptorSize()+" ; creation date="+sn.getValue().getFileDescriptor().getCreationDateTime()+" ; modification date="+sn.getValue().getFileDescriptor().getLastModificationDateTime()+" ; access date="+sn.getValue().getFileDescriptor().getLastAccessDateTime()+" ; is readable="+sn.getValue().getFileDescriptor().isReadable()+" ; hashes="+sn.getValue().getFileDescriptor().getHashes()+" ; duplicates="+sn.getValue().getDuplicates()+"}");
					}
					if(reader.isEndElement()) {
						if(reader.getLocalName().equals(jrain.filesystem.snapshot.xml.SnapshotDescriptorXML.XMLSNAPSHOTTAG)) {
							state=STATE.CLOSESNAPSHOT;
						}
						else if(reader.getLocalName().equals(jrain.filesystem.snapshot.xml.DirectoryDescriptorXML.XMLDIRECTORYTAG)) {
							state=STATE.CLOSEDIRECTORY;
						}
					}
					break;
				case CLOSEDIRECTORY:
					parents.remove(parents.size()-1);
					XML.advancePast(reader,jrain.filesystem.snapshot.xml.DirectoryDescriptorXML.XMLDIRECTORYTAG);
					XML.advance(reader);
					if(reader.isEndElement()) {//can close snapshot or directory
						if(reader.getLocalName().equals(jrain.filesystem.snapshot.xml.SnapshotDescriptorXML.XMLSNAPSHOTTAG)) {
							state=STATE.CLOSESNAPSHOT;
						}
						else if(!reader.getLocalName().equals(jrain.filesystem.snapshot.xml.DirectoryDescriptorXML.XMLDIRECTORYTAG)) {
//							state=STATE.CLOSEDIRECTORY;
//						}
//						else {
				        	System.out.println("no closing snapshot found");
				        	return;
						}
					}
					else {//start element
						state=STATE.OPENDIRECTORY;
					}
					break;
				case CLOSESNAPSHOT:
					parents.remove(parents.size()-1);
					XML.advancePast(reader,jrain.filesystem.snapshot.xml.SnapshotDescriptorXML.XMLSNAPSHOTTAG);
					XML.advance(reader);
					if(reader.isEndElement()) {//can close snapshot group only
						if(reader.getLocalName().equals(jrain.filesystem.snapshot.xml.SnapshotsGroupDescriptorXML.XMLSNAPSHOTSGROUPTAG)) {
							state=STATE.CLOSESNAPSHOTSGROUP;
						}
//						else if(!reader.getLocalName().equals(jrain.filesystem.snapshot.immutable.xml.interfaces.DirectoryDescriptorXML_I.XMLDIRECTORYTAG)) {
//							state=STATE.CLOSEDIRECTORY;
//						}
						else {
				        	System.out.println("no closing snapshots group found");
				        	return;
						}
					}
					else {//start element
						state=STATE.OPENSNAPSHOT;
					}
					break;
				case CLOSESNAPSHOTSGROUP:
					state=STATE.FINALIZE;
					break;
				default:
					System.err.println("missing state");
					return;
				}
	        	System.out.println("progress="+p.readByteCount()+"/"+totalSize+"("+((totalSize==0)?0:(p.readByteCount()*100)/(1.0*totalSize))+" %)");
	        }while(reader.hasNext() && state!=STATE.FINALIZE);
	        //update duplicates of all nodes by their new IDs
	        Iterator<TreeNode<SnapshotNode, Identifiable>> it = st.getNodes().iterator();
	        while(it.hasNext()) {
	        	TreeNode<SnapshotNode, Identifiable> n = it.next();
	        	System.out.println("node= "+n);
	        	System.out.println("before="+n.getData().getDuplicates());
	        	updateDuplicates(st, newIDs, n.getData());
	        	System.out.println("after="+st.getNode(n.identifiable()).getData().getDuplicates());
	        }
	    }
	    catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws XMLStreamException, IOException {
//		parse();
		parse2();
	}

}
