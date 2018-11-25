package com.dds.core.xml.sax;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

public class TestSAXHandler1 
{
	public static void main(String[] args) 
	{
		File file = new File("config/phonebook.xml");
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try 
		{
			SAXParser parser = spf.newSAXParser();
			spf.setNamespaceAware(true);
			spf.setValidating(true);
			
//			System.out.println("Parser will " + (spf.isNamespaceAware() ? "" : "not ")
//					+ "be namespace aware");
//			System.out.println("Parser will " + (spf.isValidating() ? "" : "not ")
//					+ "validate XML");
			SAXHandler1 handler1 = new SAXHandler1();
			parser.parse(file, handler1);
		}
		catch( IOException ie )
		{
			ie.printStackTrace();
		}
		catch (ParserConfigurationException e) 
		{
			e.printStackTrace();
		}
		catch (SAXException e) 
		{
			e.printStackTrace();
		}
	}
}
