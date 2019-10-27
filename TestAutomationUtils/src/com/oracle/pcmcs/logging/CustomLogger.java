package com.oracle.pcmcs.logging;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.PropertyConfigurator;


public class CustomLogger implements ILogger{
	
	private static final String ENTRY_MESSAGE = "Entering ";
	private static final String ARG_SEPARATOR = " - ";
	private static final String WITH_END = ")";
	private static final Object WITH_START = "(";
	private final Class component;
	private final org.apache.log4j.Logger logger;
	private CustomLogger(Class component){
		PropertyConfigurator.configure("./log4j.properties");
		this.component = component;
		this.logger = org.apache.log4j.Logger.getLogger(component.getName());
	}
	
	
	public Class getComponent() {
		return component;
	}
	
	@Override
	public void error(String message) {
		logger.error(message);
		
	}

	@Override
	public void warn(String message) {
		logger.warn(message);
		
	}

	@Override
	public void info(String message) {
		logger.info(message);
		
	}

	@Override
	public void trace(String message) {
		logger.info(message);
		
	}



	@Override
	public void error(String message, Throwable t) {
        logger.error(message,t);
		
	}

	@Override
	public void warn(String message, Throwable t) {
        logger.warn(message,t);
		
	}

	@Override
	public void info(String message, Throwable t) {
        logger.info(message,t);
		
	}

	@Override
	public void trace(String message, Throwable t) {
		 logger.trace(message,t);
		 
	}

	@Override
	public void entry(Object... args) {
		 StringBuilder message = new StringBuilder(ENTRY_MESSAGE);
		 if (args != null && args.length > 0) {
             message.append(WITH_START).append(StringUtils.join(args, ARG_SEPARATOR)).append(WITH_END);
         }
         logger.info(message.toString());
		
	}

	@Override
	public void exit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exit(Object result) {
		// TODO Auto-generated method stub
		
	}


	public static ILogger getLogger(Class component) {
		// TODO Auto-generated method stub
		return new CustomLogger(component);
	}

}
