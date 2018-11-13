		package com.bibliofreaks.action;
		
		import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

		import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
		




		import au.com.bytecode.opencsv.CSVReader;

import com.bibliofreaks.database.DBProductNewStock;
import com.bibliofreaks.feed.FeedUtil;
import com.bibliofreaks.feed.FlipKartPrice;
import com.bibliofreaks.feed.ImageNames;
import com.bibliofreaks.utils.FeedFormatUtils;
import com.bibliofreaks.utils.LoadProperties;
		
		public class BiblioFeed {
		
//			static String author = null;
			static FeedUtil feed = new FeedUtil();
			private static String bookDetails = "";
			public static Logger basicLogger = Logger.getLogger(BiblioFeed.class.getName());
// 			Public static HashMap<String, String> hashtable = null;
// 			Initialize this value each time with the last product id + 1  in website.
			public static int product_id = 7375;
			public static final int maxProductSheetCellCount = 43;
			public static ArrayList<String> allIsbns = new ArrayList<String>();
			public static void main(String[] args) {
				// TODO Auto-generated method stub
				LoadProperties.load();
				
//				BiblioFeed.generateBiblioFeedFromCSV();
				BiblioFeed.generateBiblioFeedFromDB();
			}
		
			
			public static void generateBiblioFeedFromDB(){

				try {
					
					String ISBN = null;
					ArrayList<DBProductNewStock> allbooks = new ArrayList<DBProductNewStock>();
					DBProductNewStock dbq = new DBProductNewStock();
					try {
						allbooks = dbq.getAllRecordsInTable();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
//					GenerateFeedFromCsv.priceLoader();
					Iterator<DBProductNewStock> itr = allbooks.iterator();
					while (itr.hasNext()) {

						DBProductNewStock bookRowFromDb = itr.next();
						System.out.println(bookRowFromDb.toString());
						
						System.out.println("isbn read= " + bookRowFromDb.getIsbn());
						// ISBN = Integer.parseInt(currentBook[3]);
						ISBN = bookRowFromDb.getIsbn();
						int quantity = bookRowFromDb.getQuantity();
						product_id = bookRowFromDb.getProductID();
						String quantitystring = quantity+"";
						String weight = bookRowFromDb.getWeight()+"";
						basicLogger.debug(LoadProperties.Prop("BOOK_CATELOG_FILE"));
						CSVReader reader = new CSVReader(new FileReader(
								LoadProperties.Prop("BOOK_CATELOG_FILE")));
						// System.out.println("print: 2");
						String[] bookRowFromCSV;
						// Skip the header reading
						reader.readNext();
						
						while ((bookRowFromCSV = reader.readNext()) != null) {
			
						if(ISBN.equals(bookRowFromCSV[3])){
						GenerateFeedFromCsv.getHashMapOfRequiredFields(bookRowFromCSV);
						if (!GenerateFeedFromCsv.hashtable.isEmpty()) {
							
							GenerateFeedFromCsv.hashtable.put("QUANTITY", quantitystring);
// 							comment this if price sheet has weight value.
							GenerateFeedFromCsv.hashtable.put("WEIGHT", "350");
							BiblioFeed
									.generateBiblioFeed(GenerateFeedFromCsv.hashtable);
//							product_id++;
						}
						
						
						 break;
						}
						
						
						}
					}
					
					if(!GenerateFeedFromCsv.allBooksImageList.isEmpty()){
						
						ImageNames.generateImageNames(GenerateFeedFromCsv.allBooksImageList);
					}else{
						
						ArrayList<ArrayList<String>> imageList = new ArrayList<ArrayList<String>>();
						ArrayList<String> al = new ArrayList<String>();
						al.add("ISBN");
						al.add("DUMMY1.JPG");
						al.add("DUMMY2.JPG");
						imageList.add(al);
						ImageNames.generateImageNames(imageList);
					   }
					
				} catch (IOException localIOException) {
					System.out.println(localIOException);
					basicLogger.debug("File open has issue..." + localIOException);
				}

			}
			
			public static void generateBiblioFeedFromCSV(){
				
				try {
					
					basicLogger.debug(LoadProperties.Prop("BOOK_CATELOG_FILE"));
					CSVReader reader = new CSVReader(new FileReader(
							LoadProperties.Prop("BOOK_CATELOG_FILE")));
					// System.out.println("print: 2");
					String[] currentBook;
					//Skip the header reading
					reader.readNext();
					if(true==Boolean.parseBoolean(LoadProperties.Prop("isPriceSheetAvailable"))){
					GenerateFeedFromCsv.priceLoader();
					}
					while ((currentBook = reader.readNext()) != null) {
		
						GenerateFeedFromCsv.getHashMapOfRequiredFields(currentBook);
						if (!GenerateFeedFromCsv.hashtable.isEmpty()) {
							BiblioFeed
									.generateBiblioFeed(GenerateFeedFromCsv.hashtable);
						}
						
						product_id++;
					}
					
					if(!GenerateFeedFromCsv.allBooksImageList.isEmpty()){
						
						ImageNames.generateImageNames(GenerateFeedFromCsv.allBooksImageList);
					}else{
						
						ArrayList<ArrayList<String>> imageList = new ArrayList<ArrayList<String>>();
						ArrayList<String> al = new ArrayList<String>();
						al.add("ISBN");
						al.add("DUMMY1.JPG");
						al.add("DUMMY2.JPG");
						imageList.add(al);
						ImageNames.generateImageNames(imageList);
					}
		
				} catch (IOException localIOException) {
					System.out.println(localIOException);
					basicLogger.debug("File open has issue..." + localIOException);
				}

			}
			public static void generateBiblioFeed(HashMap<String, String> tempMap) {
		
				FileInputStream sfeedfin = null;
				File sfeedfilepath;
				FileOutputStream sfeedfout = null;
				Workbook workb;
				HashMap<String, String> hashtable = tempMap;
				System.out.println("In generateBiblioFeed method....");
				// DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHH");
				// Date date = new Date();
				// System.out.println("date with which final feed file will be created=\t"
				// + dateFormat.format(date));
				// basicLogger.debug("date with which final feed file will be created=\t"
				// + dateFormat.format(date));
				
		
				sfeedfilepath = new File(LoadProperties.Prop("BIBLIO_FEED_FILE"));
				System.out.println(sfeedfilepath);
				try {
					sfeedfin = new FileInputStream(sfeedfilepath);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
		
					System.out.println("Biblio feed file is not found for reading...");
					e.printStackTrace();
				}
		
				
				if (sfeedfilepath != null && sfeedfilepath.exists()
						&& !(sfeedfilepath.length() <= 0)) {
		
					System.out.println("file opened successsfully for reading...");
					System.out.println("before opening work book");
					try {
						workb = WorkbookFactory.create(sfeedfin);
		
						/** This product sheet formatting.... **/
						Sheet productsheet = workb.getSheet("Products");
						System.out.println("In Products sheet...");
						basicLogger.debug("In Products sheet...");
						Row horizontalrow = productsheet.createRow(productsheet
								.getLastRowNum() + 1);
						for (int i = 0; i <= maxProductSheetCellCount; i++) {
							Cell curCel = horizontalrow.createCell(i);
							if (i == 0) {
								curCel.setCellType(Cell.CELL_TYPE_NUMERIC);
								curCel.setCellValue(product_id);
								System.out.println("In product_id column...");
								basicLogger.debug("In product_id column...");
							} else if (i == 1) {
								curCel.setCellType(Cell.CELL_TYPE_STRING);
								curCel.setCellValue(hashtable.get("TITLE"));
								System.out.println("In name column...");
								basicLogger.debug("In name column...");
							} else if (i == 2) {
								curCel.setCellType(Cell.CELL_TYPE_STRING);
								curCel.setCellValue("59");
								System.out.println("In categories column...");
								basicLogger.debug("In categories column...");
		
							}
							
							else if (i == 3) {
								
								curCel.setCellType(Cell.CELL_TYPE_STRING);
								curCel.setCellValue("BF" + product_id);
								System.out.println("In sku column...");
								basicLogger.debug("In sku column...");
							}
		
							else if (i == 4) {
								
								System.out.println("In upc column...");
								basicLogger.debug("In upc column...");
							}
		
							else if (i == 5) {
								System.out.println("In ean column...");
								basicLogger.debug("In ean column...");
							}
		
							else if (i == 6) {
								System.out.println("In jan column...");
								basicLogger.debug("In jan column...");
							}
		
//							else if (i == 7) {
//								System.out.println("In jan column...");
//								basicLogger.debug("In jan column...");
//							}
		
							else if (i == 7) {
								curCel.setCellType(Cell.CELL_TYPE_STRING);
								if (!hashtable.get("ISBN").isEmpty()
										&& hashtable.get("ISBN") != null) {
									curCel.setCellValue(hashtable.get("ISBN"));
								} else {
									System.out.println("ISBN is empty in hashmap...");
									basicLogger.debug("ISBN is empty in hashmap...");
								}
								System.out.println("In isbn column...");
								basicLogger.debug("In isbn column...");
							} 
							
							else if (i == 8) {
								System.out.println("In mpn column...");
		
								basicLogger.debug("In mpn column...");
							}
		
							else if (i == 9) {
								curCel.setCellType(Cell.CELL_TYPE_STRING);
								System.out.println("In location column...");
								basicLogger.debug("In location column...");
								curCel.setCellValue("Bangalore");
							}
		
							else if (i == 10) {
								curCel.setCellType(Cell.CELL_TYPE_NUMERIC);
								
								if (!hashtable.get("QUANTITY").isEmpty()) {
									curCel.setCellValue(Integer.parseInt(hashtable.get("QUANTITY")));
								} else {
									System.out.println("In QUANTITY is empty...");
									basicLogger.debug("In QUANTITY is empty...");
								}
							}
		
							else if (i == 11) {
								curCel.setCellType(Cell.CELL_TYPE_STRING);
								curCel.setCellValue("BF" + product_id);
								System.out.println("In model column...");
								basicLogger.debug("In model column...");
							}
		
							else if (i == 12) {
								curCel.setCellType(Cell.CELL_TYPE_STRING);
								if (!hashtable.get("AUTHORS").isEmpty()) {
									curCel.setCellValue(hashtable.get("AUTHORS"));
								} else {
									System.out.println("In author is empty...");
									basicLogger.debug("In author is empty...");
								}
								System.out.println("In author/manufacturer column...");
								basicLogger.debug("In author/manufacturer column...");
							}
		
							else if (i == 13) {
		
								System.out.println("In image_name column...");
								basicLogger.debug("In image_name column...");
								curCel.setCellType(Cell.CELL_TYPE_STRING);
								curCel.setCellValue(hashtable.get("IMAGE1"));
		
							}
		
							else if (i == 14) {
		
								System.out.println("In requiresshipping column...");
								basicLogger.debug("In requiresshipping column...");
								curCel.setCellType(Cell.CELL_TYPE_STRING);
								curCel.setCellValue("yes");
		
							}
		
							else if (i == 15) {
		
								System.out.println("In price column...");
								basicLogger.debug("In price column...");
								curCel.setCellType(Cell.CELL_TYPE_NUMERIC);
								curCel.setCellValue(Integer.parseInt(hashtable
										.get("MRP")));
		
							}
		
							else if (i == 16) {
		
								System.out.println("In points column...");
								basicLogger.debug("In points column...");
								curCel.setCellType(Cell.CELL_TYPE_NUMERIC);
								curCel.setCellValue(1);
		
							}
		
							else if (i == 17) {
		
								System.out.println("In date_added column...");
								basicLogger.debug("In date_added column...");
								// curCel.setCellType(Cell.CELL_TYPE_NUMERIC);
								// curCel.setCellValue();
		
							}
		
							else if (i == 18) {
		
								System.out.println("In date_modified column...");
								basicLogger.debug("In date_modified column...");
								// curCel.setCellType(Cell.CELL_TYPE_NUMERIC);
								// curCel.setCellValue();
		
							}
		
							else if (i == 19) {
		
								System.out.println("In date_available column...");
								basicLogger.debug("In date_available column...");
								// curCel.setCellType(Cell.CELL_TYPE_NUMERIC);
								// curCel.setCellValue();
		
							}
		
							else if (i == 20) {
		
								System.out.println("In weight column...");
								basicLogger.debug("In weight column...");
								curCel.setCellType(Cell.CELL_TYPE_NUMERIC);
								curCel.setCellValue(hashtable
										.get("WEIGHT"));
		
							}
		
							else if (i == 21) {
		
								System.out.println("In unit of weight column...");
								basicLogger.debug("In unit of weight column...");
								curCel.setCellType(Cell.CELL_TYPE_STRING);
								curCel.setCellValue("g");
		
							}
		
							else if (i == 22) {
		
								System.out.println("In length column...");
								basicLogger.debug("In length column...");
								curCel.setCellType(Cell.CELL_TYPE_NUMERIC);
								 curCel.setCellValue(0);
		
							}
		
							else if (i == 23) {
		
								System.out.println("In width column...");
								basicLogger.debug("In width column...");
								curCel.setCellType(Cell.CELL_TYPE_NUMERIC);
								 curCel.setCellValue(0);
		
							}
		
							else if (i == 24) {
		
								System.out.println("In height column...");
								basicLogger.debug("In height column...");
								curCel.setCellType(Cell.CELL_TYPE_NUMERIC);
								curCel.setCellValue(0);
		
							}
		
							else if (i == 25) {
		
								System.out.println("In length unit column...");
								basicLogger.debug("In length unit column...");
								curCel.setCellType(Cell.CELL_TYPE_STRING);
								curCel.setCellValue("cm");
		
							}
		
							else if (i == 26) {
		
								System.out.println("In statusenabled column...");
								basicLogger.debug("In statusenabled column...");
								curCel.setCellType(Cell.CELL_TYPE_STRING);
								curCel.setCellValue("true");
		
							}
		
							else if (i == 27) {
		
								System.out.println("In tax_class_id column...");
								basicLogger.debug("In tax_class_id column...");
								curCel.setCellType(Cell.CELL_TYPE_NUMERIC);
								curCel.setCellValue(0);
		
							}
		
//							else if (i == 28) {
//		
//								System.out.println("In viewed column...");
//								basicLogger.debug("In viewed column...");
//								curCel.setCellType(Cell.CELL_TYPE_NUMERIC);
//								// curCel.setCellValue(0);
//		
//							} else if (i == 30) {
//		
//								System.out.println("In language_id column...");
//								basicLogger.debug("In language_id column...");
//								curCel.setCellType(Cell.CELL_TYPE_NUMERIC);
//								curCel.setCellValue(1);
//		
//							} else if (i == 31) {
//		
//								System.out.println("In seo_ keyword column...");
//								basicLogger.debug("In seo_ keyword column...");
//								curCel.setCellType(Cell.CELL_TYPE_STRING);
//								curCel.setCellValue(hashtable.get("TITLE")
//										+ ", Buy Used Books Online, Second Hand Books Online, Preused Books Store, Cheap Books");
//		
//							}
		
							else if (i == 28) {
		
								System.out.println("In description column...");
								basicLogger.debug("In description column...");
								curCel.setCellType(Cell.CELL_TYPE_STRING);
								curCel.setCellValue(hashtable.get("DESCRIPTION"));
		
							}
		
							else if (i == 29) {
								
								System.out.println("In meta_title column...");
								basicLogger.debug("In meta_title column...");
								curCel.setCellType(Cell.CELL_TYPE_STRING);
								curCel.setCellValue("Buy "
										+ hashtable.get("TITLE")
										+ " by "
										+ hashtable.get("AUTHORS")
										+ " Online at Best Prices in India -BiblioFreaks.com");
		
							}
							else if (i == 30) {
		
								System.out.println("In meta_description column...");
								basicLogger.debug("In meta_description column...");
								curCel.setCellType(Cell.CELL_TYPE_STRING);
								curCel.setCellValue("Buy "
										+ hashtable.get("TITLE")
										+ " by "
										+ hashtable.get("AUTHORS")
										+ " Online at Best Prices in India -BiblioFreaks.com");
		
							}
		
							else if (i == 31) {
		
								System.out.println("In meta_keywords column...");
								basicLogger.debug("In meta_keywords column...");
								curCel.setCellType(Cell.CELL_TYPE_STRING);
								curCel.setCellValue(hashtable.get("TITLE")
										+ ", Buy Used Books Online, Second Hand Books Online, Preused Books Store, Cheap Books");
		
							}
		
							else if (i == 32) {
		
								System.out.println("In stock_status_id column...");
								basicLogger.debug("In stock_status_id column...");
								curCel.setCellType(Cell.CELL_TYPE_NUMERIC);
								curCel.setCellValue(5);
		
							}
		
							else if (i == 33) {
		
								System.out.println("In store_id column...");
								basicLogger.debug("In store_id column...");
								curCel.setCellType(Cell.CELL_TYPE_NUMERIC);
								curCel.setCellValue(0);
		
							}
		
							else if (i == 34) {
		
								System.out.println("In layout column...");
								basicLogger.debug("In layout column...");
								curCel.setCellType(Cell.CELL_TYPE_NUMERIC);
								// curCel.setCellValue(0);
		
							}
		
							else if (i == 35) {
		
								System.out.println("In related_ids column...");
								basicLogger.debug("In related_ids column...");
								curCel.setCellType(Cell.CELL_TYPE_STRING);
								// curCel.setCellValue();
		
							}
		
							else if (i == 36) {
		
								System.out.println("In tags column...");
								basicLogger.debug("In tags column...");
								curCel.setCellType(Cell.CELL_TYPE_STRING);
								// curCel.setCellValue();
		
							}
		
							else if (i == 37) {
		
								System.out.println("In sort order column...");
								basicLogger.debug("In sort order column...");
								curCel.setCellType(Cell.CELL_TYPE_NUMERIC);
								curCel.setCellValue(0);
		
							}
		
							else if (i == 38) {
		
								System.out.println("In substract column...");
								basicLogger.debug("In substract column...");
								curCel.setCellType(Cell.CELL_TYPE_STRING);
								curCel.setCellValue("true");
		
							}
		
							else if (i == 39) {
		
								System.out.println("In minimum column...");
								basicLogger.debug("In minimum column...");
								curCel.setCellType(Cell.CELL_TYPE_NUMERIC);
								curCel.setCellValue(1);
		
							}
		
							else {
								System.out.println("The cell range is not in bounds");
							}
		
						}
		
						System.out.println("Done with products sheet...");
						/** Formating Additonal Images Sheet  **/

//						Sheet imagessheet = workb.getSheet("AdditionalImages");
//						System.out.println("In Additional images sheet...");
//						basicLogger.debug("In Additional images sheet...");
//						Row imagesrow = imagessheet.createRow(imagessheet
//								.getLastRowNum() + 1);
//						for (int i = 0; i <= 2; i++) {
//							Cell cel = imagesrow.createCell(i);
//							switch (i) {
//		
//							case 0:
//		
//								System.out.println("In product_id column...");
//								basicLogger.debug("In product_id column...");
//								cel.setCellType(Cell.CELL_TYPE_NUMERIC);
//								cel.setCellValue(product_id);
//								break;
//		
//							case 1:
//								System.out.println("In image_name column...");
//								basicLogger.debug("In image_name column...");
//								cel.setCellType(Cell.CELL_TYPE_STRING);
//								cel.setCellValue(hashtable.get("IMAGE2"));
//								break;
//							case 2:
//								System.out.println("In sort order column...");
//								basicLogger.debug("In sort order column...");
//								cel.setCellType(Cell.CELL_TYPE_NUMERIC);
//								cel.setCellValue(0);
//								break;
//							}
//		
//						}
//						System.out.println("Additional image sheet is done...");
		
						/** Formating attributes sheet **/
						Sheet attributessheet = workb.getSheet("ProductAttributes");
						System.out.println("In Attributes sheet...");
						basicLogger.debug("In Attributes sheet...");
		
						for (int j = 0; j < 11; j++) {
		
							Row attributesrow = attributessheet
									.createRow(attributessheet.getLastRowNum() + 1);
							for (int i = 0; i <= 5; i++) {
								Cell cel = attributesrow.createCell(i);
								switch (i) {
		
								case 0:
		
									System.out.println("In product_id column...");
									basicLogger.debug("In product_id column...");
									cel.setCellType(Cell.CELL_TYPE_NUMERIC);
									cel.setCellValue(product_id);
									break;
		
								case 1:
									System.out.println("In attribute_group column...");
									basicLogger.debug("In attribute_group column...");
									cel.setCellType(Cell.CELL_TYPE_STRING);
									cel.setCellValue("Book Details");
									break;
		
								case 2:
		
									switch (j) {
		
									case 0:
										System.out
												.println("In attribute_name column...");
										basicLogger
												.debug("In attribute_name column...");
										cel.setCellType(Cell.CELL_TYPE_STRING);
										cel.setCellValue("Book Condition");
										break;
		
									case 1:
										System.out
												.println("In attribute_name column...");
										basicLogger
												.debug("In attribute_name column...");
										cel.setCellType(Cell.CELL_TYPE_STRING);
										cel.setCellValue("Author");
										break;
									case 2:
										System.out
												.println("In attribute_name column...");
										basicLogger
												.debug("In attribute_name column...");
										cel.setCellType(Cell.CELL_TYPE_STRING);
										cel.setCellValue("ISBN");
										break;
									case 3:
										System.out
												.println("In attribute_name column...");
										basicLogger
												.debug("In attribute_name column...");
										cel.setCellType(Cell.CELL_TYPE_STRING);
										cel.setCellValue("Language");
										break;
		
									case 4:
										System.out
												.println("In attribute_name column...");
										basicLogger
												.debug("In attribute_name column...");
										cel.setCellType(Cell.CELL_TYPE_STRING);
										cel.setCellValue("Binding Type");
										break;
									case 5:
										System.out
												.println("In attribute_name column...");
										basicLogger
												.debug("In attribute_name column...");
										cel.setCellType(Cell.CELL_TYPE_STRING);
										cel.setCellValue("Number of Pages");
										break;
		
									case 6:
										System.out
												.println("In attribute_name column...");
										basicLogger
												.debug("In attribute_name column...");
										cel.setCellType(Cell.CELL_TYPE_STRING);
										cel.setCellValue("Publisher");
										break;
									case 7:
										System.out
												.println("In attribute_name column...");
										basicLogger
												.debug("In attribute_name column...");
										cel.setCellType(Cell.CELL_TYPE_STRING);
										cel.setCellValue("Published Year");
										break;
									case 8:
										System.out
												.println("In attribute_name column...");
										basicLogger
												.debug("In attribute_name column...");
										cel.setCellType(Cell.CELL_TYPE_STRING);
										cel.setCellValue("Series / Sequel");
										break;
		
									case 9:
										System.out
												.println("In attribute_name column...");
										basicLogger
												.debug("In attribute_name column...");
										cel.setCellType(Cell.CELL_TYPE_STRING);
										cel.setCellValue("Volume");
										break;
		
									case 10:
										System.out
												.println("In attribute_name column...");
										basicLogger
												.debug("In attribute_name column...");
										cel.setCellType(Cell.CELL_TYPE_STRING);
										cel.setCellValue("Weight");
										break;
		
									default:
		
										System.out.println("Cel is out of range ...");
										basicLogger.debug("Cel is out of range...");
		
									}
		
									break;
		
								case 3:
		
									switch (j) {
		
									case 0:
										System.out
												.println("In attribute_text Book Condition value column...");
										basicLogger
												.debug("In attribute_text Book Condition value column...");
										cel.setCellType(Cell.CELL_TYPE_STRING);
										cel.setCellValue("Used - Good Condition");
										break;
		
									case 1:
										System.out
												.println("In attribute_text value column...");
										basicLogger
												.debug("In attribute_text value column...");
										cel.setCellType(Cell.CELL_TYPE_STRING);
										cel.setCellValue(hashtable.get("AUTHORS"));
										break;
									case 2:
										System.out
												.println("In attribute_text ISBN value column...");
										basicLogger
												.debug("In attribute_text ISBN value column...");
										cel.setCellType(Cell.CELL_TYPE_STRING);
										cel.setCellValue(hashtable.get("ISBN"));
										break;
									case 3:
										System.out
												.println("In attribute_text language value column...");
										basicLogger
												.debug("In attribute_text language value column...");
										cel.setCellType(Cell.CELL_TYPE_STRING);
										cel.setCellValue(hashtable.get("LANGUAGE"));
										break;
		
									case 4:
										System.out
												.println("In attribute_text Binding Type value column...");
										basicLogger
												.debug("In attribute_text Binding Type value column...");
										cel.setCellType(Cell.CELL_TYPE_STRING);
										cel.setCellValue(hashtable.get("PRINTTYPE"));
										break;
									case 5:
										System.out
												.println("In attribute_text NOP value column...");
										basicLogger
												.debug("In attribute_text NOP value column...");
										cel.setCellType(Cell.CELL_TYPE_STRING);
										cel.setCellValue(hashtable.get("NOP"));
										break;
		
									case 6:
										System.out
												.println("In attribute_text Publisher value column...");
										basicLogger
												.debug("In attribute_text Publisher value column...");
										cel.setCellType(Cell.CELL_TYPE_STRING);
										cel.setCellValue(hashtable.get("PUBLISHER"));
										break;
									case 7:
										System.out
												.println("In attribute_text published date value column...");
										basicLogger
												.debug("In attribute_text published date value column...");
										cel.setCellType(Cell.CELL_TYPE_STRING);
										cel.setCellValue(hashtable.get("PUBLISHEDDATE"));
										break;
									case 8:
										System.out
												.println("In attribute_text series value column...");
										basicLogger
												.debug("In attribute_text series value column...");
										cel.setCellType(Cell.CELL_TYPE_STRING);
										cel.setCellValue(hashtable.get("SERIES"));
										break;
		
									case 9:
										System.out
												.println("In attribute_text volume value column...");
										basicLogger
												.debug("In attribute_text volume value column...");
										cel.setCellType(Cell.CELL_TYPE_STRING);
										cel.setCellValue(hashtable.get("VOLUME"));
										break;
		
									case 10:
										System.out
												.println("In attribute_text Weight value column...");
										basicLogger
												.debug("In attribute_text Weight value column...");
										cel.setCellType(Cell.CELL_TYPE_NUMERIC);
										cel.setCellValue(hashtable.get("WEIGHT"));
										break;
		
									default:
		
										System.out.println("Cel is out of range ...");
										basicLogger.debug("Cel is out of range...");
		
									}
		
									break;
								}
		
							}
						}
						System.out.println("Done with attributes sheet...");
		
						/** Formating specials sheet **/
						Sheet specialssheet = workb.getSheet("Specials");
						System.out.println("In specials sheet...");
						basicLogger.debug("In specials sheet...");
						Row specialsrow = specialssheet.createRow(specialssheet
								.getLastRowNum() + 1);
						for (int i = 0; i <= 5; i++) {
							Cell cel = specialsrow.createCell(i);
		
							switch (i) {
		
							case 0:
		
								System.out.println("In product_id column...");
								basicLogger.debug("In product_id column...");
								cel.setCellType(Cell.CELL_TYPE_NUMERIC);
								cel.setCellValue(product_id);
								break;
		
							case 1:
								System.out.println("In customer_group column...");
								basicLogger.debug("In customer_group column...");
								cel.setCellType(Cell.CELL_TYPE_STRING);
								cel.setCellValue("Default");
								break;
							case 2:
								System.out.println("In priority column...");
								basicLogger.debug("In priority column...");
								cel.setCellType(Cell.CELL_TYPE_NUMERIC);
								cel.setCellValue(0);
								break;
		
							case 3:
								System.out.println("In price column...");
								basicLogger.debug("In price column...");
								cel.setCellType(Cell.CELL_TYPE_NUMERIC);
								if (null != hashtable.get("BFSP")
										&& hashtable.get("BFSP").length() > 0) {
									cel.setCellValue(hashtable.get("BFSP"));
								} else {
									System.out.println("Setting default selling price");
									cel.setCellValue(99);
								}
		
								break;
		
							case 4:
								System.out.println("In date_start column...");
								basicLogger.debug("In date_start column...");
								cel.setCellType(Cell.CELL_TYPE_NUMERIC);
								cel.setCellValue("0000-00-00");
								break;
							case 5:
								System.out.println("In date_end column...");
								basicLogger.debug("In date_end column...");
								cel.setCellType(Cell.CELL_TYPE_NUMERIC);
								cel.setCellValue("0000-00-00");
								break;
		
							}
						}
		
						System.out.println("Done with specials sheet...");
		
						/** Formating rewards sheet **/
						Sheet rewardssheet = workb.getSheet("Rewards");
						System.out.println("In rewards sheet...");
						basicLogger.debug("In rewards sheet...");
						Row rewardsrow = rewardssheet.createRow(rewardssheet
								.getLastRowNum() + 1);
						for (int i = 0; i <= 2; i++) {
		
							Cell cel = rewardsrow.createCell(i);
		
							switch (i) {
		
							case 0:
		
								System.out.println("In product_id column...");
								basicLogger.debug("In product_id column...");
								cel.setCellType(Cell.CELL_TYPE_NUMERIC);
								cel.setCellValue(product_id);
								break;
		
							case 1:
								System.out.println("In customer_group column...");
								basicLogger.debug("In customer_group column...");
								cel.setCellType(Cell.CELL_TYPE_STRING);
								cel.setCellValue("Default");
								break;
							case 2:
								System.out.println("In points column...");
								basicLogger.debug("In points column...");
								cel.setCellType(Cell.CELL_TYPE_NUMERIC);
								cel.setCellValue(1);
								break;
		
							default:
								System.out.println("cel is out of case...");
								basicLogger.debug("cel is out of case...");
								// cel.setCellType(Cell.CELL_TYPE_NUMERIC);
								// cel.setCellValue(Integer.parseInt(hashtable.get("BSP")));
								break;
		
							}
						}
		
						System.out.println("Done with rewards sheet...");
						try {
							sfeedfout = new FileOutputStream(sfeedfilepath);
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							
							System.out.println("Shop2Grab feed file is not found for writing...");
							e.printStackTrace();
						}
						workb.write(sfeedfout);
						System.out.println("About to close writing file");
						
					} catch (InvalidFormatException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		
				}
				else{
					System.out.println("File path is not accessible...");
				}
		
				try {
					sfeedfout.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
				try {
					sfeedfin.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
		}
