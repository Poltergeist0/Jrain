package jrain.filesystem.snapshot.xml;

import java.util.Map.Entry;
import java.util.Set;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import jrain.filesystem.snapshot.immutable.BaseSnapshotsDescriptor;
import jrain.filesystem.snapshot.immutable.SnapshotDescriptor;
import jrain.xml.XML;
import jrain.identifiable.Identifiable;
import jrain.entry.mutable.Pair;

/**
 * @author poltergeist0
 *
 * Collection of methods to read and write {@link SnapshotsDescriptor} from/to XML stream
 */
public interface SnapshotDescriptorXML{
	public static final String XMLSNAPSHOTTAG=SnapshotDescriptor.tagSnapshot;
	public static final String XMLSNAPSHOTBASEPATHTAG=SnapshotDescriptor.tagSnapshotBasePath;
	public static final String XMLSNAPSHOTRECURSIONTAG=SnapshotDescriptor.tagSnapshotRecursion;
	
	/**
	 * Read the base path part of the {@link SnapshotsDescriptor}
	 * 
	 * @param reader is the XML stream reader
	 * @return a {@link String} with the base path or null if it could not be read
	 * @throws XMLStreamException
	 */
	private static String readBasePath(XMLStreamReader reader) throws XMLStreamException {
		//assume that reader is already at a START_ELEMENT
		String s=XML.readSimpleElementText(reader, XMLSNAPSHOTBASEPATHTAG);
		if(s!=null) {
			return s;
		}
		return null;
	}

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
		Entry<jrain.identifiable.immutable.Identifiable, BaseSnapshotsDescriptor> base=null;
		String basePath=null;
		Set<String> hashes=null;
		Integer recursion=null;
		int tries=4;//number of fields
		while(base==null || basePath==null || hashes==null || recursion==null) {
			if(base==null) base=BaseSnapshotsDescriptorXML.readBaseSnapshotsDescriptor(reader, Identifiable.objectTypeOf(SnapshotDescriptor.class),uid);
			if(basePath==null) basePath=readBasePath(reader);
			if(hashes==null) hashes=HashesXML.readHashesToCalculate(reader);
			if(recursion==null) recursion=readRecursion(reader);
			if(tries<=0) {
				return null;
			}
			--tries;
		}
		return new Pair<>(base.getKey(), new SnapshotDescriptor(base.getValue().identifier(), base.getValue().getDescriptorName(),basePath,hashes,recursion,null,null, base.getValue().getDescriptorSize()));
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
		BaseSnapshotsDescriptorXML.writeBaseSnapshotsDescriptor(out, id,tabbing+"\t");
		XML.writeSimpleElementText(out, XMLSNAPSHOTBASEPATHTAG, id.getBasePath(),tabbing+"\t");
		HashesXML.writeHashesToCalculate(out, id.hashes(),tabbing+"\t");
		XML.writeSimpleElementText(out, XMLSNAPSHOTRECURSIONTAG, String.valueOf(id.getRecursion()),tabbing+"\t");
		return true;
	}
}
