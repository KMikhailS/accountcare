package ru.kmikhails.accountcare;

import ru.kmikhails.accountcare.entity.Account;
import ru.kmikhails.accountcare.repository.CrudRepository;
import ru.kmikhails.accountcare.repository.DataSource;
import ru.kmikhails.accountcare.repository.impl.AccountRepository;
import ru.kmikhails.accountcare.repository.impl.MockAccountRepository;
import ru.kmikhails.accountcare.service.AccountService;
import ru.kmikhails.accountcare.service.impl.AccountServiceImpl;
import ru.kmikhails.accountcare.validator.Validator;
import ru.kmikhails.accountcare.validator.impl.AccountValidator;
import ru.kmikhails.accountcare.view.MainFrame;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AccountCareApplication {
	public static void main(String[] args) {
		try (InputStream inputStream = AccountCareApplication.class.getClassLoader().getResourceAsStream("config.properties")) {
//			Properties properties = new Properties();
//			properties.load(inputStream);

			System.getProperties().load(inputStream);

//			System.setProperties(properties);
		} catch (IOException e) {
			e.printStackTrace();
		}

		String propFilename = "application";
//		DataSource dataSource = new DataSource(propFilename);
		Validator<Account> validator = new AccountValidator();
		CrudRepository<Account> mockRepository = new MockAccountRepository();
//		CrudRepository<Account> accountRepository = new AccountRepository(dataSource);
		AccountService accountService = new AccountServiceImpl(mockRepository, validator);
		MainFrame mainFrame = new MainFrame(accountService);
		mainFrame.run();
	}
}
