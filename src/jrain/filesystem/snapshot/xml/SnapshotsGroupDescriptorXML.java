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

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import jrain.filesystem.snapshot.immutable.BaseDescriptor;
import jrain.filesystem.snapshot.immutable.SnapshotsGroupDescriptor;
import jrain.xml.XML;
import jrain.identifiable.immutable.Identifiable;
import jrain.entry.mutable.Pair;

/**
 * @author poltergeist0
 *
 * Collection of methods to read and write {@link SnapshotsGroupDescriptor} from/to XML stream
 */
public interface SnapshotsGroupDescriptorXML {
	public static final String XMLSNAPSHOTSGROUPTAG=SnapshotsGroupDescriptor.tagSnapshotsGroup;
//	public static final String XMLSNAPSHOTSGROUPFILENAMETAG=SnapshotsGroupDescriptor.tagSnapshotsGroupFileName;
	
	/**
	 * Read the file name part of the {@link SnapshotsGroupDescriptor}
	 * 
	 * @param reader is the XML stream reader
	 * @return a {@link String} with the file name or null if it could not be read
	 * @throws XMLStreamException
	 */
//	private static String readFileName(XMLStreamReader reader) throws XMLStreamException {
//		//assume that reader is already at a START_ELEMENT
//		String s=XML.readSimpleElementText(reader, XMLSNAPSHOTSGROUPFILENAMETAG);
//		if(s!=null) {
//			return s;
//		}
//		return null;
//	}

	/**
	 * Construct a {@link SnapshotsGroupDescriptor} from the individual fields and some extra information.
	 * 
	 * @param uid is an optional UUID to be used
	 * @param name is the name of the object
	 * @param size is the sum of the sizes of all sub objects in the object
	 * @return an {@link Entry} with the key being the {@link Identifiable} read from the file and the value being the {@link BaseDescriptor} with a new {@link Identifiable}
	 */
	public static Entry<Identifiable, SnapshotsGroupDescriptor> getFileSystemObjectDescriptor(Identifiable uid,Entry<Identifiable, Identifiable> id, String name,Long size,String path) {
		return new Pair<>(id.getKey(), new SnapshotsGroupDescriptor(((uid==null)?id.getValue().identifier():uid.identifier()), name, path, size));
	}
	
	/**
	 * Read a {@link SnapshotsGroupDescriptor} from a XML stream
	 * 
	 * @param reader is the XML stream reader
	 * @param uid is an optional UUID to be used
	 * @return an {@link Entry} with the key being the {@link Identifiable} read from the file and the value being the {@link SnapshotsGroupDescriptor} with a new {@link Identifiable}
	 * @throws XMLStreamException
	 */
	public static Entry<jrain.identifiable.immutable.Identifiable, SnapshotsGroupDescriptor> readSnapshotsGroupDescriptor(XMLStreamReader reader, jrain.identifiable.immutable.Identifiable uid) throws XMLStreamException {
		//assume that reader is already at a START_ELEMENT
		String s=reader.getLocalName();
		if(!s.equals(XMLSNAPSHOTSGROUPTAG)) return null;//not a directory
		XML.advance(reader);
		Entry<jrain.identifiable.immutable.Identifiable, jrain.identifiable.immutable.Identifiable> id=null;
		String name=null;
		Long size=null;
		String path=null;
		int tries=4;//number of fields
		while(id==null || name==null || size==null || path==null) {
			if(id==null) id=BaseDescriptorXML.readID(reader, jrain.identifiable.Identifiable.objectTypeOf(SnapshotsGroupDescriptor.class));
			if(name==null) name=BaseDescriptorXML.readName(reader);
			if(size==null) size=BaseDescriptorXML.readSize(reader);
			if(path==null) path=FileSystemObjectDescriptorXML.readPath(reader);
			if(tries<=0) {//none of the tags exist. After one attempt to read all are null
				return null;
			}
			--tries;
		}
		return getFileSystemObjectDescriptor(uid, id, name, size,path);
	}

	/**
	 * Write a {@link SnapshotsGroupDescriptor} to a XML stream
	 * 
	 * @param out is the stream to write the XML
	 * @param id is the {@link SnapshotsGroupDescriptor}
	 * @param tabbing is the amount of TAB characters for text alignment
	 * @return true (may be changed in the future)
	 * @throws XMLStreamException
	 */
	public static boolean writeSnapshotsGroupDescriptor(XMLStreamWriter out, SnapshotsGroupDescriptor id, String tabbing) throws XMLStreamException {
		out.writeCharacters(tabbing);
		out.writeStartElement(XMLSNAPSHOTSGROUPTAG);
		out.writeCharacters("\n");
		FileSystemObjectDescriptorXML.writeFileSystemObjectDescriptor(out, id,tabbing+"\t");
//		XML.writeSimpleElementText(out, XMLSNAPSHOTSGROUPFILENAMETAG, id.getFilename(),tabbing+"\t");
		return true;
	}
}
