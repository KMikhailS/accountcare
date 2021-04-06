package ru.kmikhails.accountcare.repository.impl;

import ru.kmikhails.accountcare.entity.Account;
import ru.kmikhails.accountcare.entity.Company;
import ru.kmikhails.accountcare.entity.InspectionOrganization;
import ru.kmikhails.accountcare.entity.TableType;
import ru.kmikhails.accountcare.exception.DataBaseException;
import ru.kmikhails.accountcare.repository.AbstractCrudRepository;
import ru.kmikhails.accountcare.repository.DataSource;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountRepository extends AbstractCrudRepository<Account> {
    private static final String ADD_QUERY = "INSERT INTO accounts (account_number, account_date, company_id, " +
            "service_type, amount, amount_with_nds, instruments, invoice_number, invoice_date, " +
            "delivery_to_accounting_date, inspection_organization_id, notes, account_file_path, table_type_id, " +
            "is_our, invoice_file_path, row_color) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String FIND_BY_ID_QUERY = "SELECT a.account_id, a.status, a.instruments, a.account_number," +
            "a.account_date, a.company_id, c.company, " +
            "a.service_type, a.amount, a.amount_with_nds, a.invoice_date, a.invoice_number, a.delivery_to_accounting_date, " +
            "a.inspection_organization_id, io.inspection_organization, a.account_file_path, a.table_type_id," +
            "t.table_type, a.notes, a.notes, a.is_our, a.invoice_file_path, a.row_color " +
            "FROM accounts a " +
            "JOIN companies c " +
            "ON a.company_id = c.company_id " +
            "JOIN inspection_organizations io " +
            "ON a.inspection_organization_id = io.inspection_organization_id " +
            "JOIN table_types t " +
            "ON a.table_type_id = t.table_type_id " +
            "WHERE a.account_id = ?";
    private static final String DELETE_QUERY = "UPDATE accounts SET status = 'DELETED' WHERE account_id = ?";
    private static final String FIND_BY_ACCOUNT_NUMBER_AND_COMPANY =
            "SELECT a.account_id, a.status, a.instruments, a.account_number, a.account_date, a.company_id, c.company, " +
                    "a.service_type, a.amount, a.amount_with_nds, a.invoice_date, a.invoice_number, a.delivery_to_accounting_date, " +
                    "a.inspection_organization_id, io.inspection_organization, a.account_file_path, a.table_type_id," +
                    "t.table_type, a.notes, a.is_our, a.invoice_file_path, a.row_color " +
                    "FROM accounts a " +
                    "JOIN companies c " +
                    "ON a.company_id = c.company_id " +
                    "JOIN inspection_organizations io " +
                    "ON a.inspection_organization_id = io.inspection_organization_id " +
                    "JOIN table_types t " +
                    "ON a.table_type_id = t.table_type_id " +
                    "WHERE a.account_number = ? AND a.company_id = ? AND status = 'NEW'";
    private static final String FIND_ALL_BY_TABLE_TYPE_QUERY =
            "SELECT a.account_id, a.status, a.instruments, a.account_number, a.account_date, a.company_id, c.company, " +
                    "a.service_type, a.amount, a.amount_with_nds, a.invoice_date, a.invoice_number, a.delivery_to_accounting_date, " +
                    "a.inspection_organization_id, io.inspection_organization, a.account_file_path, a.table_type_id," +
                    "t.table_type, a.notes, a.notes, a.is_our, a.invoice_file_path, a.row_color " +
                    "FROM accounts a " +
                    "JOIN companies c " +
                    "ON a.company_id = c.company_id " +
                    "JOIN inspection_organizations io " +
                    "ON a.inspection_organization_id = io.inspection_organization_id " +
                    "JOIN table_types t " +
                    "ON a.table_type_id = t.table_type_id " +
                    "WHERE t.table_type = ? AND status = 'NEW'";
    private static final String UPDATE = "UPDATE accounts SET account_number = ?, account_date = ?, company_id = ?," +
            "service_type = ?, amount = ?, amount_with_nds = ?, instruments = ?, invoice_number = ?, invoice_date = ?," +
            "delivery_to_accounting_date = ?, inspection_organization_id = ?, notes = ?, account_file_path = ?," +
            "table_type_id = ?, is_our = ?, invoice_file_path = ?, row_color = ? WHERE account_id = ?";
    private static final String FIND_BY_NAME_QUERY = "";

    public AccountRepository(DataSource dataSource) {
        super(dataSource, ADD_QUERY, FIND_BY_ID_QUERY, FIND_BY_NAME_QUERY);
    }

    @Override
    public Optional<Account> save(Account account) {
        try (final Connection connection = dataSource.getConnection();
             final PreparedStatement addStatement = connection.prepareStatement(ADD_QUERY);
             final PreparedStatement findStatement = connection.prepareStatement(FIND_BY_ACCOUNT_NUMBER_AND_COMPANY)) {
            try {
                connection.setAutoCommit(false);

                insert(addStatement, account);
                addStatement.executeUpdate();

                findStatement.setString(1, account.getAccountNumber());
                findStatement.setLong(2, account.getCompany().getId());

                try (final ResultSet resultSet = findStatement.executeQuery()) {
                    if (resultSet.next()) {
                        Optional<Account> resultAccount = Optional.of(mapResultSetToEntity(resultSet));
                        connection.commit();

                        return resultAccount;
                    }
                }
            } catch (SQLException ex) {
                connection.rollback();
                throw new DataBaseException(ex);
            }
        } catch (SQLException e) {
//            LOG.error(e);
            throw new DataBaseException(e);
        }
        return Optional.empty();
    }

    @Override
    public void deleteById(Long id) {
        deleteByParam(id, DELETE_QUERY, LONG_PARAM_SETTER);
    }

    @Override
    public List<Account> findAllByTableType(String tableType) {
        return findAllByStringParam(tableType, FIND_ALL_BY_TABLE_TYPE_QUERY, STRING_PARAM_SETTER);
    }

    @Override
    public List<Account> findAll() {
        return new ArrayList<>();
    }

    @Override
    public void update(Account account) {
        try (final Connection connection = dataSource.getConnection();
             final PreparedStatement statement = connection.prepareStatement(UPDATE)) {

            insert(statement, account);
            statement.setLong(18, account.getId());

            statement.executeUpdate();

        } catch (SQLException ex) {
            throw new DataBaseException(ex);
        }
    }

    public Optional<Account> findByAccountNumberAndCompany(String accountNumber, long companyId) {
        try (final Connection connection = dataSource.getConnection();
             final PreparedStatement statement = connection.prepareStatement(FIND_BY_ACCOUNT_NUMBER_AND_COMPANY)) {

            statement.setString(1, accountNumber);
            statement.setLong(2, companyId);

            try (final ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToEntity(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
//			LOG.error(e);
            throw new DataBaseException(e);
        }
        return Optional.empty();
    }

    protected void insert(PreparedStatement statement, Account account) throws SQLException {
        statement.setString(1, account.getAccountNumber());
        if (account.getAccountDate() != null) {
            statement.setDate(2, Date.valueOf(account.getAccountDate()));
        } else {
            statement.setDate(2, null);
        }
        statement.setLong(3, account.getCompany().getId());
        statement.setString(4, account.getServiceType());
        statement.setString(5, account.getAmount());
        statement.setString(6, account.getAmountWithNDS());
        statement.setString(7, account.getInstruments());
        statement.setString(8, account.getInvoiceNumber());
        if (account.getInvoiceDate() != null) {
            statement.setDate(9, Date.valueOf(account.getInvoiceDate()));
        } else {
            statement.setDate(9, null);
        }
        if (account.getDeliveryToAccountingDate() != null) {
            statement.setDate(10, Date.valueOf(account.getDeliveryToAccountingDate()));
        } else {
            statement.setDate(10, null);
        }
        statement.setLong(11, account.getInspectionOrganization().getId());
        statement.setString(12, account.getNotes());
        statement.setString(13, account.getAccountFile());
        statement.setLong(14, account.getTableType().getId());
        statement.setBoolean(15, account.getOur());
        statement.setString(16, account.getInvoiceFile());
        if (account.getRowColor() != null) {
            statement.setInt(17, account.getRowColor());
        } else {
            statement.setNull(17, java.sql.Types.INTEGER);
        }
    }

    protected Account mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return Account.builder()
                .withId(resultSet.getLong("account_id"))
                .withStatus(resultSet.getString("status"))
                .withAccountNumber(resultSet.getString("account_number"))
                .withAccountDate(LocalDate.parse(resultSet.getString("account_date")))
                .withInstruments(resultSet.getString("instruments"))
                .withCompany(Company.builder()
                        .withId(resultSet.getLong("company_id"))
                        .withCompany(resultSet.getString("company"))
                        .build())
                .withServiceType(resultSet.getString("service_type"))
                .withAmount(resultSet.getString("amount"))
                .withAmountWithDNS(resultSet.getString("amount_with_nds"))
                .withInvoiceNumber(resultSet.getString("invoice_number"))
                .withInvoiceDate(resultSet.getString("invoice_date") != null
                        ? LocalDate.parse(resultSet.getString("invoice_date")) : null)
                .withDeliveryToAccountingDate(resultSet.getString("delivery_to_accounting_date") != null
                        ? LocalDate.parse(resultSet.getString("delivery_to_accounting_date")) : null)
                .withInspectionOrganization(InspectionOrganization.builder()
                        .withId(resultSet.getLong("inspection_organization_id"))
                        .withInspectionOrganization(resultSet.getString("inspection_organization"))
                        .build())
                .withNotes(resultSet.getString("notes"))
                .withAccountFile(resultSet.getString("account_file_path"))
                .withTableType(TableType.builder()
                        .withId(resultSet.getLong("table_type_id"))
                        .withTableType(resultSet.getString("table_type"))
                        .build())
                .withIsOur(resultSet.getBoolean("is_our"))
                .withInvoiceFile(resultSet.getString("invoice_file_path"))
                .withRowColor(resultSet.getInt("row_color"))
                .build();
    }

}
