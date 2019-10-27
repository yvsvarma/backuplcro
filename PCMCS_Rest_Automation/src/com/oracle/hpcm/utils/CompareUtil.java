package com.oracle.hpcm.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

/**
 * 
 * @author Mohnish
 */
public class CompareUtil {

	public static boolean compare(String filePathExp, String filePathAct)
	throws IOException, InterruptedException {
		if (new File(filePathExp).exists()) {

			if (new File(filePathAct).exists()) {

				boolean exitValue = FileUtils.contentEquals(new File(
				filePathExp), new File(filePathAct));

				if (exitValue) {
					LogUtil.print("[CompareUtil.compare] : Expected and actual files are identical. ");
					return true;
				} else {
					LogUtil.print("[CompareUtil.compare] : Expected and actual files are NOT identical.");
					return false;
				}

			} else {
				LogUtil.print("[CompareUtil.compare] Actual file does not exist at : " + filePathAct + ".");
				LogUtil.print("[CompareUtil.compare] Aborting compare.");
				return false;
			}
		} else {
			LogUtil.print("[CompareUtil.compare] Expected file does not exist at : " + filePathExp + ".");
			LogUtil.print("[CompareUtil.compare] Aborting compare.");
			return false;
		}
	}
}