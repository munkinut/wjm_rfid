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
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * @author milbuw
 *
 */
public class TriggerController {
    
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
    private static boolean logging;
    
    private static TriggerController triggerController = null;
    private Socket socket = null;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private TriggerListener triggerListener;
    
    private TriggerController() throws BRIWrapperException {
        try {
            loadProperties();
            initSocket();
            initListener();
            issueEchoCmd();
        }
        catch (IOException ioe) {
            throw new BRIWrapperException("IO exception during construction", ioe);
        }
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
    
    private void issueEchoCmd() throws IOException {
        out.println(CMD_ECHO_ON);
        String readerInput;
        while ((readerInput = in.readLine()) != null) {
            if (readerInput.equalsIgnoreCase(BRI_PROMPT)) {  // ready prompt
                System.out.println(ECHO_MSG);
                break;
            }
        }
    }
    
    private void initListener() {
    }
    
    public static TriggerController getInstance() throws BRIWrapperException {
        if (triggerController == null) triggerController = new TriggerController();
        return triggerController;
    }
    
    public void listen() {
        triggerListener = new TriggerListener(this, LISTENER_NAME, in, BRI_PROMPT, BRI_ERROR);
        triggerListener.start();
    }
    
    private void checkSocket() throws BRIWrapperException {
        if (socket == null || in == null || out == null) {
            close(); // cleanup
            throw new BRIWrapperException("Wrapper lost connection");
        }
    }
    
    public void blinkenlights(int times, long delay) throws BRIWrapperException {
        checkSocket();
        for (int j = 0; j < times; j++) {
	        for (int i = 15; i >= 0; i--) {
	            out.println("writegpio = " + i);
	            try {
	                Thread.sleep(delay);
	            }
	            catch (InterruptedException ie) {
	                // Do nothing!
	            }
	        }
        }
    }
    
    public void writePattern(int pattern) throws BRIWrapperException {
        checkSocket();
        if (pattern < 0 || pattern > 15)
            throw new BRIWrapperException("Pattern must be 0 - 15");
        out.println("writegpio = " + pattern);
    }
    
    public void triggerReset() throws BRIWrapperException {
        checkSocket();
        out.println("trigger reset");
    }
    
    public void triggerWait() throws BRIWrapperException {
        checkSocket();
        out.println("triggerwait");
    }
    
    public void triggerSet(String name, int switches, long filterDelay) throws BRIWrapperException {
        checkSocket();
        if ((switches < 0) || (switches > 15))
            throw new BRIWrapperException("Switch value must be between 0 and 15.");
        out.println("trigger \"" + name + "\" gpio 15 " + switches + " filter " + filterDelay);
    }
    
    public void triggerKill(String name) throws BRIWrapperException {
        checkSocket();
        if (name == null || name.equalsIgnoreCase(""))
            throw new BRIWrapperException("Trigger name cannot be null or empty.");
        out.println("trigger \"" + name + "\"");
    }
    
    public void triggerKillAll() throws BRIWrapperException {
        checkSocket();
        out.println("trigger reset");
    }
    
    public List identify() throws BRIWrapperException {
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
                //System.out.println("IDENTIFY:" + readerInput + "**");
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
            throw new BRIWrapperException(ioe);
        }
        return tagIds;
    }
    
    public void close() {
        stopListening();
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        }
        catch (IOException ioe) {
            // something couldnt close, forget about it
        }
    }
    
    private void stopListening() {
        if (triggerListener != null) {
            triggerListener.stopRunning();
            out.println(CMD_QUIT);
            triggerListener = null;
        }
    }
    
    protected void notify(String evt) {
        System.out.println(evt);
        try {
            //stopListening();
            //blinkenlights(1,500);
            
            List tagIds = identify();
            int tags = tagIds.size();
            String tagId;
            for (int i = 0; i < tags; i++) {
                tagId = (String)tagIds.get(i);
                System.out.println(tagId);
            }
            if (tags < 2) writePattern(14);
            else writePattern(13);
            
            listen();
            triggerWait();
        }
        catch (BRIWrapperException bwe) {
            System.err.println(bwe.getMessage());
        }
    }
    
    public static void main(String[] args) {
        try {

            TriggerController triggerController = TriggerController.getInstance();
            triggerController.triggerSet("mytrig", 14, 100);
            triggerController.listen();
            triggerController.triggerWait();
            while (true) {}
            
        }
        catch (BRIWrapperException bwe) {
            System.err.println(bwe);
        }
        finally {
            triggerController.close();
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

class TriggerListener extends Thread {
    
    private TriggerController triggerController;
    private BufferedReader in;
    private String prompt;
    private boolean running;
    private String errMsg;
    private String TRIG_EVENT = "EVT:TRIGGER";
    
    public TriggerListener(TriggerController triggerController, String name, BufferedReader in, String prompt, String errMsg) {
        super(name);
        this.triggerController = triggerController;
        this.in = in;
        this.prompt = prompt;
        this.errMsg = errMsg;
        this.running = false;
    }
    
    public void run() {
        running = true;
        String readerInput = null;
        try {
            String eventMsg = prompt.concat(TRIG_EVENT);
            while (running && ((readerInput = in.readLine()) != null)) {
                if (readerInput.length() == 0) continue;  // blank line
                if (!readerInput.startsWith(eventMsg)) continue;  // anything other than EVT
                break;
            }
            running = false;
            triggerController.notify(readerInput.substring(prompt.length()));
        }
        catch (IOException ioe) {
            System.err.println("Reader thread ending.");
            try {
                if (in != null) in.close();
            }
            catch (IOException ioe2) {
                System.err.println("Reader thread closed.");
            }
        }
    }
    
    public boolean running() {
        return running;
    }
    
    public void stopRunning() {
        // will kill the thread on next message from reader
        running = false;
    }
    
}
