/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oracle.hpcm.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

    private List<String> fileList;
    public String outputZipFile;
    public String sourceFolder; // SourceFolder path

    public ZipUtil(String sourceFolder, String outputZipFile) {
        this.sourceFolder = sourceFolder;
        this.outputZipFile = outputZipFile;
        fileList = new ArrayList<String>();
    }

    public void zipper() {
        this.generateFileList(new File(sourceFolder));
        System.out.println(outputZipFile);
        this.zipIt(outputZipFile);

    }

    public static void compress(String source, String destination) {
        byte[] buffer = new byte[1024];

        try {

            FileOutputStream fos = new FileOutputStream(destination);
            ZipOutputStream zos = new ZipOutputStream(fos);
            ZipEntry ze = new ZipEntry("file");
            zos.putNextEntry(ze);
            FileInputStream in = new FileInputStream(source);

            int len;
            while ((len = in.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }

            in.close();
            zos.closeEntry();

            //close os
            zos.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public void zipIt(String zipFile) {
        byte[] buffer = new byte[1024];
        String source = "";
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            try {
                source = sourceFolder.substring(sourceFolder.lastIndexOf("\\") + 1, sourceFolder.length());
            } catch (Exception e) {
                source = sourceFolder;
            }
            fos = new FileOutputStream(zipFile);
            zos = new ZipOutputStream(fos);

            //System.out.println("Output to Zip : " + zipFile);
            FileInputStream in = null;

            for (String file : this.fileList) {
                // System.out.println("File Added : " + file);
                ZipEntry ze = new ZipEntry(source + File.separator + file);
                zos.putNextEntry(ze);
                try {
                    in = new FileInputStream(sourceFolder + File.separator + file);
                    int len;
                    while ((len = in.read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }
                } finally {
                    in.close();
                }
            }

            zos.closeEntry();
            //System.out.println("Folder successfully compressed");

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                zos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void generateFileList(File node) {

        // add file only
        if (node.isFile()) {
            fileList.add(generateZipEntry(node.toString()));

        }

        if (node.isDirectory()) {
            String[] subNote = node.list();
            for (String filename : subNote) {
                generateFileList(new File(node, filename));
            }
        }
    }

    private String generateZipEntry(String file) {
        return file.substring(sourceFolder.length() + 1, file.length());
    }
    private static final int BUFFER_SIZE = 4096;

    /**
     * Extracts a zip file specified by the zipFilePath to a directory specified
     * by destDirectory (will be created if does not exists)
     *
     * @param zipFilePath
     * @param destDirectory
     * @throws IOException
     */
    public static void unzip(String zipFilePath, String destDirectory) throws IOException, FileNotFoundException {
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
        // iterates over entries in the zip file
        System.out.println(entry.getName() + "-" + entry.isDirectory());
        while (entry != null) {
            String filePath = destDirectory + File.separator + entry.getName();

            if (!entry.isDirectory()) {
                //make directory if it doesnot exist
                File dir = new File(new File(filePath).getParent());
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                // if the entry is a file, extracts it
                extractFile(zipIn, filePath);
            } else {
                // if the entry is a directory, make the directory
                File dir = new File(filePath);
                dir.mkdir();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
    }

    /**
     * Extracts a zip entry (file entry)
     *
     * @param zipIn
     * @param filePath
     * @throws IOException
     */
    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }
     public static void unzipper(String zipFilePath, String destDir) {
        File dir = new File(destDir);
        // create output directory if it doesn't exist
        if(!dir.exists()) dir.mkdirs();
        FileInputStream fis;
        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while(ze != null){
                String fileName = ze.getName();
                File newFile = new File(destDir + File.separator + fileName);
                System.out.println("Unzipping to "+newFile.getAbsolutePath());
                  //create directories for sub directories in zip
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                if(ze.getSize()!=0)
                {
                    zis.closeEntry();
                    ze = zis.getNextEntry();
                    continue;
                }
                try{
                while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
                }
                }catch(ZipException zex){
                    zis = new ZipInputStream(fis);
                }
                fos.close();
                //close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            //close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    
}
