package com.bibliofreaks.feed;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import au.com.bytecode.opencsv.CSVReader;

import com.bibliofreaks.action.GenerateFeedFromCsv;
import com.bibliofreaks.action.BiblioFeed;
import com.bibliofreaks.utils.LoadProperties;


/**
 * @author naveen.bondili
 *
 */
public class FlipKartPrice {
	
	public static Logger basicLogger = Logger.getLogger(FlipKartPrice.class.getName());
	public static HashMap<String,ArrayList<Object>> flipMap = new HashMap<String,ArrayList<Object>>();
	
	
	
	public static void main(String[] args) {
		
		LoadProperties.load();
//		getBookListForFkPrice();
		double random = getRadomInRange(1,3);
		System.out.println("random num= "+ (long)(60*1000*random));
	}
	
	public static void getBookListForFkPrice(){

        
		
		String ISBN = ""; 
		try {

			FileWriter fw = new FileWriter(LoadProperties.Prop("PRICE_SHEET"),true);
			fw.write("ISBN,FkMRP,FkSP,BfSP,Weight"+"\n");
			
			CSVReader reader = new CSVReader(new FileReader(LoadProperties.Prop("BOOK_CATELOG_FILE_FOR_PRICE")));
			// System.out.println("print: 2");
			String[] currentBook;
			//to skip header
			reader.readNext();
			while ((currentBook = reader.readNext()) != null) {
                
				try {
					
					double random = getRadomInRange(1,3);
					long sleepmillisec = (long)(60*1000*random);
					System.out.println("Going to sleep for "+sleepmillisec+ "millisec....");
					Thread.sleep(sleepmillisec);
//					Thread.sleep(1000);
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("isbn read="+currentBook[3]);
//				 ISBN = Integer.parseInt(currentBook[3]);
				 ISBN = currentBook[3];
				 
				flipMap.put(ISBN,FlipKartPrice.getFilpKartPrice(ISBN) );
				System.out.println("hashmap of isbn and prices"+flipMap);
				fw.write(""+ISBN+","+flipMap.get(ISBN).get(0).toString()+","+flipMap.get(ISBN).get(1).toString()+","+flipMap.get(ISBN).get(2).toString()+","+flipMap.get(ISBN).get(3).toString()+"\n");
				flipMap.clear();
			}

			fw.close();
			reader.close();
			
		} catch (IOException localIOException) {
			System.out.println(localIOException);
			basicLogger.debug("File open has issue..." + localIOException);
		}

	
	}
   public static double getRadomInRange(int min,int max){
		
		double x = min + Math.random() * ((max - min) + 1);
		return x;
	}

	public static ArrayList<Object> getFilpKartPrice(String iSBN){
		
		ArrayList<Object> templist = new ArrayList<Object>();
		int sprice =0;
		int mprice =0;
		String tmp;
		int weight = 0;
	try {
//			System.setProperty("http.proxyHost","122.166.62.113");
//			System.setProperty("http.proxyPort","5631");
//			Document doc = Jsoup.connect("http://www.flipkart.com/it-s-not-bike-english/p/itmdun3yfjphvbku?pid="+ISBN+"&query="+ISBN).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0").get();
		    
		Response response= Jsoup.connect("http://www.flipkart.com/search?q="+iSBN)
		           .ignoreContentType(true)
		           .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")  
		           .referrer("http://www.google.com")   
		           .timeout(120000) 
		           .followRedirects(true)
		           .execute();

		Document doc = response.parse();
//		Document doc = Jsoup.connect("http://www.flipkart.com/search?q="+iSBN).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0").get();
			Elements pages= doc.select("span[class=selling-price omniture-field]");
			System.out.println(pages.text());
			Iterator<Element> spectable = doc.select("table[class=specTable]").select("tr").iterator();
			while (spectable.hasNext()) {
				Element trData = spectable.next();
				if(trData.toString().contains("Weight")) {
					Elements val = trData.select("td[class=specsValue]");
					if(null!=val){
					String x = val.text().split(" ")[0];
					if(null!=x&&x!=""){
					    weight = Integer.parseInt(x.split("\\.")[0]);
					    System.out.println("weight ="+weight);
					}
					}
					break;
				}
			}

			tmp = pages.text().replaceAll("\\D","");
			System.out.println("tmp= "+tmp);
			ArrayList al = new ArrayList<>();
			
			if(tmp != null && tmp.length() > 0){
				sprice = Integer.parseInt(tmp);
				System.out.println("sprice= "+sprice);
			} else{
				
				System.out.println("Could not fetch sprice from site...");
			}
			
			
			Elements page2= doc.select("span[class=price list]");
			tmp = page2.text().replaceAll("\\D","");
            if(tmp != null && tmp.length() > 0){
    			mprice = Integer.parseInt(tmp);
    			System.out.println("mprice= "+mprice);
            }else{
				
				System.out.println("Could not fetch mprice from site...");
			}
            
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	 
	    templist.add(mprice);
	    templist.add(sprice);
	    templist.add((int) (sprice*.65));
	    templist.add(weight);
	  return templist;
	}
	
	
  }

