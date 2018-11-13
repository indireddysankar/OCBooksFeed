package com.bibliofreaks.feed;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.bibliofreaks.database.DBProductNewStock;
import com.bibliofreaks.utils.LoadProperties;

import au.com.bytecode.opencsv.CSVReader;

public class CrawlForBookData {

	public static Logger basicLogger = Logger.getLogger(CrawlForBookData.class.getName());
	public static HashMap<String, ArrayList<Object>> flipMap = new HashMap<String, ArrayList<Object>>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LoadProperties.load();
		crawlforbookdetails();
	}

	public static void crawlforbookdetails(){
		String ISBN = null;
		
		ArrayList<DBProductNewStock> allbooks = new ArrayList<DBProductNewStock>();
		DBProductNewStock dbq = new DBProductNewStock();
		try {
			allbooks = dbq.getAllRecordsInTable();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Iterator<DBProductNewStock> itr = allbooks.iterator();
		while (itr.hasNext()) {

			DBProductNewStock curbook = itr.next();
			curbook.toString();
			try {
				
				double random = FlipKartPrice.getRadomInRange(1,3);
				long sleepmillisec = (long)(60*1000*random);
				System.out.println("Going to sleep for "+sleepmillisec+ " millisec....");
				Thread.sleep(sleepmillisec);
				// Thread.sleep(60*1000);
				Thread.sleep(1000);

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("isbn read= " + curbook.getIsbn());
			// ISBN = Integer.parseInt(currentBook[3]);
			ISBN = curbook.getIsbn();
			if (null != ISBN) {
				flipMap.put(ISBN, FlipKartPrice.getFilpKartPrice(ISBN));
				System.out.println("hashmap of isbn and prices fetched from fk" + flipMap);
				curbook.setFk_mrp((int) flipMap.get(ISBN).get(0));
				curbook.setFk_sp((int) flipMap.get(ISBN).get(1));
				curbook.setBf_sp((int) flipMap.get(ISBN).get(2));
				if ((flipMap.get(ISBN).get(3) == null) || ((int) flipMap.get(ISBN).get(3) < 50)) {
					curbook.setWeight(300);
				} else {
					curbook.setWeight((int) flipMap.get(ISBN).get(3));
				}

				try {
					curbook.updatePriceAndWeightByIsbn();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				flipMap.clear();

			}
		}

	}
	
}
