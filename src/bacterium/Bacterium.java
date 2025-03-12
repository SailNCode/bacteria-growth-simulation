package bacterium;

import pres.Cell;
import pres.PetriTableModel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

public class Bacterium implements Callable<Integer> {
    private final String name;
    private final Color color;
    private final int speedFactor;
    private final PetriTableModel petriTableModel;
    //Flag for informing whether Bacterium started working
    private volatile boolean started = false;
    //Internal copy of the board to keep track of occupied cells
    private final int[][] boardCopy;
    private volatile int squaresOccupied = 0;

    //OK
    public Bacterium(String name, Color color, PetriTableModel petriTableModel, BacteriumType bacteriumType) {
        this.name = name;
        this.color = color;
        this.speedFactor = bacteriumType.getSpeedFactor();
        this.petriTableModel = petriTableModel;
        int sideNCells = petriTableModel.getSideNCells();
        boardCopy = new int[sideNCells][sideNCells];

        /*  Occupying initial position on the board, this method always return Cell.
            If no Cell has been found, then Exception would be thrown.
        */
        Cell startingCell  = petriTableModel.occupyRandomCell(color);
        boardCopy[startingCell.rowIndex][startingCell.columnIndex] = 1;
        synchronized (this) {
            squaresOccupied++;
        }
    }

    public Bacterium(String name, Color color, PetriTableModel petriTableModel) {
        this(name, color, petriTableModel, BacteriumType.values()[new Random().nextInt(BacteriumType.values().length)]);
    }

    @Override
    public Integer call() throws Exception {
        started = true;
        boolean boardModified = true;

        while (!Thread.currentThread().isInterrupted() && boardModified) {
            try {
                Thread.sleep(2000 / speedFactor);
            } catch (InterruptedException e) {
                break;
            }
            boardModified = false;

            for (Cell cell: getAvailableNeigbouringCells()) {
                if (petriTableModel.occupyCell(color, cell.rowIndex, cell.columnIndex)) {
                    synchronized (this) {
                        squaresOccupied++;
                    }
                    boardCopy[cell.rowIndex][cell.columnIndex] = 1;
                    boardModified = true;
                }
            }
        }
        return squaresOccupied;
    }

    @Override
    public String toString() {
        return name;
    }
    public Color getColor() {
        return color;
    }
    public boolean isStarted() {
        return started;
    }
    public synchronized int getSquaresOccupied() {
        return squaresOccupied;
    }
    public List<Cell> getAvailableNeigbouringCells() {
        List<Cell> availableCells = new ArrayList<>();

        for (int row = 0; row < boardCopy.length; row++) {
            for (int col = 0; col < boardCopy[row].length; col++) {
                if (boardCopy[row][col] == 1) { // The current cell is occupied
                    // Check all adjacent neighbors
                    int[][] neighbors = {
                            {row - 1, col}, // Top
                            {row + 1, col}, // Bottom
                            {row, col - 1}, // Left
                            {row, col + 1}, // Right
                    };

                    for (int[] neighbor : neighbors) {
                        int rowIndex = neighbor[0];
                        int columnIndex = neighbor[1];

                        // Add as available cell if in bounds and not occupied yet
                        if (isInBounds(rowIndex, columnIndex) && boardCopy[rowIndex][columnIndex] == 0) {
                            availableCells.add(new Cell(rowIndex, columnIndex));
                        }
                    }
                }
            }
        }

        return availableCells;
    }

    public boolean isInBounds(int rowIndex, int columnIndex) {
        return rowIndex >= 0 && rowIndex < boardCopy.length && //Row index correct
                columnIndex >= 0 && columnIndex < boardCopy[0].length; //Column index correct

    }
}
