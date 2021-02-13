package ru.kmikhails.accountcare.service.impl;

import ru.kmikhails.accountcare.entity.Account;
import ru.kmikhails.accountcare.entity.AccountStatus;
import ru.kmikhails.accountcare.exception.AccountException;
import ru.kmikhails.accountcare.repository.CrudRepository;
import ru.kmikhails.accountcare.service.AccountService;
import ru.kmikhails.accountcare.validator.Validator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountServiceImpl implements AccountService {

	private final CrudRepository<Account> accountRepository;
	private final Validator<Account> accountValidator;

	public AccountServiceImpl(CrudRepository<Account> accountRepository, Validator<Account> accountValidator) {
		this.accountRepository = accountRepository;
		this.accountValidator = accountValidator;
	}

	@Override
	public void addNewAccount(Account account) {
		accountValidator.validate(account);
		accountRepository.save(account);

//		return accountRepository.findByAccountNumberAndCompany(account.getAccountNumber(), account.getCompany())
//				.orElseThrow(() -> new AccountException(
//						String.format("После сохранения не могу найти счёт с номером [%s] и организацией [%s]",
//						account.getAccountNumber(), account.getCompany())));
	}

	@Override
	public void deleteById(Long id) {
		accountRepository.deleteById(id);
	}

	@Override
	public Account findById(Long id) {
		return accountRepository.findById(id).orElseThrow(
				() -> new AccountException(String.format("Не могу найти счёт с идентификатором [%d]", id)));
	}

	@Override
	public List<Account> findAll() {
		return accountRepository.findAll();
	}

	@Override
	public void changeStatus(Long id, AccountStatus status) {

	}

	@Override
	public void update(Long id) {

	}

	@Override
	public List<Account> findAllTest() {
		return new ArrayList<Account>();
	}

	@Override
	public Optional<Account> findByAccountNumberAndDate(String accountNumber, LocalDate date) {
		return Optional.empty();
	}
}
