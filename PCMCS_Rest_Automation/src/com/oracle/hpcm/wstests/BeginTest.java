package com.oracle.hpcm.wstests;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.io.FileUtils;
import org.testng.IReporter;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;

import com.oracle.hpcm.utils.LogUtil;
import com.oracle.hpcm.utils.ZipUtil;

public class BeginTest {
	public static Logger logger;

	public static void main(String[] args) {
		String testResultText = "";
		boolean testResult = false;
		System.setProperty("logging.filepath", "./logs/wslogs.log");
		TestNG testng = new TestNG();
		testng.setDefaultSuiteName("Rest Cloud Webservices Test Suite");
		try {
			clean();
			LogUtil.setupLogger();
			logger = Logger.getLogger("BeginTest");

			logger.log(Level.INFO,
					"============Rest Cloud WebServices Regression Automation Run==============");
			System.setProperty("http.proxyHost", "127.0.0.1");
			System.setProperty("https.proxyHost", "127.0.0.1");
			System.setProperty("http.proxyPort", "8888");
			System.setProperty("https.proxyPort", "8888");
			processArgs(args);
			printProperties();

			// System.exit(0);
			TestListenerAdapter tla = new TestListenerAdapter();
			CustomEmailableReporter cer = new CustomEmailableReporter();
			testng.setTestClasses(new Class[] { CloudTestFixture.class});
			testng.addListener(tla);
			testng.addListener(cer);
			for( IReporter reporter : testng.getReporters())
				logger.info(reporter.getClass().getCanonicalName());
			
			testng.run();
			int failCount = tla.getFailedTests().size();
			int passCount = tla.getPassedTests().size();
			int skipCount = tla.getSkippedTests().size();
			int totalCount = failCount + passCount + skipCount;
			testResultText =

			"Total tests run: " + totalCount + ", Pass: " + passCount
					+ "  Failures: " + failCount + "  Skiped: " + skipCount;
			;
			logger.log(Level.INFO, testResultText);

		} catch (Exception ex) {
			StringWriter errors = new StringWriter();
			ex.printStackTrace(new PrintWriter(errors));
			logger.log(Level.SEVERE, errors.toString());

		} finally {
			for (Handler handle : logger.getHandlers()) {
				handle.close();
			}
			new ZipUtil("./test-output", System.getProperty("testreport.zip"))
					.zipper();

			testResult = !testng.hasFailure();
			logger.log(Level.INFO, "Is Test passed: " + testResult);

			logger.log(Level.INFO, "Finished.");
			String status = testResult ? "Passed." : "Failed.";
			if (System.getProperty("sendmail").equals("true"))
				sendMail(
						System.getProperty("mailto"),
						"Rest Webservice Automation" + " on "
								+ System.getProperty("server")
								+ " : Job Run Status : " + status);
			if (testResult == true)
				System.exit(0);
			else
				System.exit(-1);
		}
	}

	static void printProperties() {

		logger.log(Level.INFO, "---------Properties------");

		String[] propNames = { "server", "user", "password", "port",
				"deleteport", "sendmail", "lcmimporttimeout",
				"epmadeploytimeout", "cubedeploytimeout",
				"runcalculationtimeout", "mailto", "apptype", "apsserver","version" };

		for (String propname : propNames) {

			logger.log(Level.INFO,
					propname + ": " + System.getProperty(propname));
		}
	}

	static void clean() throws IOException {
		File logFolder = new File("./logs");
		if (logFolder.exists()) {
			FileUtils.cleanDirectory(logFolder);
		} else {
			logFolder.mkdir();
		}
		File testngFolder = new File("./test-output");
		if (testngFolder.exists()) {
			FileUtils.cleanDirectory(testngFolder);
		} else {
			testngFolder.mkdir();
		}
	}

