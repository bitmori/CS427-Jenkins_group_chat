/**
 * @TODO
 * - The tests within this class should be changed in this class so that they
 *   don't rely on the order of execution.
 */
package edu.illinois.t25;

import edu.illinois.t25.data.*;
import org.junit.Test;
import org.junit.BeforeClass;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class GroupDatabaseTest {

	@BeforeClass
	public static void TestSetup() {
		GroupDatabase.CreateGroupTable();
	}

	@Test
	public void ConnectionTest(){
		GroupDatabase.CreateGroupTable();
		Connection c = null;
	    Statement stmt = null;
	    ResultSet set = null;
	    try {
	      c = GroupDatabase.Connect();
	      System.out.println("Opened database successfully");
	      
	      stmt = c.createStatement();
	      String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='GroupHierarchy'";
	      set = stmt.executeQuery(sql);

	      assertTrue(set.getString(1).equals("GroupHierarchy"));
	      stmt.close();
	      c.close();
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    
	}
	
	@Test
	public void InsertTest(){
		UserGroup group1 = new UserGroup("CS427",null);
		UserGroup child = new UserGroup("T25",group1);
		
		GroupDatabase.InsertGroup(group1.getName(), null);
		GroupDatabase.InsertGroup(child.getName(), group1);
		
		Connection c = null;
	    Statement stmt = null;
	    ResultSet set = null;
	    try {
	      c = GroupDatabase.Connect();
	      System.out.println("Opened database successfully");
	      
	      stmt = c.createStatement();
	      String sql = "SELECT PARENT from GroupHierarchy where GROUPNAME = 'CS427'";
	      set = stmt.executeQuery(sql);

	      assertTrue(set.getInt(1)==0);
	      
	      String sql2 = "SELECT PARENT from GroupHierarchy where GROUPNAME = 'T25'";
	      set = stmt.executeQuery(sql2);

	      assertTrue(set.getInt(1)==GroupDatabase.CheckParent("CS427"));
	      stmt.close();
	      c.close();
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    
	}

	@Test
	public void ChangeTest(){
		UserGroup group2 = new UserGroup("CS418",null);
		UserGroup child = new UserGroup("T25",null);
		
		GroupDatabase.InsertGroup(child.getName(), null);
		GroupDatabase.InsertGroup("CS418", null);
		GroupDatabase.ChangeParent("T25", group2);
		
		Connection c = null;
	    Statement stmt = null;
	    ResultSet set = null;
	    try {
	      c = GroupDatabase.Connect();
	      System.out.println("Opened database successfully");
	      
	      stmt = c.createStatement();
	      String sql = "SELECT PARENT from GroupHierarchy where GROUPNAME = 'CS418'";
	      set = stmt.executeQuery(sql);

	      assertTrue(set.getInt(1)==0);
	      
	      String sql2 = "SELECT PARENT from GroupHierarchy where GROUPNAME = 'T25'";
	      set = stmt.executeQuery(sql2);

	      assertTrue(set.getInt(1)==GroupDatabase.CheckParent("CS418"));
	      stmt.close();
	      c.close();
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	}

}
