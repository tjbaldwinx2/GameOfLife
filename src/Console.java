/**
 * The Console class is a console interface to the Life game.
 *
 * @author Tyler Baldwin
 * @date May 2018
 */

import java.util.Scanner;


public class Console {
     static int rounds = 2;


    /**
     * @param args unused
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Please enter the size of the matrix(rows, columns) :");
        int rows = in.nextInt();
        int columns = in.nextInt();
        System.out.println("How many rounds do you want to see?");
        rounds = in.nextInt();
        System.out.println("Please enter random seed: ");
        long seed = in.nextLong();
        System.out.println("Please enter birth range (low, high) :");
        int birthLow = in.nextInt();
        int birthHigh = in.nextInt();
        System.out.println("Please enter live range (low, high): ");
        int liveLow = in.nextInt();
        int liveHigh = in.nextInt();
        try {
            Life game = new Life(seed, rows, columns, birthLow, birthHigh, liveLow, liveHigh);
            playLife(game);
        } catch (IllegalArgumentException e) {
            System.out.println("Inappropriate values: " + e.getMessage());
        }
    }

    /**
     * Print a boolean matrix
     * @param matrix is a boolean matrix to be printed with # for true and - for false.
     */
    public static void printWorld(boolean[][] matrix) {
        for (int r = 0; r < matrix.length; r++) {
            for (int c = 0; c < matrix[0].length; c++) {
                if (matrix[r][c])
                    System.out.print(" # ");
                else
                    System.out.print(" - ");
            }
            System.out.println();
        }
        System.out.println();

    }

    /**
     * Play the game of Life starting with a given state
     * @param game is the Life object that provides the current state of Life
     */
    public static void playLife(Life game) {
        printWorld(game.world());
        for (int i = 0; i < rounds; i++) {
            game.update();
            printWorld(game.world());
        }
    }

}