	static void processArgs(String[] args) throws Exception {

		// setup command line options

		Options options = new Options();
		options.addOption("server", true,
				"URL of the server e.g. slc04ssp.us.oracle.com.");
		options.addOption("user", true, "User ID.");
		options.addOption("password", true, "Password.");
		options.addOption("epmapiversion", true, "epm automate API version e.g. 11.1.2.3.600");
		options.addOption("port", true, "Port of the application e.g. 19000.");
		options.addOption("deleteport", true, "Delete WLS port.");
		options.addOption("e2e", true,
				"E2E covers all webservices of app type.");
		options.addOption("sendmail", true, "send mail option.");
		options.addOption("lcmimporttimeout", true, "LCM Import Timeout.");
		options.addOption("epmadeploytimeout", true, "EPMA Deploy Timeout.");
		options.addOption("cubedeploytimeout", true, "Cube Deploy Timeout.");
		options.addOption("version", true, "Webservice Version e.g. v1, 11.1.2.4.000 .");
		options.addOption("runcalculationtimeout", true,
				"Calculation Run Timeout.");
		options.addOption("checkcalculations", true, "Calculation Run Timeout.");
		options.addOption("mailto", true,
				"Mail addresses seperated by semicolon.");
		options.addOption("apsserver", true,
				"server of essabse if distributed environment. ");
		options.addOption("help", false, "Help.");
		CommandLineParser parser = new BasicParser();
		CommandLine cmd = parser.parse(options, args);
		if (cmd.hasOption("help")) {
			// print the help and exit
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("RestWebSericesTest", options);
			System.exit(0);
		}

		// Now process the arguments
		processGlobalProperties(cmd);
		processModelProperties(cmd);

		/* Setup timeouts */
		// do it for later
	}

	static void processGlobalProperties(CommandLine cmd) {
		// check if global.properties exist or not
		boolean doesGlobalPropertyFileExists = false;
		Properties globalProp = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream("./conf/global.properties");
			globalProp.load(input);
			doesGlobalPropertyFileExists = true;

		} catch (IOException ex) {
			doesGlobalPropertyFileExists = false;
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// Read properties from command line

		String[] requiredPropertiesNamesList = { "server", "user", "password",
				"port", "deleteport" ,"version","epmapiversion"};

		for (String propertyName : requiredPropertiesNamesList) {
			String propValueFromCommandLine = cmd.getOptionValue(propertyName);

			if (propValueFromCommandLine != null) {
				System.setProperty(propertyName, propValueFromCommandLine);
			} else {
				if (propValueFromCommandLine == null
						&& doesGlobalPropertyFileExists
						&& globalProp.getProperty(propertyName) != null) {
					logger.log(
							Level.INFO,
							"Required "
									+ propertyName
									+ " is not provided as an argument. Defaulting to global.properties value. ");
					System.setProperty(propertyName,
							globalProp.getProperty(propertyName));
				} else {
					logger.log(
							Level.INFO,
							"Required "
									+ propertyName
									+ " is neither provided as an argument nor is present in global.properties value. Exiting.");
					System.exit(1);
				}
			}

		}

		// setup essabse server properties
		String apsserver = cmd.getOptionValue("apsserver");
		if (apsserver != null) {
			logger.log(Level.INFO,
					"Required essbase server is provided as an argument.");
			System.setProperty("apsserver", apsserver);
		} else {
			logger.log(
					Level.INFO,
					"Required essbase server is not provided as an argument. Defaulting to server name.");
			System.setProperty("apsserver", System.getProperty("server"));
		}
		// setup mailto and sendmail properties

		String propValueFromCommandLine = cmd.getOptionValue("sendmail");
		if (propValueFromCommandLine != null) {
			if (propValueFromCommandLine.equalsIgnoreCase("true")) {
				System.setProperty("sendmail", "true");
			} else {
				System.setProperty("sendmail", "false");
			}
		} else {
			String propValueFromGlobalFile = globalProp.getProperty("sendmail");
			if (propValueFromGlobalFile == null) {
				logger.log(
						Level.INFO,
						"Required sendmail property is neither provided as an argument nor is present in global.properties value. Defaulting to false.");
				System.setProperty("sendmail", "false");
			} else {
				logger.log(
						Level.INFO,
						"Required sendmail property is not provided as an argument. defaulting to global.properties value.");
				if (propValueFromGlobalFile.equalsIgnoreCase("true")) {
					System.setProperty("sendmail", "true");
				} else {
					System.setProperty("sendmail", "false");
				}
			}
		}
		// setup mailto based on the sendmail property
		if (System.getProperty("sendmail").equals("true")) {
			propValueFromCommandLine = cmd.getOptionValue("mailto");
			if (propValueFromCommandLine != null) {
				System.setProperty("mailto", propValueFromCommandLine);
			} else {
				String propValueFromGlobalFile = globalProp
						.getProperty("mailto");
				if (propValueFromGlobalFile == null) {
					logger.log(
							Level.INFO,
							"Required mailto property is neither provided as an argument nor is present in global.properties value. Defaulting to noreply@oracle.com");
					System.setProperty("mailto", "noreply@oracle.com");
				} else {
					System.setProperty("mailto", propValueFromGlobalFile);
				}
			}
		} else {
			logger.log(
					Level.INFO,
					"Sendmail property is set to false. Defaulting mailto to null as a mail will not be sent.");
			// do nothing, as a subsequent test for this property will result in
			// a null object
		}

	}

