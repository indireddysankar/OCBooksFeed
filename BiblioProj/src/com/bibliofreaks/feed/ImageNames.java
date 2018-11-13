package com.bibliofreaks.feed;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.bibliofreaks.utils.LoadProperties;

public class ImageNames {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LoadProperties.load();
		ArrayList<ArrayList<String>> imageList = new ArrayList<ArrayList<String>>();
		ArrayList<String> al = new ArrayList<String>();
		al.add("ISBN");
		al.add("IMA1");
		al.add("IMA2");
		ImageNames.generateImageNames(imageList);
		
	}

	public static void generateImageNames(ArrayList<ArrayList<String>> imageList){
		
		
		File imagenamefile = new File(LoadProperties.Prop("IMAGES_NAMES_FILE"));
		try {
			FileWriter imagefout;
			try {
				imagefout = new FileWriter(imagenamefile, true);
				imagefout.write("ISBN,IMAGE1,IMAGE2"+"\n");
				Iterator<ArrayList<String>> itr1 = imageList.iterator();
				
			while(itr1.hasNext()){
				ArrayList<String> imlist = itr1.next();
				/* comment the below if additional image is not available*/
//				String  line = imlist.get(0)+","+imlist.get(1)+","+imlist.get(2)+"\n";
				String  line = imlist.get(0)+","+imlist.get(1)+"\n";
				imagefout.write(line);
				}
		
			  System.out.println("writing images names to file is over...");
			  imagefout.flush();
			  imagefout.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		 }
		
		
	}

}