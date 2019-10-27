PCMCS Engine Automation Readme

This document provides instructions to set up environment for executing the test automation developed for Engine automation.

PREREQUISITES:

1. The following modules are to be installed and configured before starting the test execution.
	- JDK (Jdk 1.7.0_51 or above)
2. Open the SingleApplicationEngineTest.bat/MultipleApplicationsEnginetest.bat file and change the JAVA_HOME: to the location where JDK is available.
3. All the jar files required for execution are available in libs folder.

TEST ENVIRONMENT INFORMATION:

The information related to the test environment is required.
	- url: http://slc10vic.us.oracle.com:9000 or https://pcmcs-test-domain.us.oracle.com.
	- id: Login ID
	- password: Login Password
	- domain : if running on adc pod, provide the domain, else false
	-timeout : in seconds, higher value on ADC pods(>1000)

EXECUTION:

1. For testing single app, execute the following command to perform required actions.
	
	SingleApplicationEngineTest.bat url port userid password isADC domain timeout modelName applicationName 
	url : in format of slc10zwb.us.oracle.com.
	port : Always provide a value. it wont be used on ADC pods.
	userid : user id,(must be Service Instance Admin)
	password : password
	isADC : true if system is an ADC pod, else false
	domain : Always provide a value. it will be used only on ADC pods.
	timeout : in seconds
	modelName : LMA,LM1,LM2,LM3,LM4,LM5,BML12,BML30
	applicationName : Application name 
	
ex. SingleApplicationEngineTest.bat slc10zwb.us.oracle.com 9000 epm_default_cloud_admin password1 false none 1000 BML12 RBML12 
	
2. For testing multiple apps one by one, execute the following command to perform required actions.
	
	SingleApplicationEngineTest.bat url port userid password isADC domain timeout modelName applicationName 
	url : in format of slc10zwb.us.oracle.com.
	port : Always provide a value. it wont be used on ADC pods.
	userid : user id,(must be Service Instance Admin)
	password : password
	isADC : true if system is an ADC pod, else false
	domain : Always provide a value. it will be used only on ADC pods.
	timeout : in seconds

	
ex. MultipleApplicationsEngineTest.bat pcmcs-usinternalops65151.stg-epm.us1.oraclecloud.com 9000 ServiceInstanceAdmin Welc0me! true usinternalops65151 2000 

Test Results 

Check the testng test results at ./test-output/index.html.
	