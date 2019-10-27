package com.oracle.hpcm.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import com.oracle.hpcm.utils.logging.LogFormatter;

public class LogUtil {
	public static StringBuilder logText = new StringBuilder();
	public static boolean logInit = false;
	public static Logger logger;

	public static void print(String op) {
		System.out.println(op);
		logText.append("\n");
		logText.append(op);
	}

	public static void wait(int time) {
		try {
			print("Waiting for " + time + " seconds.");
			Thread.currentThread();
			Thread.sleep(time * 1000); // sleep for 1000 * time ms
		} catch (InterruptedException ie) {
		}
	}

	public static Logger getLogger() throws SecurityException, IOException {

		System.out.println("Logger get called = " + logInit);
		return logger;

	}

	public static void setupLogger() {
		logInit = true;
		logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

		try {

			FileInputStream fis = new FileInputStream("./conf/log4j.properties");
			LogManager.getLogManager().readConfiguration(fis);
			logger.addHandler(new java.util.logging.ConsoleHandler());
			logger.setUseParentHandlers(false);
			logger.info("log setup done.");
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws SecurityException,
			IOException {
		Logger logger = Logger.getLogger("");
		logger.setUseParentHandlers(false);
		ConsoleHandler consoleHandler = new ConsoleHandler();
		consoleHandler.setFormatter(new LogFormatter());
		logger.addHandler(consoleHandler);
		FileHandler filehandler = new FileHandler("myapp-log.txt");
		filehandler.setFormatter(new LogFormatter());
		logger.addHandler(filehandler);
		logger.setLevel(Level.INFO);
		logger.log(Level.INFO, "Severe message....");
		logger.log(Level.INFO, "Config Message....");

	}
}
