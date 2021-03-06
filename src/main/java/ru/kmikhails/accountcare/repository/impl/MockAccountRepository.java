//package ru.kmikhails.accountcare.repository.impl;
//
//import ru.kmikhails.accountcare.entity.Account;
//import ru.kmikhails.accountcare.entity.AccountStatus;
//import ru.kmikhails.accountcare.repository.CrudRepository;
//
//import java.time.LocalDate;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//public class MockAccountRepository implements CrudRepository<Account> {
//
//	@Override
//	public void save(Account entity) {
//	}
//
//	@Override
//	public void deleteById(Long id) {
//
//	}
//
//	@Override
//	public Optional<Account> findById(Long id) {
//		return Optional.empty();
//	}
//
//	@Override
//	public List<Account> findAll() {
//		List<Account> accounts = Arrays.asList(
//				Account.builder()
//						.withAccountNumber("1111")
//						.withAccountDate(LocalDate.parse("2020-12-02"))
//						.withCompany("CSM")
////						.withAmount(1500.00f)
//						.withInvoiceNumber("0101001")
//						.withInvoiceDate(LocalDate.parse("2020-12-03"))
//						.withDeliveryToAccountingDate(LocalDate.parse("2020-12-04"))
//						.build(),
//				Account.builder()
//						.withAccountNumber("1111")
//						.withAccountDate(LocalDate.parse("2020-12-02"))
//						.withCompany("UNIIM")
////						.withAmount(4000.00f)
//						.withInvoiceNumber("020202")
//						.withInvoiceDate(LocalDate.parse("2020-12-03"))
//						.withDeliveryToAccountingDate(LocalDate.parse("2020-12-04"))
//						.build(),
//				Account.builder()
//						.withAccountNumber("22222")
//						.withAccountDate(LocalDate.parse("2020-12-02"))
//						.withCompany("UNIIM")
////						.withAmount(4000.00f)
//						.withInvoiceNumber("020202")
//						.withInvoiceDate(LocalDate.parse("2020-12-03"))
//						.withDeliveryToAccountingDate(LocalDate.parse("2020-12-04"))
//						.build(),
//				Account.builder()
//						.withAccountNumber("333333")
//						.withAccountDate(LocalDate.parse("2020-12-02"))
//						.withCompany("URAL-TEST")
////						.withAmount(6000.00f)
//						.withInvoiceNumber("030303")
//						.withInvoiceDate(LocalDate.parse("2020-12-03"))
//						.withDeliveryToAccountingDate(LocalDate.parse("2020-12-04"))
//						.build(),
//				Account.builder()
//						.withAccountNumber("44444")
//						.withAccountDate(LocalDate.parse("2020-12-02"))
//						.withCompany("ЦСМ")
////						.withAmount(2500.00f)
//						.withInvoiceNumber("040404")
//						.withInvoiceDate(LocalDate.parse("2020-12-03"))
//						.withDeliveryToAccountingDate(LocalDate.parse("2020-12-04"))
//						.build()
//		);
//		accounts.forEach(account -> account.setStatus("NEW"));
//		return accounts;
//	}
//
//	@Override
//	public void update(Long id) {
//
//	}
//
//	public Optional<Account> findByAccountNumberAndCompany(String accountNumber, String company) {
//		return Optional.empty();
//	}
//}
