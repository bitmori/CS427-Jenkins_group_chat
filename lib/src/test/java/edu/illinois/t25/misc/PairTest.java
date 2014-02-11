/**
 * @file PairTest.java
 *
 * @author Piyush Bathwal, Joseph Ciurej
 * @date Fall 2013
 *
 * @TODO
 */
package edu.illinois.t25;

import edu.illinois.t25.misc.Pair;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 * Container class for the test suite that tests the functionality of the 
 * "Pair" type.
 */
public class PairTest
{
	/// Testing Variables ///
	
	/**
	 * A static integer object instance with a value of one.
	 */
	private final static Integer csOne = new Integer( 1 );

	/**
	 * A static integer object instance with a value of two.
	 */
	private final static Integer csTwo = new Integer( 2 );

	/** 
	 * A test pair object instance on which all the tests are performed.
	 */
	private Pair<Integer, Integer> mTestPair;

	/// Testing Set Up/Tear Down ///
	
	/**
	 * Sets up all the tests by creating a test pair.
	 */
	@Before
	public void TestSetup()
	{
		mTestPair = new Pair<Integer, Integer>( csOne, csTwo );
	}

	/// Testing Functions ///
	
	@Test
	public void TestConstructor()
	{
		assertEquals( "First item should be first value passed to the constructor.",
			csOne, mTestPair.getFirst() );
		assertEquals( "Second item should be second value passed to the constructor.",
			csTwo, mTestPair.getSecond() );
	}

	@Test
	public void TestEquals()
	{
		Pair<Integer, Integer> samePair = new Pair<Integer, Integer>( csOne, csTwo );
		Pair<Integer, Integer> swappedPair = new Pair<Integer, Integer>( csTwo, csOne );
		Pair<String, String> stringPair = new Pair<String, String>( "1", "2" );

		assertTrue( "Pairs with same first and second items should be equal.",
			mTestPair.equals( samePair ) );
		assertFalse( "Pairs with swapped first and second items shouldn't be equal.",
			mTestPair.equals( swappedPair ) );
		assertFalse( "Pairs with different type items shouldn't be equal.",
			mTestPair.equals( stringPair ) );
	}

	@Test
	public void TestToString()
	{
		assertEquals( "Pair should be a string of the form '(first, second)'.",
			"(1, 2)", mTestPair.toString() );
	}

}
