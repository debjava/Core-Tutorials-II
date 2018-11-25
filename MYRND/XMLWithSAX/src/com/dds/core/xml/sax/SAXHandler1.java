package com.dds.core.xml.sax;

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXHandler1 extends DefaultHandler 
{
	private Map<String, String> dataMap = null;
	private String value = null;
	
	public Map<String, String> getDataMap() {
		return dataMap;
	}

	public SAXHandler1() 
	{
		super();
		dataMap = new HashMap<String, String>();
	}
	
	@Override
	public void startDocument() throws SAXException 
	{
		super.startDocument();
		System.out.println("startDocument()");
	}
	
	@Override
	public void endDocument() throws SAXException 
	{
		super.endDocument();
		System.out.println("endDocument()");
	}

	@Override
	public void characters(char[] arg0, int arg1, int arg2) throws SAXException 
	{
		super.characters(arg0, arg1, arg2);
//		System.out.println(arg0);
//		System.out.println(arg1);
//		System.out.println(arg2);
		System.out.println("Characters--->>>"+new String(arg0,arg1,arg2));
		value = new String(arg0,arg1,arg2);
	}

	@Override
	public void endElement(String arg0, String arg1, String arg2)
	throws SAXException 
	{
		super.endElement(arg0, arg1, arg2);
		System.out.println("------- endElement() --------");
		System.out.println("arg0----"+arg0);
		System.out.println("arg1----"+arg1);
		System.out.println("arg2----"+arg2);
		System.out.println("------- endElement() --------");
	}
	
	@Override
	public void startElement(String arg0, String arg1, String tagName,
			Attributes arg3) throws SAXException 
	{
		super.startElement(arg0, arg1, tagName, arg3);
		System.out.println("-------- startElement() ---------");
//		System.out.println("arg0----->"+arg0);
//		System.out.println("arg1----->"+arg1);
		System.out.println("tagName----->"+tagName);
		if( tagName.equals("NAME"))
			dataMap.put(tagName, value);
	}
}
