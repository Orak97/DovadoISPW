package logic.model;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Log {
	private static Log INSTANCE;
	private static final String LOGLEVEL = "INFO"; //Qui alternare INFO o WARNING a seconda di ciò che si vuole intercettare;
	private Logger logger;
	private FileHandler fh;
	private String fileName  = "log.txt";
	
	public static Log getInstance(){
		if(INSTANCE==null)
			INSTANCE = new Log();
		return INSTANCE;
	}
	
	public Logger getLogger() {
		return this.logger;
	}
	
	private Log() {
		try {
			File fileLog = new File(fileName);
				if (!fileLog.exists()) {
					System.out.println("il file non esiste");
					fileLog.createNewFile();
				}
		
		
		fh = new FileHandler(fileName, true);
		
		logger = Logger.getLogger("logProject");
		logger.addHandler(fh);
		SimpleFormatter formatter = new SimpleFormatter();
		fh.setFormatter(formatter);
		
		
		Level logLev;
		if (LOGLEVEL.equals("INFO")) {
			logLev = Level.INFO;
		} else logLev = Level.WARNING;
		
		logger.setLevel(logLev);
		
		} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
}
