package ru.kmikhails.accountcare.validator.impl;

import ru.kmikhails.accountcare.entity.Account;
import ru.kmikhails.accountcare.validator.Validator;

import static ru.kmikhails.accountcare.util.StringUtils.isEmpty;

public class AccountValidator implements Validator<Account> {

	@Override
	public void validate(Account account) {
//		if (isEmpty(account.getAccountNumber())) {
//			throw new AccountException("Не введен номер счёта");
//		}
//		if (isEmpty(account.getAccountDate().toString())) {
//			throw new AccountException("Не введена дата счёта");
//		}
//		if (isEmpty(account.getInspectionOrganization())) {
//			throw new AccountException("Не введена проверяющая организация");
//		}
//		if (isEmpty(account.getCompany())) {
//			throw new AccountException("Не введено предприятие");
//		}
	}
}
