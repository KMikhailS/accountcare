package ru.kmikhails.accountcare.view;

import ru.kmikhails.accountcare.entity.Account;
import ru.kmikhails.accountcare.entity.AccountStatus;
import ru.kmikhails.accountcare.repository.CrudRepository;
import ru.kmikhails.accountcare.repository.impl.MockAccountRepository;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.stream.Collectors;

public class AccountTableModel extends AbstractTableModel {

	private static final String[] TABLE_HEADERS = {
			"№ счета", "Дата", "Предприятие", "Сумма с НДС", "№ Счет фактуры", "Дата Счета фактуры",
			"Дата сдачи в бух"
	};

	private final List<Account> accounts;
	private final String[] columnNames = TABLE_HEADERS;

	private final Class[] columnClass = new Class[] {
		String.class, String.class, String.class, Float.class, String.class, String.class, String.class
	};

	public AccountTableModel(CrudRepository<Account> repository) {
		this.accounts = repository.findAll();
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
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
				.filter(acc -> acc.getStatus().equals(AccountStatus.NEW))
				.count();
//		return accounts.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Account account = accounts.stream()
				.filter(acc -> acc.getStatus().equals(AccountStatus.NEW))
				.collect(Collectors.toList())
				.get(rowIndex);
//		Account account = accounts.get(rowIndex);

		switch (columnIndex) {
			case 0:
				return account.getAccountNumber();
			case 1:
				return account.getAccountDate().toString();
			case 2:
				return account.getCompany();
			case 3:
				return account.getAmount();
			case 4:
				return account.getInvoiceNumber();
			case 5:
				return account.getInvoiceDate();
			case 6:
				return account.getDeliveryToAccountingDate();
			default:
				return null;
		}
	}

	public void deleteRow(String accountNumber, String company) {
		accounts.stream()
				.filter(acc -> acc.getAccountNumber().equals(accountNumber))
				.filter(acc -> acc.getCompany().equals(company))
				.findFirst()
				.ifPresent(acc -> acc.setStatus(AccountStatus.DELETED));
		this.fireTableDataChanged();
	}
}
