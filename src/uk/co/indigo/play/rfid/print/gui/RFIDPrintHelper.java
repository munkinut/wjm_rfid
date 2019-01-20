package uk.co.indigo.play.rfid.print.gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Random;

import uk.co.indigo.epc.SSCC96Codec;
import uk.co.indigo.epc.TagSSCC96;
import uk.co.indigo.play.binhexdec.Bin2Hex;
import uk.co.indigo.play.rfid.print.PrintException;
import uk.co.indigo.play.rfid.queue.DirQueueMonitor;
import uk.co.indigo.play.rfid.queue.FileProcessor;
import uk.co.indigo.play.rfid.queue.XMLPrintJobProcessor;

/**
 * This is the controller that provides API proxies for the encode and print functions.
 * It is called by the view: RFIDPrintSWT.
 * 
 * @author milbuw
 */
public class RFIDPrintHelper {
	
	private static final String SSCC96_PRINTJOB_TMPL = "conf\\sscc96_printjob_tmpl.xml";
	private static final String QTY = "QTY";
	private static final String PRINTER = "PRINTER";
	private static final String DATA = "RFID_DATA";
	private static final String OUTFILE_TMPL = "sscc96_printjob_NUMBER.xml";
	private static final Random generator = new Random();
	private static DirQueueMonitor dqm = null;
	
	private RFIDPrintHelper() {}
	
	/**
	 * A controller proxy to the SSCC encoding services.
	 * 
	 * @param hv A decimal String representation of the header value.
	 * @param fv A decimal String representation of the filter value.
	 * @param pv A decimal String representation of the partition value.
	 * @param cpi A decimal String representation of the company partition prefix.
	 * @param sr A decimal String representation of the serial reference.
	 * @param ed A decimal String representation of the extension digit.
	 * @param cd A decimal String representation of the check digit.
	 * @return A String representation of the hex encoded binary SSCC.
	 */
	public static String encode(String hv, String fv, String pv, String cpi, String sr, String ed, String cd) {
		String sc = ed + cpi + sr + cd;
		TagSSCC96 tag = new TagSSCC96(Integer.parseInt(hv), Integer.parseInt(fv), Integer.parseInt(pv), cpi, sr, ed, cd, sc);
		String binary = SSCC96Codec.encodeSSCC96(tag);
		return Bin2Hex.bin2HexDigits(binary);
	}
	
	/**
	 * @param printerName Must be a valid printer name as determined by uk.co.indigo.play.rfid.print.PrintController factory method.
	 * @param data Raw hex data representation of the SSCC.
	 * @param quantity The number of SSCC labels to print.
	 * @throws PrintException Thrown on problems with the print template or the queue.
	 */
	public static void print(String printerName, String data, int quantity) throws PrintException {
		String template = acquireTemplate();
		String result_01 = template.replaceFirst(QTY, "" + quantity);
		String result_02 = result_01.replaceFirst(PRINTER, printerName);
		String result_03 = result_02.replaceFirst(DATA, data);
		String nextJobName = OUTFILE_TMPL.replaceFirst("NUMBER", "" + getJobNumber());
		queuePrintJob(nextJobName, result_03);
	}
	
	public static void startQueue(String queueDir, String archiveDir, long dutyCycle) {
		FileProcessor fp = new XMLPrintJobProcessor();
		dqm = new DirQueueMonitor(fp, 
								  new File(queueDir), 
								  new File(archiveDir), 
								  dutyCycle);
		Thread queueMon = new Thread(dqm);
		queueMon.start();
		dqm.setRunning(true);
	}
	
	public static void stopQueue() {
		if (dqm != null) dqm.stop();
	}

	/**
	 * @return A String containing the template text for an SSCC96 print job.
	 * @throws PrintException Thrown on problems finding or loading the template file.
	 */
	private static String acquireTemplate() throws PrintException {
		String template = null;
		File templateFile = new File(SSCC96_PRINTJOB_TMPL);
		//System.out.println(templateFile);
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
	 * @param jobName A name for the job.  Should be unique.
	 * @param job The data filled version of the template.
	 * @throws PrintException Thrown when there is a problem writing the job to the queue.
	 */
	private static void queuePrintJob(String jobName, String job) throws PrintException {
		try {
			 //FIXME: Needs to pick up the correct queue instead of using the default queue from DirQueueMonitor.
			File outFile = new File(DirQueueMonitor.DEFAULT_QUEUE, jobName);
			FileWriter fw = new FileWriter(outFile);
			StringReader sr = new StringReader(job);
			int c;
			while ((c = sr.read()) != -1) {
				fw.write(c);
			}
			fw.close();
			sr.close();
		}
		catch (IOException ioe) {
			throw new PrintException(ioe);
		}
	}
	
	/**
	 * @return A long generated by the Java pseudo random number generator.
	 */
	private static long getJobNumber() {
		return Math.abs(generator.nextLong());
	}
}
