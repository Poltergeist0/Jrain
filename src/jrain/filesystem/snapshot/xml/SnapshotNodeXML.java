package jrain.filesystem.snapshot.xml;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import jrain.filesystem.snapshot.immutable.DirectoryDescriptor;
import jrain.filesystem.snapshot.immutable.FileDescriptor;
import jrain.filesystem.snapshot.immutable.SnapshotDescriptor;
import jrain.filesystem.snapshot.immutable.SnapshotsGroupDescriptor;
import jrain.filesystem.snapshot.snapshotTree.immutable.SnapshotNode;
import jrain.xml.XML;
import jrain.identifiable.immutable.Identifiable;
import jrain.entry.mutable.Pair;

/**
 * @author poltergeist0
 *
 * Collection of methods to read and write {@link SnapshotNode} from/to XML stream
 */
public interface SnapshotNodeXML {
	public static final String _xmlDuplicatesTag="duplicates";
	public static final String _xmlDuplicateTag="duplicate";

	/**
	 * Read the duplicates part of the {@link SnapshotNode}
	 * 
	 * @param reader is the XML stream reader
	 * @return a {@link Set} with the {@link Identifiable} of the duplicates
	 * @throws XMLStreamException
	 */
	private static Set<Identifiable> readDuplicates(XMLStreamReader reader,String objType) throws XMLStreamException{
		//assume that reader is already at a START_ELEMENT
		String s=reader.getLocalName();
		if(!s.equals(_xmlDuplicatesTag)) return null;//not a directory
		XML.advance(reader);
		HashSet<Identifiable> hashes=new HashSet<>();
		s=XML.readSimpleElementText(reader, _xmlDuplicateTag);
		while(s!=null) {
//			hashes.add(new Identifiable(s));
			hashes.add(new Identifiable(jrain.identifiable.Identifiable.build(objType,s)));
			s=XML.readSimpleElementText(reader, _xmlDuplicateTag);
		}
		XML.advancePast(reader, _xmlDuplicatesTag);
		XML.advance(reader);
		return hashes;
	}
	
	/**
	 * Read a {@link SnapshotNode} from a XML stream
	 * 
	 * @param reader is the XML stream reader
	 * @param uid is an optional UUID to be used
	 * @return an {@link Entry} with the key being the {@link Identifiable} read from the file and the value being the {@link SnapshotNode} with a new {@link Identifiable}
	 * @throws XMLStreamException
	 */
	public static Entry<Identifiable, SnapshotNode> readSnapshotNode(XMLStreamReader reader, Identifiable uid) throws XMLStreamException {
		//assume that reader is already at a START_ELEMENT
		Entry<Identifiable, SnapshotsGroupDescriptor> sgd=SnapshotsGroupDescriptorXML.readSnapshotsGroupDescriptor(reader,uid);
		if(sgd!=null) {
			return new Pair<>(sgd.getKey(), new SnapshotNode(sgd.getValue()));
		}
		Set<Identifiable> dup=new HashSet<>();	//duplicates
		Entry<Identifiable, SnapshotDescriptor> sd=SnapshotDescriptorXML.readSnapshotDescriptor(reader,uid);
		if(sd!=null) {
			dup=readDuplicates(reader,sd.getValue().objectType());
			return new Pair<>(sd.getKey(), new SnapshotNode(sd.getValue(),dup,false,false));
		}
		Entry<Identifiable, DirectoryDescriptor> dd=DirectoryDescriptorXML.readDirectoryDescriptor(reader,uid);
		if(dd!=null) {
			dup=readDuplicates(reader,dd.getValue().objectType());
//			XML.advancePast(reader, DirectoryDescriptorXML.XMLDIRECTORYTAG);
//			XML.advance(reader);
			return new Pair<>(dd.getKey(), new SnapshotNode(dd.getValue(),dup,false));
		}
		Entry<Identifiable, FileDescriptor> fd=FileDescriptorXML.readFileDescriptor(reader,uid);
		if(fd!=null) {
			dup=readDuplicates(reader,fd.getValue().objectType());
			//read past the closing tag for files since they have no children
			XML.advancePast(reader, FileDescriptorXML.XMLFILETAG);
			XML.advance(reader);
			return new Pair<>(fd.getKey(), new SnapshotNode(fd.getValue(),dup,false));
		}
		return null;
	}

	/**
	 * Read a {@link SnapshotNode} from a XML stream
	 * 
	 * @param reader is the XML stream reader
	 * @return an {@link Entry} with the key being the {@link Identifiable} read from the file and the value being the {@link SnapshotNode} with a new {@link Identifiable}
	 * @throws XMLStreamException
	 */
	public static Entry<Identifiable, SnapshotNode> readSnapshotNode(XMLStreamReader reader) throws XMLStreamException {
		return readSnapshotNode(reader, null);
	}
	
	/**
	 * Write the duplicates part of the {@link SnapshotNode}
	 * 
	 * @param out is the stream to write the XML
	 * @param sn is the {@link SnapshotNode} holding the duplicates
	 * @param tabbing is the amount of TAB characters for text alignment
	 * @return true (may be changed in the future)
	 * @throws XMLStreamException
	 */
	public static boolean writeSnapshotNodeDuplicates(XMLStreamWriter out, SnapshotNode sn, String tabbing) throws XMLStreamException {
		//write duplicates
		out.writeCharacters(tabbing);
		out.writeStartElement(_xmlDuplicatesTag);
		out.writeCharacters("\n");
		Iterator<Identifiable> it = sn.getDuplicates().iterator();
		while(it.hasNext()) {
			out.writeCharacters(tabbing+"\t");
			out.writeStartElement(_xmlDuplicateTag);
			out.writeCharacters(it.next().identifier().toString());
			out.writeEndElement();//close duplicate
			out.writeCharacters("\n");
		}
		out.writeCharacters(tabbing);
		out.writeEndElement();//close duplicates
		out.writeCharacters("\n");
		return true;
	}
	
	/**
	 * Write a {@link SnapshotNode} to a XML stream
	 * 
	 * @param out is the stream to write the XML
	 * @param id is the {@link SnapshotNode}
	 * @param tabbing is the amount of TAB characters for text alignment
	 * @return true if a node could be written
	 * @throws XMLStreamException
	 */
	public static boolean writeSnapshotNode(XMLStreamWriter out, SnapshotNode sn, String tabbing) throws XMLStreamException {
		switch (sn.getDescriptorType()) {
		case SNAPSHOTSGROUP:
			SnapshotsGroupDescriptorXML.writeSnapshotsGroupDescriptor(out, sn.getSnapshotsGroupDescriptor(),tabbing);
			break;
		case SNAPSHOT:
			SnapshotDescriptorXML.writeSnapshotDescriptor(out, sn.getSnapshotDescriptor(),tabbing);
			writeSnapshotNodeDuplicates(out, sn, tabbing+"\t");
			break;
		case DIRECTORY:
			DirectoryDescriptorXML.writeDirectoryDescriptor(out, sn.getDirectoryDescriptor(),tabbing);
			writeSnapshotNodeDuplicates(out, sn, tabbing+"\t");
			break;
		case FILE:
			FileDescriptorXML.writeFileDescriptor(out, sn.getFileDescriptor(),tabbing);
			writeSnapshotNodeDuplicates(out, sn, tabbing+"\t");
			out.writeCharacters(tabbing);
			out.writeEndElement();//close file
			out.writeCharacters("\n");
			break;
		default:
			return false;
		}
		return true;
	}

}
