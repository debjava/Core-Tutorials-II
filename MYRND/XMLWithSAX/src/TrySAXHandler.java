import java.io.File;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class TrySAXHandler {
	public static void main(String args[]) throws Exception {
		File file = new File("config/test.xml");
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser parser = null;
		spf.setNamespaceAware(true);
		spf.setValidating(true);
		System.out.println("Parser will " + (spf.isNamespaceAware() ? "" : "not ")
				+ "be namespace aware");
		System.out.println("Parser will " + (spf.isValidating() ? "" : "not ")
				+ "validate XML");

		parser = spf.newSAXParser();
		System.out.println("Parser object is: " + parser);

		MySAXHandler handler = new MySAXHandler();
		parser.parse(file, handler);
	}
}

class MySAXHandler extends DefaultHandler {
	public void startDocument() {
		System.out.println("Start document: ");
	}

	public void endDocument() {
		System.out.println("End document: ");
	}

	public void startElement(String uri, String localName, String qname,
			Attributes attr) {
		System.out.println("Start element: local name: " + localName + " qname: "
				+ qname + " uri: " + uri);
	}

	public void endElement(String uri, String localName, String qname) {
		System.out.println("End element: local name: " + localName + " qname: "
				+ qname + " uri: " + uri);
	}

	public void characters(char[] ch, int start, int length) {
		System.out.println("Characters: " + new String(ch, start, length));
	}

	public void ignorableWhitespace(char[] ch, int start, int length) {
		System.out
		.println("Ignorable whitespace: " + new String(ch, start, length));
	}
}


