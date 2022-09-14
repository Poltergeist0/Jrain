package jrain.filesystem.snapshot.xml;

import java.time.LocalDateTime;
import java.util.Map.Entry;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import jrain.filesystem.snapshot.immutable.BaseSnapshotsDescriptor;
import jrain.filesystem.snapshot.immutable.DirectoryDescriptor;
import jrain.xml.XML;
import jrain.identifiable.Identifiable;
import jrain.entry.mutable.Pair;

/**
 * @author poltergeist0
 *
 * Collection of methods to read and write {@link DirectoryDescriptor} from/to XML stream
 */
public interface DirectoryDescriptorXML{
	public static final String XMLDIRECTORYTAG=DirectoryDescriptor.tagDirectory;
	public static final String XMLDIRECTORYPATHTAG=DirectoryDescriptor.tagPath;
	public static final String XMLDIRECTORYREADABLETAG=DirectoryDescriptor.tagReadability;
	public static final String XMLFILECREATIONTAG=DirectoryDescriptor.tagCreationDateTime;
	public static final String XMLFILEMODIFICATIONTAG=DirectoryDescriptor.tagModificationDateTime;
	public static final String XMLFILEACCESSTAG=DirectoryDescriptor.tagAccessDateTime;

	/**
	 * Read the path part of the {@link DirectoryDescriptor}
	 * 
	 * @param reader is the XML stream reader
	 * @return a {@link String} containing the value or null if it could not be read
	 * @throws XMLStreamException
	 */
	static String readPath(XMLStreamReader reader) throws XMLStreamException {
		//assume that reader is already at a START_ELEMENT
		String s=XML.readSimpleElementText(reader, XMLDIRECTORYPATHTAG);
		if(s!=null) {
			return s;
		}
		return null;
	}

	/**
	 * Read the readability part of the {@link DirectoryDescriptor}
	 * 
	 * @param reader is the XML stream reader
	 * @return a {@link Boolean} containing the value or null if it could not be read
	 * @throws XMLStreamException
	 */
	static Boolean readReadableFlag(XMLStreamReader reader) throws XMLStreamException {
		//assume that reader is already at a START_ELEMENT
		String s=XML.readSimpleElementText(reader, XMLDIRECTORYREADABLETAG);
		if(s!=null) {
			return Boolean.parseBoolean(s);
		}
		return null;
	}

	/**
	 * Read the creation date and time part of the {@link DirectoryDescriptor}
	 * 
	 * @param reader is the XML stream reader
	 * @return a {@link LocalDateTime} containing the value or null if it could not be read
	 * @throws XMLStreamException
	 */
	static LocalDateTime readCreation(XMLStreamReader reader) throws XMLStreamException {
		//assume that reader is already at a START_ELEMENT
		String s=XML.readSimpleElementText(reader, XMLFILECREATIONTAG);
		if(s!=null) {
			return LocalDateTime.parse(s);
		}
		return null;
	}

	/**
	 * Read the last modification date and time part of the {@link DirectoryDescriptor}
	 * 
	 * @param reader is the XML stream reader
	 * @return a {@link LocalDateTime} containing the value or null if it could not be read
	 * @throws XMLStreamException
	 */
	static LocalDateTime readModification(XMLStreamReader reader) throws XMLStreamException {
		//assume that reader is already at a START_ELEMENT
		String s=XML.readSimpleElementText(reader, XMLFILEMODIFICATIONTAG);
		if(s!=null) {
			return LocalDateTime.parse(s);
		}
		return null;
	}

	/**
	 * Read the last access date and time part of the {@link DirectoryDescriptor}
	 * 
	 * @param reader is the XML stream reader
	 * @return a {@link LocalDateTime} containing the value or null if it could not be read
	 * @throws XMLStreamException
	 */
	static LocalDateTime readAccess(XMLStreamReader reader) throws XMLStreamException {
		//assume that reader is already at a START_ELEMENT
		String s=XML.readSimpleElementText(reader, XMLFILEACCESSTAG);
		if(s!=null) {
			return LocalDateTime.parse(s);
		}
		return null;
	}

