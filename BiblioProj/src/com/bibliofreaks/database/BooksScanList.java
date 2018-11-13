package com.bibliofreaks.database;

import org.apache.log4j.Logger;

public class BooksScanList {

	private String isbn = null;
	private int quantity = 0;
	private int status = 0;
	private String rack =null;
	public static String GETALL_ISBNANDQUANTITY = "SELECT isbn,quantity,fk_mrp,fk_sp,bf_sp,weight,product_id,isNew FROM oc_product_newstock WHERE isnew=1 order by product_id";
	
	public static Logger basicLogger = Logger.getLogger(BooksScanList.class.getName());

	/**
	 * @return the isbn
	 */
	public String getIsbn() {
		return isbn;
	}

	/**
	 * @param isbn the isbn to set
	 */
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}


	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	
	
}

