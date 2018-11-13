package com.bibliofreaks.action;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import sun.misc.Regexp;

import com.bibliofreaks.feed.BookInfo;
import com.bibliofreaks.feed.FeedUtil;
import com.bibliofreaks.utils.FeedFormatUtils;
import com.bibliofreaks.utils.LoadProperties;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import au.com.bytecode.opencsv.CSVReader;
public class GenerateFeedFromCsv {

	    static String author =null;
	    static FeedUtil feed = new FeedUtil();
	    private static String bookDetails = "";
	    public static Logger basicLogger                    = Logger.getLogger(GenerateFeedFromCsv.class.getName());
	    public static HashMap<String,String> hashtable = new HashMap<String,String>();
	    public static HashMap<String,ArrayList<Integer>> priceMap = new HashMap<String,ArrayList<Integer>>();
	    public static ArrayList<ArrayList<String>> allBooksImageList = new ArrayList<ArrayList<String>>();
		
		public static void getHashMapOfRequiredFields(String[] currentBook){
			
			String book = "";
			String title = "";
			int bookDataCheck = 0;
			        // nextLine[] is an array of values from the line
			    	String id = currentBook[0];
			    	System.out.println("id value in csv sheet=\t"+id);
			    	basicLogger.debug("id value in csv sheet=\t"+id);
				if (!("_id".equalsIgnoreCase(id))) {
					
//					product_id += Integer.parseInt(id);
//					System.out.println("product_id =\t"+product_id);
//			    	basicLogger.debug("product_id  =\t"+product_id);
					String isbn = currentBook[3];
					if(!nullLength(isbn)){
					System.out.println("isbn value in csv sheet=\t"+isbn);
					basicLogger.debug("isbn value in csv sheet=\t"+isbn);
					hashtable.put("ISBN", isbn);
					int defaultMRP = 300;
					if(priceMap.get(isbn)!=null){
					     
						if(priceMap.get(isbn).get(0)!=0){
								hashtable.put("MRP", String.valueOf(priceMap.get(isbn).get(0)));
								System.out.println("MRP value in csv sheet=\t"+priceMap.get(isbn).get(0));
								basicLogger.debug("MRP value in csv sheet=\t"+priceMap.get(isbn).get(0));
						}else if(0!=priceMap.get(isbn).get(2)){
							    defaultMRP= 2*priceMap.get(isbn).get(2);
								hashtable.put("MRP", ""+defaultMRP);
								System.out.println("MRP default value is double of bfsp csv sheet=\t"+defaultMRP);
								basicLogger.debug("MRP default value is double of bfsp csv sheet=\t"+defaultMRP);
						}else{
							hashtable.put("MRP", "300");
							System.out.println("MRP default value is double of bfsp csv sheet=\t"+"300");
							basicLogger.debug("MRP default value is double of bfsp csv sheet=\t"+"300");
						}
						if(0!=priceMap.get(isbn).get(2)){
							
								hashtable.put("BFSP", String.valueOf(priceMap.get(isbn).get(2)));
								System.out.println("BFSP value in csv sheet=\t"+priceMap.get(isbn).get(2));
								basicLogger.debug("BFSP value in csv sheet=\t"+priceMap.get(isbn).get(2));
						}else{
							    int defaultSp = defaultMRP/2;
								hashtable.put("BFSP", ""+defaultSp);
								System.out.println("BFSP value in csv sheet=\t"+defaultSp);
								basicLogger.debug("BFSP value in csv sheet=\t"+defaultSp);
						}
						
						if(0!=priceMap.get(isbn).get(3)&&null!=String.valueOf(priceMap.get(isbn).get(3))&&""!=String.valueOf(priceMap.get(isbn).get(3))){
							hashtable.put("WEIGHT", String.valueOf(priceMap.get(isbn).get(3)));
						}else{
							hashtable.put("WEIGHT","300");
						}
						
						
					}else{

						// putting default value of price
						hashtable.put("MRP", ""+ defaultMRP);
						System.out.println("MRP default value is double of bfsp csv sheet=\t"+defaultMRP);
						basicLogger.debug("MRP default value is double of bfsp csv sheet=\t"+defaultMRP);
						
					}
					
					title = currentBook[2];
					if(!nullLength(title)){
						title = FeedFormatUtils.formatedTitle(title);
						System.out.println("title=\t" + title);
						basicLogger.debug("title value in csv sheet after formatting is=\t"+title);
						bookDataCheck = 0;
			// Commenting this to avoid google hit unnecessary on 25092016.
           //			bookDataCheck = checkDataSheetCorrectness(isbn,title);
						System.out.println("bookDataCheck=\t"+bookDataCheck);
						basicLogger.debug("bookDataCheck=\t"+bookDataCheck);
						hashtable.put("TITLE", title);	
					}
					else {
						basicLogger.debug("title value in csv sheet=\t"+title);
						hashtable.put("TITLE", "");
					}
					
					
					}
					else{
						basicLogger.debug("isbn is value in csv sheet=\t"+isbn);
						hashtable.put("ISBN", "");
					}
					
					
					switch (bookDataCheck) {
					case 0:
					System.out.println("Inside case 0 ...");
					basicLogger.debug("Inside case 0 ...");
					System.out.println("id value in csv sheet=\t"+id);
			    	basicLogger.debug("id value in csv sheet=\t"+id);
					String author_details = currentBook[1];
					System.out.println("author_details value in csv sheet before format=\t" + currentBook[1]);
					basicLogger.debug("author_details value in csv sheet before format=\t" + currentBook[1]);
					if(!nullLength(author_details)){
					author = FeedFormatUtils.authorNameFormat(author_details);
					System.out.println("author_details value in csv sheet after format=\t" + author);
					basicLogger.debug("author_details value in csv sheet after format=\t" + author);
					hashtable.put("AUTHORS", author);
					}
					else {
						System.out.println("author_details value in csv sheet=\t" + author);
						basicLogger.debug("author_details value in csv sheet=\t" + author);
						hashtable.put("AUTHORS", "");
					}
					
					String publisher = currentBook[4];
					if(!nullLength(publisher)){
					System.out.println("publisher value in csv sheet=\t" + publisher);
					basicLogger.debug("publisher value in csv sheet=\t" + publisher);
					hashtable.put("PUBLISHER", publisher);
					}
					else{
						System.out.println("publisher value in csv sheet=\t" + publisher);
						basicLogger.debug("publisher value in csv sheet=\t" + publisher);
						hashtable.put("PUBLISHER", "");
					}
					
					String date_published = currentBook[5];
					if(!nullLength(date_published)){
					System.out.println("date_published value in csv sheet=\t" + date_published);
					basicLogger.debug("date_published value in csv sheet=\t" + date_published);
					hashtable.put("PUBLISHEDDATE", date_published);
					}
					else{
						System.out.println("date_published value in csv sheet=\t" + date_published);
						basicLogger.debug("date_published value in csv sheet=\t" + date_published);
						hashtable.put("PUBLISHEDDATE", "");
					}
					
					String series_details = currentBook[10];
					if(!nullLength(series_details)){
						
						System.out.println("series_details value in csv sheet=\t" + series_details);
						basicLogger.debug("series_details value in csv sheet=\t" + series_details);
						hashtable.put("SERIES", series_details);
					}
					else{
						System.out.println("series_details value in csv sheet=\t" + series_details);
						basicLogger.debug("series_details value in csv sheet=\t" + series_details);
						hashtable.put("SERIES", "");
					}
					
					String pages = currentBook[11];
					if(!nullLength(pages)){
						
						System.out.println("pages value in csv sheet=\t" + pages);
						basicLogger.debug("pages value in csv sheet=\t" + pages);
						hashtable.put("NOP", pages);
					}
					else{
						System.out.println("pages value in csv sheet=\t" + pages);
						basicLogger.debug("pages value in csv sheet=\t" + pages);
						hashtable.put("NOP", "");
					}
					
					String format = currentBook[18];
					if(!nullLength(format)){
						
						System.out.println("format value in csv sheet=\t" + format);
						basicLogger.debug("format value in csv sheet=\t" + format);
						hashtable.put("PRINTTYPE", format);
					}
					else{
						System.out.println("format value in csv sheet=\t" + format);
						basicLogger.debug("format value in csv sheet=\t" + format);
						hashtable.put("PRINTTYPE", "");
					}
					
					String description = currentBook[22];
					if(!nullLength(description)){
						description = FeedFormatUtils.formatedDescription(description);
						System.out.println("description value in csv sheet=\t" + description);
						basicLogger.debug("description value in csv sheet=\t" + description);
						hashtable.put("DESCRIPTION", description);
					}
					else{
						System.out.println("description value in csv sheet=\t" + description);
						basicLogger.debug("description value in csv sheet=\t" + description);
						hashtable.put("DESCRIPTION", "");
					}
					
					String genre = currentBook[23];
					if(!nullLength(genre)){
					System.out.println("genre value in csv sheet=\t" + genre);
					basicLogger.debug("genre value in csv sheet=\t" + genre);
					hashtable.put("GENRE", genre);
					}
					else{
						System.out.println("genre value in csv sheet=\t" + genre);
						basicLogger.debug("genre value in csv sheet=\t" + genre);
						hashtable.put("GENRE", "");
					}
					
					String language = currentBook[24];
					if(!nullLength(language)&&!("Unknown".equalsIgnoreCase(language))){
						System.out.println("language value in csv sheet=\t" + language);
						basicLogger.debug("language value in csv sheet=\t" + language);
						hashtable.put("LANGUAGE", language);
					}
					else{
						hashtable.put("LANGUAGE", "English");
						System.out.println("language value in csv sheet=\t" + language);
						basicLogger.debug("language value in csv sheet=\t" + language);
					}
					
					String book_image_uuid = currentBook[29];
					if(!nullLength(book_image_uuid)){
						System.out.println("book_image_uuid value in csv sheet=\t" + book_image_uuid);
						basicLogger.debug("book_image_uuid value in csv sheet=\t" + book_image_uuid);
						hashtable.put("BOOK_IMAGE_UUID", book_image_uuid);
					}
					else{
						hashtable.put("BOOK_IMAGE_UUID", "BOOK_IMAGE_UUID");
						System.out.println("language value in csv sheet=\t" + book_image_uuid);
						basicLogger.debug("language value in csv sheet=\t" + book_image_uuid);
					}
					
					/* Comment this till image name renaming program is written to take the image name from app*/
					String image1 ="";
					ArrayList<String> imagesList = new ArrayList<String>();
					if(!nullLength(book_image_uuid)){
						image1 = LoadProperties.Prop("IMAGES_FOLDER_ON_PROD")+book_image_uuid+".jpg";
					
					imagesList.add(hashtable.get("ISBN"));
					
					System.out.println("image1 value =\t" + image1);
					basicLogger.debug("image1 value =\t" + image1);
					hashtable.put("IMAGE1", image1);
					imagesList.add(image1);
					}
					else{
						hashtable.put("IMAGE1", "IMAGE1");
						System.out.println("IMAGE1 value =\t" + image1);
						basicLogger.debug("IMAGE1 value =\t" + image1);
					}
					/* this commented till image name is taken from app */
//					if(!nullLength(title)){
//						image1 = LoadProperties.Prop("IMAGES_FOLDER_ON_PROD")+FeedFormatUtils.imageNameFormat(title)+"1.JPG";
//						System.out.println("image value=\t" + image1);
//						basicLogger.debug("image value=\t" + image1);
//						hashtable.put("IMAGE1", image1);
//						
//						imagesList.add(FeedFormatUtils.imageNameFormat(title)+"1.JPG");
//					}
//					else{
//						System.out.println("image value=\t" + image1);
//						basicLogger.debug("image value=\t" + image1);
//						hashtable.put("IMAGE1", "");
//					}
					
					
					/* this should be commented as app data do not have second image */
//					String image2="";
//					if(!nullLength(title)){
//						image2 = LoadProperties.Prop("IMAGES_FOLDER_ON_PROD")+FeedFormatUtils.imageNameFormat(title)+"2.JPG";
//					System.out.println("image value=\t" + image2);
//					basicLogger.debug("image value=\t" + image2);
//					hashtable.put("IMAGE2", image2);
//					
//					imagesList.add(FeedFormatUtils.imageNameFormat(title)+"2.JPG");
//					}
//					else{
//						System.out.println("image value=\t" + image2);
//						basicLogger.debug("image value=\t" + image2);
//						hashtable.put("IMAGE2", "");
//					}
					// adding book image uuid to image rename file
//					imagesList.add(hashtable.get("BOOK_IMAGE_UUID"));
					if(!nullLength(hashtable.get("ISBN"))){
						
						
						System.out.println("Adding images names to renaming sheet...");
						
						allBooksImageList.add(imagesList);
					}
					else {
						System.out.println("Could not add image names to Imagesheet...");
						
					}
					String volume = "";
					System.out.println("volume value=\t" + volume);
					basicLogger.debug("volume value=\t" + volume);
					hashtable.put("VOLUME", volume);
					
//					String weight = "";
//					System.out.println("weight value=\t" + weight);
//					basicLogger.debug("weight value=\t" + weight);
//					hashtable.put("WEIGHT", weight);
					
					String remarks = "";
					System.out.println("remarks value=\t" + remarks);
					basicLogger.debug("remarks value=\t" + remarks);
					hashtable.put("REMARKS", remarks);
					
					break;
					
					case 1:
					
					{
						System.out.println("In case 1...");
						basicLogger.debug("In case 1...");
						System.out.println("Book title in csv sheet is not matching with title in google books...");
						basicLogger.debug("Book title in csv sheet is not matching with title in google books...");
						GenerateFeed.genFeedFromGBApi(isbn);
						if(!GenerateFeed.hashtable.isEmpty()){
								hashtable.putAll(GenerateFeed.hashtable);
								/*Comment this until we take image from app data*/
//								allBooksImageList.addAll(GenerateFeed.imageListBooks);
								image1 ="";
								book_image_uuid = currentBook[29];
								imagesList = new ArrayList<String>();
								if(!nullLength(book_image_uuid)){
									image1 = LoadProperties.Prop("IMAGES_FOLDER_ON_PROD")+book_image_uuid+".jpg";
								
								imagesList.add(hashtable.get("ISBN"));
								
								System.out.println("image1 value =\t" + image1);
								basicLogger.debug("image1 value =\t" + image1);
								hashtable.put("IMAGE1", image1);
								imagesList.add(image1);
								}
								else{
									hashtable.put("IMAGE1", "IMAGE1");
									System.out.println("IMAGE1 value =\t" + image1);
									basicLogger.debug("IMAGE1 value =\t" + image1);
								}
								
								allBooksImageList.add(imagesList);
						}else{
							System.out.println("Gf feed hashtable is empty...");
						}
						
						break;
					}
					
					default: {
						System.out.println("In default case...");
						basicLogger.debug("In default case...");
						basicLogger.debug("All values are set to empty by default...");
						hashtable.put("TITLE", "");
						hashtable.put("AUTHORS", "");
						hashtable.put("ISBN", isbn);
						hashtable.put("PUBLISHER", "");
						hashtable.put("PUBLISHEDDATE", "");
						hashtable.put("LANGUAGE", "English");
						hashtable.put("DESCRIPTION", "");
						hashtable.put("MRP", "0");
						hashtable.put("BFSP", "0");
						hashtable.put("SERIES", "");
						hashtable.put("PRINTTYPE", "");
						hashtable.put("NOP", "");
						hashtable.put("VOLUME", "");
						hashtable.put("WEIGHT", "");
					}
				}
				
					
			}
				else{
					 System.out.println("Processed input file header...");
					 basicLogger.debug("Processed input file header...");
				}
				
				
}
		
