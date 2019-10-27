EPM Automate Automation Readme

This document provides instructions to set up environment for executing the test scripts developed for profitability using selenium.

PREREQUISITES:

1. The following modules are to be installed and configured before starting the test execution.
	- JDK (Jdk 1.7.0_51 or above)
2. Delete all ML applications on the PCMCS environment.
3. Open the EPMAutomate_TestSuite.bat file and change the JAVA_HOME: to the location where JDK is available.
4. All the jar files required for execution are available in libs folder.

TEST ENVIRONMENT INFORMATION:

The information related to the test environment is required.
	- url: http://slc10vic.us.oracle.com:9000 or https://pcmcs-test-domain.us.oracle.com.
	- id: Login ID
	- password: Login Password
	- domain : if running on adc pod, provide the domain, else false
EXECUTION:

1. From the command line execute the following command to perform required actions.
	EPMAutomate_TestSuite.bat url id password "path" domain
	
ex. EPMAutomate_TestSuite.bat http://slc10vic.us.oracle.com:9000 epm_default_cloud_admin password1 false
	EPMAutomate_TestSuite.bat https://pcmcs-test-usinternalops65151.stg-epm.us1.oraclecloud.com ServiceInstanceAdmin Welc0me! usinternalops65151
	path should be in double quotes.