package com.oracle.pcmcs.logging;
/*
 * This class provides the template for the logging impl
 */
public interface ILogger {
	
	public void error(String message);
	public void warn(String message);
	public void info(String message);
	public void trace(String message);
	public void error(String message,Throwable t);
	public void warn(String message,Throwable t);
	public void info(String message,Throwable t);
	public void trace(String message,Throwable t);
    public void entry(Object... args);
    public void exit();
    public void exit(Object result);
}