		public static void priceLoader() {
		    
			try {
				
				LoadProperties.load();
				CSVReader reader = new CSVReader(new FileReader(LoadProperties.Prop("PRICE_SHEET")));
				System.out.println("Price sheet opened successfully...");
			    String [] currentBookPriceRow;
			    //Skip the header read;
			    reader.readNext();
			    while ((currentBookPriceRow = reader.readNext()) != null) {
			    	ArrayList<Integer> al = new ArrayList<Integer>();
			    	System.out.println("Price loading for ISBN= "+currentBookPriceRow[0]);
			    	System.out.println("Adding MRP= "+Integer.parseInt(currentBookPriceRow[1]));
			    	al.add(Integer.parseInt(currentBookPriceRow[1]));
			    	System.out.println("Adding FSP= "+Integer.parseInt(currentBookPriceRow[2]));
			    	al.add(Integer.parseInt(currentBookPriceRow[2]));
			    	System.out.println("Adding BFSP= "+Integer.parseInt(currentBookPriceRow[3]));
			    	al.add(Integer.parseInt(currentBookPriceRow[3]));
//			    	String weights = currentBookPriceRow[4].split(".")[0];
			    	Integer weight = (int)Math.round(Double.parseDouble(currentBookPriceRow[4]));
			    	System.out.println("Adding Weight= "+weight);
			    	al.add(weight);
			    	priceMap.put(currentBookPriceRow[0],al);
			    }
			    
			}
			 catch (IOException localIOException) {
				 	System.out.println(localIOException);
				 	basicLogger.debug("File open has issue..."+localIOException);
		            }
		    }
		
