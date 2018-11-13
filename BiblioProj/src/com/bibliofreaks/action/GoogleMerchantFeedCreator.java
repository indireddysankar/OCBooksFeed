package com.bibliofreaks.action;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import au.com.bytecode.opencsv.CSVReader;

public class GoogleMerchantFeedCreator {

	private static String bookDetails = "";
	public static Logger basicLogger = Logger
			.getLogger(GoogleMerchantFeedCreator.class.getName());

	public static void main(String[] args) throws Exception {

		HashMap<String, String> h = new HashMap<String, String>();
		try {

			CSVReader reader = new CSVReader(
					new FileReader(
							"D:\\Personal\\BiblioFreaks\\feedcreation\\stockdata2565_Nov8thGSource.csv"));
			String[] currentBook;
			reader.readNext();
			while ((currentBook = reader.readNext()) != null) {
				h = GoogleMerchantFeedCreator.dataLoader(currentBook);
				feedCreator(h);
			}
		} catch (IOException localIOException) {
			System.out.println(localIOException);
			basicLogger.debug("File open has issue..." + localIOException);
		}

	}

	public static void feedCreator(HashMap<String, String> h) {
		FileWriter fstream;
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHH");
		Date date = new Date();
		// System.out.println("date with which final feed file will be created=\t"
		// + dateFormat.format(date));
		// basicLogger.debug("date with which final feed file will be created=\t"
		// + dateFormat.format(date));
		try {

			fstream = new FileWriter(
					"D:\\Personal\\BiblioFreaks\\feedcreation\\GoogleMerchantFeed_"
							+ dateFormat.format(date) + ".csv", true);

			BufferedWriter feedfile = new BufferedWriter(fstream);

			File myFile = new File(
					"D:\\Personal\\BiblioFreaks\\feedcreation\\GoogleMerchantFeed_"
							+ dateFormat.format(date) + ".csv");
			if (myFile != null && myFile.exists() && myFile.length() <= 0) {
				feedfile.write("\"id\",\"title\",\"Author\",\"google_product_category\",\"product_type\",\"link\",\"condition\",\"price\",\"sale_price\",\"image_link\",\"additional_image_link\",\"availability\",\"ISBN\",\"identifier_exists\",\"description\"");
			}
			feedfile.write("\n");
			feedfile.append("\"" + h.get("id") + "\",\"" + h.get("title")
					+ "\",\"" + h.get("Author") + "\",\""
					+ h.get("google_product_category") + "\",\""
					+ h.get("product_type") + "\",\"" + h.get("link") + "\",\""
					+ h.get("condition") + "\",\"" + h.get("price") + "\",\""
					+ h.get("sale_price") + "\",\"" + h.get("image_link")
					+ "\",\"" + h.get("additional_image_link") + "\",\""
					+ h.get("availability") + "\",\"" + h.get("ISBN") + "\",\""
					+ h.get("identifier_exists") + "\",\""
					+ h.get("description") + "\"");
			try {
				feedfile.close();
			} catch (Exception e) {
				System.err.println("Error: " + e.getMessage());
				basicLogger.debug("Error: " + e.getMessage());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static HashMap<String, String> dataLoader(String[] currentBook) {
		// TODO Auto-generated method stub
		String id = "";
		String title = "";
		String author = "";
		String link = "";
		String price = "";
		String sale_price = "";
		String image_link = "";
		String additional_image_link = "";
		String description = "";
		String isbn = "";
		String aboutBook = "";
		String firstrow = "";
		HashMap<String, String> data = new HashMap<String, String>();
		id = currentBook[11];
		System.out.println("id value in csv sheet=\t" + id);
		basicLogger.debug("id value in csv sheet=\t" + id);
		data.put("id", id);

		title = currentBook[0];
		System.out.println("title value in csv sheet=\t" + title);
		basicLogger.debug("title value in csv sheet=\t" + title);
		data.put("title", title);

		data.put("google_product_category", "Media > Books");
		data.put("product_type", "Fictions and Novels");

		link = currentBook[21];
		System.out.println("link value in csv sheet=\t" + link);
		basicLogger.debug("link value in csv sheet=\t" + link);
		data.put("link", link);

		data.put("condition", "used");

		image_link = currentBook[4];
		System.out.println("image_link value in csv sheet=\t" + image_link);
		basicLogger.debug("image_link value in csv sheet=\t" + image_link);
		data.put("image_link", image_link);

		additional_image_link = currentBook[5];
		System.out.println("additional_image_link value in csv sheet=\t"
				+ additional_image_link);
		basicLogger.debug("additional_image_link value in csv sheet=\t"
				+ additional_image_link);
		data.put("additional_image_link", additional_image_link);

		data.put("availability", "in stock");

		data.put("identifier_exists", "");

		description = currentBook[9];
		
		if(!description.contains("<p><strong>Title :&nbsp;</strong>")){
			description = "<p><strong>Title :&nbsp;</strong></p>"+description;
		}
		System.out.println("description value in csv sheet=\t"
				+ description);
		basicLogger.debug("description value in csv sheet=\t"
				+ description);
		
		try{

		boolean found = false;
		Pattern pattern = Pattern.compile("(</strong>)(.*?)(</p>)");
		Matcher matcher = pattern.matcher(description);
		matcher.find();
		if (matcher.group(0) != null) {
			firstrow = matcher.group(0);
			firstrow = firstrow.replace("</strong>", "");
			firstrow = firstrow.replace("</p>", "");
			// data.put("title", name1);
			System.out.println("first row in html:\t" + firstrow);
			basicLogger.debug("first row in html:\t" + firstrow);
			found = true;
		}
		if (!found) {
			System.out.println("first row match not found.");
			basicLogger.debug("first row match not found.");
		}

		found = false;
		if (title.equalsIgnoreCase(firstrow)
				|| !((nullLength(currentBook[18])) && (nullLength(currentBook[19])))) {
			
			
			System.out.println("Got price from new format row");
			basicLogger.debug("Got price from new format row");
			price = currentBook[18];
			System.out.println("price value in csv sheet=\t" + price);
			basicLogger.debug("price value in csv sheet=\t" + price);
			data.put("price", price);
			sale_price = String
					.valueOf((Integer.valueOf(currentBook[18]) - Integer
							.valueOf(currentBook[19])));
			System.out.println("sale_price value in csv sheet=\t" + sale_price);
			basicLogger.debug("sale_price value in csv sheet=\t" + sale_price);
			data.put("sale_price", sale_price);
			matcher.find();
			if (matcher.group(0) != null) {
				author = matcher.group(0);
				author = author.replace("</strong>", "");
				author = author.replace("</p>", "");
				System.out.println("Author:\t" + author);
				basicLogger.debug("Author:\t" + author);
				data.put("Author", author);
				found = true;
			}
			if (!found) {
				System.out.println("Author match not found.");
				basicLogger.debug("Author match not found.");
			}
			matcher.find();
			if (matcher.group(0) != null) {
				isbn = matcher.group(0);
				isbn = isbn.replace("</strong>", "");
				isbn = isbn.replace("</p>", "");
				isbn = isbn.replaceAll("<strong>&nbsp;", "");
				System.out.println("isbn:\t" + isbn);
				basicLogger.debug("isbn:\t" + isbn);
				data.put("ISBN", isbn);
				found = true;
			}
			if (!found) {
				System.out.println("isbn match not found.");
				basicLogger.debug("isbn match not found.");
			}
			
			pattern = Pattern.compile("(<p>)(.*?)(</p>)");
			matcher = pattern.matcher(description);
			for (int i = 0; i < 10; i++) {
				matcher.find();
			}
			if (matcher.group(0) != null) {
				aboutBook = matcher.group(0);
				aboutBook = formatBookDescription(aboutBook);

				if(aboutBook.equals("About the Book :")) {
					matcher.find();
					aboutBook = matcher.group(0);
					aboutBook = formatBookDescription(aboutBook);
				}
				
				
				System.out.println("description:\t" + aboutBook);
				basicLogger.debug("description:\t" + aboutBook);
				data.put("description", aboutBook);
				found = true;
			}
			if (!found) {
				System.out.println("description match not found.");
				basicLogger.debug("description match not found.");
			}

		}

		else {
			System.out.println("Got old format row");

			 matcher.find();
//			 matcher.find();
			if (matcher.group(0) != null) {
				author = matcher.group(0);
				author = author.replace("</strong>", "");
				author = author.replace("</p>", "");
				System.out.println("Author:\t" + author);
				basicLogger.debug("Author:\t" + author);
				data.put("Author", author);
				found = true;
			}
			if (!found) {
				System.out.println("Author match not found.");
				basicLogger.debug("Author match not found.");
			}
			matcher.find();
			if (matcher.group(0) != null) {
				isbn = matcher.group(0);
				isbn = isbn.replace("</strong>", "");
				isbn = isbn.replace("</p>", "");
				isbn = isbn.replaceAll("<strong>&nbsp;", "");
				data.put("ISBN", isbn);
				System.out.println("isbn:\t" + isbn);
				basicLogger.debug("isbn:\t" + isbn);
				found = true;
			}
			if (!found) {
				System.out.println("isbn match not found.");
				basicLogger.debug("isbn match not found.");
			}

			price = currentBook[13];
			System.out.println("price:\t" + price);
			basicLogger.debug("price:\t" + price);
			data.put("price", currentBook[13]);

			sale_price = String.valueOf(Integer.valueOf(currentBook[13])
					- Integer.valueOf(currentBook[14]));
			System.out.println("sale_price:\t" + sale_price);
			basicLogger.debug("sale_price:\t" + sale_price);
			System.out.println("Got sale price from old format row");
			data.put("sale_price", sale_price);
			
			pattern = Pattern.compile("(<p>)(.*?)(</p>)");
			matcher = pattern.matcher(description);
			for (int i = 0; i < 10; i++) {
				matcher.find();
			}
			
			if (matcher.group(0) != null) {
				aboutBook = matcher.group(0);
				aboutBook = formatBookDescription(aboutBook);
				
				if(aboutBook.equals("About the Book :")) {
					matcher.find();
					aboutBook = matcher.group(0);
					aboutBook = formatBookDescription(aboutBook);
				}
				
				System.out.println("description:\t" + aboutBook);
				basicLogger.debug("description:\t" + aboutBook);
				data.put("description", aboutBook);
				found = true;
			}
			if (!found) {
				System.out.println("description match not found.");
				basicLogger.debug("description match not found.");
			}
		}
		
		}catch(Exception e){
			System.out.println("Data is not proper");
		}
		return data;
	}

	private static String formatBookDescription(String aboutBook) {
		// TODO Auto-generated method stub
		
		aboutBook = aboutBook.replace("<p>", "");
		aboutBook = aboutBook.replace("</p>", "");
		aboutBook = aboutBook.replace("<strong>About the Book :&nbsp;</strong>","");
		aboutBook = aboutBook.replace("\"", "");
		aboutBook = aboutBook.replaceAll("<br />", "");
		aboutBook = aboutBook.replaceAll("&nbsp;", "");
		aboutBook = aboutBook.replaceAll("<strong>", "");
		aboutBook = aboutBook.replaceAll("<br>", "");
		aboutBook = aboutBook.replaceAll("<em>Cell</em>", "");
		aboutBook = aboutBook.replaceAll("’", "");
		aboutBook = aboutBook.replaceAll("�", "");
		aboutBook = aboutBook.replaceAll("</em>", "");
		aboutBook = aboutBook.replaceAll("<em>", "");
		aboutBook = aboutBook.replaceAll("–", "");
		aboutBook = aboutBook.replaceAll("</strong>", "");
		aboutBook = aboutBook.replaceAll("é", "");
		aboutBook = aboutBook.replaceAll("…", "");
		aboutBook = aboutBook.replaceAll("&amp;", "");
		aboutBook = aboutBook.replaceAll("�", "");
		aboutBook = aboutBook.replaceAll("\\[\\]", "");
		aboutBook = aboutBook.replaceAll("<span>", "");
		aboutBook = aboutBook.replaceAll("</span>", "");
		aboutBook = aboutBook.replaceAll("só", "");
		aboutBook = aboutBook.replaceAll("—", "");
		aboutBook = aboutBook.replaceAll("ão", "");
		aboutBook = aboutBook.replaceAll("lê", "");
		aboutBook = aboutBook.replaceAll("á", "");
		aboutBook = aboutBook.replaceAll("ñi", "");
		return aboutBook;
	}

	/**
	 * Checks a string and returns false if the string is (null,"","null")
	 * 
	 * @param strToCheck
	 *            ---- the string to be validated
	 * @return ---boolean value---- if the string is a valid string
	 */
	public static boolean nullLength(String strToCheck) {
		if (strToCheck == null || strToCheck.trim().length() < 1
				|| strToCheck.trim().equalsIgnoreCase("")
				|| strToCheck.equalsIgnoreCase("null"))
			return true;
		return false;
	}
}
