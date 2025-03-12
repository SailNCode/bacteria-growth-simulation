package pres;

import internationalization.Bundle;

import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.List;

public class PetriTableModel extends AbstractTableModel {
    private final Color[][] cells;
    /**
     * List which contains array of not yet checked positions inside cells array.
     * If the position inside cells is found to be occupied,
     * then it is removed from this list not to check it twice.
     **/
    private final List<Integer[]> uncheckedPositions;
    private final int sideNCells;
    private final Color initialColor;
    public PetriTableModel(int sideNCells, Color boardColor) {
        this.sideNCells = sideNCells;
        this.initialColor = boardColor;
        cells = new Color[sideNCells][sideNCells];
        for (int row = 0; row < sideNCells; row++) {
            for (int column = 0; column < sideNCells; column++) {
                cells[row][column] = boardColor;
            }
        }
        uncheckedPositions = Stream.iterate(0, n -> n + 1)
                .limit(cells.length)
                .flatMap(
                        rowIndex -> Arrays.stream(Stream.iterate(0, n -> n + 1)
                                .limit(cells[0].length)
                                .map(columnIndex -> new Integer[]{rowIndex, columnIndex}).toArray(Integer[][]::new)
                        )).collect(Collectors.toList());
    }
    @Override
    public int getRowCount() {
        return cells.length;
    }

    @Override
    public int getColumnCount() {
        return cells[0].length;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return Color.class;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return cells[rowIndex][columnIndex];
    }

    public int getSideNCells() {
        return sideNCells;
    }

    public Cell occupyRandomCell(Color color) {
        Random random = new Random();

        int rowIndex,
            columnIndex;
        Integer[] selectedCell;

        //do-while is executed while empty cell is not found and there are still cells to check.
        synchronized (this) {
            do {
                selectedCell = uncheckedPositions.remove(random.nextInt(uncheckedPositions.size()));
                rowIndex = selectedCell[0];
                columnIndex = selectedCell[1];
            }
            while (!isCellEmpty(rowIndex, columnIndex) && !uncheckedPositions.isEmpty());
        }

        if (occupyCell(color, rowIndex, columnIndex)) {
            return new Cell(rowIndex, columnIndex);
        }
        throw new IllegalStateException(Bundle.get().getString("noInitialCellAssigned"));

    }

    public synchronized boolean occupyCell(Color color, int rowIndex, int columnIndex) {
        boolean modified = false;
        if (isCellEmpty(rowIndex, columnIndex)) {
            //Occupying the board with given color
            cells[rowIndex][columnIndex] = color;
            fireTableCellUpdated(rowIndex, columnIndex);
            modified = true;
        }
        return modified;
    }

    private boolean isCellEmpty(int rowIndex, int columnIndex) {
        return cells[rowIndex][columnIndex].equals(initialColor);
    }
}
