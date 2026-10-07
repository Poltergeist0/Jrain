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
import jrain.xml.XML;
import jrain.xml.identifiable.immutable.IdentifiableXML;
import jrain.identifiable.immutable.Identifiable;
import jrain.entry.mutable.Pair;

/**
 * @author poltergeist0
 *
 * Collection of methods to read and write {@link FileSystemObjectDescriptor} from/to XML stream
 * All fields must exist.
 */
public interface BaseDescriptorXML{
	public static final String XMLOBJECTTAG=BaseDescriptor.tagBase;
	public static final String XMLNAMETAG=BaseDescriptor.tagName;
	public static final String XMLSIZETAG=BaseDescriptor.tagSize;

	/**
	 * Read the ID part of the {@link BaseDescriptor}
	 * 
	 * @param reader is the XML stream reader
	 * @param type is the object type to use in the {@link Identifiable}
	 * @return a {@link Entry<Identifiable, Identifiable>} with the ID
	 * @throws XMLStreamException
	 */
	public static Entry<Identifiable, Identifiable> readID(XMLStreamReader reader,String type) throws XMLStreamException {
		//assume that reader is already at a START_ELEMENT
		Entry<Identifiable, Identifiable> id=IdentifiableXML.readIdentifiable(reader, type);
		return id;
	}

	/**
	 * Read the name part of the {@link BaseDescriptor}
	 * 
	 * @param reader is the XML stream reader
	 * @return a {@link Long} with the size or null if a size could not be read
	 * @throws XMLStreamException
	 */
	static String readName(XMLStreamReader reader) throws XMLStreamException {
		//assume that reader is already at a START_ELEMENT
		String s=XML.readSimpleElementText(reader, XMLNAMETAG);
		return s;
	}

	/**
	 * Read the size part of the {@link BaseDescriptor}
	 * 
	 * @param reader is the XML stream reader
	 * @return a {@link Long} with the size or null if a size could not be read
	 * @throws XMLStreamException
	 */
	static Long readSize(XMLStreamReader reader) throws XMLStreamException {
		//assume that reader is already at a START_ELEMENT
		String s=XML.readSimpleElementText(reader, XMLSIZETAG);
		if(s!=null) {
			return Long.valueOf(s);
		}
		return null;
	}

	/**
	 * Construct a {@link BaseDescriptor} from the individual fields and some extra information.
	 * 
	 * @param uid is an optional UUID to be used
	 * @param name is the name of the object
	 * @param size is the sum of the sizes of all sub objects in the object
	 * @return an {@link Entry} with the key being the {@link Identifiable} read from the file and the value being the {@link BaseDescriptor} with a new {@link Identifiable}
	 */
	public static Entry<Identifiable, BaseDescriptor> getBaseSnapshotsDescriptor(Identifiable uid,Entry<Identifiable, Identifiable> id, String name,Long size) {
		return new Pair<>(id.getKey(), new BaseDescriptor(((uid==null)?id.getValue().identifier():uid.identifier()), name, size));
	}
	
	/**
	 * Read a {@link BaseDescriptor} from a XML stream
	 * 
	 * @param reader is the XML stream reader
	 * @param type is the object type to use in the {@link Identifiable}
	 * @param uid is an optional UUID to be used
	 * @return an {@link Entry} with the key being the {@link Identifiable} read from the file and the value being the {@link BaseDescriptor} with a new {@link Identifiable} or NULL if any of the fields can not be read.
	 * @throws XMLStreamException
	 */
	public static Entry<Identifiable, BaseDescriptor> readBaseSnapshotsDescriptor(XMLStreamReader reader,String type, Identifiable uid) throws XMLStreamException {
		//assume that reader is already at a START_ELEMENT
		Entry<Identifiable, Identifiable> id=null;
		String name=null;
		Long size=null;
		int tries=3;//number of fields. Worst case. Assumes fields are out of order and only one per cycle is found
//		if(id==null && name==null && size==null) {//none of the tags exist. After one attempt to read all are null
//			return null;
//		}
		while(id==null || name==null || size==null) {//at least one of the fields has not been read
			if(id==null) id=readID(reader, type);
			if(name==null) name=readName(reader);
			if(size==null) size=readSize(reader);
			if(tries<=0) {//at least one of the tags does not exist
				return null;
			}
			--tries;
		}
		//read successful
		return getBaseSnapshotsDescriptor(uid,id, name, size);
	}
	
	/**
	 * Write a {@link FileSystemObjectDescriptor} to a XML stream
	 * 
	 * @param out is the stream to write the XML
	 * @param id is the {@link FileSystemObjectDescriptor}
	 * @param tabbing is the amount of TAB characters for text alignment
	 * @return true (may be changed in the future)
	 * @throws XMLStreamException
	 */
	public static boolean writeBaseSnapshotsDescriptor(XMLStreamWriter out, BaseDescriptor id, String tabbing) throws XMLStreamException {
		IdentifiableXML.writeIdentifiable(out, id,tabbing);
		XML.writeSimpleElementText(out, XMLNAMETAG, id.getDescriptorName(),tabbing);
		XML.writeSimpleElementText(out, XMLSIZETAG, String.valueOf(id.getDescriptorSize()),tabbing);
		return true;
	}
}
