package pres;

public class Cell {
    public final int rowIndex;
    public final int columnIndex;

    public Cell(int rowIndex, int columnIndex) {
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
    }

    @Override
    public String toString() {
        return String.format("Cell(row = %d, column = %d)", rowIndex, columnIndex);
    }
}
