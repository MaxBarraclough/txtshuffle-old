package txtshuffle;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import txtshuffle.TxtShuffle.NumberTooGreatException;


// TODO no-allocate versions of the methods?

// TODO machinery for applying an 'order vector' (a.k.a. a 'useful' vector)
// TODO machinery to discover the order vector for an array of unique comparable values
// TODO machinery to compose order vectors? invert them?


// TODO think about scaling the algorithm. Mult. is O(n^2). Perhaps break up the problem
// and deal with the order of sub-sequences, and also with their ordering, etc, in a 'tiered' way...
// or something similar.
// Or is the mult. not so bad, as the smaller of the two operands is bounded by n (the row count)?


// TODO composition of order maps. Is this preferable to repeated transformations? Probably about the same...


// TODO assert well-formed swizzle vectors. No repetition, no out-of-bounds values.

// TODO eliminate pointless intermediate ArrayLists and boxing

// TODO move away from int and to either long or BigNumber

public final class VectorConversions {

	/**
	 * Not to be instantiated
	 */
	private VectorConversions() {}


	/**
	 * Check that each element is in the (0..n) interval,
	 * and is unique within the array.
	 * @param vec
	 * @return
	 */
	public static boolean isValidSwizzleVector(int[] vec)
	{
		boolean ret = true;

		final int[] copy = new int[vec.length];

		// Time complexity might be better if we used a hash-set,
		// (check interval each time, and each time check that
		// the value isn't already in the set before adding)
		// but this will do fine, especially for small vectors.
		// (Assuming O(n log(n)) sort.)
		System.arraycopy(vec, 0, copy, 0, vec.length);

		java.util.Arrays.sort(copy);

		for (int i = 0; i != copy.length; ++i)
		{
			if (copy[i] != i)
			{
				ret = false;
				break;
			}
		}

		return ret;
	}

	// And now the same again, line for line, but with Integer[]

	public static boolean isValidCompactVector(List<Integer> vec)
	{
		// vec = java.util.Collections.unmodifiableList(vec); // makes no difference (other than giving us some assurances)

		boolean ret = true;

		final int outSz = vec.size();

		for (int i = 0; i != outSz; ++i) {
			final int got = vec.get(i);
			final int maxAllowed = outSz - (i + 1);

			// assert(got <= maxAllowed);
			if ((got > maxAllowed) || (got < 0)) {
				ret = false;
				break;
			}
		}

		return ret;
	}


	public static boolean isValidCompactVector(int[] vec)
	{
		boolean ret = true;

		final int outSz = vec.length;

		for (int i = 0; i != outSz; ++i) {
			final int got = vec[i];
			final int maxAllowed = outSz - (i + 1);

			// assert(got <= maxAllowed);
			if ((got > maxAllowed) || (got < 0)) {
				ret = false;
				break;
			}
		}

		return ret;
	}



	/***
	 *
	 * @param extent
	 * Vector size
	 * @param secretNum
	 * @return
	 * @throws NumberTooGreatException
	 */
	public static BigInteger[] intToCompactVector(final int extent, final BigInteger secretNum)
			throws NumberTooGreatException
	{
		TxtShuffle.throwNtgeIfTooGreat(extent, secretNum); // Ensure we have enough bits to play with

		final BigInteger extent_BI = BigInteger.valueOf(extent);

		BigInteger acc = secretNum;

		BigInteger card = BigInteger.ONE; // int card = 1;
		// int would probably be fine but we'd end up converting to BigInteger anyway

		// build up this AL 'backwards' then reverse as we copy across to an array
		final ArrayList<BigInteger> al = new ArrayList<BigInteger>(extent);

		for (int i = 0; i != extent; ++i)
		{
			// assert(card <= extent);
			assert( card.compareTo(extent_BI) <= 0 );
			// assert(card >= 0);
			assert( card.compareTo(BigInteger.ZERO) >= 0 );
			assert(  acc.compareTo(BigInteger.ZERO) >= 0 ); // assert(acc >= 0);

			final BigInteger temp = acc.mod( card ); // final int temp = acc % card;
			// first time round we do x%1 (yielding zero, of course), which is fine

			al.add(temp);

			acc = acc.subtract(temp); // acc -= temp;

			assert(acc.compareTo(BigInteger.ZERO) >= 0); // assert(acc >= 0);

			acc = acc.divide(card); // acc /= card; // first time round, divides by 1, which is fine
			card = card.add(BigInteger.ONE); // ++card;
		}

		// reverse the order as we return

		final BigInteger[] ret = new BigInteger[al.size()];
		final int lastIndex = ret.length - 1;

		for (int i = 0; i != ret.length; ++i)
		{
			int oppositeEnd = lastIndex - i;
			ret[i] = al.get(oppositeEnd);
		}

		assert(java.util.Arrays.equals(ret, intToCompactVector_Fast(extent,secretNum)));

		return ret;
	}



	public static BigInteger[] intToCompactVector_Fast(final int extent, final BigInteger secretNum)
			throws NumberTooGreatException
	{
		TxtShuffle.throwNtgeIfTooGreat(extent, secretNum); // Ensure we have enough bits to play with

		final BigInteger extent_BI = BigInteger.valueOf(extent);

		BigInteger acc = secretNum;

		BigInteger card = BigInteger.ONE; // int card = 1;
		// int would probably be fine but we'd end up converting to BigInteger anyway

		final BigInteger[] ret = new BigInteger[extent];

		for (int ii = extent - 1; ii >= 0; --ii)
		{
			assert( card.compareTo(extent_BI) <= 0 ); // assert(card <= extent);
			assert( card.compareTo(BigInteger.ZERO) >= 0 ); // assert(card >= 0);
			assert(  acc.compareTo(BigInteger.ZERO) >= 0 ); // assert(acc >= 0);

			final BigInteger temp = acc.mod( card ); // final int temp = acc % card;
			// first time round we do x%1 (yielding zero, of course), which is fine

			ret[ii] = temp; // al.add(temp);

			acc = acc.subtract(temp); // acc -= temp;

			assert(acc.compareTo(BigInteger.ZERO) >= 0); // assert(acc >= 0);

			acc = acc.divide(card); // acc /= card; // first time round, divides by 1, which is fine
			card = card.add(BigInteger.ONE); // ++card;
		}

		return ret;
	}






