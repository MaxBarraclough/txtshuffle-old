package txtshuffle;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public final class TxtShuffle {


	/**
	 * Sort an array of ints, treating those ints
	 * as indices into a String[]
	 * @author mb
	 *
	 */
	private static final class CustomIntegerComparator implements java.util.Comparator<Integer>
	{

		private final String[] strings;

		public CustomIntegerComparator(final String[] strs)
		{
			this.strings = strs;
		}

		@Override
		public int compare(final Integer i1, final Integer i2)
		{
			String s1 = this.strings[i1];
			String s2 = this.strings[i2];
			int ret = s1.compareTo(s2);
			return ret;
		}

	}

	// All this order-map business is analogous to matrix product, but that wouldn't buy us anything in implementation




	public static String[] readFileIntoStringArr(String path) throws IOException
	{
		// inspired by https://stackoverflow.com/a/326440
		final List<String> lines = Files.readAllLines(Paths.get(path), Charset.defaultCharset());

//		StringBuilder sb = new StringBuilder();

//		final int linesCount = lines.size();
//
//		for (int i = 0; i != linesCount; ++i)
//		{
//			sb.append(lines.get(i));
//		}
//
//		String ret = sb.toString();

		// surprisingly hard to get a String[] out of a List<String>
//		final Object[] retObjs = lines.toArray(); // yes, yet another avoidable copy
//		final String[] ret = (String[])retObjs;

		final String[] ret = new String[lines.size()];

		for (int i = 0; i != ret.length; ++i)
		{
			ret[i] = lines.get(i);
		}

		return ret;
	}



	// TODO the final inversion stage in findSortingOrderMap can be omitted if we
	// just apply the inverse order map vector, instead.
	// This shouldn't be any harder.


/**
 * Find the order map which transforms the original data into its sorted order
 * @param inputData
 * @return
 */
	public static int[] findSortingOrderMap(final String[] inputData)
	{
		// We sort, but we sort an int array, treating them as indices into our data.
		// The result is an int array which specifies the order of the sorted data.
		// This *isn't* the same thing as the order-map which transforms the data into its sorted order!
		// It's actually the *inverse*. So we order-map-invert the int array, and return that.

		final Integer[] orderMapBoxed = new Integer[inputData.length];

		for (int i = 0; i != orderMapBoxed.length; ++i)
		{
			orderMapBoxed[i] = i;
		}

		final CustomIntegerComparator c = new CustomIntegerComparator(inputData);

		java.util.Arrays.sort(orderMapBoxed,c);


		// Laboriously unbox

		final int[] unboxedArr = new int[orderMapBoxed.length];

		for (int i = 0; i != unboxedArr.length; ++i)
		{
			unboxedArr[i] = orderMapBoxed[i];
		}

		// Inverse
		final int[] ret = TxtShuffle.reverseOrderMap(unboxedArr);

		return ret;
	}



	// TODO move to some other class?
	public static int[] reverseOrderMap(final int[] orderMap)
	{
		assert( VectorConversions.isValidUsefulVector(orderMap) );

		final int[] reversed = new int[orderMap.length];

		for (int i = 0; i != orderMap.length; ++i)
		{
			final int index = orderMap[i];
			reversed[index] = i;
		}

		return reversed;
	}



	/**
	 * Returns a new array which is the desired reordering of the input array
	 * @param input
	 * @return
	 */
	public static String[] applyOrderMapToStringArr(final String[] input, final int[] orderMap)
	{
		assert(input.length == orderMap.length); // explodes if either is null
		assert(VectorConversions.isValidUsefulVector(orderMap));
		// ASSUME: no null values in 'input' array... this assumption is probably made elsewhere too

		final String[] output = new String[input.length];

		for (int i = 0; i != input.length; ++i)
		{
			final int desiredIndex = orderMap[i];
			output[desiredIndex] = input[i];
		}

		return output;
	}



	// EXTRINSIC WINS, THERE'S NO POINT HAULING PAIR OBJECTS AROUND



	// Two ways to track the 'original index' question:
	// 'intrinsic' with a 'wrapper type' with another member for original index, or
	// 'extrinsic' where we hash-map each item to its original index.
	// Handling non-unique entities would be slightly easier with the intrinsic approach.
	// It could be done with the intrinsic approach though, we'd just need to map to a collection.



	/*
	 * To encode, we want to convert a big number (or whatever) into an 'order map',
	 * then apply that order map to the source data. (Assume uniqueness for now.)
	 *
	 * To decode, we want to figure out what order map was applied, then map that back
	 * to a big number (or whatever).
	 */


	/*
	 * ENCODING, IN DETAIL
	 *
	 * (Ignoring non-unique entities for now.)
	 * We don't lose anything taking a sort-first approach.... do we?
	 * (i.e. scrambling the input data wouldn't actually impact our output data...)
	 * Shouldn't be any need to do that anyway - just apply the necessary ordering.
	 *
	 * No, that doesn't work! We need each order to be uniquely identifiable
	 * without reference to the original/canonical ordering!
	 * So, we *should* 'sort first'. This gives us a 'normal form' as it were,
	 * which we can then reorder in a unique and reversible way.
	 *
	 *
	 * Definition of an order map:
	 *
	 * An order map is a data structure which maps each entity to its index in the output array.
	 * Applying an order map, then, reorders the input array in a unique way (giving a unique output).
	 *
	 * We can use an array for the purpose, such that
	 *   orderMapArray[n] == m
	 * means we should do
	 *   outputArray[m] = inputArray[n]
	 *
	 * REPHRASE THIS We can represent an order map as a number,
	 * such that each order map corresponds to
	 * exactly one number in an interval from 0 to some max. number,
	 * and such that each number in that interval corresponds to exactly one order map.
	 *
	 * To do this mapping, it's easier to first consider the function which takes us from
	 * the order map to the unique number.
	 *
	 * REWRITE THIS:
	 *
	 * Given an order map of length L:
	 * set up an accumulator, initializing at 0
	 * iterate through the order map, and for each element:
	 *   'multiply up' the accumulator to 'make space' for this slot.
	 *   For the first element that means do nothing, for subsequent ones,
	 *   multiply by MAXPOSSIBLE-1....
	 *
	 *  KEEP REMOVING AS WE GO, AND KEEP THAT IN MIND WHEN WE DO THE INVERSE...
	 *
	 *  ??? WHAT'S A GOOD TREE FOR THAT KIND OF THING?
	 *  AVL trees can be adopted for the purpose - https://www.nayuki.io/page/avl-tree-list
	 *
	 *   WAIT DO WE NEED TO MESS ABOUT WITH ORDER TO GET THAT MONOTONICITY???? GRRRR!
	 *   I THINK WE NEED TO DO MORE WORK HERE, INDEXING INTO THE CANDIDATE INDICES SPACE, NOT
	 *   THE FULL INDICES SPACE
	 *
	 * GOING FORWARD WE BUILD 2 ORDER MAPS THEN WE COMBINE THEM THEN WE CONVERT TO NUMBER
	 *
	 * We generate an output array from a sorted input array by
	 * 1.   Invert the map (create a new one which gives us the inverse relation)
	 *      (For now, we're assuming entity uniqueness.)
	 *
	 * iterating through
	 * the input array and
	 *
	 * Decoding an order map:
	 * Given a map of n many elements,
	 *
	 *
	 *
	 * Generating the order map:
	 *
	 *
	 * So, using the intrinsic approach:
	 * 1.   Sort the annotated array.
	 * 2.   Consulting the order-map and the now-sorted annotated array, write the output data
	 */

//	public static void main(String[] args) {
//
//	}



}
