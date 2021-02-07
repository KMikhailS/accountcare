package ru.kmikhails.accountcare.repository;

import ru.kmikhails.accountcare.entity.Account;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<E> {

	void save(E entity);

	void deleteById(Long id);

	Optional<E> findById(Long id);

	List<E> findAll();

	void update(Long id);

	Optional<Account> findByAccountNumberAndCompany(String accountNumber, String company);
}
