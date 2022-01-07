package ru.kmikhails.accountcare.view.tablemodel;

import ru.kmikhails.accountcare.entity.Account;
import ru.kmikhails.accountcare.service.AccountService;

import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public abstract class CommonTableModel extends AbstractTableModel {

    public AccountService accountService;
    public List<Account> accounts;

    public CommonTableModel(AccountService accountService, List<Account> accounts) {
        this.accountService = accountService;
        this.accounts = accounts;
    }

    public void updateYear(String year) {
        updateTable(year);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public abstract String getColumnName(int column);

    @Override
    public abstract Class<?> getColumnClass(int columnIndex);

    @Override
    public int getRowCount() {
        return (int) accounts.stream()
                .filter(acc -> acc.getStatus().equals("NEW"))
                .count();
    }

    @Override
    public abstract int getColumnCount();

    @Override
    public abstract Object getValueAt(int rowIndex, int columnIndex);

    public abstract void updateTable(String year);

    public abstract String getTableTypeName();

    public Color getRowColor(int row) {
        Account ourAccount = accounts.get(row);
        if (ourAccount.getOur()) {
            return Color.BLUE;
        }
        return Color.BLACK;
    }

    public void deleteRow(String accountNumber, LocalDate date, String year) {
        accounts.stream()
                .filter(acc -> acc.getAccountNumber().equals(accountNumber))
                .filter(acc -> acc.getAccountDate().equals(date))
                .filter(acc -> acc.getStatus().equals("NEW"))
                .findFirst()
                .ifPresent(account -> accountService.deleteById(account.getId()));
        updateTable(year);
    }

    public void addRow(Account account, String year) {
        accountService.save(account);
        updateTable(year);
    }

    public Account findAccount(String accountNumber, LocalDate date) {
        return accounts.stream()
                .filter(acc -> acc.getAccountNumber().equals(accountNumber))
                .filter(acc -> acc.getAccountDate().equals(date))
                .filter(acc -> acc.getStatus().equals("NEW"))
                .findFirst()
                .orElse(null);
    }

    public void update(Account account, String year) {
        accountService.update(account);
        updateTable(year);
    }
}
