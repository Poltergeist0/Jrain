package jrain.xml.identifiable.immutable;

import java.util.Map.Entry;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import jrain.xml.XML;
import jrain.identifiable.immutable.Identifiable;
import jrain.entry.mutable.Pair;

/**
 * @author poltergeist_00dead
 * 
 */
public interface IdentifiableXML {//extends XMLParserBase_I<Identifiable>{
	
	public static final String XMLIDTAG=Identifiable.IDENTIFIABLETAG;
//	public static final String XMLIDTAGOPEN=XMLTAGOPENSTART+XMLIDTAG+XMLTAGOPENEND;
//	public static final String XMLIDTAGCLOSE=XMLTAGCLOSESTART+XMLIDTAG+XMLTAGCLOSEEND;
	
	/**
	 * @param reader
	 * @param type
	 * @return entry with old ID as key and new ID as value
	 * @throws XMLStreamException
	 */
	public static Entry<Identifiable, Identifiable> readIdentifiable(XMLStreamReader reader,String type) throws XMLStreamException {
		//assume that reader is already at a START_ELEMENT
		String s=XML.readSimpleElementText(reader, XMLIDTAG);
		if(s!=null) {
			return new Pair<>(new Identifiable(type+jrain.identifiable.immutable.Identifiable.SEPARATOR+s),new Identifiable(type+jrain.identifiable.immutable.Identifiable.SEPARATOR));
		}
		return null;
	}

	public static boolean writeIdentifiable(XMLStreamWriter out, Identifiable id, String tabbing) throws XMLStreamException {
		return XML.writeSimpleElementText(out, XMLIDTAG, id.identifier().toString(),tabbing);
	}
}
