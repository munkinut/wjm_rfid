/*
 * Created on 24-Aug-2005
 *
 */
package uk.co.indigo.play.rfid.bri;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * @author milbuw
 *
 */
public class TriggerController2 {

    private static String ip;
    private static int port;
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
    private static String BRI_ERROR = "ERR";
    private static String INIT_MSG = "Connection ready";
    private static String ECHO_MSG = "ECHO is ON";
    private static boolean NO_BLANKS = true;
    private static String CMD_ECHO_ON = "attrib echo=on";
    private static String LISTENER_NAME = "bri";
    private String TRIG_EVENT = "EVT:TRIGGER";
    private String EVT_MSG = BRI_PROMPT.concat(TRIG_EVENT);
    private static boolean logging;
    
    private boolean listening = false;
    
    private static TriggerController2 triggerController = null;
    private Socket socket = null;
    private BufferedReader in = null;
    private PrintWriter out = null;
    
    private HashMap triggerListeners = null;
    
    public static void main(String[] args) {
        try {

            final TriggerController2 triggerController = TriggerController2.getInstance();
            Trigger trigger_01 = new Trigger("mytrig01", 1, 1, 100);
            Trigger trigger_02 = new Trigger("mytrig02", 2, 2, 100);

            triggerController.addTriggerListener(trigger_01, new TriggerListenerInterface() {
            	public void notify(TriggerEvent te) throws BRIWrapperException {
            		System.out.println("BING! " + te.toString());
           			List tags = triggerController.identify();
           			Iterator i = tags.iterator();
           			String tag = null;
           			while (i.hasNext()) {
           				tag = (String)i.next();
           				System.out.println(tag);
           			}
            	}
            });
            
            triggerController.addTriggerListener(trigger_02, new TriggerListenerInterface() {
            	public void notify(TriggerEvent te) throws BRIWrapperException {
            		System.out.println("BING! " + te.toString());
            		// create a print controller
            		// aquire some tag data
            		// send it to the print controller
            		// demonstrates printing on a trigger
            	}
            });
            
            triggerController.listen();
            
        }
        catch (BRIWrapperException bwe) {
            System.err.println(bwe);
        }
        finally {
            if (triggerController != null) triggerController.close();
        }
    }

    private TriggerController2() throws BRIWrapperException {
        loadProperties();
        triggerListeners = new HashMap();
        initSocket();
        issueEchoCmd();
    }
    
    private void initSocket() throws BRIWrapperException{
        try {
	        socket = new Socket(ip, port);
	        out = new PrintWriter(socket.getOutputStream(), true);
	        in = new BufferedReader(new InputStreamReader(
	                socket.getInputStream()));
	        System.out.println(INIT_MSG);
        }
        catch (IOException ioe) {
            throw new BRIWrapperException("IO exception during construction", ioe);
        }
    }
    
    private void issueEchoCmd() throws BRIWrapperException {
        out.println(CMD_ECHO_ON);
        String readerInput;
        try {
	        while ((readerInput = in.readLine()) != null) {
	            if (readerInput.equalsIgnoreCase(BRI_PROMPT)) {  // ready prompt
	                System.out.println(ECHO_MSG);
	                break;
	            }
	        }
        }
        catch (IOException ioe) {
        	throw new BRIWrapperException("Could not read input", ioe);
        }
    }
    
    public static TriggerController2 getInstance() throws BRIWrapperException {
        if (triggerController == null) triggerController = new TriggerController2();
        return triggerController;
    }
    
    public void listen() throws BRIWrapperException {
        try {
        	listening = true;
            triggerWait();
            String readerInput = null;
            while (((readerInput = in.readLine()) != null) && (listening)) {
                if (readerInput.length() == 0) continue;  // blank line
                if (!readerInput.startsWith(EVT_MSG)) continue;  // anything other than EVT
                notify(readerInput.substring(BRI_PROMPT.length()));
                triggerWait();
            }
        }
        catch (IOException ioe) {
            throw new BRIWrapperException("Could not read input", ioe);
        }
        finally {
        	listening = false;
        }
    }
    
    public void addTriggerListener(Trigger trigger, TriggerListenerInterface triggerListener) 
    	throws BRIWrapperException {
    	if (trigger == null)
    		throw new BRIWrapperException("Trigger argument was null");
    	if (triggerListener == null)
    		throw new BRIWrapperException("TriggerListnerInterface argument was null");
    	if (!triggerListeners.containsKey(trigger))
    		triggerListeners.put(trigger, new ArrayList());
    	ArrayList listeners = (ArrayList)triggerListeners.get(trigger);
    	listeners.add(triggerListener);
    	triggerSet(trigger);
    }
    
