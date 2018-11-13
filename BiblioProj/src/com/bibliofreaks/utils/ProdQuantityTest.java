package com.bibliofreaks.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.bibliofreaks.action.GenerateFeed;
import com.bibliofreaks.database.DBProductNewStock;
import com.bibliofreaks.database.OCProduct;

public class ProdQuantityTest {
	
	public static Logger basicLogger                    = Logger.getLogger(ProdQuantityTest.class.getName());
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HashMap<String, Integer> bookquantmaps = new HashMap<String, Integer>();
		LoadProperties.load();
		
		try {

			String isbn = null;
			BufferedReader br = new BufferedReader(new FileReader(LoadProperties.Prop("SCAN_FILE_PATH")));
			while ((isbn = br.readLine()) != null) {
				basicLogger.debug("isbn value read from text file is=\t"+isbn);
				if(null!=bookquantmaps&&bookquantmaps.containsKey(isbn)){
					int count = bookquantmaps.get(isbn);
					count++;
					bookquantmaps.put(isbn, count);
				}else{
					bookquantmaps.put(isbn,1);
				}
				
			}
		} catch (IOException localIOException) {
			System.out.println(localIOException);
			basicLogger.debug("file open has issue.."+localIOException);
		}
		
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));  
		System.out.println("Enter Starting Letter of books to check scan quantities....");  
		String startLetter = null;
		try {
			startLetter = br.readLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// Now run the checks...
		try {
			runQuantCheck(bookquantmaps, startLetter);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void runQuantCheck(HashMap<String, Integer> scanMap,String startLetter) throws IOException{
		
		File quantRSFile = new File(LoadProperties.Prop("SITE_QUANT_UPDATE_FILE"));
		FileWriter quantDiffResultFile;
		int scanquantity = 0;
		ArrayList<OCProduct> resultrows  = new ArrayList<OCProduct>();
		OCProduct ocplocal = new OCProduct();
		try {
			resultrows = ocplocal.getBooksNameStartingWithLetter(startLetter);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int total =0;
		for(OCProduct bookrow:resultrows){
			total++;
				int sitequantity = bookrow.getQuantity();
				String siteisbn = bookrow.getIsbn();
				int product_id = bookrow.getProduct_id();
				if(scanMap.containsKey(siteisbn)){
					
					System.out.println("siteisbn "+siteisbn+" is available in scanlist");
					scanquantity = scanMap.get(siteisbn);
					if(scanquantity==sitequantity){
						System.out.println("scanquantity "+scanquantity+" is equal to sitequantity"+ sitequantity);
					}
					else{
//						if(scanquantity<sitequantity){
						System.out.println("ISBN= "+siteisbn+" is having scanqunatity= "+scanquantity+"   sitequantity= "+sitequantity);
						
						
								quantDiffResultFile = new FileWriter(quantRSFile, true);
								quantDiffResultFile.write("UPDATE oc_product SET quantity ="+ scanquantity +" WHERE ISBN="+siteisbn+" AND status =1 AND product_id ="+product_id+";\n");
								quantDiffResultFile.flush();
								quantDiffResultFile.close();
							
//						 } 
//						 else{
//							 
//							    quantDiffResultFile = new FileWriter(quantRSFile, true);
//								quantDiffResultFile.write("UPDATE oc_product SET quantity ="+ scanquantity +" WHERE ISBN="+siteisbn+" AND status =1 AND product_id ="+product_id+";\n");
//								quantDiffResultFile.flush();
//								quantDiffResultFile.close();
//							System.out.println("ISBN= "+siteisbn+" is having scanqunatity= "+scanquantity+"   sitequantity= "+sitequantity+"-- Ignoring");
//						}
					}
					
					System.out.println("ScanList "+total+". ISBN="+siteisbn+", COUNT="+scanMap.get(siteisbn));
				}
				else{
					System.out.println("siteisbn is not avialable in scanlist"+siteisbn);
					quantDiffResultFile = new FileWriter(quantRSFile, true);
					quantDiffResultFile.write("UPDATE oc_product SET status = 0 WHERE ISBN="+siteisbn+" AND product_id ="+product_id+";\n");
					quantDiffResultFile.flush();
					quantDiffResultFile.close();
					System.out.println("ScanList "+total+". ISBN="+siteisbn);
				}
				
			
			
			
		}
	}
	

}
