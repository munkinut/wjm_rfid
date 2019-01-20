/*
 * Created on 27-Jul-2005
 *
 */
package uk.co.indigo.play.rfid.bri;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

/**
 * @author milbuw
 *
 */
public class BRIWrapper {
    
    private static String ip;
    private static int port;
    private static boolean logging;
    private static String PROPS_FILE = "conf\\BRIConsole.properties";
    private static String PROPS_TITLE = "BRI Console Properties";
    private static String PORT_PROP = "port";
    private static String IP_PROP = "ip";
    private static String LOGGING_PROP = "logging";
    private static String CMD_QUIT = "quit";
    private static String CMD_HELP = "help";
    private static String MY_PROMPT = "KO>";
    private static String BLANK_INPUT = "";
    private static String BRI_PROMPT = "OK>";
    private static String INIT_MSG = "Connection ready";
    private static String ECHO_MSG = "ECHO is ON";
    private static boolean NO_BLANKS = true;
    private static String CMD_ATTRIB = "attrib echo=on";
    
    private static BRIWrapper briWrapper = null;
    private Socket socket = null;
    private BufferedReader in = null;
    private PrintWriter out = null;
    
    private BRIWrapper() throws BRIWrapperException {
        try {
            loadProperties();
            socket = new Socket(ip, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            if (logging) System.out.println(INIT_MSG);
            out.println(CMD_ATTRIB);
            String readerInput;
            while ((readerInput = in.readLine()) != null) {
                if (readerInput.equalsIgnoreCase(BRI_PROMPT)) {  // ready prompt
                    if (logging) {
                        System.out.println(ECHO_MSG);
                    }
                    break;
                }
            }
        }
        catch (IOException ioe) {
            throw new BRIWrapperException("IO exception during construction", ioe);
        }
    }
    
    public static BRIWrapper getInstance() throws BRIWrapperException {
        if (briWrapper == null) briWrapper = new BRIWrapper();
        return briWrapper;
    }
    
    public String[] identify() throws BRIWrapperException {
        if (socket == null || in == null || out == null) {
            close(); // cleanup
            throw new BRIWrapperException("Wrapper lost connection");
        }
        out.println("r");
        ArrayList tags = new ArrayList();
        String readerInput;
        try {
	        while ((readerInput = in.readLine()) != null) {
	            if (readerInput.equalsIgnoreCase(BRI_PROMPT)) break; // ready prompt
	            if (readerInput.endsWith("r") || // echo from BRI
	               (NO_BLANKS && readerInput.length() == 0)) continue; // blank line
	            tags.add(readerInput);
	        }
	        String[] tagsArray = new String[tags.size()];
	        Iterator i = tags.iterator();
	        String tag;
	        int j = 0;
	        while (i.hasNext()) {
	            tag = (String)i.next();
	            tagsArray[j] = tag;
	            j++;
	        }
	        return tagsArray;
	    }
        catch (IOException ioe) {
            throw new BRIWrapperException("IO exception during read", ioe);
        }
    }
    
    public void close() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        }
        catch (IOException ioe) {
            // something couldnt close, forget about it
        }
    }
    
    public static void main(String[] args) {
        try {
            BRIWrapper briWrapper = BRIWrapper.getInstance();
            String[] tags = briWrapper.identify();
            for (int i = 0; i < tags.length; i++) {
                System.out.println(tags[i]);
            }
            briWrapper.close();
        }
        catch (BRIWrapperException briwe) {
            System.err.println(briwe);
        }
    }

    private static void storeProperties() throws IOException {
        Properties props = new Properties();
        props.setProperty(IP_PROP,ip);
        props.setProperty(PORT_PROP, Integer.toString(port));
        props.setProperty(LOGGING_PROP, Boolean.toString(logging));
        File propsFile = new File(PROPS_FILE);
        try {
            FileOutputStream fos = new FileOutputStream(propsFile);
            props.store(fos, PROPS_TITLE);
        }
        catch (IOException ioe) {
            System.err.println(ioe);
        }
    }
    
    private static void loadProperties() throws IOException {
        Properties props = new Properties();
        File propsFile = new File(PROPS_FILE);
        FileInputStream fis = new FileInputStream(propsFile);
        props.load(fis);
        ip = props.getProperty(IP_PROP);
        port = Integer.parseInt(props.getProperty(PORT_PROP));
        logging = Boolean.valueOf(props.getProperty(LOGGING_PROP)).booleanValue();
    }
}

