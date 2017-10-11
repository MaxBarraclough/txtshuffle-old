package txtshuffle;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public final class TxtShuffle {


	// TODO move code away from arrays and toward.... something else... some interface?

	// TODO if we care more about fast decode than fast encode,
	// we should do the inverseSwizzleVector on the encode side, not on the decode side


	public final static class NumberTooGreatException extends Exception
	{
		private static final long serialVersionUID = -2586736610035380677L;
	}


	private static BigInteger naiveFact(final int val)
	{
		assert(val >= 0);

		// int acc = 1;
		BigInteger acc = BigInteger.ONE;
		// for (int i = val; i >= 0; --i) // NO! Won't map 0 to 1 as desired. We can skip the last two iterations.
		for (int i = val; i > 1; --i) // mult the large nums first; overflow as early as possible
		{
			// acc = Math.multiplyExact(i, acc); // throws on overflow
			acc = acc.multiply(BigInteger.valueOf(i));
		}

		return acc;
	}


	public static void throwNtgeIfTooGreat(final int vecLength, final BigInteger numToEncode)
			throws NumberTooGreatException
	{
		boolean numberIsOk = false;

//		try
		{
			final BigInteger maxValPlusOne = naiveFact(vecLength);

			// numberIsOk = (numToEncode < maxValPlusOne);
			numberIsOk = (numToEncode.compareTo(maxValPlusOne) == -1);
		}
//		catch (ArithmeticException ae)
//		{
//			numberIsOk = false;
//		}

		if (!numberIsOk)
		{
			throw new NumberTooGreatException();
		}
	}


	// TODO param name consistency



	// TODO PRIORITY move this over to BigInteger

	public static String[] encodeSmallNumberIntoData(final String filePath, final int secretNum)
			throws IOException, NumberTooGreatException
	{
		final String[] strs = TxtShuffle.readFileIntoStringArr(filePath);

		throwNtgeIfTooGreat(strs.length, BigInteger.valueOf(secretNum));


		// TODO this won't be necessary once everything is BigInteger
		//// Nasty business converting from BigInteger[] to int[] ////

		final BigInteger secretNum_BI = BigInteger.valueOf(secretNum);

		final int[] compact
		  = VectorConversions.intToCompactVector(strs.length, secretNum_BI);

//		final int[] compact = new int[compact_BIs.length];
//		for(int i = 0; i != compact.length; ++i)
//		{
//			compact[i] = compact_BIs[i].intValue();
//		}

		//////////////////////////////////////////////////////////////

		final int[] useful = VectorConversions.compactToSwizzle(compact);

		// final int[] swizzleVectorForFilesOrder = TxtShuffle.findSortingSwizzleVector(strs);
		// No! Not needed for the encode direction, only for decode.

		java.util.Arrays.sort(strs); // Mutates existing array

		final String[] strsEncodingNum = TxtShuffle.applySwizzleVectorToStringArr(strs, useful);

		return strsEncodingNum;
	}



	public static BigInteger retrieveNumberFromData(final String[] data)
	{
//		final int[] retrievedSortingSwizzleVec = TxtShuffle.findSortingSwizzleVector(data);
//		final int[] retrievedUseful = TxtShuffle.invertSwizzleVector(retrievedSortingSwizzleVec);

		final int[] retrievedUseful = TxtShuffle.findInverseOfSortingSwizzleVec(data);

		final int[] retrievedCompact = VectorConversions.swizzleToCompact(retrievedUseful);

		final BigInteger retrievedNum = VectorConversions.compactVectorToInt(retrievedCompact);

		return retrievedNum;
	}



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

	// All this swizzle vec business is analogous to matrix product, but that wouldn't buy us anything in implementation




	public static String[] readFileIntoStringArr(final String path) throws IOException
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



	// TODO the final inversion stage in findSwizzleVector can be omitted if we
	// just apply the inverse swizzle vector, instead.
	// This shouldn't be any harder.


/**
 * AVOID this method - we want to avoid doing that invert!
 * Find the swizzle vector which transforms the original data into its sorted order
 * @param inputData
 * @return
 */
	public static int[] findSortingSwizzleVector_AVOID(final String[] inputData)
	{
		// We sort, but we sort an int array, treating them as indices into our data.
		// The result is an int array which specifies the order of the sorted data.
		// This *isn't* the same thing as the swizzle-vec which transforms the data into its sorted order!
		// It's actually the *inverse*. So we 'invert' the swizzle vector, and return that.

		final Integer[] swizzleVecBoxed = new Integer[inputData.length];

		for (int i = 0; i != swizzleVecBoxed.length; ++i)
		{
			swizzleVecBoxed[i] = i;
		}

		final CustomIntegerComparator c = new CustomIntegerComparator(inputData);

		java.util.Arrays.sort(swizzleVecBoxed,c);


		// Laboriously unbox

		final int[] unboxedArr = new int[swizzleVecBoxed.length];

		for (int i = 0; i != unboxedArr.length; ++i)
		{
			unboxedArr[i] = swizzleVecBoxed[i];
		}

		// Invert
		final int[] ret = TxtShuffle.invertSwizzleVector(unboxedArr);

		return ret;
	}






/**
 * AVOID this method - we want to avoid doing that invert!
 * Find the swizzle vector which transforms the original data into its sorted order
 * @param inputData
 * @return
 */
	public static int[] findInverseOfSortingSwizzleVec(final String[] inputData)
	{
		// We sort, but we sort an int array, treating them as indices into our data.
		// The result is an int array which specifies the order of the sorted data.
		// This *isn't* the same thing as the swizzle-vec which transforms the data into its sorted order!
		// It's actually the *inverse*. Fortunately, this is just as useful.

		final Integer[] swizzleVecBoxed = new Integer[inputData.length];

		for (int i = 0; i != swizzleVecBoxed.length; ++i)
		{
			swizzleVecBoxed[i] = i;
		}

		final CustomIntegerComparator c = new CustomIntegerComparator(inputData);

		java.util.Arrays.sort(swizzleVecBoxed,c);


		// Laboriously unbox

		final int[] unboxedArr = new int[swizzleVecBoxed.length];

		for (int i = 0; i != unboxedArr.length; ++i)
		{
			unboxedArr[i] = swizzleVecBoxed[i];
		}

		return unboxedArr;
	}








	// TODO move to some other class?
	// If we were using matrix-product to implement our vector swizzling,
	// we could implement this as a transpose, as permutation matrices are orthogonal matrices.
	public static int[] invertSwizzleVector(final int[] swizzleVec)
	{
		assert( VectorConversions.isValidSwizzleVector(swizzleVec) );

		final int[] reversed = new int[swizzleVec.length];

		for (int i = 0; i != swizzleVec.length; ++i)
		{
			final int index = swizzleVec[i];
			reversed[index] = i;
		}

		return reversed;
	}



	/**
	 * Returns a new array which is the desired reordering of the input array
	 * @param input
	 * @return
	 */
	public static String[] applySwizzleVectorToStringArr(final String[] input, final int[] swizzleVec)
	{
		assert(input.length == swizzleVec.length); // explodes if either is null
		assert(VectorConversions.isValidSwizzleVector(swizzleVec));
		// ASSUME: no null values in 'input' array... this assumption is probably made elsewhere too

		final String[] output = new String[input.length];

		for (int i = 0; i != input.length; ++i)
		{
			final int desiredIndex = swizzleVec[i];
			output[desiredIndex] = input[i];
		}

		return output;
	}




	public static String[] applyInvSwizzleVectorToStringArr(final String[] input, final int[] invSwizzleVec)
	{
		assert(input.length == invSwizzleVec.length); // explodes if either is null
		assert(VectorConversions.isValidSwizzleVector(invSwizzleVec));
		// ASSUME: no null values in 'input' array... this assumption is probably made elsewhere too

		final String[] output = new String[input.length];

		for (int i = 0; i != input.length; ++i)
		{
			// final int desiredIndex = swizzleVec[i];
			// String valToAssign = input[i];
			// output[desiredIndex] = valToAssign;

			// With a forward swizzle vector, vec[x] == y means do out[y] = input[x]
			// with an inv. swizzle vect,     vec[x] == y means do out[x] = input[y]

			final int desiredIndex = i;
			String valToAssign = input[invSwizzleVec[i]];
			output[desiredIndex] = valToAssign;
		}

		assert(java.util.Arrays.equals(applySwizzleVectorToStringArr(input,invertSwizzleVector(invSwizzleVec)), output));

		return output;
	}


	// EXTRINSIC WINS, THERE'S NO POINT HAULING PAIR OBJECTS AROUND



	// Two ways to track the 'original index' question:
	// 'intrinsic' with a 'wrapper type' with another member for original index, or
	// 'extrinsic' where we hash-map each item to its original index.
	// Handling non-unique entities would be slightly easier with the intrinsic approach.
	// It could be done with the intrinsic approach though, we'd just need to map to a collection.



	/*
	 * To encode, we want to convert a big number (or whatever) into a 'swizzle vector' (a 'pivot vector'?),
	 * then apply that swizzle vector to the source data. (Assume element-uniqueness for now.)
	 *
	 * To decode, we want to figure out what swizzle vector was applied, then map that back
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
	 * Definition of a swizzle vector:
	 *
	 * A swizzle vector is a data structure which maps each entity to its index in the output array.
	 * Applying an swizzle vector, then, reorders the input array in a unique way (giving a unique output).
	 *
	 * It's name reflects the 'swizzle' feature of, for example, OpenCL C, where
	 *   myVec1.xyz = myVec1.zyx;
	 * will reverse the elements of the myVec1 vector.
	 *
	 *
	 * We can use an array for the purpose, such that
	 *   orderMapArray[n] == m
	 * means we should do
	 *   outputArray[m] = inputArray[n]
	 *
	 * REPHRASE THIS We can represent a swizzle vector as a number,
	 * such that each swizzle vector corresponds to
	 * exactly one number in an interval from 0 to some max. number,
	 * and such that each number in that interval corresponds to exactly one swizzle vector.
	 *
	 * To do this mapping, it's easier to first consider the function which takes us from
	 * the swizzle vector to the unique number.
	 *
	 * REWRITE THIS:
	 *
	 * Given a swizzle vector of length L:
	 * set up an accumulator, initializing at 0
	 * iterate through the swizzle vector, and for each element:
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
	 * GOING FORWARD WE BUILD 2 SWIZZLE VECTORS THEN WE COMBINE THEM THEN WE CONVERT TO NUMBER
	 *
	 * We generate an output array from a sorted input array by
	 * 1.   Invert the map (create a new one which gives us the inverse relation)
	 *      (For now, we're assuming entity uniqueness.)
	 *
	 * iterating through
	 * the input array and
	 *
	 * Decoding a swizzle vector:
	 * Given a map of n many elements,
	 *
	 *
	 *
	 * Generating the swizzle vector:
	 *
	 *
	 * So, using the intrinsic approach:
	 * 1.   Sort the annotated array.
	 * 2.   Consulting the swizzle vector and the now-sorted annotated array, write the output data
	 */

//	public static void main(String[] args) {
//
//	}



}
