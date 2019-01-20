package uk.co.indigo.play.rfid.queue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Monitors a queue directory for new files, passes them to a processor then
 * moves them to an archive of processed files.
 *  
 * @author milbuw
 */
public class DirQueueMonitor implements Runnable {
	
	/**
	 * The default queue E.g. \/temp or C:\\temp.
	 */
	public static final String DEFAULT_QUEUE = File.separator + "temp";
	/**
	 * The default archive for processed files.
	 */
	public static final String DEFAULT_ARCHIVE = File.separator + "archive";
	/**
	 * The minimum cycle time for monitoring in milliseconds.
	 */
	public static final long MIN_DUTY_CYCLE = 1000;
	
	public static final long DEFAULT_DUTY_CYCLE = 10000;

	private File queueDir;
	private File archiveDir;
	private FileProcessor fileProcessor;
	private boolean running;
	private long dutyCycle;
	
	/**
	 * Creates an XMLPrintJobProcessor and starts a queue monitor in a thread
	 * which cycles every 10 seconds.
	 * 
	 * @param args No args required.
	 */
	public static void main(String[] args) {
		FileProcessor fp = new XMLPrintJobProcessor();
		//FileProcessor fp = new DummyPrintJobProcessor();
		DirQueueMonitor dqm = new DirQueueMonitor(fp, 
												  new File(DEFAULT_QUEUE), 
												  new File(DEFAULT_ARCHIVE), 
												  DEFAULT_DUTY_CYCLE);
		Thread queueMon = new Thread(dqm);
		queueMon.start();
		dqm.setRunning(true);
		// RUN FOR 60 MINS
		try {
			Thread.sleep(60000 * 60);
			dqm.stop();
		}
		catch (InterruptedException ie) {
			// do nothing
		}
	}

	/**
	 * Constructs a new DirQueueMonitor.
	 * 
	 * @param fileProcessor - Something to filter the files appearing in the queue directory.
	 * @param queueDir - The queue directory.
	 * @param archiveDir - The archive directory for processed files.
	 * @param dutyCycle - How often to cycle in milliseconds.
	 */
	public DirQueueMonitor(FileProcessor fileProcessor, File queueDir, File archiveDir, long dutyCycle) {
		super();
		if (dutyCycle < MIN_DUTY_CYCLE) this.dutyCycle = MIN_DUTY_CYCLE;
		else this.dutyCycle = dutyCycle;
		this.fileProcessor = fileProcessor;
		if (queueDir.exists() &&
		    queueDir.isDirectory())
		   		this.queueDir = queueDir;
		else throw new IllegalArgumentException("Queue directory " + queueDir + " does not exist.");
		if (archiveDir.exists() &&
			    archiveDir.isDirectory())
			   		this.archiveDir = archiveDir;
			else throw new IllegalArgumentException("Archive directory " + archiveDir + " does not exist.");
	}

	/**
	 * Implementation of Runnable.run() - Gets a list of files in the queue dir, sorted by date.
	 * Passes them to the configured processor then moves them to the archive directory.
	 */
	public void run() {
		while (running) {
			System.out.println("Checking for files...");
			File[] sortedFiles = getSortedFileList(queueDir);
			File file;
			for (int i = 0; i < sortedFiles.length; i++) {
				file = sortedFiles[i];
				try {
					processFile(file);
					moveFile(file, archiveDir);
				}
				catch (FileProcessorException fpe) {
					System.err.println(fpe.getMessage());
				}
				catch (IOException ioe) {
					System.err.println(ioe.getMessage());
				}
			}
			try {
				Thread.sleep(dutyCycle);
			}
			catch (InterruptedException ie) {
				break;
			}
		}
	}
	
	/**
	 * Indicates a stop request to the thread.  Thread will die on next duty cycle.
	 */
	public void stop() {
		setRunning(false);
	}
	
	/**
	 * Gets a sorted list of files in the queue directory.
	 * 
	 * @param dir The queue directory.
	 * @return A date ordered array of File objects representing files in the queue.
	 */
	private File[] getSortedFileList(File dir) {
		File[] files = dir.listFiles();
		Arrays.sort(files, new Comparator() {
			public int compare(Object o1, Object o2) {
				File f1 = (File) o1;
				File f2 = (File) o2;
				return (int) (f1.lastModified() - f2.lastModified());
			}
		});
		return files;
	}
	
	/**
	 * Proxy method that passes a file to the configured processor.
	 * 
	 * @param file The file to process.
	 * @throws FileProcessorException Thrown if the processor fails.
	 */
	private void processFile(File file) throws FileProcessorException {
		fileProcessor.process(file);
	}
	
	/**
	 * Moves processed file to the archive directory.
	 * 
	 * @param toMove The file to move.
	 * @param dest The archive directory.
	 * @throws IOException Thrown if the move failed.
	 */
	private void moveFile(File toMove, File dest) throws IOException {
	    boolean success = toMove.renameTo(new File(dest, toMove.getName()));
	    if (!success) {
	        throw new IOException("File move for " + toMove + " failed");
	    }
	}

	/**
	 * Determines whether or not the thread should continue based on the state of the running flag.
	 * 
	 * @return True if running flag is set true.
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * Sets the state of the running flag.
	 * 
	 * @param running True to set running flag, otherwise false.
	 */
	public void setRunning(boolean running) {
		this.running = running;
	}

}
