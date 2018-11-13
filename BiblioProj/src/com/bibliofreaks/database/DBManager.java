package com.bibliofreaks.database;


import org.apache.log4j.Logger;

import com.bibliofreaks.action.BiblioFeed;
import com.bibliofreaks.utils.LoadProperties;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

import com.bibliofreaks.utils.LoadProperties;

public class DBManager {
	public static Logger basicLogger = Logger.getLogger(DBManager.class.getName());

	public static void main(String[] args) {

		LoadProperties.load();
		DBManager.getDBConnection();

	}

	public static Connection getDBConnection() {

		Connection dbConnection = null;

		try {

			Class.forName(LoadProperties.Prop("DB_DRIVER"));

		} catch (ClassNotFoundException e) {

			System.out.println(e.getMessage());

		}

		try {

			dbConnection = DriverManager.getConnection(LoadProperties.Prop("DB_CONNECTION"),
					LoadProperties.Prop("TEST_DB_USER"), LoadProperties.Prop("TEST_DB_PASSWORD"));
			return dbConnection;

		} catch (SQLException e) {

			System.out.println(e.getMessage());

		}

		return dbConnection;
	}
	
	
	
	// catch (SQLException ex){
	// // handle any errors
	// System.out.println("SQLException: " + ex.getMessage());
	// System.out.println("SQLState: " + ex.getSQLState());
	// System.out.println("VendorError: " + ex.getErrorCode());
	// }
	// finally {
	// // it is a good idea to release
	// // resources in a finally{} block
	// // in reverse-order of their creation
	// // if they are no-longer needed
	//
	// if (rs != null) {
	// try {
	// rs.close();
	// } catch (SQLException sqlEx) { } // ignore
	//
	// rs = null;
	// }
	//
	// if (stmt != null) {
	// try {
	// stmt.close();
	// } catch (SQLException sqlEx) { } // ignore
	//
	// stmt = null;
	// }
	//
	// }
	// } catch (SQLException ex) {
	// // handle any errors
	// System.out.println("SQLException: " + ex.getMessage());
	// System.out.println("SQLState: " + ex.getSQLState());
	// System.out.println("VendorError: " + ex.getErrorCode());
	// }

	// }
}