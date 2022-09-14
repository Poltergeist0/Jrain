package jrain.filesystem.snapshot.xml;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import jrain.filesystem.snapshot.SnapshotDescriptor;
import jrain.filesystem.snapshot.immutable.FileDescriptor;
import jrain.hash.hashInstance.hashes.immutable.Hashes;
import jrain.xml.XML;
import jrain.exceptions.ExceptionInvalidValue;
import jrain.utils.HexadecimalUtils;

/**
 * @author poltergeist0
 *
 * Collection of methods to read and write {@link Hashes} from/to XML stream
 */
public interface HashesXML {
	
	public static final String HASHESTAG="hashes";
	public static final String _xmlHashesTag=HASHESTAG;

	/**
	 * Convert hash names to valid XML strings.
	 * 
	 * This conversion needs to be performed since some hash names have restricted
	 * XML characters.
	 * 
	 * @param s is the original hash name to convert
	 * @return the converted hash name
	 */
	private static String convertHashNamesToXML(String s) {
		return s.replace("/", "-");
	}

	/**
	 * Build a table for fast conversion of hash names to valid XML strings.
	 * This prevents recalculation of the strings for each hash that needs to be
	 * read.
	 * The key is the hash name and the value is the corresponding XML string.
	 * 
	 * @return a {@link HashMap} with the conversion table
	 */
	private static HashMap<String, String> hashNamesToXML() {
		HashMap<String, String> hsh=new HashMap<>();
		Iterator<String> it = jrain.hash.hashInstance.hashes.Hashes.algorithms().iterator();
		while(it.hasNext()) {
			String h = it.next();
			hsh.put(h, convertHashNamesToXML(h));
		}
		return hsh;
	}

	/**
	 * Build a table for fast conversion of XML strings to valid hash names.
	 * This prevents recalculation of the strings for each hash that needs to be
	 * read.
	 * The key is the XML string and the value is the corresponding hash name.
	 * 
	 * @return a {@link HashMap} with the conversion table
	 */
	private static HashMap<String, String> hashXMLToNames() {
		HashMap<String, String> hsh=new HashMap<>();
		Iterator<String> it = jrain.hash.hashInstance.hashes.Hashes.algorithms().iterator();
		while(it.hasNext()) {
			String h = it.next();
			hsh.put(convertHashNamesToXML(h),h);
		}
		return hsh;
	}

	/**
	 * Read the hashes to calculate from a {@link SnapshotDescriptor}.
	 * 
	 * @param reader is the XML stream reader
	 * @return a {@link Set} with the hashes that have a value of true
	 * @throws XMLStreamException
	 */
	public static Set<String> readHashesToCalculate(XMLStreamReader reader) throws XMLStreamException {
		//assume that reader is already at a START_ELEMENT
		HashMap<String, String> hsh = hashXMLToNames();
		HashSet<String> hashes=new HashSet<>();
		boolean fail=true;//failed to read at least one
		while(reader.hasNext() && (reader.isStartElement() && reader.getLocalName().startsWith(SnapshotDescriptor.tagSnapshotCalculate))) {
			String s=reader.getElementText();
			if(Boolean.parseBoolean(s)==true) {
				hashes.add(hsh.get(reader.getLocalName().substring(SnapshotDescriptor.tagSnapshotCalculate.length())));
			}
			fail=false;
			reader.nextTag();
		}
		if(fail) return null;//failed to read at least one
		return hashes;
	}

	/**
	 * Read the hashes from a {@link FileDescriptor}
	 * 
	 * @param reader is the XML stream reader
	 * @return an {@link Hashes} object
	 * @throws XMLStreamException
	 */
	public static Hashes readCalculatedHashes(XMLStreamReader reader) throws XMLStreamException {
		//assume that reader is already at a START_ELEMENT
		HashMap<String, String> hsh = hashXMLToNames();
		String s=reader.getLocalName();
		if(!s.equals(_xmlHashesTag)) return null;
		HashMap<String, byte[]> hashes=new HashMap<>();
		while(reader.hasNext() && !(reader.isEndElement() && reader.getLocalName().equals(_xmlHashesTag))) {
			reader.next();
			if(reader.isStartElement()) {
				try {
					hashes.put(hsh.get(reader.getLocalName()), HexadecimalUtils.convertFromHex(reader.getElementText()));
				} catch (ExceptionInvalidValue e) {
					System.err.println("Illegal character in hexadecimal string at "+reader.getLocation());
					e.printStackTrace();
				}
			}
		}
		XML.advancePast(reader, _xmlHashesTag);
		XML.advance(reader);
		return new Hashes(hashes);
	}

	/**
	 * Write the hashes to calculate from a {@link SnapshotDescriptor}.
	 * 
	 * @param out is the stream to write the XML
	 * @param hashes are the name of the hashes that have a value of true
	 * @param tabbing is the amount of TAB characters for text alignment
	 * @return true (may be changed in the future)
	 * @throws XMLStreamException
	 */
	public static boolean writeHashesToCalculate(XMLStreamWriter out, Set<String> hashes, String tabbing) throws XMLStreamException {
		HashMap<String, String> hsh = hashNamesToXML();
		Iterator<String> it = jrain.hash.hashInstance.hashes.Hashes.algorithms().iterator();
		while(it.hasNext()) {
			String h = it.next();
			XML.writeSimpleElementText(out, SnapshotDescriptor.tagSnapshotCalculate+hsh.get(h), ((hashes.contains(h))?"true":"false"),tabbing);
		}
		return true;
	}

	/**
	 * Write the calculated {@link Hashes} from a {@link FileDescriptor} to XML.
	 * 
	 * @param out is the stream to write the XML
	 * @param hashes are the {@link Hashes} to write
	 * @param tabbing is the amount of TAB characters for text alignment
	 * @return true (may be changed in the future)
	 * @throws XMLStreamException
	 */
	public static boolean writeCalculatedHashes(XMLStreamWriter out, Hashes hashes, String tabbing) throws XMLStreamException {
		HashMap<String, String> hsh = hashNamesToXML();
		out.writeCharacters(tabbing);
		out.writeStartElement(_xmlHashesTag);
		out.writeCharacters("\n");
		Iterator<String> it = hashes.hashes().iterator();
		while(it.hasNext()) {
			String h = it.next();
			XML.writeSimpleElementText(out, hsh.get(h), HexadecimalUtils.convertToHex(hashes.hash(h)),tabbing+"\t");
		}
		out.writeCharacters(tabbing);
		out.writeEndElement();
		out.writeCharacters("\n");
		return true;
	}

}
