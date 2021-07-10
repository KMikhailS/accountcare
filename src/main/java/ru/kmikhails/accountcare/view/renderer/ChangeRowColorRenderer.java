package ru.kmikhails.accountcare.view.renderer;

import ru.kmikhails.accountcare.view.tablemodel.CommonTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ChangeRowColorRenderer extends DefaultTableCellRenderer {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MMM-yyyy");

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {
        if( value instanceof LocalDate) {
            LocalDate date = (LocalDate) value;
            value = FORMATTER.format(date);
        }
        CommonTableModel model = (CommonTableModel) table.getModel();
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        int trueRow = table.getRowSorter().convertRowIndexToModel(row);
        c.setForeground(model.getRowColor(trueRow));

        return c;
    }
}
