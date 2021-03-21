package ru.kmikhails.accountcare.repository.impl;

import org.h2.tools.RunScript;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.kmikhails.accountcare.entity.Account;
import ru.kmikhails.accountcare.entity.Company;
import ru.kmikhails.accountcare.entity.InspectionOrganization;
import ru.kmikhails.accountcare.entity.TableType;
import ru.kmikhails.accountcare.repository.DataSource;

import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountRepositoryTest {
//    private static final String DB_PROPERTIES = "application-test";
//    private static Account ACCOUNT;
//
//    private final DataSource dataSource = new DataSource(DB_PROPERTIES);
//    private final AccountRepository accountRepository = new AccountRepository(dataSource);
//
//    @BeforeEach
//    void initDB() {
//        try (Connection connection = dataSource.getConnection()) {
//            File schemaScript = new File(getClass().getResource("/schema-test.sql").getFile());
//            File dataScript = new File(getClass().getResource("/data-test.sql").getFile());
//            RunScript.execute(connection, new FileReader(schemaScript));
//            RunScript.execute(connection, new FileReader(dataScript));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        ACCOUNT = Account.builder()
//                .withAccountNumber("4444")
//                .withAccountDate(LocalDate.parse("2021-02-12"))
//                .withCompany(Company.builder()
//                .withId(1)
//                .withCompany("skb")
//                .build())
//                .withServiceType("service")
//                .withInstruments("instruments")
//                .withAmount("100")
//                .withAmountWithDNS("120")
//                .withInvoiceNumber("4040")
//                .withInvoiceDate(LocalDate.parse("2021-02-13"))
//                .withDeliveryToAccountingDate(LocalDate.parse("2021-02-14"))
//                .withInspectionOrganization(InspectionOrganization.builder()
//                .withId(1)
//                .withInspectionOrganization("csm")
//                .build())
//                .withNotes("notes")
//                .withTableType(TableType.builder()
//                .withId(1)
//                .withTableType("csm")
//                .build())
//                .build();
//    }


    @Test
    void saveShouldReturnAccount() {
//        Account actualAccount = accountRepository.save(ACCOUNT).orElseThrow(() -> new ArithmeticException("Could not save account"));
//        Account expectedAccount = Account.builder()
//                .withId(4L)
//                .withStatus("NEW")
//                .withAccountNumber("4444")
//                .withAccountDate(LocalDate.parse("2021-02-12"))
//                .withCompany(Company.builder()
//                        .withId(1)
//                        .withCompany("skb")
//                        .build())
//                .withServiceType("service")
//                .withInstruments("instruments")
//                .withAmount("100")
//                .withAmountWithDNS("120")
//                .withInvoiceNumber("4040")
//                .withInvoiceDate(LocalDate.parse("2021-02-13"))
//                .withDeliveryToAccountingDate(LocalDate.parse("2021-02-14"))
//                .withInspectionOrganization(InspectionOrganization.builder()
//                        .withId(1)
//                        .withInspectionOrganization("csm")
//                        .build())
//                .withNotes("notes")
//                .withTableType(TableType.builder()
//                        .withId(1)
//                        .withTableType("csm")
//                        .build())
//                .build();
////        assertEquals(expectedAccount, actualAccount);
    }
}