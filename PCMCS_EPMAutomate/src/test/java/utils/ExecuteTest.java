package utils;
import java.io.IOException;
public class ExecuteTest {
	public static void main(String[] args) throws IOException, InterruptedException {
		ReturnObject ro;
		Executor.runCommand("cd C:\\Oracle\\EPM Automate\\bin");
		Executor.runCommand("epmautomate login epm_default_cloud_admin password1 http://slc10vic.us.oracle.com:9000");
		ro = Executor.runCommand("epmautomate listfiles");	
		if(ro.getOutput().contains("template.zip")){
			Executor.runCommand("epmautomate deletefile profitinbox/template.zip");
		}
		Executor.runCommand("epmautomate uploadfile \"c:\\template.zip\" profitinbox");
		Executor.runCommand("epmautomate importtemplate BksML12 template.zip");
		Executor.runCommand("epmautomate deploycube BksML12 isKeepData=true isReplaceCube=true isRunNow=true comment=\"Test Cube Deploy\"");
		Executor.runCommand("epmautomate copypov BksML12 2014_January_Actual 2014_March_Actual isManageRule=true isInputData=true isAdjustmentValues=true isAllocatedValues=true createDestPOV=true DELIMITER=”_”");
		Executor.runCommand("epmautomate clearpov BksML12 2014_March_Actual isManageRule =true isInputData =true isAdjustmentValues=true isAllocatedValues=true DELIMITER=”_”");
		Executor.runCommand("epmautomate deletepov BksML12 2014_March_Actual 2016_May_Actual DELIMITER=\"_\"");
		Executor.runCommand("epmautomate runcalc BksML12 2014_January_Actual isClearCalculated=true isExecuteCalculations=true isRunNow=true subsetStart=10 subsetEnd =20 ruleSetName=\"Utilities Expense Adjustment\" ruleName =\"Occupancy Expense Allocations\" exeType=\"ALL_RULES\" comment=\"Test calculation\" DELIMITER=\"_\"");
		Executor.runCommand("epmautomate exporttemplate BksML12 BksML12_Template.zip");
		Executor.runCommand("epmautomate exportqueryresults BksML12 queryName =\"Profitability-Products\" fileName=\"MyQuery1.txt\" exportOnlyLevel0Flg=true");
		Executor.runCommand("epmautomate logout");
	}
}
