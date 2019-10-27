/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.oracle.hpcm.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil
{

private List<String> fileList;
public String outputZipFile;
public String sourceFolder ; // SourceFolder path

public ZipUtil(String sourceFolder,String outputZipFile)
{
    this.sourceFolder=sourceFolder;
    this.outputZipFile=outputZipFile;
   fileList = new ArrayList<String>();
}

public  void zipper()
{
   this.generateFileList(new File(sourceFolder));
   System.out.println(outputZipFile);
   this.zipIt(outputZipFile);
   
}
public static void compress(String source, String destination){
	byte[] buffer = new byte[1024];
	 
	try{

		FileOutputStream fos = new FileOutputStream(destination);
		ZipOutputStream zos = new ZipOutputStream(fos);
		ZipEntry ze= new ZipEntry("file");
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

	}catch(IOException ex){
	   ex.printStackTrace();
	}

}
public void zipIt(String zipFile)
{
   byte[] buffer = new byte[1024];
   String source = "";
   FileOutputStream fos = null;
   ZipOutputStream zos = null;
   try
   {
      try
      {
         source = sourceFolder.substring(sourceFolder.lastIndexOf("\\") + 1, sourceFolder.length());
      }
     catch (Exception e)
     {
        source = sourceFolder;
     }
     fos = new FileOutputStream(zipFile);
     zos = new ZipOutputStream(fos);

     //System.out.println("Output to Zip : " + zipFile);
     FileInputStream in = null;

     for (String file : this.fileList)
     {
       // System.out.println("File Added : " + file);
        ZipEntry ze = new ZipEntry(source + File.separator + file);
        zos.putNextEntry(ze);
        try
        {
           in = new FileInputStream(sourceFolder + File.separator + file);
           int len;
           while ((len = in.read(buffer)) > 0)
           {
              zos.write(buffer, 0, len);
           }
        }
        finally
        {
           in.close();
        }
     }

     zos.closeEntry();
     //System.out.println("Folder successfully compressed");

  }
  catch (IOException ex)
  {
     ex.printStackTrace();
  }
  finally
  {
     try
     {
        zos.close();
     }
     catch (IOException e)
     {
        e.printStackTrace();
     }
  }
}

public void generateFileList(File node)
{

  // add file only
  if (node.isFile())
  {
     fileList.add(generateZipEntry(node.toString()));

  }

  if (node.isDirectory())
  {
     String[] subNote = node.list();
     for (String filename : subNote)
     {
        generateFileList(new File(node, filename));
     }
  }
}

private String generateZipEntry(String file)
{
   return file.substring(sourceFolder.length() + 1, file.length());
}
} 
