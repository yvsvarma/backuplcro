package com.oracle.hpcm.restclient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;

import com.oracle.hpcm.utils.EPMAutomateUtility;
import com.oracle.hpcm.utils.ResultObject;
import com.oracle.hpcm.utils.UserObject;
import com.oracle.hpcm.webservices.common.DeleteApplicationConsumer;
import com.oracle.hpcm.webservices.common.ExportDiagnosticsJobConsumer;
import com.oracle.hpcm.webservices.common.GetApplicationsConsumer;
import com.oracle.hpcm.webservices.common.GetTaskStatusByProcessNameConsumer;
import com.oracle.hpcm.webservices.common.JobFileApplicationUpdateDimensionConsumer;
import com.oracle.hpcm.webservices.common.ProcessExportTemplateConsumer;
import com.oracle.hpcm.webservices.common.ProcessFileApplicationUpdateDimensionConsumer;
import com.oracle.hpcm.webservices.common.ProcessImportTemplateConsumer;
import com.oracle.hpcm.webservices.ml.MLDeployCubeConsumer;
import com.oracle.hpcm.webservices.ml.RunAnalyticConsumer;

public class PCMCSRestClient {

    private UserObject userObject = null;
    public int timeout = 300;

    public enum AnalyticItemType {
        ANALYSIS_VIEW, SCATTER, WHALE
    }

    public PCMCSRestClient(String user, String server, String password, String port, String domain, boolean staging,
            String version, String epmapiversion, int timeout) {
        super();

        this.timeout = timeout;
        if (staging) {
            userObject = new UserObject(domain + "." + user, password, server, port, epmapiversion, version, domain,
                    staging, timeout);
        } else {
            userObject = new UserObject(user, password, server, port, epmapiversion, version, domain, staging, timeout);
        }
    }

    public PCMCSRestClient(UserObject uo) {
        super();
        this.timeout = uo.getTimeout();
        userObject = uo;
        // System.setProperty("staging", ""+staging);
    }

