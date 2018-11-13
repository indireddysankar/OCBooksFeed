package com.bibliofreaks.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.bibliofreaks.utils.LoadProperties;

public class DBProductNewStock {

	private String isbn = null;
	private int quantity = 0;
	private int fk_mrp = 0;
    private int fk_sp = 0;
    private int bf_sp = 0;
    private int weight = 300;
    private int product_id = 0;
    private int isNew = 0;
	public static String GETALL_ISBNANDQUANTITY = "SELECT isbn,quantity,fk_mrp,fk_sp,bf_sp,weight,product_id,isNew FROM oc_product_newstock WHERE isnew=1 order by product_id";
	public static String GET_ROW_BY_ISBN = "SELECT isbn,quantity FROM oc_product_newstock WHERE ISBN = ?";
	public static String UPDATE_QUANTITY_BY_ISBN = "UPDATE oc_product_newstock SET QUANTITY = ?,isnew = ? WHERE ISBN = ?";
	public static String UPDATE_PRICE_WEIGHT_BY_ISBN = "UPDATE oc_product_newstock SET FK_MRP = ?,FK_SP = ?,BF_SP = ?,WEIGHT = ? WHERE ISBN = ?";
	public static String IS_ISBN_AVIALABLE = "SELECT * FROM oc_product_newstock WHERE ISBN = ?";
	public static String INSERT_QUANT_ISBN = "INSERT INTO oc_product_newstock (ISBN, QUANTITY,ISNEW) VALUES (?,?,?)";
	public static String INSERT_PRICE_WEIGHT_ISBN = "INSERT INTO oc_product_newstock (ISBN,FK_MRP,FK_SP,BF_SP,WEIGHT) VALUES (?,?,?,?,?)";
	public static String DELETE_ROW_BY_ISBN = "DELETE FROM oc_product_newstock WHERE ISBN = ?";
	public static String GET_ROW_BY_ISBN_ACTIVE = "SELECT isbn,quantity FROM oc_product_newstock WHERE ISBN = ?";
	public static Logger basicLogger = Logger.getLogger(DBProductNewStock.class.getName());

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getFk_mrp() {
		return fk_mrp;
	}

	public void setFk_mrp(int fk_mrp) {
		this.fk_mrp = fk_mrp;
	}

	public int getFk_sp() {
		return fk_sp;
	}

	public void setFk_sp(int fk_sp) {
		this.fk_sp = fk_sp;
	}

	public int getBf_sp() {
		return bf_sp;
	}

