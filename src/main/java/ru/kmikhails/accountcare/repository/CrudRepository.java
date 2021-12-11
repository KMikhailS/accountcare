package ru.kmikhails.accountcare.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CrudRepository<E> {

	Optional<E> save(E entity);

	void deleteById(Long id);

	Optional<E> findById(Long id);

	List<E> findAll();

	List<E> findAllByTableType(String tableType, int year);

	void update(E e);

	Optional<E> findByName(String name);

	Optional<E> findByAccountNumberAndDate(String accountNumber, LocalDate date);
}
