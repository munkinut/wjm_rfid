package uk.co.indigo.play.rfid.xml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.apache.xpath.*;

/**
 * Parses XML print jobs.  The are usually picked up from the queue by the DirQueueMonitor.
 * @author milbuw
 *
 */
public class LabelXMLParser {
	
	private static final String XPATH_FIELD = "//rfid/field";
	private static final String XPATH_PRINTER = "//label/printer";
	private static final String XPATH_LABEL = "//label";
	private static final String LABEL_TYPE = "type";
	private static final String LABEL_QTY = "quantity";
	private static final String PRINTER_NAME = "name";
	private static final String FIELD_NAME = "name";
	private static final String FIELD_FORMAT = "format";
	
	private Document labelXMLDoc;

	/**
	 * Creates a parser for the specified file.
	 * 
	 * @param labelXMLFile The XML file to parse.
	 * @throws LabelParserException Thrown if there are any problems reading the file.
	 */
	public LabelXMLParser(File labelXMLFile) throws LabelParserException {
		super();
		try {
			labelXMLDoc = DocumentBuilderFactory.newInstance().
				newDocumentBuilder().parse(labelXMLFile);
		}
		catch (ParserConfigurationException pce) {
			throw new LabelParserException(pce);
		}
		catch (SAXException se) {
			throw new LabelParserException(se);
		}
		catch (IOException ioe) {
			throw new LabelParserException(ioe);
		}
	}
	
	/**
	 * Get the printer name from the print job.
	 * 
	 * @return The printer name.
	 * @throws LabelParserException Thrown if the printer name could not be found.
	 */
	public String getPrinterName() throws LabelParserException {
		try {
			NodeList fieldNodes = XPathAPI.selectNodeList(labelXMLDoc, XPATH_PRINTER);
			Node fieldNode;
			NamedNodeMap nodeMap;
			String nameValue = "";
			for (int i = 0; i < fieldNodes.getLength(); i++) {
				fieldNode = fieldNodes.item(i);
				nodeMap = fieldNode.getAttributes();
				nameValue = nodeMap.getNamedItem(PRINTER_NAME).getNodeValue();
			}
			return nameValue;
		}
		catch (TransformerException te) {
			throw new LabelParserException(te);
		}
	}
	
	/**
	 * Get the label type from the print job.
	 * 
	 * @return The label type.
	 * @throws LabelParserException Thrown if the label type could not be found.
	 */
	public String getLabelType() throws LabelParserException {
		try {
			NodeList fieldNodes = XPathAPI.selectNodeList(labelXMLDoc, XPATH_LABEL);
			Node fieldNode;
			NamedNodeMap nodeMap;
			String nameValue = "";
			for (int i = 0; i < fieldNodes.getLength(); i++) {
				fieldNode = fieldNodes.item(i);
				nodeMap = fieldNode.getAttributes();
				nameValue = nodeMap.getNamedItem(LABEL_TYPE).getNodeValue();
			}
			return nameValue;
		}
		catch (TransformerException te) {
			throw new LabelParserException(te);
		}
	}
	
	/**
	 * Get the label quantity from the print job.
	 * 
	 * @return The number of labels to print.
	 * @throws LabelParserException Thrown if the label quantity could not be found.
	 */
	public int getLabelQuantity() throws LabelParserException {
		try {
			NodeList fieldNodes = XPathAPI.selectNodeList(labelXMLDoc, XPATH_LABEL);
			Node fieldNode;
			NamedNodeMap nodeMap;
			String nameValue = "";
			for (int i = 0; i < fieldNodes.getLength(); i++) {
				fieldNode = fieldNodes.item(i);
				nodeMap = fieldNode.getAttributes();
				nameValue = nodeMap.getNamedItem(LABEL_QTY).getNodeValue();
			}
			return Integer.parseInt(nameValue);
		}
		catch (NumberFormatException nfe) {
			throw new LabelParserException(nfe);
		}
		catch (TransformerException te) {
			throw new LabelParserException(te);
		}
	}

	/**
	 * Get a list of data fields from the print job.
	 * @return The list of fields.  This is a list of RFIDField objects.
	 * @throws LabelParserException Thrown if no data fields could be found.
	 */
	public List getFields() throws LabelParserException {
		List fields = new ArrayList();
		try {
			NodeList fieldNodes = XPathAPI.selectNodeList(labelXMLDoc, XPATH_FIELD);
			Node fieldNode;
			NamedNodeMap nodeMap;
			String nameValue;
			String formatValue;
			String fieldValue;
			int formatInt;
			for (int i = 0; i < fieldNodes.getLength(); i++) {
				fieldNode = fieldNodes.item(i);
				nodeMap = fieldNode.getAttributes();
				nameValue = nodeMap.getNamedItem(FIELD_NAME).getNodeValue();
				formatValue = nodeMap.getNamedItem(FIELD_FORMAT).getNodeValue();
				formatInt = convertFormat(formatValue);
				fieldValue = fieldNode.getFirstChild().getNodeValue();
				RFIDField rf = new RFIDField(nameValue, formatInt, fieldValue);
				fields.add(rf);
			}
			return fields;
		}
		catch (TransformerException te) {
			throw new LabelParserException(te);
		}
		catch (RFIDFieldException rfe) {
			throw new LabelParserException(rfe);
		}
	}
	
	/**
	 * Converts the string format for the data type to an int format suitable for 
	 * use in the RFIDField object.
	 * 
	 * @param format The string format.
	 * @return The int equivalent.
	 */
	private int convertFormat(String format) {
		int formatInt = 0;
		if (format.equalsIgnoreCase("str"))
			formatInt = RFIDField.FORMAT_STR;
		else if (format.equalsIgnoreCase("hex"))
			formatInt = RFIDField.FORMAT_HEX;
		return formatInt;
	}
	
	public static void main(String[] args) {
		try {
			LabelXMLParser lxp = new LabelXMLParser(new File("conf\\label.xml"));
			List fields = lxp.getFields();
			RFIDField rf = null;
			for (Iterator j = fields.iterator(); j.hasNext();) {
					rf = (RFIDField)j.next();
					System.out.println("Field name -> " + rf.getName());
					System.out.println("Field format -> " + rf.getFormat());
					System.out.println("Field value -> " + rf.getValue());
			}
			String printer = lxp.getPrinterName();
			System.out.println("Printer name -> " + printer);
			String labelType = lxp.getLabelType();
			System.out.println("Label type -> " + labelType);
			int labelQty = lxp.getLabelQuantity();
			System.out.println("Label qty -> " + labelQty);
		}
		catch (LabelParserException lpe) {
			System.err.println(lpe.getMessage());
		}
	}

}
