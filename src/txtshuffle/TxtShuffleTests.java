package txtshuffle;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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

		final int[] reversed = TxtShuffle.reverseOrderMap(usefulArr);

		final boolean shouldBeFalse = java.util.Arrays.equals(usefulArr, reversed);

		org.junit.Assert.assertFalse(shouldBeFalse);

		final int[] backAgain = TxtShuffle.reverseOrderMap(reversed);

		org.junit.Assert.assertArrayEquals(usefulArr, backAgain);

		// TxtShuffle.applyOrderMapToStringArr(input, orderMap)
	}

	@Test
	public final void testFindSortingOrderMap() throws IOException
	{
		final String[] strs = TxtShuffle.readFileIntoStringArr("example1.txt");

		final int[] orderMapForFilesOrder = TxtShuffle.findSortingOrderMap(strs);

		final String[] strsSorted = strs.clone();
		java.util.Arrays.sort(strsSorted);

		final boolean shouldBeFalse = java.util.Arrays.equals(strs, strsSorted);
		org.junit.Assert.assertFalse(shouldBeFalse);


		final String[] strsAfterSortingOrder =
		  TxtShuffle.applyOrderMapToStringArr(strs, orderMapForFilesOrder);

		org.junit.Assert.assertArrayEquals(strsSorted, strsAfterSortingOrder);

		final int[] reversedOrderMap = TxtShuffle.reverseOrderMap(orderMapForFilesOrder);
		final String[] strsUnsorted = strs.clone();
		TxtShuffle.applyOrderMapToStringArr(strsUnsorted, reversedOrderMap);

		org.junit.Assert.assertArrayEquals(strs, strsUnsorted);
	}

}
