package txtshuffle;

import java.util.ArrayList;

public final class VectorConversions {

	/**
	 * 
	 * @param usefulVector
	 */
	public static int[] usefulToCompact(final int[] usefulVector)
	{
		final int sz = usefulVector.length;
		
		// TODO this is a disastrous choice of data structure!
		// An 'AVL tree list' would make more sense.
		final ArrayList<Integer> workingVector = new ArrayList<Integer>(sz);
		
		for (int i = 0; i != sz; ++i)
		{
			workingVector.add(i);
		}
		
		final ArrayList<Integer> outputVector = new ArrayList<Integer>(sz - 1);
		
		for (int i = 0; i != sz; ++i)
		{
			final int searchingFor = usefulVector[i];
			final int indexIntoWorkingVec = workingVector.indexOf(searchingFor);
			
			assert(indexIntoWorkingVec >= 0);
			
			outputVector.add(indexIntoWorkingVec);
			workingVector.remove(indexIntoWorkingVec);
		}
		
		// // // CHECK INVARIANTS tick through and ensure monotonic 'narrowing' is as expected
		
		{
			final int outSz = outputVector.size();
			for (int i = 0; i != outSz; ++i)
			{
				final int got = outputVector.get(i);
				final int maxAllowed = outSz - (i + 1);
				
				if (! ((got <= maxAllowed)))
				{
					int jjjj = 7;
				 // oops!
				}
				
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
		
		return outArr;
	}
}
