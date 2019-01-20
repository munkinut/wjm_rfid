package uk.co.indigo.play.rfid.queue;

import uk.co.indigo.play.rfid.print.PrintController;
import uk.co.indigo.play.rfid.print.PrintException;
import uk.co.indigo.play.rfid.xml.*;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

/**
 * Plugs into the DirQueueMonitor to handle XML print jobs.
 * The format of the print job XML is shown in conf\sscc96_printjob_tmpl.xml.
 * 
 * @author milbuw
 *
 */
public class XMLPrintJobProcessor implements FileProcessor {
	
	private static final String FIELD_DATA = "data";

	private Logger logger;

	public XMLPrintJobProcessor() {
		super();
		logger = Logger.getLogger(getClass().getName());
	}

	/**
	 * Processes the file passed to it by DirQueueMonitor.
	 * In this case, uses the LabelXMLParser class to get the printer name, 
	 * label type, quantity and data then passes these to the print controller 
	 * for assignment to the printer.
	 * 
	 * @param file A File object representing the print job.
	 * 
	 * @see uk.co.indigo.play.rfid.queue.FileProcessor#process(java.io.File)
	 */
	public void process(File file) throws FileProcessorException {
		try {
			logger.info(file.getName());
			LabelXMLParser lxp = new LabelXMLParser(file);
			List fields = lxp.getFields();
			String printerName = lxp.getPrinterName();
			logger.info("Printer name -> " + printerName);
			String labelType = lxp.getLabelType();
			logger.info("Label type -> " + labelType);
			int labelQuantity = lxp.getLabelQuantity();
			logger.info("Label qty -> " + labelQuantity);
			if (labelQuantity <= 0) {
				throw new FileProcessorException("Label quantity must be greater than zero.");
			}
			String rawData = null;
			RFIDField rf = null;
			for (Iterator j = fields.iterator(); j.hasNext();) {
				rf = (RFIDField)j.next();
				String fieldName = rf.getName();
				if (fieldName.equalsIgnoreCase(FIELD_DATA)) {
					rawData = rf.getValue();
				}
				logger.info("Field name -> " + fieldName +
							" Field format -> " + rf.getFormat() +
							" Field value -> " + rf.getValue());
				
			}
			if ((rawData == null) || (rawData.equalsIgnoreCase(""))) {
				throw new FileProcessorException("Data field was missing or empty.");
			}
			sendToPrintController(printerName, labelType, labelQuantity, rawData);
		}
		catch (LabelParserException lpe) {
			logger.warning(lpe.getMessage());
			throw new FileProcessorException(lpe);
		}
	}
	
	/**
	 * Sends parsed print job to the print controller.
	 * 
	 * @param printerName The name of the printer.
	 * @param labelType The type of label to print.
	 * @param labelQuantity How many labels to print.
	 * @param rawData The hex data representation of the SSCC code.
	 * @throws FileProcessorException Thrown on problems expressed by the print controller.
	 */
	private void sendToPrintController(String printerName, String labelType, int labelQuantity, String rawData) 
		throws FileProcessorException {
		
		try {
			PrintController pc = new PrintController(printerName, labelType);
			//System.out.println(rawData);
			pc.print(rawData, labelQuantity);
		}
		catch (PrintException pe) {
			throw new FileProcessorException(pe);
		}
	}

}
