package ru.kmikhails.accountcare.service;

import ru.kmikhails.accountcare.entity.Account;
import ru.kmikhails.accountcare.entity.AccountStatus;

import java.util.List;

public interface AccountService {

	Account addNewAccount(Account account);

	void deleteById(Long id);

	Account findById(Long id);

	List<Account> findAll();

	void changeStatus(Long id, AccountStatus status);

	void update(Long id);
}
