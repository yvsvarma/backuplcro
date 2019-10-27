package com.oracle.hpcm.tests;

import com.oracle.hpcm.restclient.Executor;
import com.oracle.hpcm.restclient.ReturnObject;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import java.util.Iterator;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.testng.asserts.SoftAssert;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.Difference;
import org.xmlunit.diff.ElementSelectors;


/**
 * 
 * @author Mohnish
 * exposes a static method verifyDirsAreEqual which is the entry point
 */
public class FolderCompareUtil {
	public static Logger log = Logger.getLogger("");
	public static void verifyDirsAreEqual(File expected, File generated, SoftAssert s_assert,ExtentTest test) 
            throws IOException {
    

	Files.walkFileTree(expected.toPath(), new SimpleFileVisitor<Path>() {
    @Override
    public FileVisitResult preVisitDirectory(Path dir,
            BasicFileAttributes attrs)
              throws IOException {
        FileVisitResult result = super.preVisitDirectory(dir, attrs);

        // get the relative file name from path "expected"
        //Path relativize = expected.toPath().relativize(dir);
        // construct the path for the counterpart file in "generated"
       // File otherDir = generated.toPath().resolve(relativize).toFile();
        
		/*log.info("=== preVisitDirectory === compare " + dir + " to " + otherDir);
		if(!(Arrays.toString(otherDir.list()).equals(Arrays.toString(dir.toFile().list())))){
			if(!dir.toFile().getAbsolutePath().contains("Preferences") && !dir.toFile().getAbsolutePath().contains("ApplicationUserPreferences")){
				s_assert.fail(dir.toFile().getName()+" Folder doesnt contain same file!?!?");
				report.addComparisons(dir.toFile().getName(), "Folder doesnt contain same files between actual and expected.");
			}
		}*/
        return result;
    }
    @Override
    public FileVisitResult visitFile(Path file,
            BasicFileAttributes attrs)
            throws IOException {
        FileVisitResult result = super.visitFile(file, attrs);
        // get the relative file name from path "expected"
        Path relativize = expected.toPath().relativize(file);
        // construct the path for the counterpart file in "generated"
        File fileInOther = generated.toPath().resolve(relativize).toFile();
        
        //Complea ignore
        if(file.toFile().getAbsolutePath().contains("InputData") 
        		|| file.toFile().getAbsolutePath().contains("application.xml")
        		|| file.toFile().getAbsolutePath().contains("ApplicationUserPreferences"))
        	return result;
        if(!file.toFile().exists()){
        	s_assert.fail("expected ("+file+") does not exist.");
        	test.log( LogStatus.ERROR,file.toString() + " : this file does not exist in expected folder.");
        	return result;
        }
        if(!fileInOther.exists())
        {
        	test.log( LogStatus.ERROR,file.toString() + " : this file does not exist in actual folder.");
        	s_assert.fail("(actual "+fileInOther+") does not exist.");
        	return result;
        }
        //log.info("=== comparing: " + file + " to " + fileInOther);
       //Following code is where the  major compare occurs.. 
        try {
        	File fileExp = file.toFile();
        	File fileAct = fileInOther;
        	if(!(	fileExp.getAbsolutePath().contains("ApplicationUserPreferences") ||fileExp.getAbsolutePath().contains("Dashboards") ||
					fileExp.getAbsolutePath().contains("Preferences")|| fileExp.getAbsolutePath().contains("POV") ||
					fileExp.getAbsolutePath().contains("DataGrants") ||	fileExp.getAbsolutePath().contains("Dimensions") || 
					fileExp.getAbsolutePath().contains("SmartView") || 	fileExp.getAbsolutePath().contains("InputData")||
					fileExp.getAbsolutePath().contains("EssbaseData")||fileExp.getAbsolutePath().contains("SmartView") ||fileExp.getAbsolutePath().contains("application.xml") ||
					fileExp.getAbsolutePath().contains("MetaData") | fileExp.getAbsolutePath().contains("Model")
        		||fileExp.getAbsolutePath().contains("ProfitCurves") || fileExp.getAbsolutePath().contains("Scatter")))
			{
        		//xml files
        		if(!XMLCompare(file.toFile(),fileInOther,test)){
        			s_assert.fail("Actual file ("+fileInOther+") does NOT match the expected file ("+file+")!");
        		}
			}else{
				boolean deep_compare = true;
				String message = "";
				if(!(fileExp.getAbsolutePath().contains("EssbaseData")||
						fileExp.getAbsolutePath().contains("ProfitCurves") || 
						fileExp.getAbsolutePath().contains("Scatter")||
						fileExp.getAbsolutePath().contains("model.updated") ||
						fileExp.getAbsolutePath().contains("Application") ||
						fileExp.getAbsolutePath().contains("last.validation.success") 
						||fileExp.getAbsolutePath().contains("SmartView")
						||fileExp.getAbsolutePath().contains("Customer") 
						//||fileExp.getAbsolutePath().contains("MetaData") 
						||fileExp.getAbsolutePath().contains("Dashboards")
						||fileExp.getAbsolutePath().contains("application.xml") 
						//||fileExp.getAbsolutePath().contains("Model")
						||fileExp.getAbsolutePath().contains("DataGrants")))
						{	
							log.info("comparing the contents for "+fileExp.getAbsolutePath());
							deep_compare = FileUtils.contentEquals(fileExp, fileAct);
							if(!deep_compare ){
								message = "Actual file ("+fileInOther+") does NOT match the expected file ("+file+")!";
						}
					}else{
						if(!(fileExp.getAbsolutePath().contains("application.xml")))
						{
							long expSize = FileUtils.sizeOf(fileExp);
							long actSize = FileUtils.sizeOf(fileAct);
							log.info("comparing the size for "+fileExp.getAbsolutePath());
							if(!((expSize*0.90) <= actSize &&  actSize <= (expSize*1.1))){
								log.info(" e :"+expSize);
								log.info(" a :"+actSize);
								
								message = fileExp.getAbsolutePath()+" : Expected and actual files are NOT of within 10% size. these file can be widely different. Further checks are required.";
								log.info(message);
								deep_compare=false; 
							}
					}
				if(!deep_compare){
					s_assert.fail(message);
					ReturnObject ro = Executor.runCommand("FC /C /N /W \""+ file.toAbsolutePath()
					+"\" \""+fileInOther.getAbsolutePath()+"\"");
					String compare ="Actual file does NOT match the expected file. Out put of FC command. :";
					test.log(LogStatus.ERROR, message);
                                        test.log(LogStatus.ERROR, ro.getOutput());
				}
			}
		}			
	}catch (InterruptedException e) {
			e.printStackTrace();
		}                    
        return result;
    }
});
}
	
	public static boolean XMLCompare(File fileExp, File fileAct,ExtentTest test)throws IOException, InterruptedException {
		test.log(LogStatus.INFO,"comparing the xml for "+fileExp.getAbsolutePath());
		
		 Diff myDiffSimilar = DiffBuilder.compare(FileUtils.readFileToString(fileExp)).withTest(FileUtils.readFileToString(fileAct))
			     .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byName)).ignoreWhitespace()
			     .checkForSimilar()
			     .build();
		
		boolean exitValue = !myDiffSimilar.hasDifferences();
		
	    Iterator<Difference> iter = myDiffSimilar.getDifferences().iterator();
	    StringBuilder sb = new StringBuilder("");
	    int size = 0;
	    while (iter.hasNext()) {
	        sb.append("\n").append(iter.next().toString());
	        size++;
	    }
	    if(!exitValue && size > 0 ){
	    	test.log(LogStatus.ERROR,fileExp.getAbsolutePath()+ " has "+size+" differences.", sb.toString());
	    }
	    return exitValue;
	}
	
}