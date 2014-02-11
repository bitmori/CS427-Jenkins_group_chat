package edu.illinois.t25.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class that stores and manages all group information in a database
 * 
 * @depend - - - UserGroup
 */
public class GroupDatabase {

	// / Initialize ///
	/**
	 * Creates and initializes database
	 */
	public static void CreateGroupTable() {
		Connection c = null;
		Statement stmt = null;
		try {
			c = Connect();
			stmt = c.createStatement();
			String sql = "CREATE TABLE if not exists GroupHierarchy "
					+ "(GROUPID		INTEGER PRIMARY KEY AUTOINCREMENT    NOT NULL,"
					+ " GROUPNAME   TEXT    NOT NULL, "
					+ " PARENT      INT     NOT NULL)";
			stmt.executeUpdate(sql);
			stmt.close();
			c.close();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}

	// / Methods ///

	/**
	 * Insert new group into the database
	 * 
	 * @param name
	 *            Name of the group
	 * @param parent
	 *            Parent of the group if exist else null
	 */
	public static void InsertGroup(String name, UserGroup parent) {
		Connection c = null;
		Statement stmt = null;
		int parents = 0;

		try {
			c = Connect();
			stmt = c.createStatement();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		if (parent == null) {
			parents = 0;
		} else {
			parents = CheckParent(parent.getName());
		}

		String sql = "INSERT INTO GroupHierarchy (GROUPNAME,PARENT)"
				+ "VALUES ('" + name + "'," + parents + ");";
		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}

	/**
	 * Changes parents for a certain group with a new parent
	 * 
	 * @param name
	 *            Name of the group that will need a new parent
	 * @param newparent
	 *            new parent of the group if exist else null
	 */
	public static void ChangeParent(String name, UserGroup newparent) {
		Connection c = null;
		Statement stmt = null;
		int parents = 0;

		try {
			c = Connect();
			stmt = c.createStatement();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		if (newparent == null) {
			parents = 0;
		} else {
			parents = CheckParent(newparent.getName());
		}

		String sql = "UPDATE GroupHierarchy SET PARENT = " + parents
				+ " WHERE GROUPNAME = '" + name + "';";
		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}

	// / Helper Methods ///
	/**
	 * Checks if group exists in the database
	 * 
	 * @param parent
	 *            groupname of the parent that needs to be checked
	 * @return the groupID of the parent
	 */
	public static int CheckParent(String parent) {
		Connection c = null;
		Statement stmt = null;
		ResultSet set = null;

		try {
			c = Connect();
			stmt = c.createStatement();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		if (parent == null) {
			String sql = "SELECT GROUPID from GroupHierarchy where GROUPNAME = '"
					+ parent + "'";
			try {
				set = stmt.executeQuery(sql);
				if (set.first())
					return set.getInt(1);
				else
					return 0;
			} catch (SQLException e) {
				System.err.println(e.getClass().getName() + ": "
						+ e.getMessage());
				System.exit(0);
			}
		}
		return 0;
	}

	/**
	 * Connects to the database
	 * 
	 * @return returns the database connection
	 */
	public static Connection Connect() {
		Connection c = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		return c;
	}
}
