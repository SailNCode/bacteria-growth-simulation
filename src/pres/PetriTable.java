package pres;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.Enumeration;

public class PetriTable extends JTable {
    public PetriTable(Dimension tableDim, PetriTableModel petriTableModel) {
        super(petriTableModel);
        super.setPreferredSize(tableDim);

        int cellLength = tableDim.width / petriTableModel.getSideNCells();;
        super.setDefaultRenderer(Color.class, new PetriTableRenderer(cellLength));

        //Setting up cells' dimensions
        Enumeration<TableColumn> tableColumnEnumeration = getColumnModel().getColumns();
        while (tableColumnEnumeration.hasMoreElements()) {
            TableColumn currentColumn = tableColumnEnumeration.nextElement();
            currentColumn.setPreferredWidth(cellLength);
            currentColumn.setMinWidth(cellLength);
            currentColumn.setMaxWidth(cellLength);
        }
        super.setRowHeight(cellLength);

    }

}
