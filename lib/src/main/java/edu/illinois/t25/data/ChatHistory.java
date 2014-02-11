package edu.illinois.t25.data;

import java.io.File;
import java.sql.*;

public class ChatHistory {
	static final int ALL = 0;
	static final int AUTHOR = 1;
	static final int GROUPNAME = 2;
	static final int RECIPIENT = 3;
	static final int MESSAGE = 4;

	// / Initialize ///

	/**
	 * Initialize the database
	 */
	public static void dbStartup() {
		File f = new File("./test.db");
		if (f.exists()) {
			System.out.println("Database already exists");
			return;
		}

		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			System.out.println("Opened database successfully");
		} catch (Exception e) {
			System.err.println("Opening Database: " + e.getClass().getName()
					+ ": " + e.getMessage());
			System.exit(0);
		}
		try {
			stmt = c.createStatement();
			String sql = "CREATE TABLE CHAT "
					+ "(ID INTEGER PRIMARY KEY     AUTOINCREMENT,"
					+ " AUTHOR    TEXT    NOT NULL, "
					+ " GROUP_NAME          TEXT    NOT NULL, "
					+ " RECIPIENT      TEXT," + " TIMESTAMP      TEXT, "
					+ " MESSAGE        TEXT    NOT NULL)";
			stmt.executeUpdate(sql);
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println("Writing to db: " + e.getClass().getName()
					+ ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Table created successfully");

	}

	// / Methods ///

	/**
	 * Function to insert a new message into the database
	 * 
	 * @param author
	 *            name of person who posted
	 * @param group
	 *            name of group posted to
	 * @param recip
	 *            name of individual recipient
	 * @param msg
	 *            message that was posted
	 * 
	 * @return true if insert worked properly else false
	 */
	public static boolean insert(String author, String group, String recip,
			String msg) {
		if (group.equals("") && recip.equals("")) {
			System.out.println("ERROR : cannot have both group and recipient.");
			return false;
		}

		Connection c = null;
		Statement stmt = null;
		Timestamp ts = null;

		try {
			c = connect();
			stmt = c.createStatement();

			java.util.Date date = new java.util.Date();
			ts = new Timestamp(date.getTime());
			System.out.println("Messaged saved at " + ts);

			String sql = "INSERT INTO CHAT (AUTHOR,GROUP_NAME,RECIPIENT,TIMESTAMP,MESSAGE) "
					+ "VALUES ('"
					+ author
					+ "', '"
					+ group
					+ "', '"
					+ recip
					+ "', '" + ts + "', '" + msg + "');";
			stmt.executeUpdate(sql);

			stmt.close();
			c.commit();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * Function to get all messages posted in a group by a certain group
	 * 
	 * @param gname
	 *            name of group to search for
	 * @return String of all messages and their associated authors
	 */
	public static String selectGroup(String gname) {
		Connection c = null;
		Statement stmt = null;
		String ret = "";

		try {
			c = connect();
			stmt = c.createStatement();

			ResultSet rs = stmt
					.executeQuery("SELECT TIMESTAMP,MESSAGE,AUTHOR FROM CHAT WHERE GROUP_NAME='"
							+ gname + "';");
			while (rs.next()) {
				ret += "Time: " + rs.getString("timestamp");
				ret += ", Author: " + rs.getString("author");
				ret += ", Message: " + rs.getString("message") + "\n";
			}

			System.out.println(ret);

			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ret;
	}

	/**
	 * Function to select all messages to and from an individual
	 * 
	 * @param person
	 *            name of individual
	 * @return String all messages to and from person
	 */
	public static String selectPerson(String person) {
		Connection c = null;
		Statement stmt = null;
		String ret = "";

		try {
			c = connect();
			stmt = c.createStatement();

			ResultSet rs = stmt
					.executeQuery("SELECT TIMESTAMP,MESSAGE,AUTHOR FROM CHAT WHERE RECIPIENT='"
							+ person + "';");
			while (rs.next()) {
				ret += "Time: " + rs.getString("timestamp");
				ret += ", Author: " + rs.getString("author");
				ret += ", Message: " + rs.getString("message") + "\n";
			}

			ResultSet rs2 = stmt
					.executeQuery("SELECT TIMESTAMP,MESSAGE,RECIPIENT FROM CHAT WHERE AUTHOR='"
							+ person + "';");
			while (rs2.next()) {
				ret += "TIME: " + rs.getString("timestamp");
				ret += ", RECIPEINT: " + rs.getString("recipient");
				ret += ", MESSAGE: " + rs.getString("message") + "\n";
			}

			System.out.println(ret);

			rs.close();
			rs2.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ret;
	}

	

	// / helper method ///
	/**
	 * Connects to the database
	 * 
	 * @return returns the database connection
	 */
	private static Connection connect() throws ClassNotFoundException,
			SQLException {
		Connection c;
		Class.forName("org.sqlite.JDBC");
		c = DriverManager.getConnection("jdbc:sqlite:test.db");
		c.setAutoCommit(false);
		return c;
	}
}