    private void checkSocket() throws BRIWrapperException {
        if (socket == null || in == null || out == null) {
            close(); // cleanup
            throw new BRIWrapperException("Wrapper lost connection");
        }
    }
    
    private void writePattern(int pattern) throws BRIWrapperException {
        checkSocket();
        if (pattern < 0 || pattern > 15)
            throw new BRIWrapperException("Pattern must be 0 - 15");
        out.println("writegpio = " + pattern);
    }
    
    private void triggerReset() throws BRIWrapperException {
        checkSocket();
        out.println("trigger reset");
    }
    
    private void triggerWait() throws BRIWrapperException {
        checkSocket();
        out.println("triggerwait");
    }
    
    private void triggerSet(Trigger trigger) throws BRIWrapperException {
    	if (trigger == null) throw new BRIWrapperException("Trigger argument was null");
        checkSocket();
        String name = trigger.getName();
        int mask = trigger.getMask();
        int switches = trigger.getSwitches();
        long delay = trigger.getDelay();
        out.println("trigger \"" + name + "\" gpio " + mask + " " + switches + " filter " + delay);
    }
    
    private void triggerKill(Trigger trigger) throws BRIWrapperException {
    	if (trigger == null) throw new BRIWrapperException("Trigger argument was null");
        checkSocket();
        String name = trigger.getName();
        if (name == null || name.equals(""))
            throw new BRIWrapperException("Trigger name cannot be null or empty.");
        out.println("trigger \"" + name + "\"");
    }
    
    private void triggerKillAll() throws BRIWrapperException {
        checkSocket();
        out.println("trigger reset");
    }
    
    private List identify() throws BRIWrapperException {
        checkSocket();
        if (socket == null || in == null || out == null) {
            close(); // cleanup
            throw new BRIWrapperException("Wrapper lost connection");
        }
        out.println("r");
        String readerInput;
        List tagIds = Collections.synchronizedList(new ArrayList());
        int blankLines = 0;
        try {
            while ((readerInput = in.readLine()) != null) {
                if (readerInput.length() == 0) {
                    blankLines++;
                    if (blankLines == 2) break; // no tags
                    continue;  // blank line
                }
                if (readerInput.endsWith("r")) continue;  // echo from reader
                if (readerInput.endsWith("quit")) continue; 
                if (readerInput.equalsIgnoreCase(BRI_ERROR)) continue;
                if (readerInput.equalsIgnoreCase(BRI_PROMPT)) break; // prompt by itself
                tagIds.add(readerInput); // anything is a tag id 
            }
        }
        catch (IOException ioe) {
            throw new BRIWrapperException("Could not read input", ioe);
        }
        return tagIds;
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
    
    private void notify(String evt) throws BRIWrapperException {
    	if (evt == null) throw new BRIWrapperException("String argument was null");
        Trigger trigger = parseTriggerEvent(evt);
        List listeners = (List)triggerListeners.get(trigger);
        if (listeners != null) {
	        TriggerListenerInterface tli = null;
	        for (int i = 0; i < listeners.size(); i++) {
	        	tli = (TriggerListenerInterface)listeners.get(i);
	        	tli.notify(new TriggerEvent(trigger));
	        }
        }
    }
    
    private Trigger parseTriggerEvent(String evt) throws BRIWrapperException {
    	if (evt == null) throw new BRIWrapperException("String argument was null");
    	StringTokenizer st = new StringTokenizer(evt);
    	if (st.countTokens() != 4) throw new BRIWrapperException("Malformed event");
    	st.nextToken(); // evt:trigger
    	String nameStr = st.nextToken();
    	st.nextToken(); // gpio
    	String switchStr = st.nextToken();
    	int switchVal = Integer.parseInt(switchStr);
    	return new Trigger(nameStr, 0, switchVal, 0L);
    }
    
    private static void storeProperties() throws BRIWrapperException {
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
            throw new BRIWrapperException("Could not store properties",ioe);
        }
    }
    
    private static void loadProperties() throws BRIWrapperException {
        Properties props = new Properties();
        File propsFile = new File(PROPS_FILE);
        try {
	        FileInputStream fis = new FileInputStream(propsFile);
	        props.load(fis);
	        ip = props.getProperty(IP_PROP);
	        port = Integer.parseInt(props.getProperty(PORT_PROP));
	        logging = Boolean.valueOf(props.getProperty(LOGGING_PROP)).booleanValue();
        }
        catch (IOException ioe) {
        	throw new BRIWrapperException("Could not load properties", ioe);
        }
    }

	public boolean isListening() {
		return listening;
	}

	public void setListening(boolean listening) {
		this.listening = listening;
	}

}

