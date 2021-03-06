package ru.kmikhails.accountcare;

import ru.kmikhails.accountcare.entity.Account;
import ru.kmikhails.accountcare.entity.Company;
import ru.kmikhails.accountcare.entity.InspectionOrganization;
import ru.kmikhails.accountcare.entity.TableType;
import ru.kmikhails.accountcare.repository.CrudRepository;
import ru.kmikhails.accountcare.repository.DataSource;
import ru.kmikhails.accountcare.repository.impl.AccountRepository;
import ru.kmikhails.accountcare.repository.impl.CompanyRepository;
import ru.kmikhails.accountcare.repository.impl.InspectionOrganizationRepository;
import ru.kmikhails.accountcare.repository.impl.TableTypeRepository;
import ru.kmikhails.accountcare.service.AccountService;
import ru.kmikhails.accountcare.service.impl.AccountServiceImpl;
import ru.kmikhails.accountcare.service.impl.CompanyService;
import ru.kmikhails.accountcare.service.impl.InspectionOrganizationService;
import ru.kmikhails.accountcare.service.impl.TableTypeService;
import ru.kmikhails.accountcare.validator.Validator;
import ru.kmikhails.accountcare.validator.impl.AccountValidator;
import ru.kmikhails.accountcare.view.MainFrame;

import java.util.ResourceBundle;

public class AccountCareApplication {
	public static void main(String[] args) {
		String propFilename = "application";
		ResourceBundle resource = ResourceBundle.getBundle(propFilename);
		DataSource dataSource = new DataSource(resource);
		Validator<Account> validator = new AccountValidator();
		CrudRepository<Account> accountRepository = new AccountRepository(dataSource);
		CrudRepository<Company> companyRepository = new CompanyRepository(dataSource);
		CrudRepository<InspectionOrganization> inspectionOrganizationRepository = new InspectionOrganizationRepository(dataSource);
		CrudRepository<TableType> tableTypesRepository = new TableTypeRepository(dataSource);
		AccountService accountService = new AccountServiceImpl(accountRepository, validator);
		CompanyService companyService = new CompanyService(companyRepository);
		InspectionOrganizationService inspectionOrganizationService =
				new InspectionOrganizationService(inspectionOrganizationRepository);
		TableTypeService tableTypeService = new TableTypeService(tableTypesRepository);
		MainFrame mainFrame = new MainFrame(resource, accountService, companyService, tableTypeService, inspectionOrganizationService);
		mainFrame.run();
	}
}
