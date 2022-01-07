package ru.kmikhails.accountcare.service;

import ru.kmikhails.accountcare.entity.Account;
import ru.kmikhails.accountcare.entity.AccountStatus;

import java.time.LocalDate;
import java.util.List;

public interface AccountService extends Service<Account> {

	void changeStatus(Long id, AccountStatus status);

	List<Account> findAllByTableType(String tableType, String year);

	Account findByAccountNumberAndDate(String accountNumber, LocalDate date);

	Account buildAccount(Long id, String accountNumber, LocalDate accountDate, Long companyId, String company,
						 Long inspectionOrganizationId, String inspectionOrganization, String serviceType,
						 Long tableTypeId, String tableType, String amount, String amountWithNDS,
						 String instruments, String invoiceNumber, LocalDate invoiceDate,
						 LocalDate deliveryToAccountingDate, String notes, String accountFile,
						 Boolean isOur, String invoiceFile, Integer rowColor);
}
