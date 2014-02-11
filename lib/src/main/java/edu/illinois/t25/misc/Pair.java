/**
 * @file Pair.java
 *
 * @author arturh, Piyush Bathwal, Joseph Ciurej
 * @date Fall 2013
 *
 * @TODO
 * - Finalize the name of the global group and update it in the default
 *   constructor for the chat message.
 */
package edu.illinois.t25.misc;

/**
 * An abstract representation of an immutable pair of related (though 
 * not aggregated) objects.
 *
 * @see "http://stackoverflow.com/questions/156275/what-is-the-equivalent-of-the-c-pairl-r-in-java"
 */
public class Pair<T1, T2> 
{
	/// Constructors ///
	
	/**
	 * Constructs a pair with the two given objects as entries.
	 *
	 * @param pFirst The first item in the pair being constructed.
	 * @param pSecond The second item in the pair being constructed.
	 */
    public Pair( T1 pFirst, T2 pSecond ) 
	{
		mFirst = pFirst;
		mSecond = pSecond;
    }

	/// Methods ///
	
	/**
	 * Returns the hash code of the pair, which is a combination
	 * code created by combining the two pair elements.
	 *
	 * @return An integer hash code that encodes the pair instance.
	 */
    public int hashCode() 
	{
    	int hashFirst = mFirst != null ? mFirst.hashCode() : 0;
    	int hashSecond = mSecond != null ? mSecond.hashCode() : 0;

    	return (hashFirst + hashSecond) * hashSecond + hashFirst;
    }

	/**
	 * Returns true if the given object is equivalent to the instance pair
	 * and false otherwise.
	 *
	 * @param pOther The object that will be compared for equality.
	 * @return True if the instance and the parameter object are equivalent,
	 * 	false otherwise.
	 */
    public boolean equals( Object pOther ) 
	{
    	if( pOther instanceof Pair) 
		{
    		Pair otherPair = (Pair)pOther;

    		return 
    		((  this.mFirst == otherPair.mFirst ||
    			( this.mFirst != null && otherPair.mFirst != null &&
    			  this.mFirst.equals(otherPair.mFirst))) &&
    		 (	this.mSecond == otherPair.mSecond ||
    			( this.mSecond != null && otherPair.mSecond != null &&
    			  this.mSecond.equals(otherPair.mSecond))) );
    	}

    	return false;
    }

	/**
	 * Returns a string representation of the pair object of the form
	 * '(first, second)'.
	 *
	 * @return A string that represents the pair object.
	 */
    public String toString()
    { 
           return "(" + mFirst + ", " + mSecond + ")"; 
    }
	

	// Getter/Setter Methods //
	
	/**
	 * @return A reference to the first element in the pair.
	 */
    public T1 getFirst() 
	{
    	return mFirst;
    }

	/**
	 * @return A reference to the second element in the pair.
	 */
    public T2 getSecond() 
	{
    	return mSecond;
    }

	/// Fields ///
	
	/**
	 * The first item in the pair.
	 */
    private final T1 mFirst;

	/**
	 * The second item in the pair.
	 */
    private final T2 mSecond;

}
