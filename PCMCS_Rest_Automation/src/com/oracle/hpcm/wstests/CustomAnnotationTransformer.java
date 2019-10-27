package com.oracle.hpcm.wstests;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

public class CustomAnnotationTransformer implements IAnnotationTransformer {
	@Override
	 public void transform(ITestAnnotation annotation, Class testClass,Constructor testConstructor, Method testMethod)   {

	   
	   if(testMethod.getName().equals("testDeleteProfitabilityApp") && System.getProperty("predeleteapp").equals("false")){
		   annotation.setEnabled(false);
		   annotation.setTimeOut(Long.parseLong(System.getProperty("epmadeploytimeout"), 10));
	   }
	   
	   if (testMethod.getName().equals("testDeployProfitablityApp") && System.getProperty("epmadeploy").equals("false")) {
		   annotation.setEnabled(false);
		   annotation.setTimeOut(Long.parseLong(System.getProperty("epmadeploytimeout"), 10));
	   }
	   
	   if(testMethod.getName().equals("testLCMImport") && System.getProperty("lcmimport").equals("false")){
		   annotation.setEnabled(false);
		   annotation.setTimeOut(Long.parseLong(System.getProperty("lcmimporttimeout"), 10));
	   }
	   
	   if(testMethod.getName().equals("testDeployCube") && System.getProperty("cubedeploy").equals("false")){
		   annotation.setEnabled(false);
		   annotation.setTimeOut(Long.parseLong(System.getProperty("cubedeploytimeout"), 10));
	   }
	   
	   if(testMethod.getName().equals("testLoadData") && System.getProperty("loaddata").equals("false")){
		   annotation.setEnabled(false);
	   }
	   
	   
	   if(testMethod.getName().equals("testRunCalcJob") && System.getProperty("runcalculation").equals("false")){
		   annotation.setEnabled(false);
		   annotation.setTimeOut(Long.parseLong(System.getProperty("runcalculationtimeout"), 10));
	   }
	   
	   
	   if(testMethod.getName().equals("testDeleteEssAppPost") && System.getProperty("postdeletecube").equals("false")){
		   annotation.setEnabled(false);
	   }
	   if(testMethod.getName().equals("testCheckCalculation") && System.getProperty("checkcalculations").equals("false")){
		   annotation.setEnabled(false);
	   }
	   if(testMethod.getName().equals("testExportData") && System.getProperty("checkcubeexport").equals("false")){
		   annotation.setEnabled(false);
	   }
	   if(testMethod.getName().equals("testDeleteProfitabilityAppPost") && System.getProperty("postdeletecube").equals("false")){
		   annotation.setEnabled(false);
	   }
	 }

}

