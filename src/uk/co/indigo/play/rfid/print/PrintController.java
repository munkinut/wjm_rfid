package uk.co.indigo.play.rfid.print;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Routes print jobs to its defined printer and label type.
 * 
 * @author milbuw
 *
 */
public class PrintController {
	
	private String template;
	private PrintSpooler printSpooler;
	private static final String SSCC_96_TYPE = "SSCC-96";
	private static final String SSCC_96_PM4i_TEMPLATE = "conf\\PM4iTemplate_01.prn";
	private static final String PRINTER_PM4I = "PM4i";
	
	private static final String SSCC_96_TMPL_TXT = "!RFID_SSCC_96_HEX!";
	private static final String SSCC_96_TMPL_QTY = "!QUANTITY!";
	
	/**
	 * Constructs a print controller.
	 * @param printerName The name of the printer to use.
	 * @param labelType The type of label to use.
	 * @throws PrintException Throw if there are any problems in processing the job.
	 */
	public PrintController(String printerName, String labelType) throws PrintException {
		super();
		template = acquireTemplate(labelType);
		printSpooler = new PrintSpooler(selectPrinter(printerName));
	}
	
	public static void main(String [] args) {
		try {
			PrintController pc = new PrintController("PM4i", "SSCC-96");
			pc.print("31603932445DCC4439000000", 1);
		}
		catch (PrintException pe) {
			System.err.println(pe.getMessage());
		}
	}
	
	/**
	 * Loads a template file based on the defined label type.
	 * 
	 * @param labelType The type of label to use.
	 * @return The template.
	 * @throws PrintException Thrown if the template cannot be loaded.
	 */
	private String acquireTemplate(String labelType) throws PrintException {
		File templateFile = null;
		if (labelType.equalsIgnoreCase(PrintController.SSCC_96_TYPE)) {
			templateFile = new File(SSCC_96_PM4i_TEMPLATE);
		}
		else throw new PrintException("Invalid label type: " + labelType);
		StringWriter sw = new StringWriter();
		int c;
		try {
			FileReader inFile = new FileReader(templateFile);
			while ((c = inFile.read()) != -1) {
				sw.write(c);
			}
			template = sw.toString();
			sw.close();
			inFile.close();
			//System.out.println(template);
			return template;
		}
		catch (FileNotFoundException fnfe) {
			throw new PrintException("Could not find template file.", fnfe);
		}
		catch (IOException ioe) {
			throw new PrintException("Could not acquire template file.", ioe);
		}
	}
	
	/**
	 * Selects a printer based on the defined printer name.
	 * 
	 * @param printerName The name of the printer.
	 * @return A Printer object representing the printer.
	 * @throws PrintException Thrown if no printer can be found.
	 */
	private Printer selectPrinter(String printerName) throws PrintException {
		if (printerName.equalsIgnoreCase(PRINTER_PM4I)) return new PM4iPrinter();
		else throw new PrintException("No such printer.");
	}
	
	/**
	 * Merges print data with the template to form a data string that can be sent to the printer.
	 * @param hexTagData The data to print.  This is the hex sscc tag data.
	 * @param quantity How many labels to print.
	 * @return A string containing the merged data.
	 */
	private String mergeData(String hexTagData, int quantity) {
		String labelTmpl = template;
		String labelTmpl_02 = labelTmpl.replaceFirst(SSCC_96_TMPL_TXT, hexTagData);
		String labelTmpl_03 = labelTmpl_02.replaceFirst(SSCC_96_TMPL_QTY, (quantity<2)?(""):(""+quantity));
		return labelTmpl_03;
	}
	
	//public void printTemplate() throws PrintException {
	//	printSpooler.print(template);
	//}
	
	/**
	 * Print the data to the predefined printer and label.
	 * 
	 * @param hexTagData The data to print.  This is the hex sscc tag data.
	 * @param quantity How many labels to print.
	 * @throws PrintException Thrown if there are any problems from the printer spooler.
	 */
	public void print(String hexTagData, int quantity) throws PrintException {
		String mergedLabel = mergeData(hexTagData, quantity);
		printSpooler.print(mergedLabel);
	}

}
