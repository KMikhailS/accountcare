package ru.kmikhails.accountcare.validator.impl;

import ru.kmikhails.accountcare.entity.Account;
import ru.kmikhails.accountcare.exception.AccountException;
import ru.kmikhails.accountcare.validator.Validator;

import static ru.kmikhails.accountcare.util.StringUtils.isEmpty;

public class AccountValidator implements Validator<Account> {

	@Override
	public void validate(Account account) {
		if (isEmpty(account.getAccountNumber())) {
			throw new AccountException("Не введен номер счёта");
		}
		if (account.getAccountDate() == null) {
			throw new AccountException("Не введена дата счёта");
		}
		if (isEmpty(account.getInspectionOrganization().getInspectionOrganization())) {
			throw new AccountException("Не введена проверяющая организация");
		}
		if (isEmpty(account.getCompany().getCompany())) {
			throw new AccountException("Не введено предприятие");
		}
	}
}
