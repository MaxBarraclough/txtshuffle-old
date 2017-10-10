package txtshuffle;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigInteger;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import txtshuffle.TxtShuffle.NumberTooGreatException;

public final class TxtShuffleTests {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testReversal() {


		final int[] usefulArr = new int[] {3, 8, 5, 9, 4, 7, 6, 0, 2, 1}; // non involutory

		final int[] reversed = TxtShuffle.invertSwizzleVector(usefulArr);

		final boolean shouldBeFalse = java.util.Arrays.equals(usefulArr, reversed);

		org.junit.Assert.assertFalse(shouldBeFalse);

		final int[] backAgain = TxtShuffle.invertSwizzleVector(reversed);

		org.junit.Assert.assertArrayEquals(usefulArr, backAgain);

		// TxtShuffle.applyOrderMapToStringArr(input, orderMap)
	}

	@Test
	public final void testFindSortingOrderMap() throws IOException
	{
		final String[] strs = TxtShuffle.readFileIntoStringArr("example1.txt");

		final int[] orderMapForFilesOrder = TxtShuffle.findSortingSwizzleVector(strs);

		final String[] strsSorted = strs.clone();
		java.util.Arrays.sort(strsSorted);

		final boolean shouldBeFalse = java.util.Arrays.equals(strs, strsSorted);
		org.junit.Assert.assertFalse(shouldBeFalse);


		final String[] strsAfterSortingOrder =
		  TxtShuffle.applyOrderMapToStringArr(strs, orderMapForFilesOrder);

		org.junit.Assert.assertArrayEquals(strsSorted, strsAfterSortingOrder);

		final int[] reversedOrderMap = TxtShuffle.invertSwizzleVector(orderMapForFilesOrder);
		final String[] strsUnsorted = strs.clone();
		TxtShuffle.applyOrderMapToStringArr(strsUnsorted, reversedOrderMap);

		org.junit.Assert.assertArrayEquals(strs, strsUnsorted);
	}


	@Test
	public final void encodeIntoDataAndRetrieve() throws IOException, NumberTooGreatException
	{
		final int secretNum = 19409;
		final BigInteger secretNum_BI = BigInteger.valueOf(secretNum);
		// 40319; // 40320 is fact(8) and is the lowest too-high integer

		String[] encoded = TxtShuffle.encodeSmallNumberIntoData("example1.txt", secretNum);

		final BigInteger retrievedNum = TxtShuffle.retrieveNumberFromData(encoded);

		org.junit.Assert.assertEquals(secretNum_BI, retrievedNum);
	}


	@Test
	public final void encodeIntoDataAndRetrieve_FineGrain() throws IOException, NumberTooGreatException
	{
		final BigInteger secretNum =
        new BigInteger(
         "123321100012345678998877663335544332231415926535897932384626433287911"
        +"998890055496064220900999744643839384732828372372347749000230696474742"
        +"993259855500111011011100424242000119911919191999447474744242442444222"
        +"998890055496064220900999744643839384732828372372347749000230696474742"
        +"228890055496064220900999744643839384732828372372347749000230696474742"
        +"111190055496064220900999744643839384732828372372347749000230696474111"
        +"118890055496064220900999744643839384732828372372347749000230696474742"
		);
				// BigInteger.valueOf(1233211230696474742L);

		final String[] strs = TxtShuffle.readFileIntoStringArr("example1.txt");

		// TODO ugly conversion business again ///////////////////////////////
		final int[] compact =
				VectorConversions.intToCompactVector(
				  strs.length,
				  secretNum
				);

//		final int[] compact = new int[compact_BIs.length];
//		for(int i = 0; i != compact.length; ++i)
//		{
//			compact[i] = compact_BIs[i].intValue();
//		}

		//////////////////////////////////////////////////////////////////////

		final int[] useful = VectorConversions.compactToSwizzle(compact);

		// final int[] orderMapForFilesOrder = TxtShuffle.findSortingOrderMap(strs); // NO! not needed for the encode direction, only for decode!

		final String[] strsSorted = strs.clone();
		java.util.Arrays.sort(strsSorted);

		final String[] strsEncodingNum = TxtShuffle.applyOrderMapToStringArr(strsSorted, useful);

		{
			final boolean shouldBeFalse = java.util.Arrays.equals(strs, strsEncodingNum);
			org.junit.Assert.assertFalse(shouldBeFalse);
		}

		{
			final boolean shouldBeFalse = java.util.Arrays.equals(strsSorted, strsEncodingNum);
			org.junit.Assert.assertFalse(shouldBeFalse);
		}

// Now let's go back and retrieve the number

		final int[] retrievedSortingOrderMap = TxtShuffle.findSortingSwizzleVector(strsEncodingNum);

		final int[] retrievedUseful = TxtShuffle.invertSwizzleVector(retrievedSortingOrderMap);

		org.junit.Assert.assertArrayEquals(retrievedUseful, useful);

		final int[] retrievedCompact = VectorConversions.swizzleToCompact(retrievedUseful);

		org.junit.Assert.assertArrayEquals(compact, retrievedCompact);

		final BigInteger retrievedNum = VectorConversions.compactVectorToInt(retrievedCompact);

		org.junit.Assert.assertEquals(secretNum, retrievedNum);

//		final String[] strsAfterSortingOrder =
//		  TxtShuffle.applyOrderMapToStringArr(strs, orderMapForFilesOrder);
//
//		org.junit.Assert.assertArrayEquals(strsSorted, strsAfterSortingOrder);
//
//		final int[] reversedOrderMap = TxtShuffle.inverseOrderMap(orderMapForFilesOrder);
//		final String[] strsUnsorted = strs.clone();
//		TxtShuffle.applyOrderMapToStringArr(strsUnsorted, reversedOrderMap);
	}

}
