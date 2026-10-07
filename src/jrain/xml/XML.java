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
package jrain.xml;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public interface XML {
//	public static enum INFOTYPE {ALL,OPENTAGANDINFO,INFO};
	
//	public static final String XMLTAGOPENSTART="<";
//	public static final String XMLTAGOPENEND=">";
//	public static final String XMLTAGCLOSESTART="</";
//	public static final String XMLTAGCLOSEEND=XMLTAGOPENEND;
//	public static final String XMLTRUE="True";
//	public static final String XMLFALSE="False";
	
	/**
	 * Get the string corresponding to the XML opening tag
	 * 
	 * @param tag
	 * @return
	 */
//	public static String tagOpen(final String tag) {return XMLTAGOPENSTART+tag+XMLTAGOPENEND;}
	
	/**
	 * Get the string corresponding to the XML closing tag
	 * 
	 * @param tag
	 * @return
	 */
//	public static String tagClose(final String tag) {return XMLTAGCLOSESTART+tag+XMLTAGCLOSEEND;}
	
	/**
	 * Check if the given string contains the XML opening tag of the given tag at 
	 * the given offset, disregarding any white space
	 * 
	 * @param data
	 * @param offset
	 * @param tag
	 * @return
	 */
//	public static boolean hasTagOpen(final String data,final int offset,final String tag) {
////		int first=JumpWhiteSpaces.jumpWhiteSpaces(data, offset);
////		if(data.startsWith(tagOpen(tag),first)) return true;
////		return false;
//		if(tagOpen(data, offset, tag)==offset) return false;
//		return true;
//	}

	/**
	 * Check if the given string contains the XML closing tag of the given tag at 
	 * the given offset, disregarding any white space
	 * 
	 * @param data
	 * @param offset
	 * @param tag
	 * @return
	 */
//	public static boolean hasTagClose(final String data,final int offset,final String tag) {
////		int first=JumpWhiteSpaces.jumpWhiteSpaces(data, offset);
////		if(data.startsWith(tagClose(tag),first)) return true;
////		return false;
//		if(tagClose(data, offset, tag)==offset) return false;
//		return true;
//	}

	/**
	 * Advance the given XML opening tag in the offset, if it exists. Otherwise return
	 * the same offset.
	 * Disregards any white space.
	 * 
	 * @param data
	 * @param offset
	 * @param tag
	 * @return
	 */
//	public static int tagOpen(final String data,final int offset,final String tag) {
//		int first=JumpWhiteSpaces.jumpWhiteSpaces(data, offset);
//		if(data.startsWith(tagOpen(tag),first)) return first+tagOpen(tag).length();
//		return offset;
//	}

	/**
	 * Advance the given XML closing tag in the offset, if it exists. Otherwise return
	 * the same offset.
	 * Disregards any white space.
	 * 
	 * @param data
	 * @param offset
	 * @param tag
	 * @return
	 * @throws XMLStreamException 
	 */
//	public static int tagClose(final String data,final int offset,final String tag) {
//		int first=JumpWhiteSpaces.jumpWhiteSpaces(data, offset);
//		if(data.startsWith(tagClose(tag),first)) return first+tagClose(tag).length();
//		return offset;
//	}
	
	/**
	 * Advance to next start element or end element
	 * @param reader
	 * @throws XMLStreamException
	 */
	public static void advance(XMLStreamReader reader) throws XMLStreamException {
		if(reader.hasNext()) reader.next();//advance
		while(reader.hasNext() && !reader.isStartElement() && !reader.isEndElement()) reader.next();
	}

	/**
	 * Advance to the next element after the closing tag
	 * 
	 * @param reader
	 * @param tag
	 * @throws XMLStreamException
	 */
	public static void advancePast(XMLStreamReader reader, String tag) throws XMLStreamException {
		while(
				reader.hasNext() && 
				!(reader.isEndElement() && reader.getLocalName().equals(tag))
				) reader.next();//move past closing duplicates tag
		if(reader.hasNext() && reader.isEndElement() && reader.getLocalName().equals(tag)) reader.next();
	}

	public static String readSimpleElementText(XMLStreamReader reader,String tag) throws XMLStreamException {
		//assume that reader is already at a START_ELEMENT
//		System.out.print("name="+reader.getLocalName());
		String s=reader.getLocalName();
		if(s.equals(tag)) {
			String t=reader.getElementText();//read and advance to END_ELEMENT
//			System.out.println(" ; text="+t);
//			while(!reader.isStartElement() && reader.hasNext()) {
////				int r = 
//				reader.next();//advance to next tag
////				if(r==XMLStreamConstants.END_ELEMENT) break;
//			}
			advance(reader);
			return t;
		}
		return null;
	}

	public static boolean writeSimpleElementText(XMLStreamWriter out, String tag, String id, String tabbing) throws XMLStreamException {
		out.writeCharacters(tabbing);
		out.writeStartElement(tag);
		out.writeCharacters(id);
		out.writeEndElement();
		out.writeCharacters("\n");
		return true;
	}
}