	/**
	 *
	 * @param compactVector
	 * Must *not* omit the final element, i.e. final element must be 0
	 *
	 * @return
	 */
	public static BigInteger compactVectorToInt(final int[] compactVector)
	{
		assert(0 == compactVector[compactVector.length - 1]); // TODO should conditionally throw?

		assert(isValidCompactVector(compactVector));

		final int stopBefore = compactVector.length - 1;
		// omit last element in the compact vector, by stopping *before* the last element's index

		// int acc = 0;
		BigInteger acc = BigInteger.ZERO;

		BigInteger card = BigInteger.valueOf(compactVector.length);
		// int card = compactVector.length;
		// cardinality of the set of 'options' is initially the length of the vector

		for (int i = 0; i != stopBefore; ++i)
		{
			// acc *= card; // First iteration, just multiplies zero. That's fine.

			acc = acc.multiply(card);

			// acc += compactVector[i];
			BigInteger toAdd = BigInteger.valueOf(compactVector[i]);
			acc = acc.add( toAdd );

			// --card;
			card = card.subtract(BigInteger.ONE);
		}
		return acc;
	}



	// TODO avoid linear-time horrors in this one, using AVL tree list

	public static int[] swizzleToCompact(final int[] swizzleVec)
	{
		assert(isValidSwizzleVector(swizzleVec)); // TODO should conditionally throw?

		final int sz = swizzleVec.length;

		// TODO this is a disastrous choice of data structure!
		// An 'AVL tree list' would make more sense.
		final ArrayList<Integer> workingVector = new ArrayList<Integer>(sz);

		for (int i = 0; i != sz; ++i)
		{
			workingVector.add(i);
		}

		final ArrayList<Integer> outputVector = new ArrayList<Integer>(sz);

		for (int i = 0; i != sz; ++i)
		{
			final int searchingFor = swizzleVec[i];
			final int indexIntoWorkingVec = workingVector.indexOf(searchingFor);

			assert(indexIntoWorkingVec >= 0);

			outputVector.add(indexIntoWorkingVec);
			workingVector.remove(indexIntoWorkingVec);
		}

		assert( workingVector.isEmpty() );
		assert( isValidCompactVector(outputVector) );

		// Clumsily convert from List<Integer> to int[]

		final int outSz = outputVector.size();
		final int[] outArr = new int[outSz];
		for(int i = 0; i != outSz; ++i)
		{
			outArr[i] = outputVector.get(i);
		}

		assert(isValidCompactVector(outArr));

		assert(java.util.Arrays.equals(
				outArr,
				swizzleToCompact_Fast(swizzleVec)
		       ));


		return outArr;
	}


	public static int[] swizzleToCompact_Fast(final int[] swizzleVec)
	{
		assert(isValidSwizzleVector(swizzleVec)); // TODO should conditionally throw?

		final int sz = swizzleVec.length;

		// TODO this is a disastrous choice of data structure!
		// An 'AVL tree list' would make more sense.
		final ArrayList<Integer> workingVector = new ArrayList<Integer>(sz);

		for (int i = 0; i != sz; ++i)
		{
			workingVector.add(i);
		}

		final int[] outArr = new int[sz];

		for (int i = 0; i != sz; ++i)
		{
			final int searchingFor = swizzleVec[i];
			final int indexIntoWorkingVec = workingVector.indexOf(searchingFor);

			assert(indexIntoWorkingVec >= 0);

			// outputVector.add(indexIntoWorkingVec);
			outArr[i] = indexIntoWorkingVec;

			workingVector.remove(indexIntoWorkingVec);
		}

		assert( workingVector.isEmpty() );
		assert( isValidCompactVector(outArr) );

		return outArr;
	}






	public static int[] compactToSwizzle(int[] compactVector)
	{
		assert(isValidCompactVector(compactVector));

		// First set up the vector augmentation, as with usefulToCompact
		// TODO comment this properly
		final int sz = compactVector.length;

		// TODO this is a disastrous choice of data structure!
		// An 'AVL tree list' would make more sense.
		final ArrayList<Integer> workingVector = new ArrayList<Integer>(sz);

		for (int i = 0; i != sz; ++i)
		{
			workingVector.add(i);
		}

		final ArrayList<Integer> outputVector = new ArrayList<Integer>(sz);


		for (int indexIntoCompactVec = 0; indexIntoCompactVec != sz; ++indexIntoCompactVec)
		{
			final int indexIntoWorkingVec = compactVector[indexIntoCompactVec];
			final int val = workingVector.get(indexIntoWorkingVec);
			outputVector.add(val);
			workingVector.remove(indexIntoWorkingVec);
		}

		assert(workingVector.isEmpty());

		// Clumsily convert from List<Integer> to int[]

		final int outSz = outputVector.size();
		final int[] outArr = new int[outSz];
		for(int i = 0; i != outSz; ++i)
		{
			outArr[i] = outputVector.get(i);
		}

		assert(isValidSwizzleVector(outArr));

		return outArr;
	}


}
