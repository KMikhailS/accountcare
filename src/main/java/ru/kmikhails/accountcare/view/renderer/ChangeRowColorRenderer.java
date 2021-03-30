package ru.kmikhails.accountcare.view.renderer;

import ru.kmikhails.accountcare.entity.Account;
import ru.kmikhails.accountcare.util.RowNumberHolder;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class ChangeRowColorRenderer extends DefaultTableCellRenderer {

    private final List<Account> accounts;
    private final RowNumberHolder rowNumberHolder;

    public ChangeRowColorRenderer(List<Account> accounts, RowNumberHolder rowNumberHolder) {
        this.accounts = accounts;
        this.rowNumberHolder = rowNumberHolder;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected,
                hasFocus, row, column);
        List<Account> collect = accounts.stream().filter(Account::getOur).collect(Collectors.toList());

//        Integer myRow = null;
        String accountNumber = null;

        for (Account account : collect) {
            if (value instanceof String) {
                accountNumber = (String) value;
            }
//            int i1 = table.getRowSorter().convertRowIndexToView(i);
//            String valueAt = (String) table.getModel().getValueAt(i, 0);
                if (account.getAccountNumber().equals(accountNumber)) {
                    rowNumberHolder.setRowNumber(row);
                }
                if (rowNumberHolder.getRowNumber() != null && row == rowNumberHolder.getRowNumber()) {
                    c.setForeground(Color.BLUE);
                }
                if (column == 9) {
                    rowNumberHolder.resetRowNumber();
                }

//            }
        }
//
//
//        String versionVal = table.getValueAt(row, 1).toString();
//
//        if(versionVal.contains("FAKE")) {
//            //set to red bold font
//            c.setForeground(Color.RED);
//            c.setFont(new Font("Dialog", Font.BOLD, 12));
//        } else {
//            //stay at default
//            c.setForeground(Color.BLACK);
//            c.setFont(new Font("Dialog", Font.PLAIN, 12));
//        }
        return c;
    }
}
