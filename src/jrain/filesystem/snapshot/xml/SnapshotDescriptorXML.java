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
package jrain.filesystem.snapshot.xml;

import java.util.Map.Entry;
import java.util.Set;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import jrain.filesystem.snapshot.immutable.DirectoryDescriptor;
import jrain.filesystem.snapshot.immutable.SnapshotDescriptor;
import jrain.xml.XML;
import jrain.identifiable.immutable.Identifiable;
import jrain.entry.mutable.Pair;

/**
 * @author poltergeist0
 *
 * Collection of methods to read and write {@link SnapshotsDescriptor} from/to XML stream
 */
public interface SnapshotDescriptorXML{
	public static final String XMLSNAPSHOTTAG=SnapshotDescriptor.tagSnapshot;
//	public static final String XMLSNAPSHOTBASEPATHTAG=SnapshotDescriptor.tagSnapshotBasePath;
	public static final String XMLSNAPSHOTRECURSIONTAG=SnapshotDescriptor.tagSnapshotRecursion;
	
	/**
	 * Read the base path part of the {@link SnapshotsDescriptor}
	 * 
	 * @param reader is the XML stream reader
	 * @return a {@link String} with the base path or null if it could not be read
	 * @throws XMLStreamException
	 */
//	private static String readBasePath(XMLStreamReader reader) throws XMLStreamException {
//		//assume that reader is already at a START_ELEMENT
//		String s=XML.readSimpleElementText(reader, XMLSNAPSHOTBASEPATHTAG);
//		if(s!=null) {
//			return s;
//		}
//		return null;
//	}

	/**
	 * Read the recursion part of the {@link SnapshotsDescriptor}
	 * 
	 * @param reader is the XML stream reader
	 * @return a {@link Integer} with the recursion or null if it could not be read
	 * @throws XMLStreamException
	 */
	private static Integer readRecursion(XMLStreamReader reader) throws XMLStreamException {
		//assume that reader is already at a START_ELEMENT
		String s=XML.readSimpleElementText(reader, XMLSNAPSHOTRECURSIONTAG);
		if(s!=null) {
			return Integer.valueOf(s);
		}
		return null;
	}

	/**
	 * Construct a {@link DirectoryDescriptor} from the individual fields and some extra information.
	 * 
	 * @param uid is an optional UUID to be used
	 * @param name is the name of the object
	 * @param size is the sum of the sizes of all sub objects in the object
	 * @return an {@link Entry} with the key being the {@link Identifiable} read from the file and the value being the {@link DirectoryDescriptor} with a new {@link Identifiable}
	 */
	public static Entry<Identifiable, SnapshotDescriptor> getSnapshotDescriptor(Identifiable uid,Entry<Identifiable, Identifiable> id, String name,Long size,String path,Set<String> hashes,	Integer recursion) {
		return new Pair<>(id.getKey(), new SnapshotDescriptor(((uid==null)?id.getValue().identifier():uid.identifier()), name, path,hashes,recursion,null,null,size));
	}
	
	/**
	 * Read a {@link SnapshotsDescriptor} from a XML stream
	 * 
	 * @param reader is the XML stream reader
	 * @param uid is an optional UUID to be used
	 * @return an {@link Entry} with the key being the {@link Identifiable} read from the file and the value being the {@link SnapshotsDescriptor} with a new {@link Identifiable}
	 * @throws XMLStreamException
	 */
	public static Entry<jrain.identifiable.immutable.Identifiable, SnapshotDescriptor> readSnapshotDescriptor(XMLStreamReader reader, jrain.identifiable.immutable.Identifiable uid) throws XMLStreamException {
		//assume that reader is already at a START_ELEMENT
		String s=reader.getLocalName();
		if(!s.equals(XMLSNAPSHOTTAG)) return null;//not a directory
		XML.advance(reader);
//		Entry<jrain.identifiable.immutable.Identifiable, FileSystemObjectDescriptor> base=null;
		Entry<jrain.identifiable.immutable.Identifiable, jrain.identifiable.immutable.Identifiable> id=null;
		String name=null;
		Long size=null;
		String path=null;
//		LocalDateTime creation=null;
//		LocalDateTime modification=null;
//		LocalDateTime access=null;
//		Boolean readable=null;
		Set<String> hashes=null;
		Integer recursion=null;
		int tries=10;//number of fields
		while(id==null || name==null || size==null || path==null || hashes==null || recursion==null) {
			if(id==null) id=BaseDescriptorXML.readID(reader,jrain.identifiable.Identifiable.objectTypeOf(SnapshotDescriptor.class));
			if(name==null) name=BaseDescriptorXML.readName(reader);
			if(size==null) size=BaseDescriptorXML.readSize(reader);
			if(path==null) path=FileSystemObjectDescriptorXML.readPath(reader);
//			if(creation==null) creation=DirectoryDescriptorXML.readCreation(reader);
//			if(modification==null) modification=DirectoryDescriptorXML.readModification(reader);
//			if(access==null) access=DirectoryDescriptorXML.readAccess(reader);
//			if(readable==null) readable=DirectoryDescriptorXML.readReadableFlag(reader);
			if(hashes==null) hashes=HashesXML.readHashesToCalculate(reader);
			if(recursion==null) recursion=readRecursion(reader);
			if(tries<=0) {
				return null;
			}
			--tries;
		}
		return getSnapshotDescriptor(uid,id, name, size,path,hashes,recursion);
	}

	/**
	 * Write a {@link SnapshotsDescriptor} to a XML stream
	 * 
	 * @param out is the stream to write the XML
	 * @param id is the {@link SnapshotsDescriptor}
	 * @param tabbing is the amount of TAB characters for text alignment
	 * @return true (may be changed in the future)
	 * @throws XMLStreamException
	 */
	public static boolean writeSnapshotDescriptor(XMLStreamWriter out, SnapshotDescriptor id, String tabbing) throws XMLStreamException {
		out.writeCharacters(tabbing);
		out.writeStartElement(XMLSNAPSHOTTAG);
		out.writeCharacters("\n");
		FileSystemObjectDescriptorXML.writeFileSystemObjectDescriptor(out, id,tabbing+"\t");
//		XML.writeSimpleElementText(out, XMLSNAPSHOTBASEPATHTAG, id.getBasePath(),tabbing+"\t");
		HashesXML.writeHashesToCalculate(out, id.hashes(),tabbing+"\t");
		XML.writeSimpleElementText(out, XMLSNAPSHOTRECURSIONTAG, String.valueOf(id.getRecursion()),tabbing+"\t");
		return true;
	}
}
