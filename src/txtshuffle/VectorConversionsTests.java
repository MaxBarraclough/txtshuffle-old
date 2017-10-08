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

/**
 * @author mb
 *
 */
public class VectorConversionsTests {

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

//	/**
//	 * Test method for {@link txtshuffle.VectorConversions#isValidUsefulVector(int[])}.
//	 */
//	@Test
//	public final void testIsValidUsefulVector() {
//		fail("Not yet implemented");
//	}
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
	 */
	@Test
	public final void testIntToCompactVectorAndBack() {
		final int[] expectedOutputArr = new int[] {3, 8, 5, 9, 4, 7, 6, 0, 2, 1}; // length 10
		// final int[] outputCompactArr = VectorConversions.usefulToCompact(usefulArr);
		// final int outputInt = VectorConversions.compactVectorToInt(outputCompactArr);

		final int theInt = 1395835;

		final int[] compactFromInt = VectorConversions.intToCompactVector(10, theInt);

		final int[] usefulFromCompact = VectorConversions.compactToUseful(compactFromInt);

		org.junit.Assert.assertArrayEquals(expectedOutputArr, usefulFromCompact);

		final int[] backToCompactFromUseful = VectorConversions.usefulToCompact(usefulFromCompact);
		final int backToInt = VectorConversions.compactVectorToInt(backToCompactFromUseful);

		org.junit.Assert.assertEquals(theInt, backToInt);
	}


//	/**
//	 * Test method for {@link txtshuffle.VectorConversions#compactVectorToInt(int[])}.
//	 */
//	@Test
//	public final void testCompactVectorToInt() {
//		fail("Not yet implemented");
//	}


	/**
	 * Test method for {@link txtshuffle.VectorConversions#usefulToCompact(int[])}.
	 */
	@Test
	public final void testUsefulToCompactAndBack() {
		// final int[] usefulArr = new int[] {1,2,0};

		// causes our int accumulator variables to overflow, seemingly
//		final int[] usefulArr = new int[]
//				{10, 15, 23, 14, 12, 16, 28, 18, 24, 21, 20, 4, 29, 19, 9, 13, 17, 22, 8, 2, 3, 0, 27, 26, 6, 25, 7, 1, 5, 11};

		final int[] usefulArr = new int[] {3, 8, 5, 9, 4, 7, 6, 0, 2, 1};

		final int[] outputCompactArr = VectorConversions.usefulToCompact(usefulArr);

//		final String toPrint1 = Arrays.toString(outputCompactArr);
//
//		System.out.println(toPrint1);
//
//		final int outputInt = VectorConversions.compactVectorToInt(outputCompactArr);
//
//		System.out.println(outputInt);
//
//		final int[] backToCompactFromInt = VectorConversions.intToCompactVector(outputCompactArr.length, outputInt);
//		final String toPrint2 = Arrays.toString(backToCompactFromInt);
//		System.out.println(toPrint2);

		final int[] backToUsefulFromCompact = VectorConversions.compactToUseful(outputCompactArr);

		// final String toPrint3 = Arrays.toString(backToUsefulFromCompact);
		// System.out.println(toPrint3);

		org.junit.Assert.assertArrayEquals(usefulArr, backToUsefulFromCompact);
	}

//	/**
//	 * Test method for {@link txtshuffle.VectorConversions#compactToUseful(int[])}.
//	 */
//	@Test
//	public final void testCompactToUseful() {
//		fail("Not yet implemented");
//	}

}