    public String[] getApplications() {
        GetApplicationsConsumer getAppConsumer = new GetApplicationsConsumer(userObject);
        try {
            return getAppConsumer.getApplicationsNames();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteApplication(String applicationName) throws ParseException {
        DeleteApplicationConsumer delApp = new DeleteApplicationConsumer(this.userObject);
        GetTaskStatusByProcessNameConsumer taskStatus = new GetTaskStatusByProcessNameConsumer(this.userObject);
        ResultObject ro = delApp.deleteApp(applicationName);
        boolean deleteResult = false;
        if (!ro.isResult()) {
            return false;
        } else {
            deleteResult = taskStatus.waitForJobToFinish(ro.getText(), this.timeout);
            // Assert.assertTrue(deleteResult,"Deletion Job failed.");
        }
        return deleteResult;
    }
    public boolean checkForSystemConnectivity(){
        try{
            this.getApplications();
            return true;
        }catch(Exception e){
            return false;
        }
    }
    public String runAnalyticItem(String applicationName, String itemName, AnalyticItemType type)
            throws ParseException {
        RunAnalyticConsumer runAnalytics = new RunAnalyticConsumer(this.userObject);
        ResultObject ro = runAnalytics.runItem(applicationName, itemName, type.toString());
        // System.out.println(ro.getDetails());
        if (ro.isResult() == false) {
            return null;
        } else {
            return ro.getDetails();
        }

    }

    public void cleanEnviornment() throws ParseException {

        for (String app : this.getApplications()) {
            this.deleteApplication(app);
            // Assert.assertTrue(deleteResult,"Deletion Job failed.");
        }
    }

    public boolean importTemplate(String applicationName, String fileName)
            throws ParseException, IOException, SAXException {
        ProcessImportTemplateConsumer importApp = new ProcessImportTemplateConsumer(this.userObject);
        GetTaskStatusByProcessNameConsumer taskStatus = new GetTaskStatusByProcessNameConsumer(this.userObject);
        ResultObject ro = importApp.importTemplate(applicationName, "", "", "", "", fileName, true);
        boolean importResult = false;
        if (!ro.isResult()) {
            return false;
        } else {
            importResult = taskStatus.waitForJobToFinish(ro.getText(), this.timeout);
            // Assert.assertTrue(deleteResult,"Deletion Job failed.");
        }
        return importResult;
    }

    public boolean exportTemplate(String applicationName, String fileName) throws ParseException {
        ProcessExportTemplateConsumer exportApp = new ProcessExportTemplateConsumer(this.userObject);
        GetTaskStatusByProcessNameConsumer taskStatus = new GetTaskStatusByProcessNameConsumer(this.userObject);
        ResultObject ro = exportApp.exportTemplate(applicationName, fileName);
        boolean exportResult = false;
        if (!ro.isResult()) {
            return false;
        } else {
            exportResult = taskStatus.waitForJobToFinish(ro.getText(), this.timeout);
            // Assert.assertTrue(deleteResult,"Deletion Job failed.");
        }
        return exportResult;
    }

    public boolean deployCube(String appName, boolean isKeepData,
            boolean isReplaceCube, boolean isRunNow, String comment) {
        boolean depResult = false;
        try {
            MLDeployCubeConsumer depCube = new MLDeployCubeConsumer(this.userObject);
            GetTaskStatusByProcessNameConsumer taskStatus = new GetTaskStatusByProcessNameConsumer(this.userObject);
            ResultObject ro = depCube.runDeployCubeJob(appName, isKeepData, isReplaceCube, isRunNow, comment);

            if (!ro.isResult()) {
                return false;
            } else {
                depResult = taskStatus.waitForJobToFinish(ro.getText(), this.timeout);
                //Assert.assertTrue(deleteResult,"Deletion Job failed.");
            }
        } catch (ParseException p) {
        }
        return depResult;

    }

    public boolean uploadDimensionJob(String applicationName, String filepath, String stringDelimiter) throws IOException, ParseException {

        JobFileApplicationUpdateDimensionConsumer uploadDim = new JobFileApplicationUpdateDimensionConsumer(
                this.userObject);
        //GetTaskStatusByProcessNameConsumer taskStatus = new GetTaskStatusByProcessNameConsumer(this.userObject);
        boolean result = false;
        ResultObject ro = uploadDim.uploadDimension(applicationName, filepath, stringDelimiter);
        GetTaskStatusByProcessNameConsumer taskStatus = new GetTaskStatusByProcessNameConsumer(this.userObject);
        if (!ro.isResult()) {
            return result;
        } else {
            result = taskStatus.waitForJobToFinish(ro.getText(), this.timeout);
            //Assert.assertTrue(deleteResult,"Deletion Job failed.");
        }
        return result;
    }

    public boolean exportDiagnostics(String applicationName, String filename) throws IOException, ParseException {

        ExportDiagnosticsJobConsumer exportDiag = new ExportDiagnosticsJobConsumer(
                this.userObject);
        //GetTaskStatusByProcessNameConsumer taskStatus = new GetTaskStatusByProcessNameConsumer(this.userObject);
        boolean result = false;
        ResultObject ro = exportDiag.exportDiagnostics(applicationName, filename);
        GetTaskStatusByProcessNameConsumer taskStatus = new GetTaskStatusByProcessNameConsumer(this.userObject);
        if (!ro.isResult()) {
            return result;
        } else {
            result = taskStatus.waitForJobToFinish(ro.getText(), this.timeout);
            //Assert.assertTrue(deleteResult,"Deletion Job failed.");
        }
        return result;
    }

    public boolean uploadDimension(String applicationName, String filepath) throws IOException, ParseException {

        ProcessFileApplicationUpdateDimensionConsumer uploadDim = new ProcessFileApplicationUpdateDimensionConsumer(
                this.userObject);
        //GetTaskStatusByProcessNameConsumer taskStatus = new GetTaskStatusByProcessNameConsumer(this.userObject);
        ResultObject ro = uploadDim.uploadFile(applicationName, filepath);

        return ro.isResult();
    }

    public void uploadFileToService(String filePath, String fileName, String remoteFolder) throws Exception {
        EPMAutomateUtility.uploadFileOverwrite(this.userObject, filePath, fileName, remoteFolder);
    }

    public boolean downloadFile(String remoteFilePath, String fileName, String destination) throws Exception {
        // check if file exists at the source
        // if(!EPMAutomateUtility.doesFileExists(remoteFilePath,fileName))
        // return false;
        // Check if destination folder exists, if not then create the folder
        File destinationFolder = new File(destination);
        if (!destinationFolder.exists()) {
            destinationFolder.mkdir();
        }
        // check if local file exist at destination folder, if yes, then delete
        // the file.
        File localFile = new File(destination + "/" + fileName);
        if (localFile.exists()) {
            localFile.delete();
        }

        FileOutputStream os = new FileOutputStream(new File(destination + "/" + fileName));
        os.write(EPMAutomateUtility.getFileContents(this.userObject, remoteFilePath, fileName));
        os.close();
        return true;
    }

    public void epmAutomateLogin() throws IOException, InterruptedException {
        if (this.userObject.getDomain() == null || this.userObject.isStaging() == false) {
            Executor.runCommand(String.format("epmautomate login %s %s %s", this.userObject.getUserName(),
                    this.userObject.getPassword(),
                    "http://" + this.userObject.getServer() + ":" + this.userObject.getPort()));
        } else {
            Executor.runCommand(String.format("epmautomate login %s %s %s %s", this.userObject.getUserName(),
                    this.userObject.getPassword(),
                    "http://" + this.userObject.getServer() + ":" + this.userObject.getPort(),
                    this.userObject.getDomain()));
        }
    }

    public void epmautomateLogout() throws IOException, InterruptedException {
        Executor.runCommand("epmautomate logout");
    }

    public ReturnObject runCommand(String command) throws Exception {
        return Executor.runCommand(command);
    }

    public UserObject getUserObject() {
        // TODO Auto-generated method stub
        return this.userObject;
    }
}
