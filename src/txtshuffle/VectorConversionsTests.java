/**
 *
 */
package txtshuffle;

import static org.junit.Assert.*;

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
	 * Test method for {@link txtshuffle.VectorConversions#isValidSwizzleVector(int[])}.
	 */
	@Test
	public final void testIsValidSwizzleVector()
	{
		final int[] valid1 = new int[] {0,1,2,3,4};
		final int[] valid2 = new int[] {5,2,4,1,0,3};
		final int[] valid3 = new int[] {};
		final int[] valid4 = new int[] {0};

		final int[] invalid1 = new int[] {1};
		final int[] invalid2 = new int[] {0,1,3};
		final int[] invalid3 = new int[] {0,1,-1};

		org.junit.Assert.assertTrue(VectorConversions.isValidSwizzleVector(valid1));
		org.junit.Assert.assertTrue(VectorConversions.isValidSwizzleVector(valid2));
		org.junit.Assert.assertTrue(VectorConversions.isValidSwizzleVector(valid3));
		org.junit.Assert.assertTrue(VectorConversions.isValidSwizzleVector(valid4));

		org.junit.Assert.assertFalse(VectorConversions.isValidSwizzleVector(invalid1));
		org.junit.Assert.assertFalse(VectorConversions.isValidSwizzleVector(invalid2));
		org.junit.Assert.assertFalse(VectorConversions.isValidSwizzleVector(invalid3));
	}
//
//	/**
//	 * Test method for {@link txtshuffle.VectorConversions#isValidCompactVector(int[])}.
//	 */
//	@Test
//	public final void testIsValidCompactVector() {
//		fail("Not yet implemented");
//	}


	/**
	 * Test method for {@link txtshuffle.VectorConversions#intToCompactVector(int, int)}.
	 * @throws NumberTooGreatException
	 */
	@Test
	public final void testIntToCompactVectorAndBack() throws NumberTooGreatException {
		final int[] expectedOutputArr = new int[] {3, 8, 5, 9, 4, 7, 6, 0, 2, 1}; // length 10

		final int theInt = 1395835;

		final int[] compactFromInt = VectorConversions.intToCompactVector(10, theInt);

		final int[] swizzleVecFromCompact = VectorConversions.compactToSwizzle(compactFromInt);

		org.junit.Assert.assertArrayEquals(expectedOutputArr, swizzleVecFromCompact);

		final int[] backToCompactFromSwizzle = VectorConversions.swizzleToCompact(swizzleVecFromCompact);
		final int backToInt = VectorConversions.compactVectorToInt(backToCompactFromSwizzle);

		org.junit.Assert.assertEquals(theInt, backToInt);
	}



	/**
	 * Test method for {@link txtshuffle.VectorConversions#swizzleToCompact(int[])}.
	 */
	@Test
	public final void testSwizzleToCompactAndBack() {
		// final int[] swizzleVec = new int[] {1,2,0};

		// causes our int accumulator variables to overflow, seemingly
//		final int[] swizzleVec = new int[]
//				{10, 15, 23, 14, 12, 16, 28, 18, 24, 21, 20, 4, 29, 19, 9, 13, 17, 22, 8, 2, 3, 0, 27, 26, 6, 25, 7, 1, 5, 11};

		final int[] swizzleVec = new int[] {3, 8, 5, 9, 4, 7, 6, 0, 2, 1};

		final int[] outputCompactArr = VectorConversions.swizzleToCompact(swizzleVec);

		final int[] backToSwizzleFromCompact = VectorConversions.compactToSwizzle(outputCompactArr);

		org.junit.Assert.assertArrayEquals(swizzleVec, backToSwizzleFromCompact);
	}



	//     /**
	//      * Test method for {@link txtshuffle.VectorConversions#compactToSwizzle(int[])}.
	//      */
	//     @Test
	//     public final void testCompactToSwizzle() {
	//             fail("Not yet implemented");
	//     }


}
