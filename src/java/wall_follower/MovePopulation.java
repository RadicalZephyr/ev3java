/* @(#)MovePopulation.java
 */
/**
 *
 *
 * @author <a href="mailto:geoff@minty-dark-tower">Geoff Shannon</a>
 */
package wall_follower;

import java.util.Random;
import java.util.Collections;

import java.util.NavigableSet;
import java.util.TreeSet;

import java.util.List;
import java.util.ArrayList;

public class MovePopulation {

    private static final int BASE_ITERATIONS_PER_GEN = 10;
    private static final int ITERATIONS_MULTIPLIER = 2;

    private TreeSet<Move> nonePressed;
    private TreeSet<Move> leftPressed;
    private TreeSet<Move> rightPressed;
    private TreeSet<Move> leftAndRight;
    private Random rand;

    private int generation;
    private int iteration;
    private int nextGeneration;

    public MovePopulation(Random rand) {
        generation = 0;
        iteration = 0;
        nextGeneration = BASE_ITERATIONS_PER_GEN;

        this.rand = rand;
        this.nonePressed = new TreeSet<Move>();
        this.leftPressed = new TreeSet<Move>();
        this.rightPressed = new TreeSet<Move>();
        this.leftAndRight = new TreeSet<Move>();
    }

    public void add(Move move) {
        NavigableSet<Move> set = getSetForMove(move);
        set.add(move);
    }

    public Move getMoveForReading(SensorReading reading) {
        // Assume that get is run once for each iteration
        iteration++;
        if (iteration >= nextGeneration) {
            incrementGeneration();
        }

        Move move = new Move(reading);
        Move minMove = new Move(reading);
        minMove.minDistance = reading.distance - Move.DISTANCE_RANGE;

        NavigableSet<Move> set = getSetForMove(move);

        NavigableSet<Move> legalMoves = set.subSet(minMove, true,
                                             move, true);

        if (legalMoves.size() == 0) {
            seedPopulationForReading(reading);
            return getMoveForReading(reading);
        }

        Move finalMove = legalMoves.first();
        int size = legalMoves.size();
        int item = rand.nextInt(size);
        int i = 0;
        for (Move m : legalMoves) {
            if (i == item) {
                finalMove = m;
            }
            i++;
        }
        return finalMove;
    }

    private NavigableSet<Move> getSetForReading(SensorReading reading) {
        return getSet(reading.leftPressed, reading.rightPressed);
    }

    private NavigableSet<Move> getSetForMove(Move move) {
        return getSet(move.leftPressed, move.rightPressed);
    }

    private NavigableSet<Move> getSet(boolean left, boolean right) {
        if (left && right) {
          return leftAndRight;
        } else if (left) {
            return leftPressed;
        } else if (right) {
            return rightPressed;
        } else {
            return nonePressed;
        }
    }

    /**
     *  This method MUST ensure that getMoveForReading will find at
     *  least one legal move.  It is used to generate populations on
     *  the fly for new situations.
     */
    public void seedPopulationForReading(SensorReading reading) {
        NavigableSet<Move> set = getSetForReading(reading);
        for (int i = 0; i < 10; i++) {
            set.add(new Move(reading, rand));
        }
    }

    private void incrementGeneration() {
        generation++;
        iteration = 0;

        List<Move> allMoves = new ArrayList<Move>();
        allMoves.addAll(leftAndRight);
        allMoves.addAll(leftPressed);
        allMoves.addAll(rightPressed);
        allMoves.addAll(nonePressed);

        Collections.sort(allMoves, new Move.CompareFitness());
    }
}
