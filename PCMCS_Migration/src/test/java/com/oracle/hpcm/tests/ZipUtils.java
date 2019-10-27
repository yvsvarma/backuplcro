/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oracle.hpcm.tests;
import net.lingala.zip4j.core.ZipFile; 
/**
 *
 * @author mbanchho
 */
public class ZipUtils {
        public static void unzip(String targetZipFilePath, String destinationFolderPath) {
        try {
            ZipFile zipFile = new ZipFile(targetZipFilePath);
            zipFile.extractAll(destinationFolderPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
