package com.bibliofreaks.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.bibliofreaks.utils.LoadProperties;

public class OCProduct {

	private static final String GETALL_ROWS_NAME_START_LETTER_ORDERED = "SELECT * FROM oc_product AS t1 JOIN oc_product_description AS t2 ON t1.product_id = t2.product_id WHERE t1.status =1 AND (t2.name LIKE ? OR t2.name LIKE ?) order by t2.name";
	private String isbn = null;
	private int quantity = 0;
    private int product_id = 0;
    private int status = 0;
    
    public static String GET_ROW_BY_ISBN_ACTIVE = "SELECT isbn,quantity FROM oc_product WHERE ISBN = ? AND STATUS =1";
	public static Logger basicLogger = Logger.getLogger(OCProduct.class.getName());
	
	public OCProduct(String isbn, int quantity, int product_id, int status) {
		// TODO Auto-generated constructor stub
		isbn = this.isbn;
		quantity = this.quantity;
		product_id = this.product_id;
		status = this.status;
	}

	public OCProduct() {
		// TODO Auto-generated constructor stub
	}

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
	 * @return the product_id
	 */
	public int getProduct_id() {
		return product_id;
	}

	/**
	 * @param product_id the product_id to set
	 */
	public void setProduct_id(int product_id) {
		this.product_id = product_id;
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

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LoadProperties.load();
		OCProduct ocp = new OCProduct();
//		ocp.setIsbn("97800995145");
//		try {
//			OCProduct currentrow = ocp.getRowByIsbnActive();
//			
//			System.out.println("data:"+currentrow.getIsbn()+"  ,  "+currentrow.getQuantity());
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		try {
			ocp.getBooksNameStartingWithLetter("S");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	public OCProduct getRowByIsbnActive() throws SQLException {

		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		OCProduct row = null;

		try {
			dbConnection = DBManager.getDBConnection();
			preparedStatement = dbConnection.prepareStatement(GET_ROW_BY_ISBN_ACTIVE);
			preparedStatement.setString(1, this.getIsbn());
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				 row = new OCProduct();
				String isbn = rs.getString("isbn");
				int quantity = rs.getInt("quantity");
				System.out.println("ISBN = " + isbn + " QUANTITY = " + quantity);
				row.setQuantity(quantity);
				row.setIsbn(isbn);
			}

		} catch (SQLException e) {

			System.out.println(e.getMessage());

		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}

		}

		return row;

	}

	public ArrayList<OCProduct> getBooksNameStartingWithLetter(String startLetter) throws SQLException {
		ArrayList<OCProduct> al = new ArrayList<OCProduct>();
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		OCProduct row = null;
		try {
			
			dbConnection = DBManager.getDBConnection();
			preparedStatement = dbConnection.prepareStatement(GETALL_ROWS_NAME_START_LETTER_ORDERED);
			startLetter = startLetter.trim();
			preparedStatement.setString(1, startLetter +"%");
			preparedStatement.setString(2, "The "+startLetter +"%");
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				row = new OCProduct();
				row.setIsbn(rs.getString("isbn"));
				row.setQuantity(rs.getInt("quantity"));
				row.setProduct_id(rs.getInt("product_id"));
				System.out.println("Row fetched : ISBN = " + row.getIsbn() + " QUANTITY = " + row.getQuantity()+" Product_id= "+row.getProduct_id());
				al.add(row);
				row.toString();
			}

		} catch (SQLException e) {

			System.out.println(e.getMessage());

		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}

		}
        return al;
	}
}
