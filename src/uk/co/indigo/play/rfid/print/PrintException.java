package uk.co.indigo.play.rfid.print;

/**
 * Custom exception for RFID print problems.
 * 
 * @author milbuw
 *
 */
public class PrintException extends Exception {

	/**
	 * 
	 */
	public PrintException() {
		super();
	}

	/**
	 * @param arg0
	 */
	public PrintException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public PrintException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * @param arg0
	 */
	public PrintException(Throwable arg0) {
		super(arg0);
	}

}
