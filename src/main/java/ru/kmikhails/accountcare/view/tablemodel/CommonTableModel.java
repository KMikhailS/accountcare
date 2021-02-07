package ru.kmikhails.accountcare.view.tablemodel;

import ru.kmikhails.accountcare.entity.Account;

import javax.swing.table.AbstractTableModel;
import java.time.LocalDate;

public abstract class CommonTableModel extends AbstractTableModel {

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public abstract String getColumnName(int column);

    @Override
    public abstract Class<?> getColumnClass(int columnIndex);

    @Override
    public abstract int getRowCount();

    @Override
    public abstract int getColumnCount();

    @Override
    public abstract Object getValueAt(int rowIndex, int columnIndex);

    public abstract void deleteRow(String accountNumber, LocalDate date);

    public abstract void addRow(Account account);
}
