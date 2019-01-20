package uk.co.indigo.play.rfid.queue;

import java.io.File;

public class DummyPrintJobProcessor implements FileProcessor {

	public DummyPrintJobProcessor() {
		super();
	}

	public void process(File file) throws FileProcessorException {
		System.out.println("File " + file + " processed.");
	}

}
