package com.oracle.hpcm.wstests;
import java.util.List;
import java.util.Map;

import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;

public class CustomReporter implements IReporter{
    @Override
    public void generateReport(List xmlSuites, List suites,
        String outputDirectory) {
        //Iterating over each suite included in the test
        for (Object suite : suites) {
            //Following code gets the suite name
            String suiteName = ((ISuite)suite).getName();
	    //Getting the results for the said suite
	    Map suiteResults =((ISuite) suite).getResults();
	    for (Object sr : suiteResults.values()) {
	        ITestContext tc =((ISuiteResult)sr).getTestContext();
	        System.out.println("Passed tests for suite '" + suiteName +
	             "' is:" + tc.getPassedTests().getAllResults().size());
	        System.out.println("Failed tests for suite '" + suiteName +
	             "' is:" + 
	             tc.getFailedTests().getAllResults().size());
	        System.out.println("Skipped tests for suite '" + suiteName +
	             "' is:" + 
	             tc.getSkippedTests().getAllResults().size());
	      }
        }
    }
}
