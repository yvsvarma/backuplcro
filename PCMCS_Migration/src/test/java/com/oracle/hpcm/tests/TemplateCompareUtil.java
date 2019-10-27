package com.oracle.hpcm.tests;

import com.oracle.hpcm.restclient.Executor;
import com.oracle.hpcm.restclient.ReturnObject;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.testng.asserts.SoftAssert;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.ElementSelectors;


/**
 * 
 * @author Mohnish
 */
public class TemplateCompareUtil {
	public static Logger log = Logger.getLogger("");
	public static void verifyDirsAreEqual(File expected, File generated, SoftAssert s_assert,Report report) 
            throws IOException {
    

	Files.walkFileTree(expected.toPath(), new SimpleFileVisitor<Path>() {
    @Override
    public FileVisitResult preVisitDirectory(Path dir,
            BasicFileAttributes attrs)
              throws IOException {
        FileVisitResult result = super.preVisitDirectory(dir, attrs);

        // get the relative file name from path "expected"
        Path relativize = expected.toPath().relativize(dir);
        // construct the path for the counterpart file in "generated"
        File otherDir = generated.toPath().resolve(relativize).toFile();
        
		log.info("=== preVisitDirectory === compare " + dir + " to " + otherDir);
		if(!(Arrays.toString(otherDir.list()).equals(Arrays.toString(dir.toFile().list())))){
			if(!dir.toFile().getAbsolutePath().contains("Preferences")){
				s_assert.fail(dir.toFile().getName()+" Folder doesnt contain same file!?!?");
				report.addComparisons(dir.toFile().getName(), "Folder doesnt contain same files between actual and expected.");
			}
		}
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
        if(!file.toFile().exists()){
        	s_assert.fail("expected ("+file+") does not exist.");
        	report.addComparisons(file.toString(), " this file does not exist in expected folder.");
        	return result;
        }
        if(!fileInOther.exists())
        {
        	report.addComparisons(file.toString(), " this file does not exist in actual folder.");
        	s_assert.fail("(actual "+fileInOther+") does not exist.");
        	return result;
        }
        //log.info("=== comparing: " + file + " to " + fileInOther);
       
        try {
        	
		
        	if(!compare(file.toFile(),fileInOther,report)){
				s_assert.fail("Actual file ("+fileInOther+") does NOT match the expected file ("+file+")!");
                                String command;
                                String OS = System.getProperty("os.name");
                               if(OS.contains("window"))
                                   command = "FC /C /N /W \""+ file.toAbsolutePath()+"\" \""+fileInOther.getAbsolutePath()+"\"";
                               else
                                   command = "diff  \""+ file.toAbsolutePath()+"\" \""+fileInOther.getAbsolutePath()+"\"";
				ReturnObject ro = Executor.runCommand(command);
				String compare ="Actual file does NOT match the expected file. Out put of FC command. :"+ ro.getOutput();
				report.addComparisons(file.toString(), compare);
        	}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}                    
        return result;
    }
});
}
	public static boolean compare(File fileExp, File fileAct,Report report)
	throws IOException, InterruptedException {
		if(fileExp.isDirectory() && fileAct.isDirectory()){
			return true;
		}
		if (fileExp.exists()) {

			if (fileAct.exists()) {
				boolean exitValue = true;
				//these folders contain non xml files and should not compared like xml but flat files.
				if(!(fileExp.getAbsolutePath().contains("ProfitCurves") || fileExp.getAbsolutePath().contains("Scatter")||
						fileExp.getAbsolutePath().contains("Dashboards") || fileExp.getAbsolutePath().contains("ApplicationUserPreferences") ||
						fileExp.getAbsolutePath().contains("Preferences")
						|| fileExp.getAbsolutePath().contains("POV") ||
						fileExp.getAbsolutePath().contains("DataGrants") ||
						fileExp.getAbsolutePath().contains("Dimensions") || 
						fileExp.getAbsolutePath().contains("SmartView") || 
						fileExp.getAbsolutePath().contains("InputData")||
						fileExp.getAbsolutePath().contains("EssbaseData")||fileExp.getAbsolutePath().contains("SmartView") ||
						fileExp.getAbsolutePath().contains("Model") ||fileExp.getAbsolutePath().contains("application.xml") ||
						fileExp.getAbsolutePath().contains("MetaData")))
				{
					log.info("comparing the xml for "+fileExp.getAbsolutePath());
					
					 Diff myDiffSimilar = DiffBuilder.compare(FileUtils.readFileToString(fileExp)).withTest(FileUtils.readFileToString(fileAct))
						     .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byName)).ignoreWhitespace()
						     .checkForSimilar()
						     .build();
					
					exitValue = !myDiffSimilar.hasDifferences();
					 /*DefaultComparisonFormatter formatter = new DefaultComparisonFormatter();
					  if (exitValue) {
					    StringBuffer expectedBuffer = new StringBuffer();
					    StringBuffer actualBuffer = new StringBuffer();
					    for (Difference d: myDiffSimilar.getDifferences()) {
					      expectedBuffer.append(formatter.getDetails(d.getComparison().getControlDetails(), null, true));
					      expectedBuffer.append("\n----------\n");

					      actualBuffer.append(formatter.getDetails(d.getComparison().getTestDetails(), null, true));
					      actualBuffer.append("\n----------\n");
					    }
					    //throw new ComparisonFailure("There are xml differences.", expectedBuffer.toString(), actualBuffer.toString());
					    log.info("Expected : \n"+expectedBuffer);
					    log.info("Actual : \n"+actualBuffer);
					  }*/
					/*if(exitValue){
						for(Difference difference : myDiffSimilar.getDifferences()){
							difference.setComparisonFormatter();
							log.info(difference.getComparison().toString(Compariso));
						}
					}*/
				}
				else{
					if(!(fileExp.getAbsolutePath().contains("EssbaseData")||
							fileExp.getAbsolutePath().contains("ProfitCurves") || 
							fileExp.getAbsolutePath().contains("Scatter")||
							fileExp.getAbsolutePath().contains("model.updated") ||
							fileExp.getAbsolutePath().contains("Application") ||
							fileExp.getAbsolutePath().contains("last.validation.success") ||fileExp.getAbsolutePath().contains("SmartView")
							||fileExp.getAbsolutePath().contains("Customer") 
							||fileExp.getAbsolutePath().contains("MetaData") 
							||fileExp.getAbsolutePath().contains("Dashboards")
							||fileExp.getAbsolutePath().contains("application.xml") 
							||fileExp.getAbsolutePath().contains("Model")
							||fileExp.getAbsolutePath().contains("DataGrants")))
						{	
							exitValue= FileUtils.contentEquals(fileExp, fileAct);
						}
					else{
						long expSize = FileUtils.sizeOf(fileExp);
						long actSize = FileUtils.sizeOf(fileAct);
						if(!((expSize*0.90) <= actSize &&  actSize <= (expSize*1.1))){
							log.info(" e :"+expSize);
							log.info(" a :"+actSize);
							//report.
							log.info(fileExp.getAbsolutePath()+" : Expected and actual files are NOT of within 5% size. these file can be widely different. Further checks are required.");
							return false;
						}
					}
				}
				if (exitValue) {
					//log.info("[CompareUtil.compare] : Expected and actual files are identical. ");
					return true;
				} else {
					log.info(fileExp.getAbsolutePath()+" : Expected and actual files are NOT identical.");
					return false;
				}

			} else {
				log.info("[CompareUtil.compare] Actual file does not exist at : " + fileAct.getAbsolutePath() + ".");
				log.info("[CompareUtil.compare] Aborting compare.");
				return false;
			}
		} else {
			log.info("[CompareUtil.compare] Expected file does not exist at : " + fileExp.getAbsolutePath() + ".");
			log.info("[CompareUtil.compare] Aborting compare.");
			return false;
		}	
	}
}