	static void processModelProperties(CommandLine cmd) {

		System.setProperty("model.dir", "./models/");
		GregorianCalendar cal = new GregorianCalendar();
		String timestamp = "" + cal.get(Calendar.DAY_OF_MONTH)
				+ cal.get(Calendar.MONTH)
				+ cal.get(Calendar.HOUR)
				+ cal.get(Calendar.MINUTE);
		System.setProperty("logging.file", "./logs/wslogs.log");
		System.setProperty("testreport.zip", "./logs/testreport"+timestamp+".zip");
		System.setProperty("concise.report",
				"./test-output/emailable-report.html");
		System.setProperty("model.prop.file", System.getProperty("model.dir")
				+ "model.properties");

		// check if model exits in models folder

		if (!(new File(System.getProperty("model.dir")).exists())) {
			logger.log(Level.INFO, "Models folder does not exist. Exiting.");
			System.exit(1);
		}

		// Setup model related properties

		boolean doesModelPropFileExists = false;
		// read from prop file

		Properties modelProp = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream(System.getProperty("model.dir")
					+ "model.properties");
			modelProp.load(input);
			doesModelPropFileExists = true;

		} catch (IOException ex) {
			doesModelPropFileExists = false;
			// ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// Set timeout properties
		String[] requiredPropertiesNamesList = { "lcmimporttimeout",
				"epmadeploytimeout", "cubedeploytimeout",
				"runcalculationtimeout" };
		for (String propertyName : requiredPropertiesNamesList) {
			String propValueFromCommandLine = cmd.getOptionValue(propertyName);

			if (propValueFromCommandLine != null) {
				System.setProperty(propertyName, propValueFromCommandLine);
			} else {
				if (propValueFromCommandLine == null && doesModelPropFileExists
						&& modelProp.getProperty(propertyName) != null) {
					logger.log(
							Level.INFO,
							"Required "
									+ propertyName
									+ " is not provided as an argument. Defaulting to model.properties value. ");
					System.setProperty(propertyName,
							modelProp.getProperty(propertyName));
				} else {
					logger.log(
							Level.INFO,
							"Required "
									+ propertyName
									+ " is neither provided as an argument nor is present in model.properties value. Defaulting to a value 600.");
					System.setProperty(propertyName, "600");
				}
			}

		}



	}

	public static void sendMail(String toAddresses, String subject) {
		logger.log(Level.INFO, "Sending report mail  to " + toAddresses + ".");
		String from = "noreply-rest-automation@oracle.com";
		String host = "internal-mail-router.oracle.com";
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", host);
		Session session = Session.getDefaultInstance(properties);
		// Converting to addresses to string array by splitting on ;
		String[] toAddressArray = toAddresses.split(";");
		Address[] addresses = new Address[toAddressArray.length];
		for (int i = 0; i < addresses.length; i++) {
			try {
				addresses[i] = new InternetAddress(toAddressArray[i]);
			} catch (AddressException e) {
			}
		}
		try {

			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from, "Rest Automation"));
			message.addRecipients(Message.RecipientType.TO, addresses);
			message.setSubject(subject);
			String[] files = { System.getProperty("logging.file"),
					System.getProperty("testreport.zip") };
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			String htmlText = FileUtils.readFileToString(new File(System
					.getProperty("concise.report")));
			messageBodyPart.setContent(htmlText, "text/html");
			Multipart multipart = new MimeMultipart("mixed");
			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart);
			for (String filePath : files) {
				if (new File(filePath).exists()) {
					// If actual data file does not exists, there is no need to
					// mail expected data file.

					if (FileUtils.sizeOf(new File(filePath)) > 100000) {
						ZipUtil.compress(filePath, "./logs/"
								+ new File(filePath).getName() + ".zip");
						messageBodyPart = new MimeBodyPart();
						messageBodyPart.attachFile("./logs/"
								+ new File(filePath).getName() + ".zip");
						multipart.addBodyPart(messageBodyPart);
					} else {
						messageBodyPart = new MimeBodyPart();
						messageBodyPart.attachFile(filePath);
						multipart.addBodyPart(messageBodyPart);
					}
				}
			}
			// Send message

			message.setContent(multipart);
			Transport.send(message);
			logger.log(Level.INFO, "Mail sent successfully to " + toAddresses
					+ ".");

		} catch (MessagingException mex) {
			mex.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException ex) {
			Logger.getLogger(BeginTest.class.getName()).log(Level.SEVERE, null,
					ex);
		}
	}
}
