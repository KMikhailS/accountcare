package ru.kmikhails.accountcare.service;

import ru.kmikhails.accountcare.entity.Account;
import ru.kmikhails.accountcare.entity.AccountStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AccountService extends Service<Account> {

	void changeStatus(Long id, AccountStatus status);

	List<Account> findAllByTableType(String tableType);

	Optional<Account> findByAccountNumberAndDate(String accountNumber, LocalDate date);
}
