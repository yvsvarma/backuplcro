package com.oracle.hpcm.wstests;

import java.util.List;

import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.xml.XmlSuite;

public class MyReporterListenerAdapter implements IReporter {

	@Override
	public void generateReport(List<XmlSuite> xml, List<ISuite> suites, String outdir) {}
		// TODO Auto-generated method stub
		
}
