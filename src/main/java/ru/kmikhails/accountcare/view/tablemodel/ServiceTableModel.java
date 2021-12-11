package ru.kmikhails.accountcare.view.tablemodel;

import ru.kmikhails.accountcare.entity.Account;
import ru.kmikhails.accountcare.entity.InspectionOrganization;
import ru.kmikhails.accountcare.service.AccountService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ServiceTableModel extends CommonTableModel {

    private static final String[] TABLE_HEADERS = {
            "№ счета", "Дата", "Поверяющая организация", "Вид услуги", "Сумма", "Сумма с НДС",
            "№ Счет фактуры", "Дата Счета фактуры", "Дата сдачи в бух", "Примечания"
    };

    private final String[] columnNames = TABLE_HEADERS;

    private final Class[] columnClass = new Class[]{
            String.class, LocalDate.class, InspectionOrganization.class, String.class, String.class, String.class,
            String.class, LocalDate.class, LocalDate.class, String.class
    };

    public ServiceTableModel(AccountService accountService, List<Account> accounts) {
        super(accountService, accounts);
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
                return account.getInspectionOrganization().getInspectionOrganization();
            case 3:
                return account.getServiceType();
            case 4:
                return account.getAmount();
            case 5:
                return account.getAmountWithNDS();
            case 6:
                return account.getInvoiceNumber();
            case 7:
                return account.getInvoiceDate() == null ? "" : account.getInvoiceDate();
            case 8:
                return account.getDeliveryToAccountingDate() == null ? "" : account.getDeliveryToAccountingDate();
            case 9:
                return account.getNotes();
            default:
                return null;
        }
    }

    public void updateTable(int year) {
        accounts = accountService.findAllByTableType("прочие услуги", year);
        this.fireTableDataChanged();
    }

    public String getTableTypeName() {
        return "прочие услуги";
    }
}
