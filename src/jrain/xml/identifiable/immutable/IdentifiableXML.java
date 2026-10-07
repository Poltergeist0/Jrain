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
