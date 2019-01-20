package uk.co.indigo.play.rfid.print;

/**
 * A decorator class that would add any extra filtering, caching, buffering etc
 * before a print job is allowed to hit a printer.
 * 
 * In the default case, it simply forwards the data direct to the printer.
 * 
 * @author milbuw
 *
 */
public class PrintSpooler {
	
	private Printer printer;

	/**
	 * @param printer The printer to which this spooler is attached.
	 */
	public PrintSpooler(Printer printer) {
		super();
		this.printer = printer;
	}
	
	/**
	 * The decorator method.  This can be extended to provide filtering.
	 * @param rawData The raw data to be sent to the printer.
	 * @throws PrintException Thrown if there are any problems sending to the printer.
	 */
	public void print(String rawData) throws PrintException {
		printer.print(rawData);
	}

}
