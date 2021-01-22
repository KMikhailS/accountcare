package ru.kmikhails.accountcare.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kmikhails.accountcare.entity.Account;
import ru.kmikhails.accountcare.entity.AccountStatus;
import ru.kmikhails.accountcare.exception.AccountException;
import ru.kmikhails.accountcare.repository.impl.AccountRepository;
import ru.kmikhails.accountcare.service.AccountService;
import ru.kmikhails.accountcare.validator.impl.AccountValidator;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

	private final AccountRepository accountRepository;
	private final AccountValidator accountValidator;

	@Autowired
	public AccountServiceImpl(AccountRepository accountRepository, AccountValidator accountValidator) {
		this.accountRepository = accountRepository;
		this.accountValidator = accountValidator;
	}

	@Transactional
	@Override
	public Account addNewAccount(Account account) {
		accountValidator.validate(account);
		accountRepository.save(account);

		return accountRepository.findByAccountNumberAndCompany(account.getAccountNumber(), account.getCompany())
				.orElseThrow(() -> new AccountException(
						String.format("После сохранения не могу найти счёт с номером [%s] и организацией [%s]",
						account.getAccountNumber(), account.getCompany())));
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
}
