package jrain.filesystem.snapshot.xml;

import java.util.Map.Entry;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import jrain.filesystem.snapshot.immutable.BaseSnapshotsDescriptor;
import jrain.xml.XML;
import jrain.xml.identifiable.immutable.IdentifiableXML;
import jrain.identifiable.immutable.Identifiable;
import jrain.entry.mutable.Pair;

/**
 * @author poltergeist0
 *
 * Collection of methods to read and write {@link BaseSnapshotsDescriptor} from/to XML stream
 */
public interface BaseSnapshotsDescriptorXML{
	public static final String XMLNAMETAG=BaseSnapshotsDescriptor.tagName;
	public static final String XMLSIZETAG=BaseSnapshotsDescriptor.tagSize;

	/**
	 * Read the size part of the {@link BaseSnapshotsDescriptor}
	 * 
	 * @param reader is the XML stream reader
	 * @return a {@link Long} with the size or null if a size could not be read
	 * @throws XMLStreamException
	 */
	private static Long readSize(XMLStreamReader reader) throws XMLStreamException {
		//assume that reader is already at a START_ELEMENT
		String s=XML.readSimpleElementText(reader, XMLSIZETAG);
		if(s!=null) {
			return Long.valueOf(s);
		}
		return null;
	}

	/**
	 * Read a {@link BaseSnapshotsDescriptor} from a XML stream
	 * 
	 * @param reader is the XML stream reader
	 * @param type is the object type to use in the {@link Identifiable}
	 * @param uid is an optional UUID to be used
	 * @return an {@link Entry} with the key being the {@link Identifiable} read from the file and the value being the {@link BaseSnapshotsDescriptor} with a new {@link Identifiable}
	 * @throws XMLStreamException
	 */
	public static Entry<Identifiable, BaseSnapshotsDescriptor> readBaseSnapshotsDescriptor(XMLStreamReader reader,String type, Identifiable uid) throws XMLStreamException {
		//assume that reader is already at a START_ELEMENT
		Entry<Identifiable, Identifiable> id=IdentifiableXML.readIdentifiable(reader, type);
		String name=XML.readSimpleElementText(reader, XMLNAMETAG);
		Long size=readSize(reader);
		if(id==null && name==null && size==null) {//none of the tags exist. After one attempt to read all are null
			return null;
		}
		if(id==null || name==null || size==null) { //second attempt to read all fields
			if(id==null) id=IdentifiableXML.readIdentifiable(reader, type);
			if(name==null) name=XML.readSimpleElementText(reader, XMLNAMETAG);
			if(size==null) size=readSize(reader);
			if(id==null || name==null || size==null) {//third attempt to read all fields
				if(id==null) id=IdentifiableXML.readIdentifiable(reader, type);
				if(name==null) name=XML.readSimpleElementText(reader, XMLNAMETAG);
				if(size==null) size=readSize(reader);
				if(id==null || name==null || size==null) {//at least one of the tags does not exist. After 3 (number of fields) attempts to read all should be not null
					return null;
				}
			}
		}
		//read successful
		return new Pair<>(id.getKey(), new BaseSnapshotsDescriptor(((uid==null)?id.getValue().identifier():uid.identifier()), name, size));
	}
	
	/**
	 * Write a {@link BaseSnapshotsDescriptor} to a XML stream
	 * 
	 * @param out is the stream to write the XML
	 * @param id is the {@link BaseSnapshotsDescriptor}
	 * @param tabbing is the amount of TAB characters for text alignment
	 * @return true (may be changed in the future)
	 * @throws XMLStreamException
	 */
	public static boolean writeBaseSnapshotsDescriptor(XMLStreamWriter out, BaseSnapshotsDescriptor id, String tabbing) throws XMLStreamException {
		IdentifiableXML.writeIdentifiable(out, id,tabbing);
		XML.writeSimpleElementText(out, XMLNAMETAG, id.getDescriptorName(),tabbing);
		XML.writeSimpleElementText(out, XMLSIZETAG, String.valueOf(id.getDescriptorSize()),tabbing);
		return true;
	}
}
