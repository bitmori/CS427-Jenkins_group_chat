package edu.illinois.t25;

import edu.illinois.t25.data.ChatHistory;
import org.junit.Test;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.*;

import java.io.File;

public class ChatHistoryTest {

	private static boolean insertSuccessful = false;
	private static boolean autoIncrementSuccessful = false;
	private static boolean doubleInsertFailure = false;

	@BeforeClass
	public static void init()
	{
		File f = new File("./chatlog.db");
		if(f.exists()) 
		{
			if(f.delete())
			{
				System.out.println("DB deleted");
			}
		}
		
		ChatHistory.dbStartup();
		
		insertSuccessful = ChatHistory.insert("tim", "t25", "qiyue", "testing...");
		autoIncrementSuccessful = ChatHistory.insert("qiyue", "t25", "tim", "testing auto increment...");
		doubleInsertFailure = !ChatHistory.insert("qiyue", "", "", "testing failed insert...");
	}
	
	@Test
	public void testInsert() {
		assertEquals( insertSuccessful, true );
		assertEquals( autoIncrementSuccessful, true );
		assertEquals( doubleInsertFailure, true );
	}
	
	@Test
	public void testSelectGroup()
	{
		assertTrue(ChatHistory.selectGroup("t25") != null);
	}
	
	@Test
	public void testSelectPerson()
	{
		assertTrue(ChatHistory.selectPerson("qiyue") != null);		
	}

}
