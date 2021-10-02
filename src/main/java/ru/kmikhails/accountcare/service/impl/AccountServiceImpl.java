package ru.kmikhails.accountcare.service.impl;

import ru.kmikhails.accountcare.entity.*;
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
	public void save(Account account) {
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
		return new ArrayList<>();
	}

	@Override
	public void changeStatus(Long id, AccountStatus status) {

	}

	@Override
	public void update(Account account) {
		accountRepository.update(account);
	}

	@Override
	public Account findByName(String name) {
		return null;
	}

	@Override
	public List<Account> findAllByTableType(String tableType) {
		return accountRepository.findAllByTableType(tableType);
	}

	@Override
	public Account findByAccountNumberAndDate(String accountNumber, LocalDate date) {
		return accountRepository.findByAccountNumberAndDate(accountNumber, date).orElse(null);
	}

	@Override
	public Account buildAccount(Long id, String accountNumber, LocalDate accountDate, Long companyId, String company,
								 Long inspectionOrganizationId, String inspectionOrganization, String serviceType,
								 Long tableTypeId, String tableType, String amount, String amountWithNDS,
								 String instruments, String invoiceNumber, LocalDate invoiceDate,
								 LocalDate deliveryToAccountingDate, String notes, String accountFile,
								 Boolean isOur, String invoiceFile, Integer rowColor) {
		return Account.builder()
				.withId(id)
				.withAccountNumber(accountNumber)
				.withAccountDate(accountDate)
				.withCompany(Company.builder()
						.withId(companyId)
						.withCompany(company)
						.build())
				.withInspectionOrganization(InspectionOrganization.builder()
						.withId(inspectionOrganizationId)
						.withInspectionOrganization(inspectionOrganization)
						.build())
				.withServiceType(serviceType)
				.withTableType(TableType.builder()
						.withId(tableTypeId)
						.withTableType(tableType)
						.build())
				.withAmount(amount)
				.withAmountWithDNS(amountWithNDS)
				.withInstruments(instruments)
				.withInvoiceNumber(invoiceNumber)
				.withInvoiceDate(invoiceDate)
				.withDeliveryToAccountingDate(deliveryToAccountingDate)
				.withNotes(notes)
				.withAccountFile(accountFile)
				.withIsOur(isOur)
				.withInvoiceFile(invoiceFile)
				.withRowColor(rowColor)
				.build();
	}
}
