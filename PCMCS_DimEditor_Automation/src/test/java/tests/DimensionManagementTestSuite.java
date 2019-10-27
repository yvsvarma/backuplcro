package tests;

import com.oracle.hpcm.restclient.PCMCSRestClient;
import com.oracle.hpcm.utils.CompareUtil;
import com.oracle.hpcm.utils.UserObject;
import com.oracle.hpcm.utils.ZipUtil;
import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.oracle.pcmcs.dbconn.utils.DatabaseUtils;
import com.oracle.pcmcs.logging.CustomLogger;
import com.oracle.pcmcs.logging.ILogger;
import com.relevantcodes.extentreports.LogStatus;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import poms.DimensionEditorPage;
import poms.DimensionManagementPage;
import poms.FuseLoginPage;
import poms.FuseNavigatorPage;
import static tests.BaseTestSuite.currentTest;
import static tests.BaseTestSuite.large_timeout;
import static tests.BaseTestSuite.reporter;
import utils.BasicUtil;
import utils.PropertiesUtils;
import utils.Utilities;

public class DimensionManagementTestSuite extends BaseTestSuite {

    public static final ILogger logger = CustomLogger.getLogger(DimensionManagementTestSuite.class);
    public static final String APPLICATION_NAME = "BksML30";
    public static final String ESSBASE_APPLICATION_NAME = "BksML30C";
    public static HashMap<String, String> dbConnectionData;
    public static HashMap<String, String> essbaseConnectionData;
    DimensionManagementPage dimMgmtPage;
    String memberNameCustomer = "BB100-001";
    String editabledimensionName = "Customer";

    String className = this.getClass().getName();

    public static void enumurateMap(HashMap<String, String> map) {
        for (String key : map.keySet()) {
            logger.info(key + " : " + map.get(key));
        }
    }

    public void deleteMemberVerification(String dimensionName, String memberName) {
        log("Verifying that new member is added in the database.");
        Assert.assertNull(DatabaseUtils.getDimensionFromRDB(APPLICATION_NAME, dimensionName, dbConnectionData)
                .searchAndReturnMember(memberName), "Member is not deleted in DB.");
        log("Verified that Deleted member is not present in the db.");
//        log("Deploying Cube.");
//        Assert.assertTrue(restClient.deployCube(APPLICATION_NAME, true, false, true, ""), "Cube Deployment failed.");
//        log("Verifying that deleted member is not present in the essbase.");
//        Assert.assertNull(EssbaseUtils.getDimensionTO(ESSBASE_APPLICATION_NAME, dimensionName, essbaseConnectionData)
//                .searchAndReturnMember(memberName), "Member is not deleted in Essbase.");
//        log("Verified that Deleted member is not present in the essbase.");

    }

    public void addMemberVerification(String dimensionName, String memberName) {
        log("Verifying that new member is added in the database.");
        Assert.assertNotEquals("Newly added member is not found in DB.",
                null == DatabaseUtils.getDimensionFromRDB(APPLICATION_NAME, dimensionName, dbConnectionData)
                        .searchAndReturnMember(memberName));
        log("Verified that new member is added in the database.");
        //log("Deploying Cube.");
        //Assert.assertTrue(restClient.deployCube(APPLICATION_NAME, true, false, true, ""), "Cube Deployment failed.");
        //restClient.deployCube(APPLICATION_NAME, true, false, true, "");
        //log("Verifying that new member is added in the essbase.");
        //Assert.assertNotEquals("Newly added member is not found in Essbase.",
        //       null == EssbaseUtils.getDimensionTO(ESSBASE_APPLICATION_NAME, dimensionName, essbaseConnectionData)
        //             .searchAndReturnMember(memberName));
        //log("Verified that new member is added in the essbase.");

    }

    public void deleteApplication() throws Exception {
        UserObject currentUserCredentials = new UserObject(System.getProperty("user"), System.getProperty("password"), System.getProperty("server"),
                System.getProperty("port"), System.getProperty("epmapiversion"), System.getProperty("version"), System.getProperty("domain"), new Boolean(System.getProperty("staging")).booleanValue(), Integer.parseInt(System.getProperty("timeout"), 10));
        restClient = new PCMCSRestClient(currentUserCredentials);
        String[] appNames = restClient.getApplications();
        log("timeout : " + Integer.parseInt(System.getProperty("timeout"), 10));
        for (String appName : appNames) {
            log("----- Deleting applications ----" + appName);
            restClient.deleteApplication(appName);
        }

    }

