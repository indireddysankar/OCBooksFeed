package com.bibliofreaks.action;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.apache.log4j.Logger;

import au.com.bytecode.opencsv.CSVReader;

import com.bibliofreaks.utils.LoadProperties;

public class RenamingImageFiles {
	public static Logger basicLogger = Logger.getLogger(RenamingImageFiles.class.getName());
	static int numberOfLinesInFile = 0;
	public static void renameImageFiles() {
    	
    	LoadProperties.load();
        // change file names in 'Directory':
        try {
        	
        	basicLogger.debug(LoadProperties.Prop("IMAGES_NAMES_FILE"));
			CSVReader reader = new CSVReader(new FileReader(
					LoadProperties.Prop("IMAGES_NAMES_FILE")));
			// System.out.println("print: 2");

			try {
				          numberOfLinesInFile = countLines(LoadProperties.Prop("IMAGES_NAMES_FILE"));
				          // remove header into the count
				          numberOfLinesInFile = numberOfLinesInFile-1;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	String absolutePath = LoadProperties.Prop("IMAGES_FOLDER");
        basicLogger.debug(LoadProperties.Prop("IMAGES_FOLDER"));
        File dir = new File(absolutePath);
        File[] filesInDir = dir.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				if(name.endsWith(".JPG")){
					System.out.println(name);
					return true;
				}
					
				return false;
			}
		});
        
        for(File testfile:filesInDir){
        
        	System.out.println(testfile.getName());
        }
        
        System.out.println("filesInDir length = "+filesInDir.length+"  numberOfLinesInFile = "+numberOfLinesInFile);
        Collections.sort(Arrays.asList(filesInDir));
        if(filesInDir.length==2*numberOfLinesInFile){
        	boolean iseven = true;
        	try{

        			String[] ImagesNameRow;
        			// skip first row
        			ImagesNameRow = reader.readNext();
        	for(int i=0;i<2*numberOfLinesInFile;i=i+2) {
        			File frontImage = filesInDir[i];
        			File backImage = filesInDir[i+1];
		            String frontImageName = frontImage.getName();
		            System.out.println("frontImage="+frontImageName);
		            String backImageName = backImage.getName();
		            System.out.println("backImageName="+backImageName);
					ImagesNameRow = reader.readNext();
					   
							String frontImagePath = absolutePath + "/" + ImagesNameRow[1];
							frontImage.renameTo(new File(frontImagePath));
				            System.out.println(frontImageName + " changed to " + ImagesNameRow[1]);
							String backImagePath = absolutePath + "/" + ImagesNameRow[2];
							backImage.renameTo(new File(backImagePath));
				            System.out.println(backImageName + " changed to " + ImagesNameRow[2]);
		            
        		}
        		}catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
  
        	}else{
        		System.out.println("The file counts are not double of the number of rows in names sheet...");
        	}
        } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    } // close function
	
	public static int countLines(String filename) throws IOException {
	    InputStream is = new BufferedInputStream(new FileInputStream(filename));
	    try {
	        byte[] c = new byte[1024];
	        int count = 0;
	        int readChars = 0;
	        boolean empty = true;
	        while ((readChars = is.read(c)) != -1) {
	            empty = false;
	            for (int i = 0; i < readChars; ++i) {
	                if (c[i] == '\n') {
	                    ++count;
	                }
	            }
	        }
	        return (count == 0 && !empty) ? 1 : count;
	    } finally {
	        is.close();
	    }
	}
    
    public static void main(String []args){
    	
    	RenamingImageFiles.renameImageFiles();
    }
} // close class
