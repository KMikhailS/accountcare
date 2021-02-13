package ru.kmikhails.accountcare.repository.impl;

import ru.kmikhails.accountcare.entity.Account;
import ru.kmikhails.accountcare.exception.AccountException;
import ru.kmikhails.accountcare.repository.CrudRepository;
import ru.kmikhails.accountcare.repository.DataSource;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

public class AccountRepository implements CrudRepository<Account> {
	private static final String ADD_ACCOUNT = "INSERT INTO accounts (account_number, account_date, company, " +
			"service_type, amount, amount_with_nds, instruments, invoice_number, invoice_date, " +
			"delivery_to_accounting_date, inspection_organization, notes, account_file_path) VALUES (?, ?, ?, ?, ?, " +
			"?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String DELETE_ACCOUNT = "DELETE FROM accounts WHERE account_id = ?";
	private static final String FIND_ACCOUNT_BY_ID = "SELECT * FROM accounts WHERE account_id = ?";
	private static final String FIND_BY_ACCOUNT_NUMBER_AND_COMPANY = "SELECT * FROM accounts " +
																		"WHERE account_number = '%s' AND company = '%s'";
	private static final String FIND_ALL_QUERY = "SELECT * FROM accounts WHERE status = ?";

	private final DataSource dataSource;
	public AccountRepository(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public void save(Account account) {
		try(final PreparedStatement statement = dataSource.getConnection().prepareStatement(ADD_ACCOUNT)) {

			insert(statement, account);
			statement.executeUpdate();
		} catch (SQLException e) {
//			LOG.error(e);
//			throw new DataBaseException(e);
			e.printStackTrace();
		}
	}

	@Override
	public void deleteById(Long id) {
		deleteByParam(id, DELETE_ACCOUNT, LONG_PARAM_SETTER);
	}

	@Override
	public Optional<Account> findById(Long id) {
		return findByParam(id, FIND_ACCOUNT_BY_ID, LONG_PARAM_SETTER);
	}

	@Override
	public List<Account> findAll() {
//		List<Account> accountList = jdbcTemplate.query(FIND_ALL_QUERY, rowMapper());
//		System.out.println(accountList);
		return findAllByStringParam("NEW", FIND_ALL_QUERY, STRING_PARAM_SETTER);

//		return accountList;
	}

	@Override
	public void update(Long id) {

	}

	@Override
	public Optional<Account> findByAccountNumberAndCompany(String accountNumber, String company) {
//		try {
//			return Optional.ofNullable(jdbcTemplate.queryForObject(
//					String.format(FIND_BY_ACCOUNT_NUMBER_AND_COMPANY, accountNumber, company), rowMapper()));
//		} catch (EmptyResultDataAccessException e) {
//			return Optional.empty();
//		}
		return Optional.empty();
	}

	private void insert(PreparedStatement statement, Account account) throws SQLException {
		statement.setString(1, account.getAccountNumber());
		if (account.getAccountDate() != null) {
			statement.setDate(2, Date.valueOf(account.getAccountDate()));
		} else {
			statement.setDate(2, null);
		}
		statement.setString(3, account.getCompany());
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
		statement.setString(11, account.getInspectionOrganization());
		statement.setString(12, account.getNotes());
		statement.setString(13, account.getAccountFile());
	}

	private Account mapResultSetToEntity(ResultSet resultSet) throws SQLException {
		return   Account.builder()
				.withId(resultSet.getLong("account_id"))
				.withStatus(resultSet.getString("status"))
				.withAccountNumber(resultSet.getString("account_number"))
				.withAccountDate(LocalDate.parse(resultSet.getString("account_date")))
				.withInstruments(resultSet.getString("instruments"))
				.withCompany(resultSet.getString("company"))
				.withServiceType(resultSet.getString("service_type"))
				.withAmount(resultSet.getString("amount"))
				.withAmountWithDNS(resultSet.getString("amount_with_nds"))
				.withInvoiceNumber(resultSet.getString("invoice_number"))
//				.withInvoiceDate(LocalDate.parse(resultSet.getString("invoice_date")))
//				.withDeliveryToAccountingDate(LocalDate.parse(resultSet.getString("delivery_to_accounting_date")))
				.withInspectionOrganization(resultSet.getString("inspection_organization"))
				.withNotes(resultSet.getString("notes"))
				.withAccountFile(resultSet.getString("account_file_path"))
				.build();
	}

	protected static final BiConsumer<PreparedStatement, Long> LONG_PARAM_SETTER = (statement, value) -> {
		try {
			statement.setLong(1, value);
		} catch (SQLException e) {
			e.printStackTrace();
//			LOG.error(e);
		}
	};

	protected static final BiConsumer<PreparedStatement, String> STRING_PARAM_SETTER = (statement, string) -> {
		try {
			statement.setString(1, string);
		} catch (SQLException e) {
			e.printStackTrace();
//			LOG.error(e);
		}
	};

	protected <P> void deleteByParam(P param, String deleteByParam, BiConsumer<PreparedStatement, P> designatedParamSetter) {
		try (final PreparedStatement statement = dataSource.getConnection().prepareStatement(deleteByParam)) {

			designatedParamSetter.accept(statement, param);
			if (statement.executeUpdate() == 0) {
				throw new IllegalArgumentException("Entity with id " + param + " does not exist");
			}
		} catch (SQLException e) {
			e.printStackTrace();
//			LOG.error(e);
			throw new AccountException(e);
		}
	}

	protected <P> Optional<Account> findByParam(P param, String findByParam, BiConsumer<PreparedStatement, P> designatedParamSetter) {
		try (final PreparedStatement statement = dataSource.getConnection().prepareStatement(findByParam)) {

			designatedParamSetter.accept(statement, param);

			try (final ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					return Optional.of(mapResultSetToEntity(resultSet));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
//			LOG.error(e);
			throw new AccountException(e);
		}

		return Optional.empty();
	}

	protected <P> List<Account> findAllByStringParam(P param, String findByParam, BiConsumer<PreparedStatement, P> designatedParamSetter) {
		try (final PreparedStatement statement = dataSource.getConnection().prepareStatement(findByParam)) {

			designatedParamSetter.accept(statement, param);

			try (final ResultSet resultSet = statement.executeQuery()) {
				List<Account> entities = new ArrayList<>();
				while (resultSet.next()) {
					entities.add(mapResultSetToEntity(resultSet));
				}
				return entities;
			}
		} catch (SQLException e) {
			e.printStackTrace();
//			LOG.error(e);
			throw new AccountException(e);
		}
	}
}
