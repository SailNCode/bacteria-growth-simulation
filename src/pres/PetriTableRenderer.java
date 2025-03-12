package pres;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class PetriTableRenderer extends JLabel implements TableCellRenderer {
    public PetriTableRenderer(int cellLength) {
        super.setOpaque(true);
        super.setPreferredSize(new Dimension(cellLength, cellLength));
    }
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
           boolean hasFocus, int row, int column) {
        super.setBackground((Color) value);
        return this;
    }
}
