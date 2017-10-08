package txtshuffle;

import java.util.Arrays;

public final class TxtShuffle {


	/**
	 * Sort an array of ints, treating those ints
	 * as indices into a String[]
	 * @author mb
	 *
	 */
	private static final class CustomIntegerComparator implements java.util.Comparator<Integer>
	{

		final String[] strings;

		public CustomIntegerComparator(String[] strs)
		{
			this.strings = strs;
		}

		@Override
		public int compare(Integer i1, Integer i2)
		{
			String s1 = this.strings[i1];
			String s2 = this.strings[i2];
			int ret = s1.compareTo(s2);
			return ret;
		}

	}

	// All this order-map business is analogous to matrix product, but that wouldn't buy us anything in implementation


	/**
	 * Returns a new array which is the desired reordering of the input array
	 * @param input
	 * @return
	 */
	private static String[] applyOrderMapToStringArr(String[] input, int[] orderMap)
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

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// final int[] usefulArr = new int[] {1,2,0};


		// causes our int accumulator variables to overflow, seemingly
//		final int[] usefulArr = new int[]
//				{10, 15, 23, 14, 12, 16, 28, 18, 24, 21, 20, 4, 29, 19, 9, 13, 17, 22, 8, 2, 3, 0, 27, 26, 6, 25, 7, 1, 5, 11};

		final int[] usefulArr = new int[] {3, 8, 5, 9, 4, 7, 6, 0, 2, 1};


		final int[] outputCompactArr = VectorConversions.usefulToCompact(usefulArr);

		final String toPrint1 = Arrays.toString(outputCompactArr);

		System.out.println(toPrint1);

		final int outputInt = VectorConversions.compactVectorToInt(outputCompactArr);

		System.out.println(outputInt);


		final int[] backToCompactFromInt = VectorConversions.intToCompactVector(outputCompactArr.length, outputInt);
		final String toPrint2 = Arrays.toString(backToCompactFromInt);
		System.out.println(toPrint2);


		final int[] backToUsefulFromCompact = VectorConversions.compactToUseful(outputCompactArr);
		final String toPrint3 = Arrays.toString(backToUsefulFromCompact);
		System.out.println(toPrint3);
	}



}
