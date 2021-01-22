package ru.kmikhails.accountcare.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.kmikhails.accountcare.entity.Account;
import ru.kmikhails.accountcare.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class AccountRepository implements CrudRepository<Account> {
	private static final String ADD_ACCOUNT = "INSERT INTO accounts (account_number, account_date) VALUES (?, ?)";
	private static final String DELETE_ACCOUNT = "DELETE FROM accounts WHERE id = ?";
	private static final String FIND_ACCOUNT_BY_ID = "SELECT * FROM accounts WHERE id = ?";
	private static final String FIND_BY_ACCOUNT_NUMBER_AND_COMPANY = "SELECT * FROM accounts " +
																		"WHERE account_number = %s AND company = %s";

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public AccountRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public int save(Account account) {
		return jdbcTemplate.update(ADD_ACCOUNT, insert(account));
	}

	@Override
	public void deleteById(Long id) {
		jdbcTemplate.update(DELETE_ACCOUNT, id);
	}

	@Override
	public Optional<Account> findById(Long id) {
		try {
			return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_ACCOUNT_BY_ID, rowMapper(), id));
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	@Override
	public List<Account> findAll() {
		return null;
	}

	@Override
	public void update(Long id) {

	}

	public Optional<Account> findByAccountNumberAndCompany(String accountNumber, String company) {
		try {
			return Optional.ofNullable(jdbcTemplate.queryForObject(
					String.format(FIND_BY_ACCOUNT_NUMBER_AND_COMPANY, accountNumber, company), rowMapper()));
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	private static Object[] insert(Account account) {
		return new Object[] {account.getAccountNumber(), account.getAccountDate()};
	}

	private RowMapper<Account> rowMapper() {
		return (resultSet, rowName) -> Account.builder()
				.withAccountNumber(resultSet.getString("account_number"))
				.withAccountDate(LocalDate.parse(resultSet.getString("account_date")))
				.build();
	}
}
