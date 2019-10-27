/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oracle.hpcm.utils;


import com.essbase.api.base.EssException;
import com.essbase.api.dataquery.IEssCubeView;
import com.essbase.api.dataquery.IEssMdAxis;
import com.essbase.api.dataquery.IEssMdDataSet;
import com.essbase.api.dataquery.IEssMdMember;
import com.essbase.api.dataquery.IEssOpMdxQuery;
import com.essbase.api.datasource.IEssCube;
import com.essbase.api.datasource.IEssCube.EEssDataLevel;
import com.essbase.api.datasource.IEssOlapFileObject;
import com.essbase.api.datasource.IEssOlapServer;
import com.essbase.api.domain.IEssDomain;
import com.essbase.api.session.IEssbase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class CubeOperation {
	private String user;
	private String password;
	private String server;
	private String provider;
	private String EssAppName;
	private String cubeName;
	private String aps_server;
    public static Logger logger = Logger.getLogger("");
	IEssCubeView cv = null;
	IEssbase ess = null;

	IEssDomain dom;
	IEssOlapServer olapSvr;

	public CubeOperation(String appName) throws Exception {

		server = System.getProperty("server");
		user = System.getProperty("user");
		password = System.getProperty("password");
		EssAppName = appName + "C";
		cubeName = appName + "C";
		//cv = dom.openCubeView("Mdx Query Example", this.server,this.EssAppName, this.cubeName);
		aps_server = System.getProperty("server");
		provider = "http://"+aps_server+":13080/aps/JAPI";
		//Do not change the above url.Don't change the word JAPI to lower case. Will otherwise break.

		logger.info("Connecting to aps server: "+provider);
		try{
		initCube();}
		catch(EssException e){
			logger.info(e.getLogMessage()+"");
			e.printStackTrace();
		}

	}
	public void initCube() throws Exception {
		ess = IEssbase.Home.create(IEssbase.JAPI_VERSION);
		dom = ess.signOn(user, password, false, null, provider);

		olapSvr = dom.getOlapServer(aps_server);
		olapSvr.connect();
	}
	public void initCubeView() throws Exception {
		cv = dom.openCubeView("Mdx Query Example", this.server, this.EssAppName, this.cubeName);
	}
	public boolean deleteEssApp() {
		try {
			// Delete the App if it already exists
			logger.info("[DeleteEssApp]Trying to delete Essbase Application. ");
			olapSvr.getApplication(EssAppName).delete();
			logger.info("[DeleteEssApp]Essbase Application deleted.");
		} catch (EssException x) {
			// Ignore Error
			return true;
		}
		return true;
	}

	public boolean loadDataBSO(String filePath) throws Exception {
		IEssCube cube = olapSvr.getApplication(EssAppName).getCube(cubeName);

		File dataFile = new File(filePath);
		byte[] bFile = new byte[(int) dataFile.length()];
		FileInputStream fileStream;

		logger.info("Starting data load : " + dataFile);
		logger.info("Does the input data file exists: " + dataFile.exists());
		fileStream = new FileInputStream(dataFile);

		fileStream.read(bFile);
		fileStream.close();
		int bufferId1 = 600;

		cube.setActive();
		cube.clearAllData();
/*		cube.createOlapFileObject(IEssOlapFileObject.TYPE_TEXT, "dataload");
		cube.copyOlapFileObjectToServer(IEssOlapFileObject.TYPE_TEXT,"dataload",filePath,true);*/
		//
      	
		try {
			cube.deleteOlapFileObject(IEssOlapFileObject.TYPE_TEXT, "dataload");
		} catch (Exception e) {}

		logger.info("Begining loaddata.");
		cube.copyOlapFileObjectToServer(IEssOlapFileObject.TYPE_TEXT, "dataload", bFile, true);
			

		logger.info("Client side Data file Loading to BSO Complete.");
		 cube.loadData(IEssOlapFileObject.TYPE_RULES, null,
		            IEssOlapFileObject.TYPE_TEXT, "dataload", false);

		logger.info("Data was loaded to Essbase.");
		
		return true;
	}

	public void tearDown() throws Exception {
		if (ess != null && ess.isSignedOn() == true) ess.signOff();
	}



	public InputStream getResource(String name) throws FileNotFoundException {
		return new FileInputStream(new File(name));
	}

	public void exportCubeData() throws EssException, IOException {

		IEssCube cube = olapSvr.getApplication(EssAppName).getCube(cubeName);
		cube.setActive();
		File exportFile = new File(System.getProperty("model.dir.actual"));
		exportFile.createNewFile();
		logger.info("[ExportCubeData]Exporting Calculated data in to the file " + exportFile.getAbsolutePath() + ".");
		try {
			cube.deleteOlapFileObject(IEssOlapFileObject.TYPE_TEXT, "calcdat");
		} catch (Exception e) {
			//do nothing

		}
		//cube.createOlapFileObject(IEssOlapFileObject.TYPE_TEXT,"calcdat");

		cube.exportData(this.EssAppName + "/" + this.EssAppName + "/calcdat.txt", EEssDataLevel.LEVEL0, false);
		logger.info("[ExportCubeData]Expoerted calculated data in to the file on the server.");
		byte[] byteArray = cube.copyOlapFileObjectFromServer(IEssOlapFileObject.TYPE_TEXT, "calcdat", false);
		FileOutputStream fos = new FileOutputStream(exportFile);
		fos.write(byteArray);
		fos.close();
		logger.info("[ExportCubeData]Expoerted calculated data in to the file " + exportFile.getAbsolutePath() + ".");
		// cube.copyOlapFileObjectFromServer();
		this.olapSvr.disconnect();


	}
	public String runMDX(String mdxQuery) throws Exception {
		try {
			IEssOpMdxQuery op = cv.createIEssOpMdxQuery();
			op.setQuery(false, true, mdxQuery, false, IEssOpMdxQuery.EEssMemberIdentifierType.NAME);
			op.setXMLAMode(true);

			op.setNeedFormattedCellValue(true);
			op.setNeedSmartlistName(true);
			op.setNeedFormatString(false);
			op.setNeedMeaninglessCells(false);

			this.cv.performOperation(op);

			IEssMdDataSet mddata = cv.getMdDataSet();

			//printMdDataSet(mddata, op);
			return printMdDataSetInGridForm(mddata);

		} catch (Exception E) {
			ess.signOff();
			throw E;
		}
	}
	public void logout() throws Exception {
		ess.signOff();
	}
	public void queryData() throws Exception {

		initCubeView();
		logger.info("[CubeOperation.QueryData] Querying the data in cube after calculation.");
		Scanner fin = new Scanner(new File(System.getProperty("model.dir") + "\\mdx.txt"));
		String mdxQueryOriginal = fin.useDelimiter("\\n\\r").next();
		mdxQueryOriginal = mdxQueryOriginal.replace("%APP%", EssAppName).replace("%CUBE%", cubeName);
		String mdxQuery = mdxQueryOriginal;
		fin.close();
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(new FileReader(System.getProperty("model.dir") + "\\POV"));
		JSONObject jsonObject = (JSONObject) obj;
		JSONArray jArray = (JSONArray) jsonObject.get("POVArray");
		String textToBePrinted = "";
		for (int i = 0; i < jArray.size(); i++) {
			JSONObject pov = (JSONObject) jArray.get(i);
			String POVText = (String) pov.get("POV");
			String[] POVMembers = POVText.split(":");
			String povName = POVMembers[0] + "-" + POVMembers[1] + "-" + POVMembers[2];
			//logger.info(" ---------- " + POVText +" ---------"+RuleMembers.length);
			mdxQuery = mdxQueryOriginal.replace("%POV1%", POVMembers[0]).replace("%POV3%", POVMembers[1]).replace("%POV2%", POVMembers[2]);
			try {
				//logger.info(mdxQuery);
				textToBePrinted += runMDX(mdxQuery);
				logger.info("[CubeOperation.queryData] Execution Successful of Query for this POV.");
			} catch (EssException e) {
				logger.info("[CubeOperation.queryData] MDX Query failed for POV " + povName);
				//logger.info(mdxQuery);
				if (e.getMessage().equals("Invalid tuple index for axis. [0]")) logger.info("[CubeOperation.queryData] No Data in this POV.");
				else logger.info("[CubeOperation.queryData]" + e.getMessage());
				initCube();
				initCubeView();
			}
		}
		FileOutputStream opfile = new FileOutputStream(new File(System.getProperty("model.dir.actual")));
		opfile.write(textToBePrinted.getBytes());
		opfile.close();
		logger.info("[CubeOperation.QueryData]Exported data is saved in this file: " + System.getProperty("model.dir.actual"));
	}

	private static String printMdDataSetInGridForm(IEssMdDataSet mddata) throws Exception {
		StringBuilder textToBeReturned = new StringBuilder();

		IEssMdAxis[] axis = mddata.getAllAxes();
		//logger.info("Length of axis arr: "+axis.length);
		int cols = axis[0].getTupleCount();
		int rows = axis[1].getTupleCount();
		logger.info("[CubeOperation]MDX Row Count: " + rows + " MDX Col Count: " + cols);
		IEssMdMember[] mem = axis[1].getAllTupleMembers(0);

		textToBeReturned.append("\n");
		int k = 0;
		for (int i = 0; i < rows; i++) {
			mem = axis[1].getAllTupleMembers(i);
			textToBeReturned.append(printTuple(mem));

			for (int j = 0; j < cols; j++) {
				if (mddata.isMissingCell(k)) {
					textToBeReturned.append("#Missing\t");
				} else {

					float val = (float) mddata.getCellValue(k);
					textToBeReturned.append(val).append("\t");
				}

				k++;
			}
			textToBeReturned.append("\n");
		}
		return textToBeReturned + "";
	}

	private static String printTuple(IEssMdMember[] mem) throws EssException {
		String textToBeReturned = "";
		textToBeReturned += "(";
		for (int i = 0; i < mem.length - 1; i++)
		textToBeReturned += mem[i].getName() + ",";
		textToBeReturned += mem[mem.length - 1].getName() + ")\t";
		return textToBeReturned;
	}


	public void loadDataFromFile(String fileName) throws Exception {
		//String fileName = System.getProperty("model.dir.input");
		IEssCube cube = olapSvr.getApplication(EssAppName).getCube(cubeName);
		cube.setActive();
		cube.clearAllData();
		cube.beginUpdate(true, false);

		BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName))));
		String data;
		while ((data = r.readLine()) != null) {
			cube.sendString(data + "\n");
		}
		cube.endUpdate();
		logger.info("Data was loaded to Essbase");
		r.close();
	}

}