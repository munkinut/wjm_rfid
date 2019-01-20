/*
 * Created on 20-Jul-2005
 *
 */
package uk.co.indigo.play.rfid.bri;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;

/**
 * @author milbuw
 *
 */
public class BRIConsoleThreaded {
    
    private static String ip;
    private static int port;
    private static String PROPS_FILE = "conf\\BRIConsole.properties";
    private static String PROPS_TITLE = "BRI Console Properties";
    private static String PORT_PROP = "port";
    private static String IP_PROP = "ip";
    private static String CMD_QUIT = "quit";
    private static String CMD_HELP = "help";
    private static String MY_PROMPT = "KO>";
    private static String ECHO_MSG = "ECHO is ON";
    private static String READER_THREAD_NAME = "rt_01";
    private static String CMD_ATTRIB = "attrib echo=on";
    private static String BLANK_INPUT = "";
    private static String BRI_PROMPT = "OK>";
    private static String ERR_MSG = "ERR";

    public static void main(String[] args) {
        
        Socket socket = null;
        
        try {
            // get ip and port of BRI server and connect
            loadProperties();
            socket = new Socket(ip, port);
            // setup readers and writer
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            // display title banner
            banner();
            // send reader an attrib command to switch echo on
            out.println(CMD_ATTRIB);
            String readerInput;
            // wait for a prompt from the reader then carry on
            while ((readerInput = in.readLine()) != null) {
                if (readerInput.equalsIgnoreCase(BRI_PROMPT)) {  // ready prompt
                    System.out.println(ECHO_MSG);
                    System.out.println();
                    break;
                }
            }
            // start listening for messages
            ReaderThread t = new ReaderThread(READER_THREAD_NAME, in, BRI_PROMPT, ERR_MSG);
            t.start();
            // prompt user for commands
            prompt();
            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                if (userInput.equalsIgnoreCase(CMD_QUIT)) break;
                if (userInput.equalsIgnoreCase(CMD_HELP)) {
                    help();
                    prompt();
                    continue;
                }
                if (userInput.equals(BLANK_INPUT)) {
                    prompt();
                    continue;
                }
                // it wasn't a quit or help command so pass to the reader
                // and allow us to carry on
                out.println(userInput);
                prompt();
            }
            // quit command was called, tell message listener to stop
            t.stopRunning();
            // provoke an ERR message from the reader
            out.println(CMD_QUIT);
            // clean up
            in.close();
            out.close();
            socket.close();
        }
        catch (UnknownHostException uhe) {
            System.err.println(uhe);
            System.exit(1);
        }
        catch (IOException ioe) {
            System.err.println(ioe);
            try {
                if (socket != null) socket.close();
            }
            catch (IOException ioe2) {
                System.err.println("Socket close failed.");
            }
            System.exit(1);
        }
    }
    
    private static void banner() {
        System.out.println("Java BRI Console (c) 2005 Indigo Software");
        System.out.println("Type help for console commands");
        System.out.println();
    }
    
    private static void help() {
        System.out.println("quit - quit");
        System.out.println("anything else - gets sent to the reader");
    }
    
    private static void prompt() {
        System.out.print(MY_PROMPT);
    }
    
    private static void storeProperties() throws FileNotFoundException, IOException {
        Properties props = new Properties();
        props.setProperty(IP_PROP,ip);
        props.setProperty(PORT_PROP, Integer.toString(port));
        File propsFile = new File(PROPS_FILE);
        try {
            FileOutputStream fos = new FileOutputStream(propsFile);
            props.store(fos, PROPS_TITLE);
        }
        catch (FileNotFoundException fnfe) {
            System.err.println(fnfe);
        }
        catch (IOException ioe) {
            System.err.println(ioe);
        }
    }
    
    private static void loadProperties() throws FileNotFoundException, IOException {
        Properties props = new Properties();
        File propsFile = new File(PROPS_FILE);
        FileInputStream fis = new FileInputStream(propsFile);
        props.load(fis);
        ip = props.getProperty(IP_PROP);
        port = Integer.parseInt(props.getProperty(PORT_PROP));
    }
    
}

class ReaderThread extends Thread {
    
    private BufferedReader in;
    private String prompt;
    private boolean running;
    private String errMsg;
    private static String TRIG_PATTERN = "EVT:TRIGGER";
    
    public ReaderThread(String name, BufferedReader in, String prompt, String errMsg) {
        super(name);
        this.in = in;
        this.prompt = prompt;
        this.errMsg = errMsg;
        this.running = false;
    }
    
    public void run() {
        running = true;
        String readerInput;
        try {
            // wait for messages from reader
            // as long as stopRunning() has not been called
            String trigPrompt = prompt.concat(TRIG_PATTERN);
            while (running && ((readerInput = in.readLine()) != null)) {
                
                //System.out.println("*" + readerInput + "*");
                // ignore reader prompts and blank lines
                if (readerInput.length() == 0) continue; // blank
                if (readerInput.startsWith(trigPrompt)) 
                    readerInput = readerInput.substring(prompt.length());
                else if (readerInput.startsWith(prompt)) continue;
                // print anything else
                System.out.println(readerInput);
            }
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
    
    public void stopRunning() {
        // will kill the thread on next message from reader
        running = false;
    }
    
}

