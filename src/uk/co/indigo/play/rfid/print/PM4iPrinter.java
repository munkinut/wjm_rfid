package uk.co.indigo.play.rfid.print;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Properties;

/**
 * Representation of the Intermec PM4i printer.
 * 
 * @author milbuw
 *
 */
public class PM4iPrinter implements Printer {
	
	private String ip;
	private int port;
	
    private static String PROPS_FILE = "conf\\PM4iPrinter.properties";
    private static String PROPS_TITLE = "PM4i Printer Properties";
    private static String PORT_PROP = "port";
    private static String IP_PROP = "ip";
    
    private static String ERR_NOLOAD = "Could not load properties.";
    private static String ERR_NOCONNECT = "Could not connect.";
    private static String ERR_NOSEND = "Could not send because output channel was closed.";
    private static String ERR_NODISCON = "Could not disconnect because channel was closed.";
    
    private PrintWriter out = null;
	
	/**
	 * Creates a representation of the PM4i printer, loading its properties from a config file.
	 * @throws PrintException Thrown if there are any problems loading printer properties.
	 */
	public PM4iPrinter() throws PrintException {
		try {
			loadProperties();
		}
		catch (IOException ioe) {
			throw new PrintException(PM4iPrinter.ERR_NOLOAD, ioe);
		}
	}
	
	/**
	 * Sends raw data to the printer.
	 * 
	 * @see uk.co.indigo.play.rfid.print.Printer#print(java.lang.String)
	 */
	public void print(String rawData) throws PrintException {
		connect();
		send(rawData);
		disconnect();
	}
	
	/**
	 * Connects to the printer.
	 * 
	 * @throws PrintException
	 */
	private void connect() throws PrintException {
		try {
			out = new PrintWriter(new Socket(ip, port).getOutputStream(), true);
		}
		catch (IOException ioe) {
			throw new PrintException(PM4iPrinter.ERR_NOCONNECT, ioe);
		}
	}
	
	/**
	 * Sends data to the printer.
	 * 
	 * @param rawData The data to send.
	 * @throws PrintException Thrown if there are any problems sending the data.
	 */
	private void send(String rawData) throws PrintException {
		//System.out.println("RAWDATA\n\n" + rawData);
		if (out != null) {
			out.println(rawData);
		}
		else throw new PrintException(PM4iPrinter.ERR_NOSEND);
	}
	
	/**
	 * Disconnects from the printer.
	 * @throws PrintException Thrown if there are any problems disconnecting.
	 */
	private void disconnect() throws PrintException {
		if (out != null) {
			out.close();
		}
		else throw new PrintException (PM4iPrinter.ERR_NODISCON);
	}

    /**
     * Load the printer properties from a config file.
     * @throws FileNotFoundException Thrown if the config cannot be found.
     * @throws IOException Thrown if the config cannot be read.
     */
    private void loadProperties() throws FileNotFoundException, IOException {
        Properties props = new Properties();
        File propsFile = new File(PROPS_FILE);
        FileInputStream fis = new FileInputStream(propsFile);
        props.load(fis);
        ip = props.getProperty(IP_PROP);
        port = Integer.parseInt(props.getProperty(PORT_PROP));
    }

}
