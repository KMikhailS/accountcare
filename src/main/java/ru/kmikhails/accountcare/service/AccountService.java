package ru.kmikhails.accountcare.service;

import ru.kmikhails.accountcare.entity.Account;
import ru.kmikhails.accountcare.entity.AccountStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AccountService {

	void addNewAccount(Account account);

	void deleteById(Long id);

	Account findById(Long id);

	List<Account> findAll();

	void changeStatus(Long id, AccountStatus status);

	void update(Long id);

	List<Account> findAllTest();

	Optional<Account> findByAccountNumberAndDate(String accountNumber, LocalDate date);
}
