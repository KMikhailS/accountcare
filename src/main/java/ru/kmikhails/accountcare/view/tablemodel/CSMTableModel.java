package ru.kmikhails.accountcare.view.tablemodel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kmikhails.accountcare.entity.Account;
import ru.kmikhails.accountcare.entity.AccountStatus;
import ru.kmikhails.accountcare.repository.CrudRepository;
import ru.kmikhails.accountcare.repository.impl.MockAccountRepository;
import ru.kmikhails.accountcare.service.AccountService;

import javax.swing.table.AbstractTableModel;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CSMTableModel extends CommonTableModel {

	private static final String[] TABLE_HEADERS = {
			"№ счета", "Дата", "Предприятие", "Наименование СИ", "Сумма", "Сумма с НДС", "№ Счет фактуры", "Дата Счета фактуры",
			"Дата сдачи в бух", "Примечания"
	};

	private AccountService accountService;
	private List<Account> accounts;
	private final String[] columnNames = TABLE_HEADERS;

	private final Class[] columnClass = new Class[] {
		String.class, LocalDate.class, String.class, String.class, String.class, String.class, String.class,
			LocalDate.class, LocalDate.class, String.class
	};

	public CSMTableModel(AccountService accountService) {
		this.accountService = accountService;
		this.accounts = accountService.findAll();
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return columnClass[columnIndex];
	}

	@Override
	public int getRowCount() {
		return (int) accounts.stream()
				.filter(acc -> acc.getStatus().equals("NEW"))
				.count();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Account account = accounts.stream()
				.filter(acc -> acc.getStatus().equals("NEW"))
				.collect(Collectors.toList())
				.get(rowIndex);

		switch (columnIndex) {
			case 0:
				return account.getAccountNumber();
			case 1:
				return account.getAccountDate();
			case 2:
				return account.getCompany();
			case 3:
				return account.getInstruments();
			case 4:
				return account.getAmount();
			case 5:
				return account.getAmountWithNDS();
			case 6:
				return account.getInvoiceNumber();
			case 7:
				return account.getInvoiceDate() == null ? "": account.getInvoiceDate();
			case 8:
				return account.getDeliveryToAccountingDate() == null ? "": account.getDeliveryToAccountingDate();
			case 9:
				return account.getNotes();
			default:
				return null;
		}
	}

	public void refreshTable() {
		this.fireTableDataChanged();
	}

	public void deleteRow(String accountNumber, LocalDate date) {
		accounts.stream()
				.filter(acc -> acc.getAccountNumber().equals(accountNumber))
				.filter(acc -> acc.getAccountDate().equals(date))
				.filter(acc -> acc.getStatus().equals("NEW"))
				.findFirst()
				.ifPresent(account -> accountService.deleteById(account.getId()));
		accounts = accountService.findAll();
		this.fireTableDataChanged();
	}

	public void addRow(Account account) {
		accountService.addNewAccount(account);
		accounts = accountService.findAll();
		this.fireTableDataChanged();
	}
}
