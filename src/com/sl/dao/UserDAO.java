package com.sl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.sl.db.DBConn;
import com.sl.db.DBException;
import com.sl.model.UserPojo;
import com.sl.util.GlobalConstants;


public class UserDAO {

	private static final Logger LOGGER = Logger.getLogger(UserDAO.class.getName());
	
	public static UserPojo selectUSER(String userId) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet res=null;
		UserPojo pojo = null;
		try {
			conn = DBConn.getConnection();
			ps = conn.prepareStatement("select USER_ID, EMAIL, FIRST_NAME, LAST_NAME, EMAIL_VERIFICATION_HASH, EMAIL_VERIFICATION_ATTEMPTS, STATUS, CREATED_TIME from DEMO_USER where USER_ID = ?");
			ps.setString(1, userId);
			res = ps.executeQuery();
			if (res != null) {
				while (res.next()) {
					pojo = new UserPojo();
					pojo.setUSER_ID(res.getInt(1));
					pojo.setEMAIL(res.getString(2));
					pojo.setFIRST_NAME(res.getString(3));
					pojo.setLAST_NAME(res.getString(4));
					pojo.setEMAIL_VERIFICATION_HASH(res.getString(5));
					pojo.setEMAIL_VERIFICATION_ATTEMPTS(res.getInt(6));
					pojo.setSTATUS(res.getString(7));
					pojo.setCREATED_TIME(res.getString(8));
				}
			}
			DBConn.close(conn, ps, res);
		} catch (ClassNotFoundException | SQLException e) {
			DBConn.close(conn, ps, res);
			LOGGER.debug(e.getMessage());
			throw new DBException("Excepion while accessing database");
		}
		return pojo;
	}

	public static boolean verifyEmailHash(String user_id, String hash) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		boolean verified = false;
		ResultSet res=null;
		try {
			conn = DBConn.getConnection();
			ps = conn.prepareStatement("select 1 from DEMO_USER where USER_ID = ? and EMAIL_VERIFICATION_HASH = ?");
			ps.setString(1, user_id);
			ps.setString(2, hash);
			res = ps.executeQuery();
			if (res != null) {
				while (res.next()) {
					verified = true;
				}
			}
			DBConn.close(conn, ps, res);
		} catch (ClassNotFoundException | SQLException e) {
			DBConn.close(conn, ps, res);
			LOGGER.debug(e.getMessage());
			throw new DBException("Excepion while accessing database");
		}
		return verified;
	}

	public static boolean isEmailExists(String email) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		boolean verified = false;
		ResultSet res=null;
		try {
			conn = DBConn.getConnection();
			ps = conn.prepareStatement("select 1 from DEMO_USER where EMAIL = ?");
			ps.setString(1, email);
			res = ps.executeQuery();
			if (res != null) {
				while (res.next()) {
					verified = true;
				}
			}
			DBConn.close(conn, ps, res);
		} catch (ClassNotFoundException | SQLException e) {
			LOGGER.debug(e.getMessage());
			DBConn.close(conn, ps, res);
			throw new DBException("Excepion while accessing database");
		}
		return verified;
	}

	public static String insertRow(UserPojo pojo) throws DBException{
		Connection conn = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		String id = null;
		ResultSet res = null;
		try {
			conn = DBConn.getConnection();
			conn.setAutoCommit(false);
			ps1 = conn.prepareStatement("insert into DEMO_USER (EMAIL,FIRST_NAME,LAST_NAME,EMAIL_VERIFICATION_HASH,PASSWORD) values (?,?,?,?,?)");
			ps1.setString(1,pojo.getEMAIL());
			ps1.setString(2,pojo.getFIRST_NAME());
			ps1.setString(3,pojo.getLAST_NAME());
			ps1.setString(4,pojo.getEMAIL_VERIFICATION_HASH());
			ps1.setString(5,pojo.getPASSWORD());
			ps1.executeUpdate();
			
			ps2 = conn.prepareStatement("SELECT LAST_INSERT_ID()");
			res = ps2.executeQuery();
			
			if (res != null) {
				while (res.next()) {
					id = res.getString(1);
				}
			}
			
			conn.commit();
			DBConn.close(conn, ps1, ps2, res);
		} catch (ClassNotFoundException | SQLException e) {
			LOGGER.debug(e.getMessage());
			DBConn.close(conn, ps1, ps2, res);
			throw new DBException("Excepion while accessing database");
		}
		return id;
	}

	public static void deleteRow(UserPojo pojo){
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DBConn.getConnection();
			ps = conn.prepareStatement("delete from DEMO_USER where USER_ID = ?");
			ps.setInt(1,pojo.getUSER_ID());
			ps.executeUpdate();
			ps.close();
			DBConn.close(conn, ps);
		} catch (SQLException | ClassNotFoundException e) {
			LOGGER.debug(e.getMessage());
			DBConn.close(conn, ps);
			e.printStackTrace();
		}
	}

	public static void updateStaus(String user_id, String status) throws DBException{
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DBConn.getConnection();
			ps = conn.prepareStatement("update DEMO_USER set STATUS = ? where USER_ID = ?");
			ps.setString(1,status);
			ps.setString(2,user_id);
			ps.executeUpdate();
			DBConn.close(conn, ps);
		} catch (ClassNotFoundException | SQLException e) {
			LOGGER.debug(e.getMessage());
			DBConn.close(conn, ps);
			throw new DBException("Excepion while accessing database");
		}
	}

	public static void updateEmailVerificationHash(String user_id, String hash) throws DBException{
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DBConn.getConnection();
			ps = conn.prepareStatement("update DEMO_USER set EMAIL_VERIFICATION_HASH = ?, EMAIL_VERIFICATION_ATTEMPTS = ? where USER_ID = ?");
			ps.setString(1,hash);
			ps.setInt(2,0);
			ps.setString(3,user_id);
			ps.executeUpdate();
			DBConn.close(conn, ps);
		} catch (ClassNotFoundException | SQLException e) {
			LOGGER.debug(e.getMessage());
			DBConn.close(conn, ps);
			throw new DBException("Excepion while accessing database");
		}
	}

	public static int incrementVerificationAttempts(String user_id) throws DBException{
		Connection conn = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		ResultSet res = null;
		int attempts = 0;
		try {
			conn = DBConn.getConnection();
			ps = conn.prepareStatement("update DEMO_USER set EMAIL_VERIFICATION_ATTEMPTS = EMAIL_VERIFICATION_ATTEMPTS + 1 where USER_ID = ?");
			ps.setString(1,user_id);
			ps.executeUpdate();
			
			ps2 = conn.prepareStatement("SELECT EMAIL_VERIFICATION_ATTEMPTS from DEMO_USER");
			res = ps2.executeQuery();
			
			if (res != null) {
				while (res.next()) {
					attempts = res.getInt(1);
				}
			}
			DBConn.close(conn, ps, ps2, res);
		} catch (ClassNotFoundException | SQLException e) {
			LOGGER.debug(e.getMessage());
			DBConn.close(conn, ps, ps2, res);
			throw new DBException("Excepion while accessing database");
		}
		return attempts;
	}

	public static UserPojo verifyLogin(String inputEmail, String inputPassword) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		UserPojo pojo = null;
		ResultSet res=null;
		try {
			conn = DBConn.getConnection();
			ps = conn.prepareStatement("select USER_ID, EMAIL, FIRST_NAME, LAST_NAME, STATUS, CREATED_TIME from DEMO_USER where EMAIL = ? and PASSWORD = ?");
			ps.setString(1, inputEmail);
			ps.setString(2, inputPassword);
			res = ps.executeQuery();
			if (res != null) {
				while (res.next()) {
					pojo = new UserPojo();
					pojo.setUSER_ID(res.getInt(1));
					pojo.setEMAIL(res.getString(2));
					pojo.setFIRST_NAME(res.getString(3));
					pojo.setLAST_NAME(res.getString(4));
					pojo.setSTATUS(res.getString(5));
					pojo.setCREATED_TIME(res.getString(6));
				}
			}
			DBConn.close(conn, ps, res);
		} catch (ClassNotFoundException | SQLException e) {
			LOGGER.debug(e.getMessage());
			DBConn.close(conn, ps, res);
			throw new DBException("Excepion while accessing database");
		}
		return pojo;
	}

	public static boolean verifyUserIdAndPassword(String userId,
			String inputCurrentPassword) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		boolean verified = false;
		ResultSet res=null;
		try {
			conn = DBConn.getConnection();
			ps = conn.prepareStatement("select 1 from DEMO_USER where USER_ID = ? and PASSWORD = ?");
			ps.setString(1, userId);
			ps.setString(2, inputCurrentPassword);
			res = ps.executeQuery();
			if (res != null) {
				while (res.next()) {
					verified = true;
				}
			}
			DBConn.close(conn, ps, res);
		} catch (ClassNotFoundException | SQLException e) {
			LOGGER.debug(e.getMessage());
			DBConn.close(conn, ps, res);
			throw new DBException("Excepion while accessing database");
		}
		return verified;
	}

	public static void updatePassword(String userId, String inputPassword) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DBConn.getConnection();
			ps = conn.prepareStatement("update DEMO_USER set PASSWORD = ? where USER_ID = ?");
			ps.setString(1,inputPassword);
			ps.setString(2,userId);
			ps.executeUpdate();
			DBConn.close(conn, ps);
		} catch (ClassNotFoundException | SQLException e) {
			LOGGER.debug(e.getMessage());
			DBConn.close(conn, ps);
			throw new DBException("Excepion while accessing database");
		}
	}

	public static void updateEmailVerificationHashForResetPassword(String inputEmail,
			String hash) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DBConn.getConnection();
			ps = conn.prepareStatement("update DEMO_USER set EMAIL_VERIFICATION_HASH = ?, EMAIL_VERIFICATION_ATTEMPTS = ?, STATUS = ? where EMAIL = ?");
			ps.setString(1,hash);
			ps.setInt(2,0);
			ps.setString(3,GlobalConstants.IN_RESET_PASSWORD);
			ps.setString(4,inputEmail);
			ps.executeUpdate();
			DBConn.close(conn, ps);
		} catch (ClassNotFoundException | SQLException e) {
			LOGGER.debug(e.getMessage());
			DBConn.close(conn, ps);
			throw new DBException("Excepion while accessing database");
		}
	}

	public static UserPojo selectUSERbyEmail(String inputEmail) throws DBException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet res=null;
		UserPojo pojo = null;
		try {
			conn = DBConn.getConnection();
			ps = conn.prepareStatement("select USER_ID, EMAIL, FIRST_NAME, LAST_NAME, EMAIL_VERIFICATION_HASH, EMAIL_VERIFICATION_ATTEMPTS, STATUS, CREATED_TIME from DEMO_USER where EMAIL = ?");
			ps.setString(1, inputEmail);
			res = ps.executeQuery();
			if (res != null) {
				while (res.next()) {
					pojo = new UserPojo();
					pojo.setUSER_ID(res.getInt(1));
					pojo.setEMAIL(res.getString(2));
					pojo.setFIRST_NAME(res.getString(3));
					pojo.setLAST_NAME(res.getString(4));
					pojo.setEMAIL_VERIFICATION_HASH(res.getString(5));
					pojo.setEMAIL_VERIFICATION_ATTEMPTS(res.getInt(6));
					pojo.setSTATUS(res.getString(7));
					pojo.setCREATED_TIME(res.getString(8));
				}
			}
			DBConn.close(conn, ps, res);
		} catch (ClassNotFoundException | SQLException e) {
			LOGGER.debug(e.getMessage());
			DBConn.close(conn, ps, res);
			throw new DBException("Excepion while accessing database");
		}
		return pojo;
	}

	

}			
		