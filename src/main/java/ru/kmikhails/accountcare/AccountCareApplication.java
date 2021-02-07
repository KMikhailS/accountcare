package ru.kmikhails.accountcare;

import ru.kmikhails.accountcare.entity.Account;
import ru.kmikhails.accountcare.repository.CrudRepository;
import ru.kmikhails.accountcare.repository.DataSource;
import ru.kmikhails.accountcare.repository.impl.AccountRepository;
import ru.kmikhails.accountcare.service.AccountService;
import ru.kmikhails.accountcare.service.impl.AccountServiceImpl;
import ru.kmikhails.accountcare.validator.Validator;
import ru.kmikhails.accountcare.validator.impl.AccountValidator;
import ru.kmikhails.accountcare.view.MainFrame;

public class AccountCareApplication {
	public static void main(String[] args) {
		String propFilename = "application";
		DataSource dataSource = new DataSource(propFilename);
		Validator<Account> validator = new AccountValidator();
		CrudRepository<Account> accountRepository = new AccountRepository(dataSource);
		AccountService accountService = new AccountServiceImpl(accountRepository, validator);
		MainFrame mainFrame = new MainFrame(accountService);
		mainFrame.run();
	}
}
