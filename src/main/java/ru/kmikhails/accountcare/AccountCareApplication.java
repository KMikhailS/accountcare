package ru.kmikhails.accountcare;

import ru.kmikhails.accountcare.entity.Account;
import ru.kmikhails.accountcare.entity.Company;
import ru.kmikhails.accountcare.entity.InspectionOrganization;
import ru.kmikhails.accountcare.entity.TableType;
import ru.kmikhails.accountcare.repository.CrudRepository;
import ru.kmikhails.accountcare.repository.DataSource;
import ru.kmikhails.accountcare.repository.impl.*;
import ru.kmikhails.accountcare.service.AccountService;
import ru.kmikhails.accountcare.service.impl.AccountServiceImpl;
import ru.kmikhails.accountcare.validator.Validator;
import ru.kmikhails.accountcare.validator.impl.AccountValidator;
import ru.kmikhails.accountcare.view.MainFrame;

import java.io.IOException;
import java.io.InputStream;

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
		DataSource dataSource = new DataSource(propFilename);
		Validator<Account> validator = new AccountValidator();
//		CrudRepository<Account> mockRepository = new MockAccountRepository();
		CrudRepository<Account> accountRepository = new AccountRepository(dataSource);
		CrudRepository<Company> companyRepository = new CompanyRepository(dataSource);
		CrudRepository<InspectionOrganization> inspectionOrganizationRepository = new InspectionOrganizationRepository(dataSource);
		CrudRepository<TableType> tableTypesRepository = new TableTypeRepository(dataSource);
		AccountService accountService = new AccountServiceImpl(accountRepository, validator);
		MainFrame mainFrame = new MainFrame(accountService);
		mainFrame.run();
	}
}
