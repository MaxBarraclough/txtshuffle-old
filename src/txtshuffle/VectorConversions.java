package txtshuffle;

import java.util.ArrayList;




// TODO machinery for applying an 'order vector' (a.k.a. a 'useful' vector)
// TODO machinery to discover the order vector for an array of unique comparable values
// TODO machinery to compose order vectors? invert them?




// TODO assert well-formed useful vectors. No repetition, no out-of-bounds values.

// TODO eliminate pointless intermediate ArrayLists and boxing

// TODO move away from int and to either long or BigNumber

public final class VectorConversions {

	/**
	 * Not to be instantiated
	 */
	private VectorConversions() {}



	public static boolean isValidUsefulVector(int[] vec)
	{
		boolean ret = true;

		final int[] copy = new int[vec.length];

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

	public static boolean isValidCompactVector(int[] vec)
	{
		// // // CHECK INVARIANTS tick through and ensure monotonic 'narrowing' is as
		// expected

		boolean ret = true;

		final int outSz = vec.length;

		for (int i = 0; i != outSz; ++i) {
			final int got = vec[i];
			final int maxAllowed = outSz - (i + 1);

			// assert(got <= maxAllowed);
			if (got > maxAllowed) {
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
	 * @param theInt
	 * @return
	 */
	public static int[] intToCompactVector(final int extent, final int theInt)
	{
		int acc = theInt;
		int card = 1;

		// build up this AL 'backwards' then reverse as we copy across to an array
		final ArrayList<Integer> al = new ArrayList<Integer>(extent);

		for (int i = 0; i != extent; ++i)
		{
			assert(card <= extent);
			assert(card >= 0);
			assert(acc >= 0);

			final int temp = acc % card;
			// first time round we do x%1 (yielding zero, of course), which is fine

			al.add(temp);

			acc -= temp;

			assert(acc >= 0);

			acc /= card; // first time round, divides by 1, which is fine


			++card;
		}


		// reverse the order as we return

		final int[] ret = new int[al.size()];
		final int lastIndex = ret.length - 1;

		for (int i = 0; i != ret.length; ++i)
		{
			int oppositeEnd = lastIndex - i;
			ret[i] = al.get(oppositeEnd);
		}

		return ret;
	}


// // TODO separate method for compact vector verification, for use by asserts

	/**
	 *
	 * @param compactVector
	 * Must *not* omit the final element, i.e. final element must be 0
	 *
	 * @return
	 */
	public static int compactVectorToInt(final int[] compactVector)
	{
		assert(0 == compactVector[compactVector.length - 1]); // TODO should conditionally throw?

		assert(isValidCompactVector(compactVector));

		final int stopBefore = compactVector.length - 1;
		// omit last element in the compact vector, by stopping *before* the last element's index

		int acc = 0;

		int card = compactVector.length;
		// cardinality of the set of 'options' is initially the length of the vector

		for (int i = 0; i != stopBefore; ++i)
		{
			acc *= card; // First iteration, just multiplies zero. That's fine.
			acc += compactVector[i];
			--card;
		}
		return acc;
	}



	// TODO avoid linear-time horrors in this one, using AVL tree list

	/**
	 *
	 * @param usefulVector
	 */
	public static int[] usefulToCompact(final int[] usefulVector)
	{
		assert(isValidUsefulVector(usefulVector)); // TODO should conditionally throw?

		final int sz = usefulVector.length;

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
			final int searchingFor = usefulVector[i];
			final int indexIntoWorkingVec = workingVector.indexOf(searchingFor);

			assert(indexIntoWorkingVec >= 0);

			outputVector.add(indexIntoWorkingVec);
			workingVector.remove(indexIntoWorkingVec);
		}

		assert(workingVector.isEmpty());


		// TODO REMOVE THIS DUPLICATE
		{
			final int outSz = outputVector.size();
			for (int i = 0; i != outSz; ++i)
			{
				final int got = outputVector.get(i);
				final int maxAllowed = outSz - (i + 1);

				assert(got <= maxAllowed);
			}
		}


		// Clumsily convert from List<Integer> to int[]

		final int outSz = outputVector.size();
		final int[] outArr = new int[outSz];
		for(int i = 0; i != outSz; ++i)
		{
			outArr[i] = outputVector.get(i);
		}

		assert(isValidCompactVector(outArr));

		return outArr;
	}



	public static int[] compactToUseful(int[] compactVector)
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

		assert(isValidUsefulVector(outArr));

		return outArr;
	}


}
