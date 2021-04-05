package ru.kmikhails.accountcare.view.renderer;

import ru.kmikhails.accountcare.entity.Account;
import ru.kmikhails.accountcare.util.RowNumberHolder;
import ru.kmikhails.accountcare.view.tablemodel.CommonTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public class ChangeRowColorRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {
        CommonTableModel model = (CommonTableModel) table.getModel();
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        int trueRow = table.getRowSorter().convertRowIndexToModel(row);
        c.setForeground(model.getRowColor(trueRow));

        return c;
    }
}
