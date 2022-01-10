package ru.kmikhails.accountcare.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.kmikhails.accountcare.entity.*;
import ru.kmikhails.accountcare.exception.AccountException;
import ru.kmikhails.accountcare.repository.CrudRepository;
import ru.kmikhails.accountcare.service.AccountService;
import ru.kmikhails.accountcare.validator.Validator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AccountServiceImpl implements AccountService {
	private static final Logger LOG = LogManager.getLogger(AccountServiceImpl.class);

	private final CrudRepository<Account> accountRepository;
	private final Validator<Account> accountValidator;

	public AccountServiceImpl(CrudRepository<Account> accountRepository, Validator<Account> accountValidator) {
		this.accountRepository = accountRepository;
		this.accountValidator = accountValidator;
	}

	@Override
	public void save(Account account) {
		accountValidator.validate(account);
		account = accountRepository.save(account).orElseThrow(() -> new AccountException("Ошибка сохранения счёта"));
		LOG.info(String.format("Сохранён счёт с номером [%s], датой [%s], id [%d]",
				account.getAccountNumber(), account.getAccountDate(), account.getId()));
	}

	@Override
	public void deleteById(Long id) {
		accountRepository.deleteById(id);
		LOG.info(String.format("Удалён счёт с id [%d]", id));
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
		LOG.info(String.format("Изменён счёт с номером [%s], датой [%s], id [%d]",
				account.getAccountNumber(), account.getAccountDate(), account.getId()));
	}

	@Override
	public Account findByName(String name) {
		return null;
	}

	@Override
	public List<Account> findAllByTableType(String tableType, String year) {
		return accountRepository.findAllByTableType(tableType, year);
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
								 Boolean isOur, Boolean isPrepayment, String invoiceFile, Integer rowColor) {
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
				.withIsPrepayment(isPrepayment)
				.withInvoiceFile(invoiceFile)
				.withRowColor(rowColor)
				.build();
	}
}
