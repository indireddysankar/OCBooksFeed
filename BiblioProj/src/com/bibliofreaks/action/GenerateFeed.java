package com.bibliofreaks.action;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.log4j.Logger;

import com.bibliofreaks.feed.BookInfo;
import com.bibliofreaks.feed.FeedUtil;
import com.bibliofreaks.utils.FeedFormatUtils;
import com.bibliofreaks.utils.LoadProperties;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class GenerateFeed {
	private static String bookDetails = "";
	
	public static Logger basicLogger                    = Logger.getLogger(GenerateFeed.class.getName());
	public static HashMap<String, String> hashtable;
	public static ArrayList<ArrayList<String>> imageListBooks = new ArrayList<ArrayList<String>>();
	public static void main(String[] args) throws Exception {
		
		String isbn = "";
		GenerateFeed gf = new GenerateFeed();

		try {

			String liner = null;

			BufferedReader br = new BufferedReader(new FileReader(
					"D:\\Personal\\BiblioFreaks\\feedcreation\\testisbns.txt"));
			while ((isbn = br.readLine()) != null) {
				basicLogger.debug("isbn value read from text file is=\t"+isbn);
				GenerateFeed.genFeedFromGBApi(isbn);
				System.out.println("\n");
				
			}
		} catch (IOException localIOException) {
			System.out.println(localIOException);
			basicLogger.debug("file open has issue.."+localIOException);
		}
		// feed.fileformatter(hashtable);
	}

	public static void genFeedFromGBApi(String isbnparam) {
		// TODO Auto-generated method stub
		String isbn = isbnparam;
		hashtable = new HashMap<String, String>();
		FeedUtil feed = new FeedUtil();

		// BufferedReader br = new BufferedReader(
		// new FileReader(
		// "D:\\Personal\\BiblioFreaks\\feedcreation\\bookdetailsjson.json"));
		// StringBuilder bibilo = new StringBuilder();
		// while ((liner = br.readLine()) != null) {
		// bibilo.append(liner);
		// }
		// bookDetails = bibilo.toString();
		// System.out.println("Printing the bookDetails: " + bookDetails);

		// this is to avoid loading google api url
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bookDetails = feed
				.hitUrl("https://www.googleapis.com/books/v1/volumes?q=isbn:"
						+ isbn + "&key=AIzaSyChxy7xh894XuypN_UiK5qj-lbjjezrVf4");
		
//		bookDetails = feed.hitUrl("http://localhost:8080/manager/stub/books.json");
		System.out.println("Printing the bookDetails: " + bookDetails);
		System.out.println("ISBN: " + isbn);
		hashtable.put("ISBN", isbn);
		basicLogger.debug("isbn value passed is=\t"+isbn);
		
		Gson gson = new Gson();
		if (!nullLength(bookDetails)) {
			BookInfo info = gson.fromJson(bookDetails.toString(),
					new TypeToken<BookInfo>() {
					}.getType());

			if (info.getItems() != null && !(info.getItems().isEmpty())) {

				if ((info.getItems().get(0).getVolumeInfo() != null)) {
					if (!nullLength(info.getItems().get(0).getVolumeInfo()
							.getTitle())) {
						System.out.println("Title : "
								+ info.getItems().get(0).getVolumeInfo()
										.getTitle());
						hashtable.put("TITLE", info.getItems().get(0)
								.getVolumeInfo().getTitle());
						basicLogger.debug("title in gb api response=\t"+info.getItems().get(0).getVolumeInfo()
								.getTitle());

					} else {
						System.out.println("title is empty in VolumeInfo...");
						basicLogger.debug("title is empty in VolumeInfo...");
						hashtable.put("TITLE", "");
					}

					if ((info.getItems().get(0).getVolumeInfo().getAuthors() != null)
							&& (!(info.getItems().get(0).getVolumeInfo()
									.getAuthors().isEmpty()))) {
						if (!nullLength(info.getItems().get(0).getVolumeInfo()
								.getAuthors().get(0))) {

							String author = info.getItems().get(0)
									.getVolumeInfo().getAuthors().toString();
							author = author.replace("[", "");
							author = author.replace("]", "");
							System.out.println("Authors : " + author);
							basicLogger.debug("Authors in gb api response=\t"+author);
							hashtable.put("AUTHORS", author);
						} else {
							System.out
									.println("Authors in gb api response is empty...");
							basicLogger.debug("Authors is in gb api response is empty");
							hashtable.put("AUTHORS", "");
						}
					} else {
						System.out
								.println("Authors Arraylist is empty in VolumeInfo...");
						basicLogger.debug("Authors Arraylist is empty in VolumeInfo...");
						hashtable.put("AUTHORS", "");
					}

					if (!nullLength(info.getItems().get(0).getVolumeInfo()
							.getPublisher())) {
						System.out.println("publisher : "
								+ info.getItems().get(0).getVolumeInfo()
										.getPublisher());
						basicLogger.debug("publisher in gb api response=\t"+info.getItems().get(0).getVolumeInfo()
								.getPublisher());
						hashtable.put("PUBLISHER", info.getItems().get(0)
								.getVolumeInfo().getPublisher());
					} else {
						System.out
								.println("publisher is empty in VolumeInfo... ");
						basicLogger.debug("publisher is empty in VolumeInfo... ");
						hashtable.put("PUBLISHER", "");
					}

					if (!nullLength(info.getItems().get(0).getVolumeInfo()
							.getPublishedDate())) {
						System.out.println("publishedDate : "
								+ info.getItems().get(0).getVolumeInfo()
										.getPublishedDate());
						basicLogger.debug("publishedDate in gb api response=\t"+info.getItems().get(0).getVolumeInfo()
								.getPublishedDate());
						hashtable.put("PUBLISHEDDATE", info.getItems().get(0)
								.getVolumeInfo().getPublishedDate());
					} else {
						System.out
								.println("publishedDate is empty in VolumeInfo...");
						basicLogger.debug("publishedDate is empty in VolumeInfo...");
						hashtable.put("PUBLISHEDDATE", "");
					}

					if (!nullLength(info.getItems().get(0).getVolumeInfo()
							.getLanguage())) {
						if (info.getItems().get(0).getVolumeInfo()
								.getLanguage().equals("en")) {
							System.out.println("language is set as English for en value of gb...");
							basicLogger.debug("language is set as English for en value of gb...");							
							hashtable.put("LANGUAGE", "English");
						} else {
							System.out
									.println("language is not english in VolumeInfo...");
							basicLogger.debug("language is not english in VolumeInfo...");
							hashtable.put("LANGUAGE", info.getItems().get(0)
									.getVolumeInfo().getLanguage());
						}

					} else {
						System.out
								.println("language is empty in VolumeInfo...");
						basicLogger.debug("language is empty in VolumeInfo...");
						hashtable.put("LANGUAGE", "English");
					}

					if (!nullLength(info.getItems().get(0).getVolumeInfo()
							.getDescription())) {
						
						String description = info.getItems().get(0).getVolumeInfo()
								.getDescription(); 
						System.out.println("description in gb response=\t"
								+description);
						basicLogger.debug("description in gb response=\t"
								+ description);
						description = FeedFormatUtils.formatedDescription(description);
						description = description.replace("\"", "'");
						hashtable.put("DESCRIPTION",description);
						
					} else {
						System.out
								.println("description is empty in VolumeInfo...");
						basicLogger.debug("description is empty in VolumeInfo...");
						hashtable.put("DESCRIPTION", "");

					}
					String image1 ="";
					ArrayList<String> imagesList = new ArrayList<String>();
					imagesList.add("ISBN");
					if(!nullLength(hashtable.get("TITLE"))){
						image1 = LoadProperties.Prop("IMAGES_FOLDER_ON_PROD")+FeedFormatUtils.imageNameFormat(hashtable.get("TITLE"))+"1.JPG";
						System.out.println("image value=\t" + image1);
						basicLogger.debug("image value=\t" + image1);
						hashtable.put("IMAGE1", image1);
						
						imagesList.add(FeedFormatUtils.imageNameFormat(hashtable.get("TITLE"))+"1.JPG");
					}
					else{
						System.out.println("image value=\t" + image1);
						basicLogger.debug("image value=\t" + image1);
						hashtable.put("IMAGE1", "");
					}
					
					String image2="";
					if(!nullLength(hashtable.get("TITLE"))){
						image2 = LoadProperties.Prop("IMAGES_FOLDER_ON_PROD")+FeedFormatUtils.imageNameFormat(hashtable.get("TITLE"))+"2.JPG";
					System.out.println("image value=\t" + image2);
					basicLogger.debug("image value=\t" + image2);
					hashtable.put("IMAGE2", image2);
					
					imagesList.add(FeedFormatUtils.imageNameFormat(hashtable.get("TITLE"))+"2.JPG");
					}
					else{
						System.out.println("image value=\t" + image2);
						basicLogger.debug("image value=\t" + image2);
						hashtable.put("IMAGE2", "");
					}
					
					if(!nullLength(hashtable.get("ISBN"))){
						
						System.out.println("Adding images names to renaming sheet...");
						
						imageListBooks.add(imagesList);
					}
					else {
						System.out.println("Could not add image names to Imagesheet...");
						
					}
					
					hashtable.put("PRINTTYPE", "");
					hashtable.put("NOP", "");
					hashtable.put("VOLUME", "");
					hashtable.put("WEIGHT", "");
//					feed.fileformatter(hashtable);

				} else {
					System.out
							.println("volumeInfo is empty in the items list...");
					basicLogger.debug("volumeInfo is empty in the items list...");
				}

			} else {

				System.out.println("Items list is empty... ");
				basicLogger.debug("Items list is empty... ");
			}
		} else {

			System.out.println("bookDetails is empty... ");
			basicLogger.debug("bookDetails is empty... ");
		}
	}

	// JsonParser parser = new JsonParser();
	// JsonArray array = parser.parse(br).getAsJsonArray();
	// System.out.println(array.get(0));
	// GsonBuilder gsonb = new GsonBuilder();
	// Gson gson = new Gson();
	// BookInfo info = gson.fromJson(bibilo.toString(),
	// new TypeToken<BookInfo>() {
	// }.getType());
	// System.out.println(info.getKind());
	// System.out.println(info.getTotalItems());
	// System.out.println("Title : "+info.items.get(0).getVolumeInfo().getTitle());
	// System.out.println("Authors : "+info.getItems().get(0).getVolumeInfo().getAuthors());
	// System.out.println("publisher : "+info.getItems().get(0).getVolumeInfo().getPublisher());
	// System.out.println("publishedDate : "+info.getItems().get(0).getVolumeInfo().getPublishedDate());
	// System.out.println("language : "+info.getItems().get(0).getVolumeInfo().getLanguage());
	// System.out.println("description : "+info.getItems().get(0).getVolumeInfo().getDescription());
	// System.out.println("ISBN : "+info.getItems().get(0).getVolumeInfo().getIndustryIdentifiers().get(1).getIdentifier());
	// }

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
