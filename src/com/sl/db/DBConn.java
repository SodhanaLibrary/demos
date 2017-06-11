package com.sl.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.sl.util.Setup;

/**
 * @author srinivas
 * This file is to create Connections and Closing connections and statements.
 */
public class DBConn {
	
	private static final Logger LOGGER = Logger.getLogger(DBConn.class.getName());
	Connection con = null;
	static String className = "class sodhana.sdb.connection.DBConnection";

	/**
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * To Create Connections
	 */
	public static Connection getConnection() throws ClassNotFoundException,	SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection connection = null;
		connection = DriverManager.getConnection(
				Setup.DB_URL, Setup.DB_USERNAME, Setup.DB_PASSWORD);
		return connection;
	}

	/**
	 * @param con
	 * @param stmt
	 * @param rs
	 * To close statements, result sets and Connection 
	 */
	public static void close(Connection con, Statement stmt, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (SQLException e) {
			LOGGER.debug(e.getMessage());
		} catch (Exception e) {
			LOGGER.debug(e.getMessage());
		}
	}

	/**
	 * @param con
	 * @param stmt
	 * To close statements and Connection
	 */
	public static void close(Connection con, Statement stmt) {
		try {
			if (stmt != null) {
				stmt.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (SQLException e) {
			LOGGER.debug(e.getMessage());
		} catch (Exception e) {
			LOGGER.debug(e.getMessage());
		}
	}

	/**
	 * @param con
	 * @param pstmt
	 * @param rs
	 * To Close PreparedStatement and Connection
	 */
	public static void close(Connection con, PreparedStatement pstmt,
			ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (SQLException e) {
			LOGGER.debug(e.getMessage());
		} catch (Exception e) {
			LOGGER.debug(e.getMessage());
		}
	}

	/**
	 * @param con
	 * @param pstmt
	 * @param rs
	 * To Close PreparedStatement and Connection
	 */
	public static void close(Connection con, PreparedStatement pstmt, PreparedStatement pstmt2,
			ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (pstmt2 != null) {
				pstmt2.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (SQLException e) {
			LOGGER.debug(e.getMessage());
		} catch (Exception e) {
			LOGGER.debug(e.getMessage());
		}
	}

	/**
	 * @param con
	 * To close connection
	 */
	public static void closeConnection(Connection con) {
		try {
			if (con != null)
				con.close();
		} catch (SQLException e) {
			LOGGER.debug(e.getMessage());
		} catch (Exception e) {
			LOGGER.debug(e.getMessage());
		}
	}

	/**
	 * @param conn
	 * @throws SQLException
	 * To Commit and Close the connection
	 */
	public static void commitAndClose(Connection conn) throws SQLException {
		conn.commit();
		conn.close();
	}

}			
			