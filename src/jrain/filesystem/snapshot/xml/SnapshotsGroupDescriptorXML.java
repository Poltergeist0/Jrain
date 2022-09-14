package jrain.filesystem.snapshot.xml;

import java.util.Map.Entry;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import jrain.filesystem.snapshot.immutable.BaseSnapshotsDescriptor;
import jrain.filesystem.snapshot.immutable.SnapshotsGroupDescriptor;
import jrain.xml.XML;
import jrain.identifiable.Identifiable;
import jrain.entry.mutable.Pair;

/**
 * @author poltergeist0
 *
 * Collection of methods to read and write {@link SnapshotsGroupDescriptor} from/to XML stream
 */
public interface SnapshotsGroupDescriptorXML {
	public static final String XMLSNAPSHOTSGROUPTAG=SnapshotsGroupDescriptor.tagSnapshotsGroup;
	public static final String XMLSNAPSHOTSGROUPFILENAMETAG=SnapshotsGroupDescriptor.tagSnapshotsGroupFileName;
	
	/**
	 * Read the file name part of the {@link SnapshotsGroupDescriptor}
	 * 
	 * @param reader is the XML stream reader
	 * @return a {@link String} with the file name or null if it could not be read
	 * @throws XMLStreamException
	 */
	private static String readFileName(XMLStreamReader reader) throws XMLStreamException {
		//assume that reader is already at a START_ELEMENT
		String s=XML.readSimpleElementText(reader, XMLSNAPSHOTSGROUPFILENAMETAG);
		if(s!=null) {
			return s;
		}
		return null;
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
		Entry<jrain.identifiable.immutable.Identifiable, BaseSnapshotsDescriptor> base=null;
		String filename=null;
		int tries=2;//number of fields
		while(base==null || filename==null) {
			if(base==null) base=BaseSnapshotsDescriptorXML.readBaseSnapshotsDescriptor(reader, Identifiable.objectTypeOf(SnapshotsGroupDescriptor.class),uid);
			if(filename==null) filename=readFileName(reader);
			if(tries<=0 && base==null && filename==null) {//none of the tags exist. After one attempt to read all are null
				return null;
			}
			--tries;
		}
		return new Pair<>(base.getKey(), new SnapshotsGroupDescriptor(base.getValue().identifier(), base.getValue().getDescriptorName(),filename, base.getValue().getDescriptorSize()));
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
		BaseSnapshotsDescriptorXML.writeBaseSnapshotsDescriptor(out, id,tabbing+"\t");
		XML.writeSimpleElementText(out, XMLSNAPSHOTSGROUPFILENAMETAG, id.getFilename(),tabbing+"\t");
		return true;
	}
}