		public static void main(String [] args){
			
			
		}
			
		
					
		public static int checkDataSheetCorrectness(String isbn,
				String title) {
			// TODO Auto-generated method stub
			String isbnlocal = isbn;
			String titlelocal = title;
			int dataCorrect = 0;
			// This is introduce delay so that google url do not throw 503 error.
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bookDetails = feed
					.hitUrl("https://www.googleapis.com/books/v1/volumes?q=isbn:"
							+ isbnlocal+"&key=AIzaSyChxy7xh894XuypN_UiK5qj-lbjjezrVf4");
//			bookDetails = feed.hitUrl("http://localhost:8080/manager/stub/books.json");
			System.out.println("Printing the bookDetails from google Api=\t" + bookDetails);
			basicLogger.debug("Printing the bookDetails from google Api=\t" + bookDetails);
			System.out.println("ISBN: " + isbn);

			Gson gson = new Gson();
			if (!nullLength(bookDetails)) {
				BookInfo info = gson.fromJson(bookDetails.toString(),
						new TypeToken<BookInfo>() {
						}.getType());

				if (info.getItems() != null && !(info.getItems().isEmpty())) {

					if ((info.getItems().get(0).getVolumeInfo() != null)) {
						if (!nullLength(info.getItems().get(0)
								.getVolumeInfo().getTitle())) {
							System.out.println("Title : "
									+ info.getItems().get(0)
											.getVolumeInfo().getTitle());
							String gtitle = FeedFormatUtils.formatedTitle(info.getItems().get(0)
							.getVolumeInfo().getTitle());
							if(gtitle.toLowerCase().contains(titlelocal.trim().toLowerCase())||titlelocal.trim().toLowerCase().contains(gtitle.toLowerCase())){
								System.out.println("Book title in csv sheet is matched with title in google books response...");
								basicLogger.debug("Book title in csv sheet is matched with title in google books response...");
								return 0;
								
							}
							else{
								return 1;
							}
							

						} else {
							System.out
									.println("title is empty in google books api response VolumeInfo...");
							basicLogger.debug("title is empty in google books api response VolumeInfo...");
							return 0;
						}

						
					} else {
						System.out
								.println("volumeInfo is empty in the items list...");
						basicLogger.debug("volumeInfo is empty in google books api response the items list...");
//						dataCorrect = 0;
						return 0;
					}

				} else {

					System.out.println("Items list is empty... ");
					basicLogger.debug("Items is empty in google books api response...");
//					dataCorrect = 0;
					return 0;
				}
			} else {

				System.out.println("bookDetails is empty... ");
				basicLogger.debug("bookDetails is empty in google books api response...");
//				dataCorrect = 0;
				return 0;
			}
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

