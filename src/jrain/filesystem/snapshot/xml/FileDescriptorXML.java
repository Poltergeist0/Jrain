package jrain.filesystem.snapshot.xml;

import java.util.Map.Entry;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import jrain.filesystem.snapshot.immutable.DirectoryDescriptor;
import jrain.filesystem.snapshot.immutable.FileDescriptor;
import jrain.hash.hashInstance.hashes.immutable.Hashes;
import jrain.xml.XML;
import jrain.identifiable.Identifiable;
import jrain.entry.mutable.Pair;

/**
 * @author poltergeist0
 *
 * Collection of methods to read and write {@link FileDescriptor} from/to XML stream
 */
public interface FileDescriptorXML{
	public static final String XMLFILETAG=FileDescriptor.tagFile;

	/**
	 * Read a {@link FileDescriptor}.
	 * 
	 * @param reader is the XML stream reader
	 * @param uid is an optional UUID to be used
	 * @return an {@link Entry} with the key being the {@link Identifiable} read from the file and the value being the {@link DirectoryDescriptor} with a new {@link Identifiable}
	 * @throws XMLStreamException
	 */
	public static Entry<jrain.identifiable.immutable.Identifiable, FileDescriptor> readFileDescriptor(XMLStreamReader reader, jrain.identifiable.immutable.Identifiable uid) throws XMLStreamException {
		//assume that reader is already at a START_ELEMENT
		String s=reader.getLocalName();
		if(!s.equals(XMLFILETAG)) return null;//not a directory
		XML.advance(reader);
		Entry<jrain.identifiable.immutable.Identifiable, DirectoryDescriptor> base=null;
		Hashes hashes=null;
		int tries=2;//number of fields. Worst case. Assumes fields are out of order and only one per cycle is found
		while(base==null || hashes==null) {
			if(base==null) base=DirectoryDescriptorXML.readDirectoryDescriptorData(reader, Identifiable.objectTypeOf(FileDescriptor.class),uid);
			if(hashes==null) hashes=HashesXML.readCalculatedHashes(reader);
			if(tries<=0) {//at least one of the tags does not exist
				return null;
			}
			--tries;
		}
		return new Pair<>(base.getKey(), new FileDescriptor(base.getValue().identifier(), base.getValue().getDescriptorName(),base.getValue().getPath(), base.getValue().getDescriptorSize(),base.getValue().getCreationDateTime(),base.getValue().getLastModificationDateTime(),base.getValue().getLastAccessDateTime(),base.getValue().isReadable(),hashes));
	}

	/**
	 * Write a {@link FileDescriptor}.
	 * 
	 * @param out is the stream to write the XML
	 * @param id is the {@link FileDescriptor}
	 * @param tabbing is the amount of TAB characters for text alignment
	 * @return true (may be changed in the future)
	 * @throws XMLStreamException
	 */
	public static boolean writeFileDescriptor(XMLStreamWriter out, FileDescriptor id, String tabbing) throws XMLStreamException {
		out.writeCharacters(tabbing);
		out.writeStartElement(XMLFILETAG);
		out.writeCharacters("\n");
		DirectoryDescriptorXML.writeDirectoryDescriptorData(out, id,tabbing+"\t");
		HashesXML.writeCalculatedHashes(out, id.getHashes(),tabbing+"\t");
		return true;
	}
}