	/**
	 * Read the data of a {@link DirectoryDescriptor}.
	 * It is the {@link DirectoryDescriptor} without the outer tags.
	 * 
	 * Package visible so that it can be used for file descriptor.
	 * 
	 * @param reader is the XML stream reader
	 * @param type is the object type to use in the {@link Identifiable}
	 * @param uid is an optional UUID to be used
	 * @return an {@link Entry} with the key being the {@link Identifiable} read from the file and the value being the {@link DirectoryDescriptor} with a new {@link Identifiable}
	 * @throws XMLStreamException
	 */
	static Entry<jrain.identifiable.immutable.Identifiable, DirectoryDescriptor> readDirectoryDescriptorData(XMLStreamReader reader,String type, jrain.identifiable.immutable.Identifiable uid) throws XMLStreamException {
		//assume that reader is already at a START_ELEMENT
		Entry<jrain.identifiable.immutable.Identifiable, BaseSnapshotsDescriptor> base=null;
		String path=null;
		LocalDateTime creation=null;
		LocalDateTime modification=null;
		LocalDateTime access=null;
		Boolean readable=null;
		int tries=6;//number of fields. Worst case. Assumes fields are out of order and only one per cycle is found
		while(base==null || path==null || creation==null || modification==null || access==null || readable==null) {
			if(base==null) base=BaseSnapshotsDescriptorXML.readBaseSnapshotsDescriptor(reader, (type!=null)?type:Identifiable.objectTypeOf(DirectoryDescriptor.class),uid);
			if(path==null) path=readPath(reader);
			if(creation==null) creation=readCreation(reader);
			if(modification==null) modification=readModification(reader);
			if(access==null) access=readAccess(reader);
			if(readable==null) readable=readReadableFlag(reader);
			if(tries<=0) {//at least one of the tags does not exist
				return null;
			}
			--tries;
		}
		return new Pair<>(base.getKey(), new DirectoryDescriptor(base.getValue().identifier(), base.getValue().getDescriptorName(),path, base.getValue().getDescriptorSize(),creation,modification,access,readable));
	}

	/**
	 * Read a {@link DirectoryDescriptor}.
	 * 
	 * @param reader is the XML stream reader
	 * @param uid is an optional UUID to be used
	 * @return an {@link Entry} with the key being the {@link Identifiable} read from the file and the value being the {@link DirectoryDescriptor} with a new {@link Identifiable}
	 * @throws XMLStreamException
	 */
	public static Entry<jrain.identifiable.immutable.Identifiable, DirectoryDescriptor> readDirectoryDescriptor(XMLStreamReader reader, jrain.identifiable.immutable.Identifiable uid) throws XMLStreamException {
		//assume that reader is already at a START_ELEMENT
		String s=reader.getLocalName();
		if(!s.equals(XMLDIRECTORYTAG)) return null;//not a directory
		XML.advance(reader);
		return readDirectoryDescriptorData(reader, Identifiable.objectTypeOf(DirectoryDescriptor.class),uid);
	}

	/**
	 * Write the data of a {@link DirectoryDescriptor}.
	 * It is the {@link DirectoryDescriptor} without the outer tags.
	 * 
	 * Package visible so that it can be used for file descriptor.
	 * 
	 * @param out is the stream to write the XML
	 * @param id is the {@link DirectoryDescriptor}
	 * @param tabbing is the amount of TAB characters for text alignment
	 * @return true (may be changed in the future)
	 * @throws XMLStreamException
	 */
	static boolean writeDirectoryDescriptorData(XMLStreamWriter out, DirectoryDescriptor id, String tabbing) throws XMLStreamException {
		BaseSnapshotsDescriptorXML.writeBaseSnapshotsDescriptor(out, id,tabbing);
		XML.writeSimpleElementText(out, XMLDIRECTORYPATHTAG, id.getPath(),tabbing);
		XML.writeSimpleElementText(out, XMLFILECREATIONTAG, id.getCreationDateTime().toString(),tabbing);
		XML.writeSimpleElementText(out, XMLFILEMODIFICATIONTAG, id.getLastModificationDateTime().toString(),tabbing);
		XML.writeSimpleElementText(out, XMLFILEACCESSTAG, id.getLastAccessDateTime().toString(),tabbing);
		XML.writeSimpleElementText(out, XMLDIRECTORYREADABLETAG, String.valueOf(id.isReadable()),tabbing);
		return true;
	}

	/**
	 * Write a {@link DirectoryDescriptor}.
	 * 
	 * @param out is the stream to write the XML
	 * @param id is the {@link DirectoryDescriptor}
	 * @param tabbing is the amount of TAB characters for text alignment
	 * @return true (may be changed in the future)
	 * @throws XMLStreamException
	 */
	public static boolean writeDirectoryDescriptor(XMLStreamWriter out, DirectoryDescriptor id, String tabbing) throws XMLStreamException {
		out.writeCharacters(tabbing);
		out.writeStartElement(XMLDIRECTORYTAG);
		out.writeCharacters("\n");
		writeDirectoryDescriptorData(out, id,tabbing+"\t");
		return true;
	}
}
