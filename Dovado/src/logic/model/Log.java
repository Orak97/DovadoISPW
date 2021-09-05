package logic.model;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Log {
	private static Log INSTANCE;
	private static final String FILENAME  = "log.txt";
	
	private Logger logger;
	private FileHandler fh;
	
	
	public static Log getInstance(){
		if(INSTANCE==null)
			INSTANCE = new Log();
		return INSTANCE;
	}
	
	private Log() {
		try {
			File fileLog = new File(FILENAME);
			if (!fileLog.exists()) {			
				if(!fileLog.createNewFile()) {
					throw new IOException("Errore nella creazione del file "+ FILENAME);
				}
			}
		
		
			fh = new FileHandler(FILENAME, true);
		
			logger = Logger.getLogger("logProject");
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);
		
			logger.setLevel(Level.INFO);
		
		} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public Logger getLogger() {
		return this.logger;
	}
		
}
