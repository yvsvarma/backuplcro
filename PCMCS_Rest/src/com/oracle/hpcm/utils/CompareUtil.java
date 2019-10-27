package com.oracle.hpcm.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;


/**
 * 
 * @author Mohnish
 */
public class CompareUtil {
	public static Logger logger = Logger.getLogger("c");
	public static boolean compare(String filePathExp, String filePathAct)
	throws IOException, InterruptedException {
		if (new File(filePathExp).exists()) {

			if (new File(filePathAct).exists()) {

				boolean exitValue = FileUtils.contentEquals(new File(
				filePathExp), new File(filePathAct));

				if (exitValue) {
					logger.info("[CompareUtil.compare] : Expected and actual files are identical. ");
					return true;
				} else {
					logger.severe("[CompareUtil.compare] : Expected and actual files are NOT identical.");
					return false;
				}

			} else {
				logger.severe("[CompareUtil.compare] Actual file does not exist at : " + filePathAct + ".");
				logger.severe("[CompareUtil.compare] Aborting compare.");
				return false;
			}
		} else {
			logger.severe("[CompareUtil.compare] Expected file does not exist at : " + filePathExp + ".");
			logger.severe("[CompareUtil.compare] Aborting compare.");
			return false;
		}
	}
        
        public static boolean compareFiles(String file1, String file2) throws Exception {
            BufferedReader br1 = null;
            BufferedReader br2 = null;
            String sCurrentLine;
            List<String> list1 = new ArrayList<String>();
            List<String> list2 = new ArrayList<String>();
            br1 = new BufferedReader(new FileReader(file1));
            br2 = new BufferedReader(new FileReader(file2));
            while ((sCurrentLine = br1.readLine()) != null) {
                list1.add(sCurrentLine);
            }
            while ((sCurrentLine = br2.readLine()) != null) {
                list2.add(sCurrentLine);
            }
            List<String> tmpList = new ArrayList<String>(list1);
            tmpList.removeAll(list2);
            System.out.println("content from expected which is not there in actual");
            for(int i=0;i<tmpList.size();i++){
                System.out.println(tmpList.get(i)); 
            }
            int count1 = tmpList.size();
            System.out.println("content from actual which is not there in expected.");

            tmpList = list2;
            tmpList.removeAll(list1);
            for(int i=0;i<tmpList.size();i++){
                System.out.println(tmpList.get(i)); 
            }
            int count2 = tmpList.size();
           if(count1 == 0 && count2 == 0)
               return true;
           return false;
        }

}