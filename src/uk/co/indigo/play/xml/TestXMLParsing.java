package uk.co.indigo.play.xml;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.*;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.apache.xpath.*;

public class TestXMLParsing {

	public static void main(String[] args) {
		try {
			
			// init
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = db.parse(new File("test.xml"));
			
			// walk 
			System.out.println("Testing walk()");
			Element docElement = doc.getDocumentElement();
			System.out.println(docElement.getTagName());
			walk(docElement);
			
			// select
			System.out.println("Testing select()");
			String xpath = "//wjm";
			select(docElement, xpath);
			
		}
		catch (ParserConfigurationException pce) {
			System.err.println(pce.getMessage());
		}
		catch (SAXException se) {
			System.err.println(se.getMessage());
		}
		catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
	}
	
	private static void walk(Node node) {
		NodeList nodes = node.getChildNodes();
		int length = 0;
		if ((nodes != null) && ((length = nodes.getLength()) > 0)) {
			Element element;
			Node n;
			for (int i = 0; i < length; i++) {
				n = nodes.item(i);
				if (n.getNodeType() == Node.ELEMENT_NODE) {
					element = (Element)n;
					System.out.println(element.getTagName());
				}
				walk(n);
			}
		}
	}
	
	private static void select(Node node, String xpath) {
		try {
			NodeList nodes = XPathAPI.selectNodeList(node, xpath);
			int length;
			if ((nodes != null) && ((length = nodes.getLength()) > 0)) {
				Node n;
				for (int i = 0; i < length; i++) {
					n = nodes.item(i);
					walk(n);
				}
			}
		}
		catch (TransformerException te) {
			System.err.println(te.getMessage());
		}
	}

}