	public void setBf_sp(int bf_sp) {
		this.bf_sp = bf_sp;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	public int getProductID() {
		return product_id;
	}

	public void setProductID(int product_id) {
		this.product_id = product_id;
	}
	
	/**
	 * @return the isNew
	 */
	public int getIsNew() {
		return isNew;
	}

	/**
	 * @param isNew the isNew to set
	 */
	public void setIsNew(int isNew) {
		this.isNew = isNew;
	}

	public DBProductNewStock() {
	};

	DBProductNewStock(String isbnNum, int quant,int fk_mrp,int fk_sp,int bf_sp,int weight,int product_id,int isNew) {
		this.isbn = isbnNum;
		this.quantity = quant;
		this.fk_mrp = fk_mrp;
		this.fk_sp = fk_sp;
		this.bf_sp = bf_sp;
		this.weight = weight;
		this.product_id =product_id;
		this.isNew = isNew;
	}

	public static void main(String args[]) {

		LoadProperties.load();
		DBProductNewStock dqa = new DBProductNewStock("TESTISBN",10,450,275,99,412,0,0);
//		dqa.setIsbn("TESTISBN");
//		dqa.setQuantity(23);
		try {
//			dqa.insertQuantityByIsbn();
			// dqa.updateQuantityByIsbn();
			 dqa.getAllRecordsInTable();
			// dqa.getRowByIsbn();
//			dqa.isIsbnAvailable();
			// dqa.deleteRowByIsbn();
//			dqa.insertPriceAndWeightByIsbn();
//			dqa = new DBProductNewStock("TESTISBN",5,301,201,99,301);
//			dqa.updatePriceAndWeightByIsbn();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isIsbnAvailable() throws SQLException {
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		boolean isbnAvailable = false;
		try {
			dbConnection = DBManager.getDBConnection();
			preparedStatement = dbConnection.prepareStatement(IS_ISBN_AVIALABLE);
			preparedStatement.setString(1, this.getIsbn());
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next() == true) {
				isbnAvailable = true;
			} else {
				isbnAvailable = false;
			}
			System.out.println("isbnAvailable= " + isbnAvailable);
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
		return isbnAvailable;

	}

	public boolean deleteRowByIsbn() throws SQLException {
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		boolean isbnAvailable = false;
		try {
			dbConnection = DBManager.getDBConnection();
			preparedStatement = dbConnection.prepareStatement(DELETE_ROW_BY_ISBN);
			preparedStatement.setString(1, this.getIsbn());
			int rowsaffected = preparedStatement.executeUpdate();
			System.out.println("rowsaffected= " + rowsaffected);
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
		return isbnAvailable;

	}

	public int insertQuantityByIsbn() throws SQLException {

		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		int rowsaffected = -1;

		try {
			dbConnection = DBManager.getDBConnection();
			preparedStatement = dbConnection.prepareStatement(INSERT_QUANT_ISBN);
			preparedStatement.setString(1, this.getIsbn());
			preparedStatement.setInt(2, this.getQuantity());
			preparedStatement.setInt(3, this.getIsNew());
			
			
			// execute insert SQL stetement
			rowsaffected = preparedStatement.executeUpdate();

			System.out.println("Record is inserted into OC_PRODUCT_NEWSTOCK table!, rowsaffected = " + rowsaffected);

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
		return rowsaffected;
	}

	public int insertPriceAndWeightByIsbn() throws SQLException {

		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		int rowsaffected = -1;

		try {
			dbConnection = DBManager.getDBConnection();
			preparedStatement = dbConnection.prepareStatement(INSERT_PRICE_WEIGHT_ISBN);
			preparedStatement.setString(1, this.getIsbn());
			preparedStatement.setInt(2, this.getFk_mrp());
			preparedStatement.setInt(3, this.getFk_sp());
			preparedStatement.setInt(4, this.getBf_sp());
			preparedStatement.setInt(5, this.getWeight());
			preparedStatement.setInt(6, this.getIsNew());
			// execute insert SQL stetement
			rowsaffected = preparedStatement.executeUpdate();

			System.out.println("Record is inserted into OC_PRODUCT_NEWSTOCK table!, rowsaffected = " + rowsaffected);

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
		return rowsaffected;
	}

	public ArrayList<DBProductNewStock> getAllRecordsInTable() throws SQLException {
		ArrayList<DBProductNewStock> al = new ArrayList<DBProductNewStock>();
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		DBProductNewStock row = null;
		try {
			
			dbConnection = DBManager.getDBConnection();
			preparedStatement = dbConnection.prepareStatement(GETALL_ISBNANDQUANTITY);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				row = new DBProductNewStock();
				row.setIsbn(rs.getString("isbn"));
				row.setQuantity(rs.getInt("quantity"));
				row.setFk_mrp(rs.getInt("fk_mrp"));
				row.setFk_sp(rs.getInt("fk_sp"));
				row.setBf_sp(rs.getInt("bf_sp"));
				row.setWeight(rs.getInt("weight"));
				row.setProductID(rs.getInt("product_id"));
				row.setIsNew(rs.getInt("isnew"));
				System.out.println("Row fetched : ISBN = " + row.getIsbn() + " QUANTITY = " + row.getQuantity()+"FK_MRP = " + row.getFk_mrp() + " FK_SP = " + row.getFk_sp()+" BF_SP= "+row.getBf_sp()+" Weight = "+row.weight+" Product_id= "+row.getProductID()+" isnew= "+row.isNew);
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DBProductNewStock [isbn=" + isbn + ", quantity=" + quantity + ", fk_mrp=" + fk_mrp + ", fk_sp=" + fk_sp
				+ ", bf_sp=" + bf_sp + ", weight=" + weight + ", product_id=" + product_id+", isNew=" + isNew+"]";
	}

	public DBProductNewStock getRowByIsbn() throws SQLException {

		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		DBProductNewStock row = null;

		try {
			dbConnection = DBManager.getDBConnection();
			preparedStatement = dbConnection.prepareStatement(GET_ROW_BY_ISBN);
			preparedStatement.setString(1, this.getIsbn());
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				 row = new DBProductNewStock();
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

		
	public int updateQuantityByIsbn() throws SQLException {

		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		int rowcountaffected = -1;

		try {
			dbConnection = DBManager.getDBConnection();
			preparedStatement = dbConnection.prepareStatement(UPDATE_QUANTITY_BY_ISBN);
			preparedStatement.setInt(1, this.getQuantity());
			preparedStatement.setInt(2,this.isNew);
			preparedStatement.setString(3, this.getIsbn());
			
			// execute insert SQL stetement
			rowcountaffected = preparedStatement.executeUpdate();

			System.out.println("Record is updated in OC_PRODUCT_NEWSTOCK table!");

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
		return rowcountaffected;
	}

	public int updatePriceAndWeightByIsbn() throws SQLException {

		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		int rowcountaffected = -1;

		try {
			dbConnection = DBManager.getDBConnection();
			preparedStatement = dbConnection.prepareStatement(UPDATE_PRICE_WEIGHT_BY_ISBN);
			preparedStatement.setInt(1, this.getFk_mrp());
			preparedStatement.setInt(2, this.getFk_sp());
			preparedStatement.setInt(3, this.getBf_sp());
			preparedStatement.setInt(4, this.getWeight());
			preparedStatement.setString(5, this.getIsbn());
			// execute insert SQL stetement
			rowcountaffected = preparedStatement.executeUpdate();

			System.out.println("Record is updated in OC_PRODUCT_NEWSTOCK table!");

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
		return rowcountaffected;
	}


	private static java.sql.Timestamp getCurrentTimeStamp() {

		java.util.Date today = new java.util.Date();
		return new java.sql.Timestamp(today.getTime());

	}

}