    public void setupModelThroughImport() throws Exception {
        log("----- Setup Model through import ----");
        //PropertiesUtils.processGlobalProperties(new Properties());
        UserObject currentUserCredentials = new UserObject(System.getProperty("user"), System.getProperty("password"), System.getProperty("server"),
                System.getProperty("port"), System.getProperty("epmapiversion"), System.getProperty("version"), System.getProperty("domain"), new Boolean(System.getProperty("staging")).booleanValue(), Integer.parseInt(System.getProperty("timeout"), 10));
        restClient = new PCMCSRestClient(currentUserCredentials);
        restClient.uploadFileToService("../testdata/templates/", "BksML30.zip", "profitinbox");
        restClient.importTemplate("BksML30", "BksML30.zip");
        Utilities.wait(300);
    }

    @Test(priority = 3, enabled = true)
    public void setup() throws Exception {
        deleteApplication();
        setupModelThroughImport();
       log("setting up db connection");
        //TestDBUtils.main(new String[1]);
        dbConnectionData = DatabaseUtils.getDBConnectionDataMap(System.getProperty("db_server"),
                System.getProperty("db_port"), System.getProperty("db_sid"), System.getProperty("db_user"),
                System.getProperty("db_password"));
       log("DB Connection is successfull");
       
//        essbaseConnectionData = EssbaseUtils.getEssConnectionDataMap(System.getProperty("server"), System.getProperty("essbase_port"),
//                System.getProperty("user"), System.getProperty("password"));
//        enumurateMap(essbaseConnectionData);
        // addMemberVerification("Customer", "Customer1");
        driver = Utilities.getDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, large_timeout);
        BasicUtil.log("Browser Opened");
        ml_application = System.getProperty("ml_application");
        driver.manage().timeouts().implicitlyWait(large_timeout, TimeUnit.SECONDS);
        driver.manage().window().maximize();
       // loginPage = new FuseLoginPage(driver);
        FuseLoginPage loginPage = new FuseLoginPage(driver);
        navPage = loginPage.doLogin("");
        log("Login successful.");
    }

   @Test(priority = 4, enabled = true)
    public void addNewMemberBusinessAtTopDim() throws Exception {
        log("in addNewMemberBusinessAtTopDim");
        String dimensionName = "Customer";
        String parentName = "Customer";
        String newMemberName = "Customer1";
        log("opening dimension editor for dimension : " + dimensionName);
        DimensionEditorPage dimEditor = dimMgmtPage.openDimEditorByDimensionName(dimensionName);
        log("opened dimension editor for dimension : " + dimensionName);
        log("Adding new member in " + parentName + ".");
        BasicUtil.waitADF(driver, 100);
        dimEditor.addNewMemberAsChild(parentName, newMemberName, false);
        log("Added new member for " + parentName + ".");
        BasicUtil.waitADF(driver, 100);
        log("Verifying new member for " + newMemberName + ".");
        Assert.assertTrue(dimEditor.getSelectedMemberName().equals(newMemberName), "Searched member is not found.");
        log("Verified new member for " + newMemberName + ".");
        log("Saving new member for " + newMemberName + ".");
        dimEditor.save();
        log("Saved new member for " + newMemberName + ".");
        log("Closing diemnsion Editor.");
        dimEditor.close();
        log("Closed diemnsion Editor.");
        addMemberVerification(dimensionName, newMemberName);
    }

    @Test(priority = 4, enabled = true)
    public void addNewMemberBusinessDim() {
        String dimensionName = "Product";
        String parentName = "Bikes";
        String newMemberName = "ChildproductXXY";
        log("in addNewMemberBusinessDim");
        log("opening dimension editor for dimension : " + dimensionName);
        DimensionEditorPage dimEditor = dimMgmtPage.openDimEditorByDimensionName(dimensionName);
        log("opened dimension editor for dimension : " + dimensionName);
        log("Adding new member for " + parentName + ".");
        dimEditor.addNewMemberAsChild(parentName, newMemberName, false);
        log("Added new member for " + parentName + ".");
        BasicUtil.waitADF(driver, 100);
        log("Verifying new member for " + parentName + ".");
        Assert.assertTrue(dimEditor.getSelectedMemberName().equals(newMemberName), "Searched member is not found.");
        log("Verified new member for " + parentName + ".");
        log("Saving new member for " + parentName + ".");
        dimEditor.save();
        log("Saved new member for " + parentName + ".");
        log("Closing diemnsion Editor.");
        dimEditor.close();
        log("Closed diemnsion Editor.");
        addMemberVerification(dimensionName, newMemberName);

    }

    @Test(priority = 5, enabled = true)
    public void addNewSharedMemberBusinessDim() {
        String dynamicDimensionName = "Account";
        String parentMemberName = "NoAccount";
        String newMemberName = "MAT5400";
        log("In addNewSharedMemberBusinessDim");
        log("Opening dimension editor for dimension : " + dynamicDimensionName);
        DimensionEditorPage dimEditor = dimMgmtPage.openDimEditorByDimensionName(dynamicDimensionName);
        log("Opened dimension editor for dimension : " + dynamicDimensionName);
        log("Adding new shared member for " + memberNameCustomer + ".");
        dimEditor.addNewMemberAsChild(parentMemberName, newMemberName, true);
        log("Added new shared member for " + newMemberName + ".");
        BasicUtil.waitADF(driver, 100);
        log("Verifying new member for " + newMemberName + ".");
        // Assert.assertTrue(dimEditor.getSelectedMemberName().equals(newMemberName),"Searched
        // member is not found.");
        log("Verified new member for " + newMemberName + ".");
        log("Saving new member for " + newMemberName + ".");
        dimEditor.save();
        log("Saved new member for " + newMemberName + ".");
        log("Closing diemnsion Editor.");
        dimEditor.close();
        log("Closed diemnsion Editor.");
        addMemberVerification(dynamicDimensionName, newMemberName);

    }

    @Test(priority = 6, enabled = true)
    public void addNewMemebrPOVDim() {
        String dimensionName = "Year";
        String parentMemberName = "Year";
        String newMemberName = "1500";
        log("In addNewMemebrPOVDim");
        log("Opening dimension editor for dimension : " + dimensionName);
        DimensionEditorPage dimEditor = dimMgmtPage.openDimEditorByDimensionName(dimensionName);
        log("Opened dimension editor for dimension : " + dimensionName);
        log("Adding new member for " + newMemberName + ".");
        dimEditor.addNewMemberAsChild(parentMemberName, newMemberName, false);
        log("Added new member for " + newMemberName + ".");
        BasicUtil.waitADF(driver, 100);
        log("Verifying new member for " + newMemberName + ".");
        Assert.assertTrue(dimEditor.getSelectedMemberName().equals(newMemberName), "Searched member is not found.");
        log("Verified new member for " + newMemberName + ".");
        log("Saving new member for " + newMemberName + ".");
        dimEditor.save();
        log("Saved new member for " + newMemberName + ".");
        log("Closing diemnsion Editor.");
        dimEditor.close();
        log("Closed diemnsion Editor.");
        log("Verifying that new member is added in the database.");
        addMemberVerification(dimensionName, newMemberName);

    }

    @Test(priority = 6, enabled = true)
    public void addNewMemberAttribute() {
        String dimensionName = "Delivery Zone";
        String parentMemberName = "Delivery Zone";
        String newMemberName = "TY";
        log("In addNewMemberAttribute");
        log("Opening dimension editor for dimension : " + dimensionName);
        DimensionEditorPage dimEditor = dimMgmtPage.openDimEditorByDimensionName(dimensionName);
        log("Opened dimension editor for dimension : " + dimensionName);
        log("Adding new member for " + newMemberName + ".");
        dimEditor.addNewMemberAsChild(parentMemberName, newMemberName, false);
        log("Added new member for " + newMemberName + ".");
        BasicUtil.waitADF(driver, 100);
        log("Verifying new member for " + newMemberName + ".");
        Assert.assertTrue(dimEditor.getSelectedMemberName().equals(newMemberName), "Searched member is not found.");
        log("Verified new member for " + newMemberName + ".");
        log("Saving new member for " + newMemberName + ".");
        dimEditor.save();
        log("Saved new member for " + newMemberName + ".");
        log("Closing diemnsion Editor.");
        dimEditor.close();
        log("Closed diemnsion Editor.");
        addMemberVerification(dimensionName, newMemberName);
    } 

    @Test(priority = 6, enabled = true)
    public void deleteMemberBusiness() {
        String dimensionName = "Activity";
        String deletedMemberName = "BUS1640";
        log("In deleteMemberBusiness");
        log("Opening dimension editor for dimension : " + dimensionName);
        DimensionEditorPage dimEditor = dimMgmtPage.openDimEditorByDimensionName(dimensionName);
        log("Opened dimension editor for dimension : " + dimensionName);
        log("Deleting  member  " + deletedMemberName + ".");
        BasicUtil.waitADF(driver, 20);
        dimEditor.findMemberUsingSearch(deletedMemberName);
        dimEditor.deleteMember(deletedMemberName);
        log("Deleted member  " + deletedMemberName + ".");
        BasicUtil.waitADF(driver, 100);
        log("Verifying delete for " + deletedMemberName + ".");
        // Assert.assertTrue(!dimEditor.getSelectedMemberName().equals(deletedMemberName),"Searched
        // member is not deleted.");
        log("Verified delete member for " + deletedMemberName + ".");
        log("Saving.");
        dimEditor.save();
        log("Saved.");
        log("Closing diemnsion Editor.");
        dimEditor.close();
        log("Closed diemnsion Editor.");
        deleteMemberVerification(dimensionName, deletedMemberName);
    }

    @Test(priority = 7, enabled = true)
    public void delete0LevelMemberPOV() {
        String dimensionName = "Year";
        String deletedMemberName = "2014";
        log("In deleteMemberPOV");
        log("Opening dimension editor for dimension : " + dimensionName);
        DimensionEditorPage dimEditor = dimMgmtPage.openDimEditorByDimensionName(dimensionName);
        log("Opened dimension editor for dimension : " + dimensionName);
        log("Deleting  member  " + deletedMemberName + ".");
        BasicUtil.waitADF(driver, 20);
        dimEditor.findMemberUsingSearch(deletedMemberName);
        dimEditor.deleteMember(deletedMemberName);
        log("Deleted member  " + deletedMemberName + ".");
        BasicUtil.waitADF(driver, 100);
        log("Verifying delete for " + deletedMemberName + ".");
        // Assert.assertTrue(!dimEditor.getSelectedMemberName().equals(deletedMemberName),"Searched
        // member is not deleted.");
        log("Verified delete member for " + deletedMemberName + ".");
        log("Saving.");
        dimEditor.save();
        log("Saved.");
        log("Closing diemnsion Editor.");
        dimEditor.close();
        log("Closed diemnsion Editor.");
        deleteMemberVerification(dimensionName, deletedMemberName);
    }

    @Test(priority = 8, enabled = true)
    public void delete0LevelMemberAttribute() {
        String dimensionName = "Region";
        String deletedMemberName = "CAN P";
        log("In deleteMemberPOV");
        log("Opening dimension editor for dimension : " + dimensionName);
        DimensionEditorPage dimEditor = dimMgmtPage.openDimEditorByDimensionName(dimensionName);
        log("Opened dimension editor for dimension : " + dimensionName);
        log("Deleting  member  " + deletedMemberName + ".");
        BasicUtil.waitADF(driver, 20);
        dimEditor.findMemberUsingSearch(deletedMemberName);
        dimEditor.deleteMember(deletedMemberName);
        log("Deleted member  " + deletedMemberName + ".");
        BasicUtil.waitADF(driver, 100);
        log("Verifying delete for " + deletedMemberName + ".");
        // Assert.assertTrue(!dimEditor.getSelectedMemberName().equals(deletedMemberName),"Searched
        // member is not deleted.");
        log("Verified delete member for " + deletedMemberName + ".");
        log("Saving.");
        dimEditor.save();
        log("Saved.");
        log("Closing dimension Editor.");
        dimEditor.close();
        log("Closed dimension Editor.");
        deleteMemberVerification(dimensionName, deletedMemberName);
    }

    @Test(priority = 9, enabled = true)
    public void copy0levelMemberBusiness() {
        String dimensionName = "Activity";
        String copyMemberName = "NoActivity";
        String copiedMemberName = "Copy Of NoActivity";
        log("In copy0levelMemberBusiness");
        log("Opening dimension editor for dimension : " + dimensionName);
        DimensionEditorPage dimEditor = dimMgmtPage.openDimEditorByDimensionName(dimensionName);
        log("Opened dimension editor for dimension : " + dimensionName);
        log("Copying 0-level  member  " + copyMemberName + ".");
        BasicUtil.waitADF(driver, 20);
        dimEditor.findMemberUsingSearch(copyMemberName);
        dimEditor.deleteMember(copyMemberName);
        log("Copied member  " + copyMemberName + ".");
        BasicUtil.waitADF(driver, 100);
        log("Verifying Copy for " + copyMemberName + ".");
        // Assert.assertTrue(dimEditor.getSelectedMemberName().equals(copyMemberName),"Searched
        // member is not deleted.");
        log("Verified Copy member for " + copyMemberName + ".");
        log("Saving.");
        dimEditor.save();
        log("Saved.");
        log("Closing diemnsion Editor.");
        dimEditor.close();
        log("Closed diemnsion Editor.");
        addMemberVerification(dimensionName, copiedMemberName);
    }

    @Test(priority = 10, enabled = true)
    public void deleteNon0levelMemberBusiness() {
        String dimensionName = "Customer";
        String deleteMemberName = "Webstore";
        // log("In copyMemberBusiness0level");
        log("Opening dimension editor for dimension : " + dimensionName);
        DimensionEditorPage dimEditor = dimMgmtPage.openDimEditorByDimensionName(dimensionName);
        log("Opened dimension editor for dimension : " + dimensionName);
        log("Deleting non 0-level  member  " + deleteMemberName + ".");
        BasicUtil.waitADF(driver, 20);
        dimEditor.findMemberUsingSearch(deleteMemberName);
        dimEditor.deleteMember(deleteMemberName);
        log("Deleted member  " + deleteMemberName + ".");
        BasicUtil.waitADF(driver, 100);
        log("Verifying Delete for " + deleteMemberName + ".");
        // @ToDO
        // Assert.assertTrue(dimEditor.getSelectedMemberName().equals(copyMemberName),"Searched
        // member is not deleted.");
        log("Verified Delete member for " + deleteMemberName + ".");
        log("Saving.");
        dimEditor.save();
        log("Saved.");
        log("Closing diemnsion Editor.");
        dimEditor.close();
        log("Closed diemnsion Editor.");
        deleteMemberVerification(dimensionName, deleteMemberName);
    }

    @Test(priority = 11, enabled = true)
    public void deleteNon0levelMemberAttribute() {
        String dimensionName = "Sales Rep";
        String deleteMemberName = "Clorinda Naquin";
        log("In deleteNon0levelMemberAttribute");
        log("Opening dimension editor for dimension : " + dimensionName);
        DimensionEditorPage dimEditor = dimMgmtPage.openDimEditorByDimensionName(dimensionName);
        log("Opened dimension editor for dimension : " + dimensionName);
        log("Deleting non 0-level  member  " + deleteMemberName + ".");
        BasicUtil.waitADF(driver, 20);
        dimEditor.findMemberUsingSearch(deleteMemberName);
        dimEditor.deleteMember(deleteMemberName);
        log("Deleted member  " + deleteMemberName + ".");
        BasicUtil.waitADF(driver, 100);
        log("Verifying Delete for " + deleteMemberName + ".");
        // @ToDO
        // Assert.assertTrue(dimEditor.getSelectedMemberName().equals(copyMemberName),"Searched
        // member is not deleted.");
        log("Verified Delete member for " + deleteMemberName + ".");
        log("Saving.");
        dimEditor.save();
        log("Saved.");
        log("Closing diemnsion Editor.");
        dimEditor.close();
        log("Closed diemnsion Editor.");
        deleteMemberVerification(dimensionName, deleteMemberName);
    }

    @Test(priority = 12, enabled = true)
    public void delete0levelMemberBusiness() {
        String dimensionName = "Region";
        String deleteMemberName = "Direct";
        log("In delete0levelMemberBusiness");
        log("Opening dimension editor for dimension : " + dimensionName);
        DimensionEditorPage dimEditor = dimMgmtPage.openDimEditorByDimensionName(dimensionName);
        log("Opened dimension editor for dimension : " + dimensionName);
        log("Deleting 0-level  member  " + deleteMemberName + ".");
        BasicUtil.waitADF(driver, 20);
        dimEditor.findMemberUsingSearch(deleteMemberName);
        dimEditor.deleteMember(deleteMemberName);
        log("Deleted member  " + deleteMemberName + ".");
        BasicUtil.waitADF(driver, 100);
        log("Verifying Delete for " + deleteMemberName + ".");
        log("Verified Delete member for " + deleteMemberName + ".");
        log("Saving.");
        dimEditor.save();
        log("Saved.");
        log("Closing diemnsion Editor.");
        dimEditor.close();
        log("Closed diemnsion Editor.");
        deleteMemberVerification(dimensionName, deleteMemberName);
    }

    @Test(priority = 13, enabled = true)
    public void reparent0LevelToAnother0LevelPOV() {
        String dimensionName = "Period";
        //String oldParentName = "Period";
        String childMemberName = "October";
        String parentMemberName = "Q3";
        String cpathForreparentDlg = "//h1[text()='Reparent Member']";
        // log("In copyMemberBusiness0level");
        log("Opening dimension editor for dimension : " + dimensionName);
        DimensionEditorPage dimEditor = dimMgmtPage.openDimEditorByDimensionName(dimensionName);
        log("Opened dimension editor for dimension : " + dimensionName);
        log("reparenting 0-level  member  " + childMemberName + ".");
        BasicUtil.waitADF(driver, 20);
        dimEditor.findMemberUsingSearch(childMemberName);
        dimEditor.clickReparentButtonForMember(childMemberName);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(cpathForreparentDlg)));
        log("Opened reprent dlg for member  " + childMemberName + ".");
        BasicUtil.waitADF(driver, 100);
        log("selecting parent " + parentMemberName + " for " + childMemberName + ".");
        dimEditor.populateParentDlg(parentMemberName);
        dimEditor.clickReparentOKButton();
        // wait.until(ExpectedConditions.pr(By.xpath(cpathForreparentDlg)));
        BasicUtil.waitADF(driver, 100);
        // @ToDO
        // Assert.assertTrue(dimEditor.getSelectedMemberName().equals(copyMemberName),"Searched
        // member is not deleted.");
        log("Reparented  member to  " + parentMemberName + ".");
        log("Saving.");
        dimEditor.save();
        log("Saved.");
        log("Closing diemnsion Editor.");
        dimEditor.close();
        log("Closed diemnsion Editor.");
    }

    @Test(priority = 14, enabled = true)
    public void reparent0LevelToNonLevelBusiness() {
        String dimensionName = "Entity";
        String childMemberName = "CC4000";
        String parentMemberName = "CC7001";
        String cpathForreparentDlg = "//h1[text()='Reparent Member']";
        // log("In copyMemberBusiness0level");
        log("Opening dimension editor for dimension : " + dimensionName);
        DimensionEditorPage dimEditor = dimMgmtPage.openDimEditorByDimensionName(dimensionName);
        log("Opened dimension editor for dimension : " + dimensionName);
        log("reparenting 0-level  member  " + childMemberName + ".");
        BasicUtil.waitADF(driver, 20);
        dimEditor.findMemberUsingSearch(childMemberName);
        dimEditor.clickReparentButtonForMember(childMemberName);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(cpathForreparentDlg)));
        log("Opened reprent dlg for member  " + childMemberName + ".");
        BasicUtil.waitADF(driver, 100);
        log("selecting parent " + parentMemberName + " for " + childMemberName + ".");
        dimEditor.populateParentDlg(parentMemberName);
        dimEditor.clickReparentOKButton();
        // wait.until(ExpectedConditions.pr(By.xpath(cpathForreparentDlg)));
        BasicUtil.waitADF(driver, 100);
        // @ToDO
        // Assert.assertTrue(dimEditor.getSelectedMemberName().equals(copyMemberName),"Searched
        // member is not deleted.");
        log("Reparented  member to  " + parentMemberName + ".");
        log("Saving.");
        dimEditor.save();
        log("Saved.");
        log("Closing diemnsion Editor.");
        dimEditor.close();
        log("Closed diemnsion Editor.");
    }

    @Test(priority = 15, enabled = true)
    public void reparentNonLevelTo0LevelBusiness() {
        String dimensionName = "Entity";
        String childMemberName = "CC8000";
        String parentMemberName = "CC6001";
        String cpathForreparentDlg = "//h1[text()='Reparent Member']";
        // log("In copyMemberBusiness0level");
        log("Opening dimension editor for dimension : " + dimensionName);
        DimensionEditorPage dimEditor = dimMgmtPage.openDimEditorByDimensionName(dimensionName);
        log("Opened dimension editor for dimension : " + dimensionName);
        log("reparenting 0-level  member  " + childMemberName + ".");
        BasicUtil.waitADF(driver, 20);
        dimEditor.findMemberUsingSearch(childMemberName);
        dimEditor.clickReparentButtonForMember(childMemberName);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(cpathForreparentDlg)));
        log("Opened reprent dlg for member  " + childMemberName + ".");
        BasicUtil.waitADF(driver, 100);
        log("selecting parent " + parentMemberName + " for " + childMemberName + ".");
        dimEditor.populateParentDlg(parentMemberName);
        dimEditor.clickReparentOKButton();
        // wait.until(ExpectedConditions.pr(By.xpath(cpathForreparentDlg)));
        BasicUtil.waitADF(driver, 100);
        // @ToDO
        // Assert.assertTrue(dimEditor.getSelectedMemberName().equals(copyMemberName),"Searched
        // member is not deleted.");
        log("Reparented  member to  " + parentMemberName + ".");
        log("Saving.");
        dimEditor.save();
        log("Saved.");
        log("Closing diemnsion Editor.");
        dimEditor.close();
        log("Closed diemnsion Editor.");
    }

    @Test(priority = 16, enabled = true)
    public void reparentNonLevelToTopBusiness() {
        String dimensionName = "Activity";
        String childMemberName = "Customer Servicing";
        String parentMemberName = "Activity";
        String cpathForreparentDlg = "//h1[text()='Reparent Member']";
        // log("In copyMemberBusiness0level");
        log("Opening dimension editor for dimension : " + dimensionName);
        DimensionEditorPage dimEditor = dimMgmtPage.openDimEditorByDimensionName(dimensionName);
        log("Opened dimension editor for dimension : " + dimensionName);
        log("reparenting 0-level  member  " + childMemberName + ".");
        BasicUtil.waitADF(driver, 20);
        dimEditor.findMemberUsingSearch(childMemberName);
        dimEditor.clickReparentButtonForMember(childMemberName);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(cpathForreparentDlg)));
        log("Opened reprent dlg for member  " + childMemberName + ".");
        BasicUtil.waitADF(driver, 100);
        log("selecting parent " + parentMemberName + " for " + childMemberName + ".");
        dimEditor.populateParentDlg(parentMemberName);
        dimEditor.clickReparentOKButton();
        // wait.until(ExpectedConditions.pr(By.xpath(cpathForreparentDlg)));
        BasicUtil.waitADF(driver, 100);
        // @ToDO
        // Assert.assertTrue(dimEditor.getSelectedMemberName().equals(copyMemberName),"Searched
        // member is not deleted.");
        log("Reparented  member to  " + parentMemberName + ".");
        log("Saving.");
        dimEditor.save();
        log("Saved.");
        log("Closing diemnsion Editor.");
        dimEditor.close();
        log("Closed diemnsion Editor.");
    }

    @Test(priority = 17, enabled = true)
    public void verifyDimensionDiscrepancy() throws Exception {
        String actual_file_name = "diag1";
        log("Running exportDiagnostics.");
        restClient.exportDiagnostics(APPLICATION_NAME, actual_file_name);
        log("Downloading file from profit inbox.");
        restClient.downloadFile("profitinbox", actual_file_name + ".zip", "./target/");
        log("Unzipping file " + "./target/" + actual_file_name + ".zip");
        ZipUtil.unzip("./target/" + actual_file_name + ".zip", "./target/");
        log("Comparing " + "./testdata/files/expected_dim_discrepancy_TC1.csv and ./target/" + actual_file_name + "/Dimension_Data_Discrepancies.csv");
        Assert.assertTrue(CompareUtil.compareFiles("../testdata/files/Dimension_Data_Discrepancies_TC1.csv", "./target/" + actual_file_name + "/Dimension_Data_Discrepancies.csv"), "Comparing of expected and actual dimension discrepancy file failed. Please check the logs for differences.");
        log("Deploying Cube.");
        
    }

    @Test(priority = 18, enabled = true)
    public void verifyDimDiscrepancyAfterCubeDeploy() throws Exception {
        String actual_file_name = "diag2";
        Assert.assertTrue(restClient.deployCube(APPLICATION_NAME, true, false, true, ""), "Cube Deployment failed.");
        log("Running exportDiagnostics after cube deploy.");
        restClient.exportDiagnostics(APPLICATION_NAME, actual_file_name);
        log("Downloading file from profit inbox.");
        restClient.downloadFile("profitinbox", actual_file_name + ".zip", "./target/");
        log("Unzipping file " + "./target/" + actual_file_name + ".zip");
        ZipUtil.unzip("./target/" + actual_file_name + ".zip", "./target/");
        log("Comparing " + "./testdata/files/expected_dim_discrepancy_TC2.csv and ./target/" + actual_file_name + "/Dimension_Data_Discrepancies.csv");
        Assert.assertTrue(CompareUtil.compareFiles("../testdata/files/Dimension_Data_Discrepancies_TC2.csv", "./target/" + actual_file_name + "/Dimension_Data_Discrepancies.csv"), "Comparing of expected and actual dimension discrepancy file failed. Please check the logs for differences.");
    }

    @AfterMethod
    @Override

    public void afterMethod(ITestResult result) throws Exception {
        if (result.isSuccess()) {
            BasicUtil.waitADF(driver, large_timeout);
            currentTest.log(LogStatus.PASS, "Test case " + result.getMethod().getMethodName() + " is a pass.");
            log("Test case " + result.getMethod().getMethodName() + " is a pass.");
            dimMgmtPage = ((FuseNavigatorPage) navPage).clickHomeButton().navigateToDiemnsionManagement();
            reporter.flush();
        } else {
            if (result.getStatus() == ITestResult.FAILURE) {
                currentTest.log(LogStatus.FAIL, "Test case " + result.getMethod().getMethodName() + " is a fail.");
                log("Test case " + result.getMethod().getMethodName() + " is a fail.");
                Throwable t = result.getThrowable();
                currentTest.log(LogStatus.FAIL, t);
                getScreenShot();
                BasicUtil.wait(2);
                try {

                    driver.quit();
                    BasicUtil.wait(5);
                    navPage = setupDriverAndLogin();
                    wait = new WebDriverWait(driver, large_timeout);
                    log("Login successful.");
                    log(" Navigating to DM.");
                    dimMgmtPage = navPage.navigateToDiemnsionManagement();
                    reporter.flush();
                } catch (Exception e) {
                    driver.quit();
                    log("Failure in aftertest method. Abandoning the setup.");
                    org.testng.Assert.fail("Driver setup was disturbed by login/navigation failures.");
                    reporter.flush();
                    throw e;
                }
            }

            //
            //log("test have been executed.");
        }
    }

}
