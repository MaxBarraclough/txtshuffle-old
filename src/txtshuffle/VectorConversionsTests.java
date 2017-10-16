/**
 *
 */
package txtshuffle;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.Arrays;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import txtshuffle.TxtShuffle.NumberTooGreatException;

/**
 * @author mb
 *
 */
public final class VectorConversionsTests {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	// TODO IMPLEMENT

	/**
	 * Test method for {@link txtshuffle.VectorConversions#isValidACTUALIsv(int[])}.
	 */
	@Test
	public final void testIsValidACTUALIsv()
	{
		final int[] valid1 = new int[] {0,1,2,3,4};
		final int[] valid2 = new int[] {5,2,4,1,0,3};
		final int[] valid3 = new int[] {};
		final int[] valid4 = new int[] {0};

		final int[] invalid1 = new int[] {1};
		final int[] invalid2 = new int[] {0,1,3};
		final int[] invalid3 = new int[] {0,1,-1};

		org.junit.Assert.assertTrue(VectorConversions.isValidACTUALIsv(valid1));
		org.junit.Assert.assertTrue(VectorConversions.isValidACTUALIsv(valid2));
		org.junit.Assert.assertTrue(VectorConversions.isValidACTUALIsv(valid3));
		org.junit.Assert.assertTrue(VectorConversions.isValidACTUALIsv(valid4));

		org.junit.Assert.assertFalse(VectorConversions.isValidACTUALIsv(invalid1));
		org.junit.Assert.assertFalse(VectorConversions.isValidACTUALIsv(invalid2));
		org.junit.Assert.assertFalse(VectorConversions.isValidACTUALIsv(invalid3));
	}


	/**
	 * Test method for {@link txtshuffle.VectorConversions#isValidCompactVector(int[])}.
	 */
	@Test
	public final void testIsValidCompactVector() {

		final int[] valid1 = new int[] {0,0,0,0};
		final int[] valid2 = new int[] {};
		final int[] valid3 = new int[] {0};
		final int[] valid4 = new int[] {0,1,0};
		final int[] valid5 = new int[] {2,1,0};

		final int[] invalid1 = new int[] {1};
		final int[] invalid2 = new int[] {0,1,1};
		final int[] invalid3 = new int[] {0,1,-1};


		org.junit.Assert.assertTrue(VectorConversions.isValidCompactVector(valid1));
		org.junit.Assert.assertTrue(VectorConversions.isValidCompactVector(valid2));
		org.junit.Assert.assertTrue(VectorConversions.isValidCompactVector(valid3));
		org.junit.Assert.assertTrue(VectorConversions.isValidCompactVector(valid4));
		org.junit.Assert.assertTrue(VectorConversions.isValidCompactVector(valid5));

		org.junit.Assert.assertFalse(VectorConversions.isValidCompactVector(invalid1));
		org.junit.Assert.assertFalse(VectorConversions.isValidCompactVector(invalid2));
		org.junit.Assert.assertFalse(VectorConversions.isValidCompactVector(invalid3));
	}


	/**
	 * Test method for {@link txtshuffle.VectorConversions#intToCompactVector(int, int)}.
	 * @throws NumberTooGreatException
	 */
	@Test
	public final void testIntToCompactVectorAndBack_KnownVec() throws NumberTooGreatException {

		// hard-coded value, vector part
		final int[] expectedOutputArr = new int[] {3, 8, 5, 9, 4, 7, 6, 0, 2, 1}; // length 10

		// hard-coded value, int part
		final BigInteger secretNum = BigInteger.valueOf(1395835);



		// TODO nasty conversion business /////////////////////////

		final int[] compactFromInt
		  = VectorConversions.intToCompactVector(
				  10,
				  secretNum
		    );


//		final int[] compactFromInt = new int[compactFromInt_BIs.length];
//		for(int i = 0; i != compactFromInt.length; ++i)
//		{
//			compactFromInt[i] = compactFromInt_BIs[i].intValue();
//		}

		///////////////////////////////////////////////////////////


		final int[] ACTUALisvFromCompact = VectorConversions.compactToACTUALIsv(compactFromInt);

		org.junit.Assert.assertArrayEquals(expectedOutputArr, ACTUALisvFromCompact);

		final int[] backToCompactFromACTUALIsv = VectorConversions.ACTUALisvToCompact(ACTUALisvFromCompact);

		final BigInteger backToInt = VectorConversions.compactVectorToInt(backToCompactFromACTUALIsv);

		org.junit.Assert.assertEquals(secretNum, backToInt);
	}


	// TODO rename from 'int'

	@Test
	public final void testIntToCompactVectorAndBack() throws NumberTooGreatException {

		// hard-coded value, int part
		final BigInteger theBigInteger = BigInteger.valueOf(1395835);


		// TODO nasty conversion business /////////////////////////

		final int[] compactFromInt
		  = VectorConversions.intToCompactVector(
				  10,
				  theBigInteger
		    );


//		final int[] compactFromInt = new int[compactFromInt_BIs.length];
//		for(int i = 0; i != compactFromInt.length; ++i)
//		{
//			compactFromInt[i] = compactFromInt_BIs[i].intValue();
//		}

		///////////////////////////////////////////////////////////


		final int[] ACTUALisvFromCompact = VectorConversions.compactToACTUALIsv(compactFromInt);

		final int[] backToCompactFromACTUALIsv
		  = VectorConversions.ACTUALisvToCompact(ACTUALisvFromCompact);

		final BigInteger backToInt
		  = VectorConversions.compactVectorToInt(backToCompactFromACTUALIsv);

		org.junit.Assert.assertEquals(theBigInteger, backToInt);
	}





	/**
	 * Test method for {@link txtshuffle.VectorConversions#ACTUALisvToCompact(int[])}.
	 */
	@Test
	public final void testACTUALIsvToCompactAndBack() {
		// final int[] ACTUALisv = new int[] {1,2,0};

		// causes our int accumulator variables to overflow, seemingly
//		final int[] ACTUALisv = new int[]
//				{10, 15, 23, 14, 12, 16, 28, 18, 24, 21, 20, 4, 29, 19, 9, 13, 17, 22, 8, 2, 3, 0, 27, 26, 6, 25, 7, 1, 5, 11};

		final int[] ACTUALisv = new int[] {3, 8, 5, 9, 4, 7, 6, 0, 2, 1};

		final int[] outputCompactArr = VectorConversions.ACTUALisvToCompact(ACTUALisv);

		final int[] backToACTUALIsvFromCompact = VectorConversions.compactToACTUALIsv(outputCompactArr);

		org.junit.Assert.assertArrayEquals(ACTUALisv, backToACTUALIsvFromCompact);
	}



	//     /**
	//      * Test method for {@link txtshuffle.VectorConversions#compactToACTUALIsv(int[])}.
	//      */
	//     @Test
	//     public final void testCompactToACTUALIsv() {
	//             fail("Not yet implemented");
	//     }


}
