/*
 * Created on 15-Jul-2005
 *
 */
package uk.co.indigo.play.rfid.t6;

import java.util.ArrayList;
import java.util.List;

import com.intermec.datacollection.rfid.*;

/**
 * @author milbuw
 *
 */
public class T6Reader implements MessageDispatchEventListener {
    
    private static final String READER_ID = "MyIF5";
    private static final boolean USE_IP3 = false;
    private static final int TIMEOUT = 10;
    private static final boolean AUTONOMOUS = false;
    private static final String OPEN_ERROR = "Reader not open";
    private static final String IDENTIFY = "Identify";
    private static final String READ_ALL = "Read *";
    
    private Reader reader;
    
    public T6Reader() {
        reader = new Reader();
        reader.addMessageDispatchListener(this);
    }
    
    private Object[] listReaders() {
        List readers = new ArrayList();
		String readerName = reader.findFirstReader();
		while (readerName.length() > 0) {
		    readers.add(readerName);
			readerName = reader.findNextReader();
		}
		return readers.toArray();
    }
    
    public String checkReaderStatus() {
        return reader.getLastErrorMessage();
    }
    
    public boolean open() {
        boolean success = reader.open(READER_ID); 
        if (success) {
            reader.setUseIP3ScannerHandle(USE_IP3);
            reader.setCommunicationTimeout(TIMEOUT);
            reader.setAutonomousMode(AUTONOMOUS);
        }
        return success;
    }
    
    public boolean write(String message) {
        StringBuffer msgXMLBuf = new StringBuffer();
        msgXMLBuf.append("<WriteTags><Tag>");
        msgXMLBuf.append("<SWTTField EntryNr=\"0\">");
        msgXMLBuf.append(message);
        msgXMLBuf.append("</SWTTField>");
        msgXMLBuf.append("</Tag></WriteTags>");
        return reader.write(msgXMLBuf.toString());
    }
    
    public String identify() {
        return reader.command(IDENTIFY);
    }
    
    public String read() {
        return reader.command(READ_ALL);
    }
    
    public void close() {
        reader.close();
        reader.dispose();
    }

    public static void main(String[] args) {
        T6Reader t6Reader = new T6Reader();
        if (t6Reader.open()) {
            System.out.println(t6Reader.identify());
            System.out.println("Reader status : " + t6Reader.checkReaderStatus());
            //t6Reader.write("Wello Horld !  ");
            System.out.println(t6Reader.read());
            System.out.println("Reader status : " + t6Reader.checkReaderStatus());
            t6Reader.close();
        }
        else System.out.println(OPEN_ERROR);
    }
    
    public void rfidEventRead(MessageDispatchEvent evt) {
        System.out.println(evt.getMessageData());
    }
}
