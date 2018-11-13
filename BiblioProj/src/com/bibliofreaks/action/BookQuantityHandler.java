package com.bibliofreaks.action;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.bibliofreaks.database.DBProductNewStock;
import com.bibliofreaks.utils.LoadProperties;

import au.com.bytecode.opencsv.CSVReader;

public class BookQuantityHandler {

	private int quanity = 0;
	private String isbn = null;
	public static Logger basicLogger = Logger.getLogger(BookQuantityHandler.class.getName());

	BookQuantityHandler() {
	};

	public BookQuantityHandler(int quanity, String isbn) {
		super();
		this.quanity = quanity;
		this.isbn = isbn;
	}

	public int getQuanity() {
		return quanity;
	}

	public void setQuanity(int quanity) {
		this.quanity = quanity;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LoadProperties.load();

		try {

			basicLogger.debug(LoadProperties.Prop("BOOK_CATELOG_FILE_TO_DB"));
			CSVReader reader = new CSVReader(new FileReader(LoadProperties.Prop("BOOK_CATELOG_FILE_TO_DB")));
			// System.out.println("print: 2");
			String[] currentBook;
			// Skip the header reading
			reader.readNext();
			while ((currentBook = reader.readNext()) != null) {

				processCurrentBooktoDb(currentBook);
			}
		} catch (IOException localIOException) {
			System.out.println(localIOException);
			basicLogger.debug("File BOOK_CATELOG_FILE_TO_DB open has issue..." + localIOException);
		}

	}

	public static void processCurrentBooktoDb(String[] book) {

		String[] localbook = book;
		String isbn = localbook[3];
		int quantityindb = 0;
		int newquantity = 1;
		String isbnmatched = null;
		DBProductNewStock row = null;
		int rowcountaffected = -1;
		int isnew = 1;
		if (!nullLength(isbn)) {
			System.out.println("isbn value in csv sheet=\t" + isbn);
			basicLogger.debug("isbn value in csv sheet=\t" + isbn);
			DBProductNewStock dbq = new DBProductNewStock();
			dbq.setIsbn(isbn);
			try {
				
				row = dbq.getRowByIsbn();
						if(null!=row){
							quantityindb = row.getQuantity();
							isbnmatched = row.getIsbn();
							/* new quantity to db */
							newquantity = quantityindb + newquantity;
							row.setQuantity(newquantity);
							row.setIsNew(isnew);
							rowcountaffected = row.updateQuantityByIsbn();
							System.out.println("rowcountaffected= "+rowcountaffected);
						}else{
							System.out.println("Isbn is not present in db the query gave empty resultset for the isbn");
							System.out.println("Going to add to table");
							dbq.setQuantity(newquantity);
							dbq.setIsNew(isnew);
							rowcountaffected = dbq.insertQuantityByIsbn();
							System.out.println("rowcountaffected= "+rowcountaffected);
						}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
		if (strToCheck == null || strToCheck.trim().length() < 1 || strToCheck.trim().equalsIgnoreCase("")
				|| strToCheck.equalsIgnoreCase("null"))
			return true;
		return false;
	}
}