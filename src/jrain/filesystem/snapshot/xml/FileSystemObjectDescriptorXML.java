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

import jrain.xml.XML;
import jrain.identifiable.immutable.Identifiable;
import jrain.entry.mutable.Pair;
import jrain.filesystem.snapshot.immutable.BaseDescriptor;
import jrain.filesystem.snapshot.immutable.FileSystemObjectDescriptor;

/**
 * @author poltergeist0
 *
 * Collection of methods to read and write {@link FileSystemObjectDescriptor} from/to XML stream
 */
public interface FileSystemObjectDescriptorXML{
	public static final String XMLOBJECTTAG=FileSystemObjectDescriptor.tagObject;
	public static final String XMLPATHTAG=FileSystemObjectDescriptor.tagPath;

	/**
	 * Read the path part of the {@link DirectoryDescriptor}
	 * 
	 * @param reader is the XML stream reader
	 * @return a {@link String} containing the value or null if it could not be read
	 * @throws XMLStreamException
	 */
	static String readPath(XMLStreamReader reader) throws XMLStreamException {
		//assume that reader is already at a START_ELEMENT
		String s=XML.readSimpleElementText(reader, XMLPATHTAG);
		if(s!=null) {
			return s;
		}
		return null;
	}

	/**
	 * Construct a {@link FileSystemObjectDescriptor} from the individual fields and some extra information.
	 * 
	 * @param uid is an optional UUID to be used
	 * @param name is the name of the object
	 * @param size is the sum of the sizes of all sub objects in the object
	 * @return an {@link Entry} with the key being the {@link Identifiable} read from the file and the value being the {@link BaseDescriptor} with a new {@link Identifiable}
	 */
	public static Entry<Identifiable, FileSystemObjectDescriptor> getFileSystemObjectDescriptor(Identifiable uid,Entry<Identifiable, Identifiable> id, String name,Long size,String path) {
		return new Pair<>(id.getKey(), new FileSystemObjectDescriptor(((uid==null)?id.getValue().identifier():uid.identifier()), name, size, path));
	}
	
	/**
	 * Read a {@link FileSystemObjectDescriptor} from a XML stream
	 * 
	 * @param reader is the XML stream reader
	 * @param type is the object type to use in the {@link Identifiable}
	 * @param uid is an optional UUID to be used
	 * @return an {@link Entry} with the key being the {@link Identifiable} read from the file and the value being the {@link FileSystemObjectDescriptor} with a new {@link Identifiable}
	 * @throws XMLStreamException
	 */
	public static Entry<Identifiable, FileSystemObjectDescriptor> readFileSystemObjectDescriptor(XMLStreamReader reader,String type, Identifiable uid) throws XMLStreamException {
		//assume that reader is already at a START_ELEMENT
		Entry<Identifiable, Identifiable> id=null;
		String name=null;
		Long size=null;
		String path=null;
		int tries=4;//number of fields. Worst case. Assumes fields are out of order and only one per cycle is found
		while(id==null || name==null || size==null || path==null) {//at least one of the fields has not been read
			if(id==null) id=BaseDescriptorXML.readID(reader, type);
			if(name==null) name=BaseDescriptorXML.readName(reader);
			if(size==null) size=BaseDescriptorXML.readSize(reader);
			if(path==null) path=readPath(reader);
			if(tries<=0) {//at least one of the tags does not exist
				return null;
			}
			--tries;
		}
		//read successful
		return getFileSystemObjectDescriptor(uid,id, name, size,path);
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
	public static boolean writeFileSystemObjectDescriptor(XMLStreamWriter out, FileSystemObjectDescriptor id, String tabbing) throws XMLStreamException {
		BaseDescriptorXML.writeBaseSnapshotsDescriptor(out, id, tabbing);
		XML.writeSimpleElementText(out, XMLPATHTAG, id.getPath(),tabbing);
		return true;
	}
}
