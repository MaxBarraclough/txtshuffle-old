package txtshuffle;

import java.util.Arrays;

public final class TxtShuffle {

	
	private static class AnnotatedDatum
	{
		public final int val;
		public final int originalIndex;
		
		public AnnotatedDatum(int v, int o)
		{
			this.val = v;
			this.originalIndex = o;
		}
		
	}
	
	private static final int[] originalArr = {5,2,4,3,1};
	
	private static final AnnotatedDatum[] annotatedArr =
		{
			new AnnotatedDatum(5,0),
			new AnnotatedDatum(2,1),
			new AnnotatedDatum(4,2),
			new AnnotatedDatum(3,3),
			new AnnotatedDatum(1,4)
		};
	
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

		final int[] usefulArr = new int[] {1,2,0};
		
		final int[] outputCompactArr = VectorConversions.usefulToCompact(usefulArr);
		
		final String toPrint = Arrays.toString(outputCompactArr);
		
		System.out.println(toPrint);
		
	}

}
