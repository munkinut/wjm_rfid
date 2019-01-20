package uk.co.indigo.play.rfid.print;

/**
 * Defines the action of the printer.
 * 
 * @author milbuw
 *
 */
public interface Printer {

	/**
	 * Prints data to the printer.
	 * @param rawData Raw data to go to the printer.
	 * @throws PrintException Thrown if there are any problems sending.
	 */
	public void print(String rawData) throws PrintException;
}
