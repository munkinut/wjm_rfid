package uk.co.indigo.play.rfid.queue;

import java.io.File;

public interface FileProcessor {
	
	public void process(File file) throws FileProcessorException;

}
