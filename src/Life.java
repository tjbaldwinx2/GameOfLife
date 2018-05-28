/**
 *
 */

/**
 * @author Tyler Baldwin
 */
import java.util.*; // Scanner for random and scanner

public class Life { // main class
	// delare instance varables
	private boolean array1[][];
	private int birthlow;
	private int birthhigh;
	private int livelow;
	private int livehigh;


	public Life(long seed, int xdem, int ydem, int birthlow, int birthhigh, int livelow, int livehigh) throws IllegalArgumentException {

		this.birthlow = birthlow;
		this.birthhigh = birthhigh;
		this.livelow = livelow;
		this.livehigh = livehigh;
		// throws exceptions
		if (xdem < 1 || ydem < 1) {
			throw new IllegalArgumentException("Invalid Row or Column " + xdem + " " + ydem + "Must be higher than 1");
		}
		array1 = new boolean[xdem][ydem];// making 2d array
		fillarray(array1, seed);// method to fill array with values

		if (birthhigh < 1 || birthhigh > 9) {
			throw new IllegalArgumentException("Must be between 1 and 9. Check your birthhigh varable");
		}

		if (birthlow < 1 || birthlow > 9) {
			throw new IllegalArgumentException("Must be between 1 and 9. Check your birthlow varable");

		}
		if (livelow < 1 || livelow > 9) {
			throw new IllegalArgumentException("Must be between 1 and 9. Check your livelow varable");
		}

		if (livehigh < 1 || livehigh > 9) {
			throw new IllegalArgumentException("Must be between 1 and 9. Check your livehigh varable");
		}

	}

	// fills array with random variable. passes in array and a seed. when done
	// should fill array with random TF statements
	// check notebook for comment way
	private static void fillarray(boolean[][] array, long seed) {
		Random rand = new Random(seed);
		for (int i = 1; i < array.length - 1; i++) {// making sure that it print
													// only inside function
			for (int j = 1; j < array[i].length - 1; j++) {
				array[i][j] = rand.nextBoolean();// fill array with 1/0 values
			}
		}

	}

	// clones array. passes in array. when done it should clone the array so it
	// can be modifiled.
	private static boolean[][] clonearray(boolean[][] array) {
		boolean[][] myNewMatrix = (boolean[][]) array.clone();
		for (int xdem = 0; xdem < array.length; xdem++) {
			myNewMatrix[xdem] = (boolean[]) array[xdem].clone();
		}
		return myNewMatrix;
	}

	// checks for neighbors around a given value. passes in array, xindex, and
	// yindex. should check neighbor so it can check for birth.
	private static int neighborCount(boolean[][] array, int xindex, int yindex) {
		int count = 0;
		for (int a = xindex - 1; a <= xindex + 1; a++) {
			for (int r = yindex - 1; r <= yindex + 1; r++)
				if (array[a][r]) {
					count++;
				}

		}
		return count;
	}

	public boolean[][] world() {// checks for world based on clone. no
								// parameters. should pass in array1 in method.
		return clonearray(array1);
	}

	public void update() {// this updates the maxtix with instance varables. no
							// pararmetrs.
		updatematrix(array1, birthlow, birthhigh, livelow, livehigh);
	}

	private static void updatematrix(boolean[][] array, int birthlow, int birthhigh, int livelow, int livehigh) {
		boolean[][] clone = clonearray(array);// clones array from previous
												// method.
		for (int i = 1; i < array.length - 1; i++) {
			for (int j = 1; j < array[i].length - 1; j++) {
				int count1 = neighborCount(clone, i, j);
				if (array[i][j]) {
					if (count1 < livelow || count1 > livehigh) {
						array[i][j] = false;
					}
				} else {// mod this from previous if if statement. turned into
						// if else
					if (count1 >= birthlow && count1 <= birthhigh) {
						array[i][j] = true;
					}
				}
			}

		}

	}

	// prints matrix. passes in array. should print off array when done checing
	// in.
	// check comment for comment way
	public static void printMatrix(boolean[][] array) {
        for (boolean[] anArray : array) {// make sure it
            for (int j = 0; j < array[0].length; j++) {
                if (anArray[j]) {// gives true value
                    System.out.print("# ");// prints off true value
                } else
                    System.out.print("- ");// prints off false value
            }
            System.out.println("");// gives whitespace
        }
		System.out.println();
	}
}
